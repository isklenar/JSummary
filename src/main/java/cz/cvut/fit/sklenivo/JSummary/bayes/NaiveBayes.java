package cz.cvut.fit.sklenivo.JSummary.bayes;

import cz.cvut.fit.sklenivo.JSummary.Summarizer;
import cz.cvut.fit.sklenivo.JSummary.util.ParagraphSplitter;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.tokenize.TokenizerME;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivo on 20.10.14.
 */
public class NaiveBayes implements Summarizer {
    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;

    private List<BayesSentence> sentences = new ArrayList<>();

    public NaiveBayes() {



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
        String [][] paragraphs = splitParagraphs(trainingText);
        List<BayesSentence> summarySentences = createBayesSentences(summary);
    }

    private List<BayesSentence> createBayesSentences(String summary) {
        List<String> sentences = ParagraphSplitter.splitParagraph(summary);
        List<BayesSentence> ret = new ArrayList<>();
        for (String sentence : sentences){
            ret.add(new BayesSentence(sentence));
        }

        return ret;
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
        return null;
    }

}