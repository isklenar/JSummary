package cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics;

import cz.cvut.fit.sklenivo.JSummary.classification.ClassificationSentence;

/**
 * Created by Ivo on 20.4.2015.
 */
public class CosineDistance implements KNNMetric {
    @Override
    public double evaluate(ClassificationSentence s1, ClassificationSentence s2) {
        double topSum = 0;
        double s1SquareSum = 0;
        double s2SquareSum = 0;

        for (int i = 0; i < s1.getFeatures().length; i++){
            topSum += s1.getFeatures()[i] * s2.getFeatures()[i];

            s1SquareSum += Math.pow(s1.getFeatures()[i], 2);
            s2SquareSum += Math.pow(s2.getFeatures()[i], 2);
        }

        return topSum / (Math.sqrt(s1SquareSum) * Math.sqrt(s2SquareSum));
    }

    @Override
    public String toString() {
        return "CosineDistance";
    }
}
