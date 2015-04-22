package cz.cvut.fit.sklenivo.JSummary.testing.metric;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivo on 22.4.2015.
 */
public class RougeWCLS implements RougeMetric {
    private static int beta = 8;

    @Override
    public double evaluate(List<String> references, String candidate) {
        List<Double> results = new ArrayList<>();
        for (int i = 0; i < references.size(); i++){
            double max = 0;
            for (int j = 0; j < references.size(); j++){
                if (i != j){
                    double score = evalWLCS(references.get(j), candidate);
                    if (score > max){
                        max = score;
                    }
                }

            }
            results.add(max);
        }

        double acc = 0;
        for (Double result : results){
            acc += result;
        }

        return acc / results.size();
    }

    private double evalWLCS(String reference, String candidate) {
        String [] ref = reference.split(" ");
        String [] can = candidate.split(" ");
        double wlcs = WLCS(ref, can);

        double rlcs = f_inverse(wlcs/f(ref.length));

        double plcs = f_inverse(wlcs/f(can.length));

        double top = (1 + Math.pow(beta, 2))*rlcs * plcs;
        double bottom = rlcs + Math.pow(beta, 2) * plcs;

        return top/bottom;
    }


    private int WLCS(String[] ref, String[] candidate){
        int [][] lcs = new int[ref.length][candidate.length];
        int [][] wcs = new int[ref.length][candidate.length];
        for (int i = 0; i < ref.length; i++){
            lcs[i][0] = 0;
            wcs[i][0] = 0;
        }
        for (int i = 0; i < candidate.length; i++){
            lcs[0][i] = 0;
            wcs[0][i] = 0;
        }

        for (int i = 1; i < ref.length; i++){
            for (int j = 1; j < candidate.length; j++){
                if (ref[i].trim().equals(candidate[j].trim())){
                    int k = wcs[i - 1][j - 1];
                    lcs[i][j] = (int) (lcs[i - 1][j - 1] + f(k + 1) - f(k));
                    wcs[i][j] = k + 1;
                } else {
                    if (lcs[i - 1][j] > lcs[i][j - 1]){
                        lcs[i][j] = lcs[i -1][j];
                        wcs[i][j] = 0;
                    } else {
                        lcs[i][j] = lcs[i][j - 1];
                        wcs[i][j] = 0;
                    }
                }
            }
        }

        return lcs[ref.length - 1][candidate.length - 1];
    }

    private double f(double k){
        return k*k;
    }

    private double f_inverse(double k){
        return Math.sqrt(k);
    }
}
