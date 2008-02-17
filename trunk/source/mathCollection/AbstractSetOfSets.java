/*
 * @(#)AbstractSetOfSets.java, 8 Sep 2003
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
 * This class provides a skeletal implementation of the <code>SetOfSets</code>
 * interface to minimize the effort required to implement this interface.
 *
 * @author S. Hodri, S. Schuetz
 * @version 8 Sep 2003
 * @see Set
 * @see AbstractSet
 * @see SetOfSets
 */
public abstract class AbstractSetOfSets extends AbstractSet implements SetOfSets {

    /**
     * Returns the hash code value for this set of sets. To get the hash
     * code of this set of sets, new hash code values for every element of
     * this set of sets are calculated from a polynomial of 3rd order and
     * finally summed up.
     * This ensures that <code>s1.equals(s2)</code> implies that
     * <code>s1.hashCode()==s2.hashCode()</code> for any two set of sets
     * <code>s1</code> and <code>s2</code>, as required by the general contract
     * of <code>Object.hashCode()</code>.
     *
     * @return  the hash code value for this set of sets.
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
     * Returns <code>true</code> if this set of sets contains a set in which the
     * specified element is present.
     *
     * @param o  the element whose presence in any elementary set within this
     *           set of sets is tested for.
     * @return   <code>true</code> if any set within this set of sets contains
     *           the specified element, <code>false</code> otherwise.
     */
    public boolean containsAtom(Object o) {
        for (Iterator iter = this.iterator(); iter.hasNext(); ) {
            if (((Set)iter.next()).contains(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a string representation of this set of sets. The string
     * representation consists of a list of the set's elementary sets in the
     * order they are returned by its iterator, enclosed in curly brackets
     * (<code>"{}"</code>). Adjacent sets are separated by the characters
     * <code>", "</code> (comma and space). Elementary sets are converted to
     * strings as by <code>Object.toString()</code>.
     *
     * @return  a string representation of this set of sets.
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

}
