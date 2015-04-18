package cz.cvut.fit.sklenivo.JSummary.testing.test;

import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.testing.Rouge;
import cz.cvut.fit.sklenivo.JSummary.testing.SummarizableDocument;
import cz.cvut.fit.sklenivo.JSummary.testing.TestUtils;
import cz.cvut.fit.sklenivo.JSummary.testing.metric.RougeN;
import cz.cvut.fit.sklenivo.JSummary.textrank.TextRank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivo on 18.4.2015.
 */
public class TextRankTest implements Test {
    SummarizationSettings settings;
    List<SummarizableDocument> documents;
    public TextRankTest(SummarizationSettings settings, List<SummarizableDocument> documents) {
        this.settings = settings;
        this.documents = documents;
    }

    @Override
    public void run() {
        logMessage("TEST: TEXTRANK");
        logMessage("SETTINGS: " + settings);

        double avg = 0;
        for (int i = 0; i < documents.size(); i++) {
            TextRank algorithm = new TextRank();

            List<String> summary = algorithm.summarizeToSentences(documents.get(i).getSentences(), settings);

            String text = TestUtils.toText(summary);
            List<String> refSummaries = new ArrayList<>();

            for (String p : documents.get(i).getSummaries().keySet()){
                refSummaries.add(TestUtils.toText(documents.get(i).getSummaries().get(p)));
            }

            double rougeN = Rouge.evaluate(refSummaries, text, new RougeN(1));


        }


        logMessage("TEST END");
    }

    @Override
    public void logMessage(String message) {

    }
}
