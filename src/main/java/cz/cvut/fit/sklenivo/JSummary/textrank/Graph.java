package cz.cvut.fit.sklenivo.JSummary.textrank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for a weighted directed graph
 */
class Graph {
    private Map<DirectedEdge, Double> graph;


    public Graph() {
        graph = new HashMap<>();
    }

    /**
     * Inserts a new edge
     * @param directedEdge edge with two vertices
     * @param weight weight of edge
     */
    public void insert(DirectedEdge directedEdge, Double weight){
        graph.put(directedEdge, weight);
    }

    /**
     * Returns weight of edge
     * @param directedEdge edge
     * @return weight of edge
     */
    public Double get(DirectedEdge directedEdge){
        return graph.get(directedEdge);
    }

    /**
     * Returns all vertices (sentences) V, such that there exists edge from V to argument
     *
     * @param textRankSentence target sentence
     * @return collection of sentences
     */
    public ArrayList<TextRankSentence> getIncoming(TextRankSentence textRankSentence){
        ArrayList<TextRankSentence> ret = new ArrayList<>();

        //iterate over every edege in graph
        for (DirectedEdge directedEdge : graph.keySet()){
            //if parameter is destination
            if (directedEdge.isDestination(textRankSentence)){
                ret.add(directedEdge.getSource());
            }
        }

        return ret;
    }

    /**
     * Returns all vertices (sentences) V, such that there exists edge to V from argument
     *
     * @param textRankSentence source sentence
     * @return collection of sentences
     */
    public ArrayList<TextRankSentence> getOutgoing(TextRankSentence textRankSentence){
        ArrayList<TextRankSentence> ret = new ArrayList<>();

        for (DirectedEdge directedEdge : graph.keySet()){
            if (directedEdge.isSource(textRankSentence)){
                ret.add(directedEdge.getDestination());
            }
        }

        return ret;
    }

    /**
     * Returns size of graph (number of edges)
     * @return number of edges
     */
    public int size(){
        return graph.size();
    }
}
