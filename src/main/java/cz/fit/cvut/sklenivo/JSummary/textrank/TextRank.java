package cz.fit.cvut.sklenivo.JSummary.textrank;


import cz.fit.cvut.sklenivo.JSummary.Summarizer;
import cz.fit.cvut.sklenivo.JSummary.util.SentenceComparator;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * Class for performing TextRank summarization
 */
public class TextRank implements Summarizer {
    private String input;
    private Graph graph;

    private ArrayList<TextRankSentence> textRankSentences;
    private ArrayList<Double> previousScores;

    private int percentage;
    private boolean useNLP;
    private SentenceDetectorME sentenceDetector;
    private SentenceComparator sentenceComparator;

    final private double PROBABILITY_D = 0.85;
    final private double SCORE_CHANGE_THRESHOLD = 0.0001;


    /**
     * Constructs a new instance of TextRank
     */
    public TextRank() {
        try {
            InputStream fis = new FileInputStream("resources/OpenNLP/en-sent.bin"); //for sentence detection
            SentenceModel sentenceModel = new SentenceModel(fis);
            sentenceDetector = new SentenceDetectorME(sentenceModel);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing OpenNLP");
            System.exit(1);
        }

    }

    /**
     * Performs the summary
     *
     * @param input String with input text that is to be summarized
     * @param percentage percentage of the summary (e.g. value of 30 means 30% ratio of summarization)
     * @param useNLP flag to determined if the summarization algorithm should use some features from NLP
     *               might make the summarization faster, but also less accurate
     * @return summarized text
     */
    @Override
    public String summarize(String input, int percentage, boolean stemming,
                            boolean wordNet, boolean stopWords, boolean useNLP,
                            String language){
        this.input = input;
        this.percentage = percentage;
        this.useNLP = useNLP;

        sentenceComparator = new SentenceComparator(stemming, stopWords, wordNet, useNLP, language);
        previousScores = new ArrayList<>();
        graph = new Graph();

        splitSentences();
        buildGraph();
        prepareScores();

        ArrayList<TextRankSentence> summary = createSummary();

        StringBuilder builder = new StringBuilder();
        for (TextRankSentence textRankSentence : summary){
            builder.append(textRankSentence);
            builder.append(" ");
        }

        return builder.toString();
    }

    /**
     * Creates the summary, picks the top sentences
     * @return ArrayList of sentences, containing n sentences, where n determined from percentage property
     */
    private ArrayList<TextRankSentence> createSummary() {
        ArrayList<TextRankSentence> originalTextRankSentences = new ArrayList<>();
        originalTextRankSentences.addAll(textRankSentences);

        //sort them based on their score
        Collections.sort(textRankSentences, new SentenceScoreComparator());

        ArrayList<TextRankSentence> summary = new ArrayList<>();

        //calculate how many sentences to output
        int count = (int)(textRankSentences.size() * (percentage/100.0));

        //account for rounding error
        if (count == 0){
            count = 1;
        }

        System.out.printf("Summarized to %d sentences; ratio %f%n", count, (double)count/ textRankSentences.size());

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
    private void prepareScores() {
        //max 30 iterations, that is enough for majority of texts
        //the longer the text is, the faster it converges
        for (int i = 0; i < 30; i++) {
            previousScores.clear();
            for (TextRankSentence textRankSentence : textRankSentences) {
                previousScores.add(textRankSentence.getScore());
            }

            calculateScores();

            boolean breakLoop = true;
            for (int j = 0; j < textRankSentences.size(); j++) {

                //check if every sentence improved by at least SCORE_CHANGE_THRESHOLD
                if(Math.abs(textRankSentences.get(j).getScore() - previousScores.get(j)) > SCORE_CHANGE_THRESHOLD){
                    breakLoop = false;
                }
            }

            //we converged, break
            if (breakLoop){
                System.out.println(i + " iterations to converge");
                break;
            }
        }
    }

    /**
     * Calculates scores once for every sentence. Uses formula found in TextRank: Bringing Order into Texts by
     * Rada Mihalcea and Paul Tarau
     */
    private void calculateScores() {
        for (TextRankSentence textRankSentence : textRankSentences){
            ArrayList<TextRankSentence> in = graph.getIncoming(textRankSentence);
            ArrayList<TextRankSentence> out = graph.getOutgoing(textRankSentence);

            double inSum = 0;

            for(TextRankSentence inTextRankSentence : in){
                double outSum = 0;
                for (TextRankSentence outTextRankSentence : out){
                    Double addition = graph.get(new DirectedEdge(inTextRankSentence, outTextRankSentence));

                    //this happens if there isn't edge between inSentence and outSentence
                    //as if there was edge, but had weight 0
                    if (addition == null) {
                        continue;
                    }

                    outSum += addition;
                }

                double tmp = graph.get(new DirectedEdge(inTextRankSentence, textRankSentence)) / outSum;
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
    private void buildGraph() {
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
                    graph.insert(new DirectedEdge(textRankSentences.get(i), textRankSentences.get(j)), distance);
                }
            }
        }
    }

    /**
     * Splits input into sentences, stores them in a ArrayList to be used for later.
     */
    private void splitSentences() {
        String [] sentencesArr;
        if (useNLP) {
            sentencesArr = sentenceDetector.sentDetect(input);
        } else {
            //split naively
            sentencesArr = input.split(".");
        }

        textRankSentences = new ArrayList<>();

        for (String sentence : sentencesArr){
            textRankSentences.add(new TextRankSentence(sentence));
        }
    }
}
