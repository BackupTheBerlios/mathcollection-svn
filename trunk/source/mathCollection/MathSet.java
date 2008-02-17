/*
 * @(#)MathSet.java, 18 Aug 2003
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
 * An extension of <code>java.util.Set</code> for representing mathematical
 * sets.
 *
 * @author S. Hodri, S. Schuetz
 * @version 18 Aug 2003
 * @see Set
 * @see AbstractMathSet
 * @see AbstractSetOfSets
 * @see HashMathSet
 * @see HashSetOfSets
 */
public interface MathSet extends Set {

    /**
     * Returns <code>true</code> if this mathematical set is a superset of the
     * specified set. That is, if all elements of the specified set are also
     * present in this mathematical set.
     *
     * @param s  set for which is checked whether this mathematical set is a
     *           superset of or not.
     * @return   <code>true</code> if this mathematical set is a superset,
     *           <code>false</code> otherwise.
     */
    public boolean isSuperset(Set s);

    /**
     * Returns <code>true</code> if this mathematical set is a subset of the
     * specified set. That is, if all elements of this mathematical set are also
     * present in the specified set.
     *
     * @param s  set for which is checked whether this mathematical set is a
     *           subset of or not.
     * @return   <code>true</code> if this mathematical set is a subset,
     *           <code>false</code> otherwise.
     */
    public boolean isSubset(Set s);

    /**
     * Returns <code>true</code> if the specified set has no common elements
     * with this mathematical set.
     *
     * @param s  set to be checked for common elements.
     * @return   <code>true</code> if there are common elements,
     *           <code>false</code> otherwise.
     */
    public boolean isDisjoint(Set s);

    /**
     * Returns the union with the specified set. This is a new
     * <code>MathSet</code> containing all elements that are present in this
     * mathematical set or in the specified set.
     *
     * @param s  set that is to be united with.
     * @return   the union with the specified set.
     */
    public MathSet union(Set s);

    /**
     * Returns the intersection with the specified set. This is a new
     * <code>MathSet</code> containing all elements that are present in this
     * mathematical set as well as in the specified set.
     *
     * @param s  set that is to be intersected with.
     * @return   the intersection with the specified set.
     */
    public MathSet intersection(Set s);

    /**
     * Returns the asymmetric difference between this mathematical set and the
     * specified set. This is a new <code>MathSet</code> containing all elements
     * that are present in this mathematical set but not in the specified set.
     *
     * @param s  set from what the difference is calculated.
     * @return   the difference with the specified set.
     */
    public MathSet difference(Set s);

    /**
     * Returns the symmetric difference between this mathematical set and the
     * specified set. This is a new <code>MathSet</code> containing all elements
     * that are present either in this mathematical set or in the specified set
     * but not in both.
     *
     * @param s  set from what the symmetric difference is calculated
     * @return   the symmetric difference with the specified set.
     */
    public MathSet symmetricDifference(Set s);

    /**
     * Returns the power set of this mathematical set. This is a set containing
     * all subsets of this mathematical set, including, in particular, the empty
     * set and this mathematical set itself.
     *
     * @return  power set of this mathematical set.
     */
    public SetOfSets powerSet();

    /**
     * Returns all possible subsets of this set with the specified size,
     * contained in a <code>SetOfSets</code> instance. In case the
     * <code>size</code> parameter is negative or greater than the size of the
     * set itself, an empty <code>SetOfSets</code> is returned.
     *
     * @param size  size of the returned subsets.
     * @return      a <code>SetOfSets</code> containing all subsets of this
     *              mathematical set with the specified size.
     */
    public SetOfSets fixedSizeSubsets(int size);
}
