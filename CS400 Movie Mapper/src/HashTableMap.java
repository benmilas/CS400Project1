// --== CS400 File Header Information ==--
// Name: Ben Milas
// Email: bmilas@wisc.edu
// Team: Red
// Group: KE
// TA: Keren Chen
// Lecturer: Gary Dahl
// Notes to Grader: I added getKeys() and getValues() methods, even though we weren't supposed to
// edit the HashTableMap class. We couldn't get our Backend to function because Yash's code was made
// for an imported HashMap, which relied on methods similar to those. A quick fix for a big problem.

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Represents a hash table which stores a collection of generic KeyTypes and ValueTypes that
 * implements the MapADT to efficiently store and find data
 * 
 * @author Ben Milas
 *
 */
public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
  private LinkedList<HashNode>[] array; // array of linked lists which store keys and values
  private int capacity;
  private int size;
  private double loadFactor;
  private final int DEFAULT_HASHTABLE_CAPACITY = 10;
  private final double MAX_LOAD_FACTOR = 0.85;

  /**
   * A private wrapper class that holds the KeyType, ValueType, and next HashNode associated with
   * each HashTable entry to provide LinkedList functionality.
   *
   */
  private class HashNode {
    private KeyType key;
    private ValueType value;
    private HashNode next;

    /**
     * Constructor that builds an entry for the LinkedList that holds the KeyType, ValueType, and
     * next HashNode in the LinkedList
     * 
     * @param key
     * @param value
     * @param next
     */
    public HashNode(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }
  }

  /**
   * Constructor that initializes the hash table with a chosen, positive capacity
   * 
   * @param capacity the max indices of the hash table
   */
  public HashTableMap(int capacity) {
    if (capacity < 1)
      throw new IllegalArgumentException("Capacity must be positive");

    array = (LinkedList<HashNode>[]) new LinkedList[capacity];
    this.capacity = capacity;
    size = 0;
    this.loadFactor = size / (double) this.array.length;
  }

  /**
   * Constructor that initializes the hash table with a default capacity of 10
   */
  public HashTableMap() {
    array = (LinkedList<HashNode>[]) new LinkedList[DEFAULT_HASHTABLE_CAPACITY];
    capacity = DEFAULT_HASHTABLE_CAPACITY;
    size = 0;
    this.loadFactor = size / (double) this.array.length;
  }

  /**
   * Inserts a key-value pair into the hash table if the key is not null or already in use
   * 
   * @param key   the key associated with the HashNode
   * @param value the value associated with the HashNode
   * @return true if the HashNode was successfully inserted, false otherwise
   */
  @Override
  public boolean put(KeyType key, ValueType value) {
    // ensures the key does not exist and is not null
    if (key != null && !containsKey(key)) {
      int index = Math.abs(key.hashCode() % this.array.length);
      HashNode add = new HashNode(key, value);
      // first in the list
      if (array[index] == null) {
        array[index] = new LinkedList<HashNode>();
        array[index].addFirst(add);
      } else {
        array[index].add(add);
      }
      // update size and loadFactor
      size++;
      this.loadFactor = size / (double) this.array.length;
      // check if resize is needed
      if (loadFactor >= MAX_LOAD_FACTOR)
        this.rehash();

      return true;
    }
    return false;
  }

  /**
   * Getter method that returns the value associated with a specific search key if it is found
   * 
   * @param key the key that is being searched for
   * @return the ValueType associated with a key, if that key exists within the hash table
   * @throws NoSuchElementException if the key is not contained within the hash table
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    if (this.containsKey(key)) {
      int index = Math.abs(key.hashCode() % this.array.length);
      for (int i = 0; i < array[index].size(); i++) {
        if (array[index].get(i).key.equals(key))
          return (ValueType) array[index].get(i).value;
      }
    }
    throw new NoSuchElementException("Key was not found");
  }

  /**
   * Returns the number of key-value pairs stored in this collection
   * 
   * @return the number of key-value pairs stored in this collection
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Checks whether a key is contained within the hash table by using the get() function.
   * 
   * @return true if the key is contained within the hash table, false otherwise
   */
  @Override
  public boolean containsKey(KeyType key) {
    if (size == 0)
      return false;
    if (key == null)
      return false;
    int index = Math.abs(key.hashCode() % array.length);
    if (array[index] != null) {
      for (int i = 0; i < array[index].size(); i++) {
        if (array[index].get(i).key.equals(key))
          return true;
      }
    }
    return false;
  }

  /**
   * Removes an element from the hash table by searching for its key
   * 
   * @return the ValueType associated with the key that was removed from the hash table, or null if
   *         the key is null or doesn't exist within the hash table
   */
  @Override
  public ValueType remove(KeyType key) {
    int index = Math.abs(key.hashCode() % array.length);
    if (array[index] == null)
      return null;
    for (int i = 0; i < array[index].size(); i++) {
      if (array[index].get(i).key.equals(key)) {
        ValueType temp = (ValueType) array[index].get(i).value;
        array[index].remove(i);
        this.size--;
        this.loadFactor = size / (double) this.array.length;
        return temp;
      }
    }
    return null;
  }


  /**
   * Clears out the array by setting all the indices to null, and resetting the size to 0
   */
  @Override
  public void clear() {
    for (int i = 0; i < capacity; i++) {
      if (array[i] != null)
        array[i] = null;
    }
    size = 0;
  }

  /**
   * Returns the capacity of the hash table
   * 
   * @return the capacity of the hash table
   */
  public int getCapacity() {
    return capacity;
  }

  /**
   * Returns the keys contained within the hash table
   * 
   * @return a List of the KeyTypes within the table
   */
  public ArrayList<KeyType> getKeys() {
    ArrayList<KeyType> keys = new ArrayList<KeyType>();
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null) {
        for (int j = 0; j < array[i].size(); j++) {
          keys.add((KeyType) array[i].get(j).key);
        }
      }
    }
    return keys;
  }

  /**
   * Returns the values contained within the hash table
   * 
   * @return a List of the ValueTypes within the table
   */
  public ArrayList<ValueType> getValues() {
    ArrayList<ValueType> values = new ArrayList<ValueType>();
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].size(); j++) {
        values.add((ValueType) array[i].get(j).value);
      }
    }
    return values;
  }

  /**
   * Makes a copy of the old array of values, and rehashes them over to a new array with double the
   * capacity
   */
  private void rehash() {
    LinkedList<HashNode>[] oldArray = array;
    array = (LinkedList<HashNode>[]) new LinkedList[capacity * 2];
    int oldArraySize = size();
    size = 0;
    capacity *= 2;

    for (int i = 0; i < oldArraySize; i++) {
      // avoid empty array indices
      if (oldArray[i] == null)
        continue;

      HashNode currentNode = oldArray[i].get(0);
      // rehashes all the elements from the linked list associated with the index, if applicable
      while (currentNode != null) {
        KeyType key = currentNode.key;
        ValueType value = currentNode.value;

        put(key, value);
        currentNode = currentNode.next;
      }
    }
  }

  /**
   * Gets the index representation of the key's positive hashCode modulo the array's capacity
   * 
   * @param key the key being converted to an index
   * @return the index representation of the key
   */
  private int getIndex(KeyType key) {
    return Math.abs(key.hashCode()) % capacity;
  }

  /**
   * Checks whether the array is too full, and if a rehash is necessary
   * 
   * @return true if the loadFactor is greater than the MAX_LOAD_FACTOR, false otherwise
   */
  private boolean isArrayTooFull() {
    double loadFactor = size / (double) capacity;
    if (Double.compare(loadFactor, MAX_LOAD_FACTOR) >= 0)
      return true;

    return false;
  }

}
