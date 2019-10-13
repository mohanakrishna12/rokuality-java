# Rokuality Java - End to End Automation for Roku!

The Rokuality Server allows you to distribute Roku end to end tests across multiple Roku devices on your network. (Playstation and XBox coming soon!) Finally! an open source end to end test automation platform for Roku! No proprietary closed 3rd party software, and no complicated capture hardware required! Just put your Roku in dev mode, clone and start the [Rokuality Server](https://github.com/rokuality/rokuality-server), and start writing tests!

### Getting started: Get the Server
Clone/Download and start the [Rokuality Server](https://github.com/rokuality/rokuality-server) which acts as a lightweight web server proxy for your test traffic. The server does all the 'heavy lifting' on the backend.

### Getting started: Enabling Developer Mode on your Roku
[Enabling developer mode](https://blog.roku.com/developer/developer-setup-guide) on your Roku device is very straight forward. Keep track of your device username and password as created during the basic walkthrough as you'll need them to pass to your DeviceCapabilities at driver startup. Once you've enabled developer mode on your device you should be able to hit the device console page at http://yourrokudeviceip

### Getting started: Add the bindings to your Project
To use Rokuality in your tests or application, add the `rokuality-java` dependency to your pom:
```xml
    <dependency>
        <groupId>com.rokuality</groupId>
        <artifactId>rokuality-java</artifactId>
        <version>1.0.0</version>
        <scope>test</scope>
    </dependency>
```

### The Basics:
Because there's no native based test support built into Roku, the Rokuality bindings operate via Image Based Object Recognition and OCR techniques to identify 'elements' on the device screen and return them to your test scripts as Objects for verification and interaction. The project is modeled after the Selenium/Appium structure so if you've used those toolsets for browsers/mobile devices previously - this framework will look and feel very comfortable to you. See the [example tests](https://github.com/rokuality/rokuality-java/blob/master/src/test/java/com/rokuality/test/tests/RokuTests.java) for a full list of samples.

#### Declare a driver to connect to the server:
```java
    RokuDriver rokuDriver = new RokuDriver("http://yourserverurl:yourrunningserverport", DeviceCapabilities);
```
This will take care of installing/launching your sideloadable Roku zip package, ensure the device is available and ready for test, and start a dedicated session on your device as indicated via your DeviceCapabilities object. See [Device Capabilities](#device-capabilities-explained) for an explanation of what capabilities are available for your driver startup.

#### Finding elements:
There are two primary ways of finding elements on your device:

1) TEXT
```java
    rokuDriver.finder().findElement(By.Text("text to find on screen"));
```
In this example, the Rokuality server will capture the image from your device screen, and then perform an evaluation against the found text within that image and match it against your locator. If your locator text is NOT found, then a NoSuchElementException will be thrown. Tesseract is the default OCR engine used to perform textual evaluations. But you can optionally indicate that you want to use GoogleVision's OCR engine which requires you have a valid VisionUI service account setup with Google. See [Using Google Vision](#using-google-vision-ocr) for those details. But for most use cases, using the default tesseract engine is enough.

2) IMAGE - local image snippet file
```java
    rokuDriver.finder().findElement(By.Image("/path/to/an/image/snippet.png"));
```
In this example, you can provide the path to an image snippet that you expect to be contained within your device screen. The server will then ship this image snippet to itself, capture the device screen, and evaluate if it exists on the device.

OR

3) IMAGE - url to to an image snippet file
```java
    rokuDriver.finder().findElement(By.Image("http://urltoyourimagesnippet.png"));
```
In this example, you can provide a url to your locator image snippet and the server will download that image and evaluate it against the device screen. Useful for those more dynamic testing situations where you may want to query your application feeds to get the dynamic app images for evaluation, or if you want to keep your image based locators in a remote repository.

#### Elements as objects:
A found element can be stored to an object and additional details about it can be retrieved:
```java
    Element element = rokuDriver.finder().findElement(By.Text("Hello World!"));
    System.out.println(element.getLocation());
    System.out.println(element.getHeight());
    System.out.println(element.getWidth());
    System.out.println(element.getConfidence());
    System.out.println(element.getText());
```
The element details include the elements location and size details as found on the device, the text contained within the match (relevent if an image snippet locator was provided), and the confidence score of the match with higher values indicating the confidence in your find:
```xml
    java.awt.Point[x=368,y=319]
    388.0
    658.0
    91.33713
    Hello World!
```

#### Sending remote control commands to the device:
To send remote button presses to the device you can do the following:
```java
    rokuDriver.remote().pressButton(RokuButton.BACK);
    rokuDriver.remote().pressButton(RokuButton.SELECT);
    rokuDriver.remote().pressButton(RokuButton.OPTION);
    // etc
    // etc
```
All [remote command](https://github.com/rokuality/rokuality-java/blob/master/src/main/java/com/rokuality/core/enums/RokuButton.java) are available. Also you can send literal characters to the device if you need to interact with a Roku search selector:
```java
    rokuDriver.remote().sendKeys("typing out hello world on a search screen");
```

#### Getting screen artifacts:
Various methods exist for getting screen artifacts such as the screen image, sub screen image, screen recording during test, and screen text:

```java
    // get the screen size
    rokuDriver.screen().getSize()

    // get the screen image
    rokuDriver.screen().getImage();

    // get the screen sub image from starting x,y with width/height
    rokuDriver.screen().getImage(1, 1, 300, 300);

    // get the screen recording of the test session from start to now
    // note that the screen recordings are created by stitching the collected device screenshots
    // together and video quality won't be the best
    rokuDriver.screen().getRecording();
```

#### Getting screen text:
Screen text of the device is returned as a collection of ScreenText objects as found on the screen. Each ScreenText item will be an object containing details about the found device text such as location, height, and width of the word as found on the device screen:
```java
    List<ScreenText> allScreenText = rokuDriver.screen().getText();
    for (ScreenText screenText : allScreenText) {
        System.out.println(screenText.getText());
        System.out.println(screenText.getWidth());
        System.out.println(screenText.getHeight());
        System.out.println(screenText.getLocation());
    }
```
Alternatively you can get the entire device screen as a full string via `rokuDriver.screen().getTextAsString();`

#### Device Capabilities explained:
Various capabilities and values can be provided and passed to your RokuDriver instance at startup. Some of them are required and others are optional. The following are the minimum capabilities **required** to start a driver session.
```java
    // Declare a new DeviceCapability object
    DeviceCapabilities capabilities = new DeviceCapabilities();

    // Indicates we want a Roku test session
    capabilities.addCapability("Platform", "Roku");

    // App location (path or url to a sideloadable zip)
    capabilities.addCapability("AppPackage", "/path/or/url/to/your/sideloadable/app.zip");

    // Your Roku device ip address
    capabilities.addCapability("DeviceIPAddress", "yourdeviceipaddress");

    // Your Roku device username and password as created during your device developer setup.
    capabilities.addCapability("DeviceUsername", "deviceusername"); // most likely 'rokudev' unless changed
    capabilities.addCapability("DevicePassword", "devicepassword");
    
    // Pass the capabilities and start the test
    rokuDriver = new RokuDriver("http://urltoyourrunningserver:port, capabilities);
```

| Capability  | Description | Required Or Optional | Notes |
| ------------- | ------------- | ------------- | ------------- |
| Platform | Indicates the target platform for the tests. Currently only 'Roku' is supported but XBox and Playstation coming soon.  | Required | String |
| AppPackage | The sideloadable zip to be installed. Must be a valid file path to a .zip OR a valid url to a .zip.  | Required | String |
| DeviceIPAddress | The ip address of your Roku  | Required | Your roku device MUST be reachable from the machine running the Rokuality server. |
| DeviceUsername | The Roku dev console username created when you enabled developer mode on your Roku device  | Required | String |
| DevicePassword | The Roku dev console password created when you enabled developer mode on your Roku device   | Required | String |
| ImageMatchSimilarity | An optional image match similarity default used during Image locator evaluations. A lower value will allow for greater tolerance of image disimilarities between the image locator and the screen, BUT will also increase the possibility of a false positive.  | Optional | Double. Defaults to .90 |
| ScreenSizeOverride | An optional 'WIDTHxHEIGHT' cap that all screen image captures will be resized to prior to match evaluation. Useful if you want to enforce test consistence across multiple device types and multiple developer machines or ci environments.  | Optional | String - I.e. a value of '1800x1200' will ensure that all image captures are resized to those specs before the locator evaluation happens no matter what the actual device screen size is.  |
| OCRType | An optional OCR type - Options are 'Tesseract' OR 'GoogleVision'. If not provided the default 'Tesseract' OCR engine will be used. In most cases Tesseract is more than enough but if you find that your textual evalutions are lacking reliability you can provide 'GoogleVision' as a more powerful alternative. BUT if the capability is set you MUST have a valid Google Vision account setup and provide the 'GoogleCredentials' capability with a valid file path to the oath2 .json file with valid credentials for the Google Vision service.  | Optional | String - Defaults to 'Tesseract' 
| GoogleCredentials | The path to a valid .json Google Auth key service file. | Optional but Required if the 'OCRType' capability is set to 'GoogleVision' | The .json service key must exist on the machine triggering the tests. See [Using Google Vision](#using-google-vision-ocr) for additional details.  |

#### Element Timeouts and Polling:
There are two main options when it comes to element timeouts and polling

Timeouts - By default the element timeout is set to 0 milliseconds, meaning if the driver fails to find an element immediately, it will throw a NoSuchElement exception. But a better practice is to set a implicit wait timeout so the driver will poll for a duration, trying to find the element before it fails and throws the NoSuchElementException:

```java
    // will fail immediately
    rokuDriver.finder().findElement(By.Text("no such text"));
```
vs
```java
    // will fail after 5 seconds
    rokuDriver.options().setElementTimeout(5000); // timeout in milliseconds
    rokuDriver.finder().findElement(By.Text("no such text"));
```
It is generally recommended to set respective timeouts to reduce test flake, but setting the values too high can increase test duration.

Additionally you can set the interval of how often the element search polling will happen. In this example, the same timeout is applied but the element polling will happen every second. If the polling interval is ommited the default is 250 milliseconds.
```java
    // will fail after 5 seconds polling every second
    rokuDriver.options().setElementTimeout(5000); // timeout in milliseconds
    rokuDriver.options().setElementPollInterval(1000); // poll interval in seconds
    rokuDriver.finder().findElement(By.Text("no such text"));
```

#### Using Google Vision OCR:
As mentioned previously, Tesseract is the default OCR engine used when you provide a text based locator. And the Rokuality server ships the relevant trained data files so the more you use it during test, the better it will get at finding the provided text based locators. But if you find that's not as reliable as needed for your testing purposes you can use Google Vision as an alternative provided you have a valid [Google Vision](https://cloud.google.com/vision/docs/before-you-begin) account setup. You must also set the path to your .json service file containing your service key in your DeviceCapabilities prior to driver start.

```java
    DeviceCapabilities capabilities = new DeviceCapabilities();
    capabilities.addCapability("OCRType", "GoogleVision");
    capabilities.addCapability("GoogleCredentials", "/path/to/your/vision/authkey.json");
```

#### Failing to find elements? :
Image Based Locators `By.Image("pathorurltoyoourimagesnippet.png")`
1. Make sure your locator image snippet is in a valid image format. Most tests use .png format so for best results please use locators of this type.
2. Make sure that your image snippets are in good quality and you are doing an apples to apples comparison of the image snippet you wish to find within the screen image. Some image snipping tools are better than others, so if capturing static image snippets for later locator use be wary of the tools you're using. Alternatively, you can get a subscreen section from the device during test and save it as a static locator for later use.
`File locatorToUseLater = rokuDriver.screen().getImage(50, 50, 25, 12);`
3. You can optionally set the "ImageMatchSimilarity" DeviceCapability at driver startup which will set a tolerance for image comparisons. Lower values will mean a greater likelihood of getting a match, but too low of a value will introduce a false positive.
`capabilities.addCapability("ImageMatchSimilarity", .85);`

Text Based Locators `By.Text("text to search for")`
1. Check that your string isn't too complicated, i.e. a locator of `By.Text("hello world")` is much more likely to be found than a locator of `By.Text("he!!O W@rLd!#!")`. Also, single world locators are better than multiple world locators but we continue to work on the server back end to improve the reliability.
2. As mentioned previously, Tesseract is the default OCR engine but if you're finding that the results aren't as reliable as you'd like, consider using [Google Vision](#using-google-vision-ocr) as an alternative.
3. You can access the entire decoded device screen text by `rokuDriver.screen().getTextAsString()`. If your locator is present in the string but not found during test, please log a bug on the [issues](https://github.com/rokuality/rokuality-java/issues) page and we'll investigate.
4. By default the OCR evaluations happen against the entire device screen but sometimes it's better to narrow the scope of the find to a smaller region of the screen to get better results. This can be done with `finder().findElement(By.Text("text to find"), 1, 1, 500, 500)` which will limit the scope of the find to that subset of the screen and likely return better results.

#### Server timeouts and orphaned sessions:
At the end of every driver session, you should close the driver and cleanup all session data by calling the stop method:
```java
    // stop the driver and clean up all resources
    rokuDriver.stop();
```
But if you don't a safety exists to eventually clean up those assets. The server session will listen for new commands and will timeout if no commands for the session have been received for a specified duration. The default command timeout is set to 60 seconds - meaning if a session is started and no commands are sent to it for 60 seconds, then the session will automatically be terminated and released. You can increase/decrease this time by setting the 'commandtimeout' option when you launch the server. See the [Server Command Options](https://github.com/rokuality/rokuality-server) section of the server for details.