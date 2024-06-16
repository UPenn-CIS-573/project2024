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

## 2.10 Encryptation (Boshu Lei)

Execution

```
 node --security-revert=CVE-2023-46809 .\api.js
 node .\admin.js
```