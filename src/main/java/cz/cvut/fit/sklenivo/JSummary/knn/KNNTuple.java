package cz.cvut.fit.sklenivo.JSummary.knn;

import cz.cvut.fit.sklenivo.JSummary.classification.ClassificationSentence;

/**
 * Created by Ivo on 23.3.2015.
 */
public class KNNTuple implements Comparable<KNNTuple> {
    private ClassificationSentence sentence;
    private Double distance;

    public KNNTuple(ClassificationSentence sentence, Double distance) {
        this.sentence = sentence;
        this.distance = distance;
    }

    public ClassificationSentence getSentence() {
        return sentence;
    }

    public Double getDistance() {
        return distance;
    }

    @Override
    public int compareTo(KNNTuple o) {
        return Double.compare(distance, o.distance);

    }
}
