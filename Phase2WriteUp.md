Project Phase 2 Write-up
Q1.

The additional tasks that we would like graded for this phase are 2.4, 2.5, 2.6, 2.7

Q2.
Task 2.1
The method attemptLogin of the class DataManager of Contributor app  was changed. A cache using HashMap was added. Before looping through the funds, the cache is initialized. For each contributor of a fund:
Check to see whether the contributor name in the cache
Yes -> Use this name
No -> Call the getContributorName method and add the returned name to the cache


Task 2.1
The attemptLogin() method, getContributorName() method and the createFund method in DataManger of the organization app are modified. These methods are now able to handle the cases where DataManager receives bad data from the UserInterface, or when the WebClient cannot connect to the server, or when the data returned by the WebClient does not match the expectations of the DataManager, e.g. if the JSON is empty or malformed. If these cases are handled, the app will throw illegalArgumentException or illegalStateException.

The main() method, start() method and createFund() method in  UserInterface are modified. If the dataManger variable throws exceptions mentioned above, these methods in UserInterface will catch it and display meaningful message and allow the user to retry their operations.

Task2.3
This task modified the fund class to add the aggregation and getAggregatedDonations methods and an AggregateInfo class to implement the aggregation display and created an AggregationList to hold the cache. If the cache is empty, the aggregation method is called to recalculate.
The displayFund method was also modified in the userInterface which allows the user to choose to display the content of each donation or the form after aggregation.

Task 2.4
The method attemptLogin of the class DataManager was changed. A cache using HashMap was added. Before looping through the donations, the cache is initialized. For each fund of a donation:
Check to see whether the fund name in the cache
Yes -> Use this name
No -> call the getFundName method and add the returned name to the cache
Task 2.5
Modified DataManager class to handle exceptions such that all the tests in AndroidDataManagerRobustnessTest pass. 
Enhanced the user interface (UI) of the Android application to display error messages in Toast and provide the option for the user to retry operations when encountering errors related to data access.
Task 2.6
The menuActivity method was modified. We added one more button by adding the method onViewDonationsByFundButtonClick() in the menuActivity. We accordingly also modified the activity_menu.xml to display that button. We also added one more file namedViewDonationActivityByFund.java in order to display the donations by fund and also added one more file named activity_view_donations_byfund.xml to show the layout of donations by fund.
Task2.7
Added a new method deleteFund in DataManager to delete created funds. In addition, a removeFund method has been added to the organization to update the fund after it has been deleted. A third option has been added to displayFund in userInterface to delete the specified fund. Also added the ability to update the number and status of funds in the start method.
A new class called DataManager_deleteFund_Test was created to test the deleteFund method.

Q3.

Q4.

Q5.

Leyi Jiang: Task 2.1 & Task 2.4
Rachel Feng: Task 2.5
Jiahao Guo: Task 2.3 & Task 2.7
Haoyu Zhao: Task 2.2 & Task 2.6
