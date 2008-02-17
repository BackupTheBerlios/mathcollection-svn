/*
 * @(#)BitMathIntSet.java, 30 Nov 2005
 *
 * This software was developed in a project at the Institute for Intelligent
 * Systems at the University of Stuttgart (http://www.iis.uni-stuttgart.de/)
 * under guidance of Dietmar Lippold
 * (dietmar.lippold@informatik.uni-stuttgart.de).
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


package mathCollection;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * This class represents mathematical sets with integer elements and is backed
 * by an instance of <code>BitSet</code>. It offers destructive as well as
 * non-destructive mathematical set manipulation methods and features several
 * performance tweaks like cached hash code values and cardinality values.<p>
 *
 * Note that this implementation is not synchronized. If multiple threads access
 * a set concurrently, and at least one of the threads modifies the set, it must
 * be synchronized externally.  This is typically accomplished by synchronizing
 * on some object that naturally encapsulates the set.
 *
 * @author S. Hodri, S. Schuetz
 * @version 30 Nov 2005
 * @see BitSet
 */
public class BitMathIntSet implements Cloneable, Serializable {

    /**
     * An iterator class that returns every <code>int</code> element of this set
     * as an <code>Integer</code> object.
     */
    private class BitMathIntSetIterator implements Iterator {
        private int currentInt;
        private int nextInt;
        private boolean removeEnabled;

        public BitMathIntSetIterator() {
            currentInt = -1;
            nextInt = -1;
            removeEnabled = false;
            BitMathIntSet.this.isConcurrentlyModified = false;
        }

        public boolean hasNext() {
            if (BitMathIntSet.this.isConcurrentlyModified) {
                throw new ConcurrentModificationException();
            } else {
                nextInt = myBitSet.nextSetBit(currentInt + 1);
                return (nextInt != -1);
            }
        }

        public Object next() {
            if (hasNext()) {
                currentInt = nextInt;
                removeEnabled = true;
                return new Integer(currentInt);
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (BitMathIntSet.this.isConcurrentlyModified) {
                throw new ConcurrentModificationException();
            } else if (removeEnabled) {
                myBitSet.clear(currentInt);
                if (currentInt == storedMinimum) {
                    storedMinimum = -1;
                }
                if (storedSize > -1) {
                    storedSize -= 1;
                }
                storedHashCode = 0;
                removeEnabled = false;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    /**
     * The backing instance of <code>BitSet</code> where the elements of this
     * set are stored.
     */
    private BitSet myBitSet;

    /**
     * Acts as a cache for the hash code value of this set out of performance
     * considerations. Whenever this set is changed, <code>storedHashCode</code>
     * is set to 0 and gets updated as soon as the <code>hashCode()</code>
     * method is called.
     */
    private int storedHashCode = 0;

    /**
     * Acts as a cache for the cardinality value of this set out of performance
     * considerations. Whenever this set is changed, <code>storedSize</code> is
     * set to -1 and gets updated as soon as the <code>size()</code> method is
     * called.
     */
    private int storedSize = 0;

    /**
     * Acts as a cache for the smallest element of this set out of performance
     * considerations. Whenever a destructive method is called and the new value
     * for <code>storedMinimum</code> cannot be determined in constant time,
     * <code>storedMinimum</code> is set to -1 and gets updated as soon as the
     * <code>getMinimum()</code> method is called.
     */
    private int storedMinimum = -1;

    /**
     * Used to check, whether this mathematical set was modified by an
     * destructive method while iterating over it.
     */
    private boolean isConcurrentlyModified = false;

    /**
     * Constructs a new, empty mathematical set; the backing
     * <code>BitSet</code>'s initial size is large enough to explicitly
     * store integers in the range <code>0</code> through <code>63</code>.
     */
    public BitMathIntSet() {
        myBitSet = new BitSet();
    }

    /**
     * Constructs a new, empty mathematical set; the backing
     * <code>BitSet</code>'s initial size is large enough to explicitly
     * store ints in the range <code>0</code> through <code>nBits - 1</code>.
     *
     * @param   nBits  the initial size of the backing <code>BitSet</code>.
     * @throws  NegativeArraySizeException  if the specified initial size of
     *          the backing <code>BitSet</code> is negative.
     */
    public BitMathIntSet(int nBits) {
        myBitSet = new BitSet(nBits);
    }

    /**
     * Constructs a new mathematical set containing the integer values from the
     * specified array.
     *
     * @param   intArray  the array containing the integer values.
     * @throws  IndexOutOfBoundsException  if an integer in the specified array
     *          is negative.
     */
    public BitMathIntSet(int[] intArray) {
        if (intArray.length > 0) {
            int maxInit = Math.max(intArray.length,
                                   Math.max(intArray[0],
                                            intArray[intArray.length - 1]));
            myBitSet = new BitSet(maxInit);
            for (int counter = 0; counter < intArray.length; counter++) {
                myBitSet.set(intArray[counter]);
            }
            storedSize = -1;
        } else {
            myBitSet = new BitSet();
        }
    }

    /**
     * Returns an iterator over the integers in this mathematical set. The
     * values are returned as <code>Integer</code> objects in ascending order.
     *
     * @return  an Iterator over the integers in this mathematical set.
     * @see     java.util.ConcurrentModificationException
     */
    public Iterator iterator() {
        return new BitMathIntSetIterator();
    }

    /**
     * Returns the hash code value for this mathematical set. This ensures that
     * <code>s1.equals(s2)</code> implies that
     * <code>s1.hashCode()==s2.hashCode()</code> for any two mathematical sets
     * <code>s1</code> and <code>s2</code>, as required by the general contract
     * of <code>Object.hashCode()</code>.<p>
     *
     * This implementation first checks whether a cached hash code value is
     * available. If not (i.e. <code>storedHashCode</code> is zero), the hash
     * code gets calculated using <code>hashCode()</code> of the backing
     * <code>BitSet</code>.
     *
     * @return  the hash code value for this mathematical set.
     */
    public int hashCode() {
        if (storedHashCode == 0) {
            storedHashCode = myBitSet.hashCode();
        }
        return storedHashCode;
    }

    /**
     * Returns the number of integers in this set (its cardinality).<p>
     *
     * This implementation first checks whether a cached cardinality value is
     * available. If not (i.e. <code>storedSize</code> is -1), the cardinality
     * gets calculated using <code>size()</code> of the backing
     * <code>BitSet</code>.
     *
     * @return  the number of integers in this set (its cardinality).
     */
    public int size() {
        if (storedSize == -1) {
            storedSize = myBitSet.cardinality();
        }
        return storedSize;
    }

    /**
     * Returns the smallest integer value in this set (its minimum).If this
     * set is empty then -1 is returned.<p>
     *
     * This implementation first checks whether a cached minimum value is
     * available. If not (i.e. <code>storedMinimum</code> is -1), the minimum
     * gets calculated using <code>nextSetBit(0)</code> of the backing
     * <code>BitSet</code>.
     *
     * @return  the smallest integer value of this set (its minimum).
     */
    public int getMinimum() {
        if (storedMinimum == -1) {
            storedMinimum = myBitSet.nextSetBit(0);
        }
        return storedMinimum;
    }

    /**
     * Returns the highest integer value in this set (its maximum). If this
     * set is empty then -1 is returned.
     *
     * @return  the highest integer value in this set (its maximum).
     */
    public int getMaximum() {
        return myBitSet.length() - 1;
    }

    /**
     * Returns the smallest integer value in this set which is equal or
     * greater than the specified integer (the next integer value). If
     * no such integer value exists then -1 is returned.
     *
     * @param   bitIndex  the index to start checking from (inclusive).
     * @return            the next integer value up from <code>bitIndex</code>.
     * @throws  IndexOutOfBoundsException  if the specified index is negative.
     */
    public int getNext(int bitIndex) {
        if ((storedMinimum > -1) && (bitIndex <= storedMinimum)) {
            return storedMinimum;
        } else {
            return myBitSet.nextSetBit(bitIndex);
        }
    }

    /**
     * Returns <code>true</code> if this set contains no integers.
     *
     * @return  <code>true</code> if this set contains no integers,
     *          <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return myBitSet.isEmpty();
    }

    /**
     * Returns a complete copy of this <code>BitMathIntSet</code> instance.
     *
     * @return  a copy of this mathematical set.
     */
    public Object clone() {
        BitMathIntSet copy = new BitMathIntSet(this.myBitSet.length());

        copy.myBitSet.or(this.myBitSet);
        copy.storedHashCode = this.storedHashCode;
        copy.storedSize = this.storedSize;
        copy.storedMinimum = this.storedMinimum;
        return copy;
    }

    /**
     * Returns an array with the elements of this set, sorted in ascending
     * order.
     *
     * @return  an sorted array with the elements of this set.
     */
    public int[] toArray() {
        int[] elements = new int[size()];
        int elementNumber;

        elementNumber = 0;
        for (int elem = getMinimum();
             elem >= 0;
             elem = myBitSet.nextSetBit(elem + 1)) {
            elements[elementNumber] = elem;
            elementNumber++;
        }
        return elements;
    }

    /**
     * Returns a string representation of this mathematical set. The string
     * representation consists of a list of the set's integers in the
     * order they are returned by its iterator, enclosed in curly brackets
     * (<code>"{}"</code>). Adjacent elements are separated by the characters
     * <code>", "</code> (comma and space).
     *
     * @return  a string representation of this mathematical set.
     */
    public String toString() {
        return myBitSet.toString();
    }

    /**
     * Removes all elements from this set.
     */
    public void clear() {
        myBitSet.clear();
        storedSize = 0;
        storedHashCode = 0;
        storedMinimum = -1;
        isConcurrentlyModified = true;
    }

    /**
     * Returns <code>true</code> if this set contains the specified integer.
     *
     * @param   bitIndex  integer whose presence in this set is to be tested.
     * @return            <code>true</code> if this set contains the specified
     *                    integer, <code>false</code> otherwise.
     * @throws  IndexOutOfBoundsException  if the specified index is negative.
     */
    public boolean contains(int bitIndex) {
        return myBitSet.get(bitIndex);
    }

    /**
     * Adds the specified element to this set if not already present.
     *
     * @param   bitIndex  integer to be added to this set.
     * @return            <code>true</code> if the set did not already contain
     *                    the specified integer, <code>false</code> otherwise.
     * @throws  IndexOutOfBoundsException  if the specified index is negative.
     */
    public boolean add(int bitIndex) {
        if (! myBitSet.get(bitIndex)) {
            if (this.isEmpty()
                || (storedMinimum > -1) && (bitIndex < storedMinimum)) {
                storedMinimum = bitIndex;
            }
            myBitSet.set(bitIndex);
            if (storedSize > -1) {
                storedSize += 1;
            }
            storedHashCode = 0;
            isConcurrentlyModified = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes the specified integer from this set if present.
     *
     * @param   bitIndex  integer to be removed from this set, if present.
     * @return            <code>true</code> if the set contained the specified
     *                    integer, <code>false</code> otherwise.
     * @throws  IndexOutOfBoundsException  if the specified index is negative.
     */
    public boolean remove(int bitIndex) {
        if (myBitSet.get(bitIndex)) {
            myBitSet.clear(bitIndex);
            if (bitIndex == storedMinimum) {
                storedMinimum = -1;
            }
            if (storedSize > -1) {
                storedSize -= 1;
            }
            storedHashCode = 0;
            isConcurrentlyModified = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds all of the integers in the specified <code>BitMathIntSet</code> to
     * this mathematical set.<p>
     *
     * This implementation adds all of the integers by calling
     * <code>or()</code> of the backing <code>BitSet</code>.
     *
     * @param   set  mathematical set whose integers are to be added to this
     *               set.
     * @return       <code>true</code> if this mathematical set changed as a
     *               result of the call, <code>false</code> otherwise.
     */
    public boolean addAll(BitMathIntSet set) {
        int oldStoredSize = this.size();

        myBitSet.or(set.myBitSet);
        storedSize = -1;
        if (this.size() != oldStoredSize) {
            storedHashCode = 0;
            isConcurrentlyModified = true;
            if (oldStoredSize == 0) {
                storedMinimum = set.storedMinimum;
            } else if ((storedMinimum > -1) && (set.storedMinimum > -1)) {
                storedMinimum = Math.min(storedMinimum, set.storedMinimum);
            } else {
                storedMinimum = -1;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retains only the integers in this mathematical set that are contained in
     * the specified <code>BitMathIntSet</code>. In other words, removes
     * from this mathematical set all of its elements that are not contained in
     * the specified <code>BitMathIntSet</code>.<p>
     *
     * This implementation retains all of the integers by calling
     * <code>and()</code> of the backing <code>BitSet</code>.
     *
     * @param   set  integers to be retained in this mathematical set.
     * @return       <code>true</code> if this mathematical set changed as a
     *               result of the call, <code>false</code> otherwise.
     */
    public boolean retainAll(BitMathIntSet set) {
        int oldStoredSize = this.size();

        myBitSet.and(set.myBitSet);
        storedSize = -1;
        if (this.size() != oldStoredSize) {
            storedHashCode = 0;
            isConcurrentlyModified = true;
            if ((this.storedMinimum > -1) && (! myBitSet.get(storedMinimum))) {
                storedMinimum = -1;
            }
            if ((set.storedMinimum > -1) && (myBitSet.get(set.storedMinimum))) {
                storedMinimum = set.storedMinimum;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes from this mathematical set all of its integers that are
     * contained in the specified <code>BitMathIntSet</code>.<p>
     *
     * This implementation removes all of the integers by calling
     * <code>andNot()</code> of the backing <code>BitSet</code>.
     *
     * @param   set  integers to be removed from this mathematical set.
     * @return       <code>true</code> if this mathematical set changed as a
     *               result of the call, <code>false</code> otherwise.
     */
    public boolean removeAll(BitMathIntSet set) {
        int oldStoredSize = this.size();

        myBitSet.andNot(set.myBitSet);
        storedSize = -1;
        if (this.size() != oldStoredSize) {
            storedHashCode = 0;
            isConcurrentlyModified = true;
            if ((storedMinimum > -1) && (! myBitSet.get(storedMinimum))) {
                storedMinimum = -1;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Compares the specified object with this set for equality. Returns
     * <code>true</code> if the specified object is also a
     * <code>BitMathIntSet</code>, the two sets have the same size, and every
     * integer of the specified set is contained in this set.
     *
     * @param   o  object to be compared for equality with this mathematical
     *             set.
     * @return     <code>true</code> if the specified object is equal to this
     *             mathematical set, <code>false</code> otherwise.
     */
    public boolean equals(Object o) {
        if (o instanceof BitMathIntSet) {
            BitMathIntSet objectBitMathIntSet = (BitMathIntSet)o;
            if ((this.hashCode() != objectBitMathIntSet.hashCode())
                || (myBitSet.length() != objectBitMathIntSet.myBitSet.length())
                || ((this.storedSize > -1) && (objectBitMathIntSet.storedSize > -1)
                    && (this.storedSize != objectBitMathIntSet.storedSize))
                || ((this.storedMinimum > -1) && (objectBitMathIntSet.storedMinimum > -1)
                    && (this.storedMinimum != objectBitMathIntSet.storedMinimum))) {
                return false;
            }
            return myBitSet.equals(objectBitMathIntSet.myBitSet);
        } else {
            return false;
        }
    }

    /**
     * Returns <code>true</code> if this mathematical set is a subset of the
     * specified set. That is, if all elements of this mathematical set are also
     * present in the specified set.
     *
     * @param   set  set to be checked for being a superset.
     * @return       <code>true</code> if this mathematical set is a subset of
     *               the specifed set, <code>false</code> otherwise.
     */
    public boolean isSubset(BitMathIntSet set) {
        if ((myBitSet.length() > set.myBitSet.length())
            || ((this.storedSize > -1) && (set.storedSize > -1)
                && (this.storedSize > set.storedSize))
            || ((this.storedMinimum > -1) && (set.storedMinimum > -1)
                && (this.storedMinimum < set.storedMinimum))) {
            return false;
        }
        BitMathIntSet intersection = this.intersection(set);
        return intersection.equals(this);
    }

    /**
     * Returns <code>true</code> if this mathematical set is a superset of the
     * specified set. That is, if all elements of the specified set are also
     * present in this mathematical set.
     *
     * @param   set  set to be checked for being a subset.
     * @return       <code>true</code> if this mathematical set is a superset
     *               of the specifed set, <code>false</code> otherwise.
     */
    public boolean isSuperset(BitMathIntSet set) {
        if ((myBitSet.length() < set.myBitSet.length())
            || ((this.storedSize > -1) && (set.storedSize > -1)
                && (this.storedSize < set.storedSize))
            || ((this.storedMinimum > -1) && (set.storedMinimum > -1)
                && (this.storedMinimum > set.storedMinimum))) {
            return false;
        }
        BitMathIntSet intersection = this.intersection(set);
        return intersection.equals(set);
    }

    /**
     * Returns <code>true</code> if this mathematical set has no common
     * elements with the specified set.<p>
     *
     * This implementation calls <code>intersects()</code> of the backing
     * <code>BitSet</code>.
     *
     * @param   set  set to be checked for common integers.
     * @return       <code>true</code> if this mathematical set has no common
     *               elements with the specifed set, <code>false</code>
     *               otherwise.
     */
    public boolean isDisjoint(BitMathIntSet set) {
        return ! myBitSet.intersects(set.myBitSet);
    }

    /**
     * Returns the union with the specified set. This is a new
     * <code>BitMathIntSet</code> containing all integers that are present in
     * this mathematical set or in the specified set.
     *
     * @param   set  set to be united with this one.
     * @return       the union with the specified set.
     */
    public BitMathIntSet union(BitMathIntSet set) {
        BitMathIntSet union;

        if (set.isEmpty()) {
            union = (BitMathIntSet)this.clone();
            return union;
        } else if (this.isEmpty()) {
            union = (BitMathIntSet)set.clone();
            return union;
        }

        if (myBitSet.length() > set.myBitSet.length()) {
            union = (BitMathIntSet)this.clone();
            union.myBitSet.or(set.myBitSet);
        } else {
            union = (BitMathIntSet)set.clone();
            union.myBitSet.or(this.myBitSet);
        }
        union.storedHashCode = 0;
        union.storedSize = -1;
        if ((storedMinimum > -1) && (set.storedMinimum > -1)) {
            union.storedMinimum = Math.min(storedMinimum, set.storedMinimum);
        } else {
            union.storedMinimum = -1;
        }
        return union;
    }

    /**
     * Returns the intersection with the specified set. This is a new
     * <code>BitMathIntSet</code> containing all integers that are present in
     * this mathematical set as well as in the specified set.
     *
     * @param   set  set to be intersected with this one.
     * @return       the intersection with the specified set.
     */
    public BitMathIntSet intersection(BitMathIntSet set) {
        BitMathIntSet intersection;

        if (set.isEmpty() || this.isEmpty()) {
            intersection = new BitMathIntSet();
            return intersection;
        }

        if (myBitSet.length() > set.myBitSet.length()) {
            intersection = (BitMathIntSet)set.clone();
            intersection.myBitSet.and(this.myBitSet);
        } else {
            intersection = (BitMathIntSet)this.clone();
            intersection.myBitSet.and(set.myBitSet);
        }
        intersection.storedHashCode = 0;
        intersection.storedSize = -1;
        if ((storedMinimum > -1) && (intersection.myBitSet.get(storedMinimum))) {
            intersection.storedMinimum = storedMinimum;
        } else if ((set.storedMinimum > -1)
                    && (intersection.myBitSet.get(set.storedMinimum))) {
            intersection.storedMinimum = set.storedMinimum;
        } else {
            intersection.storedMinimum = -1;
        }
        return intersection;
    }

    /**
     * Returns the asymmetric difference between this mathematical set and the
     * specified set. This is a new <code>BitMathIntSet</code> containing all
     * integers that are present in this mathematical set but not in the
     * specified set.
     *
     * @param   set  set which gets subtracted from this mathematical set.
     * @return       the difference with the specified set.
     */
    public BitMathIntSet difference(BitMathIntSet set) {
        BitMathIntSet difference;

        if (set.isEmpty()) {
            difference = (BitMathIntSet)this.clone();
            return difference;
        } else if (this.isEmpty()) {
            difference = new BitMathIntSet();
            return difference;
        }

        difference = (BitMathIntSet)this.clone();
        difference.myBitSet.andNot(set.myBitSet);
        difference.storedHashCode = 0;
        difference.storedSize = -1;
        if ((storedMinimum > -1) && (! difference.myBitSet.get(storedMinimum))) {
            difference.storedMinimum = -1;
        } else {
            difference.storedMinimum = storedMinimum;
        }
        return difference;
    }

    /**
     * Returns the symmetric difference between this mathematical set and the
     * specified set. This is a new <code>BitMathIntSet</code> containing all
     * integers that are present either in this mathematical set or in the
     * specified set but not in both.
     *
     * @param   set  set from which the symmetric difference is calculated.
     * @return       the symmetric difference with the specified set.
     */
    public BitMathIntSet symmetricDifference(BitMathIntSet set) {
        BitMathIntSet symmDiff;

        if (set.isEmpty()) {
            symmDiff = (BitMathIntSet)this.clone();
            return symmDiff;
        } else if (this.isEmpty()) {
            symmDiff = (BitMathIntSet)set.clone();
            return symmDiff;
        }

        if (this.myBitSet.length() > set.myBitSet.length()) {
            symmDiff = (BitMathIntSet)this.clone();
            symmDiff.myBitSet.xor(set.myBitSet);
        } else {
            symmDiff = (BitMathIntSet)set.clone();
            symmDiff.myBitSet.xor(this.myBitSet);
        }
        symmDiff.storedHashCode = 0;
        symmDiff.storedSize = -1;
        if ((storedMinimum > -1) && (set.storedMinimum > -1)
            && (storedMinimum != set.storedMinimum)) {
            symmDiff.storedMinimum = Math.min(storedMinimum, set.storedMinimum);
        } else {
            symmDiff.storedMinimum = -1;
        }
        return symmDiff;
    }
}
