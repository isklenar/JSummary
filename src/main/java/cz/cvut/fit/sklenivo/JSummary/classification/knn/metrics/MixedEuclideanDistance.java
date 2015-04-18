package cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics;

import cz.cvut.fit.sklenivo.JSummary.classification.ClassificationSentence;

/**
 * Created by ivo on 17.4.2015.
 */
public class MixedEuclideanDistance implements KNNMetric {
    @Override
    public double evaluate(ClassificationSentence s1, ClassificationSentence s2) {
        double ret = 0;

        for (int i =0; i < s1.getFeatures().length; i++){
            if (i == 1 || i == 18){
                if (s1.getFeatures()[i] != s2.getFeatures()[i]){
                    ret += 1;
                }
            }

            ret += Math.pow(s1.getFeatures()[i] - s2.getFeatures()[i], 2);
        }

        return Math.sqrt(ret);
    }
}
