package cz.cvut.fit.sklenivo.JSummary.testing.test;

import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.classification.knn.KNN;
import cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics.KNNMetric;
import cz.cvut.fit.sklenivo.JSummary.testing.SummarizableDocument;
import cz.cvut.fit.sklenivo.JSummary.testing.TestUtils;

import java.util.List;

/**
 * Created by ivo on 19.4.2015.
 */
public class TestKNN implements Runnable {
    private List<SummarizableDocument> documents;
    private int kBound;
    private KNNMetric metric;
    private SummarizationSettings settings;

    public TestKNN(List<SummarizableDocument> documents, int kBound, KNNMetric metric, SummarizationSettings settings) {
        this.documents = documents;
        this.kBound = kBound;
        this.metric = metric;
        this.settings = settings;
    }

    @Override
    public void run() {
        for (int k = 1; k <= kBound; k++){
            StringBuilder log = new StringBuilder();
            log.append("<TEST>\n").append(k).append("-NN").append(metric.toString()).append("\n").append("SETTINGS: ").append(settings).append("\n");
            log.append("FILES: ").append(documents).append("\n");


            long start = System.nanoTime();
            double avg = 0;
            for (int i = 0; i < documents.size(); i++) {
                KNN algorithm = new KNN(k, metric);

                double perf = TestUtils.xValidationRound(algorithm, documents, i, settings);
                log.append("TEST ").append(i).append("  Rouge-1: ").append(perf).append("\n");
                avg += perf;
            }

            log.append("AVG ROUGE: ").append(avg / documents.size()).append("\n");
            log.append("TIME: ").append((System.nanoTime() - start) / 1000000000).append(" s\n");
            log.append("</TEST>").append("\n");
            TestUtils.print(log);
        }
    }
}
