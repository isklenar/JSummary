package cz.cvut.fit.sklenivo.JSummary.util;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ivo on 7.3.2015.
 */
public class WordDatabases {
    public static Set<String> UNITS_DATABASE = new TreeSet<>();
    public static Set<String> GREEK_LETTERS_DATABASE = new TreeSet<>();
    public static Set<String> NUMERIC_WORD_DATABASE = new TreeSet<>();

    static{
        UNITS_DATABASE.add("cm");
        UNITS_DATABASE.add("g");
        UNITS_DATABASE.add("kg");
        UNITS_DATABASE.add("lbs");
        UNITS_DATABASE.add("m");
        UNITS_DATABASE.add("ft");
        UNITS_DATABASE.add("km");
        UNITS_DATABASE.add("miles");
        UNITS_DATABASE.add("l");
        UNITS_DATABASE.add("ml");
        UNITS_DATABASE.add("gallons");
    }

    static {
        GREEK_LETTERS_DATABASE.add("alpha");
        GREEK_LETTERS_DATABASE.add("beta");
        GREEK_LETTERS_DATABASE.add("gamma");
        GREEK_LETTERS_DATABASE.add("delta");
        GREEK_LETTERS_DATABASE.add("epsilon");
        GREEK_LETTERS_DATABASE.add("zeta");
        GREEK_LETTERS_DATABASE.add("ete");
        GREEK_LETTERS_DATABASE.add("theta");
        GREEK_LETTERS_DATABASE.add("iota");
        GREEK_LETTERS_DATABASE.add("kappa");
        GREEK_LETTERS_DATABASE.add("lambda");
        GREEK_LETTERS_DATABASE.add("mu");
        GREEK_LETTERS_DATABASE.add("nu");
        GREEK_LETTERS_DATABASE.add("xi");
        GREEK_LETTERS_DATABASE.add("omicron");
        GREEK_LETTERS_DATABASE.add("pi");
        GREEK_LETTERS_DATABASE.add("rho");
        GREEK_LETTERS_DATABASE.add("sigma");
        GREEK_LETTERS_DATABASE.add("tau");
        GREEK_LETTERS_DATABASE.add("upsilon");
        GREEK_LETTERS_DATABASE.add("phi");
        GREEK_LETTERS_DATABASE.add("chi");
        GREEK_LETTERS_DATABASE.add("psi");
        GREEK_LETTERS_DATABASE.add("omega");

        GREEK_LETTERS_DATABASE.add("alfa");
        GREEK_LETTERS_DATABASE.add("beta");
        GREEK_LETTERS_DATABASE.add("gama");
        GREEK_LETTERS_DATABASE.add("delta");
        GREEK_LETTERS_DATABASE.add("epsilon");
        GREEK_LETTERS_DATABASE.add("zéta");
        GREEK_LETTERS_DATABASE.add("éta");
        GREEK_LETTERS_DATABASE.add("théta");
        GREEK_LETTERS_DATABASE.add("ióta");
        GREEK_LETTERS_DATABASE.add("kappa");
        GREEK_LETTERS_DATABASE.add("lambda");
        GREEK_LETTERS_DATABASE.add("mí");
        GREEK_LETTERS_DATABASE.add("ný");
        GREEK_LETTERS_DATABASE.add("ksí");
        GREEK_LETTERS_DATABASE.add("omikron");
        GREEK_LETTERS_DATABASE.add("pí");
        GREEK_LETTERS_DATABASE.add("ró");
        GREEK_LETTERS_DATABASE.add("sigma");
        GREEK_LETTERS_DATABASE.add("tau");
        GREEK_LETTERS_DATABASE.add("ypsilon");
        GREEK_LETTERS_DATABASE.add("fí");
        GREEK_LETTERS_DATABASE.add("chí");
        GREEK_LETTERS_DATABASE.add("psí");
        GREEK_LETTERS_DATABASE.add("omega");
    }
}
