package cz.cvut.fit.sklenivo.JSummary.bayes;

import cz.cvut.fit.sklenivo.JSummary.Summarizer;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.tokenize.TokenizerME;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ivo on 20.10.14.
 */
public class NaiveBayes implements Summarizer {
    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;

    private int trainingSentencesTotal = 0;
    private int trainingSummaryTotal = 0;

    private int lengthFeatureTotal = 0;
    private int [] paragraphFeaturesTotal = new int[4];

    private double P_S_IN_SUMMARY = 0.0;
    private double [] P_PARAGRAPH_FEATURE;
    private double P_LENGTH_FEATURE = 0.0;

    //P(Fi | s \in S)
    private double P_S_IN_SUMMARY_GIVEN_S = 0.0;
    private double [] P_PARAGRAPH_FEATURE_GIVEN_S;
    private double P_LENGTH_FEATURE_GIVEN_S = 0.0;

    private final static int PARAGRAPH_INITIAL = 1;
    private final static int PARAGRAPH_MEDIAL = 2;
    private final static int PARAGRAPH_FINAL = 3;

    private int lengthFeatureTotalSummary;
    private int [] paragraphFeaturesTotalSummary = new int[4];

    public NaiveBayes() {
        P_PARAGRAPH_FEATURE = new double[4];
        P_PARAGRAPH_FEATURE_GIVEN_S = new double[4];
        for (int i = 0; i < P_PARAGRAPH_FEATURE.length; i++){
            P_PARAGRAPH_FEATURE[i] = 0.0;
            P_PARAGRAPH_FEATURE_GIVEN_S[i] = 0.0;
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

        System.out.println("Text: " + text.size() + "  Summary: " + sentenceSummary.length);

        calculateFeaturesTotal(text);
        calculateFeaturesForSummary(text, sentenceSummary);

    }

    private void calculateFeaturesForSummary(ArrayList<BayesSentence> text, String [] sentenceSummary) {
        lengthFeatureTotalSummary += countLengthFeatureInSummary(text, sentenceSummary);
        countParagraphFeaturesSummary(text, sentenceSummary);

        for (int i = 1; i < paragraphFeaturesTotal.length; i++){
            P_PARAGRAPH_FEATURE_GIVEN_S[i] = paragraphFeaturesTotalSummary[i]/((double)trainingSummaryTotal);
            System.out.println("P_SUMMARY " + i + ": " + P_PARAGRAPH_FEATURE_GIVEN_S[i]);
        }

        P_LENGTH_FEATURE_GIVEN_S = lengthFeatureTotalSummary/((double)trainingSummaryTotal);
        System.out.println("L_SUMMARY: " + P_LENGTH_FEATURE_GIVEN_S);
    }

    private void calculateFeaturesTotal(ArrayList<BayesSentence> text) {
        lengthFeatureTotal += countLengthFeatureTotal(text);
        countParagraphFeaturesTotal(text);

        for (int i = 1; i < paragraphFeaturesTotal.length; i++){
            P_PARAGRAPH_FEATURE[i] = paragraphFeaturesTotal[i]/((double)trainingSentencesTotal);
            System.out.println("P_TOTAL " + i + ": " + P_PARAGRAPH_FEATURE[i]);
        }

        P_LENGTH_FEATURE = lengthFeatureTotal/((double)trainingSentencesTotal);
        System.out.println("L_TOTAL: " + P_LENGTH_FEATURE);
        P_S_IN_SUMMARY = trainingSummaryTotal/((double)trainingSentencesTotal);
        System.out.println("S_TOTAL: " + P_S_IN_SUMMARY);
    }

    private int countLengthFeatureTotal(ArrayList<BayesSentence> text) {
        int total = 0;
        for (BayesSentence s : text){
            if (s.getLengthFeature() != 0){
                total++;
            }
        }

        return total;
    }

    private void countParagraphFeaturesTotal(ArrayList<BayesSentence> text) {
        for (BayesSentence s : text){
            paragraphFeaturesTotal[s.getParagraphFeature()]++;
        }
    }

    private void countParagraphFeaturesSummary(ArrayList<BayesSentence> text, String[] summarySentences){
        for (String s : summarySentences){
            int index = text.indexOf(new BayesSentence(s, 0 ,0));
            if (index == -1){
                continue;
            }
            paragraphFeaturesTotalSummary[text.get(index).getParagraphFeature()]++;
        }
    }

    private int countLengthFeatureInSummary(ArrayList<BayesSentence> text, String[] summarySentences) {
        int count = 0;
        for (String s : summarySentences){
            int index = text.indexOf(new BayesSentence(s, 0 ,0));
            if (index == -1){
                continue;
            }
            if (text.get(index).getLengthFeature() != 0){
                count++;
            }
        }

        return count;
    }

    private String[] splitSentences(String summary) {
        String [] tmp =  summary.split("\\.");
        for (int i = 0; i < tmp.length; i++){
            tmp[i] = tmp[i].trim();
        }

        return tmp;
    }

    private ArrayList<BayesSentence> preProcess(String text){
        ArrayList<BayesSentence> sentences = new ArrayList<>();
        String [][] paragraphs = splitParagraphs(text);
        for (int p = 0; p < paragraphs.length; p++){
            for (int s = 0; s < paragraphs[p].length; s++){
                int paragraphFeature = calculateParagraphFeature(s, paragraphs[p].length);
                int lengthFeature = calculateLengthFeature(paragraphs[p][s].trim());
                sentences.add(new BayesSentence(paragraphs[p][s].trim(), paragraphFeature, lengthFeature));
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
            String [] tmp = paragraphs[i].split("\\.");
            for (String s : tmp){
                s = s.trim();
            }
            ret[i] = tmp;
        }

        return ret;
    }

    @Override
    public String summarize(String input, int percentage, boolean stemming, boolean wordNet, boolean stopWords, boolean useNLP, String language) {
        ArrayList<BayesSentence> text = preProcess(input);
        classify(text);

        int count = (int)(text.size() * (percentage/100.0));

        //account for rounding error
        if (count == 0){
            count = 1;
        }


        return selectSummary(text, count);
    }

    private String selectSummary(ArrayList<BayesSentence> text, int sentencesToSelect) {
        ArrayList<BayesSentence> copy = new ArrayList<>(text);
        Collections.sort(text);
        ArrayList<BayesSentence> selected = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < sentencesToSelect; i++){
            selected.add(text.get(i));

        }
        copy.retainAll(selected);

        for (int i = 0; i < sentencesToSelect; i++){
            builder.append(copy.get(i).getText());
            builder.append(". ");
        }

        return builder.toString();
    }

    private void classify(ArrayList<BayesSentence> text) {
        for(BayesSentence sentence : text){
            double tmp = 1;
            if (sentence.getLengthFeature() != 0){
                tmp = (P_LENGTH_FEATURE_GIVEN_S * P_S_IN_SUMMARY)/ P_LENGTH_FEATURE;
            }

            switch (sentence.getParagraphFeature()){
                case PARAGRAPH_INITIAL:
                    tmp *= (P_PARAGRAPH_FEATURE_GIVEN_S[PARAGRAPH_INITIAL]* P_S_IN_SUMMARY)/ P_PARAGRAPH_FEATURE[PARAGRAPH_INITIAL];
                    break;

                case PARAGRAPH_MEDIAL:
                    tmp *= (P_PARAGRAPH_FEATURE_GIVEN_S[PARAGRAPH_MEDIAL]* P_S_IN_SUMMARY)/ P_PARAGRAPH_FEATURE[PARAGRAPH_MEDIAL];
                    break;
                case PARAGRAPH_FINAL:
                    tmp *= (P_PARAGRAPH_FEATURE_GIVEN_S[PARAGRAPH_FINAL]* P_S_IN_SUMMARY)/ P_PARAGRAPH_FEATURE[PARAGRAPH_FINAL];
                    break;
                default:
                    break;
            }

            sentence.setP_IS_IN_SUMMARY(tmp);
            System.out.println("P s in Summary: " + tmp);
        }
    }
}