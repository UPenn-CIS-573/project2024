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

Add quit goodbye; 

Add while loops to parse input string until the program get a valid option

## 1.4 D**isplay total donations for fund** (Ruxi Xu)

modified displayFund function in UserInterface to show total amount of donation and percentage of target

## 1.8 **createFund input error handling** (Tim Song)

Add while loops to detect blank names

Add while loops to detect blank descriptions

Add while loops to parse target number, until the program gets a positive target number.

## 1.9 Login Error Handle (Boshu Lei)

throw IllegalStateException when response is null; catch this error in the main loop and display `Error in communicating with server` in Console, otherwise display `Login Fail` if the response status if fail. 

## 1.10 Date formatting (Ruxi Xu)

Format the date string in `UserInterface` and set the donation.


## 2.3 Organization App aggregate donations by contributor (Tim Song)

I create a new class `AggregateDonation` which contains the aggregated donation information for each fund.
It is created during the creation of that fund in the memory.

