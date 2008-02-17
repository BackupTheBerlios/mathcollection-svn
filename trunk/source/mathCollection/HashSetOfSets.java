/*
 * @(#)HashSetOfSets.java, 03 Feb 2004
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

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;
import java.io.Serializable;

/**
 * This class implements the <code>SetOfSets</code> interface, backed by a
 * hash table (specifically, a <code>HashSet</code> instance). It makes no
 * guarantees as to the iteration order of the set; in particular, it does not
 * guarantee that the order will remain constant over time. An instance of
 * <code>HashMultiset</code> is used for permanently maintaining a 'flat'
 * version of the set of sets (a simple set containing all the elements from the
 * elementary sets once).<p>
 *
 * Note that this implementation is not synchronized. If multiple
 * threads access a set concurrently, and at least one of the threads modifies
 * the set, it must be synchronized externally.  This is typically
 * accomplished by synchronizing on some object that naturally encapsulates
 * the set.  If no such object exists, the set should be "wrapped" using the
 * <code>MathCollections.synchronizedSetOfSets</code> method.  This is best done
 * at creation time, to prevent accidental unsynchronized access to the
 * <code>HashSetOfSets</code> instance:
 *
 * <pre>
 *    SetOfSets sos = MathCollections.synchronizedSetofSets(new HashSetofSets(...));
 * </pre>
 *
 * @author S. Hodri, S. Schuetz
 * @version 03 Feb 2004
 * @see Set
 * @see MathSet
 * @see AbstractSetOfSets
 * @see HashSet
 * @see java.util.HashMap
 */
public class HashSetOfSets extends AbstractSetOfSets implements SetOfSets, Cloneable, Serializable {

    /**
     * A 'wrapper' iterator class that uses <code>HashSet.iterator()</code>
     * while accounting for the additional storedHashCode attribute.
     */
    private class HashSetOfSetsIterator implements Iterator {
        private Iterator myIterator;
        private Object currentElement;

        public HashSetOfSetsIterator() {
            myIterator = myHashSet.iterator();
        }

        public boolean hasNext() {
            return myIterator.hasNext();
        }

        public Object next() {
            currentElement = myIterator.next();
            return currentElement;
        }

        public void remove() {
            storedHashCode = 0;
            myIterator.remove();
            flatVersion.removeAll((Collection)currentElement);
        }
    }

    /**
     * The backing instance of <code>HashSet<code> where the elements of this
     * set are stored.
     */
    private HashSet myHashSet;

    /**
     * Acts as a cache for the 'flattened' multiset version of this set of sets
     * out of performance considerations. The contents of flatVersion are always
     * up-to-date since they get updated by all destructive methods in this
     * class.
     */
    private HashMultiset flatVersion;

    /**
     * Acts as a cache for the hash code value of this set out of performance
     * considerations.
     * Whenever this set is changed, <code>storedHashCode</code> is set to 0 and
     * gets updated as soon as the <code>hashCode()</code> method is called.
     */
    private int storedHashCode = 0;

    /**
     * Constructs a new, empty set of sets; the backing <code>HashSet</code>
     * instance as well as the <code>HashMap flatVersion</code> have default
     * initial capacity (16) and load factor (0.75).
     */
    public HashSetOfSets() {
        myHashSet = new HashSet();
        flatVersion = new HashMultiset();
    }

    /**
     * Constructs a new set of sets set containing the elements in the
     * specified collection. The <code>HashSet</code> is created with default
     * load factor (0.75) and an initial capacity sufficient to contain the
     * elements in the specified collection.
     *
     * @param c  the collection whose elements are to be placed into this set.
     * @throws   java.lang.NullPointerException if the specified collection is
     *           null.
     */
    public HashSetOfSets(Collection c) {
        myHashSet = new HashSet(c);
        flatVersion = new HashMultiset(c);
    }

    /**
     * Constructs a new, empty set of sets; the backing <code>HashSet</code>
     * instance has the specified initial capacity and default load factor,
     * which is 0.75.
     *
     * @param      initialCapacity   the initial capacity of the hash set.
     * @throws     java.lang.IllegalArgumentException if the initial capacity is
     *             less than zero.
     */
    public HashSetOfSets(int initialCapacity) {
        myHashSet = new HashSet(initialCapacity);
        flatVersion = new HashMultiset(initialCapacity);
    }

    /**
     * Constructs a new, empty set of sets; the backing <code>HashSet</code>
     * instance has the specified initial capacity and the specified load
     * factor.
     *
     * @param      initialCapacity   the initial capacity of the hash set.
     * @param      loadFactor        the load factor of the hash set.
     * @throws     IllegalArgumentException if the initial capacity is less
     *             than zero, or if the load factor is nonpositive.
     */
    public HashSetOfSets(int initialCapacity, float loadFactor) {
        myHashSet = new HashSet(initialCapacity, loadFactor);
        flatVersion = new HashMultiset(initialCapacity, loadFactor);
    }

    /**
     * Returns an iterator over the elements in this set of sets. The elements
     * are returned in no particular order.
     *
     * @return  an iterator over the elements in this set of sets.
     * @see     java.util.ConcurrentModificationException
     */
    public Iterator iterator() {
        return new HashSetOfSetsIterator();
    }

    /**
     * Returns the hash code value for this set of sets. To get the hash
     * code of this set of sets, new hash code values for every element of
     * this set of sets are calculated from a polynomial of 3rd order and
     * finally summed up.
     * This ensures that <code>s1.equals(s2)</code> implies that
     * <code>s1.hashCode()==s2.hashCode()</code> for any two sets
     * <code>s1</code> and <code>s2</code>, as required by the general contract
     * of Object.hashCode.<p>
     *
     * This implementation first checks whether a cached hash code value is
     * available. If not (i.e. <code>storedHashCode</code> is zero), the hash
     * code gets calculated using <code>hashCode()</code> of the super class.
     *
     * @return  the hash code value for this set of sets.
     */
    public int hashCode() {
        if (storedHashCode == 0) {
            storedHashCode = super.hashCode();
        }
        return storedHashCode;
    }

    /**
     * Returns a shallow copy of this <code>HashSetOfSets</code> instance: the
     * elements themselves are not cloned.
     *
     * @return  a shallow copy of this set.
     */
    public Object clone() {
        HashSetOfSets copy = new HashSetOfSets(this);
        copy.storedHashCode = this.storedHashCode;
        return copy;
    }

    /**
     * Returns <code>true</code> if this set of sets contains no elements.
     *
     * @return  <code>true</code> if this set of sets contains no elements,
     *          <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return myHashSet.isEmpty();
    }

    /**
     * Returns the number of elements (contained sets) in this set of sets
     * (its cardinality).
     *
     * @return  the number of elements in this set of sets (its cardinality).
     */
    public int size() {
        return myHashSet.size();
    }

    /**
     * Removes all elements from this set of sets.
     */
    public void clear() {
        this.storedHashCode = 0;
        myHashSet.clear();
        flatVersion.clear();
    }

    /**
     * Adds the specified element (Set) to this set of sets
     * if it is not already present.<p>
     *
     * If the set gets altered, this implementation sets
     * <code>storedHashCode</code> to 0 (representing an unavailable hash code
     * value), which forces <code>hashCode()</code> to recalculate the actual
     * hash code value.
     *
     * @param o  element to be added to this set of sets.
     * @return   <code>true</code> if the set of sets did not already contain
     *           the specified element, <code>false</code> otherwise.
     * @throws   ClassCastException if the type of the specified element
     *           is incompatible (<code>!(o instanceof Set)</code>).
     */
    public boolean add(Object o) {
        if (o instanceof Set) {
            if (myHashSet.add(o)) {
                // Add the set elements to flatVersion
                flatVersion.addAll((Collection)o);
                this.storedHashCode = 0;
                return true;
            } else {
                return false;
            }
        } else {
            throw (new ClassCastException());
        }
    }

    /**
     * Removes the specified element (Set) from this set of sets if it is
     * present.<p>
     *
     * If the set gets altered, this implementation sets
     * <code>storedHashCode</code> to 0 (representing an unavailable hash code
     * value), which forces <code>hashCode()</code> to recalculate the actual
     * hash code value.
     *
     * @param o  object to be removed from this set of sets, if present.
     * @return   <code>true</code> if the set of sets contained the specified
     *           element, <code>false</code> otherwise.
     * @throws   ClassCastException if the type of the specified element
     *           is incompatible (<code>!(o instanceof Set)</code>).
     */
    public boolean remove(Object o) {
        if (o instanceof Set) {
            if (myHashSet.remove(o)) {
                flatVersion.removeAll((Collection)o);
                this.storedHashCode = 0;
                return true;
            } else {
                return false;
            }
        } else {
            throw (new ClassCastException());
        }
    }

    /**
     * Returns <code>true</code> if this set of sets contains the specified
     * element (Set).
     *
     * @param o  element whose presence in this set of sets is to be tested.
     * @return   <code>true</code> if this set of sets contains the specified
     *           element, <code>false</code> otherwise.
     * @throws   ClassCastException if the type of the specified element
     *           is incompatible (<code>!(o instanceof Set)</code>).
     */
    public boolean contains(Object o) {
        if (o instanceof Set) {
            return myHashSet.contains(o);
        } else {
            throw (new ClassCastException());
        }
    }

    /**
     * Compares the specified object with this set of sets for equality.
     * Returns <code>true</code> if the specified object is also a set, the two
     * sets have the same size, and every element of the specified set is
     * contained in this set.<p>
     *
     * This implementation first checks if the given object is a
     * <code>HashSetOfSets</code>. If so, the hash code values of this
     * set of sets and the specified <code>HashSetOfSets</code> are compared.
     * Since the hash code values are being cached, this represents a quick
     * solution if the sets aren't equal. However, if the hash code values are
     * equal, it cannot be assumed that the sets themselves are equal,
     * since different sets can have the same hash code value. In this case,
     * the result of the super method <code>equals()</code> is returned.
     *
     * @param o  object to be compared for equality with this set of sets.
     * @return   <code>true</code> if the specified object is equal to this set
     *           of sets, <code>false</code> otherwise.
     */
    public boolean equals(Object o) {
        if (o instanceof HashSetOfSets) {
            if (this.hashCode() != ((HashSetOfSets)o).hashCode()) {
                return false;
            }
        }
        return super.equals(o);
    }

    /**
     * Returns <code>true</code> if this set of sets contains a set in which the
     * specified element is present.
     *
     * @param o  the element whose presence in any elementary set within this
     *           set of sets is tested for.
     * @return   <code>true</code> if any set within this set of sets contains
     *           the specified element, <code>false</code> otherwise.
     */
    public boolean containsAtom(Object o) {
        return flatVersion.contains(o);
    }

    /**
     * Returns the 'flattened' version of this set of sets, in which every
     * element from the elementary sets is present once.
     *
     * @return  the 'flattened' version (simple set) of this set of sets.
     */
    public Set toSet() {
        return flatVersion.toSet();
    }

    /**
     * Returns the 'flattened' multiset version of this set of sets,
     * containing the same elements as in all sets of this set of sets.
     *
     * @return  the 'flattened' multiset version of this set of sets.
     */
    public Multiset toMultiset() {
        return (Multiset)flatVersion.clone();
    }

    /**
     * Returns a set containing the elementary sets from within this set of
     * sets that contain the specified element. If there are no sets containing
     * the specified element, an empty set is returned.
     *
     * @param o  the element that the returned sets have to contain.
     * @return   the elementary sets from this set of sets that contain the
     *           specified element.
     */
    public SetOfSets containingSets(Object o) {
        SetOfSets containSets = new HashSetOfSets();
        Set currentSet;

        for (Iterator iter = myHashSet.iterator(); iter.hasNext(); ) {
            currentSet = (Set)iter.next();
            if (currentSet.contains(o)) {
                containSets.add(currentSet);
            }
        }
        return containSets;
    }

    /**
     * Returns a new set containing the subsets of the specified set.
     * If the specified set is empty or no supersets exist in this set, an
     * empty set of sets is returned.
     *
     * @param s  the set that the returned sets have to be subsets of.
     * @return   the elementary sets from this set of sets that occur in
     *           the specified set.
     */
    public SetOfSets subsets(Set s) {
        SetOfSets resultingSet = new HashSetOfSets();
        HashMathSet hms;
        Set currentSet;

        if (s.isEmpty()) {
            return resultingSet;
        }

        if (s instanceof HashMathSet) {
            hms = (HashMathSet)s;
        } else {
            hms = new HashMathSet(s);
        }

        for (Iterator iter = myHashSet.iterator(); iter.hasNext(); ) {
            currentSet = (Set)iter.next();
            if (hms.isSuperset(currentSet)) {
                resultingSet.add(currentSet);
            }
        }
        return resultingSet;
    }

    /**
     * Returns a new set containing the supersets of the specified set.
     * If the specified set is empty, a copy of this set of sets is returned.
     * If no supersets exist in this set, an empty set of sets is returned.
     *
     * @param s  the set that the returned sets have to be supersets of.
     * @return   the elementary sets from this set of sets that contain the
     *           specified set.
     */
    public SetOfSets supersets(Set s) {
        SetOfSets resultingSet = new HashSetOfSets();
        HashMathSet hms;
        Set currentSet;

        if (s.isEmpty()) {
            return (SetOfSets)this.clone();
        }

        if (s instanceof HashMathSet) {
            hms = (HashMathSet)s;
        } else {
            hms = new HashMathSet(s);
        }

        if (! hms.isSubset(flatVersion.toSet())) {
            return resultingSet;
        }

        for (Iterator iter = myHashSet.iterator(); iter.hasNext(); ) {
            currentSet = (Set)iter.next();
            if (hms.isSubset(currentSet)) {
                resultingSet.add(currentSet);
            }
        }
        return resultingSet;
    }

    /**
     * Returns a new set containing the supersets of the specified set.
     * If the specified set is empty, a copy of this set of sets is returned.
     * If no supersets exist in this set, an empty set of sets is returned.<p>
     *
     * This implementation is faster for large numbers of large sets. An array
     * containing the elements of the specified set, sorted by their frequency
     * of occurrence in this set of sets, is used to make the superset relation
     * checks faster.<p>
     *
     * Note that this implementation requires the elements of this set of sets
     * to have an efficient <code>contains()</code> method (constant complexity)
     * to achieve the improved performance.
     *
     * @param s  the set that the returned sets have to be supersets of.
     * @return   the elementary sets from this set of sets that contain the
     *           specified set.
     */
    public SetOfSets sortedArraySupersets(Set s) {
        SetOfSets resultingSet = new HashSetOfSets();
        Set currentSet;
        Element[] elemArray = new Element[s.size()];
        Iterator sIter, thisIter;
        int elemNr = 0;

        if (s.isEmpty()) {
            return (SetOfSets)this.clone();
        }

        if (! ((flatVersion.toSet().size() >= s.size()) &&
               (flatVersion.toSet().containsAll(s)))) {
            return resultingSet;
        }

        sIter = s.iterator();
        while (sIter.hasNext()) {
            Element elem = new Element();
            elem.contents = sIter.next();
            elem.quantity = flatVersion.getQuantity(elem.contents);
            if (elem.quantity > 0) {
                elemArray[elemNr] = elem;
                elemNr++;
            }
        }

        Arrays.sort(elemArray);

        thisIter = this.myHashSet.iterator();
        while (thisIter.hasNext()) {
            currentSet = (Set)(thisIter.next());
            if (currentSet.size() >= s.size()) {
                elemNr = 0;
                while ((elemNr < elemArray.length)
                       && (currentSet.contains(elemArray[elemNr].contents))) {
                    elemNr++;
                }
                if (elemNr == elemArray.length) {
                    resultingSet.add(currentSet);
                }
            }
        }

        return resultingSet;
    }

    // Class for the array elements representing the flatVersion elements
    // Used in fastSupersets method
    private static class Element implements Comparable {
        public Object contents;
        public int quantity;

        public int compareTo(Object o) {
            Element e = (Element)o;
            return this.quantity - e.quantity;
        }
    }
}
