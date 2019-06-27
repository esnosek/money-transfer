# Rule based extractor

## Clone
```
git clone https://github.com/esnosek/money-transfer.git
cd money-transfer
```

## Compile
```
mvn clean package
```

## Start the application

To run end to end tests you need to start the application first. Application starts on localhost:8080, but you can change this value in application.property file
```
java -jar target/money-transfer-jar-with-dependencies.jar
```

## Run tests

```
mvn verify -Psurefire
```
