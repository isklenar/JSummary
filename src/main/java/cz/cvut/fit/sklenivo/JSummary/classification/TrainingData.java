package cz.cvut.fit.sklenivo.JSummary.classification;

/**
 * Created by ivo on 18.4.2015.
 */
public class TrainingData {
    private String text;
    private String summary;

    public TrainingData(String text, String summary) {
        this.text = text;
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public String getSummary() {
        return summary;
    }
}
