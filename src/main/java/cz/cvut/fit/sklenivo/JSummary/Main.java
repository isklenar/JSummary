package cz.cvut.fit.sklenivo.JSummary;

import cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics.ManhattanDistance;
import cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics.MixedEuclideanDistance;
import cz.cvut.fit.sklenivo.JSummary.testing.TestEvaluator;
import cz.cvut.fit.sklenivo.JSummary.util.WordDatabases;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivo on 29.1.2015.
 */
public class Main {

    public static void main(String [] args){

        TestEvaluator evaluator = new TestEvaluator();

        SummarizationSettings settingsCZ = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setStemming(false).setStopWords(false).setRatio(0.3).build();
        List<String> filesCZ = new ArrayList<>();
        filesCZ.add("resources/TestingFiles/Genetic.cz.xml");
        filesCZ.add("resources/TestingFiles/IsraelPalestineConflict.cz.xml");
        filesCZ.add("resources/TestingFiles/Malaria.cz.xml");
        filesCZ.add("resources/TestingFiles/ScienceAndSociety.cz.xml");

        SummarizationSettings settingsEN = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setStemming(false).setStopWords(false).setRatio(0.3).build();
        List<String> filesEN = new ArrayList<>();
        filesEN.add("resources/TestingFiles/Genetic.en.xml");
        filesEN.add("resources/TestingFiles/IsraelPalestineConflict.en.xml");
        filesEN.add("resources/TestingFiles/Malaria.en.xml");
        filesEN.add("resources/TestingFiles/ScienceAndSociety.en.xml");

        for (String file : filesCZ){
            System.out.println("   " + file.toUpperCase());
            List<String> tmp = new ArrayList<>();
            tmp.add(file);

            evaluator.testTextRank(settingsCZ, tmp);
        }

        for (String file : filesEN){
            System.out.println("   " + file.toUpperCase());
            List<String> tmp = new ArrayList<>();
            tmp.add(file);

            evaluator.testTextRank(settingsEN, tmp);
        }


        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------------------------------------");

        for (String file : filesCZ){
            System.out.println("   " + file.toUpperCase());
            List<String> tmp = new ArrayList<>();
            tmp.add(file);

            evaluator.testLSA(settingsCZ, tmp);
        }

        for (String file : filesEN){
            System.out.println("   " + file.toUpperCase());
            List<String> tmp = new ArrayList<>();
            tmp.add(file);

            evaluator.testLSA(settingsEN, tmp);
        }


        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------------------------------------");
        for (String file : filesCZ){
            System.out.println("   " + file.toUpperCase());
            List<String> tmp = new ArrayList<>();
            tmp.add(file);

            evaluator.xValidateBayes(settingsCZ, tmp);
        }
        evaluator.xValidateBayes(settingsCZ, filesCZ);

        for (String file : filesEN){
            System.out.println("   " + file.toUpperCase());
            List<String> tmp = new ArrayList<>();
            tmp.add(file);

            evaluator.xValidateBayes(settingsEN, tmp);
        }
        evaluator.xValidateBayes(settingsEN, filesEN);

        for (String file : filesCZ){
            System.out.println("   " + file.toUpperCase());
            List<String> tmp = new ArrayList<>();
            tmp.add(file);

            evaluator.xValidateKnn(settingsCZ, tmp, 20, new ManhattanDistance());
        }

        evaluator.xValidateKnn(settingsCZ, filesCZ, 20, new ManhattanDistance());

        for (String file : filesEN){
            System.out.println("   " + file.toUpperCase());
            List<String> tmp = new ArrayList<>();
            tmp.add(file);

            evaluator.xValidateKnn(settingsEN, tmp, 20, new ManhattanDistance());
        }
        evaluator.xValidateKnn(settingsEN, filesEN, 20, new ManhattanDistance());

        System.out.println("EUCLIDIEAN");

        for (String file : filesCZ){
            System.out.println("   " + file.toUpperCase());
            List<String> tmp = new ArrayList<>();
            tmp.add(file);

            evaluator.xValidateKnn(settingsCZ, tmp, 20, new MixedEuclideanDistance());
        }

        evaluator.xValidateKnn(settingsCZ, filesCZ, 20, new MixedEuclideanDistance());

        for (String file : filesEN){
            System.out.println("   " + file.toUpperCase());
            List<String> tmp = new ArrayList<>();
            tmp.add(file);

            evaluator.xValidateKnn(settingsEN, tmp, 20, new MixedEuclideanDistance());
        }
        evaluator.xValidateKnn(settingsEN, filesEN, 20, new MixedEuclideanDistance());




        //evaluator.testNonTrainable(new TextRank(),settings,files);
        //evaluator.testNonTrainable(new LSASummarizer(), settings, files);


       //SummarizationUI ui = new SummarizationUI();
    }
}
