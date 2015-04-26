package cz.cvut.fit.sklenivo.JSummary.testing.metric;

import java.util.List;

/**
 * Created by ivo on 18.4.2015.
 */
public class RougeLCS implements RougeMetric{

    private static int beta = 8;

    @Override
    public double evaluate(List<String> references, String candidate) {
        double acc = 0;
        for (int i = 0; i < references.size(); i++){
            double max = 0;
            for (int j = 0; j < references.size(); j++){
                if (i != j){
                    double score = evalLCS(references.get(j), candidate);
                    if (score > max){
                        max = score;
                    }
                }

            }
            acc += max;
        }


        return acc / references.size();
    }

    private double evalLCS(String reference, String candidate) {
        String [] ref = reference.split(" ");
        String [] can = candidate.split(" ");
        double lcs = LCS(ref, can);
        double rlcs = lcs/(double)ref.length;

        double plcs = lcs/(double)can.length;

        double top = (1 + Math.pow(beta, 2))*rlcs * plcs;
        double bottom = rlcs + Math.pow(beta, 2) * plcs;

        return top/bottom;
    }


    private int LCS(String[] ref, String[] candidate){
        int [][] lcs = new int[ref.length][candidate.length];
        for (int i = 0; i < ref.length; i++){
            lcs[i][0] = 0;
        }
        for (int i = 0; i < candidate.length; i++){
            lcs[0][i] = 0;
        }

        for (int i = 1; i < ref.length; i++){
            for (int j = 1; j < candidate.length; j++){
                if (ref[i].trim().equals(candidate[j].trim())){
                    lcs[i][j] = lcs[i - 1][j - 1] + 1;
                } else {
                    lcs[i][j] = Math.max(lcs[i - 1][j], lcs[i][j - 1]);
                }
            }
        }

        return lcs[ref.length - 1][candidate.length - 1];
    }
}
