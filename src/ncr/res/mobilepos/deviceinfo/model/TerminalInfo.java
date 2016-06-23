package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TerminalInfo")
@ApiModel(value="TerminalInfo")
public class TerminalInfo {
	@XmlElement(name = "CompanyId")
	private String companyId;
	@XmlElement(name = "StoreId")
	private String storeId;
	@XmlElement(name = "TerminalId")
	private String terminalId;
	@XmlElement(name = "FloorId")
	private String floorId;
	@XmlElement(name = "TerminalName")
	private String terminalName;
	@XmlElement(name = "IPAddress")
	private String ipAddress;
	@XmlElement(name = "StoreClass")
	private String storeClass;
	@XmlElement(name = "TerminalType")
	private String terminalType;
	@XmlElement(name = "TillType")
	private String tillType;
	@XmlElement(name = "RelationType")
	private String relationType;
	@XmlElement(name = "ConnectionFlag1")
	private int connectionFlag1;
	@XmlElement(name = "ConnectionFlag2")
	private int connectionFlag2;
	@XmlElement(name = "ConnectionFlag3")
	private int connectionFlag3;
	@XmlElement(name = "ConnectionFlag4")
	private int connectionFlag4;
	@XmlElement(name = "ConnectionFlag5")
	private int connectionFlag5;
	@XmlElement(name = "ConnectionFlag6")
	private int connectionFlag6;
	@XmlElement(name = "ConnectionFlag7")
	private int connectionFlag7;
	@XmlElement(name = "ConnectionFlag8")
	private int connectionFlag8;
	private int connectionFlag9;
	private int connectionFlag10;
	private int connectionFlag11;
	private int connectionFlag12;
	private int connectionFlag13;
	private int connectionFlag14;
	private int connectionFlag15;
	@XmlElement(name = "LogoFileName")
	private String logoFileName;
	@XmlElement(name = "InshiFileName")
	private String inshiFileName;
	@XmlElement(name = "Note")
	private String note;
	@XmlElement(name = "SubCode1")
	private String subCode1;
	@XmlElement(name = "SubCode2")
	private String subCode2;
	private String subCode3;
	private String subCode4;
	private String subCode5;
	private int subNum1;
	private int subNum2;
	private int subNum3;
	private int subNum4;
	private int subNum5;
	@XmlElement(name = "CompanyName")
	private String companyName;

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@ApiModelProperty(value="会社コード", notes="会社コード")
	public String getCompanyId() {
		return companyId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@ApiModelProperty(value="店舗コード", notes="店舗コード")
	public String getStoreId() {
		return storeId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	@ApiModelProperty(value="端末NO", notes="端末NO")
	public String getTerminalId() {
		return terminalId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	@ApiModelProperty(value="フロアコード", notes="フロアコード")
	public String getFloorId() {
		return floorId;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	@ApiModelProperty(value="端末ID(ホスト名)", notes="端末ID(ホスト名)")
	public String getTerminalName() {
		return terminalName;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@ApiModelProperty(value="IPアドレス", notes="IPアドレス")
	public String getIpAddress() {
		return ipAddress;
	}

	public void setStoreClass(String storeClass) {
		this.storeClass = storeClass;
	}

	@ApiModelProperty(value="本部区分", notes="本部区分")
	public String getStoreClass() {
		return storeClass;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	@ApiModelProperty(value="端末区分", notes="端末区分")
	public String getTerminalType() {
		return terminalType;
	}

	public void setTillType(String tillType) {
		this.tillType = tillType;
	}

	@ApiModelProperty(value="POS区分", notes="POS区分")
	public String getTillType() {
		return tillType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	@ApiModelProperty(value="親子区分", notes="親子区分")
	public String getRelationType() {
		return relationType;
	}

	public void setConnectionFlag1(int connectionFlag1) {
		this.connectionFlag1 = connectionFlag1;
	}

	@ApiModelProperty(value="接続フラグ１:プリンタ", notes="接続フラグ１:プリンタ")
	public int getConnectionFlag1() {
		return connectionFlag1;
	}

	public void setConnectionFlag2(int connectionFlag2) {
		this.connectionFlag2 = connectionFlag2;
	}

	@ApiModelProperty(value="接続フラグ２:スキャナ", notes="接続フラグ２:スキャナ")
	public int getConnectionFlag2() {
		return connectionFlag2;
	}

	public void setConnectionFlag3(int connectionFlag3) {
		this.connectionFlag3 = connectionFlag3;
	}

	@ApiModelProperty(value="接続フラグ３:ドロア", notes="接続フラグ３:ドロア")
	public int getConnectionFlag3() {
		return connectionFlag3;
	}

	public void setConnectionFlag4(int connectionFlag4) {
		this.connectionFlag4 = connectionFlag4;
	}

	@ApiModelProperty(value="接続フラグ４:カードリーダー", notes="接続フラグ４:カードリーダー")
	public int getConnectionFlag4() {
		return connectionFlag4;
	}

	public void setConnectionFlag5(int connectionFlag5) {
		this.connectionFlag5 = connectionFlag5;
	}

	@ApiModelProperty(value="接続フラグ５:自動釣銭", notes="接続フラグ５:自動釣銭")
	public int getConnectionFlag5() {
		return connectionFlag5;
	}

	public void setConnectionFlag6(int connectionFlag6) {
		this.connectionFlag6 = connectionFlag6;
	}

	@ApiModelProperty(value="接続フラグ６:OES", notes="接続フラグ６:OES")
	public int getConnectionFlag6() {
		return connectionFlag6;
	}

	public void setConnectionFlag7(int connectionFlag7) {
		this.connectionFlag7 = connectionFlag7;
	}

	@ApiModelProperty(value="接続フラグ７:キッチンプリンタ", notes="接続フラグ７:キッチンプリンタ")
	public int getConnectionFlag7() {
		return connectionFlag7;
	}

	public void setConnectionFlag8(int connectionFlag8) {
		this.connectionFlag8 = connectionFlag8;
	}

	@ApiModelProperty(value="接続フラグ８:電子マネー", notes="接続フラグ８:電子マネー")
	public int getConnectionFlag8() {
		return connectionFlag8;
	}

	public void setConnectionFlag9(int connectionFlag9) {
		this.connectionFlag9 = connectionFlag9;
	}

	@ApiModelProperty(value="接続フラグ９:IC PINパッド", notes="接続フラグ９:IC PINパッド")
	public int getConnectionFlag9() {
		return connectionFlag9;
	}

	public void setConnectionFlag10(int connectionFlag10) {
		this.connectionFlag10 = connectionFlag10;
	}

	@ApiModelProperty(value="接続フラグ１０", notes="接続フラグ１０")
	public int getConnectionFlag10() {
		return connectionFlag10;
	}

	public void setConnectionFlag11(int connectionFlag11) {
		this.connectionFlag11 = connectionFlag11;
	}

	@ApiModelProperty(value="接続フラグ１１", notes="接続フラグ１１")
	public int getConnectionFlag11() {
		return connectionFlag11;
	}

	public void setConnectionFlag12(int connectionFlag12) {
		this.connectionFlag12 = connectionFlag12;
	}

	@ApiModelProperty(value="接続フラグ１２", notes="接続フラグ１２")
	public int getConnectionFlag12() {
		return connectionFlag12;
	}

	public void setConnectionFlag13(int connectionFlag13) {
		this.connectionFlag13 = connectionFlag13;
	}

	@ApiModelProperty(value="接続フラグ１３", notes="接続フラグ１３")
	public int getConnectionFlag13() {
		return connectionFlag13;
	}

	public void setConnectionFlag14(int connectionFlag14) {
		this.connectionFlag14 = connectionFlag14;
	}

	@ApiModelProperty(value="接続フラグ１４", notes="接続フラグ１４")
	public int getConnectionFlag14() {
		return connectionFlag14;
	}

	public void setConnectionFlag15(int connectionFlag15) {
		this.connectionFlag15 = connectionFlag15;
	}

	@ApiModelProperty(value="接続フラグ１５", notes="接続フラグ１５")
	public int getConnectionFlag15() {
		return connectionFlag15;
	}

	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}

	@ApiModelProperty(value="ロゴファイル名", notes="ロゴファイル名")
	public String getLogoFileName() {
		return logoFileName;
	}

	public void setInshiFileName(String inshiFileName) {
		this.inshiFileName = inshiFileName;
	}

	@ApiModelProperty(value="印紙ファイル名", notes="印紙ファイル名")
	public String getInshiFileName() {
		return inshiFileName;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@ApiModelProperty(value="メモ", notes="メモ")
	public String getNote() {
		return note;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@ApiModelProperty(value="会社名称", notes="会社名称")
	public String getCompanyName() {
		return companyName;
	}

	public void setSubCode1(String subCode1) {
		this.subCode1 = subCode1;
	}

	@ApiModelProperty(value="領収書用印紙税ビットマップファイル名", notes="領収書用印紙税ビットマップファイル名")
	public String getSubCode1() {
		return subCode1;
	}

	public void setSubCode2(String subCode2) {
		this.subCode2 = subCode2;
	}

	@ApiModelProperty(value="QRコードビットマップファイル名", notes="QRコードビットマップファイル名")
	public String getSubCode2() {
		return subCode2;
	}

	public void setSubCode3(String subCode3) {
		this.subCode3 = subCode3;
	}

	@ApiModelProperty(value="ロゴファイル名(Enterprise用)", notes="ロゴファイル名(Enterprise用)")
	public String getSubCode3() {
		return subCode3;
	}

	public void setSubCode4(String subCode4) {
		this.subCode4 = subCode4;
	}

	@ApiModelProperty(value="予約", notes="予約")
	public String getSubCode4() {
		return subCode4;
	}

	public void setSubCode5(String subCode5) {
		this.subCode5 = subCode5;
	}

	@ApiModelProperty(value="予約", notes="予約")
	public String getSubCode5() {
		return subCode5;
	}

	public void setSubNum1(int subNum1) {
		this.subNum1 = subNum1;
	}

	@ApiModelProperty(value="予約", notes="予約")
	public int getSubNum1() {
		return subNum1;
	}

	public void setSubNum2(int subNum2) {
		this.subNum2 = subNum2;
	}

	@ApiModelProperty(value="予約", notes="予約")
	public int getSubNum2() {
		return subNum2;
	}

	public void setSubNum3(int subNum3) {
		this.subNum3 = subNum3;
	}

	@ApiModelProperty(value="予約", notes="予約")
	public int getSubNum3() {
		return subNum3;
	}

	public void setSubNum4(int subNum4) {
		this.subNum4 = subNum4;
	}

	@ApiModelProperty(value="予約", notes="予約")
	public int getSubNum4() {
		return subNum4;
	}

	public void setSubNum5(int subNum5) {
		this.subNum5 = subNum5;
	}

	@ApiModelProperty(value="予約", notes="予約")
	public int getSubNum5() {
		return subNum5;
	}
}
