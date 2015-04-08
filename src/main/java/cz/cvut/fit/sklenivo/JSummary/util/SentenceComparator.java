package cz.cvut.fit.sklenivo.JSummary.util;



import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.textrank.TextRankSentence;
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
    private TokenizerME tokenizer;


    private SnowballStemmer stemmer;

    private SummarizationSettings settings;
    /**
     * Creates new instance of SentenceComparator
     */
    public SentenceComparator(SummarizationSettings settings) {
        this.settings = settings;

        if (settings.isStemming()){
            initStemmer();
        }

        if (settings.isStopWords()){
            initStopWords();
        }


        if(settings.isUseNLP()){
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
            stemmer = (SnowballStemmer) Class.forName("org.tartarus.snowball.ext." + settings.getLanguage() + "Stemmer").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println("Error creating stemmer for language " + settings.getLanguage());
            e.printStackTrace();
            stemmer = null;
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
            tokenizer = null;
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
        if (settings.isUseNLP() && tokenizer != null){
            ret = tokenizer.tokenize(s1); //using openNLP's tokenizer
        } else {
            ret = s1.split(" ");
        }


        if (settings.isStemming() && stemmer != null){
            for (int i = 0; i < ret.length; i++){
                stemmer.setCurrent(ret[i]);
                stemmer.stem();
                ret[i] = stemmer.getCurrent();
            }
        }

        return ret;
    }
}
