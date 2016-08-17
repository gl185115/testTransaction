/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.report.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Class for column representation of a Report.
 */
public class ReportColumn {

    /** The width size of the report column. */
    private float width;

    /** The list of data to add. */
    private List<String> data;

    /** The name of the report column. */
    private String name;

    /**
     * Custom constructor of ReportColumn *.
     *
     * @param nameToSet
     *            the name to set
     */
    public ReportColumn(final String nameToSet) {
        this.name = nameToSet;
        data = new ArrayList<String>();
    }

    /**
     * Gets the name of the column.
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the name of the column.
     *
     * @param nameToSet
     *            the new name
     */
    public final void setName(final String nameToSet) {
        this.name = nameToSet;
    }

    /**
     * Gets the width of the column.
     *
     * @return the width
     */
    public final float getWidth() {
        return width;
    }

    /**
     * Sets the width of the column.
     *
     * @param widthToSet
     *            the new width
     */
    public final void setWidth(final float widthToSet) {
        this.width = widthToSet;
    }

    /**
     * Gets the list of data in a column.
     *
     * @return the data
     */
    public final List<String> getData() {
        return data;
    }

    /**
     * Add data to column.
     *
     * @param dataToSet
     *            the data to set
     */
    public final void addData(final String dataToSet) {
        this.data.add(dataToSet);
    }
}
