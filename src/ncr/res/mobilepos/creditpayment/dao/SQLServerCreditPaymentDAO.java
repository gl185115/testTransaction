package ncr.res.mobilepos.creditpayment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditpayment.model.CreditPayment;
import ncr.res.mobilepos.creditpayment.model.CreditPaymentDataList;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerCreditPaymentDAO extends AbstractDao 
                                       implements ICreditPaymentDAO {
    /**
     * DBManager that manages the database.
     */
    private DBManager dbManager;
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * The class constructor.
     * @throws DaoException        Exception thrown when construction fails.
     */
    public SQLServerCreditPaymentDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();

        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Getter.
     * @throws DaoException     Exception when there is an error.
     * @return credit payment information.
     */
    public final CreditPaymentDataList getCreditPayment() throws DaoException {

        tp.methodEnter("getCreditPayment");
        
        CreditPaymentDataList creditpaymentDatalist = new CreditPaymentDataList();
        List<CreditPayment> creditpayment = new ArrayList<CreditPayment>();
        
        // TODO: change implementation since PRM_CREDIT_PAYMENTMETHOD table was removed
        
        // temp response
        creditpaymentDatalist.setNCRWSSResultCode(ResultBase.RES_OK);
        creditpaymentDatalist.setCreditPayment(creditpayment);
        
        tp.methodExit(creditpaymentDatalist);

        return creditpaymentDatalist;
    }
}
