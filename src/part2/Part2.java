package part2;

import java.util.*;
import java.io.*;

/**
 * @author B00273607
 */

public class Part2 {

	/**
	 * An application that tests performances of the Extended Binary Search Tree, TreeSet and HashSet
	 * Also tests Extended BST serialization
	 * 	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		final String START = "Multiple data structure perfromance test started.\n";
		final String START_QUESTION = "Please select a test to run: ";
		final String SELECTION1 = "1 - |Data structure comparison test|"
				+ "\n    Create an Extended Binary Search Tree, TreeSet and HashSet and test"
				+ "\n    their performance before and after a large number of removals and"
				+ "\n    additions (final size of datasets remains the same). Also serializes"
				+ "\n    final tree and checks serialization was successful by loading the object.";
		final String SELECTION2 = "2 - |Serialization test|"
				+ "\n    A short test that serializes a tree of Integers and "
				+ "\n    deserializes it to prove the data has been preserved.";
		final String SELECTION3 = "3 - |TreeSet linear test|"
				+ "\n    A test to investigate performance of red black trees (TreeSet) when "
				+ "\n    adding Integers in ascending order.";
		System.out.print(START);
		System.out.println(START_QUESTION);
		System.out.println(SELECTION1);
		System.out.println(SELECTION2);
		System.out.println(SELECTION3);
		System.out.print("Your choice: ");
		try (Scanner keyboard = new Scanner(System.in)) {
			int choice = keyboard.nextInt();
			switch (choice) {
			case 1:
				multiComparisonTest();
				break;
			case 2:
				serializationTest();
				break;
			case 3:
				redBlackStressTest();

			}
		}
	}


	private static long height(TreeSet<Item> tree) {
		long maxComp = 0;
		for (Item current : tree) {
			Item.resetCompCount();
			tree.contains(current);
			if (maxComp < Item.getCompCount()) {
				maxComp = Item.getCompCount();
			}
		}
		return maxComp - 1;
	}

	private static void additionTest(int size, BinarySearchTree<Item> bst, List<Item> dataSet, TreeSet<Item> redBlack,
			HashSet<Item> hashTable) {

		/// --------------------------------------
		/// Add odd elements in range
		System.out.println("\n||| Adding to data structures |||");
		int count = 1;
		for (count = 1; count <= size * 2; count += 2) {
			Item candidate = new Item(count);
			dataSet.add(candidate);
		}
		Item.resetCompCount();
		Collections.shuffle(dataSet);
		
		System.out.println("Performing addition in random order.");
		/// --------------------------------------
		System.out.println("\n--- Extended Binary Search Tree ---");
		for (count = 0; count < dataSet.size(); count++) {
			Item candidate = new Item(dataSet.get(count).value());
			bst.add(candidate);
		}
		System.out.printf("Added %d elements to the Binary Search Tree (odd from 1 to %d, in random order).\n", bst.size(), count * 2);
		System.out.printf("The number of comparisons after adding is %d \n", Item.getCompCount());
		System.out.println("Height of the Extended Binary Search Tree: " + bst.findIterativeHeight());
		System.out.println("Number of leaves: " + bst.leaves());
		System.out.println("Internal path length: " + bst.InternalPathLength());
		System.out.println("External path length: " + (bst.InternalPathLength() + 2 * bst.size())); // As E = I + 2n

		Item.resetCompCount();
		/// --------------------------------------
		System.out.println("\n--- Red Black Tree (TreeSet) ---");
		for (count = 0; count < dataSet.size(); count++) {
			Item candidate = new Item(dataSet.get(count).value());
			redBlack.add(candidate);
		}
		System.out.printf("Added %d elements to the TreeSet (odd from 1 to %d, in random order).\n", redBlack.size(), count * 2);
		System.out.printf("The number of comparisons after adding is %d \n", Item.getCompCount());
		System.out.println("Height of the TreeSet: " + height(redBlack));
		
		Item.resetCompCount();
		/// --------------------------------------
		System.out.println("\n--- Hash Table (HashSet) ---");
		for (count = 0; count < dataSet.size(); count++) {
			Item candidate = new Item(dataSet.get(count).value());
			hashTable.add(candidate);
		}
		System.out.printf("Added %d elements to the HashSet(odd from 1 to %d, in random order).\n", hashTable.size(), count * 2);
		System.out.printf("The number of comparisons after adding is %d \n", Item.getCompCount());

		Item.resetCompCount();
	}

	private static void displayInfo(int size, BinarySearchTree<Item> bst, List<Item> dataSet, TreeSet<Item> redBlack,
			HashSet<Item> hashTable) {
		/// --------------------------------------
		System.out.println("\n||| Displaying info of data structures |||");
		System.out.println("--- Extended Binary Search Tree ---");
		System.out.println("The size of the Extended Binary Search Tree is: " + bst.size());
		System.out.println("Height of the Extended Binary Search Tree: " + bst.findIterativeHeight());
		System.out.println("Number of leaves: " + bst.leaves());
		System.out.println("Internal path length: " + bst.InternalPathLength());
		System.out.println("External path length: " + (bst.InternalPathLength() + 2 * bst.size())); // As E = I + 2n
		Item.resetCompCount();
		/// --------------------------------------
		System.out.println("\n--- Red Black Tree (TreeSet) ---");
		System.out.println("The size of the TreeSet is: " + redBlack.size());
		System.out.println("Height of the TreeSet: " + height(redBlack));
		Item.resetCompCount();
		/// --------------------------------------
		System.out.println("\n--- Hash Table (HashSet) ---");
		System.out.println("The size of the HashSet is: " + hashTable.size());
		Item.resetCompCount();
	}

	private static void searchTest(int size, BinarySearchTree<Item> bst, TreeSet<Item> redBlack,
			HashSet<Item> hashTable) {

		int searchElNo = 10;
		
		if(searchElNo > size) //To avoid infinite loops in smaller sizes
			searchElNo = size;
		System.out.println("\n||| Searching elements in data structures |||");
		
		// Create 10 (or searchElNo) candidates and search for them
		List<Item> successfulSearchItems = new LinkedList<>();
		System.out.println("Generating random successful search candidates...");
		Random rnd = new Random();
		for (int i = 0; i < searchElNo; ++i) {
			int randomInt = rnd.nextInt(size * 2) + 1;
			while (successfulSearchItems.contains(new Item(randomInt)) || (randomInt % 2 == 0))
				randomInt = rnd.nextInt(size * 2) + 1;

			successfulSearchItems.add(new Item(randomInt));
		}
		System.out.printf("Generated %d candidates. Candidates are: ", successfulSearchItems.size());
		if (successfulSearchItems.size() <= 25){
			for (int i = 0; i < successfulSearchItems.size(); i++) {
				System.out.printf("%d ", successfulSearchItems.get(i).value());
			}
		} else System.out.print("(list too long...)");

		List<Item> unsuccessfulSearchItems = new LinkedList<>();
		System.out.println("\n\nGenerating random unsuccessful search candidates...");
		Random rnd_un = new Random();
		for (int i = 0; i < searchElNo; ++i) {
			int randomInt = rnd_un.nextInt(size * 2) + 1;
			while (unsuccessfulSearchItems.contains(new Item(randomInt)) || (randomInt % 2 != 0))
				randomInt = rnd_un.nextInt(size * 2) + 1;

			unsuccessfulSearchItems.add(new Item(randomInt));
		}
		System.out.printf("Generated %d candidates. Candidates are: ", unsuccessfulSearchItems.size());
		if (unsuccessfulSearchItems.size() <= 25){
			for (int i = 0; i < unsuccessfulSearchItems.size(); i++) {
				System.out.printf("%d ", unsuccessfulSearchItems.get(i).value());
			}
		} else System.out.print("(list too long...)");

		Item.resetCompCount(); 
		float averageComps = 0;

		/// --------------------------------------
		/// Search in Extended Binary Search Tree
		/// --------------------------------------
		System.out.println("\n\n--- Extended Binary Search Tree ---");
		System.out.printf("Successfully searching %d elements.\n", searchElNo);
		for (int i = 0; i < successfulSearchItems.size(); i++) {
			bst.contains(successfulSearchItems.get(i));
			averageComps += Item.getCompCount();
			Item.resetCompCount();
		}
		System.out.printf("The average number of comparisons: %.2f \n",
				(averageComps / successfulSearchItems.size()));
		Item.resetCompCount();

		System.out.printf("Unsuccessfully searching %d elements.\n", searchElNo);
		averageComps = 0;

		for (int i = 0; i < unsuccessfulSearchItems.size(); i++) {
			bst.contains(unsuccessfulSearchItems.get(i));
			averageComps += Item.getCompCount();
			Item.resetCompCount();
		}
		System.out.printf("The average number of comparisons: %.2f \n",
				(averageComps / unsuccessfulSearchItems.size()));
		Item.resetCompCount();
		
		/// --------------------------------------
		/// Search in TreeSet
		/// --------------------------------------
		System.out.println("\n--- Red Black Tree (TreeSet) ---");
		System.out.printf("Successfully searching %d elements.\n", searchElNo);
		averageComps = 0;
		for (int i = 0; i < successfulSearchItems.size(); i++) {
			redBlack.contains(successfulSearchItems.get(i));
			averageComps += Item.getCompCount();
			Item.resetCompCount();
		}
		System.out.printf("The average number of comparisons: %.2f \n",
				(averageComps / successfulSearchItems.size()));
		Item.resetCompCount();

		System.out.printf("Unsuccessfully searching %d elements.\n", searchElNo);
		averageComps = 0;
		for (int i = 0; i < unsuccessfulSearchItems.size(); i++) {
			redBlack.contains(unsuccessfulSearchItems.get(i));
			averageComps += Item.getCompCount();
			Item.resetCompCount();
		}
		System.out.printf("The average number of comparisons: %.2f \n",
				(averageComps / unsuccessfulSearchItems.size()));
		Item.resetCompCount();
		
		/// --------------------------------------
		/// Search in HashSet
		/// --------------------------------------
		System.out.println("\n--- Hash Table (HashSet) ---");
		System.out.printf("Successfully searching %d elements.\n", searchElNo);
		averageComps = 0;
		for (int i = 0; i < successfulSearchItems.size(); i++) {
			hashTable.contains(successfulSearchItems.get(i));
			averageComps += Item.getCompCount();
			Item.resetCompCount();
		}
		System.out.printf("The average number of comparisons: %.2f \n",
				(averageComps / successfulSearchItems.size()));
		Item.resetCompCount();

		System.out.printf("Unsuccessfully searching %d elements.\n", searchElNo);
		averageComps = 0;
		for (int i = 0; i < unsuccessfulSearchItems.size(); i++) {
			hashTable.contains(unsuccessfulSearchItems.get(i));
			averageComps += Item.getCompCount();
			Item.resetCompCount();
		}
		System.out.printf("The average number of comparisons: %.2f \n",
				(averageComps / unsuccessfulSearchItems.size()));
		Item.resetCompCount();
	}

	private static void removeAddFifth(int size, BinarySearchTree<Item> bst, TreeSet<Item> redBlack,
			HashSet<Item> hashTable) {

		// generate 1/5th of size candidates to remove, shuffle and re-add
		List<Item> removalCandidates = new LinkedList<>();
		Random rnd_r = new Random();
		for (int i = 0; i < (size / 5); ++i) {
			int randomInt = rnd_r.nextInt(size * 2) + 1;
				while (removalCandidates.contains(new Item(randomInt)) || (randomInt % 2 == 0))
					randomInt = rnd_r.nextInt(size * 2) + 1;

			removalCandidates.add(new Item(randomInt));
		}

		int errorCount = 0;
		for (Item cand : removalCandidates) {
			if (!bst.remove(cand))
				errorCount++;
			if (!redBlack.remove(cand))
				errorCount++;
			if (!hashTable.remove(cand))
				errorCount++;
		}
		// Debug messages
		// ---------------------
		//System.out.printf("Removed elements with %d errors.\n", errorCount);
		//System.out.printf("BST size is now %d.\n", bst.size());
		//System.out.printf("RBT size is now %d.\n", redBlack.size());
		//System.out.printf("HT size is now %d.\n", hashTable.size());
		// ---------------------
		Collections.shuffle(removalCandidates);
		// ---------------------
		//Collections.shuffle(removalCandidates);
		//Collections.shuffle(removalCandidates);
		// ---------------------
		for (Item cand : removalCandidates) {
			if (!bst.add(cand))
				errorCount++;
			if (!redBlack.add(cand))
				errorCount++;
			if (!hashTable.add(cand))
				errorCount++;
		}
		// Debug messages
		// ---------------------
		//System.out.printf("Added elements with %d errors.\n", errorCount);
		//System.out.printf("BST size is now %d.\n", bst.size());
		//System.out.printf("RBT size is now %d.\n", redBlack.size());
		//System.out.printf("HT size is now %d.\n", hashTable.size());
		// ---------------------

	}

	public static void multiComparisonTest() {
		BinarySearchTree<Item> bst = new BinarySearchTree<>();
		List<Item> dataSet = new LinkedList<>();
		TreeSet<Item> redBlack = new TreeSet<>();
		HashSet<Item> hashTable = new HashSet<>();

		System.out.print("Please enter the size of the data set: ");

		try (Scanner keyboard = new Scanner(System.in)) {
			int size = keyboard.nextInt();
			System.out.printf("The size of the dataSet for this run is %d \n", size);
			System.out.printf("The number of computations at start is %d \n", Item.getCompCount());

			// Create the data structures
			additionTest(size, bst, dataSet, redBlack, hashTable);
			// Search for 10 items and output the average number of comparisons
			searchTest(size, bst, redBlack, hashTable);
			// Remove, shuffle and re-add a fifth of the elements 10 times
			System.out.println("\n||| Performing addition and removal |||");
			for (int i = 0; i < 10; i++)
				removeAddFifth(size, bst, redBlack, hashTable);
			searchTest(size, bst, redBlack, hashTable); 
			// Display set information
			displayInfo(size, bst, dataSet, redBlack, hashTable);
			
			System.out.println("\nSerializing Extended Binary Search Tree...");
			try {
				FileOutputStream fos = new FileOutputStream("bst.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(bst);
				oos.close();
				System.out.println("Serialization successful.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Deserializing Extended Binary Search Tree...");
			try {
				FileInputStream fis = new FileInputStream("bst.ser");
				ObjectInputStream ois = new ObjectInputStream(fis);
				BinarySearchTree<Item> copy = (BinarySearchTree<Item>) ois.readObject();
				ois.close();
				System.out.println("Deserialized tree equal to actual tree: " + copy.equals(bst));
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			System.out.println("Testing complete....");

		}
	}

	public static void serializationTest() {
		BinarySearchTree<Integer> bst = new BinarySearchTree<>();
		
		System.out.println("Empty Tree before serialization:");
		System.out.println(bst.toStringBreadth());
		
		try {
			FileOutputStream fos = new FileOutputStream("bst.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(bst);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Empty Tree obtained from deserialization:");
		try {
			FileInputStream fis = new FileInputStream("bst.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			BinarySearchTree<Integer> copy = (BinarySearchTree<Integer>) ois.readObject();
			ois.close();
			System.out.println(copy.toStringBreadth());
			System.out.println("The trees are equal (equals method): " + copy.equals(bst));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bst.add(10);
		bst.add(9);
		bst.add(20);
		bst.add(30);
		bst.add(355);
		bst.add(40);
		bst.add(3);
		
		System.out.println("Tree before serialization:");
		System.out.println(bst.toStringBreadth());
		
		try {
			FileOutputStream fos = new FileOutputStream("bst.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(bst);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Tree obtained from deserialization:");
		try {
			FileInputStream fis = new FileInputStream("bst.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			BinarySearchTree<Integer> copy = (BinarySearchTree<Integer>) ois.readObject();
			ois.close();
			System.out.println(copy.toStringBreadth());
			System.out.println("The trees are equal (equals method): " + copy.equals(bst));
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	public static void redBlackStressTest(){
		TreeSet<Item> redBlack = new TreeSet<>();
		TreeSet<Item> randomRedBlack = new TreeSet<>();
		
		System.out.print("Please enter the size of the data set: ");
		Item.resetCompCount();
		try (Scanner keyboard = new Scanner(System.in)) {
			int size = keyboard.nextInt();
			System.out.printf("The size of the dataSet for this run is %d \n", size);
			System.out.printf("The number of computations at start is %d \n", Item.getCompCount());
			
			//-----------------------------------------------
			// Linearly built tree
			System.out.println("\n--- || Linearly built TreeSet ||---");
			System.out.println("\n--- Adding elements... ---");
			int count = 1;
			for (count = 1; count <= size * 2; count += 2) {
				Item candidate = new Item(count);
				redBlack.add(candidate);
			}
			System.out.printf("Added %d elements (odd from 1 to %d, in linear order).\n", redBlack.size(), size * 2);
			System.out.println("The size of the RedBlack Tree is: " + redBlack.size());
			System.out.printf("The number of computations after adding is %d \n", Item.getCompCount());
			System.out.println("Height of the TreeSet: " + height(redBlack));
	
			Item.resetCompCount();
			int errorCount = 0;
			System.out.println("\n--- Searching all elements (odd numbers)... ---");
			for (count = 1; count <= size * 2; count += 2) {
				Item candidate = new Item(count);
				if (!redBlack.contains(candidate))
					errorCount++;
			}
			System.out.printf("Found elements with %d  errors.\n", errorCount);
			System.out.printf("The number of computations to find all elements is %d \n", Item.getCompCount());
			Item.resetCompCount();
			
			errorCount = 0;
			System.out.println("\n--- Searching all elements (even numbers)... ---");
			for (count = 0; count <= (size * 2) - 2; count += 2) {
				Item candidate = new Item(count);
				if (redBlack.contains(candidate))
					errorCount++;
			}
			System.out.printf("Searched unsuccessfuly elements with %d  errors.\n", errorCount);
			System.out.printf("The number of computations to search unsuccessfuly all even numbers in range is %d \n", Item.getCompCount());
			Item.resetCompCount();
	
			System.out.println("\n--- Removing elements... ---");
			for (count = 1; count <= size * 2; count += 2) {
				Item candidate = new Item(count);
				redBlack.remove(candidate);
			}
			System.out.printf("Removed %d elements (odd from 1 to %d, in linear order).\n", redBlack.size(), size * 2);
			System.out.println("The size of the RedBlack Tree is: " + redBlack.size());
			System.out.printf("The number of computations after removing is %d \n", Item.getCompCount());
			System.out.println("Height of the TreeSet: " + height(redBlack));
			Item.resetCompCount();
			//-----------------------------------------------
			// Randomly built tree
			System.out.println("\n--- || Randomly built TreeSet ||---");
			System.out.println("\n--- Adding elements... ---");
			count = 1;
			LinkedList<Item> dataSet = new LinkedList<>();
			for (count = 1; count <= size * 2; count += 2) {
				Item candidate = new Item(count);
				dataSet.add(candidate);
			}
			Collections.shuffle(dataSet);
			
			Item.resetCompCount();
			for(Item i : dataSet)
				randomRedBlack.add(i);
			
			System.out.printf("Added %d elements (odd from 1 to %d, in random order).\n", randomRedBlack.size(), size * 2);
			System.out.println("The size of the RedBlack Tree is: " + randomRedBlack.size());
			System.out.printf("The number of computations after adding is %d \n", Item.getCompCount());
			System.out.println("Height of the TreeSet: " + height(randomRedBlack));
	
			Item.resetCompCount();
			errorCount = 0;
			System.out.println("\n--- Searching all elements (odd numbers)... ---");
			for (count = 1; count <= size * 2; count += 2) {
				Item candidate = new Item(count);
				if (!randomRedBlack.contains(candidate))
					errorCount++;
			}
			System.out.printf("Found elements with %d  errors.\n", errorCount);
			System.out.printf("The number of computations to find all elements is %d \n", Item.getCompCount());
			Item.resetCompCount();
			
			errorCount = 0;
			System.out.println("\n--- Searching all elements (even numbers)... ---");
			for (count = 0; count <= (size * 2) - 2; count += 2) {
				Item candidate = new Item(count);
				if (randomRedBlack.contains(candidate))
					errorCount++;
			}
			System.out.printf("Searched unsuccessfuly elements with %d  errors.\n", errorCount);
			System.out.printf("The number of computations to search unsuccessfuly all even numbers in range is %d \n", Item.getCompCount());
			Item.resetCompCount();
	
			System.out.println("\n--- Removing elements... ---");
			for (count = 1; count <= size * 2; count += 2) {
				Item candidate = new Item(count);
				randomRedBlack.remove(candidate);
			}
			System.out.printf("Removed %d elements (odd from 1 to %d, in random order).\n", randomRedBlack.size(), size * 2);
			System.out.println("The size of the RedBlack Tree is: " + randomRedBlack.size());
			System.out.printf("The number of computations after removing is %d \n", Item.getCompCount());
			System.out.println("Height of the TreeSet: " + height(randomRedBlack));
			
			Item.resetCompCount();	
		}
	}
}
