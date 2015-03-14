package cz.cvut.fit.sklenivo.JSummary.bayes;

import cz.cvut.fit.sklenivo.JSummary.Summarizer;
import cz.cvut.fit.sklenivo.JSummary.util.SentenceUtils;
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

    private List<BayesSentence> model = new ArrayList<>();

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
        String [] paragraphs = splitParagraphs(trainingText);
        List<BayesSentence> sentences = new ArrayList<>();

        for (int i = 0; i < paragraphs.length; i++){
            sentences.addAll(createBayesSentences(paragraphs[i]));
        }

        List<BayesSentence> summarySentences = createBayesSentences(summary);

        for (BayesSentence sentence : sentences){
            sentence.extractFeatures();

            if (summarySentences.contains(sentence)){
                sentence.setInSummary(true);
            }
        }

        model.addAll(sentences);

        printBayesTable(model);
    }

    private void printBayesTable(List<BayesSentence> sentences) {
        System.out.print("\t| ");
        for(int i = 0; i < sentences.get(0).getFeatures().length; i++){
            System.out.print((i < 10 ? i + " " : i) + "\t| ");
        }
        System.out.println("In summary");
        int i = 0;

        for (BayesSentence sentence : sentences){
            printSentenceFeatures(sentence, i);
            i++;
        }
    }

    private void printSentenceFeatures(BayesSentence sentence, int i) {
        System.out.print("s" + i + "\t| ");

        for (Double feature : sentence.getFeatures()){
            System.out.print(feature + "\t| ");
            i++;
        }

        System.out.println(sentence.isInSummary());
    }

    private List<BayesSentence> createBayesSentences(String paragraph) {
        List<String> sentences = SentenceUtils.splitToSentences(paragraph);
        List<BayesSentence> ret = new ArrayList<>();
        for (int i = 0; i < sentences.size(); i++){
            int paragraphPosition = i == 0 ? 1 : i == sentences.size() - 1 ? 3 : 2;
            ret.add(new BayesSentence(sentences.get(i), paragraphPosition));
        }

        return ret;
    }


    private String[] splitParagraphs(String trainingText) {
        String [] paragraphs = trainingText.split("\n\n+");

        return paragraphs;
    }


    @Override
    public String summarize(String input, double ratio, boolean stemming, boolean wordNet, boolean stopWords, boolean useNLP, String language) {
        return null;
    }
}