package Common;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Roles 
{
	public void DeleteRole(String Rolename, WebDriver driver)
	{
		try
		{
			System.out.println("Delete role name is -------: " +Rolename);
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
	
	
	

}
