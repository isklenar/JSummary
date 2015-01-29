package cz.fit.cvut.sklenivo.JSummary;

import cz.fit.cvut.sklenivo.JSummary.bayes.NaiveBayes;

/**
 * Created by Ivo on 29.1.2015.
 */
public class Main {
    private static String text = "Vydatnější sněžení bude na Moravě a ve Slezsku, v nížinách tam napadne až 10 centimetrů sněhu, na horách až 20 centimetrů, uvedla Honsová. Na českých horách podle ní připadne kolem 15 centimetrů, v nížinách tak vydatné sněžení neočekává. Teploty přes den dosáhnou 0 až 4 stupňů Celsia, silný vítr ale výrazně sníží pocitovou teplotu.\n\nKvůli novému sněhu a hrozícím sněhovým jazykům už také ve čtvrtek vydal Český hydrometeorologický ústav varování pro Vysočinu a celou Moravu a Slezsko. Výstraha trvá od čtvrteční 22. do páteční 23. hodiny.\n\nO víkendu bude sněžit jen minimálně, a to zejména v horských oblastech. Převládat bude oblačno až polojasno, ráno naměříme -2 až -6 stupňů, při skoro jasné nebo polojasné obloze klesne teplota až na -10 stupňů. Odpolední maxima se budou v sobotu i v neděli pohybovat mezi -1 až 3 stupni.\n\nV neděli odpoledne a večer by měla opět začít přibývat oblačnost a v noci na pondělí můžeme podle meteoroložky počítat se sněhem.\n\nPodobný charakter by měl vydržet i v prvním únorovém týdnu. Převládat bude oblačná až polojasná obloha s občasnými sněhovými přeháňkami. Ráno mohou teploty klesat až k -10 stupňům, odpoledne se budou pohybovat mezi -2 až 2 stupni.";
    private static String summary = "„Vydatnější sněžení bude na Moravě a ve Slezsku, v nížinách tam napadne až 10 centimetrů sněhu, na horách až 20 centimetrů,“ uvedla Honsová. Na českých horách podle ní připadne kolem 15 centimetrů, v nížinách tak vydatné sněžení neočekává. O víkendu bude sněžit jen minimálně, a to zejména v horských oblastech. Převládat bude oblačno až polojasno, ráno naměříme -2 až -6 stupňů, při skoro jasné nebo polojasné obloze klesne teplota až na -10 stupňů. Odpolední maxima se budou v sobotu i v neděli pohybovat mezi -1 až 3 stupni. Ráno mohou teploty klesat až k -10 stupňům, odpoledne se budou pohybovat mezi -2 až 2 stupni.";
    public static void main(String [] args){
        NaiveBayes bayes = new NaiveBayes();
        bayes.train(text, summary);
    }
}
