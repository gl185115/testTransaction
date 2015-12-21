// Copyright(c) 2015 NCR Japan Ltd.
package ncr.res.mobilepos.creditauthorization.helper;

@SuppressWarnings("serial")
public class MobileDecryptException extends Exception {
	public MobileDecryptException(){
		super("Mobile Decription Exception");
	}
	public MobileDecryptException(String msg){
		super(msg);
	}
	public MobileDecryptException(Throwable e){
		super("Mobile Decription Exception", e);
	}
	public MobileDecryptException(String msg, Throwable e){
		super(msg, e);
	}
}
