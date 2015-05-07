package cz.cvut.fit.sklenivo.JSummary.textrank;


import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.util.Languages;
import cz.cvut.fit.sklenivo.JSummary.util.StemmerFactory;
import cz.cvut.fit.sklenivo.JSummary.util.WordDatabases;
import opennlp.tools.tokenize.TokenizerME;
import org.tartarus.snowball.SnowballStemmer;

import java.util.*;

/**
 * Utility class that compares senteces.
 */
class SentenceComparator {
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
        Set<String> s1set = new HashSet<>(tokenize(sentence1.getSentence()));
        Set<String> s2set = new HashSet<>(tokenize(sentence2.getSentence()));

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
            stemmer = StemmerFactory.create(settings.getLanguage());
        } catch (IllegalArgumentException e) {
            stemmer = null;
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
     * @return list of tokenized words
     */
    private List<String> tokenize(String s1) {
        List<String>  ret = new ArrayList<>(Arrays.asList(s1.split(" ")));

        if (settings.isStopWords() && settings.getLanguage().equals(Languages.CZECH_LANGUAGE)){
            ret.removeAll(WordDatabases.CZECH_STOP_WORDS);
        }

        if (settings.isStopWords() && settings.getLanguage().equals(Languages.ENGLISH_LANGUAGE)){
            ret.removeAll(WordDatabases.ENGLISH_STOP_WORDS);
        }

        if (settings.isStemming() && stemmer != null){
            for (int i = 0; i < ret.size(); i++){
                stemmer.setCurrent(ret.get(i));
                stemmer.stem();
                ret.set(i, stemmer.getCurrent());
            }
        }

        return ret;
    }
}
