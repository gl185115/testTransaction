/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Item
 *
 * Model class for item object.
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import ncr.res.mobilepos.model.ResultBase;

/**
 * Department Model Object.
 *
 * Encapsulates the Department's information.
 *
 */
@XmlRootElement(name = "Department")
public class Department extends ResultBase {
    /**
     * The Department ID.
     */
    private String dpt;
    /**
     * The Department Name.
     */
    private String dptName;
    /**
     * The Department Kana Name.
     */
    private String dptKanaName;
    /**
     * The Department Category.
     */
    private String category;

    /**
     * Getter for dpt.
     * @return dpt  Department number
     */
    @XmlElement(name = "Dpt")
    public final String getDpt() {
        return this.dpt;
    }

    /**
     * Setter for setting Department.
     * @param dptToSet  Department number to set
     */
    public final void setDpt(final String dptToSet) {
        this.dpt = dptToSet;
    }

    /**
     * Getter for Department Name.
     * @return Department Name
     */
    @XmlElement(name = "DptName")
    public final String getDptName() {
        return this.dptName;
    }
    /**
     * Setter for Department name.
     * @param dptNameToSet  Department Name to set
     */
    public final void setDptName(final String dptNameToSet) {
        this.dptName = dptNameToSet;
    }

    /**
     * Getter for Department Kana Name.
     * @return Department Kana name.
     */
    @XmlElement(name = "DptKanaName")
    public final String getDptKanaName() {
        return this.dptKanaName;
    }

    /**
     * Setter for Department Kana Name.
     * @param dptKanaNameToSet  Department Kana Name
     */
    public final void setDptKanaName(final String dptKanaNameToSet) {
        this.dptKanaName = dptKanaNameToSet;
    }

    /**
     * Getter for Category.
     * @return  category
     */
    @XmlElement(name = "Category")
    public final String getCategory() {
        return this.category;
    }

    /**
     * Setter for Category.
     * @param categoryToSet  Category
     */
    public final void setCategory(final String categoryToSet) {
        this.category = categoryToSet;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Dpt: " + dptName).append("; ");
        sb.append("DptKanaName: " + dptName).append("; ");
        sb.append("Category: " + category);
        return sb.toString();
    }
}
