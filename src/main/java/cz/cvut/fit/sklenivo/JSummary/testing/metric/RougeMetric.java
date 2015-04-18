package cz.cvut.fit.sklenivo.JSummary.testing.metric;

import java.util.List;

/**
 * Created by ivo on 18.4.2015.
 */
public interface RougeMetric {
    public double evaluate(List<String> references, String candidate);
}
