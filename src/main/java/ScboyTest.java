

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ScboyTest {

    static private String driverPath = "E:\\Documents\\SJTU\\software_testing\\hw\\chromedriver_win32\\chromedriver.exe";
    private WebDriver driver;
    //private Set<Cookie> cookies;

    @BeforeClass
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver",driverPath);
        driver = new ChromeDriver();
        // 设置加载超时时间
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        // 定位时间，超时后抛异常
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() throws Exception {
        //driver.close();
    }

    private void checkLogin(){
        String loginUrl = "https://www.scboy.com/?user-login.htm";
        String myAccount = "18621105309";
        String myPwd = "123456";
        String scboyHomeUrl = "https://www.scboy.com/";

        driver.get(loginUrl);
        // 检查页面基本信息 title url
        String title = driver.getTitle();
        String currentUrl = driver.getCurrentUrl();
        assertEquals("用户登录",title);
        assertEquals(loginUrl, currentUrl);

        // 输入用户名
        WebElement acctInput = driver.findElement(By.id("mobile"));
        acctInput.sendKeys(myAccount);
        String currentAcct = acctInput.getAttribute("value");
        assertEquals(myAccount,currentAcct);

        // 输入密码
        WebElement pwdInput = driver.findElement(By.id("password"));
        pwdInput.sendKeys(myPwd);
        String currentPwd = pwdInput.getAttribute("value");
        assertEquals(myPwd,currentPwd);

        // 检查登录按钮
        WebElement submitButton = driver.findElement(By.id("submit"));
        assertTrue(submitButton.isEnabled());
        submitButton.click();
        // 检查页面跳转情况
        WebDriverWait webDriverWait = new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.titleIs("星际老男孩"));
        assertEquals(scboyHomeUrl,driver.getCurrentUrl());
        //cookies = driver.manage().getCookies();
//        for (Cookie c : cookies) {
//            System.out.println(String.format("%s -> %s -> %s -> %s -> %s",
//                    c.getDomain(), c.getName(), c.getValue(), c.getExpiry(), c.getPath()));
//        }
    }

    private void jumpToKPL(){
        String scboyHomeUrl = "https://www.scboy.com/";
        String kplxqUrl = "https://www.scboy.com/?forum-1.htm";
        String postUrl = "https://www.scboy.com/?thread-create-1.htm";

        // 检查导航栏是不是都可以点击
        // 这里要不要重新获取主页呢？防止前面没有执行登录。好像有点多此一举
        driver.get(scboyHomeUrl);
        List<WebElement> elements = driver.findElements(By.className("nav-link"));
        assertFalse(elements.isEmpty());
        for(WebElement element:elements){
            assertTrue(element.isEnabled());
            //System.out.println(element.getAttribute("href"));
        }

        // 跳转到 "科普鲁星区" 然后尝试 "发新帖" 按钮
        WebElement kplxqButton = driver.findElement(By.xpath("//*[@id=\"nav\"]/ul[1]/li[3]/a"));
        kplxqButton.click();
        // chrome 应该在 "科普鲁星区"
        // 检查
        WebDriverWait webDriverWait = new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.titleIs("科普鲁星区(星际2)-星际老男孩"));
        assertEquals(kplxqUrl,driver.getCurrentUrl());

        // 接下来，按下“发新帖"按钮 准备发帖
        WebElement postButton = driver.findElement(By.xpath("//*[@id=\"body\"]/div/div/div[2]/a"));
        assertTrue(postButton.isEnabled());
        postButton.click();
        //webDriverWait = new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.titleIs("发帖"));
        assertEquals(postUrl,driver.getCurrentUrl());
    }

    private void checkPost(){
        String postUrl = "https://www.scboy.com/?thread-create-1.htm";
        // 先get一次，添加cookie时要在同一个域名下
        driver.get(postUrl);

        // 设置 cookies
        Cookie cookie_test = new Cookie("cookie_test",
                "_2F04_2Fs7KP15ejKq1W9smUiLERtDMGaOOvt_2FNi_2BVX0K44Dy7c2fxvPRl3vVAK7GuY6",
                "www.scboy.com",
                "/",
                null);
        Cookie bbs_token = new Cookie("bbs_token",
                "n_2FecWcfyNKaozP5J9kCA9QrDC6VSP4YkCA7O1wSV4xu0EkIKXr3o4frW1SsXtrV4CF4jSQajDIbeoBbzakDMQQ_3D_3D",
                "www.scboy.com",
                "/",
                null);
        Cookie bbs_sid = new Cookie("bbs_sid",
                "e40a0cadf458cea17bf470e4056b8b54",
                "www.scboy.com",
                "/",
                null);
        // 添加cookie
        driver.manage().addCookie(cookie_test);
        driver.manage().addCookie(bbs_token);
        driver.manage().addCookie(bbs_sid);
        // 现在可以直接进入发帖页面了
        driver.get(postUrl);
        assertEquals(postUrl,driver.getCurrentUrl());

        // 接着检查页面上的各种组件功能
        // 测试创建表格，模拟鼠标操作
        WebElement createTabel = driver.findElement(By.id("edui140_button_body"));
        assertTrue(createTabel.isEnabled());
        // 点击后出现选择行列的界面
        createTabel.click();
        WebElement tabelOverlay = driver.findElement(By.id("edui141_overlay"));
        Actions actions = new Actions(driver);
        actions.moveToElement(tabelOverlay,1,3);
        //actions.click();
        actions.perform();

        WebElement tabelLabel = driver.findElement(By.id("edui141_label"));
        //assertEquals("0列 x 0行", tabelLabel.getText());


    }

    @Test
    public void testLoginAndJump(){
        checkLogin();
        jumpToKPL();

    }

    @Test
    public void testPost(){
        checkPost();
    }
}