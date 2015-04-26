package cz.cvut.fit.sklenivo.JSummary.testing;

import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.TrainableSummarizer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ivo on 18.4.2015.
 */
public class TestUtils {
    public static String toText(List<String> sentences) {
        StringBuilder builder = new StringBuilder();

        for (String s : sentences){
            builder.append(s).append(" ");
        }

        return builder.toString();
    }

     private static Object lock = new Object();

    public static void print(StringBuilder log){
        synchronized (lock){
            System.out.println(log.toString());

            Calendar cal = Calendar.getInstance();
            cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            System.out.println("Current Time: " + sdf.format(cal.getTime()) );
        }
    }

    public static RougeResult xValidationRound(TrainableSummarizer algorithm, List<SummarizableDocument> documents, int i, SummarizationSettings settings) {
        for (int j = 0; j < documents.size(); j++){
            if (i == j){
                continue;
            }

            String trainingText = toText(documents.get(j).getSentences());
            String summaryText = toText(documents.get(j).getSummaries().get(documents.get(j).getSummaries().keySet().iterator().next()));
            algorithm.addTrainingData(trainingText, summaryText);
        }
        algorithm.train(settings);
        String output = algorithm.summarize(documents.get(i).getSentences(), settings);
        List<String> refSummaries = new ArrayList<>();

        for (String p : documents.get(i).getSummaries().keySet()){
            refSummaries.add(toText(documents.get(i).getSummaries().get(p)));
        }

        return Rouge.evaluate(refSummaries, output);
    }
}
