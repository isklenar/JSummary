package cz.cvut.fit.sklenivo.JSummary;

/**
 * Created by ivo on 8.4.2015.
 */
public class SummarizationSettingsBuilder {
    private double ratio = 0.5;
    private boolean stemming = false;
    private boolean stopWords = false;
    private boolean normalization = false;
    private String language;

    public SummarizationSettingsBuilder setRatio(double ratio) {
        this.ratio = ratio;
        return this;
    }

    public SummarizationSettingsBuilder setStemming(boolean stemming) {
        this.stemming = stemming;
        return this;
    }

    public SummarizationSettingsBuilder setStopWords(boolean stopWords) {
        this.stopWords = stopWords;
        return this;
    }

    public SummarizationSettingsBuilder setNormalization(boolean normalization) {
        this.normalization = normalization;
        return this;
    }

    public SummarizationSettingsBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }

    public SummarizationSettings build(){
        SummarizationSettings settings = new SummarizationSettings(ratio, stemming, stopWords, normalization, language);
        ratio = 0.5;
        stemming = normalization = stopWords = false;
        language = null;

        return settings;
    }
}
