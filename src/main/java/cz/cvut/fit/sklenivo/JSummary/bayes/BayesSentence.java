package cz.cvut.fit.sklenivo.JSummary.bayes;

import cz.cvut.fit.sklenivo.JSummary.util.WordDatabases;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ivo on 20.10.14.
 */
public class BayesSentence implements Comparable<BayesSentence> {
    private String text;
    private double [] features = new double[Features.FEATURES_TOTAL.getValue()];

    private final int DOT_END = 1;
    private final int QUESTION_MARK_END = 2;
    private final int EXCLAMATION_MARK_END = 3;

    private boolean inSummary;


    public BayesSentence(String sentence, int paragraphPosition) {
        text = sentence;
        features[Features.PARAGRAPH_POSITION.value] = paragraphPosition;
    }

    private enum Features {
        CHARACTER_COUNT(0), //
        PARAGRAPH_POSITION(1),
        WORD_COUNT(2),//
        COMMA_COUNT(3), //
        CAPITAL_WORDS_COUNT(4), //
        ACRONYM_COUNT(5),
        PARENTHESIS_COUNT(6),
        SENTENCE_SYMBOLS_COUNT(7),
        UNITS_COUNT(8), //
        NUMBERS_COUNT(9),
        ABBREVIATION_COUNT(10),
        APOSTROPHE_COUNT(11),
        SHORT_WORDS_COUNT(12), //
        LONG_WORDS_COUNT(13), //
        NOUNS_COUNT(14),
        ADJECTIVES_COUNT(15),
        VERBS_COUNT(16),
        LINKS_COUNT(17),
        SENTENCE_END(18),
        MATH_SYMBOLS_COUNT(19),
        GREEK_LETTERS_COUNT(20),
        DATETIME_COUNT(21),
        NUMERIC_COUNT(22),
        FEATURES_TOTAL(23);


        private int value;
        private Features(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public void extractFeatures(MaxentTagger tagger){
        extractCharacterCountFeature();
        extractWordCountFeature();
        extractCommaCountFeature();
        extractCapitalWordsCountFeature();
        extractAcronymCountFeature();
        extractParenthesisCountFeature();
        extractSentenceSymbolsCountFeature();
        extractUnitsCountFeature();
        extractNumbersCountFeature();
        extractAbbreviationCountFeature();
        extractApostropheCountFeature();
        extractShortWordsFeature();
        extractLongWordsFeature();
        extractNounsCountFeature(tagger);
        extractAdjectivesCountFeature(tagger);
        extractVerbsCountFeature(tagger);
        extractLinksCountFeature();
        extractSentenceEndFeature();
        extractMathSymbolsCountFeature();
        extractGreekLettersCountFeature();
        extractDateTimeCountFeature();
        extractNumericCountFeature();
    }

    private void extractNumericCountFeature() {

    }

    private void extractDateTimeCountFeature() {

    }

    private void extractGreekLettersCountFeature() {
        String [] words = text.split(" ");
        int count = 0;
        for (String word : words){
            if (WordDatabases.GREEK_LETTERS_DATABASE.contains(word.toLowerCase())){
                count++;
            }
        }

        features[Features.GREEK_LETTERS_COUNT.value] = count;
    }

    private void extractMathSymbolsCountFeature() {
        int count = text.length() - text.replace("+", "").length();
        count += text.length() - text.replace("*", "").length();
        count += text.length() - text.replace("^", "").length();

        features[Features.SENTENCE_SYMBOLS_COUNT.value] = count;
    }

    private void extractSentenceEndFeature() {
        if (text.endsWith(".")){
            features[Features.SENTENCE_END.value] = DOT_END;
        }

        if (text.endsWith("?")){
            features[Features.SENTENCE_END.value] = QUESTION_MARK_END;
        }

        if (text.endsWith("!")){
            features[Features.SENTENCE_END.value] = EXCLAMATION_MARK_END;
        }
    }

    private void extractLinksCountFeature() {
        // http://stackoverflow.com/questions/163360/regular-expresion-to-match-urls-in-java
        Pattern pattern = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        while (matcher.find()){
            count++;
        }

        features[Features.LINKS_COUNT.value] = count;
    }

    private void extractVerbsCountFeature(MaxentTagger tagger) {
        String tagged = tagger.tagString(text);

        Pattern pattern = Pattern.compile("_VB");
        Matcher matcher = pattern.matcher(tagged);

        int count = 0;
        while (matcher.find()){
            count++;
        }

        features[Features.VERBS_COUNT.value] = count;
    }

    private void extractAdjectivesCountFeature(MaxentTagger tagger) {
        String tagged = tagger.tagString(text);

        Pattern pattern = Pattern.compile("_JJ");
        Matcher matcher = pattern.matcher(tagged);

        int count = 0;
        while (matcher.find()){
            count++;
        }

        features[Features.ADJECTIVES_COUNT.value] = count;
    }

    private void extractNounsCountFeature(MaxentTagger tagger) {
        String tagged = tagger.tagString(text);

        Pattern pattern = Pattern.compile("_NN");
        Matcher matcher = pattern.matcher(tagged);

        int count = 0;
        while (matcher.find()){
            count++;
        }

        features[Features.NOUNS_COUNT.value] = count;
    }

    private void extractLongWordsFeature() {
        String [] words = text.split(" ");
        int count = 0;
        for (String word : words){
            if (word.length() >= 10){
                count++;
            }
        }

        features[Features.LONG_WORDS_COUNT.value] = count;
    }

    private void extractShortWordsFeature() {
        String [] words = text.split(" ");
        int count = 0;
        for (String word : words){
            if (word.length() <= 3){
                count++;
            }
        }

        features[Features.SHORT_WORDS_COUNT.value] = count;
    }

    private void extractApostropheCountFeature() {
        int count = text.length() - text.replace("'", "").length();
        features[Features.APOSTROPHE_COUNT.value] = count;
    }

    private void extractNumbersCountFeature() {
        Pattern pattern = Pattern.compile("\\d+\\d*\\.?\\d*");
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        while (matcher.find()){
            count++;
        }

        features[Features.NUMBERS_COUNT.value] = count;
    }

    private void extractUnitsCountFeature() {
        String [] words = text.split(" ");
        int count = 0;
        for (String word : words){
            if (WordDatabases.UNITS_DATABASE.contains(word)){
                count++;
            }
        }

        features[Features.UNITS_COUNT.value] = count;
    }

    private void extractAbbreviationCountFeature() {
        Pattern pattern = Pattern.compile("([a-zA-Z]\\.([a-zA-Z]\\.)+)");
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        while (matcher.find()){
            count++;
        }

        features[Features.ABBREVIATION_COUNT.value] = count;
    }

    private void extractSentenceSymbolsCountFeature() {
        int count = text.length() - text.replace(";", "").length();
        count += text.length() - text.replace(":", "").length();
        count += text.length() - text.replace("-", "").length();
        count += text.length() - text.replace("/", "").length();
        count += text.length() - text.replace("...", "").length();

        features[Features.SENTENCE_SYMBOLS_COUNT.value] = count;

    }

    private void extractParenthesisCountFeature() {
        int count = text.length() - text.replace("(", "").length();
        count += text.length() - text.replace(")", "").length();
        count += text.length() - text.replace("]", "").length();
        count += text.length() - text.replace("[", "").length();
        count += text.length() - text.replace("{", "").length();
        count += text.length() - text.replace("}", "").length();
        features[Features.PARENTHESIS_COUNT.value] = count;
    }

    private void extractAcronymCountFeature() {
        Pattern pattern = Pattern.compile("([A-Z][A-Z]+)"); // regex for words starting with capital letter
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        while (matcher.find()){
            count++;
        }

        features[Features.ACRONYM_COUNT.value] = count;
    }

    private void extractCapitalWordsCountFeature() {
        Pattern pattern = Pattern.compile("([A-Z][a-z]+)+"); // regex for words starting with capital letter
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        while (matcher.find()){
            count++;
        }

        features[Features.CAPITAL_WORDS_COUNT.value] = count;
    }

    private void extractCommaCountFeature() {
        int count = text.length() - text.replace(",", "").length(); // number of ','
        features[Features.COMMA_COUNT.value] = count;
    }

    private void extractWordCountFeature() {
        int count = text.split(" ").length;
        features[Features.WORD_COUNT.value] = count;
    }

    private void extractCharacterCountFeature() {
        int count = text.length();
        features[Features.CHARACTER_COUNT.value] = count;
    }


    @Override
    public int compareTo(BayesSentence o) {
        return text.compareTo(o.text);
    }

    /**
     * Sets new inSummary.
     *
     * @param inSummary New value of inSummary.
     */
    public void setInSummary(boolean inSummary) {
        this.inSummary = inSummary;
    }

    /**
     * Gets inSummary.
     *
     * @return Value of inSummary.
     */
    public boolean isInSummary() {
        return inSummary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BayesSentence sentence = (BayesSentence) o;

        if (text != null ? !text.trim().equals(sentence.text.trim()) : sentence.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }


    /**
     * Gets features.
     *
     * @return Value of features.
     */
    public double[] getFeatures() {
        return features;
    }
}
