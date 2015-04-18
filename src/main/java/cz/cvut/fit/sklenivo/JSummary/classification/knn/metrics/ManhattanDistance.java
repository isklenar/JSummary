package cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics;

import cz.cvut.fit.sklenivo.JSummary.classification.ClassificationSentence;

/**
 * Created by ivo on 17.4.2015.
 */
public class ManhattanDistance implements KNNMetric {

    @Override
    public double evaluate(ClassificationSentence s1, ClassificationSentence s2) {
        double ret = 0;

        for (int i = 0; i < s1.getFeatures().length; i++){
            ret += Math.abs(s1.getFeatures()[i] - s2.getFeatures()[i]);
        }

        return ret;
    }

    @Override
    public String toString() {
        return "ManhattanDistance";
    }
}
