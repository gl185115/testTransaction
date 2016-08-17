package ncr.res.mobilepos.report.model;

/**
 * @author Zhou
 * ClerkProductReport Class is a Model representation of the Clerk Product Report.
 */
import ncr.res.mobilepos.report.constants.ReportConstants;
public class ClerkProductReport extends Report {
  
	/**
    * Construct for clerk product report.
    *
    * @param deviceNo
    *            device identifier of the device requesting the clerk proudct report
    * @param operatorNo
    *            operator identifier of the operator requesting the clerk product
    *            report
    */
	public ClerkProductReport() {
		this.addColumn(ReportConstants.COLHEADER_NUMBER);
		this.addColumn(ReportConstants.COLHEADER_NAME);
		this.addColumn(ReportConstants.COLHEADER_ITEMS);
		this.addColumn(ReportConstants.COLHEADER_AMOUNT);
	}

	/**
	 * Add value to product itemcode.
	 *
	 * @param data
	 *            the data
	 * @return true, if successful
	 */
	public final boolean addDataToProductCode(final String data) {
		this.addDataToColumn(ReportConstants.PARAM0, data);
		return true;
	}

	/**
	 * Add value to product name.
	 *
	 * @param data
	 *            the data
	 * @return true, if successful
	 */
	public final boolean addDataToProductName(final String data) {
		this.addDataToColumn(ReportConstants.PARAM1, data);
		return true;
	}

	/**
	 * Add value to sales item.
	 *
	 * @param data
	 *            the data
	 * @return true, if successful
	 */
	public final boolean addDataToSalesItems(final String data) {
		this.addDataToColumn(ReportConstants.PARAM2, data);
		return true;
	}

	/**
	 * Add value to sales amount.
	 *
	 * @param data
	 *            the data
	 * @return true, if successful
	 */
	public final boolean addDataToSalesAmount(final String data) {
		this.addDataToColumn(ReportConstants.PARAM3, data);
		return true;
	}

}
