// --== CS400 File Header Information ==--
// Name: Lwin Oo
// Email: loo@wisc.edu
// Team: KE Red
// Role: FrontEnd Developer
// TA: Keren Chen
// Lecturer: Gary Dahl
// Notes to Grader: I didn't have that much good idea to write this. So, I did that very closely
// with Blue Team Frontend Developer

/**
 * This class contains a set of tests for the front end of the Movie Mapper project.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

// Implementation notes:
// I changed the backend to return a List<String> instead of List<MovieInterface> from the
// getThreeMovies method. When the MovieInterface get's implimented, some code will need to be
// changed
// I've marked all this code in comments, trying to give what it should be changed to
//
// Things I've Noticed: When scrolling through movies, an entry of 0 (or a selectionScroll < 0)
// displays no movies, but shows no error message. Entering a number again displays movies. Could be
// a feature?


public class TestFrontend {

  public static void main(String[] args) {
    (new TestFrontend()).runTests();
  }

  /**
   * This method calls all of the test methods in the class and ouputs pass / fail for each test.
   */
  public void runTests() {
    System.out.print(
        "Test enter 'x' to exit: ");
    if (this.enterXToExit()) {
      System.out.println("PASSED");
    } else {
      System.out.println("FAILED");
    }
    System.out.print(
        "Test frontend initially lists no movie: ");
    if (this.testFrontendInitialOutputNoMovie()) {
      System.out.println("PASSED");
    } else {
      System.out.println("FAILED");
    }
    System.out.print(
        "Test 'g' load genre selection screen: ");
    if (this.testFrontendGForGenres()) {
      System.out.println("PASSED");
    } else {
      System.out.println("FAILED");
    }
    System.out.print("Test 'r' load rating selection screen: ");
    if (this.testFrontendRForRatings()) {
      System.out.println("PASSED");
    } else {
      System.out.println("FAILED");
    }
    System.out.print(
        "Test '2' to remove low rated movies: ");
    if (this.testFrontend2ForRatings()) {
      System.out.println("PASSED");
    } else {
      System.out.println("FAILED");
    }
  }

  /**
   * This test runs the front end and redirects its output to a string. It then passes in 'x' as a
   * command. When the front end exists, the tests succeeds. If 'x' does not exist the app, the test
   * will not terminate (it won't fail explicitely in this case). The test fails explicitely if the
   * front end is not instantiated or if an exception occurs.
   * 
   * @return true if the test passed, false if it failed
   */
  public boolean enterXToExit() {
    PrintStream standardOut = System.out;
    InputStream standardIn = System.in;
    try {
      // set the input stream to our input (with an x to test of the program exists)
      String input = "x";
      InputStream inputStreamSimulator = new ByteArrayInputStream(input.getBytes());
      System.setIn(inputStreamSimulator);
      ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
      // set the output to the stream captor to read the output of the front end
      System.setOut(new PrintStream(outputStreamCaptor));
      // instantiate when front end is implemented
      Object frontend = new Frontend();

      Frontend.main(new String[] {"movies.csv"}); // !!!Remove this line when you uncomment the one
                                                  // above

      ((Frontend) frontend).isTesting(true);
      // set the output back to standard out for running the test
      System.setOut(standardOut);
      // same for standard in
      System.setIn(standardIn);
      if (frontend == null) {
        // test fails
        return false;
      } else {
        // test passed
        return true;
      }
    } catch (Exception e) {
      // make sure stdin and stdout are set correctly after we get exception in test
      System.setOut(standardOut);
      System.setIn(standardIn);
      e.printStackTrace();
      // test failed
      return false;
    }
  }

  /**
   * This test runs the front end and redirects its output to a string. It then passes in 'x' as a
   * command to exit the app. The test succeeds if the front end does not contain any of the three
   * movies (the movie titles are not in the string captured from the front end) by default. It
   * fails if any of those three titles are present in the string or an exception occurs.
   * 
   * @return true if the test passed, false if it failed
   */
  public boolean testFrontendInitialOutputNoMovie() {
    PrintStream standardOut = System.out;
    InputStream standardIn = System.in;
    try {
      // set the input stream to our input (with an x to test of the program exists)
      String input = "x" + System.lineSeparator();
      InputStream inputStreamSimulator = new ByteArrayInputStream(input.getBytes());
      System.setIn(inputStreamSimulator);
      ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
      // set the output to the stream captor to read the output of the front end
      System.setOut(new PrintStream(outputStreamCaptor));
      // instantiate when front end is implemented
      Object frontend = new Frontend();

      Frontend.main(new String[] {"movies.csv"});

      ((Frontend) frontend).isTesting(true);
      // set the output back to standard out for running the test
      System.setOut(standardOut);
      // same for standard in
      System.setIn(standardIn);
      String appOutput = outputStreamCaptor.toString();
      if (frontend == null || appOutput.contains("The Source of Shadows")
          || appOutput.contains("The Insurrection") || appOutput.contains("Valley Girl")) {
        // test failed
        return false;
      } else {
        // test passed
        return true;
      }
    } catch (Exception e) {
      // make sure stdin and stdout are set correctly after we get exception in test
      System.setOut(standardOut);
      System.setIn(standardIn);
      e.printStackTrace();
      // test failed
      return false;
    }
  }

  /**
   * This test runs the front end and redirects its output to a string. It then passes in 'g' as a
   * command to go to the genre selection mode. It then exists the app by pressing 'x' to go back to
   * the main mode and another 'x' to exit. The test succeeds if the genre selectio screen contains
   * all five genres from the data. It fails if any of them are missing, the front end has not been
   * instantiated (is null), or there is an exception.
   * 
   * @return true if the test passed, false if it failed
   */
  public boolean testFrontendGForGenres() {
    PrintStream standardOut = System.out;
    InputStream standardIn = System.in;
    try {
      // set the input stream to our input (with an g to test of the program lists genres)
      String input = "g" + System.lineSeparator() + "x" + System.lineSeparator() + "x"
          + System.lineSeparator();
      InputStream inputStreamSimulator = new ByteArrayInputStream(input.getBytes());
      System.setIn(inputStreamSimulator);
      ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
      // set the output to the stream captor to read the output of the front end
      System.setOut(new PrintStream(outputStreamCaptor));
      // instantiate when front end is implemented
      Object frontend = new Frontend();

      Frontend.main(new String[] {"movies.csv"}); // !!!Remove this line when you uncomment the one
                                                  // above

      ((Frontend) frontend).isTesting(true);
      // set the output back to standard out for running the test
      System.setOut(standardOut);
      // same for standard in
      System.setIn(standardIn);
      // add all tests to this method
      String appOutput = outputStreamCaptor.toString();
      if (frontend != null && appOutput.contains("Horror") && appOutput.contains("Action")
          && appOutput.contains("Comedy") && appOutput.contains("Romance")
      /* && appOutput.contains("Musical") */) { // Will be necessary to test when backend is
                                                // implemented

        // test passes if all genres from the data are displayed on the screen
        return true;
      } else {
        // test failed
        return false;
      }
    } catch (Exception e) {
      // make sure stdin and stdout are set correctly after we get exception in test
      System.setOut(standardOut);
      System.setIn(standardIn);
      e.printStackTrace();
      System.out.println("Caught an exception");
      // test failed
      return false;
    }
  }



  /**
   * This test runs the front end and redirects its output to a string. It then passes in 'r' as a
   * command to go to the rating selection mode. It then exits the app by pressing 'x' to go back to
   * the main mode and another 'x' to exit. The test succeeds if the rating selection screen
   * contains all ten possible ratings from the data. It fails if any of them are missing, the front
   * end has not been instantiated (is null), or there is an exception.
   * 
   * @return true if the test passed, false if it failed
   */
  public boolean testFrontendRForRatings() {
    PrintStream standardOut = System.out;
    InputStream standardIn = System.in;
    try {
      // set the input stream to our input (with an g to test of the program lists genres)
      String input = "r" + System.lineSeparator() + "1" + System.lineSeparator() + "2"
          + System.lineSeparator() + "3" + System.lineSeparator() + "4" + System.lineSeparator()
          + "5" + System.lineSeparator() + "6" + System.lineSeparator() + "7"
          + System.lineSeparator() + "8" + System.lineSeparator() + "9" + System.lineSeparator()
          + "10" + System.lineSeparator() + "x" + System.lineSeparator() + "x";
      InputStream inputStreamSimulator = new ByteArrayInputStream(input.getBytes());
      System.setIn(inputStreamSimulator);
      ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
      // set the output to the stream captor to read the output of the front end
      System.setOut(new PrintStream(outputStreamCaptor));
      // instantiate when front end is implemented
      Object frontend = new Frontend();

      Frontend.main(new String[] {"movies.csv"}); // !!!Remove this line when you uncomment the one
                                                  // above

      ((Frontend) frontend).isTesting(true);
      // set the output back to standard out for running the test
      System.setOut(standardOut);
      // same for standard in
      System.setIn(standardIn);
      // add all tests to this method
      String appOutput = outputStreamCaptor.toString();
      if (frontend != null && appOutput.contains("1<---") && appOutput.contains("2<---")
          && appOutput.contains("3<---") && appOutput.contains("4<---")
          && appOutput.contains("5<---") && appOutput.contains("6<---")
          && appOutput.contains("7<---") && appOutput.contains("8<---")
          && appOutput.contains("9<---") && appOutput.contains("10<---")) {
        // test passes if all ratings from the data are displayed on the screen
        return true;
      } else {
        // test failed
        return false;
      }
    } catch (Exception e) {
      // make sure stdin and stdout are set correctly after we get exception in test
      System.setOut(standardOut);
      System.setIn(standardIn);
      e.printStackTrace();
      // test failed
      return false;
    }
  }


  /**
   * This test runs the front end and redirects its output to a string. It then passes in 'r' as a
   * command to go to the rating selection mode, and then passes '2' to deselect movies with a
   * rating between 2 and 3. It then exits the app by pressing 'x' to go back to the main mode and
   * another 'x' to exit. It then removes the part of the output string from before passing '2' The
   * test succeeds if the rating selection screen contains only 1 reference to the movie with a
   * rating of 2.9 It fails if the front end has not been instantiated (is null), or there is an
   * exception.
   * 
   * @return true if the test passed, false if it failed
   */
  public boolean testFrontend2ForRatings() {
    PrintStream standardOut = System.out;
    InputStream standardIn = System.in;
    try {
      // set the input stream to our input (with an g to test of the program lists genres)
      String input = "r" + System.lineSeparator() + "2" + System.lineSeparator() + "x"
          + System.lineSeparator() + "x" + System.lineSeparator();
      InputStream inputStreamSimulator = new ByteArrayInputStream(input.getBytes());
      System.setIn(inputStreamSimulator);
      ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
      // set the output to the stream captor to read the output of the front end
      System.setOut(new PrintStream(outputStreamCaptor));
      // instantiate when front end is implemented
      Object frontend = new Frontend();

      Frontend.main(new String[] {"movies.csv"}); // !!!Remove this line when you uncomment the one
                                                  // above

      ((Frontend) frontend).isTesting(true);
      // set the output back to standard out for running the test
      System.setOut(standardOut);
      // same for standard in
      System.setIn(standardIn);
      // add all tests to this method
      String appOutput = outputStreamCaptor.toString();

      if (frontend != null && appOutput.contains("The Insurrection")) {
        // test fails if the output contains a second instance of "The Insurrection"
        return true;
      } else {
        // test passes otherwise
        return false;
      }
    } catch (Exception e) {
      // make sure stdin and stdout are set correctly after we get exception in test
      System.setOut(standardOut);
      System.setIn(standardIn);
      e.printStackTrace();
      // test failed
      return false;
    }
  }

}
