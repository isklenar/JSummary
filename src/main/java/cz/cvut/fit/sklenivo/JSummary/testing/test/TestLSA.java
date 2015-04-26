package cz.cvut.fit.sklenivo.JSummary.testing.test;

import cz.cvut.fit.sklenivo.JSummary.LSA.LSASummarizer;
import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.testing.Rouge;
import cz.cvut.fit.sklenivo.JSummary.testing.RougeResult;
import cz.cvut.fit.sklenivo.JSummary.testing.SummarizableDocument;
import cz.cvut.fit.sklenivo.JSummary.testing.TestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivo on 19.4.2015.
 */
public class TestLSA implements Runnable {
    private SummarizationSettings settings;
    private List<SummarizableDocument> documents;

    public TestLSA(SummarizationSettings settings, List<SummarizableDocument> documents) {
        this.settings = settings;
        this.documents = documents;
    }

    @Override
    public void run() {
        StringBuilder log = new StringBuilder();
        log.append("<TEST>\nLSA\n" + "SETTINGS: ").append(settings).append("\n").append("SETTINGS: ").append(settings).append("\n");
        log.append("FILES: ").append(documents).append("\n");

        long start = System.nanoTime();
        RougeResult avg = new RougeResult();
        for (int i = 0; i < documents.size(); i++) {
            LSASummarizer algorithm = new LSASummarizer();

            long taskStart = System.nanoTime();
            List<String> summary = algorithm.summarizeToSentences(documents.get(i).getSentences(), settings);
            long taskEnd = System.nanoTime();

            String text = TestUtils.toText(summary);
            List<String> refSummaries = new ArrayList<>();

            for (String p : documents.get(i).getSummaries().keySet()){
                refSummaries.add(TestUtils.toText(documents.get(i).getSummaries().get(p)));
            }

            RougeResult perf = Rouge.evaluate(refSummaries, text);
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
