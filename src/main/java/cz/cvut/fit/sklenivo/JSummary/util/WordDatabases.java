package cz.cvut.fit.sklenivo.JSummary.util;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ivo on 7.3.2015.
 */
public class WordDatabases {
    public static Set<String> unitsDatabase = new TreeSet<>();
    public static Set<String> greekLetters = new TreeSet<>();

    static{
        unitsDatabase.add("cm");
        unitsDatabase.add("g");
        unitsDatabase.add("kg");
        unitsDatabase.add("lbs");
        unitsDatabase.add("m");
        unitsDatabase.add("in");
        unitsDatabase.add("ft");
        unitsDatabase.add("km");
        unitsDatabase.add("miles");
        unitsDatabase.add("l");
        unitsDatabase.add("ml");
        unitsDatabase.add("gallons");
    }

    static {
        greekLetters.add("alpha");
        greekLetters.add("beta");
        greekLetters.add("gamma");
        greekLetters.add("delta");
        greekLetters.add("epsilon");
        greekLetters.add("zeta");
        greekLetters.add("ete");
        greekLetters.add("theta");
        greekLetters.add("iota");
        greekLetters.add("kappa");
        greekLetters.add("lambda");
        greekLetters.add("mu");
        greekLetters.add("nu");
        greekLetters.add("xi");
        greekLetters.add("omicron");
        greekLetters.add("pi");
        greekLetters.add("rho");
        greekLetters.add("sigma");
        greekLetters.add("tau");
        greekLetters.add("upsilon");
        greekLetters.add("phi");
        greekLetters.add("chi");
        greekLetters.add("psi");
        greekLetters.add("omega");

        greekLetters.add("alfa");
        greekLetters.add("beta");
        greekLetters.add("gama");
        greekLetters.add("delta");
        greekLetters.add("epsilon");
        greekLetters.add("zéta");
        greekLetters.add("éta");
        greekLetters.add("théta");
        greekLetters.add("ióta");
        greekLetters.add("kappa");
        greekLetters.add("lambda");
        greekLetters.add("mí");
        greekLetters.add("ný");
        greekLetters.add("ksí");
        greekLetters.add("omikron");
        greekLetters.add("pí");
        greekLetters.add("ró");
        greekLetters.add("sigma");
        greekLetters.add("tau");
        greekLetters.add("ypsilon");
        greekLetters.add("fí");
        greekLetters.add("chí");
        greekLetters.add("psí");
        greekLetters.add("omega");
    }
}
