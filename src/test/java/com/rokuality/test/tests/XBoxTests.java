package com.rokuality.test.tests;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.rokuality.core.driver.By;
import com.rokuality.core.driver.DeviceCapabilities;
import com.rokuality.core.driver.Element;
import com.rokuality.core.driver.ScreenText;
import com.rokuality.core.driver.xbox.XBoxDeviceInfo;
import com.rokuality.core.driver.xbox.XBoxDriver;
import com.rokuality.core.enums.XBoxButton;
import com.rokuality.core.exceptions.NoSuchElementException;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.exceptions.SessionNotStartedException;

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

		// OCR module (Tesseract or GoogleVision)
		capabilities.addCapability("OCRType", "GoogleVision");
		capabilities.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");
		
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
				xboxDriver.options().setElementTimeout(1000);
				xboxDriver.finder().findElement(by, 500, 500, 100, 100);
			} catch (NoSuchElementException nse) {
				exception = nse;
			}
			Assert.assertNotNull(exception);
		}
		
	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void remoteControlTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		xboxDriver = new XBoxDriver(SERVER_URL, caps);
		xboxDriver.options().setElementTimeout(8000);

		xboxDriver.finder().findElement(By.Text("featured"));
		xboxDriver.remote().pressButton(XBoxButton.UP_ARROW);
		xboxDriver.remote().pressButton(XBoxButton.RIGHT_ARROW);
		xboxDriver.remote().pressButton(XBoxButton.RIGHT_ARROW);
		xboxDriver.remote().pressButton(XBoxButton.RIGHT_ARROW);
		xboxDriver.remote().pressButton(XBoxButton.A);
		xboxDriver.finder().findElement(By.Text("Watch with Your TV Provider"));
		System.out.println(xboxDriver.screen().getTextAsString());
		xboxDriver.remote().pressButton(XBoxButton.B);
		xboxDriver.remote().pressButton(XBoxButton.B);
		xboxDriver.finder().findElement(By.Text("featured"));

		System.out.println(xboxDriver.screen().getRecording());

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void deviceInfoTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		XBoxDeviceInfo deviceInfo = xboxDriver.info().getDeviceInfo();
		Assert.assertEquals("Xbox One", deviceInfo.getConsoleType());

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void getScreenTextGoogleVisionTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");
		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		xboxDriver.options().setElementTimeout(8000);
		xboxDriver.finder().findElement(By.Text("featured"));

		List<ScreenText> allScreenText = xboxDriver.screen().getText();
		
		boolean matchFound = false;
		for (ScreenText screenText : allScreenText) {
			if (screenText.getText().equals("Featured")) {
				matchFound = true;

				System.out.println(screenText);
				Point textLocation = screenText.getLocation();
				Assert.assertTrue(textLocation.x > 90 && textLocation.x < 110);
				Assert.assertTrue(textLocation.y > 440 && textLocation.y < 460);

				Dimension textSize = screenText.getSize();
				Assert.assertTrue(textSize.width > 130 && textSize.width < 140);
				Assert.assertTrue(textSize.height > 20 && textSize.height < 30);

				Assert.assertTrue(screenText.getWidth() > 130 && screenText.getWidth() < 140);
				Assert.assertTrue(screenText.getHeight() > 20 && screenText.getHeight() < 30);

				break;
			}
		}
		Assert.assertTrue(matchFound);

		List<ScreenText> allSubScreenText = xboxDriver.screen().getText(1, 1, 500, 500);
		Assert.assertFalse(allSubScreenText.isEmpty());
		Assert.assertTrue(allScreenText.size() != allSubScreenText.size());

		String subScreenTxt = xboxDriver.screen().getTextAsString(80, 400, 150, 50);
		Assert.assertFalse(subScreenTxt.toLowerCase().contains("featured"));
		Assert.assertTrue(xboxDriver.screen().getTextAsString().toLowerCase().contains("featured"));

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void getScreenSizeTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		Dimension screenSize = xboxDriver.screen().getSize();
		System.out.println(screenSize);
		Assert.assertTrue(screenSize.getWidth() > 1900 && screenSize.getWidth() < 1930);
		Assert.assertTrue(screenSize.getHeight() > 1000 && screenSize.getHeight() < 1090);

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void getScreenArtifactsTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		File screenImage = xboxDriver.screen().getImage();
		System.out.println("Screen image saved to: " + screenImage.getAbsolutePath());
		Assert.assertTrue(screenImage.exists() && screenImage.isFile());

		File screenSubImage = xboxDriver.screen().getImage(1, 1, 300, 300);
		System.out.println("Screen sub image saved to: " + screenSubImage.getAbsolutePath());
		Assert.assertTrue(screenSubImage.exists() && screenSubImage.isFile());

		File screenRecording = xboxDriver.screen().getRecording();
		System.out.println("Screen recording saved to: " + screenRecording.getAbsolutePath());
		Assert.assertTrue(screenRecording.exists() && screenRecording.isFile());
		
		File screenRecording2 = xboxDriver.screen().getRecording();
		Assert.assertTrue(screenRecording2.exists() && screenRecording2.isFile());
		Assert.assertFalse(screenRecording.equals(screenRecording2));

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void sessionNotStartedInvalidCapsTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.removeCapability("DeviceIPAddress");

		boolean success = false;
		try {
			xboxDriver = new XBoxDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			success = e.getMessage().contains("DeviceIPAddress");
		}
		Assert.assertTrue(success);
		caps.addCapability("DeviceIPAddress", "1.1.1.1");

		success = false;
		caps.removeCapability("App");
		try {
			xboxDriver = new XBoxDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("You must provide the App capability");
		}
		Assert.assertTrue(success);

		success = false;
		caps.addCapability("App", "MTV");
		caps.removeCapability("HomeHubIPAddress");
		try {
			xboxDriver = new XBoxDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("The HomeHubIPAddress capability cannot be null or empty!");
		}
		Assert.assertTrue(success);

		success = false;
		caps.addCapability("HomeHubIPAddress", "1.1.1.1");
		caps.removeCapability("DeviceName");
		try {
			xboxDriver = new XBoxDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("The DeviceName capability cannot be null or empty!");
		}
		Assert.assertTrue(success);

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void sessionNotStartedDeviceNotReachableTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("DeviceIPAddress", "1.1.1.1");

		boolean success = false;
		try {
			xboxDriver = new XBoxDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			success = e.getMessage().contains("Is the device online");
		}
		Assert.assertTrue(success);

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void sessionNotStartedInstallFailedTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", "no package here");

		boolean success = false;
		try {
			xboxDriver = new XBoxDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			success = true;
			Assert.assertTrue(e.getMessage().contains("Failed to decode app package file"));
		}
		Assert.assertTrue(success);

		caps.addCapability("AppPackage", "http://www.youwontfindanypackagehereatthislocationipromise.com");
		success = false;
		try {
			xboxDriver = new XBoxDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			success = true;
			Assert.assertTrue(e.getMessage().contains("The provided app package is not valid"));
		}
		Assert.assertTrue(success);

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void multiElementMatchFindTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");

		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		xboxDriver.options().setElementTimeout(15000);
		xboxDriver.finder().findElement(By.Text("featured"));

		xboxDriver.remote().pressButton(XBoxButton.UP_ARROW);
		xboxDriver.remote().pressButton(XBoxButton.RIGHT_ARROW);
		xboxDriver.remote().pressButton(XBoxButton.RIGHT_ARROW);
		xboxDriver.remote().pressButton(XBoxButton.RIGHT_ARROW);
		xboxDriver.remote().pressButton(XBoxButton.A);

		List<Element> elements = xboxDriver.finder().findElements(By.Text("sign in"));
		Assert.assertEquals(2, elements.size());

		Element elementMatch1 = elements.get(0);
		System.out.println(elementMatch1.toString());
		Assert.assertEquals("sign in", elementMatch1.getText());
		Point elementMatch1Location = elementMatch1.getLocation();
		Dimension elementMatch1Size = elementMatch1.getSize();
		Assert.assertTrue(elementMatch1Location.x > 970 && elementMatch1Location.x < 980);
		Assert.assertTrue(elementMatch1Location.y > 300 && elementMatch1Location.y < 320);
		Assert.assertTrue(elementMatch1Size.width > 60 && elementMatch1Size.width < 70);
		Assert.assertTrue(elementMatch1Size.height > 20 && elementMatch1Size.height < 30);

		Element elementMatch2 = elements.get(1);
		System.out.println(elementMatch2.toString());
		Assert.assertEquals("Sign In", elementMatch2.getText());
		Point elementMatch2Location = elementMatch2.getLocation();
		Dimension elementMatch2Size = elementMatch2.getSize();
		Assert.assertTrue(elementMatch2Location.x > 690 && elementMatch2Location.x < 705);
		Assert.assertTrue(elementMatch2Location.y > 420 && elementMatch2Location.y < 430);
		Assert.assertTrue(elementMatch2Size.width > 50 && elementMatch2Size.width < 65);
		Assert.assertTrue(elementMatch2Size.height > 10 && elementMatch2Size.height < 25);

	}

	@Test(groups = { "XBox" })
	@Features("XBox")
	public void isElementPresentTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");

		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		xboxDriver.options().setElementTimeout(15000);
		xboxDriver.finder().findElement(By.Text("featured"));

		boolean elementPresent = xboxDriver.finder().findElements(By.Text("featured")).size() > 0;
		Assert.assertTrue(elementPresent);

		xboxDriver.options().setElementTimeout(0);
		elementPresent = xboxDriver.finder().findElements(By.Text("no such element")).size() > 0;
		Assert.assertFalse(elementPresent);

	}

	
}
