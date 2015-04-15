package cz.cvut.fit.sklenivo.JSummary;

import java.util.List;

/**
 * Created by ivo on 20.10.14.
 */
public interface Summarizer {
    public String summarize(String input, SummarizationSettings settings);

    public String summarize(List<String> input, SummarizationSettings settings);
}
