package cz.cvut.fit.sklenivo.JSummary.testing;

import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
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

    public void testNonTrainable(TestableSummarizer algorithm, SummarizationSettings settings, List<String> files) {
        System.out.println("STARTING TEST: " + algorithm);
        System.out.println("SETTINGS: " + settings);

        List<SummarizableDocument> documents = prepareDocuments(files);
        String prefix = OUTPUT_FOLDER + "/" + algorithm.toString();
        File file = new File(prefix);
        file.mkdirs();
        prefix = prefix + "/";
        for (int i = 0; i < documents.size(); i++) {

            exportSummaries(documents.get(i), i, prefix);
            exportOriginal(documents.get(i), i, prefix);
            List<String> summary = algorithm.summarizeToSentences(documents.get(i).getSentences(), settings);
            exportSummary(summary, i, prefix);
            System.out.println("TEST #" + i);
        }

        System.out.println("TEST END");
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
}

