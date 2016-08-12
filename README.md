#Finatra Quill Mysql Basics

###Info
This is a very simple project that demonstrates usage of:

 1. [Finatra](http://twitter.github.io/finatra/) server, controller, model
 2. [Quill](http://getquill.io/) with MySQL DB
 3. [Bootstrap](http://getbootstrap.com/) and [jQuery](https://jquery.com/) for UI
 4. Basic scala tests with [Mockito](http://mockito.org/)  

###Setup:

 1. Run MySQL and execute script from [init.sql](./sql/init.sql)
 2. Adjust config at [application.conf](./src/main/resources/conf/application.conf) with your MySQL credentials
 3. Run application via
    ```$ sbt run```
 4. Then browse to: [http://127.0.0.1:9999](http://127.0.0.1:9999)
 
Feel free to use, comment or collaborate. 