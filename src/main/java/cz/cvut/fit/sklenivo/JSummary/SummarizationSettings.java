package cz.cvut.fit.sklenivo.JSummary;

/**
 * Created by ivo on 8.4.2015.
 */
public class SummarizationSettings {
    private double ratio;
    private boolean stemming;
    private boolean stopWords;
    private boolean normalization;
    private String language;

    public SummarizationSettings(double ratio, boolean stemming, boolean stopWords, boolean normalization, String language) {
        this.ratio = ratio;
        this.stemming = stemming;
        this.stopWords = stopWords;
        this.normalization = normalization;
        this.language = language;
    }

    public double getRatio() {
        return ratio;
    }

    public boolean isStemming() {
        return stemming;
    }

    public boolean isStopWords() {
        return stopWords;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isNormalization() {
        return normalization;
    }

    @Override
    public String toString() {
        return "SummarizationSettings{" +
                "ratio=" + ratio +
                ", stemming=" + stemming +
                ", stopWords=" + stopWords +
                ", normalization=" + normalization +
                ", language='" + language + '\'' +
                '}';
    }
}
