Math Geniuses App
=================

Android app to make it easier for kids to learn the 4 basic math operations: +, -, x, /.

Setting the Environment
=======================

To build the app we used:

- Eclipse with Android plugin, installed from http://developer.android.com/sdk/index.html
- Google Play Services Library, installed from the SDK manager, it can be found in the Extras directory
 - In order to use Google Play Services with your app, you have to generate a key in the Google developers site, as explained here: [Start integrating Google+ into your Android app](https://developers.google.com/+/mobile/android/getting-started) and here: [Google+ Sign-in for Android](https://developers.google.com/+/mobile/android/sign-in). If you don't follow these steps, you won't be able to login into the app, since Google+ is the only mechanism provided so far for login.
- appcompat_v7 library, included by default by the IDE

Understanding the Code Structure
================================

The code is structured in 4 packages:
- com.javaproject.mathgeniuses
 - the mathgeniuses package contains all the activities, fragments and adapters. This is the package dedicated to the classes that have to do with the user interface.
- com.javaproject.mathgeniuses.database
 - the database package contains the database contract and the database adapter classes, which are used for creating the SQLite database.
- com.javaproject.mathgeniuses.entities
 - the entities package contains the business objects that hold the core information of the application.
- com.javaproject.mathgeniuses.utils
 - the utils package contains utility clases, like dialog helpers, Google+ connectivity classes.

Using the App
=============

Once you start the app, you will encounter the following screens:
- Login screen: here you must sign in with your Google+ account. Once you do, you will continue to the following screen.
- Menu screen: this screen presents the 5 options you have to practices math: +, -, x, / and all. Right now you can select + and -, since these are the only categories that have math exercises to practice. So touch the + button to go to the Lesson choice screen.
- Lesson Choice screen: here you will see 5 addition lessons. Each addition lesson is a little bit more advanced than the previous one. A lesson consists of 10 exercises. You can see what percentage of exercises you have completed for each lesson in the progress bar below the lesson's name. If you have completed all the exercises for one lesson, then you can see if you got a perfect score (3 stars) or a not so perfect score. It doesn't matter if you have completed the lesson or not, every time you select a lesson, you start from exercise 1.
- Play Exercises screen: once you have selected a lesson, the first exercise starts. There are two types of exercises: the dragging exercise and the shaking exercise. For the draggin exercise, you have to drag the objects from the lower box to the upper box until you have the correct answer. For the shaking exercise, you have to shake the device until you have the correct amount of objects in the answer box. All the exercises are 10 seconds long (we don't have any kids here, so for the young adults, we thing 10 seconds is challenging enough to complete the exercises).
- Once you have completed the 10 exercises for one lesson, a Toast will appear showing your score. You can return to the Lesson Choice screen by pressing the back button.
