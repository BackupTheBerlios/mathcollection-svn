/*
 * @(#)HashMultiset.java, 07 Mar 2004
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
import java.util.Iterator;
import java.util.HashMap;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * This class implements the <code>Multiset</code> interface, backed by a hash
 * map (specifically, a <code>HashMap</code> instance).<p>
 *
 * Equal elements are returned in successive order, whereas different
 * elements have no specific iteration order; in particular, it is not
 * guaranteed that the element order will remain constant over time.
 * Multiple copies of the same element only require as much memory as a
 * single instance of it. This class permits the <code>null</code> element.<p>
 *
 * Note that this implementation is not synchronized. If multiple
 * threads access a set concurrently, and at least one of the threads modifies
 * the set, it must be synchronized externally.  This is typically
 * accomplished by synchronizing on some object that naturally encapsulates
 * the set.  If no such object exists, the set should be "wrapped" using the
 * <code>MathCollections.synchronizedMultiset</code> method.  This is best done
 * at creation time, to prevent accidental unsynchronized access to the
 * <code>HashMultiset</code> instance:
 *
 * <pre>
 *     Multiset mus = MathCollections.synchronizedMultiset(new HashMultiset(...));
 * </pre>
 *
 * @author S. Hodri, S. Schuetz
 * @version 07 Mar 2004
 * @see Collection
 * @see Multiset
 * @see AbstractMultiset
 * @see HashMap
 */
public class HashMultiset extends AbstractMultiset implements Multiset, Cloneable, Serializable {

    /**
     * An iterator that, in spite of the specific element storage technique in
     * a <code>Multiset</code> (equal elements get 'counted' instead of
     * each being stored separately), iterates over individual
     * <code>Multiset</code> elements.
     */
    private class HashMultisetIterator implements Iterator {
        private Iterator myKeySetIterator;
        private Object currentElement;
        private Object oldCurrentElement;
        private int multiElementIndex;
        private int multiElementNumber;
        private boolean removeEnabled;

        public HashMultisetIterator() {
            myKeySetIterator = myHashMap.keySet().iterator();
            removeEnabled = false;
            HashMultiset.this.isConcurrentlyModified = false;
            currentElement = null;
            multiElementNumber  = 0;
            multiElementIndex  = 0;
        }

        public boolean hasNext() {
            if (HashMultiset.this.isConcurrentlyModified) {
                throw new ConcurrentModificationException();
            } else if (multiElementIndex > 0) {
                return true;
            } else {
                return (myKeySetIterator.hasNext());
            }
        }

        public Object next() {
            if (hasNext()) {
                removeEnabled = true;
                if (multiElementIndex == 0) {
                    currentElement = myKeySetIterator.next();
                    multiElementNumber = getQuantity(currentElement);
                    multiElementIndex  = multiElementNumber - 1;
                } else {
                    multiElementIndex--;
                }
            } else {
                throw new NoSuchElementException();
            }
            return currentElement;
        }

        public void remove() {
            if (HashMultiset.this.isConcurrentlyModified) {
                throw new ConcurrentModificationException();
            } else if (removeEnabled) {
                if (multiElementNumber > 1) {
                    Integer oldKeyCount, newKeyCount;

                    oldKeyCount = (Integer)myHashMap.get(currentElement);
                    newKeyCount = new Integer(oldKeyCount.intValue() - 1);
                    myHashMap.put(currentElement, newKeyCount);
                    multiElementNumber--;
                } else {
                    myKeySetIterator.remove();
                    multiElementNumber = 0;
                }
                removeEnabled = false;
                storedHashCode = 0;
                storedSize--;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    /**
     * The backing instance of <code>HashMap</code> where the elements of this
     * set are stored.
     */
    private HashMap myHashMap;

    /**
     * Acts as a cache for the hash code value of this set out of performance
     * considerations.
     * Whenever this set is changed, storedHashCode is set to 0 and gets updated
     * as soon as the <code>hashCode()</code> method is called.
     */
    private int storedHashCode = 0;

    /**
     * Acts as a cache for the size value of this set out of performance
     * considerations. The value of storedSize is always up-to-date since it
     * gets updated by all destructive methods in this class.
     */
    private int storedSize = 0;

    /**
     * Used to check, whether this multiset was modified by an destructive
     * method while iterating over it.
     */
    private boolean isConcurrentlyModified = false;

    /**
     * Constructs a new, empty multiset; the backing <code>HashMap</code>
     * instance has default initial capacity (16) and load factor (0.75).
     */
    public HashMultiset() {
        myHashMap = new HashMap();
    }

    /**
     * Constructs a new multiset containing the elements in the specified
     * collection. The backing <code>HashMap</code> instance is created with
     * default load factor (0.75) and an initial capacity sufficient to contain
     * the elements in the specified collection.
     *
     * @param c  the collection whose elements are to be placed into this
     *           multiset.
     * @throws   NullPointerException if the specified collection is null.
     */
    public HashMultiset(Collection c) {
        myHashMap = new HashMap();
        this.addAll(c);
    }

    /**
     * Constructs a new, empty multiset; the backing <code>HashMap</code>
     * instance has specified initial capacity and default load factor
     * (0.75). Note that the backing <code>HashMap</code> only stores single
     * copies of equal elements.<p>
     *
     * @param  initialCapacity           the initial capacity for distinct
     *                                   elements.
     * @throws IllegalArgumentException  if the initial capacity is negative.
     */
    public HashMultiset(int initialCapacity) {
        myHashMap = new HashMap(initialCapacity);
    }

    /**
     * Constructs a new, empty multiset; the backing <code>HashMap</code>
     * instance has specified initial capacity and load factor. Note that the
     * backing <code>HashMap</code> only stores single copies of equal elements.
     *
     * @param  initialCapacity           the initial capacity for distinct
     *                                   elements.
     * @param  loadFactor                the load factor.
     * @throws IllegalArgumentException  if the initial capacity is negative
     *         or the load factor is nonpositive.
     */
    public HashMultiset(int initialCapacity, float loadFactor) {
        myHashMap = new HashMap(initialCapacity, loadFactor);
    }

    /**
     * Returns an iterator over the elements in this multiset. Different
     * elements are returned in no particular order, however, equal elements
     * are always returned subsequently.
     *
     * @see     ConcurrentModificationException
     * @return  an Iterator over the elements in this multiset.
     */
    public Iterator iterator() {
        return new HashMultisetIterator();
    }

    /**
     * Returns the hash code value for this multiset. To get the hash
     * code of this multiset, new hash code values for every element of
     * this multiset are calculated from a polynomial of 3rd order and
     * finally summed up.
     * This ensures that <code>s1.equals(s2)</code> implies that
     * <code>s1.hashCode()==s2.hashCode()</code> for any two multisets
     * <code>s1</code> and <code>s2</code>, as required by the general contract
     * of <code>Object.hashCode()</code>.<p>
     *
     * This implementation first checks whether a cached hash code value is
     * available. If not (i.e. <code>storedHashCode</code> is zero), the hash
     * code gets calculated using <code>hashCode()</code> of the super class.
     *
     * @return  the hash code value for this multiset.
     */
    public int hashCode() {
        if (storedHashCode == 0) {
            storedHashCode = super.hashCode();
        }
        return storedHashCode;
    }

    /**
     * Returns a shallow copy of this <code>HashMultiset</code> instance: the
     * elements themselves are not cloned.
     *
     * @return  a shallow copy of this multiset.
     */
    public Object clone() {
        HashMultiset copy = new HashMultiset(this.myHashMap.size());
        copy.myHashMap.putAll(this.myHashMap);
        copy.storedSize = this.storedSize;
        copy.storedHashCode = this.storedHashCode;
        return copy;
    }

    /**
     * Returns <code>true</code> if this multiset contains no elements.
     *
     * @return  <code>true</code> if this multiset contains no elements,
     *          <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return myHashMap.isEmpty();
    }

    /**
     * Returns the number of elements in this multiset (its cardinality).
     *
     * @return  the number of elements in this multiset (its cardinality).
     */
    public int size() {
        return storedSize;
    }

    /**
     * Removes all elements from this set.
     */
    public void clear() {
        storedSize = 0;
        storedHashCode = 0;
        isConcurrentlyModified = true;
        myHashMap.clear();
    }

    /**
     * Returns <code>true</code> if this set contains the specified element.
     *
     * @param o  element whose presence in this set is to be tested.
     * @return   <code>true</code> if this set contains the specified element,
     *           <code>false</code> otherwise.
     */
    public boolean contains(Object o) {
        return myHashMap.containsKey(o);
    }

    /**
     * Adds the specified element to this multiset.<p>
     *
     * This implementation sets <code>storedHashCode</code> to 0 (representing
     * an unavailable hash code value), which forces <code>hashCode()</code> to
     * recalculate the actual hash code value.
     *
     * @param o  element to be added to this set.
     * @return   <code>true</code>, adding an element to a multiset will always
     *           be a success.
     */
    public boolean add(Object o) {
        Integer oldKeyCount;
        Integer newKeyCount;

        oldKeyCount = (Integer)myHashMap.get(o);
        if (oldKeyCount != null) {
            newKeyCount = new Integer(oldKeyCount.intValue() + 1);
            myHashMap.put(o, newKeyCount);
        } else {
            myHashMap.put(o, new Integer(1));
        }
        storedSize++;
        storedHashCode = 0;
        isConcurrentlyModified = true;
        return true;
    }

    /**
     * Adds the specified element <code>quantity</code> of times to this
     * multiset. If <code>quantity</code> is negative or 0, the multiset
     * remains unchanged and <code>false</code> is returned.<p>
     *
     * If the set gets altered, this implementation sets
     * <code>storedHashCode</code> to 0 (representing an unavailable hash code
     * value), which forces <code>hashCode()</code> to recalculate the actual
     * hash code value.
     *
     * @param o         element to be added to this set.
     * @param quantity  quantity of elements to add.
     * @return          <code>true</code> if <code>quantity</code> is
     *                  greater than 0, <code>false</code> otherwise
     */
    public boolean add(Object o, int quantity) {
        if (quantity <= 0) {
            return false;
        } else {
            return this.setQuantity(o, this.getQuantity(o) + quantity);
        }
    }

    /**
     * Removes the specified element from this multiset if it is present. If the
     * element is present more than once, its quantity gets decreased by one.<p>
     *
     * If the set gets altered, this implementation sets
     * <code>storedHashCode</code> to 0 (representing an unavailable hash code
     * value), which forces <code>hashCode()</code> to recalculate the actual
     * hash code value.
     *
     * @param o  object to be removed from this multiset, if present.
     * @return   <code>true</code> if the multiset contained the specified
     *           element, <code>false</code> otherwise.
     */
    public boolean remove(Object o) {
        Integer oldKeyCount;
        Integer newKeyCount;

        oldKeyCount = (Integer)myHashMap.get(o);
        if (oldKeyCount != null) {
            if (oldKeyCount.intValue() == 1) {
                myHashMap.remove(o);
            } else {
                newKeyCount = new Integer(oldKeyCount.intValue() - 1);
                myHashMap.put(o, newKeyCount);
            }
            storedSize--;
            storedHashCode = 0;
            isConcurrentlyModified = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes the specified element <code>quantity</code> of times from this
     * multiset if possible. If <code>quantity</code> is negative or 0, the
     * multiset remains unchanged and <code>false</code> is returned.<p>
     *
     * If the set gets altered, this implementation sets
     * <code>storedHashCode</code> to 0 (representing an unavailable hash code
     * value), which forces <code>hashCode()</code> to recalculate the actual
     * hash code value.
     *
     * @param o         object to be removed from this multiset, if present.
     * @param quantity  quantity of elements to remove.
     * @return          <code>true</code> if the multiset got altered,
     *                  <code>false</code> otherwise.
     */
    public boolean remove(Object o, int quantity) {
        if (quantity <= 0) {
            return false;
        } else {
            return this.setQuantity(o, this.getQuantity(o) - quantity);
        }
    }

    /**
     * Compares the specified object with this multiset for equality. Returns
     * <code>true</code> if the specified object is also a multiset, the two
     * sets have the same size, and every element of the specified set is
     * contained in this set the same number of times.<p>
     *
     * This implementation first checks if the given object is a
     * <code>HashMultiset</code>. If so, the hash code values of this multiset
     * and the specified <code>HashMultiset</code> are compared.
     * Since the hash code values are being cached, this represents a quick
     * solution if the sets aren't equal. However, if the hash code values are
     * equal, it cannot be assumed that the sets themselves are equal,
     * since different sets can have the same hash code value. In this case,
     * the sets are compared on a per-element basis using the super method
     * <code>equals</code>.
     *
     * @param o  object to be compared for equality with this multiset.
     * @return   <code>true</code> if the specified object is equal to this
     *           multiset, <code>false</code> otherwise.
     */
    public boolean equals(Object o) {
        if (o instanceof HashMultiset) {
            if (this.hashCode() != ((HashMultiset)o).hashCode()) {
                return false;
            }
        }
        return super.equals(o);
    }

    /**
     * Returns the number of times the specified element is present in this
     * multiset.
     *
     * @param o  element whose quantity is returned.
     * @return   quantity of the specified element, 0 if it is not present.
     * @see      #setQuantity
     */
    public int getQuantity(Object o) {
        Integer keyCount;

        keyCount = (Integer)myHashMap.get(o);
        if (keyCount == null) {
            return 0;
        } else {
            return keyCount.intValue();
        }
    }

    /**
     * Adjusts the number of times the specified element is present in this
     * multiset to be the specified value (zero if the value is negative).<p>
     *
     * This implementation sets <code>storedHashCode</code> to 0 (representing
     * an unavailable hash code value), which forces <code>hashCode()</code> to
     * recalculate the actual hash code value.
     *
     * @param o         element whose quantity gets set.
     * @param quantity  quantity of the specified element to be set.
     * @return          <code>true</code> if this multiset has been modified,
     *                  <code>false</code> otherwise.
     * @see             #getQuantity
     */
    public boolean setQuantity(Object o, int quantity) {
        int newStoredSize;

        if (quantity <= 0) {
            newStoredSize = storedSize - getQuantity(o);
        } else {
            newStoredSize = storedSize + quantity - getQuantity(o);
        }

        if (newStoredSize == storedSize) {
            return false;
        } else {
            storedSize = newStoredSize;
            storedHashCode = 0;
            isConcurrentlyModified = true;
            if (quantity <= 0) {
                myHashMap.remove(o);
            } else {
                myHashMap.put(o, new Integer(quantity));
            }
            return true;
        }
    }

    /**
     * Returns a new <code>Set</code> containing the 'flattened' version of
     * this multiset in which every element of this multiset is present exactly
     * once.
     *
     * @return  the 'flattened' version of this multiset.
     */
    public Set toSet() {
        return myHashMap.keySet();
    }

    /**
     * Returns the size of a 'flattened' version of this multiset in which
     * every element of this multiset is present exactly once.
     *
     * @return  the size of the 'flattened' version of this multiset.
     */
    public int setSize() {
        return myHashMap.size();
    }

    /**
     * Returns <code>true</code> if this multiset is a superset of the
     * specified collection. That is, if all elements of the specified
     * collection are also present in this multiset at least the same number of
     * times.<p>
     *
     * This implementation checks if the specified collection is an instance of
     * <code>Multiset</code> or <code>Set</code>. If so, the result of the
     * super method <code>isSuperset</code> is returned. Otherwise, it tries
     * to create the intersection of this HashMultiset and the specified
     * Collection c by iterating over c and adding common elements to a new
     * multiset. If an element is found whose quantity in the current
     * intersection multiset is greater or equal than in this HashMultiset,
     * false is returned. If the intersection can be built up completely, this
     * HashMultiset is a superset of c and true is returned.
     *
     * @param c  collection to be checked for being a subset.
     * @return   <code>true</code> if this multiset is a superset of the
     *           specifed collection, <code>false</code> otherwise.
     */
    public boolean isSuperset(Collection c) {
        if ((c instanceof Multiset) || (c instanceof Set)) {
            return super.isSuperset(c);
        } else {
            Multiset ms;
            Object currentElement;

            if (this.size() >= c.size()) {
                ms = new HashMultiset();
                for (Iterator iter = c.iterator(); iter.hasNext(); ) {
                    currentElement = iter.next();
                    if (ms.getQuantity(currentElement) < this.getQuantity(currentElement)) {
                        ms.add(currentElement);
                    } else {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Returns the sum with the specified collection. This is a new
     * <code>Multiset</code> containing all elements that are present in this
     * multiset or in the specified collection. The quantities of equal
     * elements get added up.
     *
     * @param c  collection to be united with.
     * @return   the union with the specified collection.
     */
    public Multiset sum(Collection c) {
        HashMultiset resultingSet = (HashMultiset)this.clone();
        Object currentElement;

        if (c instanceof Multiset) {
            Multiset cMultiset = (Multiset)c;
            for (Iterator iter = cMultiset.toSet().iterator(); iter.hasNext(); ) {
                currentElement = iter.next();
                resultingSet.add(currentElement,
                                 cMultiset.getQuantity(currentElement));
            }
        } else {
            resultingSet.addAll(c);
        }
        return resultingSet;
    }

    /**
     * Returns the union with the specified collection. This is a new
     * <code>HashMultiset</code> containing all elements that are present in
     * this multiset or in the specified collection. For equal elements, the
     * resulting quantity is the maximum of the two given quantities.
     *
     * @param c  collection to be united with.
     * @return   the union with the specified collection.
     */
    public Multiset union(Collection c) {
        HashMultiset resultingSet;
        Iterator iter;
        Object currentElement;
        int numberThis;
        int numberCollection;

        if (c instanceof HashMultiset) {
            resultingSet = (HashMultiset)((HashMultiset)c).clone();
        } else {
            resultingSet = new HashMultiset(c);
        }

        for (iter = this.myHashMap.keySet().iterator();
             iter.hasNext(); ) {
            currentElement = iter.next();
            numberThis = this.getQuantity(currentElement);
            numberCollection = resultingSet.getQuantity(currentElement);
            if (numberThis > numberCollection) {
                resultingSet.setQuantity(currentElement, numberThis);
            }
        }
        return resultingSet;
    }

    /**
     * Returns the intersection with the specified collection. This is a new
     * <code>HashMultiset</code> containing all elements that are present in
     * this multiset as well as in the specified collection. For equal elements,
     * the resulting quantity is the minimum of the two given quantities.
     *
     * @param c  collection to be intersected with.
     * @return   the intersection with the specified collection.
     */
    public Multiset intersection(Collection c) {
        HashMultiset resultingSet;
        Multiset collectionMultiset;
        Object currentElement;
        Iterator iter;
        int numberThis, numberCollection;

        /*
         * d(Collection c) := number of unique elements in c
         */
        if (c instanceof Multiset) {
            /*
             * time  complexity = O(min{|d(this)|, |d(c)|})
             * space complexity = O(|resultingSet|)
             */
            collectionMultiset = (Multiset)c;
            if (collectionMultiset.setSize() < this.setSize()) {
                iter = collectionMultiset.toSet().iterator();
            } else {
                iter = this.toSet().iterator();
            }
            resultingSet = new HashMultiset();
            while (iter.hasNext()) {
                currentElement = iter.next();
                numberThis = this.getQuantity(currentElement);
                numberCollection = collectionMultiset.getQuantity(currentElement);
                resultingSet.setQuantity(currentElement,
                                         Math.min(numberThis, numberCollection));
            }
        } else {
            /*
             * time  complexity = O(|c|)
             * space complexity = O(|resultingSet|)
             *
             * This section creates the intersection of this HashMultiset
             * and the specified Collection c by iterating over c and
             * adding common elements into a new Multiset resultingSet.
             * If the quantity of a currentElement in the intersection Multiset
             * is greater or equal than in this HashMultiset, this
             * currentElement doesn't get added anymore.
             */
            resultingSet = new HashMultiset();
            for (iter = c.iterator(); iter.hasNext(); ) {
                currentElement = iter.next();
                if (resultingSet.getQuantity(currentElement) < this.getQuantity(currentElement)) {
                    resultingSet.add(currentElement);
                }
            }
        }
        return resultingSet;
    }

    /**
     * Returns the asymmetric difference between this multiset and the
     * specified collection. This is a new <code>HashMultiset</code> containing
     * all elements that are present in this multiset but not in the specified
     * collection. The quantities of equal elements get subtracted.
     *
     * @param c  collection from which the difference is calculated.
     * @return   the difference with the specified collection.
     */
    public Multiset difference(Collection c) {
        HashMultiset resultingSet;
        Multiset collectionMultiset;
        Iterator iter;
        Object currentElement;
        int numberThis, numberCollection;

        /*
         * d(Collection c) := number of unique elements in c
         */

        resultingSet = new HashMultiset();

        if (c instanceof Multiset) {
            /*
             * no time or space ressources needed
             */
            collectionMultiset = (Multiset)c;
        } else {
            /*
             * time  complexity = O(|c|)
             * space complexity = O(|d(c)|)
             */
            collectionMultiset = new HashMultiset(c);
        }

        /*
         * time  complexity = O(|d(this)|)
         * space complexity = O(|d(resultingSet)|)
         */
        iter = this.toSet().iterator();
        while (iter.hasNext()) {
            currentElement = iter.next();
            numberThis = this.getQuantity(currentElement);
            numberCollection = collectionMultiset.getQuantity(currentElement);
            resultingSet.setQuantity(currentElement, numberThis - numberCollection);
        }
        return resultingSet;
    }

    /**
     * Returns the symmetric difference between this multiset and the
     * specified collection. This is a new <code>HashMultiset</code> containing
     * all elements that are present either in this multiset or in the specified
     * collection but not in both. The quantities of equal elements get
     * subtracted from each other (maximum minus minimum).
     *
     * @param c  collection from which the symmetric difference is calculated
     * @return   the symmetric difference with the specified collection.
     */
    public Multiset symmetricDifference(Collection c) {
        HashMultiset resultingSet;
        Iterator iter;
        Object currentElement;
        int numberThis;
        int numberCollection;

        if (c instanceof HashMultiset) {
            resultingSet = (HashMultiset)((HashMultiset)c).clone();
        } else {
            resultingSet = new HashMultiset(c);
        }

        for (iter = this.toSet().iterator();
             iter.hasNext(); ) {
            currentElement = iter.next();
            numberThis = this.getQuantity(currentElement);
            numberCollection = resultingSet.getQuantity(currentElement);
            if (numberThis >= numberCollection) {
                resultingSet.setQuantity(currentElement, numberThis - numberCollection);
            } else {
                resultingSet.setQuantity(currentElement, numberCollection - numberThis);
            }
        }
        return resultingSet;
    }
}
