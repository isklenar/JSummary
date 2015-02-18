package cz.cvut.fit.sklenivo.JSummary.textrank;

/**
 * Class for storing a single sentence. Contains raw sentence and it's TextRank score.
 */
public class TextRankSentence {
    private String sentence;
    private double score;

    public TextRankSentence(String sentence) {
        this.sentence = sentence;

        score = 1;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getSentence() {
        return sentence;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextRankSentence textRankSentence1 = (TextRankSentence) o;

        if (!sentence.equals(textRankSentence1.sentence)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return sentence.hashCode();
    }

    @Override
    public String toString() {
        return sentence;
    }
}
