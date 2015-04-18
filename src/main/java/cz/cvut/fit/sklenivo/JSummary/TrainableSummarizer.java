package cz.cvut.fit.sklenivo.JSummary;

/**
 * Created by ivo on 18.3.2015.
 */
public interface TrainableSummarizer extends Summarizer {
    public void train(SummarizationSettings settings);
    public TrainableSummarizer addTrainingData(String trainingText, String summary);
}
