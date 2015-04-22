package cz.cvut.fit.sklenivo.JSummary;

import cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics.CosineDistance;
import cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics.ManhattanDistance;
import cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics.MixedEuclideanDistance;
import cz.cvut.fit.sklenivo.JSummary.testing.SummarizableDocument;
import cz.cvut.fit.sklenivo.JSummary.testing.TestDocumentCache;
import cz.cvut.fit.sklenivo.JSummary.testing.test.TestBayes;
import cz.cvut.fit.sklenivo.JSummary.testing.test.TestKNN;
import cz.cvut.fit.sklenivo.JSummary.testing.test.TestLSA;
import cz.cvut.fit.sklenivo.JSummary.testing.test.TestTextRank;
import cz.cvut.fit.sklenivo.JSummary.util.WordDatabases;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivo on 29.1.2015.
 */
public class Main {

    public static void main(String [] args) throws InterruptedException {

        final TestDocumentCache cache = new TestDocumentCache();


        final List<String> filesCZ = new ArrayList<>();
        filesCZ.add("resources/TestingFiles/Genetic.cz.xml");
        filesCZ.add("resources/TestingFiles/IsraelPalestineConflict.cz.xml");
        filesCZ.add("resources/TestingFiles/Malaria.cz.xml");
        filesCZ.add("resources/TestingFiles/ScienceAndSociety.cz.xml");

        final List<String> filesEN = new ArrayList<>();
        filesEN.add("resources/TestingFiles/Genetic.en.xml");
        filesEN.add("resources/TestingFiles/IsraelPalestineConflict.en.xml");
        filesEN.add("resources/TestingFiles/Malaria.en.xml");
        filesEN.add("resources/TestingFiles/ScienceAndSociety.en.xml");

        cache.preLoadFiles(filesCZ);
        cache.preLoadFiles(filesEN);

        /*textRankCZ(cache.retrieveDocuments(filesCZ));
        textRankEN(cache.retrieveDocuments(filesEN));*/

        LSACZ(cache.retrieveDocuments(filesCZ));
        LSAEN(cache.retrieveDocuments(filesEN));

        bayesEN(cache.retrieveDocuments(filesEN));
        bayesCZ(cache.retrieveDocuments(filesCZ));

        /*KNNEN(cache.retrieveDocuments(filesEN), 10);
        KNNCZ(cache.retrieveDocuments(filesCZ), 10);*/


        //SummarizationUI ui = new SummarizationUI();
    }

    private static void bayesCZ(List<SummarizableDocument> documents) throws InterruptedException {
        System.out.println("CZ BAYES TESTS");
        List<Thread> threads = new ArrayList<>();

        final SummarizationSettings settings = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setNormalization(false).build();
        threads.add(new Thread(new TestBayes(settings, documents)));

        final SummarizationSettings settingsNO = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setNormalization(true).build();
        threads.add(new Thread(new TestBayes(settingsNO, documents)));

        long start = System.nanoTime();
        for(Thread t : threads){
            t.start();
        }

        for(Thread t : threads){
            t.join();
        }

        System.out.println(" TOTAL TIME: " + (System.nanoTime() - start) / 1000000000 + " s");
    }
    private static void bayesEN(List<SummarizableDocument> documents) throws InterruptedException {
        System.out.println("EN BAYES TESTS");
        List<Thread> threads = new ArrayList<>();

        final SummarizationSettings settings = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setNormalization(false).build();
        threads.add(new Thread(new TestBayes(settings, documents)));

        final SummarizationSettings settingsNO = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setNormalization(true).build();
        threads.add(new Thread(new TestBayes(settingsNO, documents)));

        long start = System.nanoTime();
        for(Thread t : threads){
            t.start();
        }

        for(Thread t : threads){
            t.join();
        }

        System.out.println(" TOTAL TIME: " + (System.nanoTime() - start) / 1000000000 + " s");
    }

    private static void KNNCZ(List<SummarizableDocument> documents, int kBound) throws InterruptedException {
        System.out.println("CZ KNN TESTS");
        List<Thread> threads = new ArrayList<>();

        final SummarizationSettings settings = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setNormalization(false).build();
        threads.add(new Thread(new TestKNN(documents, kBound, new ManhattanDistance(), settings)));
        threads.add(new Thread(new TestKNN(documents, kBound, new MixedEuclideanDistance(), settings)));
        threads.add(new Thread(new TestKNN(documents, kBound, new CosineDistance(), settings)));

        final SummarizationSettings settingsNO = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setNormalization(true).build();
        threads.add(new Thread(new TestKNN(documents, kBound, new ManhattanDistance(), settingsNO)));
        threads.add(new Thread(new TestKNN(documents, kBound, new MixedEuclideanDistance(), settingsNO)));
        threads.add(new Thread(new TestKNN(documents, kBound, new CosineDistance(), settingsNO)));

        long start = System.nanoTime();
        for(Thread t : threads){
            t.start();
        }

        for(Thread t : threads){
            t.join();
        }

        System.out.println(" TOTAL TIME: " + (System.nanoTime() - start) / 1000000000 + " s");
    }

    private static void KNNEN(List<SummarizableDocument> documents, int kBound) throws InterruptedException {
        System.out.println("EN KNN TESTS");
        List<Thread> threads = new ArrayList<>();

        final SummarizationSettings settings = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setNormalization(false).build();
        threads.add(new Thread(new TestKNN(documents, kBound, new ManhattanDistance(), settings)));
        threads.add(new Thread(new TestKNN(documents, kBound, new MixedEuclideanDistance(), settings)));
        threads.add(new Thread(new TestKNN(documents, kBound, new CosineDistance(), settings)));


        final SummarizationSettings settingsNO = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setNormalization(true).build();
        threads.add(new Thread(new TestKNN(documents, kBound, new ManhattanDistance(), settingsNO)));
        threads.add(new Thread(new TestKNN(documents, kBound, new MixedEuclideanDistance(), settingsNO)));
        threads.add(new Thread(new TestKNN(documents, kBound, new CosineDistance(), settingsNO)));

        long start = System.nanoTime();
        for(Thread t : threads){
            t.start();
        }

        for(Thread t : threads){
            t.join();
        }

        System.out.println(" TOTAL TIME: " + (System.nanoTime() - start) / 1000000000 + " s");
    }

    private static void textRankEN(List<SummarizableDocument> documents) throws InterruptedException {
        System.out.println("EN TEXTRANK TESTS");
        List<Thread> threads = new ArrayList<>();

        final SummarizationSettings settings = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setStemming(false).setStopWords(false).setRatio(0.3).build();
        threads.add(new Thread(new TestTextRank(settings, documents)));

        final SummarizationSettings settingsST = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setStemming(true).setStopWords(false).setRatio(0.3).build();
        threads.add(new Thread(new TestTextRank(settingsST, documents)));


        final SummarizationSettings settingsSW = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setStemming(false).setStopWords(true).setRatio(0.3).build();
        threads.add(new Thread(new TestTextRank(settingsSW, documents)));


        final SummarizationSettings settingsST_SW = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setStemming(true).setStopWords(true).setRatio(0.3).build();
        threads.add(new Thread(new TestTextRank(settingsST_SW, documents)));

        long start = System.nanoTime();
        for(Thread t : threads){
            t.start();
        }

        for(Thread t : threads){
            t.join();
        }

        System.out.println(" TOTAL TIME: " + (System.nanoTime() - start) / 1000000000 + " s");
    }


    private static void textRankCZ(List<SummarizableDocument> documents) throws InterruptedException {
        System.out.println("CZ TEXTRANK TESTS");
        List<Thread> threads = new ArrayList<>();

        final SummarizationSettings settings = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setStemming(false).setStopWords(false).setRatio(0.3).build();
        threads.add(new Thread(new TestTextRank(settings, documents)));

        final SummarizationSettings settingsST = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setStemming(true).setStopWords(false).setRatio(0.3).build();
        threads.add(new Thread(new TestTextRank(settingsST, documents)));


        final SummarizationSettings settingsSW = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setStemming(false).setStopWords(true).setRatio(0.3).build();
        threads.add(new Thread(new TestTextRank(settingsSW, documents)));


        final SummarizationSettings settingsST_SW = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setStemming(true).setStopWords(true).setRatio(0.3).build();
        threads.add(new Thread(new TestTextRank(settingsST_SW, documents)));



        long start = System.nanoTime();
        for(Thread t : threads){
            t.start();
        }

        for(Thread t : threads){
            t.join();
        }

        System.out.println(" TOTAL TIME: " + (System.nanoTime() - start) / 1000000000 + " s");
    }


    private static void LSACZ(List<SummarizableDocument> documents) throws InterruptedException {
        System.out.println("CZ LSA TESTS");
        List<Thread> threads = new ArrayList<>();

        final SummarizationSettings settings = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setStemming(false).setStopWords(false).setRatio(0.3).build();
        threads.add(new Thread(new TestLSA(settings, documents)));

        final SummarizationSettings settingsST = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setStemming(true).setStopWords(false).setRatio(0.3).build();
        threads.add(new Thread(new TestLSA(settingsST, documents)));


        final SummarizationSettings settingsSW = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setStemming(false).setStopWords(true).setRatio(0.3).build();
        threads.add(new Thread(new TestLSA(settingsSW, documents)));


        final SummarizationSettings settingsST_SW = new SummarizationSettingsBuilder().setLanguage(WordDatabases.CZECH_LANGUAGE).setStemming(true).setStopWords(true).setRatio(0.3).build();
        threads.add(new Thread(new TestLSA(settingsST_SW, documents)));



        long start = System.nanoTime();
        for(Thread t : threads){
            t.start();
        }

        for(Thread t : threads){
            t.join();
        }

        System.out.println(" TOTAL TIME: " + (System.nanoTime() - start) / 1000000000 + " s");
    }

    private static void LSAEN(List<SummarizableDocument> documents) throws InterruptedException {
        System.out.println("EN LSA TESTS");
        List<Thread> threads = new ArrayList<>();

        final SummarizationSettings settings = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setStemming(false).setStopWords(false).setRatio(0.3).build();
        threads.add(new Thread(new TestLSA(settings, documents)));

        final SummarizationSettings settingsST = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setStemming(true).setStopWords(false).setRatio(0.3).build();
        threads.add(new Thread(new TestLSA(settingsST, documents)));


        final SummarizationSettings settingsSW = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setStemming(false).setStopWords(true).setRatio(0.3).build();
        threads.add(new Thread(new TestLSA(settingsSW, documents)));


        final SummarizationSettings settingsST_SW = new SummarizationSettingsBuilder().setLanguage(WordDatabases.ENGLISH_LANGUAGE).setStemming(true).setStopWords(true).setRatio(0.3).build();
        threads.add(new Thread(new TestLSA(settingsST_SW, documents)));



        long start = System.nanoTime();
        for(Thread t : threads){
            t.start();
        }

        for(Thread t : threads){
            t.join();
        }

        System.out.println(" TOTAL TIME: " + (System.nanoTime() - start) / 1000000000 + " s");
    }
}
