package cz.cvut.fit.sklenivo.JSummary.textrank;

import java.util.Comparator;

/**
 * Comparator for sentences based on their score
 */
class SentenceScoreComparator implements Comparator<TextRankSentence> {
    @Override
    public int compare(TextRankSentence o1, TextRankSentence o2) {
        if (o1.getScore() > o2.getScore()){
            return -1;
        }

        if (o1.getScore() == o2.getScore()){
            return 0;
        }

        return 1;
    }
}
