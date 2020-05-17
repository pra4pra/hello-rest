# hello-rest
Department Rest API Create/Update/Read

**Assumptions:**
  - created hello-rest app as per instructs provided.
  - Pre configured ppaluru/test123 user as default user.

  - Business Service layer used even there is not much logic as usual design standards.
  - Repository does not have any custom code so no test cases exist.
  - Provided test cases for controller and integration tests to cover
    - Access privilege with and without.
    - Data validation basic tests since there is not much business logic.
  - Basic authentication used as this is sample application.


  - Implemented Created/Post, Update/Put, Read/Get Department.
  - Department Structure: {"departmentId":1,"departmentName":"Finance Department","employeeCount":13,"departmentCreateDt":"2020-May-16 18:46:40","departmentUpdateDt":"2020-May-16 18:46:40"}
  - Audit Columns being used as per events (create and update)

```

API Usage

Sample requests to validate API:

curl --location --request POST 'http://localhost:8080/api/v1/department/' \
--header 'Authorization: Basic cHBhbHVydTp0ZXN0MTIzNA==' --header 'Content-Type: application/json' \
--data-raw '{ "departmentName": "test 3", "employeeCount": 13 }'

curl --location --request PUT 'http://localhost:8080/api/v1/department/1' \
--header 'Authorization: Basic cHBhbHVydTp0ZXN0MTIzNA==' --header 'Content-Type: application/json' \
--data-raw '{ "departmentId": 1, "departmentName": "test put12 102", "employeeCount": 121 }'

curl http://localhost:8080/api/v1/department/1

curl http://localhost:8080/api/v1/departments

```

Build & deploy as Maven project
-
**Building application, deploy and run as Docker container **

  - Checkout code and into the location.
  - mvn clean package
  - docker build ./ -t department-api
  - docker-compose up

  Note: if you run multiple times, some times docker suggests to remove old images. if you get into that case use following to remove and run again.
  - docker rm -f /department-api;
  - docker rm -f /postgres;
