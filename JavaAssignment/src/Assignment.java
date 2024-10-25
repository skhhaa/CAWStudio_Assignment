import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;

@Test
public class Assignment {
    public static WebDriver driver;
    public static JSONArray jsonData = null;
    public static List<WebElement> rows = new ArrayList<>();

    @BeforeTest
    public void launchingBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @AfterTest
    public void closeBrowser() {
        driver.close();
    }

    @Test(priority = 1)
    public void readData() throws Exception {
        //read the test data
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("C:\\OH Repo\\JavaAssignment\\src\\TestData.json");
        //Read JSON file
        Object obj = jsonParser.parse(reader);
        jsonData = (JSONArray) obj;
    }

    @Test(dependsOnMethods = "readData")
    public void locateElements() throws InterruptedException {
        //Locating the table data element to click
        driver.findElement(By.xpath("//summary[text()='Table Data']")).click();

        //locating the Text box to clear the existing json data
        driver.findElement(By.id("jsondata")).clear();
        Thread.sleep(10000);

        //locating the Text box to add the Json Data
        driver.findElement(By.id("jsondata")).sendKeys(jsonData.toString());
        Thread.sleep(10000);

        //Locating the refresh table button to add the json data into the table
        driver.findElement(By.xpath("//button[text()='Refresh Table']")).click();
        Thread.sleep(10000);

        //Finding the rows existing in the Dynamic table for assertion
        rows = driver.findElements(By.xpath("//table[@id='dynamictable']/tr"));
    }

    @Test(dependsOnMethods = "locateElements")
    public void Assertions() {
        //asserting the gender
        for(int i=1;i< rows.size();i++) {
            JSONObject object = (JSONObject) jsonData.get(i-1);
            WebElement genderElement = driver.findElement(By.xpath("//table[@id='dynamictable']/tr[" + (i+1)+ "]/td[1]"));
            Object actualGender =  object.get("gender");
            assertEquals(actualGender,genderElement.getText());
        }

        //asserting the name
        for(int i=1;i< rows.size();i++) {
            JSONObject object = (JSONObject) jsonData.get(i-1);
            //System.out.println(columns.get(i).getText());
            WebElement nameElement = driver.findElement(By.xpath("//table[@id='dynamictable']/tr[" + (i+1)+ "]/td[2]"));
            Object actualName =  object.get("name");
            assertEquals(actualName,nameElement.getText());
        }

        //asserting the age
        for(int i=1;i< rows.size();i++) {
            JSONObject object = (JSONObject) jsonData.get(i-1);
            WebElement ageElement = driver.findElement(By.xpath("//table[@id='dynamictable']/tr[" + (i+1)+ "]/td[3]"));
            Object actualAge =  object.get("age");
            assertEquals(actualAge.toString(),ageElement.getText());
        }
    }

}
