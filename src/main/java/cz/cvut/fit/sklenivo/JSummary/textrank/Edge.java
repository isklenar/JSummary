package cz.cvut.fit.sklenivo.JSummary.textrank;

/**
 * Class used as a key to HashMap. This represents a directed edge from source to destination.
 */
class Edge {
    private TextRankSentence vertex1;
    private TextRankSentence vertex2;

    public Edge(TextRankSentence vertex1, TextRankSentence vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    /**
     * Determines whether given sentence is source in this particular edge
     * @param textRankSentence sentence we want to check
     * @return true / false
     */
    public boolean isVertex1(TextRankSentence textRankSentence){
        return vertex1.equals(textRankSentence);
    }

    /**
     * Determines whether given sentence is destination in this particular edge
     * @param textRankSentence sentence we want to check
     * @return true / false
     */
    public boolean isVertex2(TextRankSentence textRankSentence){
        return vertex2.equals(textRankSentence);
    }

    public TextRankSentence getVertex1() {
        return vertex1;
    }

    public TextRankSentence getVertex2() {
        return vertex2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (vertex1.equals(edge.vertex1) && vertex2.equals(edge.vertex2)){
            return true;
        }

        return vertex1.equals(edge.vertex2) && vertex2.equals(edge.vertex1);
    }

    @Override
    public int hashCode() {
        int result = vertex1 != null ? vertex1.hashCode() : 0;
        result = 31 * result + (vertex2 != null ? vertex2.hashCode() : 0);
        return result;
    }
}
