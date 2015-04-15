package cz.cvut.fit.sklenivo.JSummary.testing;

import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;

import java.util.List;

/**
 * Created by ivo on 15.4.2015.
 */
public interface TestableSummarizer {

    public List<String> summarizeToSentences(List<String> input, SummarizationSettings settings);
}
