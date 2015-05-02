package cz.cvut.fit.sklenivo.JSummary.testing;

import cz.cvut.fit.sklenivo.JSummary.testing.metric.RougeLCS;
import cz.cvut.fit.sklenivo.JSummary.testing.metric.RougeMetric;
import cz.cvut.fit.sklenivo.JSummary.testing.metric.RougeN;
import cz.cvut.fit.sklenivo.JSummary.testing.metric.RougeWLCS;

import java.util.List;

/**
 * Created by ivo on 17.4.2015.
 */
public class Rouge {
    private static RougeMetric rougeN = new RougeN(1);
    private static RougeMetric rougeL = new RougeLCS();
    private static RougeMetric rougeW = new RougeWLCS();

    public static RougeResult evaluate(List<String> references, String canditate){
        RougeResult result = new RougeResult();

        result.setRougeN(rougeN.evaluate(references, canditate));
        result.setRougeL(rougeL.evaluate(references, canditate));
        result.setRougeW(rougeW.evaluate(references, canditate));

        return result;
    }

}
