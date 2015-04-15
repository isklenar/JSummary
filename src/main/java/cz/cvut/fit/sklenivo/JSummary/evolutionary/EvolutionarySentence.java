package cz.cvut.fit.sklenivo.JSummary.evolutionary;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by ivo on 15.2.2015.
 */
class EvolutionarySentence {
    private TreeSet<String> terms;

    public EvolutionarySentence(ArrayList<String> terms) {
        this.terms = new TreeSet<>(terms);
    }

    /**
     * Gets terms.
     *
     * @return Value of terms.
     */
    public TreeSet<String> getTerms() {
        return terms;
    }

    /**
     * Sets new terms.
     *
     * @param terms New value of terms.
     */
    public void setTerms(TreeSet<String> terms) {
        this.terms = terms;
    }
}
