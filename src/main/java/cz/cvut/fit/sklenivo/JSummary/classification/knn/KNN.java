package cz.cvut.fit.sklenivo.JSummary.classification.knn;

import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.TrainableSummarizer;
import cz.cvut.fit.sklenivo.JSummary.classification.*;
import cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics.CosineDistance;
import cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics.KNNMetric;
import cz.cvut.fit.sklenivo.JSummary.testing.TestableSummarizer;

import java.util.*;

/**
 * Created by ivo on 18.3.2015.
 */
public class KNN implements TrainableSummarizer, TestableSummarizer {

    private int k = 3;

    private List<ClassificationSentence> model;

    private KNNMetric metric;

    private List<TrainingData> trainingData = new ArrayList<>();
    private List<MeanAndVariance> normalizationData;

    public KNN(int k, KNNMetric metric) {
        this.k = k;
        model = new ArrayList<>();
        this.metric = metric;
    }

    public KNN(){
        model = new ArrayList<>();
    }

    @Override
    public String summarize(List<String> input, SummarizationSettings settings){
        StringBuilder builder = new StringBuilder();
        for(String sentence : input){
            builder.append(sentence).append(" ");
        }

        return summarize(builder.toString(), settings);
    }

    @Override
    public String summarize(String input, SummarizationSettings settings) {
        List<ClassificationSentence> inputSentences = ClassificationPreprocessor.preProcess(input, settings);
        classify(inputSentences, settings);

        StringBuilder builder = new StringBuilder();
        for (ClassificationSentence sentence : inputSentences){
            if (sentence.isInSummary()){
                builder.append(sentence.getText());
            }
        }
        return builder.toString();

    }

    @Override
    public void train(SummarizationSettings settings) {
        List<ClassificationSentence> sentences = ClassificationPreprocessor.preProcess(trainingData, settings);

        model.addAll(sentences);
        if (settings.isNormalization()){
            this.normalizationData = ClassificationUtils.normalize(model);
        }
    }

    @Override
    public TrainableSummarizer addTrainingData(String trainingText, String summary){
        trainingData.add(new TrainingData(trainingText, summary));

        return this;
    }


    private void classify(List<ClassificationSentence> input, SummarizationSettings settings){
        if (settings.isNormalization()){
            ClassificationUtils.normalize(input, normalizationData);
        }

        for (ClassificationSentence sentence : input) {
            List<KNNTuple> distances = new ArrayList<>();

            for (ClassificationSentence modelSentence : model) {
                double d = metric.evaluate(sentence, modelSentence);
                distances.add(new KNNTuple(modelSentence, d));
            }

            classifyByNeighbours(sentence, distances);
        }
    }

    private void classifyByNeighbours(ClassificationSentence sentence, List<KNNTuple> distances) {
        if (metric instanceof CosineDistance){
            Collections.sort(distances, new Comparator<KNNTuple>() {
                @Override
                public int compare(KNNTuple o1, KNNTuple o2) {
                    if (o2.getDistance() > o1.getDistance()){
                        return 1;
                    }
                    if (o2.getDistance() < o1.getDistance()){
                        return -1;
                    }

                    return 0;
                }
            });
        } else {
            Collections.sort(distances);
        }
        int inSummaryCount = 0;
        for (int i = 0; i < k; i++){
            if (distances.get(i).getSentence().isInSummary()){
                inSummaryCount++;
            }
        }

        if (inSummaryCount > k/2){
            sentence.setInSummary(true);
        }
    }

    @Override
    public List<String> summarizeToSentences(List<String> input, SummarizationSettings settings) {
        StringBuilder builder = new StringBuilder();
        for(String sentence : input){
            builder.append(sentence).append(" ");
        }

        List<ClassificationSentence> inputSentences = ClassificationPreprocessor.preProcess(builder.toString(), settings);
        classify(inputSentences, settings);

        List<String> ret = new ArrayList<>();
        for (ClassificationSentence sentence : inputSentences){
            if (sentence.isInSummary()){
                ret.add(sentence.getText());
            }
        }

        return ret;
    }

    @Override
    public String toString() {
        return k + "-NN";
    }
}
