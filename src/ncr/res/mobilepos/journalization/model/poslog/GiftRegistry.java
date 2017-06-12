package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GiftRegistry")
public class GiftRegistry {

	@XmlElement(name = "Brand")
	private String brand;

	@XmlElement(name = "CardNumber")
	private String cardNumber;

	@XmlElement(name = "AuthorizationNumber")
	private String authorizationNumber;

	@XmlElement(name = "ExpirationDate")
	private String expirationDate;

	@XmlElement(name = "PriorFaceValueAmount")
	private int priorFaceValueAmount;

	@XmlElement(name = "CurrentFaceValueAmount")
	private int currentFaceValueAmount;

	@XmlElement(name = "ChargeAmount")
	private int chargeAmount;

	@XmlElement(name = "JIS1")
	private String jis1;

	@XmlElement(name = "JIS2")
	private String jis2;

	@XmlElement(name = "PIN")
	private String pin;

	public final String getBrand() {
		return this.brand;
	}
	public final void setBrand(String brandToSet) {
		this.brand = brandToSet;
	}

	public final String getCardNumber() {
		return this.cardNumber;
	}
	public final void setCardNumber(String cardNumberToSet) {
		this.cardNumber = cardNumberToSet;
	}

	public final String getAuthorizationNumber() {
		return this.authorizationNumber;
	}
	public final void setAuthorizationNumber(String authorizationNumberToSet) {
		this.authorizationNumber = authorizationNumberToSet;
	}

	public final String getExpirationDate() {
		return this.expirationDate;
	}
	public final void setExpirationDate(String expirationDateToSet) {
		this.expirationDate = expirationDateToSet;
	}

	public final int getPriorFaceValueAmount() {
		return this.priorFaceValueAmount;
	}
	public final void setPriorFaceValueAmount(int priorFaceValueAmountToSet) {
		this.priorFaceValueAmount = priorFaceValueAmountToSet;
	}

	public final int getCurrentFaceValueAmount() {
		return this.currentFaceValueAmount;
	}
	public final void setCurrentFaceValueAmount(int currentFaceValueAmountToSet) {
		this.currentFaceValueAmount = currentFaceValueAmountToSet;
	}

	public final int getChargeAmount() {
		return this.chargeAmount;
	}
	public final void setChargeAmount(int chargeAmountToSet) {
		this.chargeAmount = chargeAmountToSet;
	}

	public final String getJis1() {
		return this.jis1;
	}
	public final void setJis1(String jis1ToSet) {
		this.jis1 = jis1ToSet;
	}

	public final String getJis2() {
		return this.jis2;
	}
	public final void setJis2(String jis2ToSet) {
		this.jis2 = jis2ToSet;
	}

	public final String getPin() {
		return this.pin;
	}
	public final void setPin(String pinToSet) {
		this.pin = pinToSet;
	}
}