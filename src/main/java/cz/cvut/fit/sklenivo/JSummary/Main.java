package cz.cvut.fit.sklenivo.JSummary;

import cz.cvut.fit.sklenivo.JSummary.util.SummarizationUI;

/**
 * Created by Ivo on 29.1.2015.
 */
public class Main {
    private static String text = "Mr Assad told the BBC that there had been no direct co-operation since air strikes began in Syria in September. But third parties - among them Iraq - were conveying information. He also denied that Syrian government forces had been dropping barrel bombs indiscriminately on rebel-held areas, killing thousands of civilians. Mr Assad dismissed the allegation as a childish story, in a wide-ranging interview with BBC Middle East editor Jeremy Bowen in Damascus. We have bombs, missiles and bullets... There is [are] no barrel bombs, we don't have barrels.\n" +
            "\n" +
            "Our correspondent says that his denial is highly controversial as the deaths of civilians in barrel bomb attacks are well-documented. Mr Assad's many enemies will dismiss his view of the war. For them, he has been in charge of a killing machine that has been chewing Syrians up and spitting them out. As the war enters its fifth year, the barrel bomb has become the most notorious weapon in the regime's arsenal. Two or three years ago, I saw the results of what must have been one in Douma, a suburb of Damascus that has been held by rebels since close to the beginning of the war.\n" +
            "\n" +
            "Mr Assad insisted that the Syrian army would never use them in a place where people lived. I know about the army. They use bullets, missiles and bombs. I haven't heard of the army using barrels, or maybe, cooking pots. It was a flippant response; the mention of cooking pots was either callousness, an awkward attempt at humour, or a sign that Mr Assad has become so disconnected from what is happening that he feels overwhelmed.";


    private static String summary = "He also denied that Syrian government forces had been dropping barrel bombs indiscriminately on rebel-held areas, killing thousands of civilians.\n" +
            "\n" +
            "There is no barrel bombs, we dont have barrels.\n" +
            "\n" +
            "Our correspondent says that his denial is highly controversial as the deaths of civilians in barrel bomb attacks are well-documented.\n" +
            "\n" +
            "As the war enters its fifth year, the barrel bomb has become the most notorious weapon in the regimes arsenal.";

    private static String test = "Panos Kammenos, who heads the junior coalition partner Independent Greeks, said Greece had an obligation to go to plan B if proposals are rejected. Greece is preparing to plead its case for a new deal at a meeting of eurozone finance ministers on Wednesday. It says the terms of its bailout are too severe. Last month's election of an anti-austerity government led by left-wing Prime Minister Alexis Tsipras has raised fears that Greece could leave the euro.\n" +
            "\n" +
            "EU officials have so far rejected his efforts to renegotiate bailout terms, although German Chancellor Angela Merkel has said she will wait to see if Greece puts forward \"a sustainable proposal\" on Wednesday.What we want is a deal, Mr Kammenos said in an interview on Greece's Mega TV. But if there is no deal, and if we see that Germany remains rigid and wants to blow apart Europe, then we have the obligation to go to plan B. Plan B is to get funding from another source. It could be the United States at best, it could be Russia, it could be China or other countries.";

    public static void main(String [] args){
        SummarizationUI ui = new SummarizationUI();
    }
}
