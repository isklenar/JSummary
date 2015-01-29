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

    private int trainingSentencesTotal = 0;
    private int trainingSummaryTotal = 0;
    private int lengthFeatureTotal = 0;
    int [] paragraphFeaturesTotal = new int[4];

    private double P_S_IN_SUMMARY = 0.0;
    private double [] P_PARAGRAPH_FEATURE;
    private double P_LENGTH_FEATURE = 0.0;

    private static int PARAGRAPH_INITIAL = 1;
    private static int PARAGRAPH_MEDIAL = 2;
    private static int PARAGRAPH_FINAL = 3;

    public NaiveBayes() {
        P_PARAGRAPH_FEATURE = new double[4];
        for (int i = 0; i < P_PARAGRAPH_FEATURE.length; i++){
            P_PARAGRAPH_FEATURE[i] = 0.0;
        }
/*
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
        }*/
    }

    public void train(String trainingText, String summary) {
        ArrayList<BayesSentence> text = preProcess(trainingText);
        String [] sentenceSummary = splitSentences(summary);

        trainingSentencesTotal += text.size();
        trainingSummaryTotal += sentenceSummary.length;

        lengthFeatureTotal += countLengthFeature(text, sentenceSummary);
        countParagraphFeatures(text, sentenceSummary);

        for (int i = 1; i < paragraphFeaturesTotal.length; i++){
            P_PARAGRAPH_FEATURE[i] = ((double)trainingSentencesTotal)/paragraphFeaturesTotal[i];
            System.out.println("P " + i + ": " + P_PARAGRAPH_FEATURE[i]);
        }

        P_LENGTH_FEATURE = ((double)trainingSentencesTotal)/lengthFeatureTotal;
        System.out.println("L: " + P_LENGTH_FEATURE);
        P_S_IN_SUMMARY = ((double)trainingSentencesTotal)/trainingSummaryTotal;
        System.out.println("S: " + P_S_IN_SUMMARY);
    }

    private void countParagraphFeatures(ArrayList<BayesSentence> text, String[] sentences) {
        for (String s : sentences){
            int index = text.indexOf(new BayesSentence(s, 0 ,0));
            int paragraphFeature = text.get(index).getParagraphFeature();
            paragraphFeaturesTotal[paragraphFeature]++;
        }
    }

    private int countLengthFeature(ArrayList<BayesSentence> text, String[] sentences) {
        int count = 0;
        for (String s : sentences){
            int index = text.indexOf(new BayesSentence(s, 0 ,0));
            if (text.get(index).getLengthFeature() != 0){
                count++;
            }
        }

        return count;
    }

    private String[] splitSentences(String summary) {
        return summary.split(" ");
    }

    private void trainInner(String[][] trainingSentences, String[] trainingSummary) {
        ArrayList<BayesSentence> bayesSentences = new ArrayList<>();

        for (int paragraph = 0; paragraph < trainingSentences.length; paragraph++){
            for (int sentence = 0; sentence < trainingSentences[paragraph].length; sentence++){
                int paragraphFeature = calculateParagraphFeature(sentence, trainingSentences[paragraph].length);
                bayesSentences.add(new BayesSentence(trainingSentences[paragraph][sentence], paragraph, paragraphFeature));
            }
        }
    }

    private ArrayList<BayesSentence> preProcess(String text){
        ArrayList<BayesSentence> sentences = new ArrayList<>();
        String [][] paragraphs = splitParagraphs(text);
        for (int p = 0; p < paragraphs.length; p++){
            for (int s = 0; s < paragraphs[p].length; s++){
                int paragraphFeature = calculateParagraphFeature(s, paragraphs[p].length);
                int lengthFeature = calculateLengthFeature(paragraphs[p][s]);
                sentences.add(new BayesSentence(paragraphs[p][s], paragraphFeature, lengthFeature));
            }
        }

        return sentences;
    }

    private int calculateLengthFeature(String s) {
        int length = s.split(" ").length;

        return length >= 5 ? 1 : 0;
    }

    private int calculateParagraphFeature(int sentenceNum, int paragraphLength){
        if (paragraphLength == 1){
            return PARAGRAPH_INITIAL;
        }

        if (paragraphLength == 2 && sentenceNum == 0){
            return PARAGRAPH_INITIAL;
        } else if (paragraphLength == 2 && sentenceNum == 1){
            return PARAGRAPH_FINAL;
        }

        if (sentenceNum == 0){
            return PARAGRAPH_INITIAL;
        } else if (sentenceNum == paragraphLength - 1){
            return PARAGRAPH_FINAL;
        } else {
            return PARAGRAPH_MEDIAL;
        }

    }

    private String[][] splitParagraphs(String trainingText) {
        String [] paragraphs = trainingText.split("\n\n+");
        String[][] ret = new String[paragraphs.length][];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = paragraphs[i].split(".");
        }

        return ret;
    }

    @Override
    public String summarize(String input, int percentage, boolean stemming, boolean wordNet, boolean stopWords, boolean useNLP, String language) {
        return null;
    }
}