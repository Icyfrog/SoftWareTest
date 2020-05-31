import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class ScboyForumTest {
    static final boolean shouldWeReallyPost = false;
    // 这个函数让Robot r 在键盘上按下char c对应的按键
    // 两次按键之间强制间隔100ms
    // 支持a-z A-Z 1-9 ':''.''\n''\\' 等在路径中常见的英文字符
    // 如果路径中存在不支持的类型 抛出异常
    public static void makeRobotPressChar(Robot r,char c) throws Exception{
        // System.out.println("dealing with: '"+c+"'");
        r.delay(100);
        int [] alphas = {
                KeyEvent.VK_A,KeyEvent.VK_B,KeyEvent.VK_C,KeyEvent.VK_D,KeyEvent.VK_E,KeyEvent.VK_F,KeyEvent.VK_G,
                KeyEvent.VK_H,KeyEvent.VK_I,KeyEvent.VK_J,KeyEvent.VK_K,KeyEvent.VK_L,KeyEvent.VK_M,KeyEvent.VK_N,
                KeyEvent.VK_O,KeyEvent.VK_P,KeyEvent.VK_Q,KeyEvent.VK_R,KeyEvent.VK_S,KeyEvent.VK_T,KeyEvent.VK_U,
                KeyEvent.VK_V,KeyEvent.VK_W,KeyEvent.VK_X,KeyEvent.VK_Y,KeyEvent.VK_Z
        };
        int [] numbers = {
                KeyEvent.VK_0,KeyEvent.VK_1,KeyEvent.VK_2,KeyEvent.VK_3,KeyEvent.VK_4,KeyEvent.VK_5,KeyEvent.VK_6,
                KeyEvent.VK_7,KeyEvent.VK_8,KeyEvent.VK_9
        };
        if(c >= 'a' && c<= 'z'){
            r.keyPress(alphas[c-'a']);
            r.keyRelease(alphas[c-'a']);
        }
        else if(c >= 'A' && c <= 'Z'){
            r.keyPress(alphas[c-'A']);
            r.keyRelease(alphas[c-'A']);
        }
        else if(c >= '0' && c <= '9'){
            r.keyPress(numbers[c-'0']);
            r.keyRelease(numbers[c-'0']);
        }
        else{
            switch(c){
                case ':':
                    r.keyPress(KeyEvent.VK_SHIFT);
                    r.keyPress(KeyEvent.VK_SEMICOLON);
                    r.keyRelease(KeyEvent.VK_SEMICOLON);
                    r.keyRelease(KeyEvent.VK_SHIFT);
                    break;
                case '.':
                    r.keyPress(KeyEvent.VK_PERIOD);
                    r.keyRelease(KeyEvent.VK_PERIOD);
                    break;
                case '\\':
                    r.keyPress(KeyEvent.VK_BACK_SLASH);
                    r.keyRelease(KeyEvent.VK_BACK_SLASH);
                    break;
                case '\n':
                    r.keyPress(KeyEvent.VK_ENTER);
                    r.keyRelease(KeyEvent.VK_ENTER);
                    break;
                default:
                    throw(new Exception("[ERROR] makeRobotPressChar does not support "+c));
            }
        }
    }

    public static void main(String []  args) throws Exception {
        // 各种设置
        String loginUrl = "https://www.scboy.com/?user-login.htm";
        String myAccount = "18621105309";
        String myPwd = "123456";
        String scboyHomeUrl = "https://www.scboy.com/";
        String kplxqUrl = "https://www.scboy.com/?forum-1.htm";
        String postUrl = "https://www.scboy.com/?thread-create-1.htm";
        String postTitle = "论坛可以增加一个删自己帖子的功能吗？";
        String postContentString = "如题。";
        String myPicUrl = "C:\\Users\\11570\\BigBrother\\1.jpg";
        String titleAfterSuccessPost = "论坛功能/BUG报告-星际老男孩";

        //System.setProperty("webdriver.chrome.driver","C:\\Users\\11570\\driver\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver","/home/icyfrog/IdeaProjects/SoftWareTest3/src/main/resources/chromedriver");
        WebDriver driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        // 登录
        driver.get(loginUrl);
        WebElement acctInput = driver.findElement(By.id("mobile"));
        acctInput.sendKeys(myAccount);
        WebElement pwdInput = driver.findElement(By.id("password"));
        pwdInput.sendKeys(myPwd);
        WebElement submitButton = driver.findElement(By.id("submit"));
        submitButton.click();
        // 这时候登录应该已经完成，chrome在新的网页: https://www.scboy.com/
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
        // 接下来，按下“发新帖"按钮 准备发帖
        WebElement postButton = driver.findElement(By.xpath("//*[@id=\"body\"]/div/div/div[2]/a"));
        postButton.click();
        webDriverWait = new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.titleIs("发帖"));
        // 检查当前是否是发帖的界面
        if(!driver.getCurrentUrl().equals(postUrl)) {
            System.err.println("[Error] did not navigate page to https://www.scboy.com/?thread-create-1.htm!");
            System.exit(-1);
        }
        else{
            System.out.println("[Success] navigation succeeded! Chrome is now ready to post on scboy.com!");
        }
        // 发帖 填写下列表单：
        // 论坛分区
        Select forumSlect = new Select(driver.findElement(By.name("fid")));
        // 4 是论坛功能分区
        forumSlect.selectByValue("4");
        // 标题输入
        WebElement postTitleInput = driver.findElement(By.xpath("//*[@id=\"subject\"]"));
        postTitleInput.sendKeys(postTitle);
        // 帖子类型设置：常规主题
        WebElement postTypeInput = driver.findElement(By.xpath("//*[@id=\"form\"]/div[3]/span[2]/label[1]/input"));
        postTitleInput.click();
        // 分类：论坛功能建议
        WebElement postSubTypeInput = driver.findElement(By.xpath("//*[@id=\"nav_tag_list_div\"]/table/tbody/tr/td[2]/a[2]"));
        postSubTypeInput.click();
        // 文本内容 需要处理iframe
        // 这里的iframe 还比较特殊 因为他是一个editable的内容 用JavaScriptExecutor处理
        WebElement ueditorIframe = driver.findElement(By.id("ueditor_0"));
        driver.switchTo().frame(ueditorIframe);
        WebElement editableContent = driver.findElement(By.className("view"));
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("document.body.innerHTML = '<p>"+postContentString+"</p>'");
        // 回到default页面 上传一张图片
        driver.switchTo().defaultContent();
        WebElement fileUploadIcon = driver.findElement(By.xpath("//*[@id=\"form\"]/div[10]/div[1]/a"));
        fileUploadIcon.click();
        // 这里可以修改一下 Thread.sleep显然不是很优雅
        Thread.sleep(1000);
        // 用一个robot模拟人的输入
        Robot robot = new Robot();
        for(char c: myPicUrl.toCharArray()){
            makeRobotPressChar(robot,c);
        }
        makeRobotPressChar(robot,'\n');

        // 真的要发帖的时候 把shouldWeReallyPost设置成true
        // 只是测试环境/测试测试代码的时候 设置成false
        // 效果是在这里不会真的发帖 而是手动跳转到发帖后应该跳转到的界面
        if(shouldWeReallyPost) {
            submitButton = driver.findElement(By.id("submit"));
            submitButton.click();
        }
        else {
            driver.get("https://www.scboy.com/?forum-4.htm");
        }
        webDriverWait = new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.titleIs(titleAfterSuccessPost));
        WebElement myPost = driver.findElement(By.xpath("//*[text()='"+postTitle+"']"));
        if(myPost.isDisplayed()){
            System.out.println("[Success] post is successfully displayed on page!");
        }
        else{
            System.err.println("[Error] post failed or your post is not on page right now");
            System.exit(-1);
        }

        //driver.close();
    }
}
