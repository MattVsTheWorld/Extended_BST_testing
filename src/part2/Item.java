package part2;
import java.io.*;

public class Item implements Comparable<Item>, Serializable {
	
	/**
	 * Version number - serialized instances of this class will have this
	 * identifier; deserializing an object whose version number is different
	 * from the loader's will cause a InvalidClassException.
	 */
	private static final long serialVersionUID = 3910673513921284910L;

	/**
	 * Provides an immutable integer valued item that counts comparisons.
	 */
	
	// Counter that is incremented each time the compareTo() or equals()
    	// method of an instance of the class is called
	private transient static long compCount = 0;
	
	// The integer value of this Item
	private final Integer value;
	
	/**  
	 * Constructor - creates an Item and sets its value
	 * @param value - the value for the Item
	 */
	public Item(Integer value) {
		this.value = value;
	}
	
	/**
	 * The value of this Item
	 * @return the Item's value
	 */
	public Integer value() {
		return value;
	}
	
	/**
	 * Compares the value of this Item with that of other according to
	 * the contract for Comparable.
	 * Increments the count of comparisons.
	 */
	@Override
	public int compareTo(Item other) {
		compCount++;
		return value.compareTo(other.value);
	}
	
	/**
	 * Returns the total number of comparisons performed on instances of 
	 * type Item since the counter was last reset (or the total if it has  
	 * never been reset).
	 * @return the count of calls to compareTo() for type Item
	 */
	public static long getCompCount() {
		return compCount;
	}
	
	/**
	 * Resets the counter for the number of comparisons to zero.
	 */
	public static void resetCompCount() {
		compCount = 0;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public boolean equals(Object that) {
		compCount++;
		if (this == that) return true;
		if (!(that instanceof Item)) return false;
		Item other = (Item)that;
		return value.equals(other.value);
	}
	
	/** 
	 * hashCode can be modified to any value to cause HashSet to have
	 * a high number of collisions.
	 * 
	 */	
	@Override
	public int hashCode() {
		return value.hashCode();
		// return 8;
	}
	
}
