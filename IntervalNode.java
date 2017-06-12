/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2017
// PROJECT:          p4
// FILE:             IntervalNode.java
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

/**
 * This class defines the IntervalNode for the IntervalTree. This node has three
 * components: 1) interval - the data that we want to store in this node 2)
 * maxEnd - this represents the maximum end of any interval stored in the tree
 * rooted at this node 3) leftNode and rightNode - the left and right node
 * references in the IntervalTree.
 * 
 * This class will be used while constructing the IntervalTree.
 *
 * @param <T>
 *            the template parameter for the data field - interval.
 */

public class IntervalNode<T extends Comparable<T>> {
	// Interval stored in the node.
	private IntervalADT<T> interval;

	// Each node stores the maxEnd of the interval in its subtree.
	private T maxEnd;

	// LeftNode and RightNode.
	private IntervalNode<T> leftNode, rightNode;

	/**
	 * Constructor to create a new IntervalNode. Set the interval data structure
	 * present as member variable above and maxEnd associated with the interval.
	 * Hint: Use interval.getEnd() to get the end of the interval. Note: In your
	 * intervalTree, this will get updated subsequently.
	 * 
	 * @param interval
	 *            the interval data member.
	 */
	public IntervalNode(IntervalADT<T> interval) {
		this.interval = interval;
		maxEnd = interval.getEnd();
		leftNode = null;
		rightNode = null;
	}

	/**
	 * Returns the next in-order successor of the BST. Hint: Return left-most
	 * node in the right subtree. Return null if there is no rightNode.
	 * Initially checks if the node has a right node. If so, calls the private
	 * helper getSuccessor method on the node's right node.
	 *
	 * @return in-order successor node
	 */
	public IntervalNode<T> getSuccessor() {
		if (this.rightNode == null)
			return null;
		return getSuccessor(this.rightNode);
	}

	/**
	 * Returns the interval associated with the node.
	 * 
	 * @return the interval data field.
	 */
	public IntervalADT<T> getInterval() {
		return interval;
	}

	/**
	 * Setter for the interval.
	 * 
	 * @param interval
	 *            the interval to be set at this node.
	 */
	public void setInterval(IntervalADT<T> interval) {
		this.interval = interval;
	}

	/**
	 * Setter for the maxEnd. This represents the maximum end point associated
	 * in any interval stored at the subtree rooted at this node (inclusive of
	 * this node).
	 * 
	 * @param maxEnd
	 *            the maxEnd associated with this node.
	 *
	 */
	public void setMaxEnd(T maxEnd) {
		this.maxEnd = maxEnd;
	}

	/**
	 * Getter for the maxEnd member variable.
	 * 
	 * @return the maxEnd.
	 */
	public T getMaxEnd() {
		return maxEnd;
	}

	/**
	 * Getter for the leftNode reference.
	 *
	 * @return the reference of leftNode.
	 */
	public IntervalNode<T> getLeftNode() {
		return leftNode;
	}

	/**
	 * Setter for the leftNode of this node.
	 * 
	 * @param leftNode
	 *            the left node.
	 */
	public void setLeftNode(IntervalNode<T> leftNode) {
		this.leftNode = leftNode;
	}

	/**
	 * Getter for the rightNode of this node.
	 * 
	 * @return the rightNode.
	 */
	public IntervalNode<T> getRightNode() {
		return rightNode;
	}

	/**
	 * Setter for the rightNode of this node.
	 * 
	 * @param rightNode
	 *            the rightNode of this node.
	 */
	public void setRightNode(IntervalNode<T> rightNode) {
		this.rightNode = rightNode;
	}

	/**
	 * Checks if the current node has a left node, if not, returns the node as
	 * the successor. If current node does have a left node, recursively calls
	 * the getSuccessor method on the left node. This simply traverses to the
	 * left most node of the right subtree of the original node in the public
	 * getSuccessor method.
	 * 
	 * @param node
	 *            the rightNode of the node called by the public getSuccessor
	 *            method
	 */
	private IntervalNode<T> getSuccessor(IntervalNode<T> node) {
		if (node.leftNode == null)
			return node;
		return getSuccessor(node.leftNode);
	}
}
