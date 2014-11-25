package cz.fit.cvut.sklenivo.JSummary.bayes;

/**
 * Created by ivo on 20.10.14.
 */
public class BayesSentence {
    private boolean sentenceLengthFeature;
    private String text;
    private int paragraphNumber;
    private int sentenceNumber;

    public BayesSentence(String text, int paragraphNumber, int sentenceNumber) {
        this.text = text;
        this.paragraphNumber = paragraphNumber;
        this.sentenceNumber = sentenceNumber;
    }

    public String getText() {
        return text;
    }

    public boolean isSentenceLengthFeature() {
        return sentenceLengthFeature;
    }
}
