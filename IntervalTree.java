/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2017
// PROJECT:          p4
// FILE:             IntervalTree.java
//
// Authors: Patrick Lown, Hayley Raj, Ryan Ramsdell, Ilhan Bok, Abhi Kumar, Ben Pekala
// Author1: Hayley Raj, hraj@wisc.edu, hraj, 003
// Author2: Ryan Ramsdell, ramsdell2@wisc.edu, ramsdell2, 003
// Author3: Ilhan Bok, ibok@wisc.edu, ibok, 003
// Author4: Abhi Kumar,akumar63@wisc.edi,abhik,003
// Author5: Patrick Lown, lown@wisc.edu, lown, 003
// Author6: Benjamin Pekala, bpekala@wisc.edu, pekala, 003
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Interval tree class that stores intervals in a binary search tree for fast
 * access. Uses a maxEnd for quicker searches
 * 
 * @author Team 91
 */
public class IntervalTree<T extends Comparable<T>> implements IntervalTreeADT<T> {
	//root node of the interval tree
	private IntervalNode<T> root;
	
	
	/**
     * Getter method for the root of the tree.
     *
     * PRECONDITIONS: (tree is assumed to be non-null)
     *
     * @return (root) the root node of the tree
	*/
	@Override
	public IntervalNode<T> getRoot() {
		return this.root;
	}
	/**
	 * This is the public method for inserting into the interval tree.
	 * Logic: Checks if the root is null, if so, makes new node root.
	 * Otherwise, calls private helper method to insert
	 * 
	 * @param interval
	 *            the interval to be inserted into the tree
	 */
	@Override
	public void insert(IntervalADT<T> interval)
					throws IllegalArgumentException {
		if( interval.getEnd().compareTo(interval.getStart()) < 0 )
			throw new IllegalArgumentException();
		if (this.root == null)
		{
			this.root = new IntervalNode<T>(interval);
			return;
		}
		
		insert(this.root, interval);
	}
	/**
	 * Private helper method for insert method.  
	 * Inserts a new a node containing the passed in interval to the interval tree.
	 * Potentially changes maxEnd variable of nodes within the tree.
	 * 
	 * @param node
	 *            the current node
	 * @param interval
	 * 			  the interval of the new node
	 */
	private void insert(IntervalNode<T> node, IntervalADT<T> interval)
	{
		//create new node w/ interval
		IntervalNode<T> newNode = new IntervalNode<T>(interval);
		
		//check if intervals are the same, if so, throw exception
		if (node.getInterval().compareTo(interval) == 0)
		{
			throw new IllegalArgumentException();
		}
		/* This conditional determines how to handle the interval if it is greater than
		 * the current node's interval.
		 * If the current node does not have a right node, set its right node to the newNode, then call
		 * endAdjust() method to handle maxEnd. Then return.
		 * If current node does have a right node, call the private insert on the current node's right node.
		 * Then call the endAdjust() method to handle maxEnd. Then return.
		 */
		if (interval.compareTo(node.getInterval()) > 0)
		{
			if (node.getRightNode() == null) 
			{
				node.setRightNode(newNode);
				insertAdjust(node);
				return;
			}
			insert(node.getRightNode(), interval);
			insertAdjust(node);
			return;
		}
		//Logic for left node is same as above for right node.
		if (node.getLeftNode() == null)
		{
			node.setLeftNode(newNode);
			insertAdjust(node);
			return;
		}
		insert(node.getLeftNode(), interval);
		insertAdjust(node);
	}
	/**
	 * helper method for private insert and delete method.  This checks, for the node passed in, if either
	 * of it's childrens' maxEnds are greather than its own.  If so, adjusts its own maxEnd.  If not, does
	 * nothing.
	 * 
	 * @param node
	 *            the current node
	 */
	private void insertAdjust(IntervalNode<T> node)
	{
			//checks if left node's maxEnd is greater than its own maxEnd
			if (node.getLeftNode() != null)
			{
				//adjusts if true
				if(node.getLeftNode().getMaxEnd().compareTo(node.getMaxEnd()) > 0) node.setMaxEnd(node.getLeftNode().getMaxEnd());
			}
			//checks if right node's maxEnd is greater than its own maxEnd
			if (node.getRightNode() != null)
			{
				//adjusts if true
				if(node.getRightNode().getMaxEnd().compareTo(node.getMaxEnd()) > 0) node.setMaxEnd(node.getRightNode().getMaxEnd());
			}

	}
	
	/**
	 * Delete method will take an interval, search for that interval in the tree and if found, will delete it
	 * Has potential to change maxEnd
	 * 
	 * @param interval
	 *            the interval to be deleted
	 */
	@Override
	public void delete(IntervalADT<T> interval)
					throws IntervalNotFoundException, IllegalArgumentException {
		if(this.root == null)
		{
			throw new IllegalArgumentException();
		}
		this.root = deleteHelper(this.root,interval);
		
		//Updates MaxEnd for the root 
		recalculateMaxEnd(this.root);

	}
	/**
	 * Helper method for delete.  Recursively searches for passed in node
	 * and deletes if found.  Calls RecalculateMaxEnd() method.
	 * 
	 * @param node
	 *            the current node
	 *           
	 * @param interval
	 * 			   the interval searched for
	 */
	@Override
	public IntervalNode<T> deleteHelper(IntervalNode<T> node,
					IntervalADT<T> interval)
					throws IntervalNotFoundException, IllegalArgumentException {
		if( node == null ){
			throw new IntervalNotFoundException(interval.toString());
		}
		/*If the current node's interval matches the interval to be deleted, check
		 * if it has null children, if not return rightnode. If there is no rightnode, 
		 * return leftnode. Then set the current node's interval to its successor.
		 */
		if( node.getInterval().compareTo( interval ) == 0 ){
			//If it doesn't have children, nothing needs to replace it 
			if(  node.getLeftNode() == null &&  node.getRightNode() == null)
				return null;
			if( node.getLeftNode() == null )
				return node.getRightNode();
			if( node.getRightNode() == null )
				return node.getLeftNode();
			// If it has two children, replace it with its smallest successor
			node.setInterval( node.getSuccessor().getInterval());
			//Deletes the second copy of the successor that has now moved  
			node.setRightNode( deleteHelper(node.getRightNode(), node.getInterval()));
		}
		
		/*If the current node's interval is greater than the one to be deleted, 
		the left subtree is recursively traversed until it is found and deleted.
		*/
		else if( node.getInterval().compareTo( interval ) > 0 ){
			node.setLeftNode( deleteHelper(node.getLeftNode(), interval) );
		}
		/*If the current node's interval is less than the one to be deleted, 
		the right subtree is recursively traversed until it is found and deleted.
		*/
		else{
			node.setRightNode( deleteHelper(node.getRightNode(), interval) );
		}
		
		//Updates for MaxEnd for the nodes as necessary, except the root
		recalculateMaxEnd(node); 
		return node;
		
	}
	
	/**
	 * This private method helps recalculate max end for a node. It compares the
	 * MaxEnd of the the current node with its children if they exist to find 
	 * whichever one is the highest. 
	 * @param node The node that has replaced the deleted node
	 */
	private void recalculateMaxEnd(IntervalNode<T> node){
		//Stores the end of the current node's interval
		T maxIntervalEnd = node.getInterval().getEnd(); 
		
		//If the node has no children, sets its MaxEnd to its own interval end
		if( node.getRightNode() == null && node.getLeftNode() == null ){
			node.setMaxEnd( maxIntervalEnd );
		}
		
		//If the node only has a left child
		else if( node.getRightNode() == null ){
			//Stores the left child's MaxEnd
			T maxEndLeft = node.getLeftNode().getMaxEnd();
			//Stores the comparison of the left child's MaxEnd and the current
			//node's interval end 
			int comp2 = maxEndLeft.compareTo(maxIntervalEnd); 
			
			//If the current node's end is higher, set MaxEnd to that value 
			if (comp2 < 0)
				node.setMaxEnd(maxIntervalEnd);
			else 
				node.setMaxEnd(maxEndLeft);
		}
		
		//If the node only has a right child
		else if (node.getLeftNode() == null) {
			//Stores the right child's MaxEnd
			T maxEndRight = node.getRightNode().getMaxEnd();
			//Stores the comparison of the right child's MaxEnd and the current
			//node's interval end 
			int comp3 = maxEndRight.compareTo(maxIntervalEnd); 
			
			//If the current node's end is higher, set MaxEnd to that value
			if (comp3 < 0)
				node.setMaxEnd(maxIntervalEnd);
			else 
				node.setMaxEnd(maxEndRight);
		}
		
		//The node has two children
		else {
			//Stores the left child's MaxEnd
			T maxEndLeft = node.getLeftNode().getMaxEnd();
			//Stores the right child's MaxEnd
			T maxEndRight = node.getRightNode().getMaxEnd();
			//Compares all three maxes to find the correct MaxEnd for the node 
			node.setMaxEnd(findMax(findMax(maxEndLeft, maxEndRight),
					node.getInterval().getEnd()));
			
		}		
		
	}
	/**
	 * Compares two comparable entities and returns the largest of them
	 * 
	 * @param left One of nodes to compare
	 * @param right Other node to compare
	 * @return Largest of two values
	 */
	private T findMax(T left, T right){
		if (left.compareTo(right) < 0) {
			return right;
		} else {
			return left;
		}
	}
	/**
	 * Checks if interval passed in overlaps with any intervals currently in the tree
	 * Calls private helper method.
	 * 
	 * PRE CONDITIONS: root is assumed to not be null
	 * 
	 * @param (interval)  the interval chekced for overlapping
	 * @return (list) list of intervals
	 */
	@Override
	public List<IntervalADT<T>> findOverlapping(
					IntervalADT<T> interval) {
		List<IntervalADT<T>> result = new ArrayList<IntervalADT<T>>();
		
		if( root != null) {findOverlappingHelper(interval, root, result);
		}
		return result;
	}
	
	/**
	 * Helper method for overlapping method.  checks if there is overlap in current node
	 * If so, adds that node's interval to the list, then recursively checks its children
	 * nodes for overlapping.
	 * 
	 * PRE CONDITIONS: root is assumed to not be null
	 * 
	 * @parma (interval) the interval being checked
	 * @param (node) the node being checked
	 * @param (result) the lsit of the intervals overlapping
	 * 
	 */
	private void findOverlappingHelper(IntervalADT<T> interval, IntervalNode<T> node, List<IntervalADT<T>> result) {
		if (interval.overlaps(node.getInterval())){
			result.add(node.getInterval());
		}
		if (node.getLeftNode() != null){
			findOverlappingHelper(interval, node.getLeftNode(), result);
		}
		if (node.getRightNode() != null){
			findOverlappingHelper(interval, node.getRightNode(), result);
		}
		return;
	}
	/**
	 * Search point method that checks if point passed in is contatnied by intervals.  If so,
	 * returns a list of the intervals that contain point.  Calls recursive searchpoint helper
	 * method.
	 * 
	 * PRE CONDITIONS: root is assumed to not be null
	 * 
	 * @param (point) checked for being contained
	 * @return (res) list of intervals
	 */
	@Override
	public List<IntervalADT<T>> searchPoint(T point) {
		// Throw IllegalArgumentException per requirements
		if (null == point)
		{
			throw new IllegalArgumentException();
		}
		// Create result list
		return searchPointHelper(root, point);
	}
	/**
	 * Helper method for searchpoint.  recursively adds intervals that contain the point 
	 * passed in to a list and return the list to the searchpoint method.
	 * 
	 * PRE CONDITIONS: root is assumed to not be null
	 * 
	 * @param (node) the current node being checked
	 * @param (point) the point checked for containing
	 * 
	 * @return (res) list of intervals
	 */
	private List<IntervalADT<T>> searchPointHelper(IntervalNode<T> node, T point) {
		List<IntervalADT<T>> res = new ArrayList<IntervalADT<T>>();
		//if our point is greater than the nodes maxEnd, no nodes in subtree wiill contain point
		if (point.compareTo(node.getMaxEnd()) > 0)
		{
			return res;
		}
		//if interval contains points, add interval to the list
		if (node.getInterval().contains(point)) 
		{
			res.add(node.getInterval());
		}
		//recursively call the helper method on the nodes left and right children
		if (node.getLeftNode() != null)
		{
			res.addAll(searchPointHelper(node.getLeftNode(), point));
		}
		if (node.getRightNode() != null)
		{
			res.addAll(searchPointHelper(node.getRightNode(), point));
		}
		//sort collection before returning
		Collections.sort(res);
		return res;
		
	}
	
	/**
	 * This method returns the size of the interval tree.  The size is the number of 
	 * nodes within in the tree.
	 * 
	 * PRE CONDITIONS: root is assumed to not be null
	 * 
	 * @return (int) of size
	 */
	@Override
	public int getSize() {
		if (this.root == null)
		{
			return 0;
		}
		return sizeHelper(this.root);
	}
	
	/**
	 * This private helper method is called by the getSize() method and 
	 * returns the size of the interval tree.  The size is the number of 
	 * nodes within in the tree.
	 * 
	 * PRE CONDITIONS: node is assumed to not be null
	 * @param (node) node to be added to size
	 * @return (int) of size
	 */
	private int sizeHelper(IntervalNode<T> node)
	{
		if (node == null)
		{
			return 0;
		}
		//if the node isn't null, returns 1 (for that node) + calling the sizeHelper method on that node's children
		return 1 + sizeHelper(node.getLeftNode()) + sizeHelper(node.getRightNode());
	}
	/**
	 * This method returns the height of the interval tree.
	 * The height is the length of the path from the root to the furthest leaf.
	 * Calls private helper method heightHelper()
	 * 
	 * PRE CONDITIONS: root is assumed to not be null
	 * 
	 * @return (int) of size
	 */
	@Override
	public int getHeight() {
		if (this.root == null)
		{
			return 0;
		}
		return heightHelper(this.root);
	}
	
	/**
	 * This method returns the size of the interval tree.  The size is the number of 
	 * nodes within in the tree.
	 * 
	 * PRE CONDITIONS: root is assumed to not be null
	 * 
	 * @return (int) of size
	 */
	private int heightHelper(IntervalNode<T> node)
	{
		if (node == null) return 0;
		return 1 + Math.max(heightHelper(node.getLeftNode()),heightHelper(node.getRightNode()));
	}
	/**
	 * This method returns whether the passed in interval is contained within the tree.
	 * Calls containHelper() method for traversal.
	 * 
	 * PRE CONDITIONS: interval is assumed to not be null, root is assumed to be non null
	 * @param (interval) compared to nodes and checed for matches
	 * @return (boolean) true or false dependent on if interval is contained in tree
	 */
	@Override
	public boolean contains(IntervalADT<T> interval) {
		if (interval == null)
		{
			throw new IllegalArgumentException();
		}
		if (this.root == null)
		{
			return false;
		}
		return containHelper(this.root, interval);
	}
	/**
	 * This method checks if the interval in the node matches the interval passed in the param.
	 * Checks for match on current node, then returns call of containHelper on each of the nodes
	 * children.
	 * 
	 * PRE CONDITIONS: root is assumed to not be null
	 * @param (node) interval node that is currently being compared to
	 * @param (interval) the interval passed in to compare to node
	 * @return (boolean) true or false dependent on if interval is contained in tree
	 */
	public boolean containHelper(IntervalNode<T> node, IntervalADT<T> interval)
	{
		
		if (node == null)
		{
			return false;
		}
		if(node.getInterval().compareTo(interval) == 0)
		{
			return true;
		}
		//if either of these return true, the whole return is true
		return containHelper(node.getRightNode(), interval) || containHelper(node.getLeftNode(),interval);
		
	}
	/**
	 * This method simply calls and prints the getHeight() and getSize() methods.
	 * IF root is null, causes exception
	 * 
	 * PRE CONDITIONS: root is assumed to not be null
	 */
	@Override
	public void printStats() {
		
		System.out.println("-----------------------------------------");
		System.out.println("Height: " + this.getHeight());
		System.out.println("Size: " + this.getSize());
		System.out.println("-----------------------------------------");

	}

}