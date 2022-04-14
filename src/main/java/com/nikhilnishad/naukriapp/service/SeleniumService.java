package com.nikhilnishad.naukriapp.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.nikhilnishad.naukriapp.model.UserCred;

import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class SeleniumService {
	
	
    public WebDriver setUp(){
        System.out.println("Getting Ready");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        String userAgent="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36";
        options.addArguments("user-agent="+userAgent);
        WebDriver driver = new ChromeDriver(options);
        driver.navigate().to("https://www.naukri.com/nlogin/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(150,TimeUnit.SECONDS);
        System.out.println("Bot Ready for Login");
        return driver;
    }

    public void userLogin(WebDriver driver, String username, String passwd)
    {
        System.out.println("Trying to login");
        try{
            FileInputStream fileInputStream
                    = new FileInputStream(username+"-cookie.data");
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(fileInputStream);
            Set<Cookie> cookies = (Set<Cookie>) objectInputStream.readObject();
            objectInputStream.close();
            driver.manage().deleteAllCookies();
            for (Cookie c:cookies) driver.manage().addCookie(c);
            driver.navigate().to("https://www.naukri.com/mnjuser/homepage");
            driver.manage().timeouts().implicitlyWait(150,TimeUnit.SECONDS);
        } catch (FileNotFoundException e) {
            try{
                System.out.println("Cookies not found");
                WebElement usernameTxt = driver.findElement(By.id("usernameField"));
                usernameTxt.sendKeys(username);
                WebElement passwordTxt = driver.findElement(By.id("passwordField"));
                passwordTxt.sendKeys(passwd);
                try{
                    passwordTxt.sendKeys(Keys.RETURN);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                Thread.sleep(1000);
                driver.manage().timeouts().implicitlyWait(150,TimeUnit.SECONDS);
                System.out.println("Current URL is:" + driver.getCurrentUrl());
                FileOutputStream fileOutputStream
                        = new FileOutputStream(username+"-cookie.data");
                ObjectOutputStream objectOutputStream
                        = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(driver.manage().getCookies());
                objectOutputStream.flush();
                objectOutputStream.close();
            } catch (IOException | InterruptedException ex){
                ex.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Login Completed");
    }

    public void updateUserProfile(WebDriver driver) throws InterruptedException {
        System.out.println("Updating Profile");
        driver.navigate().to("https://www.naukri.com/mnjuser/profile?id=&orgn=homepage");
        driver.manage().timeouts().implicitlyWait(350,TimeUnit.SECONDS);
        Thread.sleep(1000);
        WebElement editButtonForHeadline = driver.findElement(By.xpath("/html/body/div[2]/div/div/span/div/div/div/div/div/div[2]/div[3]/div[3]/div/div/div/div[1]/span[2]"));
        editButtonForHeadline.click();
        Thread.sleep(1000);
        WebElement textBoxInput=driver.findElement(By.id("resumeHeadlineTxt"));
        String headlineText = textBoxInput.getText();
        Thread.sleep(1000);
        if (!headlineText.endsWith(".")){
            headlineText+=".";
        }
        else headlineText=headlineText.substring(0,headlineText.lastIndexOf("."));
        textBoxInput.clear();
        Thread.sleep(200);
        textBoxInput.sendKeys(headlineText);
        Thread.sleep(200);
        //as unable to find below xpath on multiple driver instance
        //new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[5]/div[4]/div[2]/form/div[3]/div/button"))).click();
//        List<WebElement> listOfButtons = driver.findElements(By.xpath("/html/body/div[5]/div[7]/div[2]/form/div[3]/div/button"));
//        System.out.println(listOfButtons);
//        if(listOfButtons.size()==0) {
//        	driver.findElement(By.xpath("/html/body/div[5]/div[4]/div[2]/form/div[3]/div/button")).click();
//        }
//        driver.findElement(By.xpath("/html/body/div[5]/div[7]/div[2]/form/div[3]/div/button")).click();
        
        textBoxInput.submit();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println(String.format("Updated Profile at:" + formatter.format(date)).toString());
    }

    public void tearDown(WebDriver driver) throws InterruptedException {
        if (driver != null) {
            //Thread.sleep(2000);
        	driver.manage().timeouts().implicitlyWait(500,TimeUnit.SECONDS);
            driver.quit();
        }
    }

	public void startBotForUser(UserCred userCred) {
		WebDriver driver=setUp();
		userLogin(driver,userCred.getEmail(),userCred.getPassword());
		System.out.println("login completed");
		try {
			updateUserProfile(driver);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			try {
				tearDown(driver);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
