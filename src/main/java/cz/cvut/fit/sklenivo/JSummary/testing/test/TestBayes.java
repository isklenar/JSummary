package cz.cvut.fit.sklenivo.JSummary.testing.test;

import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.classification.bayes.NaiveBayes;
import cz.cvut.fit.sklenivo.JSummary.testing.RougeResult;
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

        long start = System.nanoTime();
        RougeResult avg = new RougeResult();
        for (int i = 0; i < documents.size(); i++) {
            NaiveBayes algorithm = new NaiveBayes();

            long taskStart = System.nanoTime();
            RougeResult perf = TestUtils.xValidationRound(algorithm, documents, i, settings);
            long taskEnd = System.nanoTime();

            log.append("TEST ").append(i).append("  Rouge-1: ").append(perf).append("  TESTTIME: ").append((taskEnd - taskStart)/1000000).append("ms\n");
            avg.setRougeN(avg.getRougeN() + perf.getRougeN());
            avg.setRougeL(avg.getRougeL() + perf.getRougeL());
            avg.setRougeW(avg.getRougeW() + perf.getRougeW());

        }

        avg.setRougeN(avg.getRougeN() / documents.size());
        avg.setRougeL(avg.getRougeL() / documents.size());
        avg.setRougeW(avg.getRougeW() / documents.size());

        log.append("AVG ROUGE: ").append(avg).append("\n");
        log.append("TIME: ").append((System.nanoTime() - start) / 1000000000).append(" s\n");
        log.append("</TEST>").append("\n");

        TestUtils.print(log);
    }
}
