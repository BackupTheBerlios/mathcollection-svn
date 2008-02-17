/*
 * @(#)HashMathSet.java, 03 Jan 2004
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

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;

/**
 * This class implements the <code>MathSet</code> interface, backed by a hash
 * table (specifically, a <code>HashSet</code> instance). It makes no
 * guarantees as to the iteration order of the set; in particular, it does not
 * guarantee that the order will remain constant over time. This class permits
 * the <code>null</code> element.<p>
 *
 * Note that this implementation is not synchronized. If multiple
 * threads access a set concurrently, and at least one of the threads modifies
 * the set, it must be synchronized externally.  This is typically
 * accomplished by synchronizing on some object that naturally encapsulates
 * the set.  If no such object exists, the set should be "wrapped" using the
 * <code>MathCollections.synchronizedMathSet</code> method.  This is best done
 * at creation time, to prevent accidental unsynchronized access to the
 * <code>HashMathSet</code> instance:
 *
 * <pre>
 *     MathSet ms = MathCollections.synchronizedMathSet(new HashMathSet(...));
 * </pre>
 *
 * @author S. Hodri, S. Schuetz
 * @version 03 Jan 2004
 * @see Set
 * @see MathSet
 * @see AbstractMathSet
 * @see HashSet
 */
public class HashMathSet extends AbstractMathSet implements MathSet, Cloneable, Serializable {

    /**
     * A 'wrapper' iterator class that uses <code>HashSet.iterator()</code>
     * while accounting for the additional <code>storedHashCode</code>
     * attribute.
     */
    private class HashMathSetIterator implements Iterator {
        private Iterator myIterator;

        public HashMathSetIterator() {
            myIterator = myHashSet.iterator();
        }

        public boolean hasNext() {
            return myIterator.hasNext();
        }

        public Object next() {
            return myIterator.next();
        }

        public void remove() {
            storedHashCode = 0;
            myIterator.remove();
        }
    }

    /**
     * The backing instance of <code>HashSet<code> where the elements of this
     * set are stored.
     */
    private HashSet myHashSet;

    /**
     * Acts as a cache for the hash code value of this set out of performance
     * considerations.
     * Whenever this set is changed, <code>storedHashCode</code> is set to 0 and
     * gets updated as soon as the <code>hashCode()</code> method is called.
     */
    private int storedHashCode = 0;

    /**
     * Constructs a new, empty mathematical set; the backing
     * <code>HashSet</code> instance has default initial capacity (16) and
     * load factor (0.75).
     */
    public HashMathSet() {
        myHashSet = new HashSet();
    }

    /**
     * Constructs a new mathematical set containing the elements in the
     * specified collection. The <code>HashSet</code> is created with default
     * load factor (0.75) and an initial capacity sufficient to contain the
     * elements in the specified collection.
     *
     * @param c  the collection whose elements are to be placed into this set.
     * @throws   NullPointerException if the specified collection is null.
     */
    public HashMathSet(Collection c) {
        myHashSet = new HashSet(c);
    }

    /**
     * Constructs a new, empty mathematical set; the backing
     * <code>HashSet</code> instance has the specified initial capacity and
     * default load factor, which is 0.75.
     *
     * @param      initialCapacity   the initial capacity of the hash set.
     * @throws     IllegalArgumentException if the initial capacity is less
     *             than zero.
     */
    public HashMathSet(int initialCapacity) {
        myHashSet = new HashSet(initialCapacity);
    }

    /**
     * Constructs a new, empty mathematical set; the backing
     * <code>HashSet</code> instance has the specified initial capacity and
     * the specified load factor.
     *
     * @param      initialCapacity   the initial capacity of the hash set.
     * @param      loadFactor        the load factor of the hash set.
     * @throws     IllegalArgumentException if the initial capacity is less
     *             than zero, or if the load factor is nonpositive.
     */
    public HashMathSet(int initialCapacity, float loadFactor) {
        myHashSet = new HashSet(initialCapacity, loadFactor);
    }

    /**
     * Returns an iterator over the elements in this mathematical set. The
     * elements are returned in no particular order.
     *
     * @return  an Iterator over the elements in this mathematical set.
     * @see     java.util.ConcurrentModificationException
     */
    public Iterator iterator() {
        return new HashMathSetIterator();
    }

    /**
     * Returns the hash code value for this mathematical set. To get the hash
     * code of this mathematical set, new hash code values for every element of
     * this mathematical set are calculated from a polynomial of 3rd order and
     * finally summed up.
     * This ensures that <code>s1.equals(s2)</code> implies that
     * <code>s1.hashCode()==s2.hashCode()</code> for any two mathematical sets
     * <code>s1</code> and <code>s2</code>, as required by the general contract
     * of <code>Object.hashCode()</code>.<p>
     *
     * This implementation first checks whether a cached hash code value is
     * available. If not (i.e. <code>storedHashCode</code> is zero), the hash
     * code gets calculated using <code>hashCode()</code> of the super class.
     *
     * @return  the hash code value for this mathematical set.
     */
    public int hashCode() {
        if (storedHashCode == 0) {
            storedHashCode = super.hashCode();
        }
        return storedHashCode;
    }

    /**
     * Returns a shallow copy of this <code>HashMathSet</code> instance: the
     * elements themselves are not cloned.
     *
     * @return  a shallow copy of this set.
     */
    public Object clone() {
        HashMathSet copy = new HashMathSet(this);
        copy.storedHashCode = this.storedHashCode;
        return copy;
    }

    /**
     * Returns <code>true</code> if this set contains no elements.
     *
     * @return  <code>true</code> if this set contains no elements,
     *          <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return myHashSet.isEmpty();
    }

    /**
     * Returns the number of elements in this set (its cardinality).
     *
     * @return  the number of elements in this set (its cardinality).
     */
    public int size() {
        return myHashSet.size();
    }

    /**
     * Removes all elements from this set.
     */
    public void clear() {
        myHashSet.clear();
        this.storedHashCode = 0;
    }

    /**
     * Returns <code>true</code> if this set contains the specified element.
     *
     * @param o  element whose presence in this set is to be tested.
     * @return   <code>true</code> if this set contains the specified element,
     *           <code>false</code> otherwise.
     */
    public boolean contains(Object o) {
        return myHashSet.contains(o);
    }

    /**
     * Adds the specified element to this set if it is not already
     * present.<p>
     *
     * If the set gets altered, this implementation sets
     * <code>storedHashCode</code> to 0 (representing an unavailable hash code
     * value), which forces <code>hashCode()</code> to recalculate the actual
     * hash code value.
     *
     * @param o  element to be added to this set.
     * @return   <code>true</code> if the set did not already contain the
     *           specified element, <code>false</code> otherwise.
     */
    public boolean add(Object o) {
        if (myHashSet.add(o)) {
            this.storedHashCode = 0;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes the specified element from this set if it is present.<p>
     *
     * If the set gets altered, this implementation sets
     * <code>storedHashCode</code> to 0 (representing an unavailable hash code
     * value), which forces <code>hashCode()</code> to recalculate the actual
     * hash code value.
     *
     * @param o  object to be removed from this set, if present.
     * @return   <code>true</code> if the set contained the specified element,
     *           <code>false</code> otherwise.
     */
    public boolean remove(Object o) {
        if (myHashSet.remove(o)) {
            this.storedHashCode = 0;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Compares the specified object with this set for equality. Returns
     * <code>true</code> if the specified object is also a set, the two sets
     * have the same size, and every element of the specified set is contained
     * in this set.<p>
     *
     * This implementation first checks if the given object is a
     * <code>HashMathSet</code>. If so, the hash code values of this
     * mathematical set and the specified <code>HashMathSet</code> are compared.
     * Since the hash code values are being cached, this represents a quick
     * solution if the sets aren't equal. However, if the hash code values are
     * equal, it cannot be assumed that the sets themselves are equal,
     * since different sets can have the same hash code value. In this case,
     * the result of the super method <code>equals()</code> is returned.
     *
     * @param o  object to be compared for equality with this set.
     * @return   <code>true</code> if the specified object is equal to this set,
     *           <code>false</code> otherwise.
     */
    public boolean equals(Object o) {
        if (o instanceof HashMathSet) {
            if (this.hashCode() != ((HashMathSet)o).hashCode()) {
                return false;
            }
        }
        return super.equals(o);
    }

    /**
     * Returns <code>true</code> if this mathematical set is a subset of the
     * specified set. That is, if all elements of this mathematical set are also
     * present in the specified set.
     *
     * @param s  set to be checked for being a superset.
     * @return   <code>true</code> if this mathematical set is a subset of the
     *           specifed set, <code>false</code> otherwise.
     */
    public boolean isSubset(Set s) {
        if (this.size() <= s.size()) {
            if (s instanceof HashMathSet) {
                return s.containsAll(this);
            } else {
                return super.isSubset(s);
            }
        } else {
            return false;
        }
    }

    /**
     * Returns the union with the specified set. This is a new
     * <code>HashMathSet</code> containing all elements that are present in this
     * mathematical set or in the specified set.
     *
     * @param s  set that is to be united with.
     * @return   the union with the specified set.
     */
    public MathSet union(Set s) {
        HashMathSet copy = new HashMathSet(this);
        copy.addAll(s);
        return copy;
    }

    /**
     * Returns the intersection with the specified set. This is a new
     * <code>HashMathSet</code> containing all elements that are present in this
     * mathematical set as well as in the specified set.
     *
     * @param s  set that is to be intersected with.
     * @return   the intersection with the specified set.
     */
    public MathSet intersection(Set s) {
        HashMathSet resultingSet = new HashMathSet(Math.min(s.size(), this.size()));
        Object currentElement;

        if ((s instanceof HashMathSet) && (s.size() > this.size())) {
            /*
             * time  complexity = O(min{|s|, |this|})
             * space complexity = O(|resultingSet|)
             */
            for (Iterator iter = this.iterator(); iter.hasNext(); ) {
                currentElement = iter.next();
                if (s.contains(currentElement)) {
                    resultingSet.add(currentElement);
                }
            }
        } else {
            /*
             * time  complexity = O(|s|)
             * space complexity = O(|resultingSet|)
             */
            for (Iterator iter = s.iterator(); iter.hasNext(); ) {
                currentElement = iter.next();
                if (this.contains(currentElement)) {
                    resultingSet.add(currentElement);
                }
            }
        }
        return resultingSet;
    }

    /**
     * Returns the asymmetric difference between this mathematical set and the
     * specified set. This is a new <code>HashMathSet</code> containing all
     * elements that are present in this mathematical set but not in the
     * specified set.
     *
     * @param s  set from what the difference is calculated.
     * @return   the difference with the specified set.
     */
    public MathSet difference(Set s) {
        HashMathSet resultingSet;
        Object currentElement;

        if (s instanceof HashMathSet) {
            /*
             * time  complexity = O(|this|)
             * space complexity = O(|this|)
             */
            resultingSet = new HashMathSet(this.size());
            for (Iterator iter = this.iterator(); iter.hasNext(); ) {
                currentElement = iter.next();
                if (! s.contains(currentElement)) {
                    resultingSet.add(currentElement);
                }
            }
        } else {
            /*
             * time  complexity = O(|this| + |s|)
             * space complexity = O(|this|)
             */
            resultingSet = new HashMathSet(this);
            resultingSet.removeAll(s);
        }
        return resultingSet;
    }

    /**
     * Returns the symmetric difference between this mathematical set and the
     * specified set. This is a new <code>HashMathSet</code> containing all
     * elements that are present either in this mathematical set or in the
     * specified set but not in both.
     *
     * @param s  set from what the symmetric difference is calculated
     * @return   the symmetric difference with the specified set.
     */
    public MathSet symmetricDifference(Set s) {
        HashMathSet resultingSet;
        Iterator iterSet;
        Iterator iterThis;
        Object currentElement;

        if (s instanceof HashMathSet) {
            resultingSet = new HashMathSet();
            for (iterThis = this.iterator(); iterThis.hasNext(); ) {
                currentElement = iterThis.next();
                if (! s.contains(currentElement)) {
                    resultingSet.add(currentElement);
                }
            }
            for (iterSet = s.iterator(); iterSet.hasNext(); ) {
                currentElement = iterSet.next();
                if (! this.contains(currentElement)) {
                    resultingSet.add(currentElement);
                }
            }
        } else {
            resultingSet = new HashMathSet(s);
            for (iterThis = this.iterator(); iterThis.hasNext(); ) {
                currentElement = iterThis.next();
                if (resultingSet.contains(currentElement)) {
                    resultingSet.remove(currentElement);
                } else {
                    resultingSet.add(currentElement);
                }
            }
        }

        return resultingSet;
    }

    /**
     * Returns the power set of this mathematical set. This is a set containing
     * all subsets of this mathematical set, including, in particular, the empty
     * set and this mathematical set itself.
     *
     * @return  power set of this mathematical set.
     */
    public SetOfSets powerSet() {
        SetOfSets powSet = new HashSetOfSets();
        SetOfSets powNewElements = new HashSetOfSets();
        HashMathSet element = new HashMathSet();

        // The power set has to contain the empty set
        powSet.add((HashMathSet)element.clone());

        for (Iterator iterThisSet = this.iterator(); iterThisSet.hasNext(); ) {
            element.clear();
            powNewElements.clear();

            element.add(iterThisSet.next());

            for (Iterator iterPowerSet = powSet.iterator(); iterPowerSet.hasNext(); ) {
                powNewElements.add(element.union((HashMathSet)iterPowerSet.next()));
            }
            powSet.addAll(powNewElements);
        }

        return powSet;
    }

    /**
     * Internal subroutine for recursively determining all fixed size subsets
     * of a set.
     *
     * @param numberToAdd       number of elements to add to the subsets.
     *                          Required to be a positive integer.
     * @param elementPool       array containing all source set elements.
     * @param elementPoolStart  array index specifying the first of the
     *                          elements that are still to be processed.
     * @param setToAdd          the incomplete subset which more elements
     *                          get added to.
     * @param result            <code>SetOfSets</code> accumulating the
     *                          complete subsets.
     * @return                  an intermediate result according to the
     *                          parameters.
     */
    private HashSetOfSets recursiveFixedSizeSubsets(int numberToAdd,
                                                    Object[] elementPool,
                                                    int elementPoolStart,
                                                    HashMathSet setToAdd,
                                                    HashSetOfSets result) {
        if (numberToAdd == 1) {
            for (int i = elementPoolStart; i < elementPool.length; i++) {
                setToAdd.add(elementPool[i]);
                result.add(setToAdd.clone());
                setToAdd.remove(elementPool[i]);
            }
        } else {
            for (int i = elementPoolStart; i < elementPool.length; i++) {
                setToAdd.add(elementPool[i]);
                recursiveFixedSizeSubsets(numberToAdd - 1, elementPool, i + 1,
                                          setToAdd, result);
                setToAdd.remove(elementPool[i]);
            }
        }
        return result;
    }

    /**
     * Returns all possible subsets of this set with the specified size,
     * contained in a <code>HashSetOfSets</code> instance. In case the
     * <code>size</code> parameter is negative or greater than the size of the
     * set itself, an empty <code>SetOfSets</code> is returned.
     *
     * @param size  size of the returned subsets.
     * @return      a <code>SetOfSets</code> containing all subsets of this
     *              mathematical set with the specified size.
     */
    public SetOfSets fixedSizeSubsets(int size) {
        if ((size < 0) || (size > this.size())) {
            return new HashSetOfSets();
        } else {
            if (size == 0) {
                HashSetOfSets empty = new HashSetOfSets();
                empty.add(new HashMathSet());
                return empty;
            } else {
                return recursiveFixedSizeSubsets(size,
                                                 this.toArray(),
                                                 0,
                                                 new HashMathSet(),
                                                 new HashSetOfSets());
            }
        }
    }
}
