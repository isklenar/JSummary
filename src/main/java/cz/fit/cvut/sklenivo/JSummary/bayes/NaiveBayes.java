package cz.fit.cvut.sklenivo.JSummary.bayes;

import cz.fit.cvut.sklenivo.JSummary.Summarizer;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ivo on 20.10.14.
 */
public class NaiveBayes implements Summarizer {
    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;

    private ArrayList<BayesSentence> sentences;

    private int trainingSentecesTotal = 0;
    private int trainingSummaryTotal = 0;
    private double P_S_IN_SUMMARY = 0.0;

    public NaiveBayes() {
        try {
            InputStream fis = new FileInputStream("resources/OpenNLP/en-sent.bin"); //for sentence detection
            SentenceModel sentenceModel = new SentenceModel(fis);
            sentenceDetector = new SentenceDetectorME(sentenceModel);

            InputStream is = new FileInputStream("resources/OpenNLP/en-token.bin"); //for tokenizer
            TokenizerModel tokenizerModel = new TokenizerModel(is);
            tokenizer = new TokenizerME(tokenizerModel);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing OpenNLP");
            System.exit(1);
        }
    }

    public void train(String trainingText, String summary) {
        String [][] trainingSentences = splitParagraphs(trainingText);
        String [] trainingSummary = sentenceDetector.sentDetect(summary);

        ArrayList<BayesSentence> training = new ArrayList<>();

        for (int i = 0; i < trainingSentences.length; i++) {
            for (int j = 0; j < trainingSentences[i].length; j++) {
                training.add(new BayesSentence(trainingSentences[i][j],i,j));
            }
        }

        trainingSentecesTotal += trainingSentences.length;
        trainingSummaryTotal += trainingSummary.length;
        P_S_IN_SUMMARY = ((double)trainingSentecesTotal)/trainingSummaryTotal;
    }

    private String[][] splitParagraphs(String trainingText) {
        String [] paragraphs = trainingText.split("\n\n+");
        String[][] ret = new String[paragraphs.length][];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = sentenceDetector.sentDetect(paragraphs[i]);
        }

        return ret;
    }

    @Override
    public String summarize(String input, int percentage, boolean stemming, boolean wordNet, boolean stopWords, boolean useNLP, String language) {
        return null;
    }
}