# FallArm-Project

## Overview

FallArm is an Android project created as part of the CS551: Mobile Computing. The project aims to provide an efficient fall detection system using simulated sensor data from an Android device. The system detects a "fall down" motion, locates the patient and communicates this information to a server, which in turn requests assistance from medical staff.

## Features

- Simulates and generates accelerometer and gyroscope/orientation data dynamically.
- Changes and uses Geo-location data.
- Sends sensor data, location, and patient ID from the Android device to a server using socket programming.
- Server-side fall detection using machine learning algorithms, particularly KNN.


## Contributors 

- Dawit Woldemichael
- Siqi Liang 
- Linh Bien


## Table of Contents
- [Introduction](#introduction)
- [Key Features](#key-features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Introduction
The FallArm project aims to provide a solution for timely assistance to patients who experience falls. By utilizing sensor data, location services, and real-time communication, the Android app detects falls and alerts nurses, ensuring prompt care.



## Technologies Used
- Android Development (Java, Kotlin)
- Sensor APIs (Accelerometer, Gyroscope)
- Location Services (GPS)
- Socket Programming (Java)
- SMS Integration (Twilio, Nexmo, etc.)
- Data Privacy & Security Measures
- Machine Learning (for advanced fall detection)
- Indoor Positioning Systems (optional)
- Voice Alerts (Text-to-Speech)
- Auto Dialing (TelephonyManager)
- Version Control (Git)

## Installation
1. Clone this repository: `git clone https://github.com/your-username/FallArm.git`
2. Open the project in Android Studio.
3. Build and run the app on an Android emulator or device.

## Usage
1. Launch the FallArm app on your Android device.
2. The app will generate accelerometer and gyroscope data.
3. Location services will determine the patient's position.
4. Socket programming facilitates communication with the server.
5. Fall detection algorithm analyzes sensor data for falls.
6. If a fall is detected, an SMS notification is sent to nurses.


## Contributing
Contributions are welcome! If you have ideas, bug fixes, or enhancements, please follow these steps:
1. Fork the repository.
2. Create a new branch for your feature: `git checkout -b feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature-name`
5. Open a pull request.


