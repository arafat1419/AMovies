# AMovies
[![arafat1419](https://circleci.com/gh/arafat1419/AMovies.svg?style=shield)](https://circleci.com/gh/arafat1419/AMovies)

## App Interface
<img src="https://github.com/arafat1419/AMovies/blob/master/app_interface.png" />

## Features
* **Search :**  Search movies or tv shows by name
* **Favorite :** Make your favorite list of movies or tv shows
* **Sorting :** In homepage you can short by Best Rating, Name, or Make it Random
* **Trailer :** You can watch trailer of your movie or tv show directly in this app

## Clean Architecture
![image](https://user-images.githubusercontent.com/68770080/155262718-18d02d3b-ee76-477d-bf62-794fe89174fc.png)
Benefits of this Architecture is :
* **Independent of Framework** 
* **Testable** 
* **Independent of UI** 
* **Independent of Database** 
* **Independent of External** 

## Tech-Stack
* **Android Jetpack :** https://developer.android.com/jetpack
* **Glide :** https://github.com/bumptech/glide
* **Retrofit2 :** https://square.github.io/retrofit/
* **Lottie :** https://github.com/airbnb/lottie-android
* **OkHttp :** https://square.github.io/okhttp/
* **Koin :** https://github.com/InsertKoinIO/koin
* **SQL Cipher :** https://github.com/sqlcipher/android-database-sqlcipher
* **Leak Canary :** https://github.com/square/leakcanary
* **Material Design Component :** https://material.io/
* **Bubble Navigation :** https://github.com/gauravk95/bubble-navigation
* **Circular Progress Bar :** https://github.com/lopspower/CircularProgressBar
* **Android Youtube Player :** https://github.com/PierfrancescoSoffritti/android-youtube-player
* **Arc Layout :** https://github.com/florent37/ArcLayout

## How to use?
replace with your own token at Gradle Module:Core <br>
android { <br>
  defaultConfig {<br>
    buildConfigField "String", "REPLACE_WITH_YOUR_API", "\"Change your own token\""<br>
  }
}<br><br>

and Rebuild Project
