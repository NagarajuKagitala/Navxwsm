package Administration;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Common.Roles;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class ImportData 
{
	static WebDriver driver;
	static String URL;
	static String WSM_Username;
	static String WSM_Password;
	static String Screenshotpath;
	static String UploadUsers;
	static String UploadUserGroups;
	static String UploadRoles;
	static String UploadServerGroups;
	static String UploadObjectGroups;
	static String UploadAudits;
	static String UploadTrusts;
	static String ManagemnetData;
	static String ListOfRoles;
	
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		URL=Settings.getURL();
		WSM_Username=Settings.getWSM_Username();
		WSM_Password=Settings.getWSM_Password();
		Screenshotpath=Settings.getScreenshotPath();
		UploadUsers=Settings.getUploadUsers();
		UploadUserGroups=Settings.getUploadUserGroups();
		UploadRoles=Settings.getUploadRoles();
		UploadServerGroups=Settings.getUploadServerGroups();
		UploadObjectGroups=Settings.getUploadObjectGroups();
		UploadAudits=Settings.getUploadAudits();
		UploadTrusts=Settings.getUploadTrusts();
		
	}
	
	@Parameters({"sDriver", "sDriverpath"})
	@Test
	public static void Login(String sDriver, String sDriverpath, ITestContext context) throws Exception
	{
		Settings.read();
		String URL = Settings.getURL();
		String uname=Settings.getWSM_Username();
		String password=Settings.getWSM_Password();
		
		
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
		System.setProperty(sDriver, sDriverpath);
		driver=new ChromeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver= new EdgeDriver();
		}
		else
		{
			System.setProperty(sDriver, sDriverpath);
			driver= new FirefoxDriver();
		}
		
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		
		//Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(10000);
		
		try
		{
			driver.findElement(By.className("cc-compliance")).click();
		}
		catch(Exception e)
		{
			System.out.println("No understood button");
		}
		
		
		try
		{
			String security=driver.findElement(By.xpath("//div[2]/span")).getText();
			
			if(security.equalsIgnoreCase("Refresh Security"))
			{
				System.out.println("no need to change the Menu");
			}
			else
			{
				driver.findElement(By.id("change menu")).click();
			}
		}
		catch (Exception e)
		{
			//Click on settings
			driver.findElement(By.xpath("//div[2]/div[5]")).click();
			Thread.sleep(4000);
			
			//Select top menu
			driver.findElement(By.id("top-m-type")).click();
			Thread.sleep(3000);
			
			//click on save button
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
		}
								
	}
	
	@TestRail(testCaseId=1205)
	@Parameters({"ImportedUserName"})
	@Test(priority=1)
	public void UploadUsers(String ImportedUserName, ITestContext context) throws InterruptedException, AWTException
	{
		//Delete object if exits
		Commonoption(ImportedUserName);
		
		//Select Security manager     
		driver.findElement(By.xpath("//div[3]/app-menu-item-template[2]/div/div")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//app-menu-item-template[2]/div/div[2]/app-menu-item-template/div/div")).click();
		Thread.sleep(8000);          
		
		//Select import by group checkbox
		driver.findElement(By.id("1-choice")).click();
		Thread.sleep(3000);
		
		//Click on next button
		driver.findElement(By.xpath("//button[contains(.,'Next')]")).click();
		Thread.sleep(4000);
		
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", driver.findElement(By.id("fileSelect")));
		Thread.sleep(8000);
			
		
		//Loading a file from the load file option
		String filepath=System.getProperty("user.dir") + "\\" + UploadUsers;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(10000);
	
	    //Click on import button
	    driver.findElement(By.xpath("//button[contains(.,'Import')]")).click();
	    Thread.sleep(10000);
	    
	    //Click on refresh
		driver.findElement(By.xpath("//i[2]")).click();
		Thread.sleep(4000);
		
		//get the management data
		ManagemnetData=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
	    System.out.println("Management data is: " +ManagemnetData);
	    
	    if(ManagemnetData.contains(ImportedUserName))
		{
			System.out.println("User is imported");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "User is imported successfully");
		}
		else
		{
			System.out.println("User is not imported");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "user is not imported");
			driver.findElement(By.id("import user failed")).click();
		}
	    
	    //Delete the user
	    DeleteimportedObject();
	    
	}
	
	@TestRail(testCaseId=1206)
	@Parameters({"GroupManagement", "ImportedUserGroup"})
	@Test(priority=2)
	public void UploadUserGroups(int GroupManagement, String ImportedUserGroup, ITestContext context) throws InterruptedException, AWTException
	{
		//Delete object if exits
		CommonoptionForAll(GroupManagement, ImportedUserGroup);
		
		//Select Security manager     
		driver.findElement(By.xpath("//div[3]/app-menu-item-template[2]/div/div")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//app-menu-item-template[2]/div/div[2]/app-menu-item-template/div/div")).click();
		Thread.sleep(8000);          
		
		//Select import by group checkbox
		driver.findElement(By.id("1-choice")).click();
		Thread.sleep(3000);
		
		//Click on next button
		driver.findElement(By.xpath("//button[contains(.,'Next')]")).click();
		Thread.sleep(4000);
		
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", driver.findElement(By.id("fileSelect")));
		Thread.sleep(8000);
			
		
		//Loading a file from the load file option
		String filepath=System.getProperty("user.dir") + "\\" + UploadUserGroups;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(10000);
	
	    //Click on import button
	    driver.findElement(By.xpath("//button[contains(.,'Import')]")).click();
	    Thread.sleep(10000);
	    
	    //Click on refresh
		driver.findElement(By.xpath("//i[2]")).click();
		Thread.sleep(4000);
		
		//get the management data
		ManagemnetData=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
	    System.out.println("Management data is: " +ManagemnetData);
	    
	    if(ManagemnetData.contains(ImportedUserGroup))
		{
			System.out.println("User group is imported");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "User group is imported successfully");
		}
		else
		{
			System.out.println("User group is not imported");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "user group is not imported");
			driver.findElement(By.id("import user group failed")).click();
		}
	    
	    //Delete the user
	    DeleteimportedObject();
	    
	}
	
	@TestRail(testCaseId=1207)
	@Parameters({"RoleManagement", "ImportedRole"})
	@Test(priority=3)
	public void UploadRole(int RoleManagement, String ImportedRole, ITestContext context) throws InterruptedException, AWTException
	{
		//Go to role management
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div/i")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div[2]/app-menu-item-template[3]/div/div")).click();
		Thread.sleep(8000);
		
		//Delete object if exits
		Roles ob=new Roles();
		ob.DeleteRole(ImportedRole, driver);
		
		//Select Security manager     
		driver.findElement(By.xpath("//div[3]/app-menu-item-template[2]/div/div")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//app-menu-item-template[2]/div/div[2]/app-menu-item-template/div/div")).click();
		Thread.sleep(8000);          
		
		//Select import features/permits checkbox
		driver.findElement(By.id("0-choice")).click();
		Thread.sleep(3000);
		
		//Click on next button
		driver.findElement(By.xpath("//button[contains(.,'Next')]")).click();
		Thread.sleep(4000);
		
		//give the new role name
		driver.findElement(By.xpath("//div[5]/input")).clear();
		driver.findElement(By.xpath("//div[5]/input")).sendKeys(ImportedRole);
		Thread.sleep(3000);
		
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", driver.findElement(By.id("fileSelect")));
		Thread.sleep(8000);
			
		
		//Loading a file from the load file option
		String filepath=System.getProperty("user.dir") + "\\" + UploadRoles;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(10000);
	
	    //Click on import button
	    driver.findElement(By.xpath("//button[contains(.,'Import')]")).click();
	    Thread.sleep(10000);
	    
	    //Click on refresh
		driver.findElement(By.xpath("//i[2]")).click();
		Thread.sleep(4000);
		
		Roleslist();
		System.out.println("List of roles are ----: "+ListOfRoles);
		
			    
	    if(ListOfRoles.contains(ImportedRole))
		{
			System.out.println("Role is imported");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Role is imported successfully");
		}
		else
		{
			System.out.println("Role is not imported");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Role is not imported");
			driver.findElement(By.id("import Role failed")).click();
		}
	    
	    //Delete object if exits
		//Roles ob=new Roles();
		ob.DeleteRole(ImportedRole, driver);
	    
	}
	
	@TestRail(testCaseId=1208)
	@Parameters({"ServerGroupManagement", "ImportedServerGroup"})
	@Test(priority=4)
	public void UploadServerGroups(int ServerGroupManagement, String ImportedServerGroup, ITestContext context) throws InterruptedException, AWTException
	{
		//Delete object if exits
		CommonoptionForAll(ServerGroupManagement, ImportedServerGroup);
		
		//Select Security manager     
		driver.findElement(By.xpath("//div[3]/app-menu-item-template[2]/div/div")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//app-menu-item-template[2]/div/div[2]/app-menu-item-template/div/div")).click();
		Thread.sleep(8000);          
		
		//Select import by group checkbox
		driver.findElement(By.id("1-choice")).click();
		Thread.sleep(3000);
		
		//Click on next button
		driver.findElement(By.xpath("//button[contains(.,'Next')]")).click();
		Thread.sleep(4000);
		
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", driver.findElement(By.id("fileSelect")));
		Thread.sleep(8000);
			
		
		//Loading a file from the load file option
		String filepath=System.getProperty("user.dir") + "\\" + UploadServerGroups;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(10000);
	
	    //Click on import button
	    driver.findElement(By.xpath("//button[contains(.,'Import')]")).click();
	    Thread.sleep(10000);
	    
	    //Click on refresh
		driver.findElement(By.xpath("//i[2]")).click();
		Thread.sleep(4000);
		
		//get the management data
		ManagemnetData=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
	    System.out.println("Management data is: " +ManagemnetData);
	    
	    if(ManagemnetData.contains(ImportedServerGroup))
		{
			System.out.println("Server group is imported");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Server group is imported successfully");
		}
		else
		{
			System.out.println("Server group is not imported");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Server group is not imported");
			driver.findElement(By.id("import Server group failed")).click();
		}
	    
	    //Delete the user
	    DeleteimportedObject();
	    
	}
	
	@TestRail(testCaseId=1209)
	@Parameters({"ObjectGroupManagement", "ImportedObjectGroup"})
	@Test(priority=5)
	public void UploadObjectGroups(int ObjectGroupManagement, String ImportedObjectGroup, ITestContext context) throws InterruptedException, AWTException
	{
		//Delete object if exits
		CommonoptionForAll(ObjectGroupManagement, ImportedObjectGroup);
		
		//Select Security manager     
		driver.findElement(By.xpath("//div[3]/app-menu-item-template[2]/div/div")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//app-menu-item-template[2]/div/div[2]/app-menu-item-template/div/div")).click();
		Thread.sleep(8000);          
		
		//Select import by group checkbox
		driver.findElement(By.id("1-choice")).click();
		Thread.sleep(3000);
		
		//Click on next button
		driver.findElement(By.xpath("//button[contains(.,'Next')]")).click();
		Thread.sleep(4000);
		
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", driver.findElement(By.id("fileSelect")));
		Thread.sleep(8000);
			
		
		//Loading a file from the load file option
		String filepath=System.getProperty("user.dir") + "\\" + UploadObjectGroups;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(10000);
	
	    //Click on import button
	    driver.findElement(By.xpath("//button[contains(.,'Import')]")).click();
	    Thread.sleep(10000);
	    
	    //Click on refresh
		driver.findElement(By.xpath("//i[2]")).click();
		Thread.sleep(4000);
		
		//get the management data
		ManagemnetData=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
	    System.out.println("Management data is: " +ManagemnetData);
	    
	    if(ManagemnetData.contains(ImportedObjectGroup))
		{
			System.out.println("Object group is imported");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Object group is imported successfully");
		}
		else
		{
			System.out.println("Object group is not imported");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Object group is not imported");
			driver.findElement(By.id("import Object group failed")).click();
		}
	    
	    //Delete the user
	    DeleteimportedObject();
	    
	}
	
	@TestRail(testCaseId=1210)
	@Parameters({"AuditManagement", "ImportedAudit"})
	@Test(priority=6)
	public void UploadAudits(int AuditManagement, String ImportedAudit, ITestContext context) throws InterruptedException, AWTException
	{
		//Delete object if exits
		CommonoptionForAll(AuditManagement, ImportedAudit);
		
		//Select Security manager     
		driver.findElement(By.xpath("//div[3]/app-menu-item-template[2]/div/div")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//app-menu-item-template[2]/div/div[2]/app-menu-item-template/div/div")).click();
		Thread.sleep(8000);          
		
		//Select import by group checkbox
		driver.findElement(By.id("1-choice")).click();
		Thread.sleep(3000);
		
		//Click on next button
		driver.findElement(By.xpath("//button[contains(.,'Next')]")).click();
		Thread.sleep(4000);
		
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", driver.findElement(By.id("fileSelect")));
		Thread.sleep(8000);
			
		
		//Loading a file from the load file option
		String filepath=System.getProperty("user.dir") + "\\" + UploadAudits;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(10000);
	
	    //Click on import button
	    driver.findElement(By.xpath("//button[contains(.,'Import')]")).click();
	    Thread.sleep(10000);
	    
	    //Click on refresh
		driver.findElement(By.xpath("//i[2]")).click();
		Thread.sleep(4000);
		
		//get the management data
		ManagemnetData=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
	    System.out.println("Management data is: " +ManagemnetData);
	    
	    if(ManagemnetData.contains(ImportedAudit))
		{
			System.out.println("Audit is imported");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Audit is imported successfully");
		}
		else
		{
			System.out.println("Audit is not imported");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Audit is not imported");
			driver.findElement(By.id("import Audit failed")).click();
		}
	    
	    //Delete the user
	    DeleteimportedObject();
	    
	}
	
	@TestRail(testCaseId=1211)
	@Parameters({"TrustManagement", "ImportedTrust"})
	@Test(priority=7)
	public void UploadTrusts(int TrustManagement, String ImportedTrust, ITestContext context) throws InterruptedException, AWTException
	{
		//Delete object if exits
		CommonoptionForAll(TrustManagement, ImportedTrust);
		
		//Select Security manager     
		driver.findElement(By.xpath("//div[3]/app-menu-item-template[2]/div/div")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//app-menu-item-template[2]/div/div[2]/app-menu-item-template/div/div")).click();
		Thread.sleep(8000);          
		
		//Select import by group checkbox
		driver.findElement(By.id("1-choice")).click();
		Thread.sleep(3000);
		
		//Click on next button
		driver.findElement(By.xpath("//button[contains(.,'Next')]")).click();
		Thread.sleep(4000);
		
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", driver.findElement(By.id("fileSelect")));
		Thread.sleep(8000);
			
		
		//Loading a file from the load file option
		String filepath=System.getProperty("user.dir") + "\\" + UploadTrusts;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(10000);
	
	    //Click on import button
	    driver.findElement(By.xpath("//button[contains(.,'Import')]")).click();
	    Thread.sleep(10000);
	    
	    //Click on refresh
		driver.findElement(By.xpath("//i[2]")).click();
		Thread.sleep(4000);
		
		//get the management data
		ManagemnetData=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
	    System.out.println("Management data is: " +ManagemnetData);
	    
	    if(ManagemnetData.contains(ImportedTrust))
		{
			System.out.println("Trust is imported");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Trust is imported successfully");
		}
		else
		{
			System.out.println("Trust is not imported");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Trust is not imported");
			driver.findElement(By.id("import Trust failed")).click();
		}
	    
	    //Delete the user
	    DeleteimportedObject();
	    
	}
	
	
	public void DeleteimportedObject() throws InterruptedException
	{		
		//click on checkbox and choose Delete option
		driver.findElement(By.xpath("//td/input")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//li[contains(.,'Delete')]")).click();
		Thread.sleep(8000);
		
		//Click on confirmation yes
		driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
		Thread.sleep(6000);
		
		//Click on refresh
		driver.findElement(By.xpath("//i[2]")).click();
		Thread.sleep(4000);
		
	}
	
	public void Commonoption(String ImportedUserName) throws InterruptedException
	{
		//Select Security manager
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div/i")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div[2]/app-menu-item-template/div/div/span")).click();
		Thread.sleep(8000);
		
		try
		{
			//Search with name
			driver.findElement(By.xpath("//input")).clear();
			driver.findElement(By.xpath("//input")).sendKeys(ImportedUserName);
			
			//get the management data
			String managemnetdata=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
			System.out.println("user data is: " +managemnetdata);
			
			if(managemnetdata.contains(ImportedUserName))
			{
				System.out.println("Object already exist");
				DeleteimportedObject();
			}
			else
			{
				System.out.println("Object not existing");
			}
		}
		catch(Exception e)
		{
			System.out.println("Object is not existing");
		}	
		
	}
	
	public void CommonoptionForAll(int value, String ImportedUserName) throws InterruptedException
	{
		//Select Security manager
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div/i")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div[2]/app-menu-item-template["+ value +"]/div/div/span")).click();
		Thread.sleep(8000);
		
		try
		{
			//Search with name
			driver.findElement(By.xpath("//input")).clear();
			driver.findElement(By.xpath("//input")).sendKeys(ImportedUserName);
			
			//get the management data
			String managemnetdata=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
			System.out.println("user data is: " +managemnetdata);
			
			if(managemnetdata.contains(ImportedUserName))
			{
				System.out.println("Object already exist");
				DeleteimportedObject();
			}
			else
			{
				System.out.println("Object not existing");
			}
		}
		catch(Exception e)
		{
			System.out.println("Object is not existing");
		}	
		
	}
	
	public void Roleslist()
	{
		try
		{
			WebElement ele=driver.findElement(By.id("role-table")).findElement(By.tagName("tr"));
			List<WebElement> ths=ele.findElements(By.tagName("th"));
			System.out.println("no of roles are: " +ths.size());
			
			StringBuffer buffer=new StringBuffer();
			for(WebElement name:ths)
			{
				String Role=name.getText();
				//System.out.println("Role is: " +Role);
				buffer.append(Role);
				buffer.append(",");
			}
			
			ListOfRoles=buffer.toString();
			//System.out.println("List of roles are: "+ListOfRoles);
		}
		catch(Exception e1)
		{
			System.out.println("Role is not existing");
		}	
	}
	
	
	@Test(priority=21)
	public void Logout() throws InterruptedException
	{
		driver.findElement(By.cssSelector(".fa-sign-out-alt")).click();
		Thread.sleep(2000);
		
		driver.close();
	}
	
	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		//System.out.println("dir: " + dir);
		if (!result.getMethod().getMethodName().contains("Logout")) {
			if (ITestResult.FAILURE == result.getStatus()) {
				this.capturescreen(driver, result.getMethod().getMethodName(), "FAILURE");
				Reporter.setCurrentTestResult(result);

				Reporter.log("<br/>Failed to execute method: " + result.getMethod().getMethodName() + "<br/>");
				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsFailure/"
						+ result.getMethod().getMethodName() + ".png";

			} else {
				this.capturescreen(driver, result.getMethod().getMethodName(), "SUCCESS");
				Reporter.setCurrentTestResult(result);

				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsSuccess/"
						+ result.getMethod().getMethodName() + ".png";

			}

			String path = "<img src=\" " + screenshotPath + "\" alt=\"\"\"/\" />";
			// To add it in the report
			Reporter.log("<br/>");
			Reporter.log(path);
			
			try {
				//Update attachment to testrail server
				int testCaseID=0;
				//int status=(int) result.getTestContext().getAttribute("Status");
				//String comment=(String) result.getTestContext().getAttribute("Comment");
				  if (result.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class))
					{
					TestRail testCase = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);
					// Get the TestCase ID for TestRail
					testCaseID = testCase.testCaseId();
					
					
					
					TestRailAPI api=new TestRailAPI();
					api.Getresults(testCaseID, result.getMethod().getMethodName());
					
					}
				}catch (Exception e) {
					// TODO: handle exception
					//e.printStackTrace();
				}
		}

	}

	public void capturescreen(WebDriver driver, String screenShotName, String status) {
		try {
			
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			if (status.equals("FAILURE")) {
				FileHandler.copy(scrFile,
						new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
				Reporter.log(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png");
			} else if (status.equals("SUCCESS")) {
				FileHandler.copy(scrFile,
						new File(Screenshotpath + "./ScreenshotsSuccess/" + screenShotName + ".png"));

			}

			System.out.println("Printing screen shot taken for className " + screenShotName);

		} catch (Exception e) {
			System.out.println("Exception while taking screenshot " + e.getMessage());
		}

	}
}
