package SecurityManager;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
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

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class AuditManagement 
{
	static WebDriver driver;
	static String URL;
	static String WSM_Username;
	static String WSM_Password;
	static String Screenshotpath;
	
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		URL=Settings.getURL();
		WSM_Username=Settings.getWSM_Username();
		WSM_Password=Settings.getWSM_Password();
		Screenshotpath=Settings.getScreenshotPath();
		
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
		
		//Change to top menu
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
			Thread.sleep(3000);
			
			//Select top menu
			driver.findElement(By.id("top-m-type")).click();
			Thread.sleep(3000);
			
			//click on save button
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
		}
						
	}
	
	@TestRail(testCaseId=1199)
	@Parameters({"Auditname", "ActionMask", "WGSNAME"})
	@Test(priority=1)
	public void CreateAudit(String Auditname, String ActionMask, String WGSNAME, ITestContext context) throws InterruptedException
	{
		//Select Security manager
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div/i")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div[2]/app-menu-item-template[6]/div/div/span")).click();
		Thread.sleep(8000);
		
		try
		{
			//Search with name
			driver.findElement(By.xpath("//input")).clear();
			driver.findElement(By.xpath("//input")).sendKeys(Auditname);
			
			//get the management data
			String Usermanagemnetdata=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
			System.out.println("user data is: " +Usermanagemnetdata);
			
			if(Usermanagemnetdata.contains(Auditname))
			{
				System.out.println("Execute user delete");
				this.Auditdeleting();
			}
			else
			{
				System.out.println("Audit is not existing");
			}
		}
		catch(Exception e)
		{
			System.out.println("Audit is not existing");
		}
		
		//Click on plus icon
		driver.findElement(By.cssSelector(".fa-plus")).click();
		Thread.sleep(6000);
		
		//give the Order value
		driver.findElement(By.id("orderValue")).clear();
		driver.findElement(By.id("orderValue")).sendKeys(Auditname);
		Thread.sleep(2000);
		
		//Give the mask name
		driver.findElement(By.id("featureMask")).clear();
		driver.findElement(By.id("featureMask")).sendKeys(ActionMask);
		Thread.sleep(2000);
		
		//give the Workgroup server name
		driver.findElement(By.id("workgroupServer")).clear();
		driver.findElement(By.id("workgroupServer")).sendKeys(WGSNAME);
		Thread.sleep(2000);
		
		//Click on Ok button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//Search with name
		driver.findElement(By.xpath("//input")).clear();
		driver.findElement(By.xpath("//input")).sendKeys(Auditname);
		
		//get the management data
		String Auditmanagemnetdata=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
		System.out.println("Audit data is: " +Auditmanagemnetdata);
		
		if(Auditmanagemnetdata.contains(Auditname))
		{
			System.out.println("Audit is created");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Audit is created successfully");
		}
		else
		{
			System.out.println("Audit is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Audit is created successfully");
			driver.findElement(By.id("Create Audit failed")).click();
		}
	}
	
	@TestRail(testCaseId=1200)
	@Parameters({"Auditname", "UpdatedActionMask"})
	@Test(priority=2, dependsOnMethods = {"CreateAudit"})
	public void EditAudit(String Auditname, String UpdatedActionMask, ITestContext context) throws InterruptedException
	{
		//click on checkbox and choose edit option
		driver.findElement(By.xpath("//td/input")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//li[contains(.,'Edit')]")).click();
		Thread.sleep(8000);
		
		//Give the mask name
		driver.findElement(By.id("featureMask")).clear();
		driver.findElement(By.id("featureMask")).sendKeys(UpdatedActionMask);
		Thread.sleep(2000);
		
		//Click on Save button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//get the Mask
		String ResultMask=driver.findElement(By.xpath("//td[6]")).getText();
		System.out.println("Result mask data is: " +ResultMask);
		
		if(ResultMask.equalsIgnoreCase(UpdatedActionMask))
		{
			System.out.println("Audit is updated successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Audit is updated successfully");
		}
		else
		{
			System.out.println("Audit is not updated");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Audit is not updated");
			driver.findElement(By.id("Audit update failed")).click();
		}
		
	}
	
	@TestRail(testCaseId=1201)
	@Parameters({"Auditname"})
	@Test(priority=3, dependsOnMethods = {"CreateAudit"})
	public void DeleteAudit(String Auditname, ITestContext context) throws InterruptedException
	{
		//Uncheck all
		driver.findElement(By.id("item-checkbox-checkboxAll")).click();
		Thread.sleep(3000);
		this.Auditdeleting();
		
		//Get the result data
		String Resultusers=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
		System.out.println("Result Audits are: " +Resultusers);
		
		if(Resultusers.contains(Auditname))
		{
			System.out.println("Audit is not deleted");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Audit is not deleted");
			driver.findElement(By.id("Audit delete failed")).click();
		}
		else
		{
			System.out.println("Audit is deleted");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Audit is deleted successfully");
		}
	}
	
	
	public void Auditdeleting() throws InterruptedException
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
