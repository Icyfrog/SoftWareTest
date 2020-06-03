

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ScboyTest {

    //  注意自己的chromeDriver文件位置
    //static private String driverPath = "E:\\Documents\\SJTU\\software_testing\\hw\\chromedriver_win32\\chromedriver.exe";
    static private String driverPath = "/home/icyfrog/Downloads/chromedriver";
    static private WebDriver driver;
    //private Set<Cookie> cookies;

    @BeforeClass
    static public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver",driverPath);
        driver = new ChromeDriver();
        // 设置加载超时时间
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        // 定位时间，超时后抛异常
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();

    }

    @AfterClass
    static public void tearDown() throws Exception {
        // 执行完所有测试用例后关闭driver
        //driver.close();
    }

    // 登录操作
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

        // 这里不需要保存cookie，后面的函数中直接创建了新cookie
        //cookies = driver.manage().getCookies();
//        for (Cookie c : cookies) {
//            System.out.println(String.format("%s -> %s -> %s -> %s -> %s",
//                    c.getDomain(), c.getName(), c.getValue(), c.getExpiry(), c.getPath()));
//        }
    }

    // 从主页跳转到发帖页面的操作
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

    // 检查与表格相关的功能
    private void checkPostTable() throws Exception {
        String postUrl = "https://www.scboy.com/?thread-create-1.htm";
        Integer OPTIONSNUM = 5;
        Integer OPTIONCHOSEN = 3;
        String TIMEDDL = "2020-08-01 13:09:38";
        String SUPERLINK = "https://ow.blizzard.cn/home";
        String SUPERLINKTITLE = "这个世界需要更多的英雄";

        // 先get一次，添加cookie时要在同一个域名下
        driver.get(postUrl);
        // 先清空cookies
        driver.manage().deleteAllCookies();
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
        // 现在不用在登录界面输入用户名和密码就可以直接进入发帖页面了
        driver.get(postUrl);
        // 检查是否在发帖页面
        assertEquals(postUrl,driver.getCurrentUrl());

        // 接着检查页面上的各种组件功能-----------------------------------------------





        // 测试创建表格，模拟鼠标操作
        WebElement createTabel = driver.findElement(By.id("edui140_button_body"));
        assertTrue(createTabel.isEnabled());
        // 点击后出现选择行列的界面
        createTabel.click();
        // 移动鼠标
        WebElement tabelOverlay = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/div[2]/div/div/div[2]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(tabelOverlay);
        actions.perform();
        // 鼠标移动后显示对应的表格行列数
        WebElement tabelLabel = driver.findElement(By.id("edui141_label"));
        assertEquals("5列 x 5行", tabelLabel.getText());
        actions.click();
        actions.perform();
        // 在iframe中检查是否创建表格
        List<WebElement> rows, cols;
        rows = getTableFromFrame();
        assertEquals(5,rows.size());
        for(WebElement row : rows){
            cols = row.findElements(By.tagName("td"));
            assertEquals(5,cols.size());
        }
        // 选中表格
        actions.moveToElement(rows.get(2)).click().perform();
        // 记得要跳出frame
        driver.switchTo().defaultContent();

        // 插入行
        WebElement writeDate = driver.findElement(By.id("edui134"));
        writeDate.click();
        WebElement addRow = driver.findElement(By.id("edui145"));
        addRow.click();
        // 在frame中检查是否增加了一行
        rows = getTableFromFrame();
        assertEquals(6,rows.size());
        // 找到写入日期的单元格
        String date = rows.get(3).findElements(By.tagName("td")).get(2).getText();
        Date expectDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(formatter.format(expectDate),date);
        // 跳出frame
        driver.switchTo().defaultContent();

        // 删除行
        WebElement deleteRow = driver.findElement(By.id("edui146"));
        deleteRow.click();
        // 在frame中检查是否增加了一行
        rows = getTableFromFrame();
        assertEquals(5,rows.size());
        // 跳出frame
        driver.switchTo().defaultContent();

        // 插入列
        writeDate.click();
        WebElement addCol = driver.findElement(By.id("edui147"));
        addCol.click();
        // 进入frame检查是否在写入日期的列前加入一列
        rows = getTableFromFrame();
        WebElement row2 = rows.get(3);
        cols = row2.findElements(By.tagName("td"));
        assertEquals(6,cols.size());
        assertEquals(formatter.format(expectDate),cols.get(3).getText());
        driver.switchTo().defaultContent();

        // 删除列
        WebElement deleteCol = driver.findElement(By.id("edui148"));
        deleteCol.click();
        // 检查表格列数
        rows = getTableFromFrame();
        WebElement row0 = rows.get(0);
        cols = row0.findElements(By.tagName("td"));
        assertEquals(5,cols.size());
        // 下一个测试先在frame中操作，所以先不切换

        // 合并单元格
        // 先选中要合并的单元格 0,0 -> 1,1
        WebElement td0 = rows.get(0).findElements(By.tagName("td")).get(0);
        WebElement td1 = rows.get(1).findElements(By.tagName("td")).get(1);
        // 模拟鼠标点击拖动，选中单元格
        actions.dragAndDrop(td0,td1).perform();
        // 切换到默认界面
        driver.switchTo().defaultContent();
        // 强制等待1s，否则可能不能模拟点击功能项
        Thread.sleep(1000);
        // 点击功能项并检查
        WebElement mergeTd = driver.findElement(By.id("edui149"));
        mergeTd.click();
        // 检查
        rows = getTableFromFrame();
        cols = rows.get(0).findElements(By.tagName("td"));
        assertEquals(4,cols.size());
        assertEquals("2", cols.get(0).getAttribute("colspan"));
        assertEquals("2", cols.get(0).getAttribute("rowspan"));
        cols = rows.get(1).findElements(By.tagName("td"));
        assertEquals(3,cols.size());
        driver.switchTo().defaultContent();

        // 拆分单元格
        WebElement spliteTd = driver.findElement(By.id("edui152"));
        spliteTd.click();
        checkMergeAndSplit(0, 5, "1",0);
        checkMergeAndSplit(1, 5, "1",0);

        // 向右合并单元格
        WebElement mergeRight = driver.findElement(By.id("edui150"));
        mergeRight.click();
        checkMergeAndSplit(0, 4, "2",0);

        // 拆分成列
        WebElement splitToCols = driver.findElement(By.id("edui154"));
        splitToCols.click();
        checkMergeAndSplit(0, 5, "1",0);

        // 模拟键盘输入 控制光标在单元格中移动
        Robot robot = new Robot();
        for(int i=0; i<5; i++){
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
        }
        Thread.sleep(1000);
        // 向下合并单元格
        WebElement mergeDown = driver.findElement(By.id("edui151"));
        mergeDown.click();
        // 合并行后，上下两行的单元格行的宽度和每行的列数发生变化
        // 上面一行列数不变，仍然是5，被合并的单元格行宽变为2
        checkMergeAndSplit(1, 5, "2",1);
        // 下面一行的列数减小，变成4，行宽没有变化
        checkMergeAndSplit(2, 4, "1",1);

        // 拆分成行
        WebElement splitToRows = driver.findElement(By.id("edui153"));
        splitToRows.click();
        checkMergeAndSplit(1, 5, "1",0);

        // 表格前插入行
        WebElement addLine = driver.findElement(By.id("edui144"));
        addLine.click();
        WebElement frame = driver.findElement(By.xpath("//*[@id=\"ueditor_0\"]"));
        driver.switchTo().frame(frame);
        List<WebElement> lines = driver.findElements(By.tagName("p"));
        // 表格后原本有一个空行，表格前插入行后空行<p>应该有2个
        assertEquals(2,lines.size());


        // 删除表格
        // 先选中表格
        /*
        WebElement table = driver.findElement(By.xpath("/html/body/table/tbody"));
        actions.click(table).perform();
        driver.switchTo().defaultContent();
        Thread.sleep(1000);
        WebElement deleteTable = driver.findElement(By.id("edui143"));
        deleteTable.click();


        // 检查
        driver.switchTo().frame(frame);
        // 这里判断会等待久一些，因为应该找不到table标签，要等待异常处理
        assertFalse(isElementPresent(By.tagName("table")));
        */

        driver.switchTo().defaultContent();


    }

    // 减少重复代码，这个函数找到iframe中的表格的行，返回
    private List<WebElement> getTableFromFrame(){
        WebElement frame = driver.findElement(By.xpath("//*[@id=\"ueditor_0\"]"));
        driver.switchTo().frame(frame);
        WebElement table = driver.findElement(By.xpath("/html/body/table/tbody"));
        return table.findElements(By.tagName("tr"));
    }

    // 检查输入行数rowNum对应行的数量是否为expectSize
    // 检查对应行数的第一列的宽度，如果type==0，检查colspan，否则检查rowspan
    private void checkMergeAndSplit(Integer rowNum,
                                    long expectSize, String expectSapn, int type){
        // 检查
        // 在这个地方切换到frame中
        List<WebElement> rows = getTableFromFrame();
        List<WebElement> cols = rows.get(rowNum).findElements(By.tagName("td"));
        assertEquals(expectSize,cols.size());
        // 检查colspan
        if(type==0){
            assertEquals(expectSapn,cols.get(0).getAttribute("colspan"));
        }
        // 检查rowspan
        else{
            assertEquals(expectSapn,cols.get(0).getAttribute("rowspan"));
        }
        // 记得要切换回默认窗口
        driver.switchTo().defaultContent();
    }

    private boolean isElementPresent(By by){
        try{
            driver.findElement(by);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    // 选择帖子的主题（多选框）
    private void ChooseTheme() {
        Integer OPTIONSNUM = 5;
        Integer OPTIONCHOSEN = 3;
        String TIMEDDL = "2020-08-01 13:09:38";

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

        // 测试部分
        assertEquals("发帖测试", driver.findElement(By.xpath("//*[@id=\"subject\"]")).getAttribute("value"));
        //System.out.println("subject: " +  driver.findElement(By.xpath("//*[@id=\"subject\"]")).getAttribute("value"));
        assertEquals("Vote test", driver.findElement(By.xpath("//*[@id='haya_poll_title']")).getAttribute("value"));
        assertEquals("option test 1",
                driver.findElement(By.xpath("//div[@class = 'haya-poll-options']/div[1]/div/input")).getAttribute("value"));
        assertEquals("option test 2",
                driver.findElement(By.xpath("//div[@class = 'haya-poll-options']/div[2]/div/input")).getAttribute("value"));
        assertEquals("option test 3",
                driver.findElement(By.xpath("//div[@class = 'haya-poll-options']/div[3]/div/input")).getAttribute("value"));
        assertEquals("option test 4",
                driver.findElement(By.xpath("//div[@class = 'haya-poll-options']/div[4]/div/input")).getAttribute("value"));
        assertEquals("option test 5",
                driver.findElement(By.xpath("//div[@class = 'haya-poll-options']/div[5]/div/input")).getAttribute("value"));
        assertEquals("3",driver.findElement(By.xpath("//*[@id='haya_poll_max_num']")).getAttribute("value"));
        assertEquals("2020-08-01 13:09:38", driver.findElement(By.xpath("//*[@id='haya_poll_end_time']")).getAttribute("value"));
    }

    // 测试帖子中超链接和锚点功能
    private void HyperlinkAndAnchor() {

        String HYPERLINK = "https://ow.blizzard.cn/home";
        String HYPERLINKTITLE = "这个世界需要更多的英雄";

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
        driver.findElement(By.id("text")).sendKeys(HYPERLINKTITLE);
        driver.findElement(By.id("href")).sendKeys(HYPERLINK);
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

        // 测试部分
        driver.switchTo().frame(ueditorIframe);
        assertEquals("来个超链接玩玩：", driver.findElement(By.xpath("//body[@class='view']/p[1]")).getText());
        assertEquals("这个世界需要更多的英雄", driver.findElement(By.xpath("//body[@class='view']/p[2]")).getText());
        assertEquals("https://ow.blizzard.cn/home", driver.findElement(By.xpath("//body[@class='view']/p[2]/a")).getAttribute("href"));
        assertEquals("来个锚点玩玩", driver.findElement(By.xpath("//body[@class='view']/p[3]")).getText());
        assertEquals("One anchor", driver.findElement(By.xpath("//body[@class='view']/p[4]/img")).getAttribute("anchorname"));
        assertEquals("anchorclass", driver.findElement(By.xpath("//body[@class='view']/p[4]/img")).getAttribute("class"));

        driver.switchTo().defaultContent();
    }

    @Test
    public void testLoginAndJump(){
        checkLogin();
        jumpToKPL();

    }

    @Test
    public void testPost() throws Exception{
        checkPostTable();
        ChooseTheme();
        HyperlinkAndAnchor();
    }
}