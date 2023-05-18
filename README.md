# Leave_Management_System_Project
This is simple desktop application build for leave application and management of leave records of employee.
The fellow who is Employee of organization can apply for leave with required details and the fellow who is
in Admin Role can approve or reject leave.

Small limitation with this application is that employee can not apply for backdated and half day leave.

Once leave application for employee gets approved his balance of leaves will be updated.

FRONT END and BACK END
Core java(jdk 8.0) Menu Driver code is Front End and MySQL (MYSQL 8.0) is used to manage the data at Back End.

Core java code working as UI is not directly connected with database, instead in between one DAO implementation
code is created in which actually database connecting and data management code is created.

Database is with three tables created Role, Employee, Leavedetails and it is in 2nd normal form.
All three table are inter-connected with each other with the concept of foreign key and primary key.
