// --== CS400 File Header Information ==--
// Name: Brian Castellano
// Email: bkcastellano@wisc.edu
// Team: KE Red
// Role: Data Wrangler
// TA: Keren Chen
// Lecturer: Gary Dahl
// Notes to Grader: N/A

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Movie implements MovieInterface {

  private String title;
  private int year;
  private String genres;
  private String director;
  private String description;
  private float vote;

  // Creates a new Movie object and initializes its variables
  public Movie(String t, int y, String g, String dir, String des, float v) {
    this.title = t;
    this.year = y;
    this.genres = g;
    this.director = dir;
    this.description = des;
    this.vote = v;
  }

  @Override
  public String getTitle() {
    return this.title.trim();
  }

  @Override
  public Integer getYear() {
    return this.year;
  }

  @Override
  public List<String> getGenres() {

    // Puts the genres String into a scanner to be divided up
    Scanner s = new Scanner(this.genres);
    s.useDelimiter(",");

    List<String> g = new ArrayList<String>();

    // Splits the genres by commas and adds them to the List
    while (s.hasNext()) {
      g.add(s.next().trim());
    }
    s.close();

    return g;
  }

  @Override
  public String getDirector() {
    return this.director.trim();
  }

  @Override
  public String getDescription() {
    return this.description.trim();
  }

  @Override
  public Float getAvgVote() {
    return this.vote;
  }

  /**
   * Compares two Movie objects to each other using their average votes
   * 
   * @return 1 if this average vote is less than the other, 0 if they are equal, -1 if this average
   *         vote is greater than the other
   */
  @Override
  public int compareTo(MovieInterface otherMovie) {

    if (this.getAvgVote() == otherMovie.getAvgVote()) {
      return 0;
    } else if (this.getAvgVote() < otherMovie.getAvgVote()) {
      return 1;
    } else {
      return -1;
    }

  }

  // Formatting can be changed if needed
  @Override
  public String toString() {
    return "Average vote: " + this.getAvgVote() + "\tTitle: " + this.getTitle() + "\tYear: "
        + this.getYear() + "\tGenre(s): " + this.getGenres() + "\tDirector(s): "
        + this.getDirector() + "\tDescription: " + this.getDescription();
  }

}
