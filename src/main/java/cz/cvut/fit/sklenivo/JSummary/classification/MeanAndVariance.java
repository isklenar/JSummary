package cz.cvut.fit.sklenivo.JSummary.classification;

/**
 * Created by ivo on 18.4.2015.
 */
public class MeanAndVariance {
    private double mean;
    private double var;

    private int featureNum;

    public MeanAndVariance(double mean, double var, int featureNum) {
        this.mean = mean;
        this.var = var;
        this.featureNum = featureNum;
    }


    public double getMean() {
        return mean;
    }

    public double getVar() {
        return var;
    }

    public int getFeatureNum() {
        return featureNum;
    }
}
