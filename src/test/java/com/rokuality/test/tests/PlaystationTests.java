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
import com.rokuality.core.driver.hdmi.HDMIDriver;
import com.rokuality.core.exceptions.NoSuchElementException;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.exceptions.SessionNotStartedException;
import com.rokuality.core.utils.SleepUtils;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;

public class PlaystationTests {

	private static final File PLAYSTATION_IMAGES_DIR = new File(
			String.join(File.separator, System.getProperty("user.dir"), "src", "test", "resources", "images",
					"playstationimages") + File.separator);

	private static final String SERVER_URL = "http://localhost:7777";

	private HDMIDriver playstationDriver = null;

	@BeforeMethod(alwaysRun = true)
	public synchronized void beforeTest() {

	}

	@AfterMethod(alwaysRun = true)
	public void afterTest() {
		if (playstationDriver != null && playstationDriver.getSession() != null) {
			try {
				playstationDriver.stop();
			} catch (ServerFailureException e) {
				// thrown under various conditions of a server teardown such as the session not
				// found during teardown, etc
			}
		}
	}

	private DeviceCapabilities setBaseCapabilities() {

		DeviceCapabilities capabilities = new DeviceCapabilities();

		// Indicates we want a Roku test session
		capabilities.addCapability("Platform", "HDMI");

		// OCR module - Options are 'Tesseract' or 'GoogleVision'
		capabilities.addCapability("OCRType", "Tesseract");

		// TODO - NOT NEEDED FOR HDMI DRIVERS
		capabilities.addCapability("DeviceIPAddress", "192.168.1.38");

		// The ip address of your harmony
		capabilities.addCapability("HomeHubIPAddress", "192.168.1.41");

		// The name of your device as saved in harmony
		capabilities.addCapability("DeviceName", "Playstation4");

		// ~/Rokuality/dependencies/ffmpeg_v4.1 -f avfoundation -list_devices true -i ""
		capabilities.addCapability("VideoCaptureInput", "FHD Webcamera");
		capabilities.addCapability("AudioCaptureInput", "FHD Webcamera");

		// OPTIONAL A base image match similarity tolerance between 0 and 1
		capabilities.addCapability("ImageMatchSimilarity", .89);

		// OPTIONAL A forced image resolution size that all image captures are resized
		// to (width/height).
		// capabilities.addCapability("ScreenSizeOverride", "1280x820");

		// OPTIONAL - enforce ocr case sensitivity. If not provided all words during ocr
		// evaluation are forced to lowercase
		// for better success chances. To enforce case sensitivity set to true; defaults
		// to false;
		capabilities.addCapability("OCRCaseSensitive", true); // TODO - implement in server

		return capabilities;
	}

	@Test(groups = { "Playstation" })
	@Features("Playstation")
	public void findElementFromTextWithTesseractTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "Tesseract");

		playstationDriver = new HDMIDriver(SERVER_URL, caps);

		playstationDriver.options().setElementTimeout(5000);
		Element element = playstationDriver.finder().findElement(By.Text("Playstation store"));
		System.out.println(element);

		Point elementLocation = element.getLocation();
		Assert.assertTrue(elementLocation.x > 640 && elementLocation.x < 650);
		Assert.assertTrue(elementLocation.y > 550 && elementLocation.y < 560);

		Dimension elementSize = element.getSize();
		Assert.assertTrue(elementSize.width > 420 && elementSize.width < 430);
		Assert.assertTrue(elementSize.height > 50 && elementSize.height < 60);

		String elementText = element.getText();
		Assert.assertEquals("PlayStation Store", elementText);

		double confidence = element.getConfidence();
		Assert.assertTrue(confidence > 80.0);

		Assert.assertTrue(!element.getElementID().isEmpty());
		Assert.assertEquals(element.getSessionID(), playstationDriver.getSessionID());

	}

	@Test(groups = { "Playstation" })
	@Features("Playstation")
	public void findElementFromTextWithGoogleVisionTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");

		playstationDriver = new HDMIDriver(SERVER_URL, caps);

		playstationDriver.options().setElementTimeout(5000);
		Element element = playstationDriver.finder().findElement(By.Text("Sign in"));
		System.out.println(element);

		Point elementLocation = element.getLocation();
		Assert.assertTrue(elementLocation.x > 760 && elementLocation.x < 780);
		Assert.assertTrue(elementLocation.y > 790 && elementLocation.y < 800);

		Dimension elementSize = element.getSize();
		Assert.assertTrue(elementSize.width > 75 && elementSize.width < 90);
		Assert.assertTrue(elementSize.height > 25 && elementSize.height < 35);

		String elementText = element.getText();
		Assert.assertEquals("Sign in", elementText);

		Assert.assertTrue(!element.getElementID().isEmpty());
		Assert.assertEquals(element.getSessionID(), playstationDriver.getSessionID());

	}

	@Test(groups = { "Playstation" })
	@Features("Playstation")
	public void findElementFromImageTest() {

		DeviceCapabilities caps = setBaseCapabilities();

		playstationDriver = new HDMIDriver(SERVER_URL, caps);
		playstationDriver.options().setElementTimeout(5000);

		for (By by : Arrays.asList(By.Image(PLAYSTATION_IMAGES_DIR.getAbsolutePath() + File.separator + "playplus.png"),
				By.Image("https://dl.dropboxusercontent.com/s/3bzhadgvuuko94j/playplus.png"))) {

			Element element = playstationDriver.finder().findElement(by);
			System.out.println(element);

			Point elementLocation = element.getLocation();
			Assert.assertTrue(elementLocation.x > 90 && elementLocation.x < 100);
			Assert.assertTrue(elementLocation.y > 70 && elementLocation.y < 90);

			Dimension elementSize = element.getSize();
			Assert.assertTrue(elementSize.width > 50 && elementSize.width < 60);
			Assert.assertTrue(elementSize.height > 45 && elementSize.height < 60);

			double confidence = element.getConfidence();
			Assert.assertTrue(confidence > 0.90);

			Assert.assertTrue(!element.getElementID().isEmpty());
			Assert.assertEquals(element.getSessionID(), playstationDriver.getSessionID());
		}

	}

	@Test(groups = { "Playstation" })
	@Features("Playstation")
	public void findElementInSubScreenTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		playstationDriver = new HDMIDriver(SERVER_URL, caps);
		playstationDriver.options().setElementTimeout(5000);

		By by = By.Image(PLAYSTATION_IMAGES_DIR.getAbsolutePath() + File.separator + "playplus.png");
		Element element = playstationDriver.finder().findElement(by, 1, 1, 300, 300);
		System.out.println(element);

		Exception exception = null;
		try {
			playstationDriver.finder().findElement(by, 800, 800, 100, 100);
		} catch (NoSuchElementException nse) {
			exception = nse;
		}
		Assert.assertNotNull(exception);
	}

	@Test(groups = { "Playstation" })
	@Features("Playstation")
	public void remoteControlTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");
		playstationDriver = new HDMIDriver(SERVER_URL, caps);
		playstationDriver.options().setElementTimeout(15000);

		playstationDriver.finder().findElement(By.Text("Sign in"));

		// get the available remote commands
		String buttonOptions = playstationDriver.remote().getButtonOptions();
		System.out.println(buttonOptions);

		playstationDriver.remote().pressButton("DirectionUp");
		playstationDriver.finder().findElement(By.Text("Notifications"));

		playstationDriver.remote().pressButton("Circle");
		playstationDriver.finder().findElement(By.Text("Playstation Store"));

	}

	@Test(groups = { "Playstation" })
	@Features("Playstation")
	public void getScreenTextTesseractTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		playstationDriver = new HDMIDriver(SERVER_URL, caps);

		playstationDriver.options().setElementTimeout(5000);
		playstationDriver.finder().findElement(By.Text("playstation store"));

		List<ScreenText> allScreenText = playstationDriver.screen().getText();
		boolean matchFound = false;
		for (ScreenText screenText : allScreenText) {
			if (screenText.getText().equals("PlayStation")) {
				matchFound = true;

				System.out.println(screenText);
		
				Point textLocation = screenText.getLocation();
				Assert.assertTrue(textLocation.x > 640 && textLocation.x < 650);
				Assert.assertTrue(textLocation.y > 550 && textLocation.y < 560);

				Dimension textSize = screenText.getSize();
				Assert.assertTrue(textSize.width > 285 && textSize.width < 295);
				Assert.assertTrue(textSize.height > 50 && textSize.height < 60);

				Assert.assertTrue(screenText.getWidth() > 285 && screenText.getWidth() < 295);
				Assert.assertTrue(screenText.getHeight() > 50 && screenText.getHeight() < 60);

				double confidence = screenText.getConfidence();
				Assert.assertTrue(confidence > 85);
				break;
			}
		}
		Assert.assertTrue(matchFound);
		
		List<ScreenText> allSubScreenText = playstationDriver.screen().getText(1, 1, 500, 500);
		Assert.assertFalse(allSubScreenText.isEmpty());
		Assert.assertTrue(allScreenText.size() != allSubScreenText.size());

		String subScreenTxt = playstationDriver.screen().getTextAsString(1, 1, 400, 400);
		Assert.assertFalse(subScreenTxt.toLowerCase().contains("playstation"));
		Assert.assertTrue(playstationDriver.screen().getTextAsString().toLowerCase().contains("playstation"));

	}

	@Test(groups = { "Playstation" })
	@Features("Playstation")
	public void getScreenSizeTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		playstationDriver = new HDMIDriver(SERVER_URL, caps);

		Dimension screenSize = playstationDriver.screen().getSize();
		System.out.println(screenSize);
		Assert.assertTrue(screenSize.getWidth() > 1900 && screenSize.getWidth() < 1930);
		Assert.assertTrue(screenSize.getHeight() > 1000 && screenSize.getHeight() < 1090);

	}

	@Test(groups = { "Playstation" })
	@Features("Playstation")
	public void getScreenArtifactsTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		playstationDriver = new HDMIDriver(SERVER_URL, caps);

		File screenImage = playstationDriver.screen().getImage();
		System.out.println("Screen image saved to: " + screenImage.getAbsolutePath());
		Assert.assertTrue(screenImage.exists() && screenImage.isFile());

		File screenSubImage = playstationDriver.screen().getImage(1, 1, 300, 300);
		System.out.println("Screen sub image saved to: " + screenSubImage.getAbsolutePath());
		Assert.assertTrue(screenSubImage.exists() && screenSubImage.isFile());

		File screenRecording = playstationDriver.screen().getRecording();
		System.out.println("Screen recording saved to: " + screenRecording.getAbsolutePath());
		Assert.assertTrue(screenRecording.exists() && screenRecording.isFile());
		SleepUtils.sleep(2000);
		File screenRecording2 = playstationDriver.screen().getRecording();
		Assert.assertTrue(screenRecording2.exists() && screenRecording2.isFile());
		Assert.assertFalse(screenRecording.equals(screenRecording2));
		
	}

	@Test(groups = { "Playstation" })
	@Features("Playstation")
	public void sessionNotStartedMissingRequiredCapsTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.removeCapability("VideoCaptureInput");

		boolean success = false;
		try {
			playstationDriver = new HDMIDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("VideoCaptureInput");
		}
		Assert.assertTrue(success);
		caps.addCapability("VideoCaptureInput", "someinput");

		success = false;
		caps.removeCapability("AudioCaptureInput");
		try {
			playstationDriver = new HDMIDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("AudioCaptureInput");
		}
		Assert.assertTrue(success);
		caps.addCapability("VideoCaptureInput", "someinput");

		success = false;
		caps.removeCapability("HomeHubIPAddress");
		try {
			playstationDriver = new HDMIDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("HomeHubIPAddress");
		}
		Assert.assertTrue(success);
		caps.addCapability("HomeHubIPAddress", "someipaddress");

		success = false;
		caps.removeCapability("DeviceName");
		try {
			playstationDriver = new HDMIDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("DeviceName");
		}
		Assert.assertTrue(success);
		caps.addCapability("DeviceName", "DeviceName");

	}

	@Test(groups = { "Playstation" })
	@Features("Playstation")
	public void sessionNotStartedDeviceNotReachableTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("VideoCaptureInput", "wronginput");

		boolean success = false;
		try {
			playstationDriver = new HDMIDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("Failed to initiate hdmi driver");
		}
		Assert.assertTrue(success);
		caps.addCapability("VideoCaptureInput", "FHD Webcamera");

		success = false;
		caps.addCapability("HomeHubIPAddress", "1.1.1.1");
		try {
			playstationDriver = new HDMIDriver(SERVER_URL, caps);
		} catch (SessionNotStartedException e) {
			System.out.println(e.getMessage());
			success = e.getMessage().contains("The logitech harmony device");
		}
		Assert.assertTrue(success);

	}

}
