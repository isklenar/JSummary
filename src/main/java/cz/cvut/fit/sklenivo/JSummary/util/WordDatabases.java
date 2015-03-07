package cz.cvut.fit.sklenivo.JSummary.util;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ivo on 7.3.2015.
 */
public class WordDatabases {
    public static Set<String> unitsDatabase = new TreeSet<>();

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
}
