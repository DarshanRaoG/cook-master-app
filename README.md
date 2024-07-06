# Cook Master - Overview
* Cook Master is a recipe sharing application developed using Vue 3 + Springboot + MySQL
* It allows users to share recipes, favorite them, learn from others and more
* First time users need to sign up, others need to login once
* Comments can be added to give feedback about the recipes!

## Pre Requisites
* Must have Java installed and configured
* Should know about Maven and SpringBoot
* Node.js
* Vue.js 3
* MySQL DB (I used MySQL provided with Xampp control panel)
* IDEs I used were IntelliJ Community Edition and VSCode

  
## How to use in your local
* Download the zip file under the  `<> Code` in this repository
* Extract it, and you should see there are two folders, one for backend and one for frontend
* Backend -> SpringBoot, Frontend -> Vue.js 3
* Start your MySQL server, and create a database named as `recipedb`
* Now run the SpringBoot application
* Next, do `npm install` for frontend and then start the Vue.js server by typing `npm run serve`
* Go to the localhost:<port-number> and you should see the app's UI!

## Idea for an easy way to start the app once setup is done
* If you find this very complicated to start every time, then you could either write a batch script to start the MySQL, SpringBoot and Vue3 Servers or you could deploy the app after building it.
* For example:
   ```
    @echo off
    
    REM Start XAMPP (Apache and MySQL)
    echo Starting XAMPP...
    start "" "C:\xampp\xampp_start.exe"
    
    net start Apache
    net start MySQL
    
    REM Start Spring Boot application
    echo Starting Spring Boot application...
    start "" "java" "-jar" "path\to\your\cookmaster-0.0.1-SNAPSHOT.jar"
    
    REM Wait for a few seconds to ensure Spring Boot has started
    timeout /t 10 > nul
    
    REM Open localhost:8080 in the default web browser
    start http://localhost:8080
    
    REM Wait for user to close the command prompt window
    echo Press any key to stop servers and exit...
    pause > nul
    
    REM Stop XAMPP (Apache and MySQL)
    echo Stopping XAMPP...
    net stop Apache
    net stop MySQL
    ```

## Demo
<p align="center">
  
  ![Alt Text](https://github.com/DarshanRaoG/cook-master-app/blob/main/cookmaster-demo-720p.gif)
</p>

## Attribution Appreciated!
* If you use my code, kindly attribute it to me (https://github.com/DarshanRaoG). Thank you!
