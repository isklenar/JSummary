package cz.cvut.fit.sklenivo.JSummary.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivo on 7.3.2015.
 */
public class ParagraphSplitter {

    public static List<String> splitParagraph(String paragraph){
        List<String> sentences = new ArrayList<>();
        while (paragraph != null){
            paragraph = extractNextSentence(paragraph, sentences);
        }

        return sentences;
    }

    private static String extractNextSentence(String paragraph, List<String> sentences) {
        int i = 0;
        char [] characters = paragraph.toCharArray();
        StringBuilder builder = new StringBuilder();
        while (i < characters.length){
            if (characters[i] == '.' || characters[i] == '?' || characters[i] == '!'){
                builder.append(characters[i]);
                sentences.add(builder.toString().trim());
                return paragraph.substring(i+1);
            }

            builder.append(characters[i]);
            i++;
        }

        return null;
    }
}
