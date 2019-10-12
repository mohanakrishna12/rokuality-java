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
import com.rokuality.core.driver.roku.RokuDeviceInfo;
import com.rokuality.core.driver.roku.RokuDriver;
import com.rokuality.core.driver.xbox.XBoxDriver;
import com.rokuality.core.enums.RokuButton;
import com.rokuality.core.enums.XBoxButton;
import com.rokuality.core.exceptions.NoSuchElementException;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.exceptions.SessionNotStartedException;
import com.rokuality.core.utils.SleepUtils;

import org.testng.annotations.*;

import junit.framework.Assert;
import ru.yandex.qatools.allure.annotations.Features;

public class XBoxTests {

	private static final File ROKU_IMAGES_DIR = new File(String.join(File.separator, System.getProperty("user.dir"),
			"src", "test", "resources", "images", "xboximages") + File.separator);
	private static final File DEMO_APP_PACKAGE = new File(
			ROKU_IMAGES_DIR.getParentFile().getParent() + File.separator + "XBoxDebug.appxbundle");

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

		// Location (path or url to a sideloadable zip)
		//capabilities.addCapability("AppPackage", "https://rokualitypublic.s3.amazonaws.com/RokualityDemoApp.zip");

		// Your Roku device ip address.
		capabilities.addCapability("DeviceIPAddress", "192.168.1.36");

		// OPTIONAL A base image match similarity tolerance between 0 and 1
		capabilities.addCapability("ImageMatchSimilarity", .89);

		// OPTIONAL A forced image resolution size that all image captures are resized
		// to (width/height).
		capabilities.addCapability("ScreenSizeOverride", "1280x820");

		// OPTIONAL ocr module 
		capabilities.addCapability("OCRType", "Tesseract");

		// OPTIONAL - enforce ocr case sensitivity. If not provided all words during ocr
		// evaluation are forced to lowercase
		// for better success chances. To enforce case sensitivity set to true; defaults
		// to false;
		capabilities.addCapability("OCRCaseSensitive", true); // TODO - implement in server

		return capabilities;
	}

	@Test(groups = { "XBox", "Debug" })
	@Features("Roku")
	public void installFromLocalFileTest() {

		DeviceCapabilities caps = setBaseCapabilities();
		caps.addCapability("AppPackage", DEMO_APP_PACKAGE.getAbsolutePath());
		caps.addCapability("App", "MTV");
		caps.addCapability("OCRType", "GoogleVision");
		caps.addCapability("GoogleCredentials", System.getProperty("user.home") + File.separator + "Service.json");
		caps.addCapability("HomeHubIPAddress", "192.168.1.41");
		caps.addCapability("DeviceName", "XboxOne");

		xboxDriver = new XBoxDriver(SERVER_URL, caps);

		xboxDriver.options().setElementTimeout(15000);
		xboxDriver.finder().findElement(By.Text("featured"));

		xboxDriver.remote().pressButton(XBoxButton.DOWN_ARROW);
		SleepUtils.sleep(2000);
		//xboxDriver.remote().pressButton(XBoxButton.UP_ARROW);
		SleepUtils.sleep(2000);
		xboxDriver.remote().pressButton(XBoxButton.SELECT);
		SleepUtils.sleep(4000);
		xboxDriver.remote().pressButton(XBoxButton.BACK);
		SleepUtils.sleep(5000);
		System.out.println(xboxDriver.screen().getRecording());

	}

	
}
