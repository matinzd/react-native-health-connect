<div align="center">
  <a href="https://developer.android.com/guide/health-and-fitness/health-connect">
    <img src="https://developer.android.com/static/guide/health-and-fitness/health-connect/images/health_connect_logo_192pxnew.png"><br/>
  </a>
  <h1 align="center">React Native Health Connect</h1>
</div>

Health Connect is an Android API and platform. It unifies data from multiple devices and apps into an ecosystem. For Android developers, it provides a single interface for reading and writing a user’s health and fitness data. For Android users, it offers a place for control over which apps have read and/or write access to different types of data. Health Connect also provides on-device storage.
https://developer.android.com/guide/health-and-fitness/health-connect

## Installation

1. Install react-native-health-connect by running:   
`yarn add react-native-health-connect`

Since this module is Android-only, you do not need to run `pod install`.

## Tips
1. Keep in mind that this library is **Android only**.
1. Health Connect is now currently on alpha version. Check [here](https://developer.android.com/jetpack/androidx/releases/health-connect).

## How does Health Connect work?

Health Connect makes it easy for client apps and Android developers to integrate with the Health Connect API. The following diagram shows the basic integration process between the client app and the Health Connect API via the SDK layer and IPC (Inter-Process Communication):

<center>
  <img src="https://developer.android.com/static/guide/health-and-fitness/health-connect/images/healthconnectimage1.jpg">
</center>

Read more [here](https://developer.android.com/guide/health-and-fitness/health-connect/platform-overview/architecture).

## License

MIT
