
# Bridgewell SDK for Android

[Links to the full documentation](https://github.com/bridgewell/Android-SDK/blob/main/README.md)

**Version: 1.0**

<u><b>Note</b></u>: All integration examples are written in Kotlin. If you’re using Java please convert them to corresponding Java code

## Table of Contents

- [Requirements](#requirements)
- [General Setup](#general-setup)
    - [1. SDK installation](#1-sdk-installation)
        - [a. Using Maven Central](#a-using-maven-central)
        - [b. Using the .aar file](#b-using-the-aar-file)
    - [2. SDK initialization](#2-sdk-initialization)
        - [a. Set BW Server](#a-set-bw-server)
        - [b. Initialize SDK](#b-initialize-sdk)
        - [c. Geolocation sharing](#c-share-geo-location)
    - [3. Update Your Android Manifest](#3-update-your-android-manifest)
- [SDK Usage](#sdk-usage)
    - [1. WebView API usage](#1-web-view-api)
    - [2. Banner ads](#2-banner-ads)
        - [a. Display normal banner](#a-display-normal-banner)
        - [b. Sticky banner](#b-sticky-banner)
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

<a id="1-sdk-installation"></a>
### 1. SDK installation
<a id="a-using-maven-central"></a>
#### a. Using Maven Central
1.  Add the dependency:

```gradle
dependencies {
	implementation("com.bridgewell:bwmobile:1.3.0")
}
```

2. Sync your project.


<a id="b-using-the-aar-file"></a>
#### b. Using the .aar file

1. Place the `.aar` file into your project's `app/libs` folder: `/app/libs/bwmobile.aar`
2. Add this code to the dependencies block in your application's `build.gradle` file:

```gradle
dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar", "*.aar"])
    implementation files('libs/bwmobile.aar')
}
```

3. Sync your project.


<a id="2-sdk-initialization"></a>
### 2. SDK initialization

<a id="a-set-bw-server"></a>
#### a. Set the BW Server

After obtaining a BW server (e.g., the Rubicon Server), add its details to BW Mobile:

```kotlin
BWMobile.getInstance().setAccountId("YOUR_ACCOUNT_ID")
BWMobile.getInstance().setHostServer("YOUR_HOST_SERVER")
```


<a id="b-initialize-sdk"></a>
#### b. Initialize SDK

Once you have an account ID and host server, initialize the BW SDK as follows:

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
<a id="#c-share-geo-location"></a>
#### c. Geolocation sharing

- If this flag is set to `true` and the app collects the user's geographical location data, BW Mobile will send this data to the BW server.
- If this flag is `false` or the app does not collect location data, BW Mobile will not include any geographical location information in the call to the BW server.

    ```kotlin
    BWMobile.getInstance().setShareGeoLocation(true)
    val isShareGeoLocation = BWMobile.getInstance().getShareGeoLocation()
    ```


------------
<a id="3-update-your-android-manifest"></a>
### 3. Update your Android manifest


1. Before starting, integrate the SDK by updating your `AndroidManifest.xml` with the following permissions:

```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
```

*   `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION` allow the device to send user location data for ad targeting, which can increase revenue by making impressions more valuable to buyers.
*   `READ_PHONE_STATE` allows the Web View API to collect device data. 

2. Configure your app by following the instructions for the [Google Mobile Ads SDK](https://developers.google.com/admob/android/quick-start#import_the_mobile_ads_sdk).

<a id="sdk-usage"></a>
# SDK usage

<a id="1-web-view-api"></a>
## 1. Web View API

This section explains how to obtain device information and make it available in the WebView interface.

### Prerequisites

- Declare the permissions mentioned in [section 3](#3-update-your-android-manifest)

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
### Result:

<img src="https://github.com/user-attachments/assets/ece6e693-57f7-4c1b-b9f8-89a641b9f4bb" width="336" height="748" alt="WebView">

<a id="2-banner-ads"></a>
## 2. Banner ads

You can use the BridgeWell SDK to monetize your app with a custom ad server or even without one. Use the `InAppApi` to obtain targeting keywords for use with your custom ad server and display the winning bid without relying on a primary ad server and its SDK.

<a id="a-display-banner"></a>
### a. Display normal banner

##### Sample code:

```kotlin
// Create an instance of InAppApi
val adApi: InAppApi = InAppApi()

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

### Result:

<img src="https://github.com/user-attachments/assets/4a3a9d69-326f-4a41-84ac-b02ddd0cc3a8" width="336" height="748" alt="WebView">

<br>
<u><b>Note</b></u>: 
- If you display the ad in a ViewPager, you need to set `offscreenPageLimit` to ensure stable ad display, or pass the ViewPager into `createDisplayBannerAd`
- You also need pass some necessary parameters to createDisplayBannerAd() function

| Parameter      | Explain | Detail     |
| :---        |    :----   |          :--- |
| context      | Instance of Context        |  -  |
| viewPager      | The view pager contains the ad item |  [Optional]: Skip this parameter if you've already set `offscreenPageLimit` on your `ViewPager` or if your ad is not displayed inside a `ViewPager`.  |
| model   | Contains information to generate the ad | `ConfigId`: an ID of a Stored Impression on the Bw server  <br>  `refreshTimeSeconds`: refresh time for each `fetchDemand` call in second <br> `width`: Width of the ad <br>  `Height`: Height of the ad  |
| viewContainer | The view to which the ad will be added. | - |
| listener | Callback listener (BannerAdListener) | `onAdStartLoad`: Called when the ad starts loading <br> `onAdLoaded`: Called when the ad is loaded successfully <br> `onAdDisplayed`: Called when the ad is displayed <br> `onAdFailed`: Called when the ad fails to load <br> `onAdClicked`: Called when the ad is clicked <br> `onAdClosed`: Called when the ad is closed |

<br>


<a id="b-sticky-banner"></a>
### b. Sticky banner

##### Sample code:

```kotlin
// Create an instance of InAppApi
val adApi: InAppApi = InAppApi()

// perform to fetch and load ad
adApi.createStickyBannerAd(
   context = context,
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

### Result:

<img src="https://github.com/user-attachments/assets/4811afed-e814-49af-a2c6-cd3536a1a9ce" width="336" height="748" alt="WebView">


<br>
<u><b>Note</b></u>:
- You also need pass some necessary parameters to `createStickyBannerAd()` function

| Parameter      | Explain | Detail     |
| :---        |    :----   |          :--- |
| context      | Instance of Context        |  -  |
| model   | Contains information to generate the ad | `ConfigId`: an ID of a Stored Impression on the Bw server  <br>  `refreshTimeSeconds`: refresh time for each `fetchDemand` call in second <br> `width`: Width of the ad <br>  `Height`: Height of the ad  |
| listener | Callback listener (BannerAdListener) | `onAdStartLoad`: Called when the ad starts loading <br> `onAdLoaded`: Called when the ad is loaded successfully <br> `onAdDisplayed`: Called when the ad is displayed <br> `onAdFailed`: Called when the ad fails to load <br> `onAdClicked`: Called when the ad is clicked <br> `onAdClosed`: Called when the ad is closed |

# About

<a id="about"></a>
Copyright 2019 Bridgewell | All Rights Reserved
    
