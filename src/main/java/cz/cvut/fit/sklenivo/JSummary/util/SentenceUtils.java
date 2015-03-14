package cz.cvut.fit.sklenivo.JSummary.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivo on 7.3.2015.
 */
public class SentenceUtils {

    private static String extractNextSentence(String text, List<String> sentences) {
        char [] characters = text.toCharArray();

        for (int i =0 ; i < characters.length - 1; i++){
            if((characters[i] == '.' || characters[i] == '!' || characters[i] == '?')
                    && (characters[i+1] == ' ' || characters[i+1] == '\n')) {
                String sentence = text.substring(0, i + 2);
                sentences.add(sentence);
                return text.substring(i + 2);
            }

        }

        sentences.add(text.trim());
        return null;
    }

    public static List<String> splitToSentences(String text){
        List<String> sentences = new ArrayList<>();
        while (text != null){
            text = extractNextSentence(text, sentences);
        }

        return sentences;
    }
}
