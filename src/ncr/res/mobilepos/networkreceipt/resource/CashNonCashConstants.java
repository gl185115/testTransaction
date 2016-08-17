package ncr.res.mobilepos.networkreceipt.resource;

/**
* Constants
*/

public class CashNonCashConstants {
	
	public static final int ROLLS_500 = 50;
	
	public static final int ROLLS_OTHERS = 20;
	
	public static final int KIND_500 = 500;
	
	public static final String NCCREDIT_LABEL = "＜ ク レ ジ ッ ト ＞";
	
	public static final String NCCOMMODITYTICKET_LABEL = "商 品 券";
	
	public static final String NCGROUPTICKET_LABEL = "＜券＞";
	
	public static final String NCCOMMODITYTICKETA_LABEL = "ゼビオグループ券";
	
	public static final String NCOTHERS_LABEL = "そ の 他";
	
	public static final int DENOMLEN = 8;
	
	public static final int CHANGEMACHINEAMTLEN = 10;
	
	public static final int CASHDRAWERAMTLEN = 11;
	
	public static final int DENOMQTY_BALCASH = 14;
	
	public static final int CHANGEMACHINEDRAWERTOTALLEN = 10;
	
	public static final int CHANGEMACHINEDRAWERTOTAL_BALLEN = 17;
	
	public static final String VOUCHERCOMMON = "1";
	
	public static final String VOUCHEROTHERS = "2";
	
	public static final int PAYINPLAN_QTYLEN = 14;
	
	public static final int PAYINPLAN_DRAWERTOTALLEN = 17;
	
	public final int DetermineRolls(int kind) {
		int result;
		
		result = ROLLS_OTHERS;
		if (kind == KIND_500) {
			result = ROLLS_500;
		}
		return result;
	}
	
	public final int GetCorrectLen(String ctrlType, String lenType) {
		int result = 0;
		
		switch (ctrlType) {
		
		    case "PayOut":
		    case "PayIn":
		    	switch (lenType) {
	    	        case "col1":
	    	        	result = DENOMLEN;
	    		        break;
	    	        case "col2":
	    	        	result = CHANGEMACHINEAMTLEN;
	    	    	    break;
	    	        case "col3":
	    	        	result = CASHDRAWERAMTLEN;
	    	    	    break;
	    	        case "col4":
	    	        	result = CHANGEMACHINEDRAWERTOTALLEN;
	    	        	break;
	    	        default:
	    	        	result = 5;
	    	    	    break;
	    	    }
		    	break;
		    case "Balancing":
		    	switch (lenType) {
		    	    case "col1":
		    	    	result = DENOMLEN;
		    		    break;
		    	    case "col2":
		    	    	result = DENOMQTY_BALCASH;
		    	    	break;
		    	    case "col4":
		    	    	result = CHANGEMACHINEDRAWERTOTAL_BALLEN;
		    	    	break;
		    	    default:
		    	    	result = 5;
		    	    	break;
		    	}
		    	break;
		    case "PayInPlan":
		    	switch (lenType) {
			    	case "col1":
			    		result = DENOMLEN;
			    		break;
			    	case "col2":
			    		result = PAYINPLAN_QTYLEN;
			    		break;
			    	case "col3":
			    		result = PAYINPLAN_DRAWERTOTALLEN;
			    		break;
			    	default:
			    		result = 5;
			    		break;
		    	}
		    	break;
		    default:
		    	result = 5;
		    	break;
		}
		
		return result;
	}
	
}