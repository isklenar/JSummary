package cz.cvut.fit.sklenivo.JSummary.testing;

import java.util.List;
import java.util.Map;

/**
 * Created by Ivo on 14.4.2015.
 */
public class SummarizableDocument {
    private List<String> sentences;
    private Map<String, List<String>> summaries;

    public SummarizableDocument(List<String> sentences, Map<String, List<String>> summaries) {
        this.sentences = sentences;
        this.summaries = summaries;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public Map<String, List<String>> getSummaries() {
        return summaries;
    }


}
