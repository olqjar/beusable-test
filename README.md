# Room Occupancy Manager application for BeUsable.

## Requirements
* java 21
* gradle

## Build and test the application 

### Run unit tests
```
./gradlew test
```
### Run integration tests
```
./gradlew integrationTest
```
### Build application
The build step runs all (unit and integration) test cases.
```
./gradlew build
```


## Run application
You can launch applications using the command:
```
./gradlew bootRun
```

or you can also build jar and run it using java
```
./gradlew bootJar
java -jar build/libs/myapp-0.0.1-SNAPSHOT.jar
```
The application runs on port 8080 and accepts POST request at path
```
/api/rooms/occupation/calculate
```

There is an cURL example:
```
curl -X POST http://127.0.0.1:8080/api/rooms/occupation/calculate \
     -H "Content-Type: application/json" \
     -d '{
          "roomConfiguration": {
              "premiumRooms": 2,
              "economyRooms": 2
          },
          "customerOffers": [23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]
     }'
```

## Tests
Test included in task were implemented in class package as unit tests in class 
com.beusable.test.roommanager.service.RoomOccupationCalculatorServiceImplTest as method
* test1
* test2
* test3
* test4_fixed (see description bellow)

## The fourth test problem
There is an error in for test description:

* (input) Free Premium rooms: 7
* (input) Free Economy rooms: 1
* (output) Usage Premium: 7 (EUR 1153)
* (output) Usage Economy: 1 (EUR 45.99)

This output is never be achieved for provided test data:
[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]

Valid output values:
* (output) Usage Premium: 7 (EUR 1099)
* (output) Usage Economy: 1 (EUR 99.99)
  
1. There is no offer with EUR 45.99
2. Highest non-premium offer is EUR 99.99
3. next non-premium offer is 45
4. 1153 for premium room could be possible if test data would contain both 99.99 and 99 offers