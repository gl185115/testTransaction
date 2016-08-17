package ncr.res.mobilepos.exception;

@SuppressWarnings("serial")
public class PrivateKeyException extends Exception {
	public PrivateKeyException(){
		super("Private Key Exception");
	}
	public PrivateKeyException(String msg){
		super(msg);
	}
}
