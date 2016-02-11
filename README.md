# Firebase JUnit Example
This is an example of JUnit integration-testing of Firebase-backed Android application.

Main files:

- [ProductsService](app/src/main/java/com/firebasejunitexample/app/services/ProductsService.java) - A simple database access class for basic CRUD operations
- [ProductsServiceTest](app/src/test/java/com/firebasejunitexample/app/services/ProductsServiceTest.java) - An integration JUnit test for the above
- [FirebaseAssertions](app/src/test/java/com/firebasejunitexample/app/testutils/FirebaseAssertions.java) - Assertions for Firebase contents
- [FirebaseMocker](app/src/test/java/com/firebasejunitexample/app/testutils/FirebaseMocker.java) - Mimics Android environment to make Firebase work. Also contains some useful methods for manipulating Firebase data.