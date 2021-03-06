package cz.cvut.fit.sklenivo.JSummary.classification.bayes;

import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.TrainableSummarizer;
import cz.cvut.fit.sklenivo.JSummary.classification.*;
import cz.cvut.fit.sklenivo.JSummary.testing.TestableSummarizer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ivo on 20.10.14.
 */
public class NaiveBayes implements TrainableSummarizer, TestableSummarizer {
    private List<ClassificationSentence> model = new ArrayList<>();
    private Map<Integer, Double> featuresMeanInSummary = new HashMap<>();
    private Map<Integer, Double> featuresVarianceInSummary = new HashMap<>();
    private Map<Integer, Double> featuresMeanNotInSummary = new HashMap<>();
    private Map<Integer, Double> featuresVarianceNotInSummary = new HashMap<>();

    private Map<Integer, Map<Double, Double>> discreteProbabilityInSummary = new HashMap<>();
    private Map<Integer, Map<Double, Double>> discreteProbabilityNotInSummary = new HashMap<>();

    private List<TrainingData> trainingData = new ArrayList<>();
    private List<MeanAndVariance> normalizationData;
    private double probabilityInSummary;

    public NaiveBayes() {

    }


    @Override
    public void train(SummarizationSettings settings) {
        List<ClassificationSentence> sentences = ClassificationPreprocessor.preProcess(trainingData, settings);
        model.addAll(sentences);

        if (settings.isNormalization()){
             normalizationData = ClassificationUtils.normalize(model);
        }

        int in = 0;
        for(ClassificationSentence sentence : model){
            if(sentence.isInSummary()){
                in++;
            }
        }
        probabilityInSummary = ((double)in)/model.size();

        prepareMeansAndVariance();
    }

    @Override
    public TrainableSummarizer addTrainingData(String trainingText, String summary) {
        trainingData.add(new TrainingData(trainingText, summary));

        return this;
    }

    private void prepareMeansAndVariance() {
        for (int i = 0; i < model.get(0).getFeatures().length; i++){
            if (i == 1 || i == 18){
                Map<Double, Double> inSummary = calculateDiscreteProbability(i, true);
                discreteProbabilityInSummary.put(i, inSummary);

                Map<Double, Double> notInSummary = calculateDiscreteProbability(i, false);
                discreteProbabilityNotInSummary.put(i, notInSummary);
            }

            double mean = ClassificationUtils.calculateMean(i, true, model);
            featuresMeanInSummary.put(i, mean);

            mean = ClassificationUtils.calculateMean(i, false, model);
            featuresMeanNotInSummary.put(i, mean);

            double variance = ClassificationUtils.calculateVariance(i, true, model);
            featuresVarianceInSummary.put(i, variance);

            variance = ClassificationUtils.calculateVariance(i, false, model);
            featuresVarianceNotInSummary.put(i, variance);
        }
    }

    private Map<Double, Double> calculateDiscreteProbability(int i, boolean summary) {
        Map<Double, Integer> frequencies = new HashMap<>();
        int n = 0;
        for (ClassificationSentence sentence : model){
            if (sentence.isInSummary() == summary){
                if (frequencies.containsKey(sentence.getFeatures()[i])){
                    int tmp = frequencies.get(sentence.getFeatures()[i]);
                    frequencies.put(sentence.getFeatures()[i], tmp + 1);
                } else {
                    frequencies.put(sentence.getFeatures()[i], 1);
                }

                n++;
            }
        }

        Map<Double, Double> ret = new HashMap<>();

        for(Double val : frequencies.keySet()){
             ret.put(val, frequencies.get(val)/(double)n);
        }

        return ret;
    }



    private void printBayesTable(List<ClassificationSentence> sentences) {
        System.out.print("\t| ");
        for(int i = 0; i < sentences.get(0).getFeatures().length; i++){
            System.out.print((i < 10 ? i + " " : i) + "\t| ");
        }
        System.out.println("In summary");
        int i = 0;

        for (ClassificationSentence sentence : sentences){
            printSentenceFeatures(sentence, i);
            i++;
        }

        System.out.print("mean| ");

        for (i = 0; i < model.get(0).getFeatures().length; i++){
            System.out.print(featuresMeanInSummary.get(i) + "\t| ");
        }
        System.out.println();
        System.out.print("mean2| ");

        for (i = 0; i < model.get(0).getFeatures().length; i++){
            System.out.print(featuresMeanNotInSummary.get(i) + "\t| ");
        }
        System.out.println();
        System.out.print("var | ");

        for (i = 0; i < model.get(0).getFeatures().length; i++){
            Double var2 = featuresVarianceInSummary.get(i);
            if (var2 == null){
                System.out.print("\t|");
            } else {
                System.out.print(Math.sqrt(var2) + "\t| ");
            }
        }
        System.out.println();
        System.out.print("var2| ");

        for (i = 0; i < model.get(0).getFeatures().length; i++){
            Double var2 = featuresVarianceNotInSummary.get(i);
            if (var2 == null){
                System.out.print("\t|");
            } else {
                System.out.print(Math.sqrt(var2) + "\t| ");
            }
        }
        System.out.println();
        System.out.println("P(Summary=yes) = " + probabilityInSummary);
    }

    private void printSentenceFeatures(ClassificationSentence sentence, int i) {
        System.out.print("s" + i + "\t| ");

        for (Double feature : sentence.getFeatures()){
            System.out.print(feature + "\t| ");
            i++;
        }

        System.out.println(sentence.isInSummary());
    }

    private BigDecimal normalProbabilityDensity(double value, int i, boolean inSummary) {
        if (inSummary) {
            if (featuresVarianceInSummary.get(i) != 0 && featuresMeanInSummary.get(i) != 0) {
                double firstHalf = 1 / (2 * Math.PI * featuresVarianceInSummary.get(i));
                double secondHalf = Math.exp((-Math.pow(value - featuresMeanInSummary.get(i), 2)) / (2 * featuresVarianceInSummary.get(i)));
                //System.out.println(firstHalf + "   " + secondHalf);
                return new BigDecimal(firstHalf).multiply(new BigDecimal(secondHalf));
            }
        } else {
            if (featuresVarianceNotInSummary.get(i) != 0 && featuresMeanNotInSummary.get(i) != 0) {
                double firstHalf = 1 / (2 * Math.PI * featuresVarianceNotInSummary.get(i));
                double secondHalf = Math.exp((-Math.pow(value - featuresMeanNotInSummary.get(i), 2)) / (2 * featuresVarianceNotInSummary.get(i)));
                //System.out.println(firstHalf + "   " + secondHalf);
                return new BigDecimal(firstHalf).multiply(new BigDecimal(secondHalf));
            }
        }

        return BigDecimal.ONE;
    }

    private BigDecimal calculatePosteriory(ClassificationSentence sentence, boolean inSummary) {
        BigDecimal ret = new BigDecimal(inSummary ? probabilityInSummary : 1 - probabilityInSummary);

        for(int i = 0; i < sentence.getFeatures().length; i++){
            if (i == 1 || i == 18){
                if (inSummary){
                    if (discreteProbabilityInSummary.get(i).containsKey(sentence.getFeatures()[i])){
                        double tmp = discreteProbabilityInSummary.get(i).get(sentence.getFeatures()[i]);
                        ret = ret.multiply(new BigDecimal(tmp));
                    }
                } else {
                    if (discreteProbabilityNotInSummary.get(i).containsKey(sentence.getFeatures()[i])){
                        double tmp = discreteProbabilityNotInSummary.get(i).get(sentence.getFeatures()[i]);
                        ret = ret.multiply(new BigDecimal(tmp));
                    }
                }
            }
            else {
                BigDecimal tmp = normalProbabilityDensity(sentence.getFeatures()[i], i, inSummary);
                ret = ret.multiply(tmp);
            }

        }

        return ret;
    }

    @Override
    public String summarize(List<String> input, SummarizationSettings settings){
        StringBuilder builder = new StringBuilder();

        for (String sentence : input){
            builder.append(sentence).append(" ");
        }

        return summarize(builder.toString(), settings);
    }

    @Override
    public String summarize(String input, SummarizationSettings settings) {
        List<ClassificationSentence> sentences = ClassificationPreprocessor.preProcess(input, settings);

        if (settings.isNormalization()){
            ClassificationUtils.normalize(sentences, normalizationData);
        }

        for(ClassificationSentence sentence : sentences){
            BigDecimal inSummary = calculatePosteriory(sentence, true);
            BigDecimal notInSummary = calculatePosteriory(sentence, false);

            if (inSummary.compareTo(notInSummary) > 0){
                sentence.setInSummary(true);
            } else {
                sentence.setInSummary(false);
            }
        }
        StringBuilder builder = new StringBuilder();
        for(ClassificationSentence sentence : sentences){
            if (sentence.isInSummary()){
                builder.append(sentence.getText()).append(" ");
            }
        }
        return builder.toString();
    }

    @Override
    public List<String> summarizeToSentences(List<String> input, SummarizationSettings settings) {
        StringBuilder builder = new StringBuilder();

        for (String sentence : input){
            builder.append(sentence).append(" ");
        }

        List<ClassificationSentence> sentences = ClassificationPreprocessor.preProcess(builder.toString(), settings);

        if (settings.isNormalization()){
            ClassificationUtils.normalize(sentences, normalizationData);
        }

        for(ClassificationSentence sentence : sentences){
            BigDecimal inSummary = calculatePosteriory(sentence, true);
            BigDecimal notInSummary = calculatePosteriory(sentence, false);

            if (inSummary.compareTo(notInSummary) > 0){
                sentence.setInSummary(true);
            } else {
                sentence.setInSummary(false);
            }
        }

        List<String> ret = new ArrayList<>();
        for(ClassificationSentence sentence : sentences){
            if (sentence.isInSummary()){
                ret.add(sentence.getText());
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return "NaiveBayes";
    }

}