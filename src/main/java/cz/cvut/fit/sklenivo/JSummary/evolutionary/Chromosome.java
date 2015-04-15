package cz.cvut.fit.sklenivo.JSummary.evolutionary;

import java.util.ArrayList;

/**
 * Created by ivo on 15.2.2015.
 */
class Chromosome {
    private ArrayList<Integer> data;

    public Chromosome(ArrayList<Integer> data) {
        this.data = data;
    }


    /**
     * Sets new data.
     *
     * @param data New value of data.
     */
    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }

    /**
     * Gets data.
     *
     * @return Value of data.
     */
    public ArrayList<Integer> getData() {
        return data;
    }
}
