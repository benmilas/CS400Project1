// Implementation notes:
// I changed the backend to return a List<String> instead of List<MovieInterface> from the
// getThreeMovies method. When the MovieInterface get's implimented, some code will need to be
// changed
// I've marked all this code in comments, trying to give what it should be changed to
//
// Things I've Noticed: When scrolling through movies, an entry of 0 (or a selectionScroll < 0)
// displays no movies, but shows no error message. Entering a number again displays movies. Could be
// a feature?

// --== CS400 File Header Information ==--
// Name: Lwin Oo
// Email: loo@wisc.edu
// Team: KE Red
// Role: FrontEnd Developer
// TA: Keren
// Lecturer: Gary Dahl
// Notes to Grader: I didn't have that much good idea to write this. So, I did that very closely
// with Blue Team Frontend Developer


import java.util.Scanner;
import java.util.List;
import java.lang.NumberFormatException;
import java.util.concurrent.TimeUnit;

class Frontend {

  public enum Mode {
    base, genre, rating, exit
  } // Globally get and set the mode

  private Scanner scnr;
  private Mode mode;
  private String input;
  private Backend backend;

  public List<String> allGenres; // Track genres available
  public List<String> selectedGenres; // Track selected genres
  public int selectionScroll; // Index entry to scroll through returned movies (since only 3 are
                              // displayed at a time)

  public boolean[] selectedRatings; // Track selected ratings

  public boolean testing = false; // Used to not clear the screen when testing

  public static void main(String[] args) {
    args = new String[] {"movies.csv"};
    Backend backend = new Backend(args);
    Frontend frontend = new Frontend();
    frontend.run(backend);
  }

  public void run(Backend back) {
    // Initialize
    mode = Mode.base;
    scnr = new Scanner(System.in);
    if (back != null) {
      backend = back; // This will have to be changed to add constructor args when backend // is
                      // implimented
    }
    selectedRatings =
        new boolean[] {true, true, true, true, true, true, true, true, true, true, true};
    selectionScroll = -1;

    // Main functionality of program: Display message, take input, parse, repeat.
    while (mode != Mode.exit) {
      if (!testing) {
        clearScreen();
      }
      displayMessage();
      input = scnr.nextLine();
      parseInput(input.toLowerCase());
    }

  }

  public void isTesting(boolean is) {
    testing = is;
  }

  public void clearScreen() {
    for (int i = 0; i < 50; ++i)
      System.out.println();
  }

  public void displayMessage() {
    switch (mode) {
      case base:
        // Print menu text
        System.out.println("Welcome to movie program");
        System.out.println(
            "Main Menu:\nEnter a character to choose a mode:\ng: Genre Mode\nr: Rating Mode\nx: Exit\n");
        System.out.println("Or enter a number to scroll to that number movie\n");

        // Print movie list
        /* List<MovieInterface> */List<MovieInterface> movieList =
            backend.getThreeMovies(selectionScroll); // This
        // will
        // need
        // to
        // get
        // changed
        // when
        // the
        // MovieInterface
        // gets
        // implimented
        System.out.println("Movies we've found so far:");
        try {
          for (int i = 0; i < 3; i++) {
            System.out.println((selectionScroll + i + 2) + ": " + movieList.get(i)); // Will need to
            // be
            // changed to:
            // System.out.println(selectionScroll
            // +
            // movieList.get(i).getTitle());
            // or however it's
            // needed to display
            // selectionScroll+i +1 is so that movie indexing starts at 1 not 0
          }
        } catch (Exception excpt) {
        }

        break;

      case genre:
        // Print menu text
        System.out.println("Genre Selection Mode");
        System.out.println("Selected genres are indicated by an arrow");

        // Print genre options
        allGenres = backend.getAllGenres();
        selectedGenres = backend.getGenres();
        for (int i = 0; i < allGenres.size(); i++) {
          if (selectedGenres.contains(allGenres.get(i))) {
            System.out.println(i + ": " + allGenres.get(i) + "<---");
          } else {
            System.out.println(i + ": " + allGenres.get(i));
          }
        }

        break;

      case rating:
        // Print menu text
        System.out.println("Rating Selection Mode");
        System.out.println("Selected ratings are indicated by an arrow");

        // Print rating options
        for (int i = 0; i <= 10; i++) {
          if (!selectedRatings[i]) {
            System.out.println(i + "<---");
          } else {
            System.out.println(i);
          }
        }

        break;

    }

    return;
  }

  public void parseInput(String in) {

    switch (mode) {
      case base:
        baseMode(in);
        break;
      case genre:
        genreMode(in);
        break;
      case rating:
        ratingMode(in);
        break;

    }
  }


  public void baseMode(String in) {

    // Parse text options first
    if (in.charAt(0) == 'x') {
      mode = Mode.exit;
      return;
    }
    if (in.charAt(0) == 'g') {
      mode = Mode.genre;
      return;
    }
    if (in.charAt(0) == 'r') {
      mode = Mode.rating;
      return;
    }

    // Parse numerical options
    int selection;
    try {
      selection = Integer.parseInt(in); // Will throw a NumberFormatException if letters were
                                        // entered
      if (selection > backend.getNumberOfMovies() + 1) { // Again, so that indexing can start at 1
                                                         // not 0
        throw new NumberFormatException();
      }
      selectionScroll = selection - 1; // Set value to be used next time menu text is printed (which
                                       // happens when this returns) Must be -1 so movie list can
                                       // start at 1 instead of 0
    } catch (NumberFormatException excpt) {
      System.out
          .println("Not that many movies fit your criteria! Please enter a small integer number");
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (Exception excp) {
      }
      return;
    }

    return;
  }

  public void genreMode(String in) {

    if (in.charAt(0) == 'x') {
      mode = Mode.base;
      return;
    }
    int selection;
    try {
      selection = Integer.parseInt(in); // Will throw a NumberFormatException if letters were
                                        // entered
      if (selection > allGenres.size()) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException excpt) {
      System.out.println("Please enter a valid number");
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (Exception excp) {
      }
      return;
    }
    if (selectedGenres.contains(allGenres.get(selection))) {
      backend.removeGenre(allGenres.get(selection));
    } else {
      backend.addGenre(allGenres.get(selection));
    }
  }

  public void ratingMode(String in) {
    if (in.charAt(0) == 'x') {
      mode = Mode.base;
      return;
    }

    int selection;
    try {
      selection = Integer.parseInt(in); // Will throw a NumberFormatException if non-integer values
                                        // are entered
      if (selection > 10 || selection < 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException excpt) {
      System.out.println("Please enter an integer between 0 and 10");
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (Exception excp) {
      }
      return;
    }
    selectedRatings[selection] = !selectedRatings[selection];
    if (!selectedRatings[selection])
      backend.addAvgRating(in);
    else
      backend.removeAvgRating(in);;
  }

}
