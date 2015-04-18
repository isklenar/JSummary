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

    public static void main(String [] args) throws InterruptedException {

        final TestEvaluator evaluator = new TestEvaluator();

        final SummarizationSettings settingsCZ = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setStemming(true).setStopWords(true).setRatio(0.3).build();
        final List<String> filesCZ = new ArrayList<>();
        filesCZ.add("resources/TestingFiles/Genetic.cz.xml");
        filesCZ.add("resources/TestingFiles/IsraelPalestineConflict.cz.xml");
        filesCZ.add("resources/TestingFiles/Malaria.cz.xml");
        filesCZ.add("resources/TestingFiles/ScienceAndSociety.cz.xml");

        final SummarizationSettings settingsEN = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setStemming(true).setStopWords(true).setRatio(0.3).build();
        final List<String> filesEN = new ArrayList<>();
        filesEN.add("resources/TestingFiles/Genetic.en.xml");
        filesEN.add("resources/TestingFiles/IsraelPalestineConflict.en.xml");
        filesEN.add("resources/TestingFiles/Malaria.en.xml");
        filesEN.add("resources/TestingFiles/ScienceAndSociety.en.xml");

        evaluator.preLoadFiles(filesCZ);
        evaluator.preLoadFiles(filesEN);

        Thread [] threads = new Thread[7];
        threads[0] = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String file : filesCZ) {
                    List<String> tmp = new ArrayList<>();
                    tmp.add(file);

                    evaluator.testTextRank(settingsCZ, tmp);
                }

                for (String file : filesEN) {
                    List<String> tmp = new ArrayList<>();
                    tmp.add(file);

                    evaluator.testTextRank(settingsEN, tmp);
                }

                for (String file : filesCZ) {
                    List<String> tmp = new ArrayList<>();
                    tmp.add(file);

                    evaluator.testLSA(settingsCZ, tmp);
                }

                for (String file : filesEN) {
                    List<String> tmp = new ArrayList<>();
                    tmp.add(file);

                    evaluator.testLSA(settingsEN, tmp);
                }
            }
        });


        threads[1] = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String file : filesCZ){
                    List<String> tmp = new ArrayList<>();
                    tmp.add(file);

                    evaluator.xValidateBayes(settingsCZ, tmp);
                }
                evaluator.xValidateBayes(settingsCZ, filesCZ);

                for (String file : filesEN){
                    List<String> tmp = new ArrayList<>();
                    tmp.add(file);

                    evaluator.xValidateBayes(settingsEN, tmp);
                }
                evaluator.xValidateBayes(settingsEN, filesEN);
            }
        });

        final int kBound = 10;

        threads[2] = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String file : filesCZ){
                    List<String> tmp = new ArrayList<>();
                    tmp.add(file);

                    evaluator.xValidateKnn(settingsCZ, tmp, kBound, new ManhattanDistance());
                }

                for (String file : filesEN){
                    List<String> tmp = new ArrayList<>();
                    tmp.add(file);

                    evaluator.xValidateKnn(settingsEN, tmp, kBound, new ManhattanDistance());
                }

            }
        });

        threads[3] = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String file : filesCZ){
                    List<String> tmp = new ArrayList<>();
                    tmp.add(file);

                    evaluator.xValidateKnn(settingsCZ, tmp, kBound, new MixedEuclideanDistance());
                }



            }
        });

        threads[4] = new Thread(new Runnable() {
            @Override
            public void run() {
                evaluator.xValidateKnn(settingsCZ, filesCZ, kBound, new MixedEuclideanDistance());
            }
        });

        threads[5] = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String file : filesEN){
                    List<String> tmp = new ArrayList<>();
                    tmp.add(file);

                    evaluator.xValidateKnn(settingsEN, tmp, kBound, new MixedEuclideanDistance());
                }

            }
        });

        threads[6] = new Thread(new Runnable() {
            @Override
            public void run() {
                evaluator.xValidateKnn(settingsEN, filesEN, kBound, new MixedEuclideanDistance());
            }
        });
        long start = System.nanoTime();
        for(Thread t : threads){
            t.start();
        }

        for(Thread t : threads){
            t.join();
        }

        System.out.println(" TOTAL TIME: " + (System.nanoTime() - start) / 1000000000 + " s");
       //SummarizationUI ui = new SummarizationUI();
    }
}
