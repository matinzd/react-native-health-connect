---
sidebar_position: 1
title: Introduction
---

<div align="center">
  <img src="https://developer.android.com/static/guide/health-and-fitness/health-connect/images/health_connect_logo_192pxnew.png" />
  <div align="center">
    <h1>React Native Health Connect</h1>
  </div>
  <div align="center">
    <a href="https://www.npmjs.com/package/react-native-health-connect">
      <img src="https://img.shields.io/npm/v/react-native-health-connect.svg?style=for-the-badge&color=4284F3" />
    </a>
    <span> </span>
    <a href="https://opensource.org/licenses/MIT">
      <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge" />
    </a>
  </div>
</div>

---

This library is a wrapper around Health Connect for react native. Health Connect is an Android API and platform. It unifies data from multiple devices and apps into an ecosystem. For Android developers, it provides a single interface for reading and writing a user’s health and fitness data. For Android users, it offers a place for control over which apps have read and/or write access to different types of data. Health Connect also provides on-device storage. Read more [here](https://developer.android.com/guide/health-and-fitness/health-connect).

:::note
If your app integrates with Health Connect, before going to production, you need to fill out a form explaining what data types the app reads/writes, and how this data is used. For more information, see [How do I request access to read/write data types?](https://developer.android.com/guide/health-and-fitness/health-connect/frequently-asked-questions#q_how_do_i_request_access_to_readwrite_data_types).
:::

:::note
Health Connect is now currently on alpha channel. Check [here](https://developer.android.com/jetpack/androidx/releases/health-connect) for more information.
:::

Read more about health connect architecture [here](https://developer.android.com/guide/health-and-fitness/health-connect/platform-overview/architecture).

## Features

- Typescript :white_check_mark:
- Supports both old and new architecture :white_check_mark:

## Tips

1. Keep in mind that this library is **Android only**.
1. Health Connect is now currently on alpha version. Check [here](https://developer.android.com/jetpack/androidx/releases/health-connect).
1. If the user declines your permission request twice, your app is permanently locked out, and cannot request permissions again.

Read more about health connect architecture [here](https://developer.android.com/guide/health-and-fitness/health-connect/platform-overview/architecture).

## License

MIT
