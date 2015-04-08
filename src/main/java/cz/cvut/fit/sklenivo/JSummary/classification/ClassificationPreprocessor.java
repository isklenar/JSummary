package cz.cvut.fit.sklenivo.JSummary.classification;

import cz.cvut.fit.sklenivo.JSummary.SummarizationSettings;
import cz.cvut.fit.sklenivo.JSummary.util.POSTagger;
import cz.cvut.fit.sklenivo.JSummary.util.SentenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivo on 18.3.2015.
 */
public class ClassificationPreprocessor {

    public static List<ClassificationSentence> preProcess(String text, String summary, SummarizationSettings settings){
        String [] paragraphs = splitParagraphs(text);
        List<ClassificationSentence> sentences = new ArrayList<>();

        for (int i = 0; i < paragraphs.length; i++){
            sentences.addAll(createClassificationSentences(paragraphs[i]));
        }

        POSTagger tagger = createTagger(settings);
        for (ClassificationSentence sentence : sentences){
            sentence.extractFeatures(tagger);
        }

        if (summary == null){
            return sentences;
        }

        String [] summaryParagraphs = splitParagraphs(summary);
        for (String summaryParagraph : summaryParagraphs){
            List<ClassificationSentence> summarySentences = createClassificationSentences(summaryParagraph);

            for (ClassificationSentence summarySentence : summarySentences){
                int index = sentences.indexOf(summarySentence);
                if (index != -1){
                    sentences.get(index).setInSummary(true);
                }
            }
        }

        return sentences;
    }

    private static POSTagger createTagger(SummarizationSettings settings) {
        return new POSTagger(settings.getLanguage());
    }

    private static List<ClassificationSentence> createClassificationSentences(String paragraph) {
        List<String> sentences = SentenceUtils.splitToSentences(paragraph);
        List<ClassificationSentence> ret = new ArrayList<>();
        for (int i = 0; i < sentences.size(); i++){
            int paragraphPosition = i == 0 ? 1 : i == sentences.size() - 1 ? 3 : 2;
            ret.add(new ClassificationSentence(sentences.get(i), paragraphPosition));
        }

        return ret;
    }

    private static String[] splitParagraphs(String text) {
        String [] paragraphs = text.split("\n\n+");

        return paragraphs;
    }


}
