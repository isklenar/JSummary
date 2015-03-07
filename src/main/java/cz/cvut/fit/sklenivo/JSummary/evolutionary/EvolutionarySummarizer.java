package cz.cvut.fit.sklenivo.JSummary.evolutionary;

import cz.cvut.fit.sklenivo.JSummary.Summarizer;
import org.tartarus.snowball.SnowballStemmer;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ivo on 15.2.2015.
 */
public class EvolutionarySummarizer implements Summarizer {

    private Set<String> terms = new TreeSet<>();
    private ArrayList<EvolutionarySentence> documentSentences = new ArrayList<>();

    private ArrayList<ArrayList<EvolutionarySentence>> clusters = new ArrayList<>();

    private ArrayList<Chromosome> chromosomes = new ArrayList<>();

    private double scalingFactor = 0.7;
    private double crossRate = 0.4;
    private double [] randomValues;

    int populationSize;
    int clusterSize;

    @Override
    public String summarize(String input, int percentage, boolean stemming, boolean wordNet, boolean stopWords, boolean useNLP, String language) {
        processInput(input, stemming, language);
        initPopulation();

        System.out.println("Pop: " + populationSize + "  Clusters: " + clusterSize);

        for (int i = 0; i < chromosomes.size(); i++){
            System.out.println(chromosomes.get(i).getData());
        }

        System.out.println("T: " + createTrialOffspring(chromosomes.get(0)).getData());

        return null;
    }

    private void initPopulation() {
        Random random = new Random();
        populationSize = random.nextInt(documentSentences.size()/2) + documentSentences.size()/2;
        clusterSize = populationSize;
        for (int i = 0; i < populationSize; i++){
            chromosomes.add(randomChromosome(clusterSize));
        }
        randomValues = new double[documentSentences.size()];

        for (int i = 0; i < randomValues.length; i++){
            randomValues[i]  = random.nextDouble();
        }
    }

    private Chromosome createTrialOffspring(Chromosome parent){
        Random random = new Random();
        int firstIndex = random.nextInt(chromosomes.size());
        int secondIndex = random.nextInt(chromosomes.size());
        int thirdIndex = random.nextInt(chromosomes.size());

        while (firstIndex == secondIndex || secondIndex == thirdIndex || firstIndex == thirdIndex){
            firstIndex = random.nextInt(chromosomes.size());
            secondIndex = random.nextInt(chromosomes.size());
            thirdIndex = random.nextInt(chromosomes.size());
        }

        ArrayList<Integer> data = new ArrayList<>();
        int i = 0;
        for (Integer item : parent.getData()){
            double chance = random.nextDouble();
            if (randomValues[i] < crossRate){
                int result = chromosomes.get(firstIndex).getData().get(i);
                int scaled = (int) (scalingFactor *
                        (chromosomes.get(secondIndex).getData().get(i)
                                - chromosomes.get(thirdIndex).getData().get(i)));

                result = result + scaled;
                data.add(result);

            } else {
                data.add(item);
            }
            i++;
        }

        return new Chromosome(data);
    }

    private Chromosome randomChromosome(int clusters) {
        ArrayList<Integer> data;
        Random random = new Random();

        do {
            data = new ArrayList<>();
            for (int i = 0; i < documentSentences.size(); i++){
                data.add(random.nextInt(clusters) + 1);
            }
        } while (isValid(data, clusters));


        return new Chromosome(data);
    }

    private boolean isValid(ArrayList<Integer> data, int clusters) {
        int [] occurences = new int[clusters + 1];

        for (Integer item : data){
            occurences[item]++;
        }

        boolean valid = true;
        for (Integer item : occurences){
            if (item == 0){
                valid = false;
            }
        }

        return valid;
    }

    private void processInput(String input, boolean stemming, String language) {
        String [] sentences = input.split("\\.");

        for (String sentence : sentences){
            ArrayList<String> sentenceTerms = processSentence(sentence, stemming, language);
            terms.addAll(sentenceTerms);
            documentSentences.add(new EvolutionarySentence(sentenceTerms));
        }
    }

    private ArrayList<String> processSentence(String sentence, boolean stemming, String language) {
        SnowballStemmer stemmer = null;
        try {
            //create a stemmer for specified language
            stemmer = (SnowballStemmer) Class.forName("org.tartarus.snowball.ext." + language + "Stemmer").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println("Error creating stemmer for language " + language);
            e.printStackTrace();
            stemming = false;
        }

        String [] sentenceTerms = sentence.split(" ");
        ArrayList<String> ret = new ArrayList<>();

        for (String term : sentenceTerms){
            if (stemming){
                stemmer.setCurrent(term);
                stemmer.stem();
                ret.add(stemmer.getCurrent());
            } else {
                ret.add(term);
            }
        }

        return ret;
    }

    private double objectiveFunction(){
        return Math.pow(1 + sigm(F1()), F2());
    }

    private double F2() {
        int sum = 0;
        for (int i = 0; i < clusters.size() - 1; i++){
            double innerSum = 0;
            for (int j = i+1; j < clusters.size(); j++){
                int accumulator = 0;
                for (EvolutionarySentence s1 : clusters.get(i)){
                    for (EvolutionarySentence s2 : clusters.get(j)){
                        accumulator += similarityNGDSentence(s1, s2);
                    }
                }
                accumulator *= (1/clusters.get(j).size());
                innerSum += accumulator;
            }

            sum += innerSum;
            sum *= (1/clusters.get(i).size());
        }

        return sum;
    }

    private double F1() {
        double sum = 0;
        for (int i = 0; i < clusters.size(); i++){
            int innerSum = 0;
            for (EvolutionarySentence s1 : clusters.get(i)){
                for (EvolutionarySentence s2 : clusters.get(i)){
                    innerSum += similarityNGDSentence(s1, s2);
                }
            }

            innerSum *= clusters.size();
            sum += innerSum;
        }

        return sum;
    }

    private double sigm(double z){
        return 1/(1 + Math.exp(-z));
    }

    private double similarityNGDSentence(EvolutionarySentence s1, EvolutionarySentence s2){
        double bottom = s1.getTerms().size() * s2.getTerms().size();

        double top = 0.0;

        for (String term1 : s1.getTerms()){
            for (String term2 : s2.getTerms()){
                top += similarityNGDTerm(term1, term2);
            }
        }

        return top/bottom;
    }

    private double similarityNGDTerm(String term1, String term2){
        return Math.exp(-NGD(term1, term2));
    }

    private double NGD(String term1, String term2) {
        int sentencesTerm1 = countTerm(term1);
        int sentencesTerm2 = countTerm(term2);
        int sentencesTerm12 = countTerms(term1, term2);

        double top = Math.max(Math.log(sentencesTerm1), Math.log(sentencesTerm2)) - Math.log(sentencesTerm12);
        double bottom = Math.log(documentSentences.size()) - Math.min(Math.log(sentencesTerm1), Math.log(sentencesTerm2));

        return top/bottom;
    }

    private int countTerm(String term1) {
        return countTerms(term1, null);
    }

    private int countTerms(String term1, String term2){
        int count = 0;
        if (term2 != null){
            for (EvolutionarySentence sentence : documentSentences){
                if (sentence.getTerms().contains(term1) && sentence.getTerms().contains(term2)){
                    count++;
                }
            }
        } else {
            for (EvolutionarySentence sentence : documentSentences){
                if (sentence.getTerms().contains(term1)){
                    count++;
                }
            }
        }

        return count;
    }
}
