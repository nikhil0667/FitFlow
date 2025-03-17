# **Fitness Tracker App**
**Company** : CODTECH IT SOLUTIONS

**Name** : Khambhayta Nikhil Pravinbhai

**Intern ID** : CT08VDB

**Domain** : ANDROID DEVELOPMENT

**Duration** : 4 WEEKS

**Mentor** : NEELA SANTOSH

## **Project Overview**

- As part of my **Android Development Internship** at **CodTech IT Solutions**, I am developing a **Fitness Tracker App** that monitors users' physical activity, including **step count, distance traveled, and calories burned**.

- This app leverages **Android’s built-in sensors** for real-time tracking and stores data for historical analysis. It includes **data visualization** using **charts and graphs**, along with a **background service** that records users' daily activity automatically at **11:59 PM**.

- The primary goal of this project is to create an efficient **fitness-tracking** solution that helps users monitor their daily activity and track progress over time.

## **Key Features**
### **1. Real-Time Step Tracking**

      ✅ Uses Android’s built-in step sensor to track live step count.

      ✅ Updates step count in real-time on the home screen.

2. Distance & Calorie Calculation

      ✅ Calculates distance traveled using step length approximation.

      ✅ Calories burned is estimated based on movement data.

3. Data Visualization (Graph & List Display)

      ✅ Displays daily, weekly, and monthly step count using graphs.

      ✅ List view shows detailed step count, distance, and calories for each day.

4. Background Processing (Auto Save at 11:59 PM)

      ✅ Stores daily activity in Room Database at 11:59 PM using WorkManager.

      ✅ Ensures data is saved even if the app is closed.

6. Historical Data Storage

      ✅ Saves all past step count records for progress tracking.

      ✅ Users can view and compare their performance over time.

## **Technologies & Tools Used**

      🛠 Android Studio – Development environment

      🛠 Java/Kotlin – Primary programming language

      🛠 Android Sensors – Step Counter & Accelerometer

      🛠 LiveData & ViewModel – For real-time UI updates

      🛠 MPAndroidChart Library – For graph-based visualization

      🛠 Sqlite Database – Local storage for step count history

      🛠 WorkManager – Background task scheduler for daily data storage

## **Step-by-Step Implementation**
### **1. Project Setup & Dependencies**

     - Created a new Android Studio project.

     - Integrated Step Counter API and Room Database.
  
### **2. UI Design**
  
    - Designed an interactive UI with:
      
      ✅ Live Step Counter
      
      ✅ Graph View for Visualization
      
      ✅ List View for Daily Tracking
      
      ✅ Settings Page for User Preferences
### **3. Implementing Step Tracking**
  
    - Used SensorManager API to track steps in real-time.
  
    - Displayed step count updates instantly on the home screen.
    
### **4. Distance & Calorie Calculation**
   
    - Applied custom formula to estimate distance and calories burned.

    - Displayed results live on the dashboard.

### **5. Storing Daily Data Automatically**
    
    - Used WorkManager to save step data at 11:59 PM automatically.
    
    - Data is stored in Room Database and retrieved for future use.

### **6. Data Visualization**
    
     - Integrated MPAndroidChart for step count graphs.
     
     - Used ListView/RecyclerView for daily record display.

## **Challenges & Solutions**

  🚧 Step Count Reset on App Restart 
      – Used SharedPreferences to keep track of the last recorded steps.

  🚧 Background Processing Issues 
      – Implemented WorkManager for reliable daily data storage.

  🚧 Battery Optimization Restrictions 
      – Ensured the app works without excessive battery drain.

## **Unique Features & Enhancements**

    ✔ Real-Time Step Tracking – Uses Android’s built-in step sensor.

    ✔ Daily Data Storage at 11:59 PM – Automatically stores progress.

    ✔ Graph & List View for Insights – Helps users track trends.

    ✔ Low Power Consumption – Optimized for minimal battery usage.

## **Learning Outcomes**

    ✅ Gained hands-on experience with Android Sensors & Step Counting APIs.

    ✅ Learned how to store and retrieve data using Room Database.
    
    ✅ Implemented real-time UI updates using LiveData & ViewModel.

    ✅ Improved background processing skills with WorkManager.

    ✅ Enhanced problem-solving abilities in real-time data tracking.

