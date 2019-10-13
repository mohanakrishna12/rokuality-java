package com.rokuality.test.tests;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.Arrays;

import com.rokuality.core.driver.By;
import com.rokuality.core.driver.DeviceCapabilities;
import com.rokuality.core.driver.Element;
import com.rokuality.core.driver.xbox.XBoxDriver;
import com.rokuality.core.exceptions.NoSuchElementException;
import com.rokuality.core.exceptions.ServerFailureException;

import org.testng.annotations.*;

import junit.framework.Assert;
import ru.yandex.qatools.allure.annotations.Features;

public class XBoxTests {

	private static final File XBOX_IMAGES_DIR = new File(String.join(File.separator, System.getProperty("user.dir"),
			"src", "test", "resources", "images", "xboximages") + File.separator);
	private static final File DEMO_APP_PACKAGE = new File(
			XBOX_IMAGES_DIR.getParentFile().getParent() + File.separator + "XBoxDebug.appxbundle");

	private static final String SERVER_URL = "http://localhost:7777";

	private XBoxDriver xboxDriver = null;

	@BeforeMethod(alwaysRun = true)
	public synchronized void beforeTest() {

	}

	@AfterMethod(alwaysRun = true)
	public void afterTest() {
		if (xboxDriver != null && xboxDriver.getSession() != null) {
			try {
				xboxDriver.stop();
			} catch (ServerFailureException e) {
				// thrown under various conditions of a server teardown such as the session not
				// found during teardown, etc
			}
		}
	}

	private DeviceCapabilities setBaseCapabilities() {

		DeviceCapabilities capabilities = new DeviceCapabilities();

		// Indicates we want a Roku test session
		capabilities.addCapability("Platform", "XBox");

		// The ip address of your harmony
		capabilities.addCapability("HomeHubIPAddress", "192.168.1.41");
		
		// The name of your device as saved in harmony
		capabilities.addCapability("DeviceName", "XboxOne");

		// Location (path or url) to an appxbundle file
		capabilities.addCapability("AppPackage", "https://rokualitypublic.s3.amazonaws.com/XBoxDebug.appxbundle");
		capabilities.addCapability("App", "MTV");
		
		// Your Xbox device ip address.
		capabilities.addCapability("DeviceIPAddress", "192.168.1.36");

		// OPTIONAL A base image match similarity tolerance between 0 and 1
		capabilities.addCapability("ImageMatchSimilarity", .89);

		// OPTIONAL A forced image resolution size that all image captures are resized
		// to (width/height).
		capabilities.addCapability("ScreenSizeOverride", "1920x1080");

		// OPTIONAL ocr module 
		capabilities.addCapability("OCRType", "GoogleVision");
		capabilities.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");

		// OPTIONAL - enforce ocr case sensitivity. If not provided all words during ocr
		// evaluation are forced to lowercase
		// for better success chances. To enforce case sensitivity set to true; defaults
		// to false;
		capabilities.addCapability("OCRCaseSensitive", true); // TODO - implement in server

		return capabilities;
	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void installFromUrlTest() {

		// install app from url
		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", "https://rokualitypublic.s3.amazonaws.com/XBoxDebug.appxbundle");

		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		xboxDriver.options().setElementTimeout(15000);
		xboxDriver.finder().findElement(By.Text("featured"));

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void installFromLocalFileTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", DEMO_APP_PACKAGE.getAbsolutePath());
		caps.addCapability("App", "MTV");
		
		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		xboxDriver.options().setElementTimeout(15000);
		xboxDriver.finder().findElement(By.Text("featured"));

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void launchAlreadyInstalledApp() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.removeCapability("AppPackage");
		caps.addCapability("App", "MTV");
		
		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		xboxDriver.options().setElementTimeout(15000);
		xboxDriver.finder().findElement(By.Text("featured"));

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void findElementFromTextWithGoogleVisionTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");

		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		xboxDriver.options().setElementTimeout(15000);
		Element element = xboxDriver.finder().findElement(By.Text("featured"));
		System.out.println(element);
		
		Point elementLocation = element.getLocation();
		Assert.assertTrue(elementLocation.x > 90 && elementLocation.x < 1100);
		Assert.assertTrue(elementLocation.y > 450 && elementLocation.y < 460);

		Dimension elementSize = element.getSize();
		Assert.assertTrue(elementSize.width > 130 && elementSize.width < 145);
		Assert.assertTrue(elementSize.height > 20 && elementSize.height < 30);

		String elementText = element.getText();
		Assert.assertEquals("Featured", elementText);

		double confidence = element.getConfidence();
		Assert.assertEquals(0.0, confidence); // confidence only for tesseract

		Assert.assertTrue(!element.getElementID().isEmpty());
		Assert.assertEquals(element.getSessionID(), xboxDriver.getSessionID());

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void findElementFromImageTest() {

		DeviceCapabilities caps = setBaseCapabilities();

		xboxDriver = new XBoxDriver(SERVER_URL, caps);
		xboxDriver.options().setElementTimeout(15000);

		for (By by : Arrays.asList(By.Image(XBOX_IMAGES_DIR.getAbsolutePath() + File.separator + "featured.png"), 
				By.Image("https://dl.dropboxusercontent.com/s/36upxsx1cbhh3tp/featured.png"))) {
					
			Element element = xboxDriver.finder().findElement(by);
			System.out.println(element);
			Point elementLocation = element.getLocation();
			Assert.assertTrue(elementLocation.x > 90 && elementLocation.x < 110);
			Assert.assertTrue(elementLocation.y > 445 && elementLocation.y < 460);
	
			Dimension elementSize = element.getSize();
			Assert.assertTrue(elementSize.width > 140 && elementSize.width < 150);
			Assert.assertTrue(elementSize.height > 25 && elementSize.height < 35);
	
			double confidence = element.getConfidence();
			Assert.assertTrue(confidence > 0.90);
	
			Assert.assertTrue(!element.getText().isEmpty());
			Assert.assertTrue(!element.getElementID().isEmpty());
			Assert.assertEquals(element.getSessionID(), xboxDriver.getSessionID());
		}

	}

	@Test(groups = { "XBox" }, expectedExceptions = NoSuchElementException.class)
	@Features("XBox")
	public void elementNotFoundInTextTesseractTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "Tesseract");
		xboxDriver = new XBoxDriver(SERVER_URL, caps);
		xboxDriver.finder().findElement(By.Text("not found"));

	}

	@Test(groups = { "XBox" }, expectedExceptions = NoSuchElementException.class)
	@Features("XBox")
	public void elementNotFoundInTextGoogleVisionTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		xboxDriver = new XBoxDriver(SERVER_URL, caps);
		xboxDriver.finder().findElement(By.Text("not found"));

	}

	@Test(groups = { "XBox" }, expectedExceptions = NoSuchElementException.class)
	@Features("Xbox")
	public void elementNotFoundImageTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		xboxDriver = new XBoxDriver(SERVER_URL, caps);
		xboxDriver.finder()
				.findElement(By.Image(XBOX_IMAGES_DIR.getAbsolutePath() + File.separator + "helloworld.png"));

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void elementSetTimeoutTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		xboxDriver.options().setElementTimeout(5000);
		long startTime = System.currentTimeMillis();
		Exception exception = null;
		try {
			xboxDriver.finder().findElement(By.Text("not here"));
		} catch (NoSuchElementException nse) {
			exception = nse;
		}
		Assert.assertNotNull(exception);

		long endTime = System.currentTimeMillis();
		long diffInSeconds = (endTime - startTime) / 1000;
		System.out.println(diffInSeconds);
		Assert.assertTrue(diffInSeconds >= 5 && diffInSeconds <= 7);

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void findElementInSubScreenTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		xboxDriver = new XBoxDriver(SERVER_URL, caps);
		xboxDriver.options().setElementTimeout(15000);
		
		Element element = xboxDriver.finder().findElement(By.Text("featured"));
		System.out.println(element);

		for (By by : Arrays.asList(By.Image(XBOX_IMAGES_DIR.getAbsolutePath() + File.separator + "featured.png"), By.Text("featured"))) {
			xboxDriver.finder().findElement(by, 75, 410, 230, 90);

			Exception exception = null;
			try {
				xboxDriver.finder().findElement(by, 500, 500, 100, 100);
			} catch (NoSuchElementException nse) {
				exception = nse;
			}
			Assert.assertNotNull(exception);
		}
		
	}

	
}
