package cz.cvut.fit.sklenivo.JSummary.textrank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for a weighted directed graph
 */
class Graph {
    private Map<Edge, Double> graph;


    public Graph() {
        graph = new HashMap<>();
    }

    /**
     * Inserts a new edge
     * @param edge edge with two vertices
     * @param weight weight of edge
     */
    public void insert(Edge edge, Double weight){
        graph.put(edge, weight);
    }

    /**
     * Returns weight of edge
     * @param edge edge
     * @return weight of edge
     */
    public Double get(Edge edge){
        return graph.get(edge);
    }

    /**
     * Returns all vertices (sentences) V, such that there exists edge from V to argument
     *
     * @param textRankSentence target sentence
     * @return collection of sentences
     */
    public ArrayList<TextRankSentence> getNeighbour(TextRankSentence textRankSentence){
        ArrayList<TextRankSentence> ret = new ArrayList<>();

        //iterate over every edege in graph
        for (Edge edge : graph.keySet()){
            //if parameter is destination
            if (edge.isVertex1(textRankSentence)){
                ret.add(edge.getVertex2());
            }
            if (edge.isVertex2(textRankSentence)){
                ret.add(edge.getVertex1());
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
   /* public ArrayList<TextRankSentence> getOutgoing(TextRankSentence textRankSentence){
        ArrayList<TextRankSentence> ret = new ArrayList<>();

        for (Edge edge : graph.keySet()){
            if (edge.isSource(textRankSentence)){
                ret.add(edge.getDestination());
            }
        }

        return ret;
    }*/

    /**
     * Returns size of graph (number of edges)
     * @return number of edges
     */
    public int size(){
        return graph.size();
    }
}
