import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Demo {
    public static void main(String[] args) throws IOException, InterruptedException {


        // Prepare information to visit web
        String loginUrl = "https://www.scboy.com/?user-login.htm";
        String myAccount = "18621105309";
        String myPwd = "123456";
        String scboyHomeUrl = "https://www.scboy.com/";
        String kplxqUrl = "https://www.scboy.com/?forum-1.htm";
        String searchUrl = "https://www.scboy.com/?search-_E6_8A_95_E7_A5_A8.htm";

        System.setProperty("webdriver.chrome.driver", "/home/icyfrog/Downloads/chromedriver");
        WebDriver driver = new ChromeDriver();

        // Settings for web and window.
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        // Login
        driver.get(loginUrl);
        WebElement acctInput = driver.findElement(By.id("mobile"));
        acctInput.sendKeys(myAccount);
        WebElement pwdInput = driver.findElement(By.id("password"));
        pwdInput.sendKeys(myPwd);
        WebElement submitButton = driver.findElement(By.id("submit"));
        submitButton.click();

        // 检查
        WebDriverWait webDriverWait = new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.titleIs("星际老男孩"));
        if(!driver.getCurrentUrl().equals(scboyHomeUrl)) {
            System.err.println("[Error] login did not direct page to scboy.com!");
            System.exit(-1);
        }
        else{
            System.out.println("[Success] login succeeded! Chrome is now viewing home page of scboy.com");
        }

        // 跳转到 "科普鲁星区" 然后尝试 "发新帖" 按钮
        WebElement kplxqButton = driver.findElement(By.xpath("//*[@id=\"nav\"]/ul[1]/li[3]/a"));
        kplxqButton.click();
        // chrome 应该在 "科普鲁星区"
        // 检查
        webDriverWait = new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.titleIs("科普鲁星区(星际2)-星际老男孩"));
        if(!driver.getCurrentUrl().equals(kplxqUrl)) {
            System.err.println("[Error] did not navigate page to https://www.scboy.com/?forum-1.htm!");
            System.exit(-1);
        }
        else{
            System.out.println("[Success] navigation succeeded! Chrome is now viewing 科普鲁星区 part");
        }
        // 搜索帖子
        WebElement searchInput = driver.findElement(By.className("form-control"));
        searchInput.sendKeys("投票");
        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"search_form\"]/div/div/button"));
        searchButton.click();
        // 现在应该在搜索结果页面
        // 检查
        /*
        webDriverWait = new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.titleIs("投票"));
        if(!driver.getCurrentUrl().equals(kplxqUrl)) {
            System.err.println("[Error] did not navigate page to https://www.scboy.com/?search-_E6_8A_95_E7_A5_A8.htm!");
            System.exit(-1);
        }
        else{
            System.out.println("[Success] navigation succeeded! Chrome is now viewing 主题帖搜索”投票“ part");
        }
        */
        WebElement destinationCard = driver.findElement(By.xpath("//*[@id=\"body\"]/div/div/div/div[2]/div[2]/ul/li[9]/div[2]/div/a"));
        destinationCard.click();

        WebElement voteButton1 = driver.findElement(By.xpath("//*[@id=\"body\"]/div/div/div/div[2]/div/div/div[2]/div/div/div/div/label"));
        voteButton1.click();

        WebElement voteButton2 = driver.findElement(By.xpath("//*[@id=\"body\"]/div/div/div/div[2]/div/div/div[2]/div/div/div[3]/div/label"));
        //WebElement voteButton2 = driver.findElement(By.xpath("//*[@id=\"container\"]/div/div/div[2]/div/div/div[2]/div/div/div[3]/div/label"));
        voteButton2.click();
    }
}
