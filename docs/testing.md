# Unit Testing
When we create new features, it is very helpful to have that feature be tested with unit tests. The testing frameworks we use for unit tests are [Robolectric](http://robolectric.org/), [Mockito](http://mockito.org/), and JUnit.

### Testing Pyramid
 ![Testing Pyramid](http://martinfowler.com/bliki/images/testPyramid/pyramid.png)

 Unit tests are used to test the happy and sad paths of our application. UI or user journey tests are used to test mission critical features of our app. If you are in doubt about which type of test to write, you most likely want to write a unit test.
