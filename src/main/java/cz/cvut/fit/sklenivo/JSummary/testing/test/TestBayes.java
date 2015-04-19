package cz.cvut.fit.sklenivo.JSummary.testing.test;

import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.classification.bayes.NaiveBayes;
import cz.cvut.fit.sklenivo.JSummary.testing.SummarizableDocument;
import cz.cvut.fit.sklenivo.JSummary.testing.TestUtils;

import java.util.List;

/**
 * Created by ivo on 19.4.2015.
 */
public class TestBayes implements Runnable {
    private SummarizationSettings settings;
    private List<SummarizableDocument> documents;

    public TestBayes(SummarizationSettings settings, List<SummarizableDocument> documents) {
        this.settings = settings;
        this.documents = documents;
    }

    @Override
    public void run() {
        StringBuilder log = new StringBuilder();
        log.append("<TEST>\nXVALIDATION BAYES\n" + "SETTINGS: ").append(settings).append("\n").append("SETTINGS: ").append(settings).append("\n");
        log.append("FILES: ").append(documents).append("\n");

        double avg = 0;

        long start = System.nanoTime();
        for (int i = 0; i < documents.size(); i++) {
            NaiveBayes algorithm = new NaiveBayes();

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
