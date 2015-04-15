package cz.cvut.fit.sklenivo.JSummary.testing;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ivo on 14.4.2015.
 */
public class TestEvaluator {
    public List<SummarizableDocument> readDocument(String filename){
        List<SummarizableDocument> documents = new ArrayList<>();
        File file = new File(filename);

        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList articles = document.getElementsByTagName("document");
            for (int i = 0; i < articles.getLength(); i++){
                Map<String, List<String>> summaries = new HashMap<>();
                summaries.put("A", new ArrayList<String>());
                summaries.put("B", new ArrayList<String>());
                summaries.put("C", new ArrayList<String>());
                summaries.put("D", new ArrayList<String>());

                List<String> sentences = new ArrayList<>();

                Node article = articles.item(i);
                if (article.getNodeType() != Node.ELEMENT_NODE){
                    continue;
                }
                NodeList sen = ((Element) article).getElementsByTagName("s");

                System.out.println(sen.getLength());
                for (int j = 2; j < sen.getLength(); j++){
                    String sentence = sen.item(j).getTextContent().trim();
                    NamedNodeMap attributes = sen.item(j).getAttributes();

                    String tmp = "";
                    if (attributes != null){
                        tmp = attributes.item(0).toString();
                        if (tmp.startsWith("annotators")){
                            tmp = tmp.replace("\"","");
                            tmp = tmp.substring(11);
                            String[] annotators = tmp.trim().split(" ");

                            for (String annotator : annotators){
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

        System.out.println(documents.size());

        for(SummarizableDocument document : documents){
            System.out.println("-- " + document.getSentences().size());
            for (String annotator : document.getSummaries().keySet()){
                System.out.println("  *-- " + annotator + ": " + document.getSummaries().get(annotator).size());
            }
        }

        return documents;
    }


}
