package cz.cvut.fit.sklenivo.JSummary.testing.metric;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ivo on 18.4.2015.
 */
public class RougeLCS implements RougeMetric{
    @Override
    public double evaluate(List<String> references, String candidate) {
       /* double max = -1;

        List<String> candidateSentences = SentenceUtils.splitToSentences(candidate);
        double lcsSum = 0;
        for (String ref : references){

            lcsSum += LCS(ref, candidate);

        }

        double r_lcs = lcsSum / reference.size();
        double l_lcs = lcsSum / candidateSentences.size();*/
        return 0;
    }


    private int LCS(String xref, String xcandidate){
        List<String> ref = Arrays.asList(xref.split(" "));
        List<String> candidate = Arrays.asList(xcandidate.split(" "));


        int [][] lcs = new int[ref.size()][candidate.size()];
        for (int i = 0; i < ref.size(); i++){
            lcs[i][0] = 0;
        }
        for (int i = 0; i < candidate.size(); i++){
            lcs[0][i] = 0;
        }

        for (int i = 1; i < ref.size(); i++){
            for (int j = 1; j < candidate.size(); j++){
                if (ref.get(i).trim().equals(candidate.get(j).trim())){
                    lcs[i][j] = lcs[i - 1][j - 1] + 1;
                } else {
                    lcs[i][j] = Math.max(lcs[i - 1][j], lcs[i][j - 1]);
                }
            }
        }

        return lcs[ref.size() - 1][candidate.size() - 1];
    }
}
