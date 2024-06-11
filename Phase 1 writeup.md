# Phase 1 Writeup

## Additional Tasks to be Graded
- Task 1.5: Add & Implement 4 unit test files each covering a method in the DataManager.java for all 4 methods. Additionally, bug fix error codes that are found during testing.
- Task 1.6: Display the total amount of donation that a user has donated when checking the view donation fragment.
- Task 1.8: [Brief Description]
- Task 1.9: [Brief Description]

## Changes Made in This Phase

### Task 1.1:
- **Brief Description**: Provide a brief description of what was done in this task.

### Task 1.2:
- **Brief Description**: Provide a brief description of what was done in this task.

### Task 1.3:
- **Brief Description**: Provide a brief description of what was done in this task.

### Task 1.4:
- **Brief Description**: Provide a brief description of what was done in this task.

### Task 1.5:
- **Brief Description**: Created 3 new test files for the remaining methods in DataManager. For each of the method, create tests that tested for a success fetch, a failed fetch and an exception throw which covers all of the statements. Fixed minor part of the code where there is a mistake that resulted in incorrect test results.

### Task 1.6:
- **Brief Description**: Added a TextView in `activity_view_donations.xml` to show the total amount of donation that the current logged in user has made. Added a few lines into `ViewDonationsActivity.java` to calculate the amount of total donations made and display it onto `activity_view_donations.xml`.

### Task 1.8:
- **Brief Description**: Provide a brief description of what was done in this task.

### Task 1.9:
- **Brief Description**: Provide a brief description of what was done in this task.

## Bugs Found and Fixed in Tasks 1.2, 1.5 
### Task 1.2
- **Bug Description**: Briefly describe the bug.
- **Fix Description**: Explain how the bug was fixed.

### Task 1.5
- **Bug Description**: The setter parameters for a profile login `creditCardExpiryMonth` and `creditCardExpiryYear` are placed in the wrong order so accessing `creditCardExpiryMonth` gets the expiring year and vice versa.
- **Fix Description**: Switched the parameter order of `creditCardExpiryMonth` and `creditCardExpiryYear`

- **Bug Description**: The getter method to access the fund name from a list of JSON data within `DataManager.attemptLogin` is incorrect resulting in `null`
- **Fix Description**: Changed the getter of JSON object using `(String)jsonDonation.get("fund")` to access the fund name as a String.

## Known Bugs or Issues
- **Issue & Fix**: Android Studio throws update check on Gradle version so updated Gradle Version.
- **Issue & Fix**: Added fixes and updates paths for testPath and classPath in `gradle.build (Module)`

## Team Member Contributions
### Member 1: [Tahmid Ahamed]
- **Tasks Worked On**: Task 1.8, Task 1.9
- **Contributions**: Provide a specific description of contributions.

### Member 2: [Silvia Alemany] 
- **Tasks Worked On**: Task 1.1, Task 1.2
- **Contributions**: Provide a specific description of contributions.

### Member 3: [Geshi Yeung]
- **Tasks Worked On**: Task 1.3, Task 1.4
- **Contributions**: Provide a specific description of contributions.

### Member 4: [Aaron Wu]
- **Tasks Worked On**: Task 1.5, Task 1.6
- **Contributions**: In task 1.5, swapped the parameter locations so the error of getting the expiration month vs experation date credential is returned correctly and added test files for each of the methods in DataManager as well as making sure that 100% statement coverage is met. In task 1.6, added the TextField and the logic between `activity_view_donations.xml` and `ViewDonationsActivity.java`. TextField is added into `activity_view_donations.xml`, a variable is used to sum up the donations as the list of donation is iterated in `ViewDonationsActivity.java` and the called to display. Set up this write-up template.
