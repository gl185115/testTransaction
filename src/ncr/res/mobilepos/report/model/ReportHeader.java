/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.report.model;

/**
 * Class for header representations of a Report.
 */
public class ReportHeader {

    /** The name of the report header. */
    private String name;

    /** The value of the report header. */
    private String value;

    /** The position of the report header. */
    private int position;

    /**
     * Custom constructor.
     *
     * @param nameToSet
     *            the name to set
     * @param valueToSet
     *            the value to set
     * @param positionToSet
     *            the position to set
     */
    public ReportHeader(final String nameToSet, final String valueToSet,
            final int positionToSet) {
        this.name = nameToSet;
        this.value = valueToSet;
        this.position = positionToSet;
    }

    /**
     * Sets the name of the report header.
     *
     * @param nameToSet
     *            the new name
     */
    public final void setName(final String nameToSet) {
        this.name = nameToSet;
    }

    /**
     * Sets the value of the report header.
     *
     * @param valueToSet
     *            the new value
     */
    public final void setValue(final String valueToSet) {
        this.value = valueToSet;
    }

    /**
     * Sets the position of the report header.
     *
     * @param positionToSet
     *            the new position
     */
    public final void setPosition(final int positionToSet) {
        this.position = positionToSet;
    }

    /**
     * Gets the name of the report header.
     *
     * @return name of the header
     */
    public final String getName() {
        return name;
    }

    /**
     * Gets the value of the report header.
     *
     * @return value of the header
     */
    public final String getValue() {
        return value;
    }

    /**
     * Gets the position of the report header.
     *
     * @return position of the header
     */
    public final int getPosition() {
        return position;
    }
}
