package ncr.res.mobilepos.mastermaintenance.model;

import java.io.IOException;

import ncr.res.mobilepos.helper.CSVSerializer;

import org.jsefa.csv.annotation.CsvDataType;

/**
 * An Abstract class representing a Table in SPART to be imported
 * in WebStoreServer database.
 * @author cc185102
 *
 */
@CsvDataType
public abstract class MaintenanceTbl {
    /** The Flag value that refer to OPE_MAST_TBL. */
    private static final int OPE_MAST_TBL = 0;
    /** The Flag value that refer to MD_MM_MAST_TBL. */
    private static final int MD_MM_MAST_TBL = 1;
    /** The Flag value that refer to MD_MAST_TBL. */
    private static final int MD_MAST_TBL = 2;
    /**
     * The Master Maintenance MODELfactory.
     * @param tableflag The Flag for Table Model that the CSV represents.
     * @param fieldvalues   The CSV formatted String.
     * @return  The MasterMaintenance Table.
     * @throws IOException  ThE exception thrown when error occur.
     */
    public static MaintenanceTbl getMaintenanceModelFactory(
            final int tableflag, final String fieldvalues) throws IOException {
        MaintenanceTbl result = null;
        switch (tableflag) {
            case OPE_MAST_TBL:                
                result = getOpeMastTbl(fieldvalues);
                break;
            case MD_MM_MAST_TBL:
            	result = getMdMMMastTbl(fieldvalues);
                break;
            case MD_MAST_TBL:
            	result = getMdMastTbl(fieldvalues); 
            	break;
            default:
                break;
        }
        return result;
    }   
    
	private static MaintenanceTbl getOpeMastTbl(final String fieldvalues)
			throws IOException {
		CSVSerializer<OpeMastTbl> serializer = new CSVSerializer<OpeMastTbl>();
		return serializer.deserializeCsv(fieldvalues, OpeMastTbl.class);
	}
	
	private static MaintenanceTbl getMdMMMastTbl(final String fieldvalues)
			throws IOException {
		CSVSerializer<MdMMMastTbl> serializer2 = new CSVSerializer<MdMMMastTbl>();
		return serializer2.deserializeCsv(fieldvalues, MdMMMastTbl.class);
	}
	
	private static MaintenanceTbl getMdMastTbl(final String fieldvalues)
			throws IOException {
		CSVSerializer<MdMastTbl> serializer3 = new CSVSerializer<MdMastTbl>();
		return serializer3.deserializeCsv(fieldvalues, MdMastTbl.class);
	}    
    
}
