
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
    - [1. ShareGeoLocation](#1-sharegeolocation)
    - [2. In-App Browsers](#2-in-app-browsers)
- [Ad Setup](#ad-setup)
    - [1. Custom Bidding Integration](#1-custom-bidding-integration)
        - [a. In-app banner ad implementation](#a-display-banner)
        - [b. In-app Sticky Bottom-Right ad implementation](#b-sticky-banner)
        - [c. WebView API usage](#c-web-view-api-usage)
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

Add the dependency:

```gradle
dependencies {
	implementation("com.bridgewell:bwmobile:1.3.0")
}
```

<a id="b-file-aar-alternative"></a>
#### b. File .aar (Alternative)

**Importing the .aar file:**

1. **Copy**: Place the `.aar` file into your project's app/libs folder. /Path/to/your/project/app/libs/bwmobile.aar
2. **Dependency**: Add this to your app's `build.gradle` file:
   In build.gradle file of your application add this code to dependencies

   ```gradle
   dependencies {
   	implementation fileTree(dir: "libs", include: ["*.jar", "*.aar"])
   	implementation files('libs/bwmobile.aar')
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
BWMobile.getInstance().setHostServer("YOUR_HOST_SERVER")
```

Or you can pass other values like ```Host.APPNEXUS``` and If you have opted to host your own BW Server solution you will need to store the url to the server in your app. Make sure that your URL points to the /openrtb2/auction endpoint.


------------

<a id="b-initial-sdk"></a>
#### b. Initialize SDK

After you have an account id and host server. You should initialize BW Sdk like this

```kotlin
BWMobile.getInstance().initialize(
   context,
   object : OnInitializationListener {
       override fun onSuccess(hasWarningPBS: Boolean) {
			// Called when the SDK is initialized successfully
       }

       override fun onFailed(msg: String) {
			// Called if the SDK initialization fails
       }
   }
)

```

------------
<a id="3-update-your-android-manifest"></a>
### 3. Update your Android manifest


<a id="a-declare-permissions"></a>
### Declare some permissions

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
<a id="1-sharegeolocation"></a>
## 1. ShareGeoLocation

If this flag is True AND the app collects the user’s geographical location data, BW Mobile will send the user’s geographical location data to BW Server. If this flag is False OR the app does not collect the user’s geographical location data, BW Mobile will not populate any user geographical location information in the call to BW Server.

```kotlin
BWMobile.getInstance().setShareGeoLocation(true)
val isShareGeoLocation = BWMobile.getInstance().getShareGeoLocation()
```

<a id="2-in-app-browsers"></a>
## 2. In-app Browsers

Obtain device information and make it available in the WebView interface.

### Prerequisites

- Declare the permissions mentioned in [3](#a-declare-permissions)

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
       configId = CONFIG_ID,
       width = 300,
       height = 300,
       refreshTimeSeconds = 300
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


<a id="b-sticky-banner"></a>
### b. Sticky banner

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
                override fun onAdStartLoad(bannerView: BannerView?) {}

                override fun onAdLoaded(bannerView: BannerView?) {}

                override fun onAdDisplayed(bannerView: BannerView?) {}

                override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {}

                override fun onAdClicked(bannerView: BannerView?) {}

                override fun onAdClosed(bannerView: BannerView?) {}
            },
    )
}
```

<br>

You also need pass some necessary parameters to createDisplayBannerAd() function

| Parameter      | Explain | Detail     |
| :---        |    :----   |          :--- |
| context      | Instance of Context        |  -  |
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
    