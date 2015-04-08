package cz.cvut.fit.sklenivo.JSummary.util;


import cz.cvut.fit.sklenivo.JSummary.Summarizer;
import cz.cvut.fit.sklenivo.JSummary.bayes.NaiveBayes;
import cz.cvut.fit.sklenivo.JSummary.evolutionary.EvolutionarySummarizer;
import cz.cvut.fit.sklenivo.JSummary.knn.KNN;
import cz.cvut.fit.sklenivo.JSummary.textrank.TextRank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created by ivo on 11.9.2014.
 */
public class SummarizationUI extends JFrame {
    private HashMap<String, Summarizer> dispatch;

    private JRadioButton stemmer;
    private JRadioButton nlp;
    private JRadioButton wordnet;
    private JRadioButton stopwords;

    private JComboBox algorithm;
    private JComboBox language;

    private JTextField kTextBox;

    public SummarizationUI(){
        dispatch = new HashMap<>();
        dispatch.put("TextRank", new TextRank());
        dispatch.put("Naive Bayes", new NaiveBayes());
        dispatch.put("Evolutionary", new EvolutionarySummarizer());
        dispatch.put("kNN", new KNN());

        initUI();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("TextRank");
        this.setResizable(true);
        this.setSize(500,600);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void initOptionElements(){
        nlp = new JRadioButton();
        nlp.setText("NLP tokenizer");
        nlp.setSelected(true);

        stemmer = new JRadioButton();
        stemmer.setText("Stemming");
        stemmer.setSelected(true);

        wordnet = new JRadioButton();
        wordnet.setText("WordNet");
        wordnet.setSelected(true);

        stopwords = new JRadioButton();
        stopwords.setText("Stop Words");
        stopwords.setSelected(true);

        kTextBox = new JTextField();
        kTextBox.setText("1");
    }

    private void initComboBoxes(){
        algorithm = new JComboBox();
        algorithm.addItem("TextRank");
        algorithm.addItem("Naive Bayes");
        algorithm.addItem("kNN");
        algorithm.addItem("Evolutionary");

        language = new JComboBox();
        language.addItem("CZECH");
        language.addItem("ENGLISH");
    }

    private void initUI() {
        Container pane = getContentPane();
        BoxLayout gl = new BoxLayout(pane,BoxLayout.PAGE_AXIS);
        pane.setLayout(gl);

        final JTextArea input = new JTextArea(20,40);
        final JScrollPane inputScrollPane = new JScrollPane(input);
        input.setLineWrap(true);
        input.setWrapStyleWord(true);

        final JTextArea output = new JTextArea(15,40);
        final JScrollPane outputScrollPane = new JScrollPane(output);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);

        final JSlider slider = new JSlider(0, 100);


        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,1)); //2 rows 1 columns

        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        initComboBoxes();
        initOptionElements();

        final JButton summarize = new JButton("Summarize");
        summarize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long start = System.nanoTime();
                String inputText = input.getText();
                double percentage = slider.getValue() / 100.0;
                String out;

                out = dispatch.get(algorithm.getSelectedItem()).summarize(inputText,
                        percentage,
                        stemmer.isSelected(),
                        false,
                        false,
                        false,
                        ((String) language.getSelectedItem()).toLowerCase());


                output.setText(out);
                System.out.println((System.nanoTime() - start) / 1_000_000 + "ms");
            }
        });

        final JButton train = new JButton("Train");
        train.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (algorithm.getSelectedItem().toString().equals("Naive Bayes")){
                    NaiveBayes bayes = (NaiveBayes) dispatch.get("Naive Bayes");
                    bayes.train(input.getText(), output.getText());
                    System.out.println("Trained");
                }

                if (algorithm.getSelectedItem().toString().equals("kNN")){
                    dispatch.put("kNN", new KNN(Integer.parseInt(kTextBox.getText())));
                    KNN knn = (KNN) dispatch.get("kNN");
                    knn.train(input.getText(), output.getText());
                    System.out.println("Trained");
                }
            }
        });

        JPanel firstRow = new JPanel(new GridLayout(1,5)); //1 row 3 columns, algorithm, button, train, language, k

        firstRow.add(algorithm);
        firstRow.add(summarize);
        firstRow.add(train);
        firstRow.add(language);
        firstRow.add(kTextBox);
        panel.add(firstRow);

        JPanel secondRow = new JPanel(new GridLayout(1, 4)); //nlp, stemmer, wordnet, stopwords

        secondRow.add(nlp);
        secondRow.add(stemmer);
        secondRow.add(wordnet);
        secondRow.add(stopwords);
        panel.add(secondRow);

        this.add(inputScrollPane);
        this.add(slider);
        this.add(panel);
        this.add(outputScrollPane);
    }
}
