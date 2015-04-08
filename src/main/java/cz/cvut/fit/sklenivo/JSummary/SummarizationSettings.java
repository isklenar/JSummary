package cz.cvut.fit.sklenivo.JSummary;

/**
 * Created by ivo on 8.4.2015.
 */
public class SummarizationSettings {
    private double ratio;
    private boolean stemming;
    private boolean wordNet;
    private boolean stopWords;
    private boolean useNLP;
    private String language;

    SummarizationSettings(double ratio, boolean stemming, boolean wordNet, boolean stopWords, boolean useNLP, String language) {
        this.ratio = ratio;
        this.stemming = stemming;
        this.wordNet = wordNet;
        this.stopWords = stopWords;
        this.useNLP = useNLP;
        this.language = language;
    }

    public double getRatio() {
        return ratio;
    }

    public boolean isStemming() {
        return stemming;
    }

    public boolean isWordNet() {
        return wordNet;
    }

    public boolean isStopWords() {
        return stopWords;
    }

    public boolean isUseNLP() {
        return useNLP;
    }

    public String getLanguage() {
        return language;
    }
}
