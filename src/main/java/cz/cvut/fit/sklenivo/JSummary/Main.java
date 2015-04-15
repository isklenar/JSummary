package cz.cvut.fit.sklenivo.JSummary;

import cz.cvut.fit.sklenivo.JSummary.LSA.LSASummarizer;
import cz.cvut.fit.sklenivo.JSummary.testing.TestEvaluator;
import cz.cvut.fit.sklenivo.JSummary.textrank.TextRank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivo on 29.1.2015.
 */
public class Main {

    public static void main(String [] args){
        TestEvaluator evaluator = new TestEvaluator();

        SummarizationSettings settings = new SummarizationSettingsBuilder().setLanguage("ENGLISH").setStemming(false).setRatio(0.3).build();
        List<String> files = new ArrayList<>();
        files.add("resources/TestingFiles/Genetic.en.xml");
        evaluator.testNonTrainable(new TextRank(),settings,files);
        evaluator.testNonTrainable(new LSASummarizer(), settings, files);


       // SummarizationUI ui = new SummarizationUI();
    }
}
