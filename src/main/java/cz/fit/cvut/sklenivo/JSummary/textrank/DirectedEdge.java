package cz.fit.cvut.sklenivo.JSummary.textrank;

/**
 * Class used as a key to HashMap. This represents a directed edge from source to destination.
 */
class DirectedEdge {
    private TextRankSentence source;
    private TextRankSentence destination;

    public DirectedEdge(TextRankSentence source, TextRankSentence destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectedEdge directedEdge = (DirectedEdge) o;

        if (!source.equals(directedEdge.source)) return false;
        if (!destination.equals(directedEdge.destination)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + destination.hashCode();
        return result;
    }

    /**
     * Determines whether given sentence is source in this particular edge
     * @param textRankSentence sentence we want to check
     * @return true / false
     */
    public boolean isSource(TextRankSentence textRankSentence){
        return source.equals(textRankSentence);
    }

    /**
     * Determines whether given sentence is destination in this particular edge
     * @param textRankSentence sentence we want to check
     * @return true / false
     */
    public boolean isDestination(TextRankSentence textRankSentence){
        return destination.equals(textRankSentence);
    }

    public TextRankSentence getSource() {
        return source;
    }

    public TextRankSentence getDestination() {
        return destination;
    }

    public DirectedEdge reversed() {
        return new DirectedEdge(destination, source);
    }
}
