# Rokuality Java - End to End Automation for Roku, XBox, Playstation, Cable SetTop Boxes, and More!

The Rokuality platform allows you to distribute Roku, XBox, PS4, and Cable SetTop Box end to end tests across multiple devices on your network. The project goal is to provide a no cost/low cost open source solution for various video streaming platforms that otherwise don't offer an easily automatable solution! Clone and start the [Rokuality Server](https://github.com/rokuality/rokuality-server), and start writing tests!

### Getting started: Get the Server
Clone/Download and start the [Rokuality Server](https://github.com/rokuality/rokuality-server) which acts as a lightweight web server proxy for your test traffic. The server does all the 'heavy lifting' on the backend.

### Getting started: Add the bindings to your Project
To use Rokuality in your tests or application:

MAVEN:
```xml
    <dependency>
        <groupId>com.rokuality</groupId>
        <artifactId>rokuality-java</artifactId>
        <version>1.2.0</version>
        <scope>test</scope>
    </dependency>
```
GRADLE:
```xml
    implementation 'com.rokuality:rokuality-java:1.2.0'
```

### Getting started: Roku
See the [Getting Started: Roku](https://github.com/rokuality/rokuality-server) section for details about preparing your Roku device for test.

### Getting started: XBox
See the [Getting Started: XBox](https://github.com/rokuality/rokuality-server) section for details about preparing your XBox device for test.

### Getting started: HDMI Connected Devices (Playstation, Cable SetTopBox, AndroidTV, AppleTV, and More)
See the [Getting Started: HDMI Connected Devices (Playstation, Cable SetTopBox, AndroidTV, AppleTV, and More](https://github.com/rokuality/rokuality-server) section for details about preparing your Cable Settop Box, Playstation, AndroidTV, or AppleTV device for test.


### The Basics:
The Rokuality bindings operate via Image Based Object Recognition and OCR techniques to identify 'elements' on the device screen and return them to your test scripts as Objects for verification and interaction. The project is modeled after the Selenium/Appium structure so if you've used those toolsets for browsers/mobile devices previously - this framework will look and feel very comfortable to you. See the [Roku example tests](https://github.com/rokuality/rokuality-java/blob/master/src/test/java/com/rokuality/test/tests/RokuTests.java)  or the [XBox example tests](https://github.com/rokuality/rokuality-java/blob/master/src/test/java/com/rokuality/test/tests/XBoxTests.java) or [HDMI example tests](https://github.com/rokuality/rokuality-java/blob/master/src/test/java/com/rokuality/test/tests/PlaystationTests.java) for a full list of samples.

#### Declare a driver to connect to the server:
```java
    // Roku
    RokuDriver driver = new RokuDriver("http://yourserverurl:yourrunningserverport", DeviceCapabilities);

    // XBox
    XBoxDriver driver = new XBoxDriver("http://yourserverurl:yourrunningserverport", DeviceCapabilities);

    // HDMI device (playstation, cable settop box, androidtv, appletv, etc)
    HDMIDriver driver = new HDMIDriver("http://yourserverurl:yourrunningserverport", DeviceCapabilities);
```
This will take care of installing/launching your device app package (if Roku or XBox), ensure the device is available and ready for test, and start a dedicated session on your device as indicated via your DeviceCapabilities object. See [Device Capabilities](#device-capabilities-explained) for an explanation of what capabilities are available for your driver startup.

#### Finding elements:
There are two primary ways of finding elements on your device:

1) TEXT
```java
    driver.finder().findElement(By.Text("text to find on screen"));
```
In this example, the Rokuality server will capture the image from your device screen, and then perform an evaluation against the found text within that image and match it against your locator. If your locator text is NOT found, then a NoSuchElementException will be thrown. Tesseract is the default OCR engine used to perform textual evaluations. But you can optionally indicate that you want to use GoogleVision's OCR engine which requires you have a valid VisionUI service account setup with Google. See [Using Google Vision](#using-google-vision-ocr) for those details. But for most use cases, using the default tesseract engine is enough.

2) IMAGE - local image snippet file
```java
    driver.finder().findElement(By.Image("/path/to/an/image/snippet.png"));
```
In this example, you can provide the path to an image snippet that you expect to be contained within your device screen. The server will then ship this image snippet to itself, capture the device screen, and evaluate if it exists on the device.

OR

3) IMAGE - url to to an image snippet file
```java
    driver.finder().findElement(By.Image("http://urltoyourimagesnippet.png"));
```
In this example, you can provide a url to your locator image snippet and the server will download that image and evaluate it against the device screen. Useful for those more dynamic testing situations where you may want to query your application feeds to get the dynamic app images for evaluation, or if you want to keep your image based locators in a remote repository.

#### Elements as objects:
A found element can be stored to an object and additional details about it can be retrieved:
```java
    Element element = driver.finder().findElement(By.Text("Hello World!"));
    System.out.println(element.getLocation());
    System.out.println(element.getHeight());
    System.out.println(element.getWidth());
    System.out.println(element.getConfidence());
    System.out.println(element.getText());
```
The element details include the elements location and size details as found on the device, the text contained within the match (relevent if an image snippet locator was provided), and the confidence score of the match with higher values indicating the confidence in your find:
```xml
    java.awt.Point[x=368,y=319]
    45
    19
    91.33713
    Hello World!
```

#### Sending remote control commands to the device - Roku and XBox:
To send remote button presses to a Roku or XBox you can do the following:
```java
    // roku
    rokuDriver.remote().pressButton(RokuButton.SELECT);
    
    // xbox
    xboxDriver.remote().pressButton(XBoxButton.A);
```
All remote commands are available. See [roku remote command](https://github.com/rokuality/rokuality-java/blob/master/src/main/java/com/rokuality/core/enums/RokuButton.java) or [xbox remote command](https://github.com/rokuality/rokuality-java/blob/master/src/main/java/com/rokuality/core/enums/XBoxButton.java) for all available remote buttons. Also you can send literal characters to the device if you need to interact with a Roku search selector (coming soon for XBox as well):
```java
    rokuDriver.remote().sendKeys("typing out hello world on a search screen");
```

#### Sending remote control commands to the device - HDMI Devices (Playstation, Cable SetTop, AndroidTV, AppleTV, and more):
To send remote button presses to the HDMI/IR device you can do the following:
```java
    // get a list of available remote commands for your device
    String buttonOptions = driver.remote().getButtonOptions();
    System.out.println(buttonOptions);
    
    // send the desired button press to the device
    driver.remote().pressButton("DirectionUp");
    driver.remote().pressButton("Guide");
    driver.remote().pressButton("Select");
```

#### Getting screen artifacts:
Various methods exist for getting screen artifacts such as the screen image, sub screen image, screen recording during test, and screen text:

```java
    // get the screen size
    driver.screen().getSize()

    // get the screen image
    driver.screen().getImage();

    // get the screen sub image from starting x,y with width/height
    driver.screen().getImage(1, 1, 300, 300);

    // get the screen recording of the test session from start to now
    // note recording video quality will vary by the type of device under test
    driver.screen().getRecording();
```

#### Getting screen text:
Screen text of the device is returned as a collection of ScreenText objects as found on the screen. Each ScreenText item will be an object containing details about the found device text such as location, height, and width of the word as found on the device screen:
```java
    List<ScreenText> allScreenText = driver.screen().getText();
    for (ScreenText screenText : allScreenText) {
        System.out.println(screenText.getText());
        System.out.println(screenText.getWidth());
        System.out.println(screenText.getHeight());
        System.out.println(screenText.getLocation());
    }
```
Alternatively you can get the entire device screen as a full string via `driver.screen().getTextAsString();`

#### Device Capabilities explained:
Various capabilities and values can be provided and passed to your driver instance at startup. Some of them are required and others are optional. The following are the minimum capabilities **required** to start a driver session.

#### Roku
```java
    // Declare a new DeviceCapability object
    DeviceCapabilities capabilities = new DeviceCapabilities();

    // Indicates we want a Roku test session
    capabilities.addCapability("Platform", "Roku");

    // OCR module - Options are 'Tesseract' or 'GoogleVision'
    capabilities.addCapability("OCRType", "Tesseract");
        
    // App location (path or url to a sideloadable zip)
    capabilities.addCapability("AppPackage", "/path/or/url/to/your/sideloadable/app.zip");

    // Your Roku device ip address
    capabilities.addCapability("DeviceIPAddress", "yourdeviceipaddress");

    // Your Roku device username and password as created during your device developer setup.
    capabilities.addCapability("DeviceUsername", "deviceusername"); // most likely 'rokudev' unless changed
    capabilities.addCapability("DevicePassword", "devicepassword");
    
    // Pass the capabilities and start the test
    RokuDriver rokuDriver = new RokuDriver("http://urltoyourrunningserver:port, capabilities);
```

#### Xbox
```java
    // Declare a new DeviceCapability object
    DeviceCapabilities capabilities = new DeviceCapabilities();

    // Indicates we want a XBox test session
    capabilities.addCapability("Platform", "XBox");

    // OCR module - Options are 'Tesseract' or 'GoogleVision'
    capabilities.addCapability("OCRType", "Tesseract");
        
    // App location (path or url to a valid appxbundle app)
    // NOTE - if ommitted the server will assume the package is already installed
    // and will attempt to launch it
    capabilities.addCapability("AppPackage", "/path/or/url/to/your/package.appxbundle");

    // The app id - will be the friendly app name of your appxbundle
    capabilities.addCapability("App", "appidofyourpackage");

    // Your XBox device ip address
    capabilities.addCapability("DeviceIPAddress", "yourdeviceipaddress");

    // Your Harmony hub ip address.
    capabilities.addCapability("HomeHubIPAddress", "harmonyipaddress");

    // Your device name as saved in your harmony app on your harmony hib
    capabilities.addCapability("DeviceName", "nameofdeviceinharmony");
    
    // Pass the capabilities and start the test
    XBoxDriver xboxDriver = new XBoxDriver("http://urltoyourrunningserver:port, capabilities);
```

#### HDMI Devices (Playstation, Cable SetTop, AndroidTV, AppleTV, and more)
```java
    // Declare a new DeviceCapability object
    DeviceCapabilities capabilities = new DeviceCapabilities();

    // Indicates we want an HDMI device test session
    capabilities.addCapability("Platform", "HDMI");

    // OCR module - Options are 'Tesseract' or 'GoogleVision'
    capabilities.addCapability("OCRType", "Tesseract");
        
    // Your Harmony hub ip address.
    capabilities.addCapability("HomeHubIPAddress", "harmonyipaddress");

    // Your device name as saved in your harmony app on your harmony hib
    capabilities.addCapability("DeviceName", "nameofdeviceinharmony");

    // The video input and audio input names of your attached hdmi capture card. They can be found by running
    // the following commands:
    // MAC: ~/Rokuality/dependencies/ffmpeg_v4.1 -f avfoundation -list_devices true -i ""
    // WINDOWS: ~\Rokuality\dependencies\ffmpeg_win_v4.1\bin\ffmpeg.exe -list_devices true -f dshow -i dummy
    capabilities.addCapability("VideoCaptureInput", "video input name");
    capabilities.addCapability("AudioCaptureInput", "audio input name");
    
    // Pass the capabilities and start the test
    HDMIDriver hdmiDriver = new HDMIDriver("http://urltoyourrunningserver:port, capabilities);
```

| Capability  | Description | Required Or Optional | Notes |
| ------------- | ------------- | ------------- | ------------- |
| Platform | Indicates the target platform for the tests.  | Required | String - Options are 'Roku, 'XBox', or 'HDMI' |
| AppPackage | The sideloadable zip to be installed (Roku), or the .appxbundle (XBox). Must be a valid file path OR a valid url.  | Required for Roku and XBox - IF the 'App' capability is not provided. Ignored for HDMI devices | String |
| App | The friendly id of your app for Roku and XBox. For Roku this cap is optional. If you provide this cap and ommit the 'AppPackage' cap then the device will attempt to launch an already sideloaded .zip. For XBox this cap is always required and MUST be the app id of your installed .appxbundle - if you ommit the 'AppPackage' cap then the device will attempt to launch an already installed appxbundle matching this id. |Roku = Optional. XBox = Required. HDMI = Ignored | String |
| DeviceIPAddress | The ip address of your Roku or XBox  | Required | String - Your device MUST be reachable from the machine running the Rokuality server. |
| DeviceUsername | The dev console username created when you enabled developer mode on your device  | Required - Roku Only | String |
| DevicePassword | The dev console password created when you enabled developer mode on your device   | Required - Roku Only | String |
| ImageMatchSimilarity | An optional image match similarity default used during Image locator evaluations. A lower value will allow for greater tolerance of image disimilarities between the image locator and the screen, BUT will also increase the possibility of a false positive.  | Optional | Double. Defaults to .90 |
| ScreenSizeOverride | An optional 'WIDTHxHEIGHT' cap that all screen image captures will be resized to prior to match evaluation. Useful if you want to enforce test consistence across multiple device types and multiple developer machines or ci environments.  | Optional | String - I.e. a value of '1800x1200' will ensure that all image captures are resized to those specs before the locator evaluation happens no matter what the actual device screen size is.  |
| OCRType | The OCR type - Options are 'Tesseract' OR 'GoogleVision'. In most cases Tesseract is more than enough but if you find that your textual evalutions are lacking reliability you can provide 'GoogleVision' as a more powerful alternative. BUT if the capability is set to 'GoogleVision' you MUST have a valid Google Vision account setup and provide the 'GoogleCredentials' capability with a valid file path to the oath2 .json file with valid credentials for the Google Vision service.  | Required | String 
| GoogleCredentials | The path to a valid .json Google Auth key service file. | Optional but Required if the 'OCRType' capability is set to 'GoogleVision' | The .json service key must exist on the machine triggering the tests. See [Using Google Vision](#using-google-vision-ocr) for additional details.  |
| HomeHubIPAddress | The ip address of your logitech harmony hub. | Required for those devices that indicate it during their setup requirement, i.e. XBox or HDMI devices(Playstation, Cable SetTop, AndroidTV, AppleTV, etc) | String - See the [why harmony](https://github.com/rokuality/rokuality-server) and [configuring your harmony](https://github.com/rokuality/rokuality-server) sections of the server page for details. |
| DeviceName | The name of your device as saved in your Harmony hub i.e. 'MyXBoxOne'. | Required for those devices that indicate it during their setup requirement, i.e. XBox | String |
| VideoCaptureInput | The name of your video card capture video input if running an HDMI connected test. Will vary by the type of hdmi capture card. | Required for HDMI device types (Playstation, Cable SetTop Box, AndroidTV, AppleTV, etc. Ignored for Roku or XBox | Can be found by running a terminal command. For MAC: `~/Rokuality/dependencies/ffmpeg_v4.1 -f avfoundation -list_devices true -i ""` and for Windows: `~\Rokuality\dependencies\ffmpeg_win_v4.1\bin\ffmpeg.exe -list_devices true -f dshow -i dummy` |
| AudioCaptureInput | The name of your video card capture video input if running an HDMI connected test. Will vary by the type of hdmi capture card. | Required for HDMI device types (Playstation, Cable SetTop Box, AndroidTV, AppleTV, etc. Ignored for Roku or XBox | Can be found by running a terminal command. For MAC: `~/Rokuality/dependencies/ffmpeg_v4.1 -f avfoundation -list_devices true -i ""` and for Windows: `~\Rokuality\dependencies\ffmpeg_win_v4.1\bin\ffmpeg.exe -list_devices true -f dshow -i dummy` |

#### Element Timeouts and Polling:
There are two main options when it comes to element timeouts and polling

Timeouts - By default the element timeout is set to 0 milliseconds, meaning if the driver fails to find an element immediately, it will throw a NoSuchElement exception. But a better practice is to set a implicit wait timeout so the driver will poll for a duration, trying to find the element before it fails and throws the NoSuchElementException:

```java
    // will fail immediately
    driver.finder().findElement(By.Text("no such text"));
```
vs
```java
    // will fail after 5 seconds
    driver.options().setElementTimeout(5000); // timeout in milliseconds
    driver.finder().findElement(By.Text("no such text"));
```
It is generally recommended to set respective timeouts to reduce test flake, but setting the values too high can increase test duration.

Additionally you can set the interval of how often the element search polling will happen. In this example, the same timeout is applied but the element polling will happen every second. If the polling interval is ommited the default is 250 milliseconds.
```java
    // will fail after 5 seconds polling every second
    driver.options().setElementTimeout(5000); // timeout in milliseconds
    driver.options().setElementPollInterval(1000); // poll interval in seconds
    driver.finder().findElement(By.Text("no such text"));
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
`File locatorToUseLater = driver.screen().getImage(50, 50, 25, 12);`
3. You can optionally set the "ImageMatchSimilarity" DeviceCapability at driver startup which will set a tolerance for image comparisons. Lower values will mean a greater likelihood of getting a match, but too low of a value will introduce a false positive.
`capabilities.addCapability("ImageMatchSimilarity", .85);`

Text Based Locators `By.Text("text to search for")`
1. Check that your string isn't too complicated, i.e. a locator of `By.Text("hello world")` is much more likely to be found than a locator of `By.Text("he!!O W@rLd!#!")`. Also, single world locators are better than multiple world locators but we continue to work on the server back end to improve the reliability.
2. As mentioned previously, Tesseract is the default OCR engine but if you're finding that the results aren't as reliable as you'd like, consider using [Google Vision](#using-google-vision-ocr) as an alternative.
3. You can access the entire decoded device screen text by `driver.screen().getTextAsString()`. If your locator is present in the string but not found during test, please log a bug on the [issues](https://github.com/rokuality/rokuality-java/issues) page and we'll investigate.
4. By default the OCR evaluations happen against the entire device screen but sometimes it's better to narrow the scope of the find to a smaller region of the screen to get better results. This can be done with `finder().findElement(By.Text("text to find"), 1, 1, 500, 500)` which will limit the scope of the find to that subset of the screen and likely return better results.

#### Server timeouts and orphaned sessions:
At the end of every driver session, you should close the driver and cleanup all session data by calling the stop method:
```java
    // stop the driver and clean up all resources
    rokuDriver.stop();
```
But if you don't a safety exists to eventually clean up those assets. The server session will listen for new commands and will timeout if no commands for the session have been received for a specified duration. The default command timeout is set to 60 seconds - meaning if a session is started and no commands are sent to it for 60 seconds, then the session will automatically be terminated and released. You can increase/decrease this time by setting the 'commandtimeout' option when you launch the server. See the [Server Command Options](https://github.com/rokuality/rokuality-server) section of the server for details.