package testrail;

import java.io.FileInputStream;
import java.util.Properties;

public class Settings {
	
	private static String TESTRAILAPI;
	private static String USERNAME;
	private static String PASSWORD;
	private static String PROJECTID;
	private static String TestRunID;
	private static String ENV;
	private static String KEY;
	private static String URL;
	private static String WSM_Username;
	private static String WSM_Password;
	private static String ScreenshotPath;
	private static String UploadUsers;
	private static String UploadUserGroups;
	private static String UploadRoles;
	private static String UploadServerGroups;
	private static String UploadObjectGroups;
	private static String UploadAudits;
	private static String UploadTrusts;
	
	
	private static Properties propertiesSetting = null;
	
	
	public static  void read() throws Exception {
		if (propertiesSetting == null) {
			propertiesSetting = new Properties();
			propertiesSetting.load(new FileInputStream("File.properties"));

			TESTRAILAPI=propertiesSetting.getProperty("TESTRAILAPI");
			USERNAME=propertiesSetting.getProperty("USERNAME");
			PASSWORD=propertiesSetting.getProperty("PASSWORD");
			PROJECTID=propertiesSetting.getProperty("PROJECTID");
			TestRunID=propertiesSetting.getProperty("TestRunID");
			ENV=propertiesSetting.getProperty("ENV");
			KEY=propertiesSetting.getProperty("KEY");
			
			URL=propertiesSetting.getProperty("URL");
			WSM_Username= propertiesSetting.getProperty("WSM_Username");
			WSM_Password= propertiesSetting.getProperty("WSM_Password");
			ScreenshotPath=propertiesSetting.getProperty("ScreenshotPath");
			UploadUsers=propertiesSetting.getProperty("UploadUsers");
			UploadUserGroups=propertiesSetting.getProperty("UploadUserGroups");
			UploadRoles=propertiesSetting.getProperty("UploadRoles");
			UploadServerGroups=propertiesSetting.getProperty("UploadServerGroups");
			UploadObjectGroups=propertiesSetting.getProperty("UploadObjectGroups");
			UploadAudits=propertiesSetting.getProperty("UploadAudits");
			UploadTrusts=propertiesSetting.getProperty("UploadTrusts");
			
			
		}
	}
	
	
	

	public static String getPASSWORD() {
		return PASSWORD;
	}

	public static void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public static String getPROJECTID() {
		return PROJECTID;
	}

	public static void setPROJECTID(String pROJECTID) {
		PROJECTID = pROJECTID;
	}

	public static String getTestRunID() {
		return TestRunID;
	}

	public static void setTestRunID(String testRunID) {
		TestRunID = testRunID;
	}

	public static String getENV() {
		return ENV;
	}

	public static void setENV(String eNV) {
		ENV = eNV;
	}

	public static String getKEY() {
		return KEY;
	}

	public static void setKEY(String kEY) {
		KEY = kEY;
	}

	public static Properties getPropertiesSetting() {
		return propertiesSetting;
	}

	public static void setPropertiesSetting(Properties propertiesSetting) {
		Settings.propertiesSetting = propertiesSetting;
	}

	
	public static String getScreenshotPath() {
		return ScreenshotPath;
	}


	public static void setScreenshotPath(String screenshotPath) {
		ScreenshotPath = screenshotPath;
	}

	
	
	public static String getWSM_Username() {
		return WSM_Username;
	}

	public static void setWSM_Username(String wSM_Username) {
		WSM_Username = wSM_Username;
	}

	public static String getWSM_Password() {
		return WSM_Password;
	}

	public static void setWSM_Password(String wSM_Password) {
		WSM_Password = wSM_Password;
	}
		
	

	public static String getURL() {
		return URL;
	}

	public static void setURL(String uRL) {
		URL = uRL;
	}

	public static String getTESTRAILAPI() {
		return TESTRAILAPI;
	}

	public static void setTESTRAILAPI(String tESTRAILAPI) {
		TESTRAILAPI = tESTRAILAPI;
	}

	public static String getUSERNAME() {
		return USERNAME;
	}

	public static void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public static String getUploadUsers() {
		return UploadUsers;
	}

	public static void setUploadUsers(String uploadUsers) {
		UploadUsers = uploadUsers;
	}




	public static String getUploadUserGroups() {
		return UploadUserGroups;
	}




	public static void setUploadUserGroups(String uploadUserGroups) {
		UploadUserGroups = uploadUserGroups;
	}




	public static String getUploadRoles() {
		return UploadRoles;
	}




	public static void setUploadRoles(String uploadRoles) {
		UploadRoles = uploadRoles;
	}




	public static String getUploadServerGroups() {
		return UploadServerGroups;
	}




	public static void setUploadServerGroups(String uploadServerGroups) {
		UploadServerGroups = uploadServerGroups;
	}




	public static String getUploadObjectGroups() {
		return UploadObjectGroups;
	}




	public static void setUploadObjectGroups(String uploadObjectGroups) {
		UploadObjectGroups = uploadObjectGroups;
	}




	public static String getUploadAudits() {
		return UploadAudits;
	}




	public static void setUploadAudits(String uploadAudits) {
		UploadAudits = uploadAudits;
	}




	public static String getUploadTrusts() {
		return UploadTrusts;
	}




	public static void setUploadTrusts(String uploadTrusts) {
		UploadTrusts = uploadTrusts;
	}




		
	

}
