package cz.cvut.fit.sklenivo.JSummary.testing;

import java.util.List;

/**
 * Created by ivo on 18.4.2015.
 */
public class TestUtils {
    public static String toText(List<String> sentences) {
        StringBuilder builder = new StringBuilder();

        for (String s : sentences){
            builder.append(s).append(" ");
        }

        return builder.toString();
    }

     private static Object lock = new Object();

    public static void print(StringBuilder log){
        synchronized (lock){
            System.out.println(log.toString());
        }
    }
}
