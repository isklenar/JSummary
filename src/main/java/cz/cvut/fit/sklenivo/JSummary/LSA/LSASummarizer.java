package cz.cvut.fit.sklenivo.JSummary.LSA;

import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.Summarizer;
import cz.cvut.fit.sklenivo.JSummary.util.SentenceUtils;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

import java.util.*;

/**
 * Created by ivo on 15.4.2015.
 */
public class LSASummarizer implements Summarizer {
    List<String> terms;
    List<String> sentences;

    @Override
    public String summarize(String input, SummarizationSettings settings) {
        sentences = SentenceUtils.splitToSentences(input);

        SimpleMatrix LSAMatrix = createLSAMatrix(sentences);

        SimpleSVD svd = LSAMatrix.svd();


        return null;
    }

    private SimpleMatrix createLSAMatrix(List<String> sentences) {
        terms = new ArrayList<>(extractTerms(sentences));
        SimpleMatrix matrix = new SimpleMatrix(terms.size(), sentences.size());

        int column = 0;
        for (String sentence : sentences){
            double [] termFrequencyVector = new double[terms.size()];
            for (int i = 0; i < termFrequencyVector.length; i++){
                termFrequencyVector[i] = localWeighting(i, sentence) * globalWeighting(i);
            }

            System.out.println(Arrays.toString(termFrequencyVector));
            matrix.setColumn(column++, 0, termFrequencyVector);
        }

        return matrix;
    }

    private double localWeighting(int i, String sentence) {
        String [] t = sentence.split(" ");
        double count = 0;

        for (String term : t){
            if (terms.get(i).equals(term)){
                count = count + 1;
            }
        }

        return count;
    }

    private double globalWeighting(int i) {
        String term = terms.get(i);
        int n = 0;
        for (String sentence : sentences){
            if (sentence.contains(term)){
                n++;
            }
        }

        return Math.log(((double)sentences.size())/(n));
    }

    private Set<String> extractTerms(List<String> sentences) {
        Set<String> ret = new TreeSet<>();

        for (String sentence : sentences){
            String [] words = sentence.split(" ");
            ret.addAll(Arrays.asList(words));
        }

        return ret;
    }


}
