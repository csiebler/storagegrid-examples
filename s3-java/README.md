# Instructions

First, update `src/main/resources/config.properties` and `src/test/resources/config.properties` with your configuration settings. The code examples and the acceptence tests have seperate configuration files.

To execute the code example, first adjust the address of the StorageGRID Webscale API Gateway. Then execute:
```
$ ./gradlew run
```
This will automatically download Gradle and the required libraries. Then, it will compile and execute the example code.

To generate an Eclipse project file, use:
```
$ ./gradlew eclipse
```
and then choose `File -> Import` in Eclipse, and import the `s3-java` folder.

To run the tests (i.e., to validate other version of the used AWS SDK), run:
```
$ ./gradlew test
```

# Notes

The provided acceptence tests are a quick shot for making sure, that basic functionality works.
