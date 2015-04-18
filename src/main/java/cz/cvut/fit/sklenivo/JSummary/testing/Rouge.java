package cz.cvut.fit.sklenivo.JSummary.testing;

import cz.cvut.fit.sklenivo.JSummary.testing.metric.RougeMetric;

import java.util.List;

/**
 * Created by ivo on 17.4.2015.
 */
public class Rouge {

    public static double evaluate(List<String> references, String canditate, RougeMetric metric){
        return metric.evaluate(references, canditate);
    }

}
