package cz.cvut.fit.sklenivo.JSummary;

/**
 * Created by ivo on 18.3.2015.
 */
public interface TrainableSummarizer extends Summarizer {
    public void train(String trainingText, String summary, SummarizationSettings settings);
}
