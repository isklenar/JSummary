package cz.cvut.fit.sklenivo.JSummary.testing;

/**
 * Created by ivo on 22.4.2015.
 */
public class RougeResult {
    private double RougeN;
    private double RougeL;
    private double RougeW;

    public double getRougeN() {
        return RougeN;
    }

    public void setRougeN(double rougeN) {
        RougeN = rougeN;
    }

    public double getRougeL() {
        return RougeL;
    }

    public void setRougeL(double rougeL) {
        RougeL = rougeL;
    }

    public double getRougeW() {
        return RougeW;
    }

    public void setRougeW(double rougeW) {
        RougeW = rougeW;
    }

    @Override
    public String toString() {
        return "RougeResult{" +
                "RougeN=" + RougeN +
                ", RougeL=" + RougeL +
                ", RougeW=" + RougeW +
                '}';
    }
}
