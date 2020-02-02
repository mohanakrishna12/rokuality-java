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
import com.rokuality.core.driver.roku.RokuBy;
import com.rokuality.core.driver.roku.RokuDeviceInfo;
import com.rokuality.core.driver.roku.RokuDriver;
import com.rokuality.core.driver.roku.RokuMediaPlayerInfo;
import com.rokuality.core.enums.RokuButton;
import com.rokuality.core.enums.SessionStatus;
import com.rokuality.core.exceptions.NoSuchElementException;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.exceptions.SessionNotStartedException;

import org.json.simple.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class RokuTests {

	private static final File ROKU_IMAGES_DIR = new File(String.join(File.separator, System.getProperty("user.dir"),
			"src", "test", "resources", "images", "rokuimages") + File.separator);
	private static final File HELLO_WORLD_ZIP = new File(
			ROKU_IMAGES_DIR.getParentFile().getParent() + File.separator + "helloworld.zip");
	private static final String DEBUG_URL_ZIP = "https://rokualitypublic.s3.amazonaws.com/RokuDebug2.zip";
	private static final File INVALID_PACKAGE_ZIP = new File(
			ROKU_IMAGES_DIR.getParentFile().getParent() + File.separator + "invalidapppackage.zip");

	private static final String SERVER_URL = "http://localhost:7777";

	private RokuDriver rokuDriver = null;

	@BeforeMethod(alwaysRun = true)
	public void beforeTest() {

	}

	@AfterMethod(alwaysRun = true)
	public void afterTest() {
		if (rokuDriver != null && rokuDriver.getSession() != null) {
			try {
				rokuDriver.stop();
			} catch (ServerFailureException e) {
				// thrown under various conditions of a server teardown such as the session not
				// found during teardown, etc
			}
		}
	}

	private DeviceCapabilities setBaseCapabilities() {

		DeviceCapabilities capabilities = new DeviceCapabilities();

		// Indicates we want a Roku test session
		capabilities.addCapability("Platform", "Roku");

		// OCR module - Options are 'Tesseract' or 'GoogleVision'
		capabilities.addCapability("OCRType", "Tesseract");

		// Location (path or url to a sideloadable zip)
		capabilities.addCapability("AppPackage", "https://rokualitypublic.s3.amazonaws.com/RokualityDemoApp.zip");

		// Your Roku device ip address.
		capabilities.addCapability("DeviceIPAddress", "192.168.1.43");

		// Your Roku device username and password as created during your device
		// developer setup.
		capabilities.addCapability("DeviceUsername", "rokudev");
		capabilities.addCapability("DevicePassword", "1234");

		// OPTIONAL A base image match similarity tolerance between 0 and 1
		capabilities.addCapability("ImageMatchSimilarity", .89);

		// OPTIONAL A forced image resolution size that all image captures are resized
		// to (width/height).
		capabilities.addCapability("ScreenSizeOverride", "1280x820");

		return capabilities;
	}

	@Test(groups = { "Roku" })
	public void installFromUrlTest() {

		// install app from url
		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", "https://rokualitypublic.s3.amazonaws.com/RokualityDemoApp.zip");

		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));

	}

	@Test(groups = { "Roku" })
	public void installFromLocalFileTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", HELLO_WORLD_ZIP.getAbsolutePath());

		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("Hello World!"));

	}

	@Test(groups = { "Roku" })
	public void launchAlreadyInstalledApp() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.stop();

		caps.removeCapability("AppPackage");
		caps.addCapability("App", "dev"); // NOTE - only use this capability if an already sideloaded zip is loaded on
											// the roku!
		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));

	}

	@Test(groups = { "Roku" })
	public void findElementFromTextWithTesseractTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "Tesseract");

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		Element element = rokuDriver.finder().findElement(By.Text("SHOWS"));
		System.out.println(element);

		Point elementLocation = element.getLocation();
		Assert.assertTrue(elementLocation.x > 100 && elementLocation.x < 120);
		Assert.assertTrue(elementLocation.y > 575 && elementLocation.y < 590);

		Dimension elementSize = element.getSize();
		Assert.assertTrue(elementSize.width > 90 && elementSize.width < 105);
		Assert.assertTrue(elementSize.height > 15 && elementSize.height < 25);

		String elementText = element.getText();
		Assert.assertEquals("SHOWS", elementText);

		double confidence = element.getConfidence();
		Assert.assertTrue(confidence > 85.0);

		Assert.assertTrue(!element.getElementID().isEmpty());
		Assert.assertEquals(element.getSessionID(), rokuDriver.getSessionID());

	}

	@Test(groups = { "Roku" })
	public void findElementFromTextWithGoogleVisionTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		Element element = rokuDriver.finder().findElement(By.Text("SHOWS"));
		System.out.println(element);
		
		Point elementLocation = element.getLocation();
		Assert.assertTrue(elementLocation.x > 100 && elementLocation.x < 120);
		Assert.assertTrue(elementLocation.y > 575 && elementLocation.y < 590);

		Dimension elementSize = element.getSize();
		Assert.assertTrue(elementSize.width > 90 && elementSize.width < 105);
		Assert.assertTrue(elementSize.height > 15 && elementSize.height < 25);

		String elementText = element.getText();
		Assert.assertEquals("SHOWS", elementText);

		double confidence = element.getConfidence();
		Assert.assertEquals(0.0, confidence); // confidence only for tesseract

		Assert.assertTrue(!element.getElementID().isEmpty());
		Assert.assertEquals(element.getSessionID(), rokuDriver.getSessionID());

	}

	@Test(groups = { "Roku" })
	public void findElementFromImageTest() {

		DeviceCapabilities caps = setBaseCapabilities();

		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.options().setElementTimeout(5000);

		for (By by : Arrays.asList(By.Image(ROKU_IMAGES_DIR.getAbsolutePath() + File.separator + "shows.png"), 
				By.Image("https://dl.dropboxusercontent.com/s/jfywmqqnsndgki8/shows.png"))) {
					
			Element element = rokuDriver.finder().findElement(by);
			System.out.println(element);
			Point elementLocation = element.getLocation();
			Assert.assertTrue(elementLocation.x > 100 && elementLocation.x < 120);
			Assert.assertTrue(elementLocation.y > 560 && elementLocation.y < 580);
	
			Dimension elementSize = element.getSize();
			Assert.assertEquals(110, elementSize.width);
			Assert.assertTrue(elementSize.width > 100 && elementSize.width < 120);
			Assert.assertTrue(elementSize.height > 30 && elementSize.height < 50);
	
			double confidence = element.getConfidence();
			Assert.assertTrue(confidence > 0.90);
	
			Assert.assertTrue(!element.getText().isEmpty());
			Assert.assertTrue(!element.getElementID().isEmpty());
			Assert.assertEquals(element.getSessionID(), rokuDriver.getSessionID());
		}

	}

	@Test(groups = { "Roku" })
	public void findElementFromWebDriverLocatorsTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", DEBUG_URL_ZIP);

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(10000);
		if (rokuDriver.finder().findElements(RokuBy.Text("ACCEPT & CONTINUE")).size() > 0) {
			rokuDriver.remote().pressButton(RokuButton.SELECT);
		}
		
		Element elementByText = rokuDriver.finder().findElement(RokuBy.Text("FEATURED"));
		System.out.println(elementByText);
		Assert.assertTrue(elementByText.getElementJSON().toJSONString().contains("FEATURED"));

		Element elementByTag = rokuDriver.finder().findElement(RokuBy.Tag("Label"));
		System.out.println(elementByTag);
		Assert.assertEquals(0, elementByTag.getX());
		Assert.assertEquals(0, elementByTag.getY());
		Assert.assertEquals(663, elementByTag.getWidth());
		Assert.assertEquals(151, elementByTag.getHeight());

		Element elementByAttribute = rokuDriver.finder().findElement(RokuBy.Attribute("bounds", "{0, 0, 663, 151}"));
		System.out.println(elementByAttribute.getElementJSON());
		Assert.assertEquals(0, elementByAttribute.getX());
		Assert.assertEquals(0, elementByAttribute.getY());
		Assert.assertEquals(663, elementByAttribute.getWidth());
		Assert.assertEquals(151, elementByAttribute.getHeight());
		
	}

	@Test(groups = { "Roku" }, expectedExceptions = NoSuchElementException.class)
	public void elementNotFoundInTextTesseractTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.finder().findElement(By.Text("not found"));

	}

	@Test(groups = { "Roku" }, expectedExceptions = NoSuchElementException.class)
	public void elementNotFoundInTextGoogleVisionTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");
		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.finder().findElement(By.Text("not found"));

	}

	@Test(groups = { "Roku" }, expectedExceptions = NoSuchElementException.class)
	public void elementNotFoundImageTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.finder()
				.findElement(By.Image(ROKU_IMAGES_DIR.getAbsolutePath() + File.separator + "helloworld.png"));

	}

	@Test(groups = { "Roku" }, expectedExceptions = NoSuchElementException.class)
	public void elementNotFoundWebDriverTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", DEBUG_URL_ZIP);
		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.finder().findElement(RokuBy.Text("not found"));

	}

	@Test(groups = { "Roku" })
	public void elementSetTimeoutTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		long startTime = System.currentTimeMillis();
		Exception exception = null;
		try {
			rokuDriver.finder().findElement(By.Text("not here"));
		} catch (NoSuchElementException nse) {
			exception = nse;
		}
		Assert.assertNotNull(exception);

		long endTime = System.currentTimeMillis();
		long diffInSeconds = (endTime - startTime) / 1000;
		System.out.println(diffInSeconds);
		Assert.assertTrue(diffInSeconds >= 5 && diffInSeconds <= 7);

	}

	@Test(groups = { "Roku" })
	public void elementSetImageMatchSimilarityTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", HELLO_WORLD_ZIP.getAbsolutePath());
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setImageMatchSimilarity(0.10);
		rokuDriver.finder().findElement(By.Image(ROKU_IMAGES_DIR.getAbsolutePath() + File.separator + "shows.png"));

		rokuDriver.options().setImageMatchSimilarity(0.99);
		Exception exception = null;
		try {
			rokuDriver.finder().findElement(By.Image(ROKU_IMAGES_DIR.getAbsolutePath() + File.separator + "shows.png"));
		} catch (NoSuchElementException nse) {
			exception = nse;
		}
		Assert.assertNotNull(exception);

	}

	@Test(groups = { "Roku" })
	public void findElementInSubScreenTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.options().setElementTimeout(5000);
		
		for (By by : Arrays.asList(By.Image(ROKU_IMAGES_DIR.getAbsolutePath() + File.separator + "shows.png"), By.Text("SHOWS"))) {
			
			rokuDriver.finder().findElement(by, 100, 500, 300, 200);

			Exception exception = null;
			try {
				rokuDriver.finder().findElement(by, 1, 1, 100, 500);
			} catch (NoSuchElementException nse) {
				exception = nse;
			}
			Assert.assertNotNull(exception);
		}
		
	}

	@Test(groups = { "Roku" })
	public void remoteControlTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.options().setElementTimeout(5000);

		rokuDriver.finder().findElement(By.Text("SHOWS"));
		rokuDriver.remote().pressButton(RokuButton.BACK);
		rokuDriver.finder().findElement(By.Text("EXIT"));
		rokuDriver.remote().pressButton(RokuButton.SELECT);
		rokuDriver.finder().findElement(By.Text("SHOWS"));

		rokuDriver.remote().pressButton(RokuButton.OPTION);
		rokuDriver.finder().findElement(By.Text("LEGAL"));

		rokuDriver.remote().pressButton(RokuButton.BACK);

		for (RokuButton button : RokuButton.values()) {
			rokuDriver.remote().pressButton(button);
		}

	}

	@Test(groups = { "Roku" })
	public void deviceInfoTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		RokuDeviceInfo deviceInfo = rokuDriver.info().getDeviceInfo();
		Assert.assertTrue(deviceInfo.getVendorName().equals("Roku"));

	}

	@Test(groups = { "Roku" })
	public void noScreenSizeOverrideCapTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.removeCapability("ScreenSizeOverride");
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));

	}

	@Test(groups = { "Roku" })
	public void getScreenTextTesseractTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));

		List<ScreenText> allScreenText = rokuDriver.screen().getText();
		boolean matchFound = false;
		for (ScreenText screenText : allScreenText) {
			if (screenText.getText().equals("SHOWS")) {
				matchFound = true;

				System.out.println(screenText);
		
				Point textLocation = screenText.getLocation();
				Assert.assertTrue(textLocation.x > 100 && textLocation.x < 120);
				Assert.assertTrue(textLocation.y > 575 && textLocation.y < 590);

				Dimension textSize = screenText.getSize();
				Assert.assertTrue(textSize.width > 90 && textSize.width < 105);
				Assert.assertTrue(textSize.height > 15 && textSize.height < 25);

				Assert.assertTrue(screenText.getWidth() > 90 && screenText.getWidth() < 105);
				Assert.assertTrue(screenText.getHeight() > 15 && screenText.getHeight() < 25);

				double confidence = screenText.getConfidence();
				Assert.assertTrue(confidence > 89);
				break;
			}
		}
		Assert.assertTrue(matchFound);
		
		List<ScreenText> allSubScreenText = rokuDriver.screen().getText(1, 1, 500, 500);
		Assert.assertFalse(allSubScreenText.isEmpty());
		Assert.assertTrue(allScreenText.size() != allSubScreenText.size());

		String subScreenTxt = rokuDriver.screen().getTextAsString(1, 1, 500, 500);
		Assert.assertFalse(subScreenTxt.toLowerCase().contains("shows"));
		Assert.assertTrue(rokuDriver.screen().getTextAsString().contains("SHOWS"));

	}

	@Test(groups = { "Roku" })
	public void getScreenTextGoogleVisionTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));

		List<ScreenText> allScreenText = rokuDriver.screen().getText();
		boolean matchFound = false;
		for (ScreenText screenText : allScreenText) {
			if (screenText.getText().equals("SHOWS")) {
				matchFound = true;

				Point textLocation = screenText.getLocation();
				Assert.assertTrue(textLocation.x > 100 && textLocation.x < 120);
				Assert.assertTrue(textLocation.y > 575 && textLocation.y < 590);

				Dimension textSize = screenText.getSize();
				Assert.assertTrue(textSize.width > 90 && textSize.width < 105);
				Assert.assertTrue(textSize.height > 15 && textSize.height < 25);

				Assert.assertTrue(screenText.getWidth() > 90 && screenText.getWidth() < 105);
				Assert.assertTrue(screenText.getHeight() > 15 && screenText.getHeight() < 25);

				break;
			}
		}
		Assert.assertTrue(matchFound);

		List<ScreenText> allSubScreenText = rokuDriver.screen().getText(1, 1, 500, 500);
		Assert.assertFalse(allSubScreenText.isEmpty());
		Assert.assertTrue(allScreenText.size() != allSubScreenText.size());

		String subScreenTxt = rokuDriver.screen().getTextAsString(1, 1, 500, 500);
		Assert.assertFalse(subScreenTxt.toLowerCase().contains("shows"));
		Assert.assertTrue(rokuDriver.screen().getTextAsString().contains("SHOWS"));

	}

	@Test(groups = { "Roku" })
	public void getScreenSizeTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		Dimension screenSize = rokuDriver.screen().getSize();
		System.out.println(screenSize);
		Assert.assertTrue(screenSize.getWidth() > 1900 && screenSize.getWidth() < 1930);
		Assert.assertTrue(screenSize.getHeight() > 1000 && screenSize.getHeight() < 1090);

	}

	@Test(groups = { "Roku" })
	public void getScreenArtifactsTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", DEBUG_URL_ZIP);
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(10000);
		rokuDriver.finder().findElement(By.Text("FEATURED"));

		File screenImage = rokuDriver.screen().getImage();
		System.out.println("Screen image saved to: " + screenImage.getAbsolutePath());
		Assert.assertTrue(screenImage.exists() && screenImage.isFile());

		File screenSubImage = rokuDriver.screen().getImage(1, 1, 300, 300);
		System.out.println("Screen sub image saved to: " + screenSubImage.getAbsolutePath());
		Assert.assertTrue(screenSubImage.exists() && screenSubImage.isFile());

		File screenRecording = rokuDriver.screen().getRecording();
		System.out.println("Screen recording saved to: " + screenRecording.getAbsolutePath());
		Assert.assertTrue(screenRecording.exists() && screenRecording.isFile());
		
		File screenRecording2 = rokuDriver.screen().getRecording();
		System.out.println("Screen recording saved to: " + screenRecording2.getAbsolutePath());
		Assert.assertTrue(screenRecording2.exists() && screenRecording2.isFile());
		Assert.assertFalse(screenRecording.equals(screenRecording2));

		String xmlSource = rokuDriver.screen().getPageSource();
		Assert.assertTrue(xmlSource.contains("FEATURED"));

	}

	@Test(groups = { "Roku" })
	public void complicatedWordFindTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", HELLO_WORLD_ZIP.getAbsolutePath());
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.finder().findElement(By.Text("Hello"));
		rokuDriver.finder().findElement(By.Text("World!"));
		rokuDriver.finder().findElement(By.Text("Hello World!"));

		boolean success = false;
		try {
			rokuDriver.finder().findElement(By.Text("World! Hello"));
		} catch (NoSuchElementException e) {
			success = true;
		}
		Assert.assertTrue(success);

		try {
			rokuDriver.finder().findElement(By.Text("Hello World"));
		} catch (NoSuchElementException e) {
			success = true;
		}
		Assert.assertTrue(success);

		success = false;
		try {
			rokuDriver.finder().findElement(By.Text("Hello World!Not"));
		} catch (NoSuchElementException e) {
			success = true;
		}
		Assert.assertTrue(success);

		success = false;
		try {
			rokuDriver.finder().findElement(By.Text("Hello World! Not"));
		} catch (NoSuchElementException e) {
			success = true;
		}
		Assert.assertTrue(success);

	}

	@Test(groups = { "Roku" })
	public void sessionNotStartedInvalidCapsTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.removeCapability("DeviceIPAddress");

		boolean success = false;
		try {
			rokuDriver = new RokuDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			success = e.getMessage().contains("DeviceIPAddress");
		}
		Assert.assertTrue(success);
		caps.addCapability("DeviceIPAddress", "1.1.1.1");

		success = false;
		caps.removeCapability("DeviceUsername");
		try {
			rokuDriver = new RokuDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("DeviceUsername");
		}
		Assert.assertTrue(success);
		caps.addCapability("DeviceUsername", "username");

		success = false;
		caps.removeCapability("DevicePassword");
		try {
			rokuDriver = new RokuDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("DevicePassword");
		}
		Assert.assertTrue(success);
		caps.addCapability("DevicePassword", "password");

		success = false;
		caps.removeCapability("AppPackage");
		caps.removeCapability("App");
		try {
			rokuDriver = new RokuDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("AppPackage");
		}
		Assert.assertTrue(success);

	}

	@Test(groups = { "Roku" })
	public void sessionNotStartedDeviceNotReachableTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("DeviceIPAddress", "1.1.1.1");

		boolean success = false;
		try {
			rokuDriver = new RokuDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			success = e.getMessage().contains("Is the device online");
		}
		Assert.assertTrue(success);

	}

	@Test(groups = { "Roku" })
	public void sessionNotStartedInstallFailedTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", "no package here");

		boolean success = false;
		try {
			rokuDriver = new RokuDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			success = true;
			Assert.assertTrue(e.getMessage().contains("Failed to decode app package file"));
		}
		Assert.assertTrue(success);

		caps.addCapability("AppPackage", "http://www.youwontfindanyzippackagehereatthislocationipromise.com");
		success = false;
		try {
			rokuDriver = new RokuDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			success = true;
			Assert.assertTrue(e.getMessage().contains("The provided app package is not valid"));
		}
		Assert.assertTrue(success);

		caps.addCapability("AppPackage", INVALID_PACKAGE_ZIP.getAbsolutePath());
		success = false;
		try {
			rokuDriver = new RokuDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e);
			success = true;
			Assert.assertTrue(e.getMessage().contains("Failed to install/launch Roku app!"));
		}
		Assert.assertTrue(success);

	}

	@Test(groups = { "Roku" })
	public void sendKeysTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));
		rokuDriver.remote().pressButton(RokuButton.OPTION);
		rokuDriver.finder().findElement(By.Text("LEGAL"));
		rokuDriver.remote().pressButton(RokuButton.SELECT);
		rokuDriver.finder().findElement(By.Text("123"));
		rokuDriver.remote().sendKeys("searching for something");
		rokuDriver.finder().findElement(By.Text("searching for something"));

	}

	@Test(groups = { "Roku" })
	public void sessionCommandAfterDriverQuitTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.stop();

		boolean success = false;
		try {
			rokuDriver.finder().findElement(By.Text("session not active"));
		} catch (NoSuchElementException e) {
			success = true;
			System.out.print(e.getMessage());
			Assert.assertTrue(e.getMessage().contains("No active session found"));
		}
		Assert.assertTrue(success);

	}

	@Test(groups = { "Roku" })
	public void invalidLocatorTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		boolean success = false;
		try {
			rokuDriver.finder().findElement(By.Text(""));
		} catch (NoSuchElementException e) {
			success = true;
			Assert.assertTrue(e.getMessage().contains("Your locator is not valid!"));
		}
		Assert.assertTrue(success);

		success = false;
		try {
			rokuDriver.finder().findElement(By.Image(""));
		} catch (NoSuchElementException e) {
			success = true;
			Assert.assertTrue(e.getMessage().contains("Your locator is not valid!"));
		}
		Assert.assertTrue(success);

		success = false;
		try {
			rokuDriver.finder().findElement(By.Image("/this/image/does/not/exist.png"));
		} catch (NoSuchElementException e) {
			success = true;
			Assert.assertTrue(
					e.getMessage().contains("Is your image element a proper url or path to a valid image file?"));
		}
		Assert.assertTrue(success);

		success = false;
		try {
			rokuDriver.finder().findElement(By.Image("http://urltoanimagethatdoesnotexist.png"));
		} catch (NoSuchElementException e) {
			success = true;
			Assert.assertTrue(
					e.getMessage().contains("Is your image element a proper url or path to a valid image file?"));
		}
		Assert.assertTrue(success);

	}

	@Test(groups = { "Roku" })
	public void elementPollIntervalTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);
		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.options().setElementPollInterval(1000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));

	}

	@Test(groups = { "Roku" })
	public void googleVisionInvalidCapsTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.removeCapability("GoogleCredentials");

		boolean success = false;
		try {
			rokuDriver = new RokuDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			success = true;
			Assert.assertTrue(e.getMessage().contains("The GoogleCredentials capability must be "
					+ "provided if using the Google Vision OCR module"));
		}
		Assert.assertTrue(success);

		caps.addCapability("GoogleCredentials", "/invalid/path/to/file");
		success = false;
		try {
			rokuDriver = new RokuDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = true;
			Assert.assertTrue(e.getMessage().contains("Failed to decode Google API credentials!"));
		}
		Assert.assertTrue(success);

	}

	@Test(groups = { "Roku" })
	public void findMultipleTextElementsTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));
		
		rokuDriver.remote().pressButton(RokuButton.OPTION);
		rokuDriver.finder().findElement(By.Text("SETTINGS"));
		rokuDriver.remote().pressButton(RokuButton.RIGHT_ARROW);
		rokuDriver.remote().pressButton(RokuButton.SELECT);

		List<Element> elements = rokuDriver.finder().findElements(By.Text("Sign in"));
		Assert.assertEquals(2, elements.size());

		Element elementMatch1 = elements.get(0);
		System.out.println(elementMatch1.toString());
		Assert.assertEquals("Sign in", elementMatch1.getText());
		Point elementMatch1Location = elementMatch1.getLocation();
		Dimension elementMatch1Size = elementMatch1.getSize();
		Assert.assertTrue(elementMatch1Location.x > 360 && elementMatch1Location.x < 370);
		Assert.assertTrue(elementMatch1Location.y > 470 && elementMatch1Location.y < 480);
		Assert.assertTrue(elementMatch1Size.width > 35 && elementMatch1Size.width < 45);
		Assert.assertTrue(elementMatch1Size.height > 10 && elementMatch1Size.height < 20);

		Element elementMatch2 = elements.get(1);
		System.out.println(elementMatch2.toString());
		Assert.assertEquals("SIGN IN", elementMatch2.getText());
		Point elementMatch2Location = elementMatch2.getLocation();
		Dimension elementMatch2Size = elementMatch2.getSize();
		Assert.assertTrue(elementMatch2Location.x > 200 && elementMatch2Location.x < 220);
		Assert.assertTrue(elementMatch2Location.y > 540 && elementMatch2Location.y < 560);
		Assert.assertTrue(elementMatch2Size.width > 35 && elementMatch2Size.width < 45);
		Assert.assertTrue(elementMatch2Size.height > 10 && elementMatch2Size.height < 20);

	}

	@Test(groups = { "Roku" })
	public void isElementPresentTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));
		
		rokuDriver.remote().pressButton(RokuButton.OPTION);
		boolean elementPresent = rokuDriver.finder().findElements(By.Text("SETTINGS")).size() > 0;
		Assert.assertTrue(elementPresent);

		elementPresent = rokuDriver.finder().findElements(By.Text("no such element")).size() > 0;
		Assert.assertFalse(elementPresent);

	}

	@Test(groups = { "Roku" })
	public void findMultipleImageElementsTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));
		rokuDriver.remote().pressButton(RokuButton.DOWN_ARROW);
		rokuDriver.remote().pressButton(RokuButton.DOWN_ARROW);

		rokuDriver.finder().findElement(By.Text("SEARCH"));
		List<Element> elements = rokuDriver.finder().findElements(By.Image(ROKU_IMAGES_DIR.getAbsolutePath() + File.separator + "multimatch.png"));
		Assert.assertTrue(elements.size() > 0);

	}

	@Test(groups = { "Roku" })
	public void findElementWithHDMICaptureTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");

		caps.addCapability("VideoCaptureInput", "USB Capture HDMI");
		caps.addCapability("AudioCaptureInput", "USB Capture HDMI");

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(10000);
		Element element = rokuDriver.finder().findElement(By.Text("SHOWS"));
		System.out.println(element);

	}

	@Test(groups = { "Roku" })
	public void getActiveElementTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", DEBUG_URL_ZIP);

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(10000);
		if (rokuDriver.finder().findElements(RokuBy.Text("ACCEPT & CONTINUE")).size() > 0) {
			rokuDriver.remote().pressButton(RokuButton.SELECT);
		}
		
		Element elementByText = rokuDriver.finder().findElement(RokuBy.Text("VIEW MORE"));
		System.out.println(elementByText);
		
		Element activeElement = rokuDriver.screen().getActiveElement();
		System.out.println(activeElement);
		Assert.assertEquals(0, activeElement.getX());
		Assert.assertEquals(0, activeElement.getY());
		Assert.assertEquals(312, activeElement.getWidth());
		Assert.assertEquals(682, activeElement.getHeight());
		
	}

	@Test(groups = { "Roku" })
	public void isWebDriverElementPresent() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", DEBUG_URL_ZIP);

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(10000);
		if (rokuDriver.finder().findElements(RokuBy.Text("ACCEPT & CONTINUE")).size() > 0) {
			rokuDriver.remote().pressButton(RokuButton.SELECT);
		}
		
		List<Element> elements = rokuDriver.finder().findElements(RokuBy.Tag("Label"));
		Assert.assertTrue(elements.size() > 0);

		rokuDriver.options().setElementTimeout(1000);
		elements = rokuDriver.finder().findElements(RokuBy.Text("no such element"));
		Assert.assertTrue(elements.size() == 0);
		
	}

	@Test(groups = { "Roku" })
	public void mediaPlayerInfoTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", DEBUG_URL_ZIP);

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(10000);
		if (rokuDriver.finder().findElements(RokuBy.Text("ACCEPT & CONTINUE")).size() > 0) {
			rokuDriver.remote().pressButton(RokuButton.SELECT);
		}

		rokuDriver.finder().findElement(RokuBy.Text("VIEW MORE"));

		RokuMediaPlayerInfo mediaPlayerInfo = rokuDriver.info().getMediaPlayerInfo();
		Assert.assertFalse(mediaPlayerInfo.isError());
		Assert.assertFalse(mediaPlayerInfo.isLive());
		Assert.assertEquals(mediaPlayerInfo.getState(), "none");

	}

	@Test(groups = { "Roku" })
	public void sessionStatusTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", DEBUG_URL_ZIP);

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(10000);
		if (rokuDriver.finder().findElements(RokuBy.Text("ACCEPT & CONTINUE")).size() > 0) {
			rokuDriver.remote().pressButton(RokuButton.SELECT);
		}

		rokuDriver.finder().findElement(RokuBy.Text("VIEW MORE"));

		for (SessionStatus status : SessionStatus.values()) {
			rokuDriver.options().setSessionStatus(status);
			SessionStatus s = rokuDriver.options().getSessionStatus();
			System.out.println(s);
			Assert.assertEquals(status, s);
		}

	}

	@Test(groups = { "Roku" })
	public void appInfoTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", DEBUG_URL_ZIP);

		rokuDriver = new RokuDriver(SERVER_URL, caps);

		JSONObject installedAppsJSON = rokuDriver.info().getInstalledApps();
		Assert.assertTrue(installedAppsJSON.toJSONString().contains("USA Network"));

		JSONObject launchedAppJSON = rokuDriver.info().getActiveApp();
		Assert.assertTrue(launchedAppJSON.toJSONString().contains("dev"));

	}

	@Test(groups = { "Roku" })
	public void remoteControlDelayTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));

		long startTime = System.currentTimeMillis();
		rokuDriver.remote().pressButton(RokuButton.DOWN_ARROW);
		rokuDriver.remote().pressButton(RokuButton.RIGHT_ARROW);
		long endTime = System.currentTimeMillis();
		long diffInSeconds = (endTime - startTime) / 1000;
		System.out.println(diffInSeconds);
		Assert.assertTrue(diffInSeconds <= 1);

		rokuDriver.options().setRemoteInteractDelay(2000);
		startTime = System.currentTimeMillis();
		rokuDriver.remote().pressButton(RokuButton.RIGHT_ARROW);
		rokuDriver.remote().pressButton(RokuButton.RIGHT_ARROW);
		endTime = System.currentTimeMillis();
		diffInSeconds = (endTime - startTime) / 1000;
		System.out.println(diffInSeconds);
		Assert.assertTrue(diffInSeconds >= 4 && diffInSeconds <= 6);

		rokuDriver.options().setRemoteInteractDelay(0);
		startTime = System.currentTimeMillis();
		rokuDriver.remote().pressButton(RokuButton.RIGHT_ARROW);
		rokuDriver.remote().pressButton(RokuButton.RIGHT_ARROW);
		endTime = System.currentTimeMillis();
		diffInSeconds = (endTime - startTime) / 1000;
		System.out.println(diffInSeconds);
		Assert.assertTrue(diffInSeconds <= 1);

	}

	@Test(groups = { "Roku" })
	public void getDebugLogs() {

		DeviceCapabilities caps = setBaseCapabilities();
		rokuDriver = new RokuDriver(SERVER_URL, caps);

		rokuDriver.options().setElementTimeout(5000);
		rokuDriver.finder().findElement(By.Text("SHOWS"));

		String logContent = rokuDriver.info().getDebugLogs();
		Assert.assertTrue(logContent.toLowerCase().contains("bet"));

	}

}
