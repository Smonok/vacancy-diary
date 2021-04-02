<<<<<<< HEAD
# vacancy-diary
=======
## Installation and start-up instructions
1) Clone this repository<br>
2) Open it with your IDE<br>
3) Create PostrgeSQL DB with name `vacancy_diary`<br>
4) Modify the `applications.properties` file by setting the following parameters:    
    - For DB configuration
        * spring.datasource.username - DB user name
        * spring.datasource.password - DB password
    - For email configuration<br>
    `Note: Email notifications tested on the mailtrap.io service`
        * spring.mail.host - mail server host
        * spring.mail.port - mail server port
        * spring.mail.username - user name
        * spring.mail.password -  password
        * In the _src/main/resources/templates/email_template.json_ file you can specify the email from 
        which messages will be sent and message text
    <br>
5) If you want to change the list of available statuses for vacancies go to 
_src/main/java/pro/inmost/vacancydiary/model/Status.java_ and extend the enum<br>
6) Run the application<br>
###### When the application starts, the database will be automatically filled with some values
#### API Requests 
###### By default starts from _localhost:8090/diary/_
`authenticate` - generate bearer token **POST**<br>
<br>
`users` - find all users **GET**<br>
`users/{id}` - find user by id **GET**<br>
`users` - create user **POST**<br>
`users/{id}` - update user by id **PUT**<br>
`users/{id}` - delete user by id **DELETE**<br>

<br>

`vacancies` - find all vacancies  **GET**<br>
`vacancies/{id}` - find vacancy by id **GET**<br>
`vacancies` - create the vacancy **POST**<br>
`vacancies/{id}` - update the vacancy by id **PUT**<br>
`vacancies/users/{userId}` - find all user vacancies by userId with pagination (5 vacancies per page) **GET**<br>
`vacancies/statuses/{status}` - find all vacancies by status **GET**<br>
`vacancies/companies/{company}` - find all vacancies by company **GET**<br>
<br>
`vacancies/users/{id}/email` - send email for user by id. 
Email will be sent for all vacancies with the status _Waiting for feedback_, if at the current moment of the request a week has passed since the moment when this status was set **POST**<br>
 

>>>>>>> f05f893fb729c46646a65169585d5b2c949a7f41
