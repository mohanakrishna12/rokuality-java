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

		double confidence = element.getConfidence();
		Assert.assertTrue(confidence > 70.0);

		Assert.assertTrue(!element.getElementID().isEmpty());
		Assert.assertEquals(element.getSessionID(), playstationDriver.getSessionID());

	}

	@Test(groups = { "Playstation", "Debug" })
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

		playstationDriver.finder().findElement(By.Text("amazon"));

		// get the available remote commands
		String buttonOptions = playstationDriver.remote().getButtonOptions();
		System.out.println(buttonOptions);

		playstationDriver.remote().pressButton("DirectionDown");
		playstationDriver.remote().pressButton("DirectionDown");
		playstationDriver.remote().pressButton("DirectionDown");
		playstationDriver.remote().pressButton("DirectionDown");

		playstationDriver.finder().findElement(By.Text("More Services"));
		
		playstationDriver.remote().pressButton("Circle");
		playstationDriver.finder().findElement(By.Text("amazon"));
	}

	@Test(groups = { "Roku" })
	@Features("Roku")
	public void getScreenTextTesseractTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		playstationDriver = new HDMIDriver(SERVER_URL, caps);

		playstationDriver.options().setElementTimeout(5000);
		playstationDriver.finder().findElement(By.Text("amazon"));

		List<ScreenText> allScreenText = playstationDriver.screen().getText();
		boolean matchFound = false;
		for (ScreenText screenText : allScreenText) {
			if (screenText.getText().equals("amazon")) {
				matchFound = true;

				System.out.println(screenText);
		
				Point textLocation = screenText.getLocation();
				Assert.assertTrue(textLocation.x > 400 && textLocation.x < 420);
				Assert.assertTrue(textLocation.y > 750 && textLocation.y < 790);

				Dimension textSize = screenText.getSize();
				Assert.assertTrue(textSize.width > 190 && textSize.width < 220);
				Assert.assertTrue(textSize.height > 45 && textSize.height < 60);

				Assert.assertTrue(screenText.getWidth() > 190 && screenText.getWidth() < 220);
				Assert.assertTrue(screenText.getHeight() > 45 && screenText.getHeight() < 60);

				double confidence = screenText.getConfidence();
				Assert.assertTrue(confidence > 80);
				break;
			}
		}
		Assert.assertTrue(matchFound);
		
		List<ScreenText> allSubScreenText = playstationDriver.screen().getText(1, 1, 500, 500);
		Assert.assertFalse(allSubScreenText.isEmpty());
		Assert.assertTrue(allScreenText.size() != allSubScreenText.size());

		String subScreenTxt = playstationDriver.screen().getTextAsString(1, 1, 500, 500);
		Assert.assertFalse(subScreenTxt.toLowerCase().contains("amazon"));
		Assert.assertTrue(playstationDriver.screen().getTextAsString().contains("amazon"));

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

}
