/*
 * @(#)SetOfSets.java, 18 Aug 2003
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

import java.util.Set;

/**
 * An extension of java.util.Set for representing sets of sets.
 * The primary set elements must implement the <code>Set</code> interface.
 * The null element is not allowed.
 *
 * @author S. Hodri, S. Schuetz
 * @version 18 Aug 2003
 * @see Set
 * @see AbstractSetOfSets
 * @see HashSetOfSets
 */
public interface SetOfSets extends Set {

    /**
     * Returns the 'flattened' version of this set of sets, in which each
     * basic element of this set of sets is present exactly once.
     *
     * @return  the 'flattened' version (simple set) of this set of sets.
     */
    public Set toSet();

    /**
     * Returns the 'flattened' multiset version of this set of sets,
     * containing the same elements as in all sets of this set of sets.
     *
     * @return  the 'flattened' multiset version of this set of sets.
     */
    public Multiset toMultiset();

    /**
     * Returns <code>true</code> if this set of sets contains a set in which the
     * specified element is present.
     *
     * @param o  the element whose presence in any elementary set within this
     *           set of sets is tested for.
     * @return   <code>true</code> if any set within this set of sets contains
     *           the specified element, <code>false</code> otherwise.
     */
    public boolean containsAtom(Object o);

    /**
     * Returns a set containing the elementary sets from within this set of sets
     * that contain the specified element. If there are no sets containing the
     * specified element, an empty set is returned.
     *
     * @param o  the element that the returned sets have to contain.
     * @return   the elementary sets from this set of sets that contain the
     *           specified element.
     */
    public SetOfSets containingSets(Object o);

    /**
     * Adds the specified element to this set if it is not already
     * present.
     *
     * @param o  element to be added to this set.
     * @return   <code>true</code> if the set did not already contain the
     *           specified element, <code>false</code> otherwise.
     * @throws   ClassCastException if the type of the specified element
     *           is incompatible (<code>!(o instanceof Set)</code>).
     */
    public boolean add(Object o);

    /**
     * Removes the specified element from this set if it is present.
     *
     * @param o  object to be removed from this set, if present.
     * @return   <code>true</code> if the set did contain the specified element,
     *           <code>false</code> otherwise.
     * @throws   ClassCastException if the type of the specified element
     *           is incompatible (<code>!(o instanceof Set)</code>).
     */
    public boolean remove(Object o);

    /**
     * Returns <code>true</code> if this set contains the specified element.
     *
     * @param o  element whose presence in this set is to be tested.
     * @return   <code>true</code> if this set contains the specified element,
     *           <code>false</code> otherwise.
     * @throws   ClassCastException if the type of the specified element
     *           is incompatible (<code>!(o instanceof Set)</code>).
     */
    public boolean contains(Object o);

    /**
     * Returns a new set containing the supersets of the specified set.
     * If the specified set is empty, a copy of this set of sets is returned.
     * If no supersets exist in this set, an empty set of sets is returned.
     *
     * @param s  the set that the returned sets have to be supersets of.
     * @return   the elementary sets from this set of sets that contain the
     *           specified set.
     */
    public SetOfSets supersets(Set s);

    /**
     * Returns a new set containing the subsets of the specified set.
     * If the specified set is empty or no supersets exist in this set, an
     * empty set of sets is returned.
     *
     * @param s  the set that the returned sets have to be subsets of.
     * @return   the elementary sets from this set of sets that occur in
     *           the specified set.
     */
    public SetOfSets subsets(Set s);
}
