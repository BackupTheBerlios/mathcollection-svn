/*
 * @(#)AbstractMathSet.java, 8 Sep 2003
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
import java.util.AbstractSet;
import java.util.Iterator;

/**
 * This class provides a skeletal implementation of the <code>MathSet</code>
 * interface to minimize the effort required to implement this
 * interface.
 *
 * @author S. Hodri, S. Schuetz
 * @version 8 Sep 2003
 * @see Set
 * @see AbstractSet
 * @see HashMathSet
 */
public abstract class AbstractMathSet extends AbstractSet implements MathSet {

    /**
     * Returns the hash code value for this mathematical set. To get the hash
     * code of this mathematical set, new hash code values for every element of
     * this mathematical set are calculated from a polynomial of 3rd order and
     * finally summed up.
     * This ensures that <code>s1.equals(s2)</code> implies that
     * <code>s1.hashCode()==s2.hashCode()</code> for any two mathematical sets
     * <code>s1</code> and <code>s2</code>, as required by the general contract
     * of <code>Object.hashCode()</code>.
     *
     * @return  the hash code value for this mathematical set.
     */
    public int hashCode() {
        int elementHashCode = 0;
        int newHashCode = -1;

        for (Iterator iter = this.iterator(); iter.hasNext(); ) {
            elementHashCode = iter.next().hashCode();
            newHashCode += -1 + ( 3 + elementHashCode)
                              * ( 7 + elementHashCode)
                              * (11 + elementHashCode);
        }
        return newHashCode;
    }

    /**
     * Returns a string representation of this mathematical set. The string
     * representation consists of a list of the set's elements in the
     * order they are returned by its iterator, enclosed in curly brackets
     * (<code>"{}"</code>). Adjacent elements are separated by the characters
     * <code>", "</code> (comma and space). Elements are converted to strings as
     * by <code>Object.toString()</code>.
     *
     * @return  a string representation of this mathematical set.
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
     * Returns <code>true</code> if this mathematical set is a superset of the
     * specified set. That is, if all elements of the specified set are also
     * present in this mathematical set.<p>
     *
     * This implementation first compares the sizes of this mathematical set and
     * the specified set by invoking the <code>size</code> method on each. If
     * this mathematical set is bigger than the specified set then each element
     * of the specified set is checked for presence in this mathematical set.
     * Otherwise, <code>false</code> is returned.
     *
     * @param s  set to be checked for being a subset.
     * @return   <code>true</code> if this mathematical set is a superset of the
     *           specifed set, <code>false</code> otherwise.
     * @see      #isSubset(Set)
     */
    public boolean isSuperset(Set s) {
        if (this.size() >= s.size()) {
            return this.containsAll(s);
        } else {
            return false;
        }
    }

    /**
     * Returns <code>true</code> if this mathematical set is a subset of the
     * specified set. That is, if all elements of this mathematical set are also
     * present in the specified set.<p>
     *
     * This implementation first compares the sizes of this mathematical set and
     * the specified set by invoking the <code>size</code> method on each. If
     * this mathematical set is smaller than the specified set then the
     * intersection of this mathematical set with the specified set is
     * calculated. If the intersection has as many elements as this mathematical
     * set then <code>true</code> is returned, <code>false</code> otherwise.
     *
     * @param s  set to be checked for being a superset.
     * @return   <code>true</code> if this mathematical set is a subset of the
     *           specifed set, <code>false</code> otherwise.
     * @see      #isSuperset(Set)
     */
    public boolean isSubset(Set s) {
        if (this.size() <= s.size()) {
            MathSet intersect = this.intersection(s);
            return (this.size() == intersect.size());
        } else {
            return false;
        }
    }

    /**
     * Returns <code>true</code> if this mathematical set has no common
     * elements with the specified set.<p>
     *
     * This implementation determines which is the smaller of this set
     * and the specified set by invoking the <code>size()</code>
     * method on each. If this set has fewer elements, then the
     * implementation iterates over this set, checking each element
     * returned by the iterator in turn to see if it is contained in
     * the specified set. If it is so contained, <code>false</code>
     * is returned. If the specified set has fewer elements,
     * then the implementation iterates over the specified set,
     * returning <code>false</code> if it finds a common element.
     *
     * @param s  set to be checked for common elements.
     * @return   <code>true</code> if this mathematical set has no common
     *           elements with the specifed set, <code>false</code> otherwise.
     */
    public boolean isDisjoint(Set s) {
        Iterator iter;

        if (this.size() < s.size()) {
            for (iter = this.iterator(); iter.hasNext(); ) {
                if (s.contains(iter.next())) {
                    return false;
                }
            }
        } else {
            for (iter = s.iterator(); iter.hasNext(); ) {
                if (this.contains(iter.next())) {
                    return false;
                }
            }
        }
        return true;
    }
}
