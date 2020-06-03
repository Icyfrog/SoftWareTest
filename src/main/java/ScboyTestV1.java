import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class ScboyTestV1 {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Prepare information to visit web
        String loginUrl = "https://www.scboy.com/?user-login.htm";
        String myAccount = "18621105309";
        String myPwd = "123456";
        String scboyHomeUrl = "https://www.scboy.com/";
        String kplxqUrl = "https://www.scboy.com/?forum-1.htm";
        String postUrl = "https://www.scboy.com/?thread-create-1.htm";
        Integer OPTIONSNUM = 5;
        Integer OPTIONCHOSEN = 3;
        String TIMEDDL = "2020-08-01 13:09:38";
        String SUPERLINK = "https://ow.blizzard.cn/home";
        String SUPERLINKTITLE = "这个世界需要更多的英雄";


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
        postTitleInput.sendKeys("发帖测试");
        // 帖子类型设置：多选主题
        WebElement postTypeInput = driver.findElement(By.xpath("//input[@value = 2]"));
        postTypeInput.click();
        // 投票标题: Vote test
        WebElement voteTitleInput = driver.findElement(By.xpath("//*[@id='haya_poll_title']"));
        voteTitleInput.sendKeys("Vote test");
        // 投票选项: 增加五个选项
        WebElement addOptionsButton = driver.findElement(By.xpath("//a[text()='新增选项']"));
        for(int i=0; i<OPTIONSNUM-1; i++) {
            addOptionsButton.click();
        }
        // 投票选项: 最多选三项
        WebElement chosenOptionNum = driver.findElement(By.xpath("//*[@id='haya_poll_max_num']"));
        chosenOptionNum.clear();
        chosenOptionNum.sendKeys(String.valueOf(OPTIONCHOSEN));
        // 投票选项: 定义每项
        WebElement optionList = driver.findElement(By.xpath("//div[@class = 'haya-poll-options']"));
        String front = "./div[";
        String end = "]/div/input";
        for(int i=1; i <= OPTIONSNUM; i++) {
            String optionLocation = front + i + end;
            WebElement option = optionList.findElement(By.xpath(optionLocation));
            option.sendKeys("option test " + String.valueOf(i));
        }
        // 分类：论坛功能建议
        WebElement postSubTypeInput = driver.findElement(By.xpath("//*[@id=\"nav_tag_list_div\"]/table/tbody/tr/td[2]/a[2]"));
        postSubTypeInput.click();
        // 投票结束时间：
        WebElement timeDDLInput = driver.findElement(By.xpath("//*[@id='haya_poll_end_time']"));
        timeDDLInput.clear();
        timeDDLInput.sendKeys(TIMEDDL);

        // 切换为iframe，填写document
        WebElement ueditorIframe = driver.findElement(By.id("ueditor_0"));
        driver.switchTo().frame(ueditorIframe);
        driver.findElement(By.xpath("//body[@class='view']/p[1]")).sendKeys("来个超链接玩玩：\n");

        // 切换回主文档
        driver.switchTo().defaultContent();

        // ​新建超链接
        WebElement superLink = driver.findElement((By.xpath("//*[@id='edui95_body']")));
        superLink.click();

        // 切换为超链接的iframe, 用iframe填写超链接
        WebElement superLinkIframe = driver.findElement(By.id("edui89_iframe"));
        driver.switchTo().frame(superLinkIframe);
        driver.findElement(By.id("text")).sendKeys(SUPERLINKTITLE);
        driver.findElement(By.id("href")).sendKeys(SUPERLINK);
        //driver.findElement(By.id("title")).sendKeys("title test");
        driver.findElement(By.id("target")).click();

        // 切换为主文档，点击确认
        driver.switchTo().defaultContent();
        driver.findElement(By.id("edui93_body")).click();

        // 切换为iframe
        driver.switchTo().frame(ueditorIframe);
        driver.findElement(By.xpath("//body[@class='view']/p[2]")).sendKeys("\n");
        driver.findElement(By.xpath("//body[@class='view']/p[3]")).sendKeys("来个锚点玩玩\n");

        // 切换回主文档
        driver.switchTo().defaultContent();

        // 新建锚点，虽然我也不知道锚点有啥用
        driver.findElement(By.id("edui101_body")).click();

        // 切换为锚点的iframe
        WebElement anchorIframe = driver.findElement(By.id("edui97_iframe"));
        driver.switchTo().frame(anchorIframe);
        driver.findElement(By.id("anchorName")).sendKeys("One anchor");

        // 切换回主文档，点击确认
        driver.switchTo().defaultContent();
        driver.findElement(By.id("edui99_body")).click();


        /* 下面的是之前写的回答别人帖子，测试多选框的代码
        // 搜索帖子
        WebElement searchInput = driver.findElement(By.className("form-control"));
        searchInput.sendKeys("来投票你们想看的节目");
        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"search_form\"]/div/div/button"));
        searchButton.click();

        System.out.println("now title: "+driver.getTitle());
        System.out.println("Now thr url: " + driver.getCurrentUrl());
        // 现在应该在搜索结果页面
        // 检查

        webDriverWait = new WebDriverWait(driver,3);
        webDriverWait.until(ExpectedConditions.urlContains("search-_E6_9D_A5_E6_8A_95_E7"));
        if(!driver.getCurrentUrl().contains("search-_E6_9D_A5_E6_8A_95_E7")) {
            System.err.println("[Error] did not navigate page to \"来投票你们想看的节目\" !");
            System.exit(-1);
        }
        else{
            System.out.println("[Success] navigation succeeded! Chrome is now viewing 主题帖搜索”来投票你们想看的节目“ part");
        }

        //  WebElement destinationCard = driver.findElement(By.xpath("//*[@id=\"body\"]/div/div/div/div[2]/div[2]/ul/li/div[2]/div/a"));
        WebElement destinationCard2 = driver.findElement(By.xpath("//span[@class = 'text-danger']"));
        destinationCard2.click();

        //  Let's VOTE!
        WebElement voteButton1 = driver.findElement(By.xpath("//*[@id=\"body\"]/div/div/div/div[2]/div/div/div[2]/div/div/div/div/label"));
        voteButton1.click();

        //WebElement voteButton2 = driver.findElement(By.xpath("//*[@id=\"body\"]/div/div/div/div[2]/div/div/div[2]/div/div/div[3]/div/label"));
        WebElement voteButton2 = driver.findElement(By.xpath("//span[text()='吃鸡']"));
        voteButton2.click();
         */
    }
}