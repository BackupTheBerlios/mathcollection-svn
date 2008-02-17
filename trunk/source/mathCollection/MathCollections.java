/*
 * @(#)MathCollections.java, 17 Dec 2003
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
import java.lang.Cloneable;
import java.io.Serializable;

/**
 * This class consists exclusively of static methods that return mathematical
 * collections. It contains "wrappers", which return a new synchronized
 * mathematical collection backed by a specified collection.
 *
 * The methods of this class all throw a <code>NullPointerException</code>
 * if the mathematical collections provided to them are <code>null</code>.
 *
 * @author S. Hodri, S. Schuetz
 * @version 17 Dec 2003
 * @see MathSet
 * @see SetOfSets
 * @see Multiset
 */
public final class MathCollections {

    // Suppresses default constructor, ensuring non-instantiability.
    private MathCollections() {
    }

    static class SynchronizedMathSet implements MathSet, Serializable, Cloneable {

        private MathSet myMathSet;
        private Object  myMutex;

        SynchronizedMathSet(MathSet ms) {
            this.myMathSet = ms;
            this.myMutex = this;
        }

        SynchronizedMathSet(MathSet ms, Object mutex) {
            this.myMathSet = ms;
            this.myMutex = mutex;
        }

        public int size() {
            synchronized (myMutex) {
                return myMathSet.size();
            }
        }

        public boolean isEmpty() {
            synchronized (myMutex) {
                return myMathSet.isEmpty();
            }
        }

        public void clear() {
            synchronized (myMutex) {
                myMathSet.clear();
            }
        }

        public boolean equals(Object o) {
            synchronized (myMutex) {
                return myMathSet.equals(o);
            }
        }

        public int hashCode() {
            synchronized (myMutex) {
                return myMathSet.hashCode();
            }
        }

        public String toString() {
            synchronized (myMutex) {
                return myMathSet.toString();
            }
        }

        public Object[] toArray() {
            synchronized (myMutex) {
                return myMathSet.toArray();
            }
        }

        public Object[] toArray(Object[] o) {
            synchronized (myMutex) {
                return myMathSet.toArray(o);
            }
        }

        public Iterator iterator() {
            synchronized (myMutex) {
                return myMathSet.iterator();
            }
        }

        public Object clone() {
            synchronized (myMutex) {
                MathSet myNewMathSet = new HashMathSet(this.myMathSet);
                SynchronizedMathSet copy = new SynchronizedMathSet(myNewMathSet);
                return copy;
            }
        }

        public boolean add(Object o) {
            synchronized (myMutex) {
                return myMathSet.add(o);
            }
        }

        public boolean addAll(Collection c) {
            synchronized (myMutex) {
                return myMathSet.addAll(c);
            }
        }

        public boolean contains(Object o) {
            synchronized (myMutex) {
                return myMathSet.contains(o);
            }
        }

        public boolean containsAll(Collection c) {
            synchronized (myMutex) {
                return myMathSet.containsAll(c);
            }
        }

        public boolean remove(Object o) {
            synchronized (myMutex) {
                return myMathSet.remove(o);
            }
        }

        public boolean removeAll(Collection c) {
            synchronized (myMutex) {
                return myMathSet.removeAll(c);
            }
        }

        public boolean retainAll(Collection c) {
            synchronized (myMutex) {
                return myMathSet.retainAll(c);
            }
        }

        public MathSet union(Set s) {
            synchronized (myMutex) {
                return myMathSet.union(s);
            }
        }

        public MathSet intersection(Set s) {
            synchronized (myMutex) {
                return myMathSet.intersection(s);
            }
        }

        public MathSet difference(Set s) {
            synchronized (myMutex) {
                return myMathSet.difference(s);
            }
        }

        public MathSet symmetricDifference(Set s) {
            synchronized (myMutex) {
                return myMathSet.symmetricDifference(s);
            }
        }

        public boolean isDisjoint(Set s) {
            synchronized (myMutex) {
                return myMathSet.isDisjoint(s);
            }
        }

        public boolean isSubset(Set s) {
            synchronized (myMutex) {
                return myMathSet.isSubset(s);
            }
        }

        public boolean isSuperset(Set s) {
            synchronized (myMutex) {
                return myMathSet.isSuperset(s);
            }
        }

        public SetOfSets powerSet() {
            synchronized (myMutex) {
                return myMathSet.powerSet();
            }
        }

        public SetOfSets fixedSizeSubsets(int size) {
            synchronized (myMutex) {
                return myMathSet.fixedSizeSubsets(size);
            }
        }
    }

    static class SynchronizedSetOfSets implements SetOfSets, Serializable, Cloneable {

        private SetOfSets mySetOfSets;
        private Object    myMutex;

        SynchronizedSetOfSets(SetOfSets ms) {
            this.mySetOfSets = ms;
            this.myMutex = this;
        }

        SynchronizedSetOfSets(SetOfSets ms, Object mutex) {
            this.mySetOfSets = ms;
            this.myMutex = mutex;
        }

        public int size() {
            synchronized (myMutex) {
                return mySetOfSets.size();
            }
        }

        public boolean isEmpty() {
            synchronized (myMutex) {
                return mySetOfSets.isEmpty();
            }
        }

        public void clear() {
            synchronized (myMutex) {
                mySetOfSets.clear();
            }
        }

        public boolean equals(Object o) {
            synchronized (myMutex) {
                return mySetOfSets.equals(o);
            }
        }

        public int hashCode() {
            synchronized (myMutex) {
                return mySetOfSets.hashCode();
            }
        }

        public Set toSet() {
            synchronized (myMutex) {
                return mySetOfSets.toSet();
            }
        }

        public Multiset toMultiset() {
            synchronized (myMutex) {
                return mySetOfSets.toMultiset();
            }
        }

        public String toString() {
            synchronized (myMutex) {
                return mySetOfSets.toString();
            }
        }

        public Object[] toArray() {
            synchronized (myMutex) {
                return mySetOfSets.toArray();
            }
        }

        public Object[] toArray(Object[] o) {
            synchronized (myMutex) {
                return mySetOfSets.toArray(o);
            }
        }

        public Iterator iterator() {
            synchronized (myMutex) {
                return mySetOfSets.iterator();
            }
        }

        public Object clone() {
            synchronized (myMutex) {
                SetOfSets myNewSetOfSets = new HashSetOfSets(this.mySetOfSets);
                SynchronizedSetOfSets copy = new SynchronizedSetOfSets(myNewSetOfSets);
                return copy;
            }
        }

        public boolean add(Object o) {
            synchronized (myMutex) {
                return mySetOfSets.add(o);
            }
        }

        public boolean addAll(Collection c) {
            synchronized (myMutex) {
                return mySetOfSets.addAll(c);
            }
        }

        public boolean contains(Object o) {
            synchronized (myMutex) {
                return mySetOfSets.contains(o);
            }
        }

        public boolean containsAll(Collection c) {
            synchronized (myMutex) {
                return mySetOfSets.containsAll(c);
            }
        }

        public boolean remove(Object o) {
            synchronized (myMutex) {
                return mySetOfSets.remove(o);
            }
        }

        public boolean removeAll(Collection c) {
            synchronized (myMutex) {
                return mySetOfSets.removeAll(c);
            }
        }

        public boolean retainAll(Collection c) {
            synchronized (myMutex) {
                return mySetOfSets.retainAll(c);
            }
        }

        public SetOfSets containingSets(Object o) {
            synchronized (myMutex) {
                return mySetOfSets.containingSets(o);
            }
        }

        public boolean containsAtom(Object o) {
            synchronized (myMutex) {
                return mySetOfSets.containsAtom(o);
            }
        }

        public SetOfSets supersets(Set s) {
            synchronized (myMutex) {
                return mySetOfSets.supersets(s);
            }
        }

        public SetOfSets subsets(Set s) {
            synchronized (myMutex) {
                return mySetOfSets.subsets(s);
            }
        }
    }

    static class SynchronizedMultiset implements Multiset, Serializable, Cloneable {

        private Multiset myMultiset;
        private Object   myMutex;

        SynchronizedMultiset(Multiset mus) {
            this.myMultiset = mus;
            this.myMutex = this;
        }

        SynchronizedMultiset(Multiset mus, Object mutex) {
            this.myMultiset = mus;
            this.myMutex = mutex;
        }

        public int size() {
            synchronized (myMutex) {
                return myMultiset.size();
            }
        }

        public boolean isEmpty() {
            synchronized (myMutex) {
                return myMultiset.isEmpty();
            }
        }

        public void clear() {
            synchronized (myMutex) {
                myMultiset.clear();
            }
        }

        public boolean equals(Object o) {
            synchronized (myMutex) {
                return myMultiset.equals(o);
            }
        }

        public int hashCode() {
            synchronized (myMutex) {
                return myMultiset.hashCode();
            }
        }

        public Set toSet() {
            synchronized (myMutex) {
                return myMultiset.toSet();
            }
        }

        public int setSize() {
            synchronized (myMutex) {
                return myMultiset.setSize();
            }
        }

        public String toString() {
            synchronized (myMutex) {
                return myMultiset.toString();
            }
        }

        public Object[] toArray() {
            synchronized (myMutex) {
                return myMultiset.toArray();
            }
        }

        public Object[] toArray(Object[] o) {
            synchronized (myMutex) {
                return myMultiset.toArray(o);
            }
        }

        public Iterator iterator() {
            synchronized (myMutex) {
                return myMultiset.iterator();
            }
        }

        public Object clone() {
            synchronized (myMutex) {
                Multiset myNewMultiset = new HashMultiset(this.myMultiset);
                SynchronizedMultiset copy = new SynchronizedMultiset(myNewMultiset);
                return copy;
            }
        }

        public boolean setQuantity(Object o, int quantity) {
            synchronized (myMutex) {
                return myMultiset.setQuantity(o, quantity);
            }
        }

        public int getQuantity(Object o) {
            synchronized (myMutex) {
                return myMultiset.getQuantity(o);
            }
        }

        public boolean add(Object o) {
            synchronized (myMutex) {
                return myMultiset.add(o);
            }
        }

        public boolean add(Object o, int quantity) {
            synchronized (myMutex) {
                return myMultiset.add(o, quantity);
            }
        }

        public boolean addAll(Collection c) {
            synchronized (myMutex) {
                return myMultiset.addAll(c);
            }
        }

        public boolean contains(Object o) {
            synchronized (myMutex) {
                return myMultiset.contains(o);
            }
        }

        public boolean containsAll(Collection c) {
            synchronized (myMutex) {
                return myMultiset.containsAll(c);
            }
        }

        public boolean remove(Object o) {
            synchronized (myMutex) {
                return myMultiset.remove(o);
            }
        }

        public boolean remove(Object o, int quantity) {
            synchronized (myMutex) {
                return myMultiset.remove(o, quantity);
            }
        }

        public boolean removeAll(Collection c) {
            synchronized (myMutex) {
                return myMultiset.removeAll(c);
            }
        }

        public boolean retainAll(Collection c) {
            synchronized (myMutex) {
                return myMultiset.retainAll(c);
            }
        }

        public Multiset sum(Collection c) {
            synchronized (myMutex) {
                return myMultiset.sum(c);
            }
        }

        public Multiset union(Collection c) {
            synchronized (myMutex) {
                return myMultiset.union(c);
            }
        }

        public Multiset intersection(Collection c) {
            synchronized (myMutex) {
                return myMultiset.intersection(c);
            }
        }

        public Multiset difference(Collection c) {
            synchronized (myMutex) {
                return myMultiset.difference(c);
            }
        }

        public Multiset symmetricDifference(Collection c) {
            synchronized (myMutex) {
                return myMultiset.symmetricDifference(c);
            }
        }

        public boolean isDisjoint(Collection c) {
            synchronized (myMutex) {
                return myMultiset.isDisjoint(c);
            }
        }

        public boolean isSubset(Collection c) {
            synchronized (myMutex) {
                return myMultiset.isSubset(c);
            }
        }

        public boolean isSuperset(Collection c) {
            synchronized (myMutex) {
                return myMultiset.isSuperset(c);
            }
        }
    }

    /**
     * Returns a synchronized (thread-safe) mathematical set backed by the
     * specified mathematical set. In order to guarantee serial access, it is
     * critical that <strong>all</strong> access to the backing mathematical
     * set is accomplished through the returned mathematical set.<p>
     *
     * It is imperative that the user manually synchronize on the returned
     * mathematical set when iterating over it:
     * <pre>
     *  MathSet ms = MathCollections.synchronizedMathSet(new HashMathSet());
     *      ...
     *  synchronized (ms) {
     *      Iterator i = ms.iterator(); // Must be in the synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * Failure to follow this advice may result in non-deterministic behavior.
     *
     * <p>The returned mathematical set will be serializable if the specified
     * mathematical set is serializable.
     *
     * @param ms  the mathematical set to be "wrapped" in a synchronized
     *            mathematical set.
     * @see       #synchronizedMathSet(MathSet, java.lang.Object)
     * @return    a synchronized view of the specified mathematical set.
     */
    public static MathSet synchronizedMathSet(MathSet ms) {
        return new SynchronizedMathSet(ms);
    }

    /**
     * Same behavior as <code>synchronizedMathSet(MathSet)</code>, but the
     * mutex that is blocked by method calls can be specified.
     *
     * @param ms     the mathematical set to be "wrapped" in a synchronized
     *               mathematical set.
     * @param mutex  object to be blocked by method calls.
     * @see          #synchronizedMathSet(MathSet)
     * @return       a synchronized view of the specified mathematical set.
     */
    public static MathSet synchronizedMathSet(MathSet ms, Object mutex) {
        return new SynchronizedMathSet(ms, mutex);
    }

    /**
     * Returns a synchronized (thread-safe) set of sets backed by the
     * specified set of sets. In order to guarantee serial access, it is
     * critical that <strong>all</strong> access to the backing set of sets is
     * accomplished through the returned set of sets.<p>
     *
     * It is imperative that the user manually synchronize on the returned
     * set of sets when iterating over it:
     * <pre>
     *  SetOfSets sos = MathCollections.synchronizedSetOfSets(new HashSetOfSets());
     *      ...
     *  synchronized (sos) {
     *      Iterator i = sos.iterator(); // Must be in the synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * Failure to follow this advice may result in non-deterministic behavior.
     *
     * <p>The returned set of sets will be serializable if the specified set
     * of sets is serializable.
     *
     * @param sos  the set of sets to be "wrapped" in a synchronized
     *             set of sets.
     * @see        #synchronizedSetOfSets(SetOfSets, java.lang.Object)
     * @return     a synchronized view of the specified set of sets.
     */
    public static SetOfSets synchronizedSetOfSets(SetOfSets sos) {
        return new SynchronizedSetOfSets(sos);
    }

    /**
     * Same behavior as <code>synchronizedSetOfSets(SetOfSets)</code>, but
     * the mutex that is blocked by method calls can be specified.
     *
     * @param sos    the set of sets to be "wrapped" in a synchronized
     *               set of sets.
     * @param mutex  object to be blocked by method calls.
     * @see          #synchronizedSetOfSets(SetOfSets)
     * @return       a synchronized view of the specified set of sets.
     */
    public static SetOfSets synchronizedSetOfSets(SetOfSets sos, Object mutex) {
        return new SynchronizedSetOfSets(sos, mutex);
    }

    /**
     * Returns a synchronized (thread-safe) multiset backed by the
     * specified multiset. In order to guarantee serial access, it is
     * critical that <strong>all</strong> access to the backing multiset is
     * accomplished through the returned multiset.<p>
     *
     * It is imperative that the user manually synchronize on the returned
     * multiset when iterating over it:
     * <pre>
     *  Multiset mus = MathCollections.synchronizedMultiset(new HashMultiset());
     *      ...
     *  synchronized (mus) {
     *      Iterator i = mus.iterator(); // Must be in the synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * Failure to follow this advice may result in non-deterministic behavior.
     *
     * <p>The returned multiset will be serializable if the specified multiset
     * is serializable.
     *
     * @param mus  the multiset to be "wrapped" in a synchronized multiset.
     * @see        #synchronizedMultiset(Multiset, java.lang.Object)
     * @return     a synchronized view of the specified multiset.
     */
    public static Multiset synchronizedMultiset(Multiset mus) {
        return new SynchronizedMultiset(mus);
    }

    /**
     * Same behavior as <code>synchronizedMultiset(Multiset)</code>, but the
     * mutex that is blocked by method calls can be specified.
     *
     * @param mus    the multiset to be "wrapped" in a synchronized multiset.
     * @param mutex  object to be blocked by method calls.
     * @see          #synchronizedMultiset(Multiset)
     * @return       a synchronized view of the specified multiset.
     */
    public static Multiset synchronizedMultiset(Multiset mus, Object mutex) {
        return new SynchronizedMultiset(mus, mutex);
    }
}
