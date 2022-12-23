package SecurityManager;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
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

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class RoleManagement 
{
	static WebDriver driver;
	static String URL;
	static String WSM_Username;
	static String WSM_Password;
	static String Screenshotpath;
	static String ListOfRoles;
    static String CloneRolename;
	
	
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
	
	@TestRail(testCaseId=1189)
	@Parameters({"Rolename", "NewRolename"})
	@Test(priority=1)
	public static void CreateRole(String Rolename, String NewRolename, ITestContext context) throws InterruptedException
	{
		//Select Security manager
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div/i")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//div[3]/app-menu-item-template/div/div[2]/app-menu-item-template[3]/div/div")).click();
		Thread.sleep(8000);            
		
		//Select Role menu
		DeleteRole(NewRolename);
				
		//Click on plus icon of role
		driver.findElement(By.xpath("//div[2]/div/div/i")).click();
		Thread.sleep(4000);
		
		//Give the role name
		driver.findElement(By.id("role-name")).clear();
		driver.findElement(By.id("role-name")).sendKeys(Rolename);
		
		//Click on save button
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(10000);
		
		//Getting the list of roles
		Roleslist();
		
		//Verification
		if(ListOfRoles.contains(Rolename))
		{
			System.out.println("Role is added successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Role is added successfully");
		}
		else
		{
			System.out.println("Role is not added");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Role is failed to add");
			driver.findElement(By.id("Role failed to add")).click();
		}
	}
	
	@TestRail(testCaseId=1190)
	@Parameters({"Rolename", "NewRolename"})
	@Test(priority=2)
	public static void EditRole(String Rolename, String NewRolename, ITestContext context) throws InterruptedException
	{
		//select role menu option
		SelectingRoleMenu(Rolename);
		
		//choose edit option
		driver.findElement(By.xpath("//a/div")).click();
		Thread.sleep(8000);
		
		//update the role name
		driver.findElement(By.id("role-name")).clear();
		driver.findElement(By.id("role-name")).sendKeys(NewRolename);
		
		//Click on Save button
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(10000);
		
		//Getting the list of roles
		Roleslist();
		
		//Verification
		if(ListOfRoles.contains(NewRolename))
		{
			System.out.println("Role is udated successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Role is updated successfully");
		}
		else
		{
			System.out.println("Role is not updated");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Role is failed to update");
			driver.findElement(By.id("Role failed to update")).click();
		}
		
	}
	
	@TestRail(testCaseId=1191)
	@Parameters({"NewRolename"})
	@Test(priority=3)
	public static void CloneRole(String NewRolename, ITestContext context) throws InterruptedException
	{
		//Select role
		SelectingRoleMenu(NewRolename);
		
		//Select clone option
		driver.findElement(By.xpath("//li[2]/a/div")).click();
		Thread.sleep(4000);
		
		//Get Clone role
		 CloneRolename=driver.findElement(By.id("role-name")).getAttribute("value");
		//System.out.println("Getting clone role is: " +CloneRolename);
				
		//Click on Save button
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(10000);
		
		//Getting the list of roles
		Roleslist();
		
		//Verification
		if(ListOfRoles.contains(CloneRolename))
		{
			System.out.println("Clone role is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Clone role is working fine");
		}
		else
		{
			System.out.println("Clone role is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Clone role is not working");
			driver.findElement(By.id("clone Role failed")).click();
		}
		
		
	}
	
	@TestRail(testCaseId=1192)
	@Parameters({"NewRolename"})
	@Test(priority=4)
	public static void DeleteRole(String NewRolename,ITestContext context) throws InterruptedException
	{
		//System.out.println("Clone role is: " +CloneRolename);
		//Delete Role
		DeleteRole(CloneRolename);
		
		//Getting the list of roles
		Roleslist();
		
		//Verification
		if(ListOfRoles.contains(CloneRolename))
		{
			System.out.println("Delete role is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Delete role is not working");
			driver.findElement(By.id("Delete Role failed")).click();
		}
		else
		{
			System.out.println("Delete role is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Delete role is working fine");
			
		}
	}
	
	
	public static void SelectingRoleMenu(String Rolename) throws InterruptedException
	{		
		try
		{
			WebElement ele=driver.findElement(By.id("role-table")).findElement(By.tagName("tr"));
			List<WebElement> ths=ele.findElements(By.tagName("th"));
			System.out.println("no of roles are: " +ths.size());
			
			for(WebElement name:ths)
			{
				String Role=name.getText();
				//System.out.println("Role is: " +Role);
				
				if(Role.equalsIgnoreCase(Rolename))
				{
					List<WebElement> spans=name.findElements(By.tagName("span"));
					System.out.println("no of spans are: " +spans.size());
					for(WebElement fin:spans)
					{
						//System.out.println("html: " +fin.getAttribute("innerHTML"));
						if(fin.getAttribute("class").contains("action"))
						{
							//System.out.println("finals html: " +fin.getAttribute("innerHTML"));
							fin.click();
							Thread.sleep(10000);
							break;
						}
						
					}
					
				}
								
			}
		}
		catch(Exception e)
		{
			System.out.println("Role is not existing");
		}
		
	}
	
	public static void Roleslist()
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
			System.out.println("List of roles are: "+ListOfRoles);
		}
		catch(Exception e1)
		{
			System.out.println("Role is not existing");
		}	
	}
	
	public static void DeleteRole(String Rolename)
	{
		try
		{
			WebElement ele=driver.findElement(By.id("role-table")).findElement(By.tagName("tr"));
			List<WebElement> ths=ele.findElements(By.tagName("th"));
			System.out.println("no of roles are: " +ths.size());
			
			for(WebElement name:ths)
			{
				String Role=name.getText();
				//System.out.println("Role is: " +Role);
				
				if(Role.equalsIgnoreCase(Rolename))
				{
					List<WebElement> spans=name.findElements(By.tagName("span"));
					System.out.println("no of spans are: " +spans.size());
					for(WebElement fin:spans)
					{
						//System.out.println("html: " +fin.getAttribute("innerHTML"));
						if(fin.getAttribute("class").contains("action"))
						{
							//System.out.println("finals html: " +fin.getAttribute("innerHTML"));
							fin.click();
							Thread.sleep(10000);
							//Click on delete button
							driver.findElement(By.xpath("//li[3]/a/div")).click();
							Thread.sleep(3000);
							
							//Click on confirmation yes button
							driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
							Thread.sleep(6000);
							break;
						}
						
					}
					
				}
								
			}
		}
		catch(Exception e)
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
