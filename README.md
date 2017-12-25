# transaction-processor

Simple web application that allows to store transaction details send via post request

Technologies

Spring-Boot, Spring-Data, Akka, Cassandra, Docker, Maven, Lombok, JUnit


Testing

Run spring-boot application on localhost and send following POST request:

curl -d '{"issuerAccountId": 9,"recipientAccountId": 2,"time": "2017-12-27 17:00:16","value": 90}' -H "Content-Type: application/json" -X POST http://localhost:8080/transaction-processor/