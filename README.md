Starter code for Summer 2024 version of group project.

# Progress Report

Person: Boshu Lei, Tim Song, Ruxi Xu

---

## 1.1 DataManager Test Case (Boshu Lei)

```
DataManager_attemptLogin_Test.java
DataManager_createFund_Test.java
DataManager_getContributorName_Test.java
```

## 1.2 Bug Fix

Check fund id to be valid.

## 1.3 **main menu input handling** (Tim Song)

Add quit goodbye; While loop, parse input string to get valid option

## 1.4 D**isplay total donations for fund** (Ruxi Xu)

modified displayFund function in UserInterface to show total amount of donation and percentage of target

## 1.8 **createFund input error handling** (Tim Song)

loop name blank

loop description blank

target number positive

## 1.9 Login Error Handle (Boshu Lei)

throw IllegalStateException when response is null; catch this error in the main loop and display `Error in communicating with server` in Console, otherwise display `Login Fail` if the response status if fail. 

## 1.10 Date formatting (Ruxi Xu)

Format the date string in `UserInterface` and set the donation.

----

# Phase 2

## 2.1 Cache

In `DataManager`, a hash map is used for caching. 

```Java
private Map<String, String> contributorCache = new HashMap<>();
```

Each time when calling `getContributorName` method, the program will first check whether the Cache contains the id. If it contains, then the data in cache will be returned. Otherwise, the programm will query the server for the name. 

## 2.2 (Ruxi Xu)

Added `DataManagerRobustnessTests.java` in Organization App. All thest are passed.
UI is updated to display meaningful error message.

## 2.8 (Ruxi Xu)

Added logout function in UI classes; login again feature is also added to let user log back in after logout.

## 2.10 Encryptation (Boshu Lei)

Prepare the `public_key.pem` under `\org` and `\admin` folder. Prepare the `private_key.pem` under `\admin` folder. 

When launching javascript, please use the following command.

```
 node --security-revert=CVE-2023-46809 .\api.js
 node .\admin.js
```
