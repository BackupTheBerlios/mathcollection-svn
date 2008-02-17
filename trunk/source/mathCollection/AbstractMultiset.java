/*
 * @(#)AbstractMultiset.java, 03 Jan 2004
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
import java.util.AbstractCollection;
import java.util.Iterator;

/**
 * This class provides a skeletal implementation of the <code>Multiset</code>
 * interface to minimize the effort required to implement this interface.
 *
 * @author S. Hodri, S. Schuetz
 * @version 03 Jan 2004
 * @see Collection
 * @see AbstractCollection
 * @see HashMultiset
 */
public abstract class AbstractMultiset extends AbstractCollection implements Multiset {

    /**
     * Returns the hash code value for this multiset. To get the hash
     * code of this multiset, new hash code values for every element of
     * this multiset are calculated from a polynomial of 3rd order and
     * finally summed up.
     * This ensures that <code>s1.equals(s2)</code> implies that
     * <code>s1.hashCode()==s2.hashCode()</code> for any two multisets
     * <code>s1</code> and <code>s2</code>, as required by the general contract
     * of <code>Object.hashCode()</code>.
     *
     * @return  the hash code value for this multiset.
     */
    public int hashCode() {
        Object currentElement;
        int elementHashCode = 0;
        int newHashCode = -1;

        for (Iterator iter = this.toSet().iterator(); iter.hasNext(); ) {
            currentElement = iter.next();
            elementHashCode = currentElement.hashCode();
            newHashCode += -1 + ( 3 + elementHashCode)
                              * ( 7 + elementHashCode)
                              * (11 + elementHashCode)
                              * getQuantity(currentElement);
        }
        return newHashCode;
    }

    /**
     * Returns a string representation of this multiset. The string
     * representation consists of a list of the set's elements in the
     * order they are returned by its iterator, enclosed in curly brackets
     * (<code>"{}"</code>). Adjacent elements are separated by the characters
     * <code>", "</code> (comma and space). Elements are converted to strings as
     * by <code>Object.toString()</code>.
     *
     * @return  a string representation of this multiset.
     */
    public String toString() {
        StringBuffer output = new StringBuffer();

        output.append("{");

        for (Iterator iter = this.iterator(); iter.hasNext(); ) {
            output.append(((Object)iter.next()).toString());
            if (iter.hasNext()) {
                output.append(", ");
            }
        }
        output.append("}");

        return output.toString();
    }

    /**
     * Returns <code>true</code> if this multiset is a superset of the
     * specified collection. That is, if all elements of the specified
     * collection are also present in this multiset at least the same number of
     * times.<p>
     *
     * This implementation first compares the sizes of this multiset and
     * the specified collection by invoking the <code>size</code> method on
     * each. If this multiset is bigger than the specified collection then
     * each element of the specified collection is checked for presence in this
     * multiset (for multiple equal elements, the quantity in this
     * multiset has to be greater or equal). Otherwise, <code>false</code> is
     * returned.
     *
     * @param c  collection to be checked for being a subset.
     * @return   <code>true</code> if this multiset is a superset of the
     *           specifed collection, <code>false</code> otherwise.
     * @see      #isSubset(Collection)
     */
    public boolean isSuperset(Collection c) {
        Iterator iterThis;
        Iterator iterCollection;
        int numCollectionElement;
        Object currentElement;
        Multiset ms;

        if (c == this) {
            return true;
        }

        if (this.size() >= c.size()) {
            if (c instanceof Multiset) {
                ms = (Multiset) c;
                if (this.setSize() < ms.setSize()) {
                    return false;
                } else {
                    for (Iterator iter = ms.toSet().iterator(); iter.hasNext(); ) {
                        currentElement = iter.next();
                        if (this.getQuantity(currentElement) < ms.getQuantity(currentElement)) {
                            return false;
                        }
                    }
                }
                return true;
            } else if (c instanceof Set) {
                if (this.setSize() >= c.size()) {
                    return this.containsAll(c);
                } else {
                    return false;
                }
            } else {
                ms = this.intersection(c);
                return (c.size() == ms.size());
            }
        } else {
            return false;
        }
    }

    /**
     * Returns <code>true</code> if this multiset is a subset of the
     * specified collection. That is, if all elements of this multiset are also
     * present in the specified collection at least the same number of times.<p>
     *
     * This implementation first compares the sizes of this multiset and
     * the specified collection by invoking the <code>size</code> method on
     * each. If the specified collection is bigger than this multiset then
     * each element of this multiset is checked for presence in the specified
     * collection (for multiple equal elements, the quantity in the
     * specified collection has to be greater or equal). Otherwise,
     * <code>false</code> is returned.
     *
     * @param c  collection to be checked for being a superset.
     * @return   <code>true</code> if this multiset is a subset of the
     *           specifed collection, <code>false</code> otherwise.
     * @see      #isSuperset(Collection)
     */
    public boolean isSubset(Collection c) {
        Iterator iterThis;
        Iterator iterCollection;
        int numCollectionElement;
        Object currentElement;
        Multiset ms;

        if (c == this) {
            return true;
        }

        if (this.size() <= c.size()) {
            if (c instanceof Multiset) {
                ms = (Multiset) c;
                if (this.setSize() > ms.setSize()) {
                    return false;
                } else {
                    for (iterThis = this.toSet().iterator(); iterThis.hasNext(); ) {
                        currentElement = iterThis.next();
                        if (this.getQuantity(currentElement) > ms.getQuantity(currentElement)) {
                            return false;
                        }
                    }
                }
                return true;
            } else if (c instanceof Set) {
                if (this.setSize() == this.size()) {
                    // "this" doesn't contain any identical elements,
                    // i.e. "this" can be seen as a set
                    ms = this.intersection(c);
                    return (this.size() == ms.size());
                } else {
                    return false;
                }
            } else {
                ms = this.intersection(c);
                return (this.size() == ms.size());
            }
        } else {
            return false;
        }
    }

    /**
     * Returns <code>true</code> if this multiset has no common element with the
     * specified set.<p>
     *
     * This implementation checks whether the specified collection is an
     * instance of <code>Multiset</code> or not. If so, it iterates over
     * the multiset that has fewer different elements. During iteration, only
     * different elements are taken into account. If the specified collection is
     * not an instance of <code>Multiset</code>, it iterates over all elements
     * of the specified collection. If a common element is found, that is, if
     * an element is contained both in this multiset and in the specified
     * collection, <code>false</code> is returned.
     *
     * @param c  collection to be checked for common elements.
     * @return   <code>true</code> if this multiset has no common elements
     *           with the specifed collection, <code>false</code> otherwise.
     */
    public boolean isDisjoint(Collection c) {
        Iterator iter;

        if (c == this) {
            return true;
        }

        if (c instanceof Multiset) {
            if (this.setSize() <= ((Multiset)c).setSize()) {
                for (iter = this.toSet().iterator(); iter.hasNext(); ) {
                    if (c.contains(iter.next())) {
                        return false;
                    }
                }
                return true;
            } else {
                for (iter = ((Multiset)c).toSet().iterator(); iter.hasNext(); ) {
                    if (this.contains(iter.next())) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            for (iter = c.iterator(); iter.hasNext(); ) {
                if (this.contains(iter.next())) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Compares the specified object with this multiset for equality. Returns
     * <code>true</code> if the specified object is also a collection, the two
     * sets have the same size, and every element of the specified set is
     * contained in this set the same number of times.<p>
     *
     * If the specified object is not this multiset itself but another
     * collection, this implementation first compares the sizes of this multiset
     * and the specified collection by invoking the <code>size</code> method on
     * each. If the sizes match, the sets are compared on a per-element basis.
     *
     * @param o  object to be compared for equality with this multiset.
     * @return   <code>true</code> if the specified object is equal to this
     *           multiset, <code>false</code> otherwise.
     */
    public boolean equals(Object o) {
        Collection collection;
        Iterator iterThis;
        Iterator iterCollection;
        int numCollectionElement;
        Object currentElement;
        Multiset ms;

        if (o == this) {
            return true;
        }

        if (! (o instanceof Collection)) {
            return false;
        } else {
            collection = (Collection) o;
        }

        if (this.size() != collection.size()) {
            return false;
        } else {
            if (collection instanceof Multiset) {
                ms = (Multiset) collection;
                if (ms.setSize() != this.setSize()) {
                    return false;
                } else {
                    for (iterThis = this.toSet().iterator(); iterThis.hasNext(); ) {
                        currentElement = iterThis.next();
                        if (this.getQuantity(currentElement) != ms.getQuantity(currentElement)) {
                            return false;
                        }
                    }
                }
                return true;
            } else if (collection instanceof Set) {
                if (this.setSize() == this.size()) {
                    // "this" doesn't contain any identical elements,
                    // i.e. "this" can be seen as a set
                    return this.containsAll(collection);
                } else {
                    return false;
                }
            } else {
                ms = this.intersection(collection);
                return (this.size() == ms.size());
            }
        }
    }

}
