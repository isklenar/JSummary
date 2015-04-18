package cz.cvut.fit.sklenivo.JSummary.testing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivo on 17.4.2015.
 */
public class ROUGE {
    public static double rougeN(int n, List<String> references, String candidates){
        int topSum = 0;
        int bottomSum = 0;
        for (String reference : references) {
            int matchSum = 0;
            int totalSum = 0;
            //for (String candidate : candidates) {
                List<String> referenceGrams = splitToGrams(reference, n);
                List<String> candidatesGrams = splitToGrams(candidates, n);

                totalSum += referenceGrams.size();

                referenceGrams.retainAll(candidatesGrams);
                matchSum += referenceGrams.size();
            //}

            topSum += matchSum;
            bottomSum += totalSum;

        }

        return topSum/((double)bottomSum);
    }

    private static List<String> splitToGrams(String s, int n) {
        List<String> ret = new ArrayList<>();
        s = s.replace(".", "");
        s = s.replace("?", "");
        s = s.replace("!", "");

        String [] terms = s.split(" ");

        int i = 0;

        while (i + n < terms.length){
            StringBuilder builder = new StringBuilder();
            for (int j = i; j < i + n; j++){
                builder.append(terms[j]);
            }

            ret.add(builder.toString());

            i += n;
        }

        StringBuilder builder = new StringBuilder();

        for (int j = i; j < terms.length; j++){
            builder.append(terms[i]);
        }

        ret.add(builder.toString());
        return ret;
    }
}
