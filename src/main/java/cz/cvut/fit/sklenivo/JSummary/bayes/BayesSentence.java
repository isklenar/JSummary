package cz.cvut.fit.sklenivo.JSummary.bayes;

/**
 * Created by ivo on 20.10.14.
 */
public class BayesSentence implements Comparable<BayesSentence> {
    private String text;
    private int paragraphFeature;
    private int lengthFeature;

    private double P_IS_IN_SUMMARY;

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

    public double getP_IS_IN_SUMMARY() {
        return P_IS_IN_SUMMARY;
    }

    public void setP_IS_IN_SUMMARY(double p_IS_IN_SUMMARY) {
        P_IS_IN_SUMMARY = p_IS_IN_SUMMARY;
    }

    @Override
    public int compareTo(BayesSentence o) {
        if (this.P_IS_IN_SUMMARY < o.P_IS_IN_SUMMARY){
            return -1;
        }

        if (this.P_IS_IN_SUMMARY > o.P_IS_IN_SUMMARY){
            return 1;
        }

        return 0;
    }
}
