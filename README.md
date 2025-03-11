Develop a simple point of sale system that stores menu items, including allergy information, ingredients, and price that can be updated and changed.  My system takes a session object that stores the employee
that is using the system as well as what items are available to order, what orders are currently being worked on and makes sure that all information is backed up on the database.  The program also pulls 
information out of the database for the user to be able to manipulate it without making duplicate entries in the database.  The program checks to make sure that the employee using certain pages has the access
level to do so.

Requirements
Use Case

	User  client  server  db software  server  client  user
Description of problem

User logs into client application to enter in information needed for the transactions.  The client communicates with server, which accesses the db software to pull up menus, as well as employee information 
such as access credentials to update data.  The client application retrieves the current information from the database and communicates with the server to keep a record of the transactions processed on the 
client application to be able to update any changes made by the client application to the data stored (like menu updates, ingredients, prices, etc.).  The client application will interact with the user by a 
web interface.  The program needs to be able to travel from one html page to another depending on data manipulation and methods to complete specific tasks.
 

Design
Sequence of major functionality
Web UI (Common case)
	The web interface starts off on a login page that leads into a screen that displays table buttons that automatically assign a transaction a table menu so that is ready to hold an order at that specific table. 
 When you click on the table button, it goes into a screen that displays the current information for the transaction that is being manipulated.  Underneath the current transaction information, there is a series 
 of checkboxes that allows you to click on an item, or multiple items, that can be ordered.  After clicking “order” the system, updates the information on the display as well as the information on the database.  
 The transaction is added to a session arraylist that  keeps track of the current orders, as well as the order that is being manipulated at the current time.  The transaction screen allows you to close the 
 transaction, entering the payment type and uploading the final information to the database and return to the table screen.  Going back to the table screen, there is also the option to go into administration 
 screens that can update employee information, create a new employee or the information and do the same for menu items.  To be able to enter the admin pages, the program makes sure that you have the proper 
 security level.  After making updates/changes you are redirected back to the table screen.  You are also able to log out from the table screen, which will invalidate the session id and redirect you to the 
 login page.
Table layout
			Employee Table
firstName	  lastName	  Id	    Pay	    payType	secLevel	current
varchar	    Varchar	    Integer	numeric	Varchar	Integer	  Varchar

Menu Table

ID	    Name	  Type	  subtype	Price	  Ingredients	Allergies	Current
numeric	Varchar	Varchar	Varchar	Numeric	Varchar	    Varchar	  Varchar

Transaction Table

ID = transaction ID
eID = employee ID

ID	    eID	    Ingredients	Allergies	NetTotal	GrossTotal	pmType
Integer	Integer	String[]	  String[]	Numeric	  Numeric	    varchar
