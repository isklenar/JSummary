package cz.cvut.fit.sklenivo.JSummary.knn;

import cz.cvut.fit.sklenivo.JSummary.TrainableSummarizer;
import cz.cvut.fit.sklenivo.JSummary.classification.ClassificationPreprocessor;
import cz.cvut.fit.sklenivo.JSummary.classification.ClassificationSentence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ivo on 18.3.2015.
 */
public class KNN implements TrainableSummarizer {

    private int k = 3;

    private List<ClassificationSentence> model;

    public KNN(int k) {
        this.k = k;
        model = new ArrayList<>();
    }

    public KNN(){
        model = new ArrayList<>();
    }


    @Override
    public String summarize(String input, double ratio, boolean stemming, boolean wordNet, boolean stopWords, boolean useNLP, String language) {
        List<ClassificationSentence> inputSentences = ClassificationPreprocessor.preProcess(input, null);
        classify(inputSentences);

        StringBuilder builder = new StringBuilder();
        for (ClassificationSentence sentence : inputSentences){
            if (sentence.isInSummary()){
                builder.append(sentence.getText());
            }
        }
        return builder.toString();

    }

    @Override
    public void train(String trainingText, String summary) {
        List<ClassificationSentence> sentences = ClassificationPreprocessor.preProcess(trainingText, summary);

        model.addAll(sentences);
    }

    private void classify(List<ClassificationSentence> input){
        for (ClassificationSentence sentence : input) {
            List<KNNTuple> distances = new ArrayList<>();

            for (ClassificationSentence modelSentence : model) {
                double d = manhattanDistance(sentence, modelSentence);
                distances.add(new KNNTuple(modelSentence, d));
            }

            classifyByNeighbours(sentence, distances);
        }
    }

    private void classifyByNeighbours(ClassificationSentence sentence, List<KNNTuple> distances) {
        Collections.sort(distances);
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

    private double manhattanDistance(ClassificationSentence s1, ClassificationSentence s2){
        double ret = 0;

        for (int i =0; i < s1.getFeatures().length; i++){
            ret += Math.abs(s1.getFeatures()[i] - s2.getFeatures()[i]);
        }

        return ret;
    }
}
