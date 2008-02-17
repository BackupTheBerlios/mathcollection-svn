/*
 * Comparison.java, 17 Feb 2008
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


import mathCollection.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Performance comparison between the HashSet, HashMathSet and HashMultiset
 * classes.
 */
public class Comparison {

    public static void main(String[] args) {

        int[] sourceSizesPS = new int[] {12, 13, 14, 15, 16};
        int[] sourceSizesFSS = new int[] {15, 17};
        int[] subsetSizes = new int[] {4, 6, 8, 10, 12, 14};
        
        final int REPETITIONS = 1;

        HashMathSet testMathSet = new HashMathSet();
        HashMultiset testMultiset = new HashMultiset();
        HashSet testHashSet = new HashSet();
        long timeSumHMS, timeSumHS, timeSumHMuS;
        int sourceSize, subsetSize;

        System.out.println("* powerSet() performance comparison ");
        System.out.println("using HashSet, HashMathSet and HashMultiset classes *");
        System.out.println();
        System.out.println("S = Number of elements in source set");
        System.out.println("TimeHMS = Time to compute the power set using HashMathSet");
        System.out.println("TimeHMuS = Time to compute the power set using HashMultiset");
        System.out.println("TimeHS = Time to compute the power set using HashSet");
        System.out.println();

        // Use different numbers of elements in source set
        for (int sourceIter = 0; sourceIter < sourceSizesPS.length; sourceIter++) {
            sourceSize = sourceSizesPS[sourceIter];

            timeSumHMS = 0;
            timeSumHS = 0;
            timeSumHMuS = 0;
            // Generate source set with given number of elements
            testMathSet.clear();
            testHashSet.clear();
            testMultiset.clear();
            addElem(testMathSet, sourceSize);
            addElem(testHashSet, sourceSize);
            addElemMuS(testMultiset, sourceSize);
            for (int rep = 0; rep < REPETITIONS; rep++) {
                // Time the execution of testMathSet.powerSet()
                timeSumHMS = timeSumHMS + timePowerSetHMS(testMathSet);
                timeSumHS = timeSumHS + timePowerSetHS(testHashSet);
                timeSumHMuS = timeSumHMuS + timePowerSetHMuS(testMultiset);
            }
            outputPS(sourceSize, timeSumHMS / REPETITIONS,
                                 timeSumHS / REPETITIONS,
                                 timeSumHMuS / REPETITIONS);
        }
        
        System.out.println();
        System.out.println();
        System.out.println("* fixedSizeSubsets() performance comparison using HashSet and HashMathSet classes *");
        System.out.println();
        System.out.println("S = Number of elements in source set");
        System.out.println("Size = Number of elements in subsets");
        System.out.println("TimeHMS = Time to compute the FSS using HashMathSet");
        System.out.println("TimeHS = Time to compute the FSS using HashSet");
        System.out.println();

        // Use different numbers of elements in source set
        for (int sourceIter = 0; sourceIter < sourceSizesFSS.length; sourceIter++) {
            sourceSize = sourceSizesFSS[sourceIter];

            // Generate source set with given number of elements
            testMathSet.clear();
            testHashSet.clear();
            addElem(testMathSet, sourceSize);
            addElem(testHashSet, sourceSize);
            
            // Use different numbers of elements in subsets
            for (int subsetIter = 0; subsetIter < subsetSizes.length; subsetIter++) {
                subsetSize = subsetSizes[subsetIter];

                timeSumHMS = 0;
                timeSumHS = 0;
                for (int rep = 0; rep < REPETITIONS; rep++) {
                    // Time the execution of testMathSet.fixedSizeSubsets
                    timeSumHMS = timeSumHMS + timeFSS_HMS(testMathSet, subsetSize);
                    timeSumHS = timeSumHS + timeFSS_HS(testHashSet, subsetSize);
                }
                outputFSS(sourceSize, subsetSize, timeSumHMS / REPETITIONS, timeSumHS / REPETITIONS);
            }
        }
        
        System.out.println("Done.");
    }

    public static void outputPS(int elem, long timeHMS, long timeHS, long timeHMuS) {
        System.out.println("S = " + elem + ", TimeHMS = " + timeHMS
                         + ", TimeHMuS = " + timeHMuS + ", TimeHS = " + timeHS);
    }

    public static void outputFSS(int elem, int size, long timeHMS, long timeHS) {
        System.out.println("S = " + elem + ", Size = " + size + ", TimeHMS = " + timeHMS + ", TimeHS = " + timeHS);
    }

    public static long timePowerSetHMuS(Multiset set) {
        long time;
        Multiset result;

        time = System.currentTimeMillis();
        result = powerSetHMuS(set);
        time = System.currentTimeMillis() - time;
        return time;
    }

    public static long timePowerSetHMS(Set set) {
        long time;
        SetOfSets result;

        time = System.currentTimeMillis();
        result = powerSetHMS(set);
        time = System.currentTimeMillis() - time;
        return time;
    }

    public static long timePowerSetHS(Set set) {
        long time;
        Set result;

        time = System.currentTimeMillis();
        result = powerSetHS(set);
        time = System.currentTimeMillis() - time;
        return time;
    }

    public static long timeFSS_HMS(Set set, int size) {
        long time;
        SetOfSets result;

        time = System.currentTimeMillis();
        result = fixedSizeSubsetsHMS(set, size);
        time = System.currentTimeMillis() - time;
        return time;
    }
    
    public static long timeFSS_HS(Set set, int size) {
        long time;
        Set result;

        time = System.currentTimeMillis();
        result = fixedSizeSubsetsHS(set, size);
        time = System.currentTimeMillis() - time;
        return time;
    }
    
    public static void addElem(Set set, int numElem) {
        for (int num = 0; num < numElem; num++) {
            set.add(new Integer(num));
        }
    }

    public static void addElemMuS(Multiset set, int numElem) {
        for (int num = 0; num < numElem; num++) {
            set.add(new Integer(num));
        }
    }

    // The result is a Multiset, since a SetOfSets cannot contain Multisets 
    public static Multiset powerSetHMuS(Multiset set) {
        HashMultiset powSet = new HashMultiset();
        HashMultiset powNewElements = new HashMultiset();
        HashMultiset element = new HashMultiset();
        HashMultiset temp = new HashMultiset();
        
        // The power set has to contain the empty set
        powSet.add((HashMultiset)element.clone());
        
        for (Iterator iterThisSet = set.iterator(); iterThisSet.hasNext(); ) {
            element.clear();
            powNewElements.clear();
            
            element.add(iterThisSet.next());
            
            for (Iterator iterPowerSet = powSet.iterator(); iterPowerSet.hasNext(); ) {
                temp = (HashMultiset)element.clone();
                temp.addAll((HashMultiset)iterPowerSet.next());
                powNewElements.add(temp);
            }
            powSet.addAll(powNewElements);
        }
        return powSet;
    }
    
    /*
     * This method is identical to the one in the HashMathSet class, it is only
     * included here for completeness' sake.
     */
    public static SetOfSets powerSetHMS(Set set) {
        SetOfSets powSet = new HashSetOfSets();
        SetOfSets powNewElements = new HashSetOfSets();
        HashMathSet element = new HashMathSet();
        HashMathSet temp = new HashMathSet();
        
        // The power set has to contain the empty set
        powSet.add((HashMathSet)element.clone());
        
        for (Iterator iterThisSet = set.iterator(); iterThisSet.hasNext(); ) {
            element.clear();
            powNewElements.clear();
            
            element.add(iterThisSet.next());
            
            for (Iterator iterPowerSet = powSet.iterator(); iterPowerSet.hasNext(); ) {
                temp = (HashMathSet)element.clone();
                temp.addAll((HashMathSet)iterPowerSet.next());
                powNewElements.add(temp);
            }
            powSet.addAll(powNewElements);
        }
        return powSet;
    }
    
    public static Set powerSetHS(Set set) {
        HashSet powSet = new HashSet();
        HashSet powNewElements = new HashSet();
        HashSet element = new HashSet();
        HashSet temp = new HashSet();
        
        // The power set has to contain the empty set
        powSet.add((HashSet)element.clone());
        
        for (Iterator iterThisSet = set.iterator(); iterThisSet.hasNext(); ) {
            element.clear();
            powNewElements.clear();
            
            element.add(iterThisSet.next());
            
            for (Iterator iterPowerSet = powSet.iterator(); iterPowerSet.hasNext(); ) {
                temp = (HashSet)element.clone();
                temp.addAll((HashSet)iterPowerSet.next());
                powNewElements.add(temp);
            }
            powSet.addAll(powNewElements);
        }
        
        return powSet;
    }

    /*
     * This method is identical to the one in the HashMathSet class, it is only
     * included here for completeness' sake.
     */
    public static HashSetOfSets recursiveFixedSizeSubsetsHMS(int numberToAdd,
                                                    Object[] elementPool,
                                                    int elementPoolStart,
                                                    Set setToAdd,
                                                    HashSetOfSets result) {
        HashMathSet temp = new HashMathSet();
        
        if (numberToAdd == 1) {
            for (int i = elementPoolStart; i < elementPool.length; i++) {
                setToAdd.add(elementPool[i]);
                temp.clear();
                temp.addAll(setToAdd);
                result.add(temp);
                setToAdd.remove(elementPool[i]);
            }
        } else {
            for (int i = elementPoolStart; i < elementPool.length; i++) {
                setToAdd.add(elementPool[i]);
                recursiveFixedSizeSubsetsHMS(numberToAdd - 1, elementPool, i + 1,
                                          setToAdd, result);
                setToAdd.remove(elementPool[i]);
            }
        }
        return result;
    }

    /*
     * This method is identical to the one in the HashMathSet class, it is only
     * included here for completeness' sake.
     */
    public static SetOfSets fixedSizeSubsetsHMS(Set set, int size) {
        if ((size < 0) || (size > set.size())) {
            return new HashSetOfSets();
        } else {
            if (size == 0) {
                HashSetOfSets empty = new HashSetOfSets();
                empty.add(new HashMathSet());
                return empty;
            } else {
                return recursiveFixedSizeSubsetsHMS(size,
                                                 set.toArray(),
                                                 0,
                                                 new HashMathSet(),
                                                 new HashSetOfSets());
            }
        }
    }
    
    public static HashSet recursiveFixedSizeSubsetsHS(int numberToAdd,
                                                    Object[] elementPool,
                                                    int elementPoolStart,
                                                    Set setToAdd,
                                                    HashSet result) {
        HashSet temp = new HashSet();
        
        if (numberToAdd == 1) {
            for (int i = elementPoolStart; i < elementPool.length; i++) {
                setToAdd.add(elementPool[i]);
                temp.clear();
                temp.addAll(setToAdd);
                result.add(temp);
                setToAdd.remove(elementPool[i]);
            }
        } else {
            for (int i = elementPoolStart; i < elementPool.length; i++) {
                setToAdd.add(elementPool[i]);
                recursiveFixedSizeSubsetsHS(numberToAdd - 1, elementPool, i + 1,
                                          setToAdd, result);
                setToAdd.remove(elementPool[i]);
            }
        }
        return result;
    }
    
    public static Set fixedSizeSubsetsHS(Set set, int size) {
        if ((size < 0) || (size > set.size())) {
            return new HashSet();
        } else {
            if (size == 0) {
                HashSet empty = new HashSet();
                empty.add(new HashSet());
                return empty;
            } else {
                return recursiveFixedSizeSubsetsHS(size,
                                                 set.toArray(),
                                                 0,
                                                 new HashSet(),
                                                 new HashSet());
            }
        }
    }
}

