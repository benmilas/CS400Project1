// --== CS400 File Header Information ==--
// Name: Yash Butani
// Email: butani@wisc.edu
// Team: KE Red
// Role: Backend Developer
// TA: Keren Chen
// Lecturer: Gary Dahl
// Notes to Grader: Ben Milas and Leo Alfaro ended up doing most of the work on this class, as
// Yash's code was late and unusable because it failed to follow the project outline of using our
// implemented HashTables from last week. This was extremely frustrating and time consuming for both
// Integration Managers. That also explains the lack of comments; we simply didn't have enough time
// to rigorously test & comment this code.

import java.util.List;
import java.util.zip.DataFormatException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;

public class Backend implements BackendInterface {

  HashTableMap<String, List<MovieInterface>> genre = new HashTableMap<>();
  HashTableMap<String, List<MovieInterface>> ratings = new HashTableMap<>();
  List<MovieInterface> movies = new ArrayList<MovieInterface>();
  List<MovieInterface> genreMovies = new ArrayList<MovieInterface>();
  List<MovieInterface> ratingsMovies = new ArrayList<MovieInterface>();
  List<MovieInterface> topMovies = new ArrayList<MovieInterface>();

  public Backend(Reader reader) {
    MovieDataReader movieReader = new MovieDataReader();
    try {
      movies = movieReader.readDataSet(reader);
    } catch (IOException | DataFormatException e) {
      e.printStackTrace();
    }
  }

  public Backend(String[] args) {
    FileReader fr;
    try {
      fr = new FileReader(new File(args[0]));
      MovieDataReader movieReader = new MovieDataReader();
      movies = movieReader.readDataSet(fr);
    } catch (IOException | DataFormatException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addGenre(String genre) {
    ArrayList<MovieInterface> addGenre = new ArrayList<MovieInterface>();
    for (int i = 0; i < movies.size(); i++) {
      if (movies.get(i).getGenres().contains(genre) && !genreMovies.contains(movies.get(i))) {
        addGenre.add(movies.get(i));
        genreMovies.add(movies.get(i));
      }
    }
    this.genre.put(genre, addGenre);
  }

  @Override
  public void addAvgRating(String rating) {
    int intRating = Integer.parseInt(rating);
    ArrayList<MovieInterface> addAvgRating = new ArrayList<MovieInterface>();
    for (int i = 0; i < movies.size(); i++) {
      if (movies.get(i).getAvgVote() >= intRating && movies.get(i).getAvgVote() < intRating + 1
          && !ratingsMovies.contains(movies.get(i))) {
        addAvgRating.add(movies.get(i));
        ratingsMovies.add(movies.get(i));
      }
    }
    ratings.put(rating, addAvgRating);
  }

  @Override
  public void removeGenre(String genre) {
    if (this.genre.containsKey(genre)) {
      this.genre.remove(genre);
    }
  }

  @Override
  public void removeAvgRating(String rating) {
    int intRating = Integer.parseInt(rating);
    if (ratings.containsKey(rating)) {
      ratings.remove(rating);
      for (int i = ratingsMovies.size() - 1; i >= 0; i--) {
        if (ratingsMovies.get(i).getAvgVote() >= intRating
            && ratingsMovies.get(i).getAvgVote() < intRating + 1)
          ratingsMovies.remove(i);
      }
    }
  }

  @Override
  public List<String> getGenres() {
    for (int i = 0; i < genre.getKeys().size(); i++) {
      this.addGenre(genre.getKeys().get(i));
    }
    return genre.getKeys();
  }

  @Override
  public List<String> getAvgRatings() {
    return ratings.getKeys();
  }

  @Override
  public int getNumberOfMovies() {
    ArrayList<MovieInterface> moviesToAdd = new ArrayList<MovieInterface>();
    ArrayList<MovieInterface> moviesToRemove = new ArrayList<MovieInterface>();

    if (genre.size() == 0 && ratings.size() == 0) {
      topMovies = new ArrayList<MovieInterface>();
      return movies.size();
    }

    if (genreMovies.size() == 0 && genre.size() == 0) {
      if (ratings.size() == 0)
        topMovies = movies;
      else
        topMovies = ratingsMovies;

      return topMovies.size();
    }

    if (ratingsMovies.size() == 0 && ratings.size() == 0) {
      topMovies = genreMovies;
      for (MovieInterface m : topMovies) {
        for (int i = 0; i < genre.getKeys().size(); i++) {
          if (!m.getGenres().contains(genre.getKeys().get(i)))
            moviesToRemove.add(m);
        }
      }
      for (MovieInterface m : moviesToRemove) {
        topMovies.remove(m);
      }
      return topMovies.size();
    } else {
      topMovies = new ArrayList<MovieInterface>();
      for (MovieInterface m : ratingsMovies) {
        if (genreMovies.contains(m)) {
          moviesToAdd.add(m);
        }
      }
      for (MovieInterface m : moviesToAdd) {
        topMovies.add(m);
      }

      for (MovieInterface m : topMovies) {
        for (int i = 0; i < genre.getKeys().size(); i++) {
          if (!m.getGenres().contains(genre.getKeys().get(i)))
            moviesToRemove.add(m);
        }
      }
      for (MovieInterface m : moviesToRemove) {
        topMovies.remove(m);
      }
      return topMovies.size();
    }
  }

  @Override
  public List<String> getAllGenres() {
    List<String> allGenres = new ArrayList<String>();
    for (int i = 0; i < movies.size(); i++) {
      for (int j = 0; j < movies.get(i).getGenres().size(); j++) {
        if (!allGenres.contains(movies.get(i).getGenres().get(j)))
          allGenres.add(movies.get(i).getGenres().get(j));
      }
    }
    return allGenres;
  }

  @Override
  public List<MovieInterface> getThreeMovies(int startingIndex) {
    int numMovies = getNumberOfMovies();
    if (topMovies.size() == 0)
      return new ArrayList<MovieInterface>();
    Collections.sort(topMovies);

    List<MovieInterface> topThree = new ArrayList<MovieInterface>();
    for (int i = 0; i < Math.min(numMovies, 3); i++) {
      topThree.add(topMovies.get(i));
    }
    return topThree;
  }
}
