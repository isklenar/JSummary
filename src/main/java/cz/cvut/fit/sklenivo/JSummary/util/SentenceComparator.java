package cz.cvut.fit.sklenivo.JSummary.util;



import cz.cvut.fit.sklenivo.JSummary.textrank.TextRankSentence;
import edu.mit.jwi.IDictionary;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.tartarus.snowball.SnowballStemmer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class that compares senteces.
 */
public class SentenceComparator {
    private boolean stemming;
    private boolean stopWords;
    private boolean wordNet;
    private boolean NLP;

    private String language;

    private TokenizerME tokenizer;
    private IDictionary wordNetDictionary;

    private SnowballStemmer stemmer;

    /**
     * Creates new instance of SentenceComparator
     * @param stemming use stemming or not
     * @param stopWords use stopwords or not
     * @param wordNet use WordNet database or not
     * @param NLP use NLP tokenizer
     * @param language language for stemmer and stopwords
     */
    public SentenceComparator(boolean stemming, boolean stopWords, boolean wordNet, boolean NLP, String language) {
        this.stemming = stemming;
        this.stopWords = stopWords;
        this.wordNet = wordNet;
        this.NLP = NLP;
        this.language = language.toLowerCase();

        if (stemming){
            initStemmer();
        }

        if (stopWords){
            initStopWords();
        }

        if(wordNet){
            initWordNet();
        }

        if(NLP){
            initTokenizer();
        }
    }

    /**
     * Calculates normalized sentence similarity.
     * @param sentence1 first sentence
     * @param sentence2 second sentence
     * @return similarity
     */
    public double TextRankSentenceSimilarity(TextRankSentence sentence1, TextRankSentence sentence2){
        if (sentence1 == sentence2 || sentence1.equals(sentence2)){
            return 0;
        }
        Set<String> s1set = new HashSet<>(Arrays.asList(tokenize(sentence1.getSentence())));
        Set<String> s2set = new HashSet<>(Arrays.asList(tokenize(sentence2.getSentence())));

        int s1size = s1set.size();
        int s2size = s2set.size();

        //do intersection of both sets
        s1set.retainAll(s2set);

        //abs() to get rid of -0.0
        return Math.abs((s1set.size())/(Math.log(s1size) + Math.log(s2size)));
    }

    /**
     * creates a instance of stemmer. If this fails, no stemmer will be used
     */
    private void initStemmer(){
        try {
            //create a stemmer for specified language
            stemmer = (SnowballStemmer) Class.forName("org.tartarus.snowball.ext." + language + "Stemmer").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println("Error creating stemmer for language " + language);
            e.printStackTrace();
            stemming = false;
        }
    }

    private void initStopWords(){

    }

    private void initWordNet(){
        /*wordNetDictionary = new Dictionary(new File("resources/WordNet/dict"))*/
    }

    /**
     * Creates tokenizer for NLP.
     * If creation fails, prints an error message and won't use tokenizer for comparisons
     */
    private void initTokenizer(){

        try(InputStream is = new FileInputStream("resources/OpenNLP/en-token.bin")) {
            TokenizerModel tokenizerModel = new TokenizerModel(is);
            tokenizer = new TokenizerME(tokenizerModel);
        } catch (IOException e) {
            System.out.println("Error creating NLP tokenizer. Reason:");
            System.out.println(e.getMessage());
            NLP = false;
        }
    }


    /**
     * Tokenizes a sentence to words.
     * If any of the flags are set (NLP, stemming, stop words, wordnet) will use that specified transformation
     * on every word.
     *
     * WordNet will get the first meaning of every word.
     *
     * @param s1 String to be tokenized
     * @return Array of tokenized words
     */
    private String [] tokenize(String s1) {
        String [] ret;
        if (NLP){
            ret = tokenizer.tokenize(s1); //using openNLP's tokenizer
        } else {
            ret = s1.split(" ");
        }


        if (stemming){
            for (int i = 0; i < ret.length; i++){
                stemmer.setCurrent(ret[i]);
                stemmer.stem();
                ret[i] = stemmer.getCurrent();
            }
        }

        return ret;
    }

    public boolean useNLP() {
        return NLP;
    }

    public void setNLP(boolean NLP) {
        this.NLP = NLP;
    }

    public boolean useStemming() {
        return stemming;
    }

    public void setStemming(boolean stemming) {
        this.stemming = stemming;
    }

    public boolean useStopWords() {
        return stopWords;
    }

    public void setStopWords(boolean stopWords) {
        this.stopWords = stopWords;
    }

    public boolean useWordNet() {
        return wordNet;
    }

    public void setWordNet(boolean wordNet) {
        this.wordNet = wordNet;
    }
}
