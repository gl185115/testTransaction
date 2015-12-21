package ncr.res.mobilepos.consolidation.dao;

/**
 * Class for LineItem counters.
 */
public class LineItemCounter {
    /**
     * Index counter.
     */
    private int index;

    /**
     * Default constructor. Initializes index to zero.
     */
    public LineItemCounter() {
        this.index = 0;
    }

    /**
     * Gets index.
     *
     * @return index.
     */
    public final int getIndex() {
        return index;
    }

    /**
     * Increments index with 1.
     */
    public final void increment() {
        this.index++;
    }
}
