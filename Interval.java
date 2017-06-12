/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2017
// PROJECT:          p4
// FILE:             Interval.java
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
 * Interval class for interval tree class stores a label and start and end
 * 
 * @author Team 91
 */
public class Interval<T extends Comparable<T>> implements IntervalADT<T> {
	private T start; // variable for start of interval
	private T end; // variable for end of interval
	private String label; // variable for string label of node

	/**
	 * Constructor for interval object.
	 *
	 * @param (start)
	 *            start of interval
	 * @param (end)
	 *            end of interval
	 * @param (label)
	 *            label of interval
	 */
	public Interval(T start, T end, String label) {
		this.start = start;
		this.end = end;
		this.label = label;
	}

	/**
	 * Getter method for the start of an interval.
	 *
	 * PRECONDITIONS: (interval is assummed to be non-null)
	 *
	 * @return (start) the start of the interval
	 */
	@Override
	public T getStart() {
		return this.start;
	}

	/**
	 * Getter method for the end of an interval.
	 *
	 * PRECONDITIONS: (interval is assumed to be non-null)
	 *
	 * @return (end) the end of the interval
	 */
	@Override
	public T getEnd() {
		return this.end;
	}

	/**
	 * Getter method for the label of an interval.
	 *
	 * PRECONDITIONS: (interval is assumed to be non-null)
	 *
	 * @return (label) the label of the interval
	 */
	@Override
	public String getLabel() {
		return this.label;
	}

	/**
	 * Checks for overlaps of current interval with passed in interval. Logic:
	 * if an interval overlaps, it means that each intervals contain atlest one
	 * of the same number. i.e. [10,15] and [14,25] each contain 14. Using this
	 * logic, as long as the first number in the interval is not greater than
	 * the last number in the interval, the start of the first interval must be
	 * less than the end of the second interval. (10 <= 25). Subsequently, the
	 * start of the second interval must be less than the end of the first
	 * interval. (14 <= 15). These two comparisons assure that each interval
	 * contains at least one of the same number, confirming an overlap.
	 *
	 * PRECONDITIONS: (interval is assumed to be non-null)
	 *
	 * @return (boolean) true if overlaps
	 */
	@Override
	public boolean overlaps(IntervalADT<T> other) {
		return this.start.compareTo(other.getEnd()) <= 0 && other.getStart().compareTo(this.end) <= 0;
	}

	/**
	 * Checks if the passed in point is within the interval. Logic: using the
	 * compareTo() method, check that the point is greater than or equal to the
	 * start of the interval and check that the point is less than or equal to
	 * the end of interval. If both those conditions are satisfied, the point is
	 * contained in the interval.
	 *
	 * PRECONDITIONS: (interval is assumed to be non-null)
	 *
	 * @return (point) the point that is checked if contained in interval
	 */
	@Override
	public boolean contains(T point) {
		return point.compareTo(this.start) >= 0 && point.compareTo(this.end) <= 0;
	}

	/**
	 * Compares to intervals and returns a negative number if the invterval is
	 * less than the passed in interval, positive if interval is greater than
	 * passed in interval and zero if the intervals are equal. Logic: first
	 * check if starts are the same, if so, compare the ends, otherwise just
	 * compare the starts.
	 *
	 * PRECONDITIONS: (interval is assumed to be non-null)
	 *
	 * @return (other) interval to compare to
	 */
	@Override
	public int compareTo(IntervalADT<T> other) {

		if (this.start == other.getStart()) {
			return this.end.compareTo(other.getEnd());
		}
		return start.compareTo(other.getStart());
	}

	/**
	 * Overridden to string method: formats interval as "label ['start', 'end']"
	 *
	 * @return string name of label
	 */
	public String toString() {
		return label + " [" + start + ", " + end + "]";

	}
}
