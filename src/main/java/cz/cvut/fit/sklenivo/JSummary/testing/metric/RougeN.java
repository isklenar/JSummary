package cz.cvut.fit.sklenivo.JSummary.testing.metric;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivo on 18.4.2015.
 */
public class RougeN implements RougeMetric {
    private int n;

    public RougeN(int n) {
        this.n = n;
    }

    @Override
    public double evaluate(List<String> references, String candidate) {
        double max = -1;
        List<String> candidatesGrams = splitToGrams(candidate, n);
        for (String reference : references) {
            int matchSum = 0;
            int totalSum = 0;
            List<String> referenceGrams = splitToGrams(reference, n);

            totalSum += referenceGrams.size();

            referenceGrams.retainAll(candidatesGrams);
            matchSum += referenceGrams.size();

            double score = matchSum / (double)totalSum;

            if (score > max){
                max = score;
            }
        }

        return max;
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
