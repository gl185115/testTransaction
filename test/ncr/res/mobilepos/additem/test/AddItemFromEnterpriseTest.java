package ncr.res.mobilepos.additem.test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import ncr.res.mobilepos.barcodeassignment.factory.BarcodeAssignmentFactory;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.promotion.resource.PromotionResource;
import ncr.res.mobilepos.searchapi.helper.UrlConnectionHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

@RunWith(PowerMockRunner.class) 
public class AddItemFromEnterpriseTest {
	PromotionResource testpromotionResource = new PromotionResource();
	SQLServerItemDAO sqlItemDaoMock = null;
	
	@PrepareForTest({UrlConnectionHelper.class, DAOFactory.class})
	@Test
	public void test() {
		try {
			// set tp
			DebugLogger.initInstance("C:\\software\\ncr\\res\\dbg", 9);
			MemberModifier.field(PromotionResource.class, "tp").set(testpromotionResource, DebugLogger.getDbgPrinter(1, AddItemFromEnterpriseTest.class));
			
			// set barcodeassignment
			BarcodeAssignmentFactory.initialize("test\\resources\\para");
			MemberModifier.field(PromotionResource .class, "barcodeAssignment").set(testpromotionResource, BarcodeAssignmentFactory.getInstance());
		
			// Mock http response from enterprise url
			GlobalConstant.setEnterpriseServerUri("https://localhost:8443/resTransaction/rest/");
			GlobalConstant.setEnterpriseServerTimeout("3000");
			PowerMockito.mockStatic(UrlConnectionHelper.class);
			String itemJson = "{\"transaction\":{\"sale\":{\"actualSalesUnitPrice\":\"1000\",\"itemId\":\"4957669206754\",\"description\":{\"en\":\"ItemA\",\"ja\":\"ItemA\"},\"regularSalesUnitPrice\":\"1000\",\"discount\":\"0\",\"discountFlag\":\"1\",\"discountAmount\":\"0\",\"department\":\"0802\",\"discountable\":\"false\",\"taxRate\":\"0\",\"taxType\":\"0\",\"discountType\":\"0\",\"subNum1\":\"0\",\"dptDiscountType\":\"1\",\"line\":\"1000\",\"companyId\":\"01\",\"promotionNo\":\"null\",\"mdType\":\"null\",\"hostFlag\":\"1\",\"salesNameSource\":\"1\",\"sku\":\"null\",\"md01\":\"null\",\"md02\":\"null\",\"md03\":\"null\",\"md04\":\"null\",\"md05\":\"null\",\"md06\":\"null\",\"md07\":\"null\",\"md08\":\"null\",\"md09\":\"null\",\"md10\":\"null\",\"md11\":\"null\",\"md12\":\"null\",\"md13\":\"null\",\"md14\":\"null\",\"md15\":\"null\",\"md16\":\"null\",\"mdNameLocal\":\"null\",\"mdKanaName\":\"null\",\"diacountRate\":\"0\",\"discountAmt\":0,\"discountClass\":\"100\",\"nonSales\":\"0\",\"subInt10\":\"0\",\"ageRestrictedFlag\":\"0\",\"eventSalesPrice\":0,\"empPrice1\":\"0\",\"orgSalesPrice1\":\"0\",\"ruleQuantity1\":\"0\",\"ruleQuantity2\":\"0\",\"ruleQuantity3\":\"0\",\"conditionPrice1\":\"0\",\"conditionPrice2\":\"0\",\"conditionPrice3\":\"0\",\"averagePrice1\":\"0\",\"averagePrice2\":\"0\",\"averagePrice3\":\"0\",\"replaceSupportdiscountAmt\":\"0\",\"subNum2\":\"0\",\"oldPrice\":\"0\",\"costPrice1\":\"0\",\"makerPrice\":\"0\",\"pluPrice\":\"0\",\"discountRate\":\"0\",\"discountCount\":\"0\",\"salesPrice2\":\"1000\",\"paymentType\":\"0\",\"subCode1\":\"null\",\"subCode2\":\"null\",\"subCode3\":\"null\",\"promotionId\":\"null\",\"dptSubNum1\":\"null\",\"dptSubNum2\":\"null\",\"dptSubNum3\":\"null\",\"dptSubNum4\":\"null\",\"premiumList\":\"null\",\"itemClass\":\"null\",\"couponFlag\":\"null\",\"retailStoreId\":\"0247\",\"eventId\":\"null\",\"eventName\":\"null\",\"pstype\":\"null\",\"mixMatchCode\":\"null\",\"mdVender\":\"null\",\"salesPriceFrom\":\"null\",\"conn1\":\"null\",\"conn2\":\"null\",\"publishingCode\":\"null\",\"dptNameLocal\":\"null\",\"classNameLocal\":\"null\",\"groupName\":\"null\",\"groupID\":\"null\",\"nameText\":\"null\",\"dptSubCode1\":\"null\",\"oldSku\":\"null\",\"sizeCode\":\"null\",\"colorCode\":\"null\",\"qrPromotionId\":\"null\",\"qrCodeList\":\"null\",\"couponNo\":\"null\"}}}";
			String deptJson = "{\"ncrwssresultCode\": \"0\", \"department\": {\"departmentID\": \"0802\", \"departmentName\": {\"en\": \"ItemA\", \"ja\": \"ItemB\"}, taxRate: \"0\", \"taxType\": \"0\", \"discountType\": \"0\", \"nonSales\": \"0\", \"subCode1\": \"null\", \"subNum1\": \"null\", \"subNum2\": \"null\", \"subNum3\": \"null\", \"subNum4\": \"null\", \"groupID\":\"0001\", \"groupName\":\"0001\", \"retailStoreID\": \"0247\"}}";
			String mdNameJson = "{\"transaction\": { \"sale\": {\"mdNameLocal\": \"ItemA\" }}}";
			try {
				PowerMockito.when(UrlConnectionHelper.connectionHttpsForGet("https://localhost:8443/resTransaction/rest/resTransaction/rest/remoteitem/item_getremoteinfo?storeId=0247&pluCode=4957669206754&companyId=01&businessDate=2016-07-21", 3000))
					.thenReturn(new JSONObject(itemJson));
				PowerMockito.when(UrlConnectionHelper.connectionHttpsForGet("https://localhost:8443/resTransaction/rest/resTransaction/rest/enterprisedpt/department_getremoteinfo?companyId=01&retailStoreId=0247&codeTemp=0802&searchRetailStoreID=0247", 3000))
					.thenReturn(new JSONObject(deptJson));
				PowerMockito.when(UrlConnectionHelper.connectionHttpsForGet("https://localhost:8443/resTransaction/rest/resTransaction/rest/remoteitem/mdName_getremoteinfo?companyId=01&retailStoreId=0247&ItemCode=4957669206754", 3000))
					.thenReturn(new JSONObject(mdNameJson));
			} catch (KeyManagementException | NoSuchAlgorithmException
					| IOException | JSONException e) {
				e.printStackTrace();
			}
			
			// Mock SQLServerItemDAO to make local db returns with item not found
			GlobalConstant.setPriceIncludeTaxKey("0");
			sqlItemDaoMock = PowerMockito.mock(SQLServerItemDAO.class);
			PowerMockito.when(sqlItemDaoMock.getItemByPLU(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyInt(), Matchers.anyString())).thenReturn(null);
			
			// Mock dao calls to proceed
			PowerMockito.doNothing().when(sqlItemDaoMock).getPremiumitemInfo(Matchers.anyString(), Matchers.any(Item.class), Matchers.anyString(), Matchers.anyString());
			PowerMockito.doNothing().when(sqlItemDaoMock).getCouponInfo(Matchers.anyString(), Matchers.any(Item.class), Matchers.anyString(), Matchers.anyString());
			PowerMockito.when(sqlItemDaoMock.isHasReplaceSupportDetailInfo(Matchers.anyString(), Matchers.any(Item.class), Matchers.anyString(), Matchers.anyString())).thenReturn(false);
			DAOFactory daoFacMock = PowerMockito.mock(DAOFactory.class);
			PowerMockito.when(daoFacMock.getItemDAO()).thenReturn(sqlItemDaoMock);
			PowerMockito.mockStatic(DAOFactory.class);
			PowerMockito.when(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER)).thenReturn(daoFacMock);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Invoke BeginTransaction & ItemEntry
		String beginTxJson = "{\"TransactionMode\":\"0\",\"OperatorID\":\"9011011\",\"BeginDateTime\":\"2016-12-13T13:31:56.0Z\"}";
		testpromotionResource.beginTransaction("0247", "0004", "1000", "01", beginTxJson);
		String itemEntryTxJson = "{\"EntryFlag\":\"true\",\"Sale\":{\"ItemEntryID\":1,\"ItemID\":\""
				+ "4957669206754"
				+ "\",\"ItemIDType\":\"EAN13\",\"Department\":\"\",\"Quantity\":1}}";
		testpromotionResource.itemEntry("0247", "0004", "1000", itemEntryTxJson, "01", null, "2016-07-21");
		PowerMockito.verifyStatic();
	}
}
