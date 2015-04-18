package cz.cvut.fit.sklenivo.JSummary.testing;

import cz.cvut.fit.sklenivo.JSummary.LSA.LSASummarizer;
import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.TrainableSummarizer;
import cz.cvut.fit.sklenivo.JSummary.classification.bayes.NaiveBayes;
import cz.cvut.fit.sklenivo.JSummary.classification.knn.KNN;
import cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics.KNNMetric;
import cz.cvut.fit.sklenivo.JSummary.testing.metric.RougeN;
import cz.cvut.fit.sklenivo.JSummary.textrank.TextRank;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ivo on 14.4.2015.
 */
public class TestEvaluator {
    private final static String OUTPUT_FOLDER = "OUTPUT";

    private HashMap<String, List<SummarizableDocument>> documentCache = null;

    public List<SummarizableDocument> readDocument(String filename) {
        List<SummarizableDocument> documents = new ArrayList<>();
        File file = new File(filename);

        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList articles = document.getElementsByTagName("document");
            for (int i = 0; i < articles.getLength(); i++) {
                Map<String, List<String>> summaries = new HashMap<>();
                summaries.put("A", new ArrayList<String>());
                summaries.put("B", new ArrayList<String>());
                summaries.put("C", new ArrayList<String>());
                summaries.put("D", new ArrayList<String>());

                List<String> sentences = new ArrayList<>();

                Node article = articles.item(i);
                if (article.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                NodeList sen = ((Element) article).getElementsByTagName("s");

                for (int j = 2; j < sen.getLength(); j++) {
                    String sentence = sen.item(j).getTextContent().trim();
                    NamedNodeMap attributes = sen.item(j).getAttributes();

                    String tmp = "";
                    if (attributes != null) {
                        tmp = attributes.item(0).toString();
                        if (tmp.startsWith("annotators")) {
                            tmp = tmp.replace("\"", "");
                            tmp = tmp.substring(11);
                            String[] annotators = tmp.trim().split(" ");

                            for (String annotator : annotators) {
                                summaries.get(annotator).add(sentence);
                            }
                        }
                    }
                    sentences.add(sentence);
                }

                documents.add(new SummarizableDocument(sentences, summaries));
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return documents;
    }


    private List<SummarizableDocument> prepareDocuments(List<String> filenames) {
        List<SummarizableDocument> documents = new ArrayList<>();

        for (String file : filenames) {
            documents.addAll(readDocument(file));
        }

        return documents;
    }

    public void testLSA(SummarizationSettings settings, List<String> files){
        StringBuilder log = new StringBuilder();
        log.append("<TEST>\nLSA\n" + "SETTINGS: ").append(settings).append("\n").append("SETTINGS: ").append(settings).append("\n");
        log.append("FILES: ").append(files).append("\n");

        List<SummarizableDocument> documents = retrieveDocuments(files);
        long start = System.nanoTime();
        double avg = 0;
        for (int i = 0; i < documents.size(); i++) {
            LSASummarizer algorithm = new LSASummarizer();

            List<String> summary = algorithm.summarizeToSentences(documents.get(i).getSentences(), settings);

            String text = toText(summary);
            List<String> refSummaries = new ArrayList<>();

            for (String p : documents.get(i).getSummaries().keySet()){
                refSummaries.add(toText(documents.get(i).getSummaries().get(p)));
            }

            double perf = Rouge.evaluate(refSummaries, text, new RougeN(1));
            log.append("TEST ").append(i).append("  Rouge-1: ").append(perf).append("\n");

            avg += perf;
        }

        log.append("AVG ROUGE: ").append(avg / documents.size()).append("\n");
        log.append("TIME: ").append((System.nanoTime() - start) / 1000000).append(" ms\n");
        log.append("</TEST>").append("\n");
        TestUtils.print(log);
    }

    public void testTextRank(SummarizationSettings settings, List<String> files) {
        StringBuilder log = new StringBuilder();
        log.append("<TEST>\nTEXTRANK\n" + "SETTINGS: ").append(settings).append("\n").append("SETTINGS: ").append(settings).append("\n");
        log.append("FILES: ").append(files).append("\n");

        List<SummarizableDocument> documents = retrieveDocuments(files);
        long start = System.nanoTime();
        double avg = 0;
        for (int i = 0; i < documents.size(); i++) {
            TextRank algorithm = new TextRank();

            List<String> summary = algorithm.summarizeToSentences(documents.get(i).getSentences(), settings);

            String text = toText(summary);
            List<String> refSummaries = new ArrayList<>();

            for (String p : documents.get(i).getSummaries().keySet()){
                refSummaries.add(toText(documents.get(i).getSummaries().get(p)));
            }

            double perf = Rouge.evaluate(refSummaries, text, new RougeN(1));
            log.append("TEST ").append(i).append("  Rouge-1: ").append(perf).append("\n");

            avg += perf;
        }

        log.append("AVG ROUGE: ").append(avg / documents.size()).append("\n");
        log.append("TIME: ").append((System.nanoTime() - start) / 1000000).append(" ms\n");
        log.append("</TEST>").append("\n");
        TestUtils.print(log);
    }

    private String toText(List<String> summary) {
        StringBuilder builder = new StringBuilder();

        for (String word : summary){
            builder.append(word).append(" ");
        }

        return builder.toString();
    }

    public void xValidateBayes(SummarizationSettings settings, List<String> files) {
        StringBuilder log = new StringBuilder();
        log.append("<TEST>\nXVALIDATION BAYES\n" + "SETTINGS: ").append(settings).append("\n").append("SETTINGS: ").append(settings).append("\n");
        log.append("FILES: ").append(files).append("\n");
        List<SummarizableDocument> documents = retrieveDocuments(files);

        double avg = 0;

        long start = System.nanoTime();
        for (int i = 0; i < documents.size(); i++) {
            NaiveBayes algorithm = new NaiveBayes();

            double perf = xValidationRound(algorithm, documents, i, settings);
            log.append("TEST ").append(i).append("  Rouge-1: ").append(perf).append("\n");
            avg += perf;
        }

        log.append("AVG ROUGE: ").append(avg / documents.size()).append("\n");
        log.append("TIME: ").append((System.nanoTime() - start) / 1000000).append(" ms\n");
        log.append("</TEST>").append("\n");

        TestUtils.print(log);
    }

    public void xValidateKnn(SummarizationSettings settings, List<String> files, int kBound, KNNMetric metric){
        for (int k = 1; k <= kBound; k++){
            StringBuilder log = new StringBuilder();
            log.append("<TEST>\n").append(k).append("-NN").append(metric.toString()).append("\n").append("SETTINGS: ").append(settings).append("\n");
            log.append("FILES: ").append(files).append("\n");
            List<SummarizableDocument> documents = retrieveDocuments(files);

            long start = System.nanoTime();
            double avg = 0;
            for (int i = 0; i < documents.size(); i++) {
                KNN algorithm = new KNN(k, metric);

                double perf = xValidationRound(algorithm, documents, i, settings);
                log.append("TEST ").append(i).append("  Rouge-1: ").append(perf).append("\n");
                avg += perf;
            }

            log.append("AVG ROUGE: ").append(avg / documents.size()).append("\n");
            log.append("TIME: ").append((System.nanoTime() - start) / 1000000).append(" ms\n");
            log.append("</TEST>").append("\n");
            TestUtils.print(log);
        }


    }

    private double xValidationRound(TrainableSummarizer algorithm, List<SummarizableDocument> documents, int i, SummarizationSettings settings) {
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

        return Rouge.evaluate(refSummaries, output, new RougeN(1));
    }

    private List<SummarizableDocument> retrieveDocuments(List<String> files) {
        List<SummarizableDocument> ret = new ArrayList<>();
        for (String file : files){
            ret.addAll(documentCache.get(file));
        }

        return ret;
    }

    public void preLoadFiles(List<String> files) {
        if (documentCache == null){
            documentCache = new HashMap<>();
        }

        for (String file : files){
            documentCache.put(file, readDocument(file));
        }
    }
}

