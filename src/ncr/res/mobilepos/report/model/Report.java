/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Report
 *
 * Model for the simplest form of a Report
 *
 * Meneses, Chris Niven
 */
package ncr.res.mobilepos.report.model;
/**
 * 改定履歴
 * バージョン      改定日付      担当者名        改定内容
 * 1.01    2014.10.21    FENGSHA    レポート出力を対応
 * 1.02    2014.11.29    FengSha     レポート出力を対応
 * 1.03    2014.12.16    MAJINHUI   レポート出力を対応
 * 1.04    2014.12.26    ZHOUCHUNXIN   売上を対応
 * 1.05    2014.12.30    MAJINHUI     会計レポート出力を対応
 */

import java.util.List;
import java.util.ArrayList;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.report.constants.ReportConstants;

/**
 * Report Class is an abstract class that represents the Report and contains the
 * common fields in a Report.
 */
public abstract class Report {

    /** The columns in the report. */
    private List<ReportColumn> columns;

    /** The headers in the report. */
    private List<ReportHeader> headers;

    /** The report html format. */
    private StringBuilder reportHTML;

    /** The totals of each row. */
    private List<String> totals;

    /**
     * Default constructor Initializes headers, columns and totals to list of
     * array.
     */
    public Report() {
        columns = new ArrayList<ReportColumn>();
        headers = new ArrayList<ReportHeader>();
        totals = new ArrayList<String>();
        this.reportHTML = new StringBuilder();
    }

    /**
     * Add column to a Report table.
     *
     * @param name
     *            of the column
     */
    public final void addColumn(final String name) {
        ReportColumn column = new ReportColumn(name);
        columns.add(column);
        totals.add("0");
    }

    /**
     * Add column to a Report table.
     *
     * @param column
     *            ReportColumn
     */
    public final void addColumn(final ReportColumn column) {
        columns.add(column);
    }

    /**
     * Add header on a specific position.
     *
     * @param name
     *            the name of the header
     * @param value
     *            the value of the header
     * @param position
     *            the position of the header
     */
    public final void addHeader(final String name, final String value,
            final int position) {
        headers.add(new ReportHeader(name, value, position));
    }

    /**
     * Add data to a column index.
     *
     * @param columnIndex
     *            the column index where the data to add.
     * @param data
     *            the data to add in the column index.
     */
    public final void addDataToColumn(final int columnIndex,
            final String data) {
        columns.get(columnIndex).addData(data);
    }

    /**
     * If there is no report data, return the message of HTML type.
     *
     * @return string of html.
     */
    private String messageHTML() {
        String message = "No report was generated from your query.";
        StringBuilder messageHTML = new StringBuilder();
        messageHTML.append("<div id='messageDiv'"
                + " style='text-align: center; width: 80%;'>");
        messageHTML.append("<span class='InquiryPageText'>");
        messageHTML.append(message);
        messageHTML.append("</span></div>");
        return messageHTML.toString();
    }

    /**
     * Format report to an html type.
     *
     * @return the string HTML format.
     */
    public final String toHTML() {

        if (headers == null || headers.isEmpty()) {
            return null;
        }
        if (columns.get(0).getData().isEmpty()) {
            return messageHTML();
        }

        reportHTML = new StringBuilder(
                "<div id='reportDiv'><table id='reportHeader'><tr>");

        StringBuilder div1 = new StringBuilder("<td id='reportHeaderLeft'>");
        StringBuilder div2 = new StringBuilder("<td id='reportHeaderRight'>");

        for (int i = 0; i < headers.size(); i++) {
            ReportHeader repHead = headers.get(i);
            if (repHead == null) {
                break;
            }

            if (ReportConstants.REPORTHEADERPOS_LEFT == repHead.getPosition()) {
                div1.append(repHead.getName()).append(" : ")
                    .append(repHead.getValue()).append("<br />");
            } else if (ReportConstants.REPORTHEADERPOS_RIGHT == repHead
                    .getPosition()) {
                div2.append(repHead.getName()).append(" : ")
                    .append(repHead.getValue()).append("<br />");
            }

        }

        reportHTML.append(div1.toString()).append("</td>")
            .append(div2).append("</td></tr></table>");
        reportHTML.append("<table id='reportTable'>");

        // Add Headers
        List<String> array = new ArrayList<String>();
        List<String> alignFlag = new ArrayList<String>();

        for (ReportColumn repCol : columns) {

            if (repCol.getData() == null) {
                return null;
            }

            array.add(repCol.getName());
        }
        addHTMLRow(array, true, alignFlag);
        array.clear();

        // add data to columns row by row
        if (columns.get(0).getData() == null) {
            return null;
        }
        int numberOfRows = columns.get(0).getData().size();

        for (int i = 0; i < numberOfRows; i++) {
            for (ReportColumn repCol : columns) {

                if (repCol.getData() == null) {
                    return null;
                }
                String columnName = repCol.getName();
                array.add(repCol.getData().get(i));
                if (columnName.equals(ReportConstants.COLHEADER_CUSTOMERS)
                        || columnName.equals(ReportConstants.COLHEADER_ITEMS)) {
                    // alignment right
                    alignFlag.add("1");
                } else if (columnName.equals(ReportConstants
                        .COLHEADER_AMOUNT)) {
                    // alignment right and yen mark
                    alignFlag.add("2");
                } else if (columnName.equals(ReportConstants.COLHEADER_NAME)) {
                    // alignment left
                    alignFlag.add("3");
                } else {
                    // alignment center
                    alignFlag.add("0");
                }
            }
            addHTMLRow(array, false, alignFlag);
            array.clear();
        }

        // continue here even if columnData is null or has no data
        // possibility of data not found

        // for Totals
        reportHTML.append("<tr id='totalRow'><td id='reportTotal'>Total</td>");

        for (int i = 1; i < columns.size(); i++) {
            reportHTML.append(
                    "<td class='reportCell' style='text-align: right;'>");
            int data = Integer.parseInt(totals.get(i));

            if (data >= 0) {
                if (columns.get(i).getName()
                        .equals(ReportConstants.COLHEADER_AMOUNT)) {
                    reportHTML.append(
                            StringUtility.addCommaYenToNumString(totals
                            .get(i)));
                } else {
                    reportHTML.append(StringUtility.addCommaToNumString(Integer
                            .toString(data)));
                }
            }

            reportHTML.append("</td>");
        }

        reportHTML.append("</tr></table></div>");

        return reportHTML.toString();
    }

    /**
     * Gets the report items.
     *
     * @return the report items.
     */
    public final ReportItems getReportItems() {

        ReportItems reportItemsList = new ReportItems();

        // add data to columns row by row
        //1.03    2014.12.16     MAJINHUI   レポート出力を対応 MOD START
        // 売上表機能に対して、NODATAFOUNDの場合、正常終了と扱う
        if (columns.get(0).getData() == null
                || columns.get(0).getData().size() <= 0) {
            reportItemsList
                    .setNCRWSSResultCode(ResultBase.RESRPT_OK);
            reportItemsList
                    .setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            reportItemsList.setMessage(ResultBase.RES_SUCCESS_MSG);
            return reportItemsList;
        }
        // if (columns.get(0).getData() == null) {
        // reportItemsList.setNCRWSSResultCode(ResultBase.RESRPT_EMPTY);
        // reportItemsList.setMessage("columns are null");
        // return reportItemsList;
        // }
        //
        // if (columns.get(0).getData().size() <= 0) {
        // reportItemsList.setNCRWSSResultCode(ResultBase.RESRPT_EMPTY);
        // reportItemsList.setMessage("No rows available");
        // return reportItemsList;
        // }
        // 1.03 2014.12.16 MAJINHUI レポート出力を対応 MOD END

        // Add Headers
        List<ReportItem> reportitems = new ArrayList<ReportItem>();

        int numberOfRows = columns.get(0).getData().size();

        for (int i = 0; i < numberOfRows; i++) {
            ReportItem repItem = new ReportItem();

            for (ReportColumn repCol : columns) {
                if (repCol.getData() == null) {
                    return null;
                }
                String columnName = repCol.getName();
                if (columnName.equals(ReportConstants.COLHEADER_NUMBER)
                        || "Time Zone".equalsIgnoreCase(columnName)) {
                    repItem.getItemLabel().setCode(repCol.getData().get(i));
                } else if (columnName.equals(ReportConstants.COLHEADER_NAME)) {
                    repItem.getItemLabel().setName(repCol.getData().get(i));
                } else if (columnName.equals(ReportConstants
                        .COLHEADER_AMOUNT)) {
                    String data = repCol.getData().get(i);
                    if (data == null) {
                        data = "0";
                    }
                  //1.04   2014.12.26  売上表を対応　 START
                   // repItem.setSalesRevenue(Integer.parseInt(data));
                      repItem.setSalesRevenue(Double.parseDouble(data));
                  //1.04   2014.12.26  売上表を対応　 END
                } else if (columnName
                        .equals(ReportConstants.COLHEADER_CUSTOMERS)) {
                    String data = repCol.getData().get(i);
                    if (data == null) {
                        data = "0";
                    }
                    //repItem.setCustomer(Integer.parseInt(data));
                    repItem.setCustomer(Long.parseLong(data));
                } else if (columnName.equals(ReportConstants.COLHEADER_ITEMS)) {
                    String data = repCol.getData().get(i);
                    if (data == null) {
                        data = "0";
                    }
                    //repItem.setItems(Integer.parseInt(data));
                    repItem.setItems(Long.parseLong(data));
                }
                //1.02   2014.11.29  FengSha    レポート出力を対応　 START
                else if (columnName.equals(ReportConstants.COLHEADER_TIMEZONE)) {

                    repItem.setTimeZoneCode(repCol.getData().get(i));
                }
                else if (columnName.equals(ReportConstants.ROW_GUESTZONENAME)) {

                    repItem.setTimeZoneCode(repCol.getData().get(i));
                }
                //1.02   2014.11.29  FengSha    レポート出力を対応　 END
                //1.01   2014.10.21  FENGSHA    レポート出力を対応　 START
                else if (columnName.equals(ReportConstants.COLITEMNAME)) {
                    repItem.getItemLabel().setName(repCol.getData().get(i));
                } else if (columnName.equals(ReportConstants.COLACCOUNT)) {
                    //回数
                    repItem.setItems(StringUtility.convNullToZero(repCol
                            .getData().get(i)));
                } else if (columnName.equals(ReportConstants.COLAMOUNT)) {
                    //金額
                    repItem.setSalesRevenue(StringUtility.convNullToZero(repCol
                            .getData().get(i)));
                } else if (columnName.equals(ReportConstants.SALESORPAY)) {
                    String data = repCol.getData().get(i);
                    if (data == null) {
                        data = "1";
                    }
                    repItem.getItemLabel().setCode(data);
                }
               //1.05    2014.12.30    MAJINHUI     会計レポート出力を対応 ADD  START
                else if(columnName.equals(ReportConstants.COLITEM)){
                    repItem.setItemName(repCol.getData().get(i));
                }
                //1.05    2014.12.30    MAJINHUI     会計レポート出力を対応 ADD  END

              //1.01   2014.10.21  FENGSHA    レポート出力を対応　　 END

            }

            reportitems.add(repItem);
        }

        ReportItem[] reportItems = new ReportItem[reportitems.size()];
        reportitems.toArray(reportItems);
        reportItemsList.setReportItems(reportItems);
        reportItemsList.setNCRWSSResultCode(ResultBase.RESRPT_OK);
        // 1.03 2014.12.16 MAJINHUI レポート出力を対応 MOD START
        reportItemsList.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
        // reportItemsList.setMessage("Success");
        reportItemsList.setMessage(ResultBase.RES_SUCCESS_MSG);
        // 1.03 2014.12.16 MAJINHUI レポート出力を対応 MOD END

        return reportItemsList;
    }

    /**
     * Add html elements to a report table's row.
     *
     * @param array
     *            the array of data.
     * @param isColHeader
     *            if true, added as column header. if false, column data.
     * @param alignFlag
     *            the align flag.
     */
    private void addHTMLRow(final List<String> array,
            final boolean isColHeader, final List<String> alignFlag) {
        int toAdd = 0;
        int totalsValue = 0;

        if (isColHeader) {
            reportHTML.append("<tr id='columnHeaders'>");
        } else {
            reportHTML.append("<tr id='columnRows'>");
        }

        for (int i = 0; i < columns.size(); i++) {
            String data = array.get(i);

            if (isColHeader) {
                reportHTML.append("<td id='col")
                    .append(data.replaceAll(" ", ""))
                    .append("' class='tableHeader'>")
                    .append(data).append("</td>");
            } else {
                if ("1".equals(alignFlag.get(i))) {
                    reportHTML.append("<td class='reportCell'")
                            .append(" style='text-align: right;' >")
                            .append(StringUtility.addCommaToNumString(data))
                            .append("</td>");
                } else if ("2".equals(alignFlag.get(i))) {
                    reportHTML.append("<td class='reportCell'")
                            .append(" style='text-align: right;' >")
                            .append(StringUtility.addCommaYenToNumString(data))
                            .append("</td>");
                } else if ("3".equals(alignFlag.get(i))) {
                    reportHTML.append("<td class='reportCell' ")
                            .append("style='text-align: left;' >")
                            .append(data)
                            .append("</td>");
                } else {
                    reportHTML.append("<td class='reportCell' >")
                              .append(data)
                              .append("</td>");
                }
            }

            if (i == 0) {
                continue; // if it is the first column.
                          // Doesnt need to be totalled
            }

            // test if value is an integer and can be totalled
            try {
                toAdd = Integer.parseInt(data);
            } catch (NumberFormatException nfe) {
                totals.set(i, "-1");
                continue;
            }

            /*
             * if value can be totalled, check if it is the first value in the
             * column if it is, totalsValue should be converted to 0 since it is
             * -1 this is because all columns go through the previous catch
             * because of the column name
             */
            totalsValue = Integer.parseInt(totals.get(i));

            if (totalsValue < 0) {
                totalsValue = 0;
            }

            totalsValue += toAdd;

            totals.set(i, Integer.toString(totalsValue));

        }

        reportHTML.append("</tr>");
    }
}
