package part2;
import java.util.*;
import java.io.*;

/**
 * A class that represents an extended binary search tree.
 * @author B00273607
 * based on the original BinarySearchTree class by William Collins, with some minor modifications by Richard Beeby.
 *
 * @param <E> the element type for the tree
 */
public class BinarySearchTree<E extends Comparable<? super E>>
        extends AbstractSet<E> implements Serializable
{	/**
	 * Version number - serialized instances of this class will have this
	 * identifier; deserializing an object whose version number is different
	 * from the loader's will cause a InvalidClassException.
	 */
	private static final long serialVersionUID = 6901324832482596386L;

    // The root of this tree. This is null when the tree is empty.
    protected transient Entry<E> root;

    // The number of entries in this tree. This is zero when the tree is empty.
    protected transient int size;        
       
    // Counts the number of modifications made to this tree
    // other than by the iterator.
    protected transient int modCount = 0;
    
    /**
     *  Initializes this BinarySearchTree object to be empty, to contain only 
     *  elements of type E, to be ordered by the Comparable interface, and to 
     *  contain no duplicate elements.
     *
     */ 
    public BinarySearchTree() 
    {
    	root = new Entry<E>(null);
    	size = 0;
    } // default constructor


    /**
     * Initializes this BinarySearchTree object to contain a shallow copy of
     * a specified BinarySearchTree object.
     * The worstTime(n) is O(n), where n is the number of elements in the
     * specified BinarySearchTree object.
     *
     * @param otherTree - the specified BinarySearchTree object that this
     *                BinarySearchTree object will be assigned a shallow 
     *                copy of.
     */
    public BinarySearchTree (BinarySearchTree<? extends E> otherTree)
    {
         root = copy (otherTree.root, null);
         size = otherTree.size;  
    } // copy constructor

    protected Entry<E> copy (Entry<? extends E> p, Entry<E> parent)
    {
        if (p != null)
        {
            Entry<E> q = new Entry<E> (p.element, parent);
            q.left = copy (p.left, q);
            q.right = copy (p.right, q);
            return q;
        } // if
        return null;
    } // method copy
     
    @Override
    public boolean equals (Object obj)
    {
        if (!(obj instanceof BinarySearchTree))
            return false;
        return equals (root, ((BinarySearchTree<? extends E>)obj).root);
    } // method 1-parameter equals
    
    public boolean equals (Entry<E> p, Entry<? extends E> q)
    {
       if (p == null || q == null)
            return p == q;  
       if (p.isExternal() || q.isExternal())
           return p.isExternal() && q.isExternal(); 
       if (!p.element.equals (q.element))
           return false;
       return equals (p.left, q.left) && equals (p.right, q.right);
    } // method 2-parameter equals
    
     /**
     * Returns the hash code value for this set.  The hash code of a set is
     * defined to be the sum of the hash codes of the elements in the set,
     * where the hash code of a <tt>null</tt> element is defined to be zero.
     * This ensures that <tt>s1.equals(s2)</tt> implies that
     * <tt>s1.hashCode()==s2.hashCode()</tt> for any two sets <tt>s1</tt>
     * and <tt>s2</tt>, as required by the general contract of
     * {@link Object#hashCode}.
     *
     * <p>This implementation iterates over the set, calling the
     * <tt>hashCode</tt> method on each element in the set, and adding up
     * the results.
     *
     * @return the hash code value for this set
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
    
    /**
     *  Returns the size of this BinarySearchTree object.
     *
     * @return the size of this BinarySearchTree object.
     *
     */
    public int size( )
    {
        return size;
    } // method size() 

    /**
     *  Returns an iterator positioned at the smallest element in this 
     *  BinarySearchTree object.
     *
     *  @return an iterator positioned at the smallest element in this
     *                BinarySearchTree object.
     *
     */
    public Iterator<E> iterator()
    {
         return new TreeIterator();
    } // method iterator

    /**
     *  Determines if there is an element in this BinarySearchTree object that
     *  equals a specified element.
     *  The worstTime(n) is O(n) and averageTime(n) is O(log n).  
     *
     *  @param obj - the element sought in this BinarySearchTree object.
     *
     *  @return true - if there is an element in this BinarySearchTree object
     *                that equals obj; otherwise, return false.
     *
     *  @throws ClassCastException - if obj cannot be compared to the 
     *           elements in this BinarySearchTree object. 
     *  @throws NullPointerException - if obj is null.
     *  
     */ 
    public boolean contains (Object obj) 
    {
        return !getEntry(obj).isExternal();
    } // method contains

    /**
     *  Ensures that this BinarySearchTree object contains a specified element.
     *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
     *
     *  @param element - the element whose presence is ensured in this 
     *                 BinarySearchTree object.
     *
     *  @return true - if this BinarySearchTree object changed as a result of 
     *                this method call (that is, if element was actually
     *                inserted); otherwise, return false.
     *
     *  @throws ClassCastException - if element cannot be compared to the 
     *                  elements already in this BinarySearchTree object.
     *  @throws NullPointerException - if element is null.
     *
     */
    public boolean add (E element)  
    {        
    	Entry<E> entry = getEntry(element); 
    	if (entry.isExternal()){	   	
    		entry.makeInternal(element);
    		size++;
    		modCount++;
    		return true;
    	}
    	else return false;
    }    	

    /**
     *  Ensures that this BinarySearchTree object does not contain a specified 
     *  element.
     *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
     *
     *  @param obj - the object whose absence is ensured in this 
     *                 BinarySearchTree object.
     *
     *  @return true - if this BinarySearchTree object changed as a result of
     *                this method call (that is, if obj was actually removed);
     *                otherwise, return false.
     *
     *  @throws ClassCastException - if obj cannot be compared to the 
     *                  elements already in this BinarySearchTree object. 
     *  @throws NullPointerException - if obj is null.
     *
     */
    public boolean remove (Object obj)
    {
        Entry<E> e = getEntry (obj);
        if (e.isExternal())
            return false;
        deleteEntry (e);
        modCount++;
        return true;
    } // method remove

    /**
     *  Finds the Entry object that houses a specified element, if there is such
     *  an Entry. The worstTime(n) is O(n), and averageTime(n) is O(log n).
     *
     *  @param obj - the element whose Entry is sought.
     *
     *  @return the Entry object that houses obj - if there is such an Entry;
     *                otherwise, return null.  
     *
     *  @throws ClassCastException - if obj is not comparable to the elements
     *                  already in this BinarySearchTree object.
     *  @throws NullPointerException - if obj is null.
     *
     */
    protected Entry<E> getEntry (Object obj) 
    {
        int comp;

        if (obj == null)
           throw new NullPointerException();
        Entry<E> e = root;
        while (e != null && !e.isExternal())
        {
            comp = ((Comparable<? super E>)obj).compareTo (e.element);
            if (comp == 0)
                return e;
            else if (comp < 0)
                e = e.left;
            else
                e = e.right;
        } // while
        	return e;
    } // method getEntry
   
     /**
      *  Deletes the element in a specified Entry object from this 
      *  BinarySearchTree.
      *  
      *  @param p The Entry object whose element is to be deleted from this
      *           BinarySearchTree object.
      *
      *  @return the Entry object that was actually deleted from this 
      *           BinarySearchTree object. 
      *
      */
    protected Entry<E> deleteEntry (Entry<E> p) 
    {
        size--;
        // If p has two children, replace p's element with p's successor's
        // element, then make p reference that successor.
        if(!p.left.isExternal() && !p.right.isExternal()) // if the two children are internal
        {
            Entry<E> s = successor (p);
            p.element = s.element;
            p = s;
        } // p had two children

        // At this point, p has either no internal children or one internal child.
        Entry<E> replacement = null;
         
        if (!p.left.isExternal())
            replacement = p.left;
        else if(!p.right.isExternal())
            replacement = p.right;

        // If p has at least one internal child, link replacement to p.parent.
        if (replacement != null) 
        {
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;
        } // p has at least one internal child  
        else if (p.parent == null)
        	root.makeExternal();
        else 
        {
        	p.makeExternal();
        } // p has a parent but no internal children
        return p;
    } // method deleteEntry

    /**
     *  Finds the successor of a specified Entry object in this
     *  BinarySearchTree. The worstTime(n) is O(n) and averageTime(n) is
     *  constant.
     *
     *  @param e - the Entry object whose successor is to be found.
     *
     *  @return the successor of e if e has a successor; otherwise, return null.
     *
     */
    protected Entry<E> successor (Entry<E> e) 
    {
        if (e == null || e.isExternal())
            return null;
        else if (!e.right.isExternal())
        {
            // successor is leftmost Entry in right subtree of e
            Entry<E> p = e.right;
            while (!p.left.isExternal())
                p = p.left;
            return p;

        } // e has a right child that isn't external
        else 
        {
            // go up the tree to the left as far as possible, then go up
            // to the right.
            Entry<E> p = e.parent;
            Entry<E> ch = e;
            while (p != null && ch == p.right)
            {
                ch = p;
                p = p.parent;
            } // while
            return p;
        } // e has no right child
    } // method successor
    
    protected class TreeIterator implements Iterator<E>
    {
        protected Entry<E> lastReturned = null,
                           next;
        
        protected int modCountOnCreation;

        /**
         *  Positions this TreeIterator to the smallest element, according to 
         *  the Comparable interface, in the BinarySearchTree object.
         *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
         *
         */
        protected TreeIterator() 
        {             
            next = root;
            	if (!next.isExternal()) // necessary for empty tree
            		while(!next.left.isExternal()) // don't go into external nodes
            			next = next.left;
            modCountOnCreation = modCount;
        } // default constructor


        /**
         *  Determines if there are still some elements, in the BinarySearchTree 
         *  object this TreeIterator object is iterating over, that have not
         *  been accessed by this TreeIterator object.
         *
         *  @return true - if there are still some elements that have not been
         *                accessed by this TreeIterator object; otherwise, 
         *                return false.
         *
         */ 
        public boolean hasNext() 
        {
        	return (next!=null  && !next.isExternal());
        } // method hasNext


        /**
         *  Returns the element in the Entry this TreeIterator object was 
         *  positioned at before this call, and advances this TreeIterator
         *  object. The worstTime(n) is O(n) and averageTime(n) is constant.
         *
         *  @return the element this TreeIterator object was positioned at
         *          before this call.
         *
         *  @throws NoSuchElementException - if this TreeIterator object was not 
         *                 positioned at an Entry before this call.
         *  @throws ConcurrentModificationException - if the tree has been 
         *  modified by a call to the tree's add() or remove() method since
         *  this TreeIterator was created.
         *  
         *  @throws IllegalStateException - if the tree has been modified by a
         *                         call to the tree's add() or remove() method
         *                         since this TreeIterator was created.
         *
         */
        public E next() 
        {
        	if (modCountOnCreation != modCount) {
        		throw new ConcurrentModificationException();
        	}
            if (next == null || next.isExternal())
                throw new NoSuchElementException();
            lastReturned = next;
            next = successor (next);             
            return lastReturned.element;
        } // method next

        /**
         *  Removes the element returned by the most recent call to this 
         *  TreeIterator object's next() method.
         *  The worstTime(n) is O(n) and averageTime(n) is constant.
         *
         *  @throws IllegalStateException - if this TreeIterator's next() 
         *  method was not called before this call, or if this
         *  TreeIterator's remove() method was called between
         *  the call to the next() method and this call.
         *  
         * @throws ConcurrentModificationException - if the tree has been 
         *  modified by a call to the tree's add() or remove() method since
         *  this TreeIterator was created.
         */ 
        public void remove() 
        {
             if (lastReturned == null)
                throw new IllegalStateException();
             if (modCountOnCreation != modCount)
                throw new ConcurrentModificationException();
 
             if (!lastReturned.left.isExternal() && !lastReturned.right.isExternal())
                next = lastReturned;
            deleteEntry(lastReturned);
            lastReturned = null; 
        } // method remove     

    } // class TreeIterator

    protected static class Entry<E>
    {
        protected E element;					

        protected Entry<E> left = null,	
                           right = null,
                           parent;
 
        /**
         *  Initializes this Entry object.
         *
         *  This default constructor is defined for the sake of subclasses of
         *  the BinarySearchTree class. 
         */
        public Entry() { }


        /**
         *  Initializes this Entry object from element and parent.
         *  In this version of the Binary Search Tree, used to copy entries.
		 * 
		 * @param element of the new entry.
		 * @param parent of the new entry.
		 */
         public Entry (E element, Entry<E> parent) 
         {
             this.element = element;
             this.parent = parent;
             // TO ADD - make the node internal rather than have it do it in add
         } // constructor
         
         /**
          * Initializes a node with a null element. Left and right are set to null by default.
          * In other words, initializes an external node.
          * 
          * @param parent of the external node.
          */
         public Entry (Entry<E> parent)
         {
        	 this.element = null;
        	 this.parent = parent;
         }
         
         /** 
          *  Returns true if this entry if an external node and false otherwise.
          *  Check whether entry is an external node or not.
          *  The worstTime(n) is constant and averageTime(n) is constant.
          *  
          *  @return true - if this entry if an external node and false otherwise.
          */
         
         public boolean isExternal(){
        	 return this.element == null;
         } // method isExternal
         
         /** 
          *  Returns true if this entry is a leaf (internal node with only external children).
          *  Check whether entry is leaf or not.
          *  The worstTime(n) is constant and averageTime(n) is constant.
          *  
          *  @return true - if this entry is a leaf and false otherwise.
          */
         public boolean isLeaf() {
        	 return !isExternal() &&
        	(left.isExternal() && right.isExternal());
        	 } 
         
         /** 
          *  Converts this internal node to an external node and returns the element that the internal node
          *  contained (used when deleting an internal node that has no internal nodes as children) 
          *  The worstTime(n) is constant and averageTime(n) is constant.
          *  
          *  @return the element that internal node contained.
          *
          *  @throws IllegalStateException - if the Entry on which the method is called is not in an appropriate state
          *  (e.g. called on an external node or on an internal node with internal nodes as children).
          */
         
         public E makeExternal(){
        	if (this.isExternal())// assuming the tree is built correctly and the node exists, this equates to an external node
        		throw new IllegalStateException();
        	if (!this.left.isExternal() || !this.right.isExternal()) // if any of the two children is an internal node
        		 throw new IllegalStateException();
         	E currentElement = this.element;
         	this.element = null;
         	this.left.parent = null;
         	this.right.parent = null;
         	this.left = null;
         	this.right = null;
         	return currentElement;
         } // method makeExternal
         
         /** 
          *  Converts this external node to an internal node containing the given element and adds
          *  two new external nodes as the left and right children of the 
          *  node (used when inserting an element into the tree)
          *  The worstTime(n) is constant and averageTime(n) is constant.
          *
          *  @param element - the element contained in the new internal node. 

          *  @throws IllegalStateException - if the Entry on which the method is called is not in an appropriate state
          *  (e.g. called on an internal node).
          */
         public void makeInternal(E element){
        	 if (!this.isExternal())// assuming the tree is built correctly and the node exists, this equates to an internal node
         		throw new IllegalStateException();
         	 this.element = element;
        	 Entry<E> newLeft, newRight;
        	 newLeft = new Entry<E>(this);
        	 newRight = new Entry<E>(this);
        	 this.left = newLeft;
        	 this.right = newRight;       	 
         } // method makeInternal

    } // class Entry
    
    /** Reference: http://stackoverflow.com/questions/5262308/how-do-implement-a-breadth-first-traversal */
    /** 
     *  Displays the elements in the order they would be visited 
     *  in a breadth-first traversal of the tree
     *  The worstTime(n) is O(n) and averageTime(n) is O(n).
     *  
     *  @return a String containing the the elements
     */
    public String toStringBreadth(){
    	String breadthFirst = "";
    	Queue<Entry<E>> queue = new LinkedList<Entry<E>>() ;
    	    if (root.isExternal())
    	        return "Tree is empty (one external node).";
    	    queue.clear();
    	    queue.add(root);
    	    while(!queue.isEmpty()){
    	    	Entry<E> node = queue.remove();
    	    	if (node.isExternal())
    	    		breadthFirst += ("[EXT] ");
    	    	else
    	    		breadthFirst += (node.element + " ");
    	        if(node.left != null) queue.add(node.left);
    	        if(node.right != null) queue.add(node.right);
    	    }
    	return breadthFirst;
    } // method toStringBreadth
    
    /**
     * Wrapper method for height recursive method.]
     * 
     * @return height of the tree, or -1 if tree is empty
     */
    public int findHeight(){
    	if(root.isExternal())
    		return -1;
    	else
    		return height(root);
    }
    
    /** Reference:  // http://stackoverflow.com/questions/2597637/finding-height-in-binary-search-tree
    /** 
     *  Calculates the height of a binary search tree in a recursive way
     *  The worstTime(n) is O(n) and averageTime(n) is O(n).
     *  
     *  @return the height of the tree.
     */
    private int height(Entry<E> newRoot)
    {
    	int leftHeight = -1, rightHeight = -1;
    	if (!newRoot.left.isExternal())
    		leftHeight = height(newRoot.left);
    	if (!newRoot.right.isExternal())
    		rightHeight = height(newRoot.right);
    		
    	return Math.max(leftHeight, rightHeight) + 1;
    }
   
    
    /** Reference:  // http://algorithms.tutorialhorizon.com/find-the-height-of-a-tree-without-recursion/
    /** 
     *  Calculates the height of a binary search tree in an iterative way
     *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
     *  
     *  @return an int containing the height of the tree.
     */
    public int findIterativeHeight(){
    	// changed element = null to isext
    	int height = -1;
    	Queue<Entry<E>> queue = new LinkedList<Entry<E>>() ;
    	    if (root.isExternal())
    	        return -1;
    	    queue.clear();
    	    queue.add(root);
    	    queue.add(null); //add null as marker
    	    while(!queue.isEmpty()){
    	    	Entry<E> node = queue.remove();
    	    	if (node == null) {
    	    		if(!queue.isEmpty()){
    	    			queue.add(null);
    	    		}
    	    	height++;
    	    } else {
    	    	if (!node.left.isExternal()) {
					queue.add(node.left);
				}
				if (!node.right.isExternal()) {
					queue.add(node.right);
				}
    	    }
	    }
    	return height;
    } // findIterativeHeight method
    
 
    /** 
     * Credit: Richard Beeby
     * Wrapper method for recursive method countLeaves
     *
     *@return the number of leaves
     */
    public int leaves() {
    	 return countLeaves(root);
    	 }
    
    /**
     * Credit: Richard Beeby
     * Recursively find the number of leaves of the calling Extended Binary Search Tree.
     * 
     * @param node - starting node for current recursive cycle
     * @return the number of leaves
     */
    private int countLeaves(Entry<E> node) {
    	 if (node.isExternal()) return 0;
    	 if (node.isLeaf()) return 1;
    	 int count = countLeaves(node.left);
    	 count += countLeaves(node.right);
    	 return count;
    	 } // countLeaves method
    
    /** 
     * Wrapper method for recursive method findInternalPathLength
     * 
     * @return the internal path length of the tree
     */
    public int InternalPathLength(){
    	return findInternalPathLength(root, 0);
    }
    
    /** 
     * Reference: http://stackoverflow.com/questions/3878456/determine-the-internal-path-length-of-a-tree-c
     * 
     * Recursively calculate the Internal Path Length of a tree.
     * (The external path length is directly related so doesn't require a method)
     * 
     * @return the internal path length of the tree
     * 
     */
    private int findInternalPathLength(Entry<E> node, int value) {
    	 if (node.isExternal()) return 0;
    	 return (value+findInternalPathLength(node.right,value+1)+findInternalPathLength(node.left,value+1)); // check
    	 } // findInternalPathLength method
    
    
    /** 
     * Serialize a tree using a breadth first traversal and only saving the element of each node
     * Skips external nodes as they do not need to be serialized
     * The worstTime(n) is O(n) and averageTime(n) is O(n).
     *
     * @param os - Object output stream
     */
    private void writeObject(ObjectOutputStream os) 
    		throws IOException {
    	try{
    		os.defaultWriteObject();
    		os.writeInt(size);		
        	Queue<Entry<E>> queue = new LinkedList<Entry<E>>() ;
    	    queue.clear();
    	    queue.add(root);
    	    while(!queue.isEmpty()){		// Breadth first traversal, similar to the one used in the toStringBreadth method
    	    	Entry<E> node = queue.remove();
    	    	if(node.element != null)
    	    		os.writeObject(node.element);
    	        if(node.left != null) queue.add(node.left);
    	        if(node.right != null) queue.add(node.right);
       	    }
    	} catch (Exception e) { e.printStackTrace(); }
    } 	// writeObject method 
    
    /** 
     * Reference: http://stackoverflow.com/questions/12963445/serialization-readobject-writeobject-overides
     * 
     * Deserialize an object. Since objects are serialized in a breadth first traversal order, the tree can
     * be reconstructed by simply adding all elements (with the add() method) in the order they are saved.
     * The worstTime(n) is O(n) and averageTime(n) is O(n).
     * 
     * @param is - Object input stream
     */
    private void readObject(ObjectInputStream is) 
    		throws IOException, ClassNotFoundException {
    	try{
    		is.defaultReadObject();
    		int temp_size = is.readInt();	// Read the size
    		root = new Entry<E>();			// Initialize root as external node
    		for (int i = 0; i < temp_size; i++) {
    			E temp = (E)is.readObject();
    			if (temp != null) 			// In fact, this should never happen
    				add(temp);
    		}
    	}  catch (Exception e) { e.printStackTrace(); }
    } // readObject method
} // class BinarySearchTree

