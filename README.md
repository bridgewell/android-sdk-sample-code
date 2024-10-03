
# Bridgewell SDK for Android

[Links to the full documentation](https://github.com/bridgewell/Android-SDK/blob/main/README.md)

**Version: 1.0**

<u><b>Note</b></u>: All integration examples are written in Kotlin. If you’re using Java please convert them to corresponding Java code

## Table of Contents

- [Requirements](#requirements)
- [General Setup](#general-setup)
    - [1. SDK installation](#1-sdk-integration)
        - [a. Maven Central (Recommended)](#a-maven-central-recommended)
        - [b. File .aar (Alternative)](#b-file-aar-alternative)
    - [2. SDK initialization](#2-setting-up-the-sdk)
        - [a. Set BW Server](#a-set-bw-server)
        - [b. Initialize SDK](#b-initialize-sdk)
    - [3. Update Your Android Manifest](#3-update-your-android-manifest)
        - [a. Declare Permissions](#a-declare-permissions)
- [SDK Configuration](#sdk-configuration)
    - [4. Account ID](#4-account-id)
    - [5. Host](#5-host)
    - [6. Timeout](#6-timeout)
    - [7. Creative Factory Timeout](#7-creative-factory-timeout)
    - [9. In-App Browsers](#9-in-app-browsers)
- [Ad Setup](#ad-setup)
    - [1. Custom Bidding Integration](#1-custom-bidding-integration)
        - [a. In-app banner ad implementation](#a-display-banner)
        - [b. In-app Sticky Bottom-Right ad implementation](#b-display-video)
        - [c. WebView API usage](#c-interstitial-banner)
- [About](#about)


<a id="requirements"></a>
## Requirements


- Android 8.0 (SDK version 26) or newer
- Mobile phone with Google Play Services
- Android Studio 2023.3.1 or newer
- Gradle 8.2 or newer

<br>

<a id="general-setup"></a>
# General setup

<a id="1-sdk-integration"></a>
### 1. SDK Integration

<a id="a-maven-central-recommended"></a>
#### a. Maven Central (Recommended)

Manually:

- In your project’s settings.gradle file, change `DISTRIBUTION_TYPE_MAVEN_CENTRAL` to `true`
- Add these corresponding variables to `gradle.properties`

```
    mavenCentralUsername=example_user // Maven central generated userID
    mavenCentralPassword=example_password // Maven central generated passphrase
    signing.keyId=ABC12345 // Last 8 digits of your gpg public key
    signing.password=123456 // password for secret gpg key
    signing.secretKeyRingFile=/Users/folder/secret.gpg // path to gpg key file
```

- In build.gradle.kts, set `RELEASE_TAG_NAME` to your preferred version string.
- Comment out these variables as they are used in CI.

```
val ORG_GRADLE_PROJECT_mavenCentralUsername = System.getenv("MAVEN_USER_NAME")
val ORG_GRADLE_PROJECT_mavenCentralPassword = System.getenv("MAVEN_PASSWORD")
val ORG_GRADLE_PROJECT_signingInMemoryKey = System.getenv("MY_API_KEY")
val ORG_GRADLE_PROJECT_signingInMemoryKeyId = System.getenv("OSS_SIGNING_KEY_ID")
val ORG_GRADLE_PROJECT_signingInMemoryKeyPassword = System.getenv("OSS_SIGNING_PASSWORD")
```

- Then, run the following command:

```
./gradlew publishAndReleaseToMavenCentral --no-configuration-cache
```

The SDK will be built and published automatically.

------------

<a id="b-local-maven"></a>
#### b. Local Maven

**1. Build**: Build the library to generate an AAR file

- In your project’s settings.gradle file, change `DISTRIBUTION_TYPE_MAVEN_CENTRAL` to `false`
- Then, run the following command:

```
./gradlew buildAllLibraries
```

**2. Publish:** Publish the AAR file to your local Maven repository

```
./gradlew publishAllLibraries
```

You can copy the maven-local folder to the root folder of your other project(s).

**3. Update settings.gradle:** : Add the following to the `dependencyResolutionManagement` section of the project's `settings.gradle` file:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
        maven { url "https://jitpack.io" }
        maven {
            url uri('maven-local')
        }
    }
}
```

------------

<a id="b-file-aar-alternative"></a>
#### b. File .aar (Alternative)


1. **Download**: Go to the Actions tab on this repository.
2. **Choose Commit**: Select the commit you want to download from.
3. **Artifacts**: Go to the Artifacts section.
4. **Download**: Download the `.aar` file.

**Importing the .aar File:**

1. **Copy**: Place the `.aar` file into your project's app/libs folder. /Path/to/your/project/app/libs/bwmobile.aar
2. **Dependency**: Add this to your app's `build.gradle` file:
   In build.gradle file of your application add this code to dependencies

   ```gradle
   dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar", "*.aar"])
    implementation files('libs/bwmobile.aar')
    //if you setup maven local or publish maven
    implementation ("com.bridgewell.bwmobile:bwmobile:1.0.0@aar")
   }
   ```

3. **Sync**: Sync your project with Gradle.

------------

<a id="2-setting-up-the-sdk"></a>
### 2. Setting Up the SDK

<a id="a-set-bw-server"></a>
#### a. Set BW Server

Once you have a Bw server, you will add them to BW mobile. For example, if you’re using the Rubicon Server.

```kotlin
BWMobile.getInstance().setAccountId("YOUR_ACCOUNT_ID") 
BWMobile.getInstance().setHostServer(HostServer.RUBICON)
BWMobile.getInstance().setLoggable(true) // Enable logging (optional)
```

Or you can pass other values like ```Host.APPNEXUS``` and If you have opted to host your own Bw Server solution you will need to store the url to the server in your app. Make sure that your URL points to the /openrtb2/auction endpoint.

setLoggable(true) set show log in library and send log to server prebid

```kotlin
BWMobile.getInstance().setHostServer(HostServer.Custom("YOUR_CUSTOM_HOST_SERVER"))
```

------------

<a id="b-initial-sdk"></a>
#### b. Initialize SDK

After you have an account id and host server. You should initialize BW Sdk like this

```kotlin
BWMobile.getInstance().initialize(
   context,
   object : OnInitializationListener {


       override fun onSuccess(hasWarningPBS: Boolean) {   
       }


       override fun onFailed(msg: String) {
       }
   }
)

```

During the initialization, SDK creates internal classes and performs the health check request to the /status endpoint. If you use a custom host you should provide a custom status endpoint as well:

```kotlin
BWMobile.getInstance().setEndPoint("YOUR_END_POINT")
```

quest to the /status endpoint. If you use a custom host you should provide a custom status endpoint

------------
<a id="3-update-your-android-manifest"></a>
### 3. Update your Android manifest


<a id="a-declare-permissions"></a>
### a. Declare some permissions
#a-declare-permissions


Before you start, you need to integrate the SDK by updating your Android manifest.

```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
```

``` ACCESS_COARSE_LOCATION ``` and ``` ACCESS_FINE_LOCATION ``` will automatically allow the device to send user location for targeting, which can help increase revenue by increasing the value of impressions to buyers.


<a id="sdk-configuration"></a>
# SDK Configuration

<a id="4-account-id"></a>
## 4. Account ID

String containing the BW Server account ID.

```kotlin
BWMobile.getInstance().setAccountId("YOUR_ACCOUNT_ID")
val accountId = BWMobile.getInstance().getAccountId()
```

<a id="5-host"></a>
## 5. Host

Object containing configuration for your Bw Server host with which the Bw SDK will communicate. Choose from the system-defined Bw Server hosts or define your own custom Bw Server host

```kotlin
BWMobile.getInstance().setHostServer(host)
```

`host` should be `HostServer.RUBICON`, `HostServer.RUBICON` or `HostServer.Custom("YOUR_CUSTOM_HOST_SERVER")`

<a id="6-timeout"></a>
## 6. Timeout

The Bw timeout set in milliseconds, will return control to the ad server SDK to fetch an ad once the expiration period is achieved. Because Bw SDK solicits bids from Bw Server in one payload, setting Bw timeout too low can stymie all demand resulting in a potential negative revenue impact.

```kotlin
BWMobile.getInstance().setTimeoutMilliseconds(30_000)
val timeout = BWMobile.getInstance().getTimeoutMilliseconds()
```

<a id="7-creative-factory-timeout"></a>
## 7. Creative Factory Timeout

Indicates how long each creative has to load before it is considered a failure.

```kotlin
BWMobile.getInstance().setCreativeFactoryTimeout(8_000)
val creativeTimeout = BWMobile.getInstance().getCreativeFactoryTimeout()
```

The creativeFactoryTimeoutPreRenderContent controls the timeout for video and interstitial ads. The creativeFactoryTimeout is used for HTML banner ads.

<a id="8-sharegeolocation"></a>
## 8. ShareGeoLocation

If this flag is True AND the app collects the user’s geographical location data, BW Mobile will send the user’s geographical location data to BW Server. If this flag is False OR the app does not collect the user’s geographical location data, BW Mobile will not populate any user geographical location information in the call to BW Server.

```kotlin
BWMobile.getInstance().setShareGeoLocation(true)
val isShareGeoLocation = BWMobile.getInstance().getShareGeoLocation()
```

<a id="9-in-app-browsers"></a>
## 9. In-app Browsers

Obtain device information and make it available in the WebView interface.

### Prerequisites

- Declare the permissions mentioned in [3.a](#a-declare-permissions)

### Register the web view

```kotlin
BWMobile.getInstance().setShareGeoLocation(true)
BWMobile.getInstance().registerWebView(webView)
```

This should be done as early as possible, such as in the onCreate() method of your MainActivity

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
   super.onCreate(savedInstanceState)

   // Enable geographical location data
   BWMobile.getInstance().setShareGeoLocation(true)

   // Register the web view.
   BWMobile.getInstance().registerWebView(webView)
   
   adWrapperView.addView(webView)
   webView.loadUrl(URL)
}
```

<br><br>

<a id="ad-setup"></a>
# Ad setup

<a id="1-custom-bidding-integration"></a>
## 1. Custom Bidding Integration


You can use BW SDK to monetize your app with a custom ad server or even without it. Use the `InAppApi` to obtain the targeting keywords for following usage with the custom ad server and display the winning bid without the primary ad server and its SDK.

<a id="a-display-banner"></a>
### a. Display banner

Sample

```kotlin
// Create an instance of InAppApi
private var adApi: InAppApi = InAppApi()

// perform to fetch and load ad
adApi.createDisplayBannerAd(
   context = context,
   viewPager = yourViewPager, // optional
   model = DisplayBannerModel(
       configId = "prebid-demo-mraid-expand-1-part",
       width = 300,
       height = 250,
       refreshTimeSeconds = 35_000
   ),
   viewContainer = viewContainer,
   listener = object: BannerAdListener {
       override fun onAdStartLoad(bannerView: BannerView?) {}


       override fun onAdLoaded(bannerView: BannerView?) {}


       override fun onAdDisplayed(bannerView: BannerView?) {}


       override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {}


       override fun onAdClicked(bannerView: BannerView?) {}


       override fun onAdClosed(bannerView: BannerView?) {}


   }
)
```

<br>
<u><b>Note</b></u>: If you display the ad in a ViewPager, you need to set `offscreenPageLimit` to ensure stable ad display, or pass the ViewPager into `createDisplayBannerAd`
<br>

You also need pass some necessary parameters to createDisplayBannerAd() function

| Parameter      | Explain | Detail     |
| :---        |    :----   |          :--- |
| context      | Instance of Context        |  -  |
| viewPager      | The view pager contains the ad item |  [Optional]: Skip this parameter if you've already set the `offscreenPageLimit` on your ViewPager or if your ad is not displayed inside a ViewPager.  |
| model   | Include necessary informations to generate the ad | `ConfigId`: an ID of a Stored Impression on the Bw server  <br>  `refreshTimeSeconds`: refresh time for each fetchDemand call in milisecond <br> `width`: width of the ad <br>  `Height`: height of the ad  |
| viewContainer | which view will be add the ad into | - |
| listener | register a callback as interface BannerAdListener to listener | `onAdStartLoad` <br> `onAdLoaded` <br> `onAdDisplayed` <br> `onAdFailed` <br> `onAdClicked` <br> `onAdClosed` |

<br>

<a id="b-display-video"></a>
### b. Display video


Sample:

```kotlin
// Create an instance of InAppApi
private var inAppApi: InAppApi = InAppApi()

inAppApi.createDisplayBannerAd(
    this,
    model = DisplayVideoModel(
        configId = CONFIG_ID,
        width = WIDTH,
        height = HEIGHT,
        refreshTimeSeconds = refreshTimeSeconds
    ),
    viewContainer = adWrapperView,
    listener = object: BannerAdListener {
        override fun onAdStartLoad(bannerView: BannerView?) {}

        override fun onAdLoaded(bannerView: BannerView?) {}

        override fun onAdDisplayed(bannerView: BannerView?) {}

        override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {}

        override fun onAdClicked(bannerView: BannerView?) {}

        override fun onAdClosed(bannerView: BannerView?) {}

    }
)


```

| Parameter      | Explain | Detail     |
| :---           | :----   | :---       |
| context        | Instance of Context | - |
| model        | object DisplayVideoModel, include necessary informations to generate the ad | `ConfigId`: an ID of a Stored Impression on the Bw server  <br>  `refreshTimeSeconds`: refresh time for each fetchDemand call in milisecond <br> `width`: width of the ad <br>  `Height`: height of the ad   |
| viewContainer        | which view will be add the ad into | - |
| listener        | register a callback as interface BannerAdListener to listener | `onAdStartLoad` <br> `onAdLoaded` <br> `onAdDisplayed` <br> `onAdFailed` <br> `onAdClicked` <br> `onAdClosed` |

<a id="c-interstitial-banner"></a>
### c. Interstitial banner


#### Sample

```kotlin
// Create an instance of InAppApi
private var adApi: InAppApi = InAppApi()


// perform to fetch and load ad
adApi.createInterstitialBannerAd(
   this,
   model = InterstitialBannerModel(
       configId = "prebid-demo-display-interstitial-320-480",
   ),
   listener = object: InterstitialAdListener {
       override fun onAdLoaded(interstitialAdUnit: InterstitialAdUnit?) {}


       override fun onAdDisplayed(interstitialAdUnit: InterstitialAdUnit?) {}


       override fun onAdFailed(interstitialAdUnit: InterstitialAdUnit?, e: AdException?) {}


       override fun onAdClicked(interstitialAdUnit: InterstitialAdUnit?) {}


       override fun onAdClosed(interstitialAdUnit: InterstitialAdUnit?) {}
   }
)
```

| Parameter      | Explain | Detail     |
| :---        |    :----   |         :--- |
| context      | Instance of Context        |  -  |
| model   | Include necessary informations to generate the ad | `ConfigId`: an ID of a Stored Impression on the Bw server  <br>  `refreshTimeSeconds`: refresh time for each fetchDemand call in milisecond <br> `width`: width of the ad <br>  `Height`: height of the ad  |
| viewContainer | which view will be add the ad into | - |
| listener | register a callback as interface BannerAdListener to listener | - |

<a id="d-interstitial-video"></a>
### d. Interstital video

#### Sample

```kotlin
// Create an instance of InAppApi
private var adApi: InAppApi = InAppApi()

inAppApi.createInterstitialVideoAd(
    context = this,
    model = InterstitialBannerModel(
        configId = CONFIG_ID
    ),
    listener = object: InterstitialAdListener {
        override fun onAdLoaded(interstitialAdUnit: InterstitialAdUnit?) {}

        override fun onAdDisplayed(interstitialAdUnit: InterstitialAdUnit?) {}

        override fun onAdFailed(interstitialAdUnit: InterstitialAdUnit?, e: AdException?) {}

        override fun onAdClicked(interstitialAdUnit: InterstitialAdUnit?) {}

        override fun onAdClosed(interstitialAdUnit: InterstitialAdUnit?) {}
    }
)
```

| Parameter      | Explain | Detail     |
| :---        |    :----   |         :--- |
| context      | Instance of Context        |  -  |
| model   | object InterstitialBannerModel, include necessary informations to generate the ad | `ConfigId`: an ID of a Stored Impression on the Bw server  <br>  `minWidthPerc`: [optional] width of the ad <br>  `minHeightPerc`: [optional] height of the ad  |
| listener | register a callback as interface InterstitialAdListener to listener | - |

<a id="e-native"></a>
### e. Native

#### Sample

```kotlin
// Create an instance of InAppApi
private var adApi: InAppApi = InAppApi()

// Create native ad
inAppApi.createNativeAd(
    model =
        InAppNativeModel(
            configId = CONFIG_ID,
            configModel = getConfigNativeModel(),
        ),
    onInflateNativeAd = {
        inflateView(it)
    },
)
```

<a id="f-sticky-banner"></a>
### f. Sticky banner

#### Sample

```kotlin
// Create an instance of InAppApi
private var adApi: InAppApi = InAppApi()


// perform to fetch and load ad
private fun createStickyAd() {
    inAppApi?.createStickyBannerAd(
            this,
            // Set the banner to bottom end of the screen
            position = BWStickyBannerPosition.BOTTOM_END,
            model =
            DisplayBannerModel(
                configId = CONFIG_ID,
                width = 300,
                height = 250,
                refreshTimeSeconds = refreshTimeSeconds,
            ),
            listener =
            object : BannerAdListener {
                override fun onAdStartLoad(bannerView: BannerView?) {
                    showToast("onAdStartLoad")
                }

                override fun onAdLoaded(bannerView: BannerView?) {
                    showToast("onAdLoaded")
                }

                override fun onAdDisplayed(bannerView: BannerView?) {
                    showToast("onAdDisplayed")
                }

                override fun onAdFailed(
                    bannerView: BannerView?,
                    exception: AdException?,
                ) {
                    showToast("onAdFailed ${exception?.message}")
                    Timber.d("onAdFailed ${exception?.message}")
                }

                override fun onAdClicked(bannerView: BannerView?) {
                    showToast("onAdClicked")
                }

                override fun onAdClosed(bannerView: BannerView?) {
                    showToast("onAdClosed")
                }
            },
    )
}
```

<br>

You also need pass some necessary parameters to createDisplayBannerAd() function

| Parameter      | Explain | Detail     |
| :---        |    :----   |          :--- |
| context      | Instance of Context        |  -  |
| position      | Set the position of the sticky banner ad        |  BWStickyBannerPosition.CENTER<br>BWStickyBannerPosition.BOTTOM_END  |
| model   | Include necessary informations to generate the ad | `ConfigId`: an ID of a Stored Impression on the Bw server  <br>  `refreshTimeSeconds`: refresh time for each fetchDemand call in milisecond <br> `width`: width of the ad <br>  `Height`: height of the ad  |
| viewContainer | which view will be add the ad into | - |
| listener | register a callback as interface BannerAdListener to listener | `onAdStartLoad` <br> `onAdLoaded` <br> `onAdDisplayed` <br> `onAdFailed` <br> `onAdClicked` <br> `onAdClosed` |

#### Params detail

| Parameter      | Explain | Detail     |
| :---        |    :----   |         :--- |
| model      | object `InAppNativeModel` include ad information       |  `configId`: an ID of a Stored Impression on the Bw server<br> `configModel`: An object declares config information about the ad   |
| onInflateNativeAd | callback function to use inflate view when the ad is already available | - |

<br>

You also can find the sample to declare config model in the following code:

```kotlin
private fun getConfigNativeModel(): InAppNativeConfigModel {
    val config =
        InAppNativeConfigModel(
            contextType = NativeAdUnit.CONTEXT_TYPE.SOCIAL_CENTRIC,
            placementType = NativeAdUnit.PLACEMENTTYPE.CONTENT_FEED,
            contextSubType = NativeAdUnit.CONTEXTSUBTYPE.GENERAL_SOCIAL,
            eventTracker = NativeEventTracker.EVENT_TYPE.IMPRESSION,
            trackerMethods =
                arrayListOf(
                    NativeEventTracker.EVENT_TRACKING_METHOD.IMAGE,
                    NativeEventTracker.EVENT_TRACKING_METHOD.JS,
                ),
            titleAsset =
                NativeTitleAsset().apply {
                    setLength(90)
                    isRequired = true
                },
            iconAsset =
                NativeImageAsset(20, 20, 20, 20).apply {
                    imageType = NativeImageAsset.IMAGE_TYPE.ICON
                    isRequired = true
                },
            imageAsset =
                NativeImageAsset(200, 200, 200, 200).apply {
                    imageType = NativeImageAsset.IMAGE_TYPE.MAIN
                    isRequired = true
                },
            dataAsset =
                NativeDataAsset().apply {
                    len = 90
                    dataType = NativeDataAsset.DATA_TYPE.SPONSORED
                    isRequired = true
                },
            bodyAsset =
                NativeDataAsset().apply {
                    dataType = NativeDataAsset.DATA_TYPE.DESC
                    isRequired = true
                },
            ctaAsset =
                NativeDataAsset().apply {
                    dataType = NativeDataAsset.DATA_TYPE.CTATEXT
                    isRequired = true
                },
        )
    return config
}
```

<br>
Then the ad already available, let to inflate into your view

```kotlin
private fun inflateView(ad: PrebidNativeAd) {
    val nativeContainer = LinearLayout(this)
    nativeContainer.orientation = LinearLayout.VERTICAL
    val iconAndTitle = LinearLayout(this)
    iconAndTitle.orientation = LinearLayout.HORIZONTAL

    val icon = ImageView(this)
    icon.layoutParams = LinearLayout.LayoutParams(160, 160)
    ImageUtils.download(ad.iconUrl, icon)
    iconAndTitle.addView(icon)

    val title = TextView(this)
    title.textSize = 20f
    title.text = ad.title
    iconAndTitle.addView(title)
    nativeContainer.addView(iconAndTitle)

    val image = ImageView(this)
    image.layoutParams =
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    ImageUtils.download(ad.imageUrl, image)
    nativeContainer.addView(image)

    val description = TextView(this)
    description.textSize = 18f
    description.text = ad.description
    nativeContainer.addView(description)

    val cta = Button(this)
    cta.text = ad.callToAction
    nativeContainer.addView(cta)

    adWrapperView.addView(nativeContainer)

    ad.registerView(
        adWrapperView,
        listOf(icon, title, image, description, cta),
        object : PrebidNativeAdEventListener {
            override fun onAdClicked() {}

            override fun onAdImpression() {}

            override fun onAdExpired() {}
        },
    )
}
```

<u>Note</u>: For more detail of the configuration of native impls, please check this documentation: <https://www.iab.com/wp-content/uploads/2018/03/OpenRTB-Native-Ads-Specification-Final-1.2.pdf>


# About

<a id="about"></a>
Copyright 2019 Bridgewell | All Rights Reserved
