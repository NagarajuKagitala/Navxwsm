package Administration;

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
public class APIRoleManagement 
{
	static WebDriver driver;
	static String URL;
	static String WSM_Username;
	static String WSM_Password;
	static String Screenshotpath;
	static String Role;
	static String Groups;
	
	@BeforeTest
	public void beforeTest() throws Exception 
	{
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
	
	@Parameters({"APIRolename","Groupname"})
	@Test(priority=1)
	public static void CreateAPIRole(String APIRolename, String Groupname, ITestContext context) throws InterruptedException
	{
		//for selecting administration
		driver.findElement(By.xpath("//div[3]/app-menu-item-template[2]/div/div/span")).click();
		
		//for selecting API Role Management
		driver.findElement(By.xpath("//app-menu-item-template[2]/div/div[2]/app-menu-item-template[4]/div/div")).click();
		Thread.sleep(3000);
		
		//for deleting the role if created
		SelectingAPIRole(APIRolename);
		try
		{
			
		//for clicking the delete option
		driver.findElement(By.linkText("Delete Api Role")).click();
		Thread.sleep(3000);
		
		//for clicking confirmation button
		driver.findElement(By.xpath("//app-modal-confirmation-window/div[2]/div/div/button")).click();
		Thread.sleep(3000);
		}
		catch(Exception e)
		{
			
		}
		
		//Create the role           
		driver.findElement(By.cssSelector(".table-actions > .fa-plus")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("api-role-name")).clear();
		Thread.sleep(3000);
		driver.findElement(By.id("api-role-name")).sendKeys(APIRolename);
		Thread.sleep(4000);
		
		//clicking on groups
		driver.findElement(By.xpath("//a[contains(text(),'Groups')]")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//input")).sendKeys(Groupname);
		Thread.sleep(3000);
		
		//clicking on Add button
		driver.findElement(By.xpath("//button[contains(.,' Add')]")).click();
		Thread.sleep(3000);
		
		//clicking on save button
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(5000);
		
		//Getting list of role 
		ListofRoles();
		
		if(Role.contains(APIRolename))
		{
			System.out.println("Role is created");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Role is created");
		}
		else
		{
			System.out.println("Role is not created");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Role is created");
			driver.findElement(By.id("Role is created")).click();
		}
	}
	
	@Parameters({"APIRolename"})
	@Test(priority=2)
	public static void HideIcon(String APIRolename, ITestContext context) throws InterruptedException
	{
		//for selecting eyeicon in created role
		WebElement a=driver.findElement(By.className("table-container")).findElement(By.id("api-role-table")).findElement(By.tagName("tr"));
		List<WebElement>b=a.findElements(By.tagName("th"));
		System.out.println("number of roles are:"+b.size());
		
		for(WebElement c:b)
		{
			System.out.println("names of roles:"+c.getText());
			
			if(c.getText().equalsIgnoreCase(APIRolename))
			{
				List<WebElement>d=c.findElements(By.tagName("span"));
				
				for(WebElement e:d)
				{
					System.out.println("the class names are:"+e.getAttribute("title"));
					
					if(e.getAttribute("title").contains("Hide"))
					{
						e.click();
						Thread.sleep(4000);
						break;
					}
					
				}
				break;
			}
		}
		
		//Getting list of role 
		ListofRoles();
	    
	    if(Role.contains(APIRolename))
		{
			System.out.println("Role hide button is not working");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Role hide button is not working");
			driver.findElement(By.id("Role hide failed")).click();
		}
		else
		{
			System.out.println("Role hide button is working");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Role hide button is working");
		}
		
	}
	
	@Parameters({"APIRolename"})
	@Test(priority=3)
	public static void AddHideRoleFromDisplayedAPIRole(String APIRolename, ITestContext context) throws InterruptedException
	{
		//for clicking on displaying box icon
		driver.findElement(By.xpath("//div[@id='main-body']/div[2]/div/div/i[3]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input")).sendKeys(APIRolename);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@type='checkbox']")).click();
		Thread.sleep(2000);
		//for clicking on add button
		driver.findElement(By.xpath("//button[2]")).click();
		Thread.sleep(2000);
		// for clicking ok button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(2000);
		
		//again to check the role is added or not //Getting list of role 
	    ListofRoles();
		Thread.sleep(5000);
		
		if(Role.contains(APIRolename))
		{
			System.out.println("Adding hide Role is working");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Adding hide Role is working");
			
		}
		else
		{
			System.out.println("Adding hide Role is not working");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Adding hide Role is not working");
			driver.findElement(By.id("Add Role hide failed")).click();
		}
		
	}
	
	@Parameters({"APIRolename", "DisplayGroupName"})
	@Test(priority=4)
	public static void DisplayGroups(String APIRolename, String DisplayGroupName, ITestContext context) throws InterruptedException
	{
		//for selecting the display group icon
		SelectingDisplayGroups();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//ul")).click();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//div[3]/div/input")).sendKeys(DisplayGroupName);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[contains(.,' Add')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(2000);
		
		//for selecting the display group icon
		SelectingDisplayGroups();
		Thread.sleep(3000);
		
		StringBuffer buffer=new StringBuffer();
		WebElement ele=driver.findElement(By.className("ngx-contextmenu")).findElement(By.tagName("ul")).findElement(By.tagName("li")).findElement(By.tagName("a"));
		List<WebElement> divs=ele.findElements(By.tagName("div"));
		
		for(WebElement a:divs)
		{
			System.out.println("Roles are: " +a.getText());
			buffer.append(a.getText());
			buffer.append(",");
		}
		 Groups=buffer.toString();
		System.out.println("List of groups are: " +Groups);
		
		if(Groups.contains(DisplayGroupName))
		{
			System.out.println("Group is added to role");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Group is added to role");
		}
		else
		{
			System.out.println("Group is not added to role");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Group is not added to role");
			driver.findElement(By.id("add group to role failed")).click();
		}
		
		
	}
	
	@Parameters({"APIRolename","Editedrole"})
	@Test(priority=5)
	public static void EditAPIRole(String APIRolename, String Editedrole, ITestContext context) throws InterruptedException
	{
		//for selecting API role
		SelectingAPIRole(APIRolename);
		
		//for clicking edit role option
		driver.findElement(By.xpath("//a[contains(.,' Edit Api Role')]")).click();
		Thread.sleep(3000);
		
		//for clearing the previous name 
		driver.findElement(By.xpath("//input[@id='api-role-name']")).clear();
		
		//editing name and click
		driver.findElement(By.xpath("//input[@id='api-role-name']")).sendKeys(Editedrole);
		Thread.sleep(4000);
		
		//click on save button
		driver.findElement(By.xpath("//app-modal-api-role/div[2]/div/div/button")).click();
		Thread.sleep(4000);
		
		//getting list of roles and verifying with changed name
		ListofRoles();
		
		if(Role.contains(Editedrole))
		{
			System.out.println("Edit role option is working fine");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Edit role option is working fine");
		}
		else
		{
			System.out.println("Edit role is not working");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Edit role is not working");
			driver.findElement(By.id("Edit role fialed"));
		}
	}
	
	@Parameters({"Editedrole", "Clonerole"})
	@Test(priority=6)
	public static void CloneAPIRole(String Editedrole, String Clonerole, ITestContext context) throws InterruptedException
	{
		//for selecting API role
		SelectingAPIRole(Editedrole);
		
		//for clicking the clone option 
		driver.findElement(By.xpath("//a[contains(.,' Clone Api Role')]")).click();
		Thread.sleep(3000);
		 
		//Give the name
		driver.findElement(By.id("api-role-name")).clear();
		driver.findElement(By.id("api-role-name")).sendKeys(Clonerole);
		Thread.sleep(4000);
		
		//for clicking save button
		driver.findElement(By.xpath("//app-modal-api-role/div[2]/div/div/button")).click();
		Thread.sleep(3000);
		
		//getting list of roles to see cloned role
		ListofRoles();
		
		if(Role.contains(Clonerole))
		{
			System.out.println("Clone role is working fine");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Clone role is working fine");
		}
		else
		{
			System.out.println("Clone role is working fine");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Clone role is working fine");
			driver.findElement(By.id("Clone role failed")).click();
		}
	}
	
	@Parameters({"Editedrole", "Clonerole"})
	@Test(priority=7)
	public static void DeleteAPIRole(String Editedrole, String Clonerole, ITestContext context) throws InterruptedException
	{
		// for selecting the cloned role and delete
		SelectingAPIRole(Editedrole);
		
		//for clicking the delete option
		driver.findElement(By.linkText("Delete Api Role")).click();
		Thread.sleep(3000);
		
		//for clicking confirmation button
		driver.findElement(By.xpath("//app-modal-confirmation-window/div[2]/div/div/button")).click();
		Thread.sleep(3000);
		
		//for selecting the edited role and delete
		SelectingAPIRole(Clonerole);
		
		
		//for clicking the delete option
		driver.findElement(By.linkText("Delete Api Role")).click();
		Thread.sleep(3000);
		
		//for clicking confirmation button
		driver.findElement(By.xpath("//app-modal-confirmation-window/div[2]/div/div/button")).click();
		Thread.sleep(3000);
		
		//list of role
		ListofRoles();
		
		if(Role.contains(Clonerole))
		{
			System.out.println("Delete role is failed");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Delete role is failed");
			driver.findElement(By.id("Delete role failed")).click();
		}
		else
		{
			System.out.println("Delete role is working fine");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Delete role is working fine");
		}
		
	}
	
	@Test(priority=8)
	public static void Logout() throws InterruptedException
	{
		//for logout button
		driver.findElement(By.xpath("//div[5]/i")).click();
		Thread.sleep(4000);
		
		driver.close();
	}
	
	public static void SelectingAPIRole(String Editedrole) throws InterruptedException
	{
		//for selecting edited role
		WebElement a=driver.findElement(By.className("table-container")).findElement(By.id("api-role-table")).findElement(By.tagName("tr"));
		List<WebElement> b=a.findElements(By.tagName("th"));
		//System.out.println("number of roles are:"+b.size());
		
		for(WebElement c:b)
		{
			//System.out.println("list of roles:"+c.getText());
			
			if(c.getText().equalsIgnoreCase(Editedrole))
			{
				List<WebElement>d=c.findElements(By.tagName("span"));
				
				for(WebElement e:d)
				{
					//System.out.println("class names are:"+e.getAttribute("class"));
					
					if(e.getAttribute("class").contains("action"))
					{
						e.click();
						Thread.sleep(5000);
					}
				}
				break;
			}
		}
		
	}
	
	public static void ListofRoles()
	{
		//list of roles and verify if roles deleted or not
		
		WebElement k=driver.findElement(By.className("table-container")).findElement(By.tagName("table")).findElement(By.tagName("tr"));
		List<WebElement>l=k.findElements(By.tagName("th"));
		//System.out.println("number of roles are:"+l.size());
		
		for(WebElement m:l)
		{
			System.out.println("list of the roles:"+m.getText());
		}
		
		//for storing all role names in one string
		StringBuffer buffer=new StringBuffer();
		
		for(WebElement n:l)
		{
			String Role=n.getText();
			
			buffer.append(Role);
			buffer.append(",");
		}
		Role = buffer.toString();
		
		System.out.println("Final List of roles are :"+Role);
		
	}
	
	public static void DeleteRole() throws InterruptedException
	{
		WebElement a=driver.findElement(By.className("table-container")).findElement(By.tagName("table")).findElement(By.tagName("tr"));
		List<WebElement> b=a.findElements(By.tagName("th"));
		//System.out.println("number of roles are :"+b.size());
		
		for(WebElement c:b)
		{
			//System.out.println("list of roles :"+c.getText());
			
			if(c.getText().equalsIgnoreCase("New API Role"))
			{
				List<WebElement>d=c.findElements(By.tagName("span"));
				
				for(WebElement e:d)
				{
					//System.out.println("class names are:"+e.getAttribute("class"));
					
					if(e.getAttribute("class").contains("action"))
					{
						e.click();
						Thread.sleep(5000);
					}
				}
				break;
			}
		}
		
	}
	
	public static void SelectingDisplayGroups()
	{
	
	   WebElement a=driver.findElement(By.className("table-container")).findElement(By.id("api-role-table")).findElement(By.tagName("tr"));
	   List<WebElement>b=a.findElements(By.tagName("th"));
		
		for(WebElement c:b)
		{
			System.out.println("names of roles are :"+c.getText());
			
			if(c.getText().equalsIgnoreCase("New API Role"))
			{
				List<WebElement>d=c.findElements(By.tagName("span"));
						
						for(WebElement e:d)
						{
							System.out.println("the class names are:"+e.getAttribute("title"));
							
							if(e.getAttribute("title").contains("Display Groups"))
							{
								e.click();
								
							}
						}
				break;
				
			}
		}
	}
	
	public static void Listofgroups()
	{
		WebElement a=driver.findElement(By.className("ng-star-inserted")).findElement(By.className("dropdown-item"));
		List<WebElement>b=a.findElements(By.tagName("div"));
		System.out.println("number of divs:"+b.size());
		
		for(WebElement c:b)
		{
			System.out.println("names of groups:"+c.getAttribute("class"));
			
			if(c.getAttribute("class").contains("operators"))
			{
				System.out.println("if class contains the  group name:"+c.getText());
			}
		}
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
