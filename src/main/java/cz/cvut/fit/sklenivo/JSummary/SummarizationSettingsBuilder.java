package cz.cvut.fit.sklenivo.JSummary;

/**
 * Created by ivo on 8.4.2015.
 */
public class SummarizationSettingsBuilder {
    private double ratio = 0.5;
    private boolean stemming = false;
    private boolean wordNet = false;
    private boolean stopWords = false;
    private boolean useNLP = false;
    private String language;

    public SummarizationSettingsBuilder setRatio(double ratio) {
        this.ratio = ratio;
        return this;
    }

    public SummarizationSettingsBuilder setStemming(boolean stemming) {
        this.stemming = stemming;
        return this;
    }

    public SummarizationSettingsBuilder setWordNet(boolean wordNet) {
        this.wordNet = wordNet;
        return this;
    }

    public SummarizationSettingsBuilder setStopWords(boolean stopWords) {
        this.stopWords = stopWords;
        return this;
    }

    public SummarizationSettingsBuilder setUseNLP(boolean useNLP) {
        this.useNLP = useNLP;
        return this;
    }

    public SummarizationSettingsBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }

    public SummarizationSettings build(){
        SummarizationSettings settings = new SummarizationSettings(ratio, stemming, wordNet, stopWords, useNLP, language.toLowerCase());
        ratio = 0.5;
        stemming = wordNet = stopWords = useNLP = false;
        language = null;

        return settings;
    }
}
