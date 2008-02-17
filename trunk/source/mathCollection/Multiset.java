/*
 * @(#)Multiset.java, 18 Aug 2003
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

/**
 * An extension of java.util.Collection for representing mathematical
 * multisets.
 *
 * @author S. Hodri, S. Schuetz
 * @version 18 Aug 2003
 * @see Collection
 * @see AbstractMultiset
 * @see HashMultiset
 */
public interface Multiset extends Collection {

    /**
     * Adds the specified element <code>quantity</code> of times to this
     * multiset. If <code>quantity</code> is negative or 0, <code>false</code>
     * is returned.
     *
     * @param o         element to be added to this set.
     * @param quantity  quantity of elements to add.
     * @return          <code>true</code> if <code>quantity</code> is
     *                  greater than 0, <code>false</code> otherwise
     */
    public boolean add(Object o, int quantity);

    /**
     * Removes the specified element <code>quantity</code> of times from this
     * multiset if possible. If <code>quantity</code> is negative or 0,
     * <code>false</code> is returned.
     *
     * @param o         object to be removed from this multiset.
     * @param quantity  quantity of elements to remove.
     * @return          <code>true</code> if the multiset got altered,
     *                  <code>false</code> otherwise.
     */
    public boolean remove(Object o, int quantity);

    /**
     * Returns a 'flattened' version of this multiset in which every element
     * of this multiset is present exactly once.
     *
     * @return  the 'flattened' version of this multiset.
     */
    public Set toSet();

    /**
     * Returns the size of a 'flattened' version of this multiset in which
     * every element of this multiset is present exactly once.
     *
     * @return  the size of the 'flattened' version of this multiset.
     */
    public int setSize();

    /**
     * Returns the number of times the specified element is present in this
     * multiset.
     *
     * @param o  element whose quantity is returned.
     * @return   quantity of the specified element, 0 if it is not present.
     * @see      #setQuantity
     */
    public int getQuantity(Object o);

    /**
     * Adjusts the number of times the specified element is present in this
     * multiset to be the specified value.
     *
     * @param o         element whose quantity gets set.
     * @param quantity  quantity of the specified element to be set.
     * @return          <code>true</code> if this multiset has been modified,
     *                  <code>false</code> otherwise.
     * @see             #getQuantity
     */
    public boolean setQuantity(Object o, int quantity);

    /**
     * Returns <code>true</code> if this multiset is a superset of the
     * specified collection. That is, if all elements of the specified
     * collection are also present in this multiset at least the same number of
     * times.
     *
     * @param c  collection for which is checked whether this multiset is a
     *           superset of or not.
     * @return   <code>true</code> if this multiset is a superset,
     *           <code>false</code> otherwise.
     */
    public boolean isSuperset(Collection c);

    /**
     * Returns <code>true</code> if this multiset is a subset of the
     * specified collection. That is, if all elements of this multiset are also
     * present in the specified collection at least the same number of times.
     *
     * @param c  collection for which is checked whether this multiset is a
     *           subset of or not.
     * @return   <code>true</code> if this multiset is a subset,
     *           <code>false</code> otherwise.
     */
    public boolean isSubset(Collection c);

    /**
     * Returns <code>true</code> if the specified collection has no common
     * elements with this multiset.
     *
     * @param c  collection to be checked for common elements.
     * @return   <code>true</code> if there are no common elements,
     *           <code>false</code> otherwise.
     */
    public boolean isDisjoint(Collection c);

    /**
     * Returns the sum with the specified collection. This is a new
     * <code>Multiset</code> containing all elements that are present in this
     * multiset or in the specified collection. The quantities of equal
     * elements get added up.
     *
     * @param c  collection to be united with.
     * @return   the union with the specified collection.
     */
    public Multiset sum(Collection c);

    /**
     * Returns the union with the specified collection. This is a new
     * <code>Multiset</code> containing all elements that are present in this
     * multiset or in the specified collection. For equal elements, the
     * resulting quantity is the maximum of the two given quantities.
     *
     * @param c  collection to be united with.
     * @return   the union with the specified collection.
     */
    public Multiset union(Collection c);

    /**
     * Returns the intersection with the specified collection. This is a new
     * <code>Multiset</code> containing all elements that are present in this
     * multiset as well as in the specified collection. For equal elements,
     * the resulting quantity is the minimum of the two given quantities.
     *
     * @param c  collection to be intersected with.
     * @return   the intersection with the specified collection.
     */
    public Multiset intersection(Collection c);

    /**
     * Returns the asymmetric difference between this multiset and the
     * specified collection. This is a new <code>Multiset</code> containing all
     * elements that are present in this multiset but not in the specified
     * collection. The quantities of equal elements get subtracted.
     *
     * @param c  collection from which the difference is calculated.
     * @return   the difference with the specified collection.
     */
    public Multiset difference(Collection c);

    /**
     * Returns the symmetric difference between this multiset and the
     * specified collection. This is a new <code>Multiset</code> containing all
     * elements that are present either in this multiset or in the specified
     * collection but not in both. The quantities of equal elements get
     * subtracted from each other (maximum minus minimum).
     *
     * @param c  collection from which the symmetric difference is calculated
     * @return   the symmetric difference with the specified collection.
     */
    public Multiset symmetricDifference(Collection c);
}
