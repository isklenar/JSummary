package cz.cvut.fit.sklenivo.JSummary.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ivo on 17.4.2015.
 */
public class ClassificationUtils {

    public static double calculateVariance(int i, List<ClassificationSentence> model){
        Map<Double, Integer> featureCount = new HashMap<>();
        int n = 0;
        for(ClassificationSentence sentence : model){
            if (featureCount.containsKey(sentence.getFeatures()[i])){
                featureCount.put(sentence.getFeatures()[i], featureCount.get(sentence.getFeatures()[i]) +1);
            } else {
                featureCount.put(sentence.getFeatures()[i], 1);
            }
            n++;

        }

        return calculateVarianceInner(featureCount, n);
    }

    public static double calculateVariance(int i, boolean summary, List<ClassificationSentence> model) {
        Map<Double, Integer> featureCount = new HashMap<>();
        int n = 0;
        for(ClassificationSentence sentence : model){
            if (sentence.isInSummary() == summary){
                if (featureCount.containsKey(sentence.getFeatures()[i])){
                    featureCount.put(sentence.getFeatures()[i], featureCount.get(sentence.getFeatures()[i]) +1);
                } else {
                    featureCount.put(sentence.getFeatures()[i], 1);
                }
                n++;
            }
        }

        return calculateVarianceInner(featureCount, n);
    }

    private static double calculateVarianceInner(Map<Double, Integer> featureCount, int n){
        double ret1 = 0;
        double ret2 = 0;
        for(Double value : featureCount.keySet()){
            ret1 += (value*value) * (featureCount.get(value)/(double)n);
            ret2 += (value) * (featureCount.get(value)/(double)n);

        }

        return ret1 - (ret2 * ret2);
    }

    public static double calculateMean(int i, List<ClassificationSentence> model){
        Map<Double, Integer> featureCount = new HashMap<>();
        int n = 0;
        for(ClassificationSentence sentence : model){
            if (featureCount.containsKey(sentence.getFeatures()[i])){
                featureCount.put(sentence.getFeatures()[i], featureCount.get(sentence.getFeatures()[i]) +1);
            } else {
                featureCount.put(sentence.getFeatures()[i], 1);
            }
            n++;

        }

        return calculateMeanInner(featureCount, n);
    }

    public static List<MeanAndVariance> normalize(List<ClassificationSentence> sentences) {
        List<MeanAndVariance> ret = new ArrayList<>();

        for(int i = 0; i < sentences.get(0).getFeatures().length; i++){
            if (i == 1 || i == 18){
                ret.add(new MeanAndVariance(-1, -1, i));
                continue;
            }
            double mean = ClassificationUtils.calculateMean(i, sentences);
            double var = ClassificationUtils.calculateVariance(i, sentences);


            ret.add(new MeanAndVariance(mean, var, i));
        }

        normalize(sentences, ret);

        return ret;
    }


    public static void normalize(List<ClassificationSentence> sentences, List<MeanAndVariance> normalizationData) {
        for (ClassificationSentence sentence : sentences){
            normalizeSentence(sentence, normalizationData);
        }
    }

    private static void normalizeSentence(ClassificationSentence sentence, List<MeanAndVariance> meansAndVariances){
        for (int i = 0; i < sentence.getFeatures().length; i++){
            if (i == 1 || i == 18){
                continue;
            }
            double normalized = softMaxNormalize(sentence.getFeatures()[i], meansAndVariances.get(i).getMean(), meansAndVariances.get(i).getVar());
            if (Double.isNaN(normalized)){
                normalized = 0;
            }
            sentence.getFeatures()[i] = normalized;
        }
    }

    private static double softMaxNormalize(double val, double mean, double var){
        double exponent = (val - mean)/Math.sqrt(var);
        double tmp = 1 + Math.exp(-exponent);
        return 1/tmp;
    }

    public static double calculateMean(int i, boolean summary, List<ClassificationSentence> model) {
        Map<Double, Integer> featureCount = new HashMap<>();
        int n = 0;
        for(ClassificationSentence sentence : model){
            if (sentence.isInSummary() == summary){
                if (featureCount.containsKey(sentence.getFeatures()[i])){
                    featureCount.put(sentence.getFeatures()[i], featureCount.get(sentence.getFeatures()[i]) +1);
                } else {
                    featureCount.put(sentence.getFeatures()[i], 1);
                }
                n++;
            }
        }

        return calculateMeanInner(featureCount, n);
    }

    private static double calculateMeanInner(Map<Double, Integer> featureCount, int n){
        double ret = 0;
        for(Double value : featureCount.keySet()){
            ret += value * (featureCount.get(value)/(double)n);
        }

        return ret;
    }

}
