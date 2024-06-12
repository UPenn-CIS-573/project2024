                                  Project Phase1 Write-up

Q1. 

The additional tasks that we would like graded for this phase are Task 1.6 Task 1.8, Task 1.9 Task 1.10


Q2. 

Task 1.1:

Add some extra test cases to the createFund method to test some special cases such as when the input parameter is empty or null. Created JUnit tests for getContributorName and attemptLogin methods, making the test coverage 100%.

Task 1.2:

Modified String description = (String)data.get(“descrption”) in the method of attemptLogin; here the description is misspelled. And we have added the judgment of whether the parameter is empty or not to the methods of attemptLogin, getContributorName, and createFund.

Task 1.3:

Modified the start() method to enable it to handle invalid inputs such as non-numerical values, double. If an invalid input is entered by the user, it will display an error message and prompt the user to enter their choice. We also handle the case where the user enters an integer value but the corresponding fund does not exist. In this case, it will prompt the user to re-enter their choice. In case that the user enters “quit” or “q”, the program will exit and print “Good Bye!”.

Task 1.4:

For this task, the goal is to calculate the total amount in Funds and the percentage of the current amount in the total goal. In order to display the statistical data, we made some additions to the displayFund function in UserInterface. When it traverses all funds, it counts all donations through a long value and displays it at the end of the Fund traversal.


Task 1.6:

Modified the onResume() function in ViewDonationActivity.java. We mainly modified the UI component by adding one more entry in the ListView to display the total donation amount.




Task 1.8:
Blank Name and Description Handling:
Added input validation loops to ensure the fund name and description are not blank. The user is re-prompted if a blank input is provided.
Non-Negative Target Handling:
Ensured the target value is a non-negative number. If the user enters a negative value, the program re-prompts the user for a valid non-negative target.
Non-Numeric Target Handling:
Handled non-numeric inputs for the target value by using a try-catch block to catch NumberFormatException. If the exception is thrown, the user is re-prompted to enter a valid numeric value.

Task 1.9:

For the DataManager.attemptLogin method, the catch section adds a throws an IllegalStateException case and will print “Error in communicating with server ". Added try catch in UserInterface.Main() to catch an IllegalStateException and return “Error in communicating with server” instead of just “Login Failed”.

Task 1.10:

This task is implemented in the Organization App. To format the date data read from JSON, a new method is added to DataManager, that is parseDateFormat, which uses the string parsed from JSON as input, parses the date with the help of the SimpleDateFormat class, and returns the date in the format of MMMM dd, yyyy for creating a new Donation object.



Q3.

Task 1.2:
In the attemptLogin method String description = (String)data.get(“descrption”); here the description is misspelled, which prevents getting the content using getDescription correctly.
In addition, attemptLogin, getContributorName, and createFund methods are missing on whether the parameter is empty or null judgment, if the user inputs these parameters are null or empty case should return null directly.






Q4.

In the attemptLogin method, String description = (String)data.get(“description”); here the description is misspelled.


Q5.



Q6.

Jiahao Guo: Task 1.1 & Task 1.9

Rachel Feng: Task 1.2 & Task 1.8

Leyi Jiang: Task 1.4 & Task 1.10

Haoyu Zhao: Task 1.3 && Task 1.6
