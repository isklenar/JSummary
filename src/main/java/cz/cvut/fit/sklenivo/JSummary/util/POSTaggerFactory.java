package cz.cvut.fit.sklenivo.JSummary.util;

/**
 * Created by ivo on 22.4.2015.
 */
public class POSTaggerFactory {
    private static POSTagger czechTagger = null;
    private static POSTagger englishTagger = null;


    public synchronized static POSTagger create(String language){
        if (language.equals(WordDatabases.CZECH_LANGUAGE)){
            if (czechTagger == null){
                czechTagger = new POSTagger(language);
            }

            return czechTagger;
        } else if (language.equals(WordDatabases.ENGLISH_LANGUAGE)){
            if (englishTagger == null){
                englishTagger = new POSTagger(language);
            }

            return englishTagger;
        }

        return null;
    }
}
