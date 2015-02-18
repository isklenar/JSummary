package cz.cvut.fit.sklenivo.JSummary;

/**
 * Created by ivo on 20.10.14.
 */
public interface Summarizer {
    public String summarize(String input, int percentage, boolean stemming,
                            boolean wordNet, boolean stopWords, boolean useNLP,
                            String language);
}
