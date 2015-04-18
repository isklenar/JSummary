package cz.cvut.fit.sklenivo.JSummary.testing.metric;

import java.util.List;

/**
 * Created by ivo on 18.4.2015.
 */
public class RougeLCS implements RougeMetric{
    @Override
    public double evaluate(List<String> references, String candidate) {
        int lcs
        return 0;
    }


    private int LCS(String x, String y){
        int [][] lcs = new int[x.length()][y.length()];
        for (int i = 0; i < x.length(); i++){
            lcs[i][0] = 0;
        }
        for (int i = 0; i < x.length(); i++){
            lcs[0][i] = 0;
        }

        for (int i = 1; i < x.length(); i++){
            for (int j = 1; j < y.length(); j++){
                if (x.charAt(i) == y.charAt(j)){
                    lcs[i][j] = lcs[i - 1][j - 1] + 1;
                } else {
                    lcs[i][j] = Math.max(lcs[i - 1][j], lcs[i][j - 1]);
                }
            }
        }

        return lcs[x.length() - 1][y.length() - 1];
    }
}
