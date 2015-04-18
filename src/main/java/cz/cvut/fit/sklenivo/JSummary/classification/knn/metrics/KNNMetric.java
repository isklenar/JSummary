package cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics;

import cz.cvut.fit.sklenivo.JSummary.classification.ClassificationSentence;

/**
 * Created by ivo on 17.4.2015.
 */
public interface KNNMetric {
    public double evaluate(ClassificationSentence s1, ClassificationSentence s2);
}
