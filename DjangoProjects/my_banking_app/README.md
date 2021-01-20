For my CS50 capstone project, I created a personal financial tracking application, myFianacial Tracker, which allows users to keep a running total of the balances in their checking and savings accounts. myFianacial Tracker also allows users to add recurring bills and payments (e.g., phone bill, paycheck) to the app then see a monthly breakdown of what their account balances will be once the recurring bill or payment hits their account on the specified day.

Application Files:

layout.html
The foundation for all additional web pages. Includes the <head> section with style and script tags as well mobile friendly meta tags like viewport. This page also includes the top navigation bar and footer for all web pages.

login.html
A basic form with inputs for username and password that allows users to log into the site or links to a registration page for non-users. When a user submits valid login credentials they are redirected to the index page. The login page is the default path for non-users.

register.html
A basic form allowing user to register for an account. Input fields include username, email, password, and confirm password. When the form is submitted, the new users login credentials are stored in the User database.

index.html
The home/profile page of the app. This page displays the user's current checking and savings account balances and prompts the user to select an option from the navigation bar. This page is only displayed if a user is logged in. 

balances.html
This page allows users to toggle between Deposit, Withdrawl, and Transfer views. This page is similar to balancing a checkbook when you add money to your account or make a purchase. The Deposit view allows users to add to their current checking or savings balances. The Withdrawl view allows users to subtract form their current balances. The Transfer view allows the user to update their balances if they transfer funds between their accounts. If you transfer $10 from your checking account to your savings account, the Transer view will subtract $10 from your checking and add $10 to your savings.

recurring.html
This page includes a form allowing the user to add a recurring bill or payment to be stored in the app. Options on the recurring page include selecting whether it is a recurring bill or recurring payment, the account associated with the recurring bill/payment, description of the recurring bill/payment (e.g., phone bill, paycheck), amount of the recurring bill/payment, and day of the month the recurring bill/payment hits your account. When the add recurring bill/payment form is submitted the information is stored in a Recurring table in the database. Below the add recurring form is a detailed list of all the current recurring bills and payments along with an option to remove a given recurring bill/payment.

details.html
This is a monthly account detail page with both checking account and savings account views. for each account view the page displays the current account balance followed by the users projected account balances for the rest of the month. Specifically, it uses the recurring bills and payments the user set up in the app to quickly display the users projected balance on the day of the month that a spefic bill is taken out of an accout or a specific payment is put into an account. For example, if the user has a current balance of $100 and has a recurring bill for $20 on x day then a recurring paycheck of $100 on y day, the app will let the user know that based on their current balance on day x their projected balance will be $80 and on day y their projected balance will be $180.

balances.js
This JavaScript file works specifically with the balances.html page. It allows the user to toggle between the deposit, withdrawl, and transfer view forms. When one of the forms is submitted it send a PUT request to the appropriate view in views.py which updates the database by adding or subtracting from the current account balances.

recurring.js
This file works with the recurring.html page to allow users to create new recurring bills and payments. When the recurring bill/payment form is sumbitted this files sends the form data as a POST request to the appropriate view in views.py which creates a new Recurring object and saves the object to the Recurring table in the database.

details.js
This file allows the user to toggle between checking account and savings account views at the click of a button on the details.html page.

styles.css
This file contains just a bit of custom CSS used across the application. Most of the applications styling was done using Bootstrap.

filters.py
This file contains a custom Django template filter which is used to access and return specific items from a list, passed into the HTML template, by index.

models.py
Contains the three models used in this application. User is an AbstractUser model used to store member login credentials. Balances which is tied to a specific User and stores their current chacking and savings account balances. Recurring which stores data about recurring bills and payments that are linked to specific Users.

urls.py
Contains all of the applications URL patterns and paths.

views.py
Contains all of the views used in this application.

Requirement Fulfillment:
This project is sufficiently distinct from all other projects due to the focus of the application which deals with current and future financial tracking and account balance manipulation. None of the projects dealt with banking, financial tracking, or projected future information. 

Regarding complexity, this project was more complex than class projects for several reasons. This project utilized a large majority of the different topics covered throughout the class which brings this project up on par with the complexity of the other projects. Beyond that there are a few specific areas of this project that help make it more complex than the others. Specifically, I created a custom template filter in order to access items in a list that was passed into the HTML template. I combined this filter with list representing the range of the list (equal to range(len(my_list))) to create a numeric for loop in the HTML template in order to iterate through multiple lists and select elements by index right in the HTML. This is done on the Monthly Account Details page (details.html) in order to predict a users future account balances based on regularly scheduled, recurring bills and payments. Creating a "templatetags" folder with custom filters was not covered in this course so I believe this went above and beyond the complexity of other projects.

This application contains three models, JavaScript, and is mobile responsive through the use of viewport, and flexbox.