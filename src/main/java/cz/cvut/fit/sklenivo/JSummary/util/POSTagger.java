package cz.cvut.fit.sklenivo.JSummary.util;

import CzechLemma.CzechLemma;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class POSTagger {
    private String fileName;
    private MaxentTagger tagger = null;
    private Map<String, String> database = null;

    private CzechLemma lemma;

    public POSTagger(String language) {
        if (language.equals(WordDatabases.ENGLISH_LANGUAGE)){
            tagger = new MaxentTagger("resources/StanfordPOS/english-bidirectional-distsim.tagger");
        } else {
            this.fileName = "resources/POS/gwg-cze-latest.xml";
            lemma = new CzechLemma();
            loadDatabase(); // czech
        }

    }

    /**
     * Tags input text by each words part of speach.
     *
     * Tags: _NN noun, _VB verb, _JJ adjective.
     *
     * @param text input text
     * @return tagged text
     */
    public String tagText(String text) {
        String [] words = text.split(" ");
        StringBuilder builder = new StringBuilder();

        for(String word : words){
            String tagged = tag(word);
            builder.append(tagged).append(" ");
        }

        return builder.toString();
    }

    /**
     * Tags a single word.
     * @param word word to tag
     * @return tagged word
     */
    private String tag(String word){
        if (database == null){ //tagging english
            return tagger.tagString(word);
        }

        if (database.containsKey(lemma.lemmatizeWord(word).toLowerCase())){
            return word + "_" + database.get(lemma.lemmatizeWord(word).toLowerCase());
        }

        return word;
    }


    private void loadDatabase() {
        database = new TreeMap<>();
        try {
            File xmlFile = new File(fileName);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList synsets = document.getElementsByTagName("SYNSET");
            for (int i = 0; i < synsets.getLength(); i++){
                Node synset = synsets.item(i);
                if (synset.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) synset;
                    String pos = element.getElementsByTagName("POS").item(0).getTextContent();

                    NodeList synonyms = element.getElementsByTagName("SYNONYM");
                    List<String> words = parseWords(synonyms);

                    for(String word : words){
                        switch (pos){
                            case "n" : pos = "NN"; break;
                            case "v" : pos = "VB"; break;
                            case "a" : pos = "JJ"; break;
                        }
                        database.put(lemma.lemmatizeWord(word.toLowerCase()), pos);
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> parseWords(NodeList synonyms) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < synonyms.getLength(); i++){
            Node node = synonyms.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                NodeList literals = element.getElementsByTagName("LITERAL");

                for (int j = 0; j < literals.getLength(); j++){
                    if (literals.item(j).getNodeType() == Node.ELEMENT_NODE){
                        Element literal = (Element) literals.item(j);
                        String word = literal.getTextContent();
                        if (word != null){
                            int wordsCount = word.split(" ").length;
                            if (wordsCount == 1){
                                ret.add(word);
                            }
                        }

                    }
                }
            }
        }

        return ret;
    }


}
