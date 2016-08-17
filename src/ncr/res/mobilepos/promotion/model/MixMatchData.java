package ncr.res.mobilepos.promotion.model;

import java.util.Date;

public abstract class MixMatchData {
	/** The Mix and Match Code. */
    private String code;
    /** The Start Date. */
    private Date startDate;
    /** The End Date. */
    private Date endDate;
    /** The Name. */
    private String name;
    /** The Mix and match Type. */
    private int type;
    /**
     * @return the code
     */
    public final String getCode() {
        return code;
    }
    /**
     * @param codeToSet the code to set
     */
    public final void setCode(final String codeToSet) {
        this.code = codeToSet;
    }
    /**
     * @return the startDate
     */
    public final Date getStartDate() {
        return (Date) startDate.clone();
    }
    /**
     * @param startDateToSet the startDate to set
     */
    public final void setStartDate(final Date startDateToSet) {
        this.startDate = (Date) startDateToSet.clone();
    }
    /**
     * @return the endDate
     */
    public final Date getEndDate() {
        return (Date) endDate.clone();
    }
    /**
     * @param endDateToSet the endDate to set
     */
    public final void setEndDate(final Date endDateToSet) {
        this.endDate = (Date) endDateToSet.clone();
    }
    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }
    /**
     * @param nameToSet the name to set
     */
    public final void setName(final String nameToSet) {
        this.name = nameToSet;
    }
    /**
     * @return the type
     */
    public final int getType() {
        return type;
    }
    /**
     * @param typeToSet the type to set
     */
    public final void setType(final int typeToSet) {
        this.type = typeToSet;
    }
}
