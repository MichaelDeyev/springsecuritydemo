# springsecuritydemo
spring security demo project

Go to url:
http://localhost:8080/auth/login

after autorisation you will be redirect to success-page: http://localhost:8080/auth/success
in success-page you can select "Logout" for logout or "Main Page" to go to page with some data (http://localhost:8080/api/v1/developers?) 

There are 2 users: 
1) login: admin@mail.com; password: admin
2) login: user; password: 111111

Admin-user can create, read, update, delete data on Main Page (only by Postman for now)
Plain user can read only
