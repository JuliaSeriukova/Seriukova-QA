import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertEquals;

public class Firsttest {
    private WebDriver driver;
    private String baseUrl;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "src/chromedriver");
        driver = new ChromeDriver();
        baseUrl = "https://sandbox.cardpay.com/MI/cardpayment2.html?orderXml=PE9SREVSIFdBTExFVF9JRD0nODI5OScgT1JERVJfTlVNQkVSPSc0NTgyMTEnIEFNT1VOVD0nMjkxLjg2JyBDVVJSRU5DWT0nRVVSJyAgRU1BSUw9J2N1c3RvbWVyQGV4YW1wbGUuY29tJz4KPEFERFJFU1MgQ09VTlRSWT0nVVNBJyBTVEFURT0nTlknIFpJUD0nMTAwMDEnIENJVFk9J05ZJyBTVFJFRVQ9JzY3NyBTVFJFRVQnIFBIT05FPSc4NzY5OTA5MCcgVFlQRT0nQklMTElORycvPgo8L09SREVSPg==&sha512=998150a2b27484b776a1628bfe7505a9cb430f276dfa35b14315c1c8f03381a90490f6608f0dcff789273e05926cd782e1bb941418a9673f43c47595aa7b8b0d";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().window().fullscreen();
    }

    @Test
    public void testPaymentStatusConfirmed() {
        driver.get(baseUrl);

        WebElement cardNumber =  driver.findElement(By.id("input-card-number"));
        WebElement cardHolder =  driver.findElement(By.id("input-card-holder"));

        cardNumber.click();
        cardNumber.clear();
        cardNumber.sendKeys("4000 0000 0000 0036");
        cardHolder.click();
        cardHolder.clear();
        cardHolder.sendKeys("SERIUKOVA JULIA");
        driver.findElement(By.id("card-expires-month")).click();
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("03");
        driver.findElement(By.id("card-expires-year")).click();
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2024");
        driver.findElement(By.id("input-card-cvc")).click();
        driver.findElement(By.id("input-card-cvc")).clear();
        driver.findElement(By.id("input-card-cvc")).sendKeys("022");
        driver.findElement(By.id("action-submit")).click();
      /*  driver.get("https://sandbox.cardpay.com/sandbox-emulator/acspage/cap?RID=136&VAA=A");
        driver.get("https://sandbox.cardpay.com/MI/cardpayment2.html?bank_id=sandbox");*/

        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-status']/div[2]")).getText(), "Confirmed");

    }

    @Test
    public void testPaymentStatusDeclined() {
        driver.get(baseUrl);

        WebElement cardNumber =  driver.findElement(By.id("input-card-number"));
        WebElement cardHolder =  driver.findElement(By.id("input-card-holder"));

        cardNumber.click();
        cardNumber.clear();
        cardNumber.sendKeys("5555 5555 5555 4477");
        cardHolder.click();
        cardHolder.clear();
        cardHolder.sendKeys("IVAN IVANOV");
        driver.findElement(By.id("card-expires-month")).click();
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("01");
        driver.findElement(By.id("card-expires-year")).click();
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2023");
        driver.findElement(By.id("input-card-cvc")).click();
        driver.findElement(By.id("input-card-cvc")).clear();
        driver.findElement(By.id("input-card-cvc")).sendKeys("999");
        driver.findElement(By.id("action-submit")).click();
      /*  driver.get("https://sandbox.cardpay.com/sandbox-emulator/acspage/cap?RID=136&VAA=A");
        driver.get("https://sandbox.cardpay.com/MI/cardpayment2.html?bank_id=sandbox");*/

        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-status']/div[2]")).getText(), "Declined by issuing bank");

    }
    @Test
    public void nonValidSymbolsFieldCardholder() throws InterruptedException {
        driver.get(baseUrl);

        WebElement cardNumber =  driver.findElement(By.id("input-card-number"));
        WebElement cardHolder =  driver.findElement(By.id("input-card-holder"));
        WebElement cvc =  driver.findElement(By.id("input-card-cvc"));

        cardNumber.clear();
        cardNumber.sendKeys("4000 0000 0000 0002");
        cardHolder.click();
        cardHolder.sendKeys("1111");
        cvc.click();
        Thread.sleep(3000);
        assertEquals(driver.findElement(By.xpath("//*[@id=\"card-holder-field\"]/div/label")).getText(), "Cardholder name is not valid");
    }

    @Test
    public void specialSymbolsFieldCardholder() throws InterruptedException {
        driver.get(baseUrl);

        WebElement cardNumber =  driver.findElement(By.id("input-card-number"));
        WebElement cardHolder =  driver.findElement(By.id("input-card-holder"));
        WebElement cvc =  driver.findElement(By.id("input-card-cvc"));

        cardNumber.clear();
        cardNumber.sendKeys("5555 5555 5555 4444");
        cardHolder.click();
        cardHolder.sendKeys("%%%");
        cvc.click();
        Thread.sleep(2000);
        assertEquals(driver.findElement(By.xpath("//*[@id=\"card-holder-field\"]/div/label")).getText(), "Cardholder name is not valid");
    }

    @Test
    public void testCardNumberValidator() {
        driver.get(baseUrl);

        WebElement cardNumber =  driver.findElement(By.id("input-card-number"));
        WebElement cardHolder =  driver.findElement(By.id("input-card-holder"));

        cardNumber.clear();
        cardNumber.sendKeys("1111 1111 1111 1111");
        cardHolder.click();

        assertEquals(driver.findElement(By.xpath("//*[@id=\"card-number-field\"]/div/label")).getText(), "Card number is not valid");
    }

    @Test
    public void questionMouse() throws InterruptedException {
        driver.get(baseUrl);
        // Find the CVC hint element.
        WebElement cvcHint = driver.findElement(By.id("cvc-hint"));

        // Move mouse cursor to the CVC hint element.
        Actions actions = new Actions(driver);
        actions.moveToElement(cvcHint).click().build().perform();

        // Wait for the animations to finish.
        Thread.sleep(1000);

        // Take a screenshot into `./target/screenshots/`
        String path = captureScreen();

        try {
            // Get expected image from the `./src/expected.png`.
            File expectedFile = new File("./src/expected.png");
            BufferedImage expectedImage = ImageIO.read(expectedFile);

            // Open captured image.
            File capturedFile = new File(path);
            BufferedImage capturedImage = ImageIO.read(capturedFile);

            // Delete captured image file. capturedImage is hold in memory.
            //capturedFile.delete();

            // Check if the expected image is equal to the captured image.
            assertTrue(bufferedImagesEqual(expectedImage, capturedImage));
        } catch (IOException e) {
            // If something goes wrong we just fail the test.
            fail("Unable to compare screenshots.");
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }

    private String captureScreen() {
        String path;
        try {
            TakesScreenshot webDriver = ((TakesScreenshot)driver);
            File source = webDriver.getScreenshotAs(OutputType.FILE);
            path = "./target/screenshots/" + source.getName();
            FileUtils.copyFile(source, new File(path));
        }
        catch(IOException e) {
            path = "Failed to capture screenshot: " + e.getMessage();
        }
        return path;
    }

    private boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
            for (int x = 0; x < img1.getWidth(); x++) {
                for (int y = 0; y < img1.getHeight(); y++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                        System.out.println(img1.getRGB(x, y));
                        System.out.println(img2.getRGB(x, y));
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

}
