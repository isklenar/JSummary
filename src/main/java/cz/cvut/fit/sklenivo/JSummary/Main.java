package cz.cvut.fit.sklenivo.JSummary;

import cz.cvut.fit.sklenivo.JSummary.testing.TestEvaluator;
import cz.cvut.fit.sklenivo.JSummary.util.SummarizationUI;

/**
 * Created by Ivo on 29.1.2015.
 */
public class Main {

    public static void main(String [] args){
        TestEvaluator evaluator = new TestEvaluator();
        evaluator.readDocument("resources/TestingFiles/Genetic.en.xml");
        evaluator.readDocument("resources/TestingFiles/Genetic.cz.xml");


        SummarizationUI ui = new SummarizationUI();
    }
}
