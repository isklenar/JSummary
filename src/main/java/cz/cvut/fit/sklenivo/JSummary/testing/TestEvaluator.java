package cz.cvut.fit.sklenivo.JSummary.testing;

import cz.cvut.fit.sklenivo.JSummary.LSA.LSASummarizer;
import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.TrainableSummarizer;
import cz.cvut.fit.sklenivo.JSummary.classification.bayes.NaiveBayes;
import cz.cvut.fit.sklenivo.JSummary.classification.knn.KNN;
import cz.cvut.fit.sklenivo.JSummary.classification.knn.metrics.KNNMetric;
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
        System.out.println("STARTING TEST: LSA") ;
        System.out.println("SETTINGS: " + settings.toString());
        System.out.println();

        List<SummarizableDocument> documents = prepareDocuments(files);
        String prefix = OUTPUT_FOLDER + "/";// + algorithm.toString();
        File file = new File(prefix);
        file.mkdirs();
        prefix = prefix + "/";

        double avg = 0;
        for (int i = 0; i < documents.size(); i++) {
            LSASummarizer algorithm = new LSASummarizer();
            System.out.println("TEST #" + i);
            exportSummaries(documents.get(i), i, prefix);
            exportOriginal(documents.get(i), i, prefix);
            List<String> summary = algorithm.summarizeToSentences(documents.get(i).getSentences(), settings);

            String text = toText(summary);
            List<String> refSummaries = new ArrayList<>();

            for (String p : documents.get(i).getSummaries().keySet()){
                refSummaries.add(toText(documents.get(i).getSummaries().get(p)));
            }

            double rouge = ROUGE.rougeN(1, refSummaries, text);
            System.out.println(" *- " + rouge);
            exportSummary(summary, i, prefix);

            avg += rouge;
        }

        System.out.println("AVG ROUGE: " + avg/documents.size());

        System.out.println("TEST END");
    }

    public void testTextRank(SummarizationSettings settings, List<String> files) {
        System.out.println("STARTING TEST: TEXTRANK") ;
        System.out.println("SETTINGS: " + settings);

        List<SummarizableDocument> documents = prepareDocuments(files);
        String prefix = OUTPUT_FOLDER + "/";// + algorithm.toString();
        File file = new File(prefix);
        file.mkdirs();
        prefix = prefix + "/";

        List<String> outputs = new ArrayList<>();
        double avg = 0;
        for (int i = 0; i < documents.size(); i++) {
            TextRank algorithm = new TextRank();
            System.out.println("TEST #" + i);
            exportSummaries(documents.get(i), i, prefix);
            exportOriginal(documents.get(i), i, prefix);
            List<String> summary = algorithm.summarizeToSentences(documents.get(i).getSentences(), settings);

            String text = toText(summary);
            List<String> refSummaries = new ArrayList<>();

            for (String p : documents.get(i).getSummaries().keySet()){
                refSummaries.add(toText(documents.get(i).getSummaries().get(p)));
            }

            double rouge = ROUGE.rougeN(1, refSummaries, text);
            System.out.println(" *- " + rouge);
            exportSummary(summary, i, prefix);

            avg += rouge;
        }

        System.out.println("AVG ROUGE: " + avg/documents.size());

        System.out.println("TEST END");
    }

    private String toText(List<String> summary) {
        StringBuilder builder = new StringBuilder();

        for (String word : summary){
            builder.append(word).append(" ");
        }

        return builder.toString();
    }


    private void exportSummary(List<String> summary, int n, String prefix) {
        try {
            PrintWriter writer = new PrintWriter(prefix + n + "_O.txt", "UTF-8");

            for (String sentence : summary){
                writer.println(sentence);
            }

            writer.close();
        } catch(FileNotFoundException | UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    private void exportOriginal(SummarizableDocument summarizableDocument, int n, String prefix) {
        try {
            PrintWriter writer = new PrintWriter(prefix + n + ".txt", "UTF-8");

            for (String sentence : summarizableDocument.getSentences()){
                writer.println(sentence);
            }

            writer.close();
        } catch(FileNotFoundException | UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    private void exportSummaries(SummarizableDocument summarizableDocument, int n, String prefix) {
        try {
            for (String user : summarizableDocument.getSummaries().keySet()) {
                PrintWriter writer = new PrintWriter(prefix + n + "_" + user + ".txt", "UTF-8");
                for (String sentence : summarizableDocument.getSummaries().get(user)){
                    writer.println(sentence);
                }
                writer.close();
            }
        } catch(FileNotFoundException | UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public void xValidateBayes(SummarizationSettings settings, List<String> files) {
        System.out.println("STARTING TEST: XVALIDATION BAYES") ;
        System.out.println("SETTINGS: " + settings);

        List<SummarizableDocument> documents = prepareDocuments(files);

        double avg = 0;
        for (int i = 0; i < documents.size(); i++) {

            System.out.print("TEST " + i);
            NaiveBayes algorithm = new NaiveBayes();

            double perf = xValidationRound(algorithm, documents, i, settings);
            System.out.println("  Rouge-1: " + perf);
            avg += perf;
        }

        System.out.println("AVG ROUGE: " + avg/documents.size());

        System.out.println("TEST END");
    }

    public void xValidateKnn(SummarizationSettings settings, List<String> files, int kBound, KNNMetric metric){
        for (int k = 1; k <= kBound; k++){
            System.out.println("STARTING TEST: " + k + "-NN") ;
            System.out.println("SETTINGS: " + settings);

            List<SummarizableDocument> documents = prepareDocuments(files);

            double avg = 0;
            for (int i = 0; i < documents.size(); i++) {

                System.out.print("TEST " + i);
                KNN algorithm = new KNN(k, metric);

                double perf = xValidationRound(algorithm, documents, i, settings);
                System.out.println("  Rouge-1: " + perf);
                avg += perf;
            }

            System.out.println("AVG ROUGE: " + avg/documents.size());

            System.out.println("TEST END");
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
        double rouge = ROUGE.rougeN(1, refSummaries, output);

        return rouge;
    }
}

