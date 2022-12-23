package Administration;

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
import org.openqa.selenium.interactions.Actions;
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
public class AuditReport 
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
	
	@TestRail(testCaseId=1212)
	@Parameters({"Username", "ActualResult"})
	@Test(priority=1)
	public void SecurityManagerAuditReport(String Username, String ActualResult, ITestContext context) throws InterruptedException
	{
		//Delete user exists
		DeleteUser(Username);
		
		//Click on plus icon
		driver.findElement(By.cssSelector(".fa-plus")).click();
		Thread.sleep(6000);
		
		//give the user name
		driver.findElement(By.id("userName")).clear();
		driver.findElement(By.id("userName")).sendKeys(Username);
		
		//Click on Ok button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//Goto Audit report and Security manager
		driver.findElement(By.xpath("//div[3]/app-menu-item-template[2]/div/div")).click();
		Thread.sleep(4000);
		Actions AuditMousehour=new Actions(driver);
		AuditMousehour.moveToElement(driver.findElement(By.xpath("//app-menu-item-template[2]/div/div[2]/app-menu-item-template[3]/div/div"))).perform();
		driver.findElement(By.xpath("//app-menu-item-template[3]/div/div[2]/app-menu-item-template[2]/div/div/span")).click();
		Thread.sleep(8000);
				
		//get the security audit report data
		String Securitymanagemnetdata=driver.findElement(By.xpath("//td[3]")).getText();
		System.out.println("Security data is: " +Securitymanagemnetdata);
		
		if(Securitymanagemnetdata.equalsIgnoreCase(ActualResult))
		{
			System.out.println("Security manager Audit report added");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Security manager Audit report added successfully");
		}
		else
		{
			System.out.println("Security manager Audit report not added");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Security manager Audit report not added");
			driver.findElement(By.id("Security manager Audit report failed")).click();
		}
		
		//Delete user exists
		DeleteUser(Username);
	}
	
	@TestRail(testCaseId=1213)
	@Test(priority=2)
	public void LoginUsername(ITestContext context) throws Exception
	{
		Settings.read();
		String uname=Settings.getWSM_Username();
		
		//Store the username into string
		String LoginUser=driver.findElement(By.xpath("//b")).getText();
		System.out.println("User name is: " +LoginUser);
		
		if(LoginUser.equalsIgnoreCase(uname))
		{
			System.out.println("Login user is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Login user working fine");
		}
		else
		{
			System.out.println("Login user not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Login user not working");
			driver.findElement(By.id("Login user failed")).click();
		}
	}
	
	@TestRail(testCaseId=1214)
	@Test(priority=3)
	public void SecurityRefresh(ITestContext context) throws InterruptedException
	{
		//Click on security refresh button
		driver.findElement(By.xpath("//span[contains(.,'Refresh Security')]")).click();
		Thread.sleep(4000);
		
		//Get the string
		String Refreshdata=driver.findElement(By.xpath("//app-modal-title/div/div/span")).getText();
		System.out.println("Refresh popup title: " +Refreshdata);
		
		//click on yes button
		driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
		Thread.sleep(6000);
		
		if(Refreshdata.equalsIgnoreCase("Refresh Security"))
		{
			System.out.println("Refresh security is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Refresh security working fine");
		}
		else
		{
			System.out.println("Refresh security not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Refresh security not working");
			driver.findElement(By.id("Refresh security failed")).click();
		}
		
	}
	
	@TestRail(testCaseId=1215)
	@Test(priority=4)
	public void MenuChangeIcon(ITestContext context) throws InterruptedException
	{
		//Click on user settings button
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(4000);
		
		//Choose side menu
		driver.findElement(By.id("side-m-type")).click();
		
		//Click on save button
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(8000);
		
		//Get data
		String Sidemenu=driver.findElement(By.xpath("//div[2]/div[5]")).getText();
		System.out.println("Side menu data is: " +Sidemenu);
		
		if(Sidemenu.contains("Settings"))
		{
			System.out.println("Settings icon is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Settings icon working fine");
		}
		else
		{
			System.out.println("Settings icon not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Settings icon not working");
			driver.findElement(By.id("Settings icon failed")).click();
		}
		//Change menu to top
		driver.findElement(By.xpath("//div[2]/div[5]")).click();
		Thread.sleep(2000);
		
		//Select Top menu
		driver.findElement(By.id("top-m-type")).click();
		
		//Click on save button
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(8000);
		
	}
	
	@TestRail(testCaseId=1216)
	@Parameters({"HelpURL"})
	@Test(priority=5)
	public void HelpIcon(String HelpURL, ITestContext context) throws InterruptedException
	{
		//click on Help icon
		driver.findElement(By.xpath("//div[4]/i")).click();
		Thread.sleep(10000);
		
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//Switch to opened window
		driver.switchTo().window(handle[1]);
		
		//get current url of the page
		String CurrentUrl = driver.getCurrentUrl();
		System.out.println("URL of the page:" +CurrentUrl);
		
		driver.close();
		Thread.sleep(6000);
		
		driver.switchTo().window(handle[0]);
		
		if(CurrentUrl.equalsIgnoreCase(HelpURL))
		{
			System.out.println("Help icon is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Help url icon working fine");
		}
		else
		{
			System.out.println("Help is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Help url icon not working");
			driver.findElement(By.id("Help url icon failed")).click();
		}
		
	}
		
	public void DeleteUser(String Username) throws InterruptedException
	{
		//Select Security manager
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div/i")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div[2]/app-menu-item-template/div/div")).click();
		Thread.sleep(8000);
		
		try
		{
			//Search with name
			driver.findElement(By.xpath("//input")).clear();
			driver.findElement(By.xpath("//input")).sendKeys(Username);
			
			//get the management data
			String Usermanagemnetdata=driver.findElement(By.xpath("//app-simple-table/div/div")).getText();
			System.out.println("user data is: " +Usermanagemnetdata);
			
			if(Usermanagemnetdata.contains(Username))
			{
				System.out.println("Execute user delete");
				this.userdeleting();
			}
			else
			{
				System.out.println("User is not existing");
			}
		}
		catch(Exception e)
		{
			System.out.println("User is not existing");
		}
		
	}
	
	
	public void userdeleting() throws InterruptedException
	{		
		//click on checkbox and choose edit option
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
