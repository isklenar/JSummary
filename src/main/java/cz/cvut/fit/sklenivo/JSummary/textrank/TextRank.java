package cz.cvut.fit.sklenivo.JSummary.textrank;


import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.Summarizer;
import cz.cvut.fit.sklenivo.JSummary.testing.TestableSummarizer;
import cz.cvut.fit.sklenivo.JSummary.util.SentenceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Class for performing TextRank summarization
 */
public class TextRank implements Summarizer, TestableSummarizer {
    private double ratio;
    private SentenceComparator sentenceComparator;

    final private double PROBABILITY_D = 0.85;
    final private double SCORE_CHANGE_THRESHOLD = 0.0001;


    /**
     * Constructs a new instance of TextRank
     */
    public TextRank() {
    }

    /**
     * Performs the summary
     *
     * @param input String with input text that is to be summarized
     * @return summarized text
     */
    @Override
    public String summarize(String input, SummarizationSettings settings){
        return summarize(SentenceUtils.splitToSentences(input), settings);
    }

    @Override
    public String summarize(List<String> input, SummarizationSettings settings) {
        this.ratio = settings.getRatio();

        sentenceComparator = new SentenceComparator(settings);

        List<TextRankSentence> inputText = createSentences(input);

        Graph graph = buildGraph(inputText);

        prepareScores(inputText, graph);

        List<TextRankSentence> summary = createSummary(inputText);

        StringBuilder builder = new StringBuilder();
        for (TextRankSentence textRankSentence : summary){
            builder.append(textRankSentence);
        }

        return builder.toString();
    }

    @Override
    public List<String> summarizeToSentences(List<String> input, SummarizationSettings settings) {
        this.ratio = settings.getRatio();

        sentenceComparator = new SentenceComparator(settings);

        List<TextRankSentence> inputText = createSentences(input);

        Graph graph = buildGraph(inputText);

        prepareScores(inputText, graph);

        List<TextRankSentence> summary = createSummary(inputText);

        List<String> ret = new ArrayList<>();
        for (TextRankSentence textRankSentence : summary){
            ret.add(textRankSentence.getSentence());
        }

        return ret;
    }

    /**
     * Creates the summary, picks the top sentences
     * @return List of sentences, containing n sentences, where n determined from percentage property
     */
    private List<TextRankSentence> createSummary(List<TextRankSentence> textRankSentences) {
        ArrayList<TextRankSentence> originalTextRankSentences = new ArrayList<>();
        originalTextRankSentences.addAll(textRankSentences);

        //sort them based on their score
        Collections.sort(textRankSentences, new SentenceScoreComparator());

        ArrayList<TextRankSentence> summary = new ArrayList<>();

        //calculate how many sentences to output
        int count = (int)(textRankSentences.size() * ratio);

        //account for rounding error
        if (count == 0){
            count = 1;
        }

        for (int i = 0; i < count; i++) {
            summary.add(textRankSentences.get(i));
        }

        //this will keep all elements in summary, but they will be ordered like they were in original corpus
        originalTextRankSentences.retainAll(new LinkedHashSet<Object>(summary));

        return new ArrayList<>(originalTextRankSentences);
    }

    /**
     * This will calculate scores for all sentences.
     * Will terminate early if scores converge fast.
     */
    private void prepareScores(List<TextRankSentence> textRankSentences, Graph graph) {
        List<Double> previousScores = new ArrayList<>();

        //max 30 iterations, that is enough for majority of texts
        //the longer the text is, the faster it converges
        for (int i = 0; i < 3000; i++) {
            previousScores.clear();
            for (TextRankSentence textRankSentence : textRankSentences) {
                previousScores.add(textRankSentence.getScore());
            }

            calculateScores(textRankSentences, graph);

            boolean breakLoop = true;
            for (int j = 0; j < textRankSentences.size(); j++) {

                //check if every sentence improved by at least SCORE_CHANGE_THRESHOLD
                if(Math.abs(textRankSentences.get(j).getScore() - previousScores.get(j)) > SCORE_CHANGE_THRESHOLD){
                    breakLoop = false;
                }
            }

            //we converged, break
            if (breakLoop){
                break;
            }
        }
    }

    /**
     * Calculates scores once for every sentence. Uses formula found in TextRank: Bringing Order into Texts by
     * Rada Mihalcea and Paul Tarau
     */
    private void calculateScores(List<TextRankSentence> textRankSentences, Graph graph) {

        for (TextRankSentence textRankSentence : textRankSentences){
            ArrayList<TextRankSentence> in = graph.getNeighbour(textRankSentence);


            double inSum = 0;

            for(TextRankSentence inTextRankSentence : in){
                double outSum = 0;
                ArrayList<TextRankSentence> out = graph.getNeighbour(inTextRankSentence);
                for (TextRankSentence outTextRankSentence : out){
                    Double addition = graph.get(new Edge(inTextRankSentence, outTextRankSentence));

                    //this happens if there isn't edge between inSentence and outSentence
                    //as if there was edge, but had weight 0
                    if (addition == null) {
                        continue;
                    }

                    outSum += addition;
                }

                double tmp = graph.get(new Edge(inTextRankSentence, textRankSentence)) / outSum;
                tmp *= inTextRankSentence.getScore();

                inSum += tmp;
            }

            double score = (1 - PROBABILITY_D) + PROBABILITY_D*inSum;

            textRankSentence.setScore(score);
        }

    }

    /**
     * Creates a directed weighted graph from senteces.
     */
    private Graph buildGraph(List<TextRankSentence> textRankSentences) {
        Graph graph = new Graph();
        for (int i = 0; i < textRankSentences.size(); i++) {
            for (int j = 0; j < textRankSentences.size(); j++) {

                //we don't need to compare sentence with itself
                if (i == j) {
                    continue;
                }

                double distance = sentenceComparator.TextRankSentenceSimilarity(
                        textRankSentences.get(i), textRankSentences.get(j));

                // If distance == 0, then sentences don't have anything in common. No point adding an edge
                if (distance != 0) {
                    graph.insert(new Edge(textRankSentences.get(i), textRankSentences.get(j)), distance);
                }
            }
        }

        return graph;
    }

    /**
     * Splits input into sentences, stores them in a ArrayList to be used for later.
     */
    private List<TextRankSentence> createSentences(List<String> input) {
        List<TextRankSentence> ret = new ArrayList<>();

        for(String sentence : input){
            ret.add(new TextRankSentence(sentence));
        }

        return ret;
    }

    @Override
    public String toString() {
        return "TextRank";
    }
}
