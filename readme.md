# Driver Reports



####Assumptions

- The data is given as a file in csv format.
- If the data is incomplete or improper, the specific row is ignored and the execution continues to next row.
	+ Incomplete or Improper:
		+ Unrecognized command
		+ Invalid arguments (e.g. xyz for start time)
		+ Incomplete arguments (e.g. Trip,Dan,07:15)
		+ Invalid data row (e.g. Dan73y3332883)
- A Trip command implicitly adds a driver if a driver isn't registered with the Driver command.
----
####Design
- There is an interface **CommandProcessor**. It provides the contract for generating the driver reports from a CSV file. In future, this interface can be extended to process other kinds of input.
- The only external library added is JUNIT 5 which is necessary for the testing. Besides that, every class uses native Java code.

----
####Execution

**Prerequisites**: maven and java

Compiles and builds the jar inside target folder.

`mvn clean install`

The argument should point to a csv file.

`java -jar target/driver-report-1.0.0.jar ./src/main/resources/data.csv`

The argument to the jar is the file location. Note that the jar comes with a file named data.csv. For ease of use, you can replace the contents of data.csv with your contents. You can point to a different file of your choice. If the file is not inside the project folder, please give the full path instead of a relative path.

**List of files in the project**
- ./src/test/resources/fully_valid_data.csv
- ./src/test/resources/partially_valid_data.csv
- ./src/test/resources/test/fully_invalid_data.csv
- ./src/main/resources/data.csv

You can copy any of the above paths as-is and pass as an argument to the jar.

----
####Unit Tests and Integration Tests
- **Unit Tests:** (tests specific methods)
	+ CommandProcessorImplTest
	+ BaseCommandProcessorImplTest
- **Integration Tests:** (reads a file,generates reports and asserts their correctness)
    + CommandProcessorTest

There is no mocking involved in any of the test classes. The models and exceptions are indirectly tested through the above tests.

----

####Test Reports
Test Coverage folder contains the test coverage reports. Click on index.html and navigate to the class of your choice.

The test coverage is 100% for lines. Note that the class named **Executor** is the main class. It doesn't have a test suite since it is meant to make the program executable.

----