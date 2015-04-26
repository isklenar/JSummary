package cz.cvut.fit.sklenivo.JSummary.util;

import org.tartarus.snowball.SnowballStemmer;

/**
 * Created by ivo on 23.4.2015.
 */
public class StemmerFactory {
    public static SnowballStemmer create(String language){
        try {
            //create a stemmer for specified language
            return (SnowballStemmer) Class.forName("org.tartarus.snowball.ext." + language.toLowerCase() + "Stemmer").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalArgumentException();
        }
    }
}
