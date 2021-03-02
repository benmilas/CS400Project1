// --== CS400 File Header Information ==--
// Name: Brian Castellano
// Email: bkcastellano@wisc.edu
// Team: KE Red
// Role: Data Wrangler
// TA: Keren Chen
// Lecturer: Gary Dahl
// Notes to Grader: N/A

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

public class MovieDataReader implements MovieDataReaderInterface {

  @Override
  public List<MovieInterface> readDataSet(Reader inputFileReader)
      throws IOException, DataFormatException {

    // Checks if the file input actually exists
    if (inputFileReader == null) {
      throw new FileNotFoundException("The file was not found");
    }

    // An array that stores all of the movies, and will be returned at the end
    List<MovieInterface> movies = new ArrayList<MovieInterface>();

    // Mostly for debugging. Can be deleted later
    int line = 1;

    try {
      // Using BufferedReader to process the CSV file
      BufferedReader b = new BufferedReader(inputFileReader);

      String formattingLine = b.readLine(); // Pulls the formatting which will indicate which column
                                            // is which
      // System.out.println(formattingLine); // **DEBUG**
      Object[] format = formattingLine.split(","); // Puts all the categories into an ArrayList

      // Indexes for the relevant categories. Will e initialized in the following for loop
      int ti = -1;
      int yi = -1;
      int gi = -1;
      int diri = -1;
      int desi = -1;
      int vi = -1;
      for (int i = 0; i < format.length; i += 1) { // Finds and saves the indexes of relevant
                                                   // categories
        if (format[i].equals((String) "title")) {
          ti = i;
        }
        if (format[i].equals((String) "year")) {
          yi = i;
        }
        if (format[i].equals((String) "genre")) {
          gi = i;
        }
        if (format[i].equals((String) "director")) {
          diri = i;
        }
        if (format[i].equals((String) "description")) {
          desi = i;
        }
        if (format[i].equals((String) "avg_vote")) {
          vi = i;
        }
      }

      String read = b.readLine(); // Starts at the first movie, will be iterated later

      while (read != null) { // Stops when the line is null, indicating the end of the CSV
        Object[] data = read.split(","); // Takes the line and splits it at each comma

        for (int i = 0; i < data.length - 1; i += 1) {

          // Makes sure the split didn't render a blank String
          if (((String) data[i + 1]).length() != 0) {

            /*
             * Exploits the formatting, any comma with a space afterwards indicates that the two
             * Strings belong to the same category
             */
            if (((String) data[i + 1]).charAt(0) == ' ') {
              data[i] = (String) data[i] + "," + (String) data[i + 1]; // Combines the Strings
              // Creates a new array with the fixed Strings
              Object[] temp = new Object[data.length - 1];
              for (int j = 0; j <= i; j += 1) {
                temp[j] = data[j];
              }
              for (int j = i + 1; j < data.length - 1; j += 1) {
                temp[j] = data[j + 1];
              }
              data = temp;
              i -= 1;
            }
          }
        }

        // There should be 13 columns (and therefore 13 nodes in the array)
        if (data.length != 13) {
          throw new DataFormatException("There is a data format problem on line " + line);
        }

        /*
         * Assigns each relevant data point to a variable. This just cleans up the initialization in
         * a few lines
         */
        String t = (String) data[ti];
        int y = Integer.parseInt((String) data[yi]);
        String g = (String) data[gi];
        // quick patchwork to remove quotes
        g = g.replaceAll("\"", "");
        String dir = (String) data[diri];
        String des = (String) data[desi];
        float v = Float.parseFloat((String) data[vi]);

        // Creates a new movie object with the relevant information
        Movie m = new Movie(t, y, g, dir, des, v);
        movies.add(m);
        line += 1;

        // System.out.println(t); // **DEBUG**
        read = b.readLine();
      }

      // System.out.println("Number of movies: " + movies.size()); // **DEBUG**
    }

    catch (Exception e) {
      e.printStackTrace();
      throw new IOException("A problem while reading the file at movie " + line);
    }

    return movies;
  }

}
