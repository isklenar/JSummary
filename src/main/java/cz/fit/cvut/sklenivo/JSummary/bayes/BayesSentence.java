package cz.fit.cvut.sklenivo.JSummary.bayes;

/**
 * Created by ivo on 20.10.14.
 */
public class BayesSentence {
    private String text;
    private int paragraphFeature;
    private int lengthFeature;

    public BayesSentence(String text, int paragraphFeature, int lengthFeature) {
        this.text = text;
        this.paragraphFeature = paragraphFeature;
        this.lengthFeature = lengthFeature;
    }

    public String getText() {
        return text;
    }

    public int getParagraphFeature() {
        return paragraphFeature;
    }

    public int getLengthFeature() {
        return lengthFeature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BayesSentence that = (BayesSentence) o;

        if (!text.equals(that.text)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }
}
