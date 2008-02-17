/*
 * TestPerfSupersets.java, 17 Feb 2008
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
import java.util.Random;

/**
 * Performance testbench for the supersets methods.
 */
public class TestPerfSupersets {

    public static void main(String[] args) {

        final int ELEM_MAX = 10000;
        final int REPETITIONS = 1;
        final int SEED = 12345;

        int[] MAX_SET_ELEMS = new int[] {9500, 1000, 100, 10};
        int[] NUM_CANDIDATE_SETS = new int[] {100, 1000, 5000};
        int[] NUM_TARGET_ELEMS = new int[] {10, 100, 1000, 5000};
        /*
        int[] MAX_SET_ELEMS = new int[] {9500};
        int[] NUM_CANDIDATE_SETS = new int[] {1000};
        int[] NUM_TARGET_ELEMS = new int[] {10};
        */
        int maxSetElems;
        int numCandidateSets;
        int numTargetElems;
        Random gen = new Random(SEED);
        long timeSumSuper, timeSumFSuper;
        TestResults testResults = new TestResults();

        HashMathSet testMathSet = new HashMathSet();
        HashSetOfSets testSoS = new HashSetOfSets();

        System.out.println("* Performance testbench for the supersets methods *");
        System.out.println();
        System.out.println("E = Maximum number of elements in a set");
        System.out.println("S = Number of candidate sets");
        System.out.println("T = Number of elements in target set");
        System.out.println("TN = Time required by the 'naive' supersets method");
        System.out.println("TO = Time required by the 'optimized' supersets method");
        System.out.println("N = Number of supersets found");

        // Use different (max) numbers of elements in each set
        for (int elemIter = 0; elemIter < MAX_SET_ELEMS.length; elemIter++) {
            maxSetElems = MAX_SET_ELEMS[elemIter];
            System.out.println();

            // Use different numbers of candidate sets
            for (int candIter = 0; candIter < NUM_CANDIDATE_SETS.length; candIter++) {
                numCandidateSets = NUM_CANDIDATE_SETS[candIter];

                if (numCandidateSets * maxSetElems <= 10000000) {

                    // Generate set of sets with current parameters
                    testSoS.clear();
                    for (int setNr = 0; setNr < numCandidateSets; setNr++) {
                        addElem(testMathSet, maxSetElems, ELEM_MAX, gen, true);
                        testSoS.add(testMathSet.clone());
                        testMathSet.clear();
                    }

                    // Use different target set sizes
                    for (int targIter = 0; targIter < NUM_TARGET_ELEMS.length; targIter++) {
                        numTargetElems = NUM_TARGET_ELEMS[targIter];

                        // Generate target set
                        addElem(testMathSet, numTargetElems, ELEM_MAX, gen, false);
                        System.gc();
                        // Perform relation checks
                        timeSumSuper = 0;
                        timeSumFSuper = 0;
                        for (int rep = 0; rep < REPETITIONS; rep++) {
                            timeSumSuper += timeSupersets(testSoS, testMathSet);
                            timeSortedArraySupersets(testSoS, testMathSet, testResults);
                            timeSumFSuper += testResults.time;
                        }

                    output(maxSetElems, numCandidateSets, numTargetElems,
                       (float) timeSumSuper / (float) REPETITIONS,
                       (float) timeSumFSuper / (float) REPETITIONS,
                       testResults.numSets);
                    }
                }
            }
        }

        System.out.println("Done.");
    }

    public static void output(int maxSetElems, int numCandidateSets,
                              int numTargetElems, float timeSuper,
                              float timeFSuper, int numSets) {
        System.out.println("E = " + alignInt(maxSetElems, 4) +
                           ", S = " + alignInt(numCandidateSets, 4) +
                           ", T = " + alignInt(numTargetElems, 4) +
                           ", TN = " + alignFloat(timeSuper, 6) +
                           ", TO = " + alignFloat(timeFSuper, 5) +
                           ", N = " + numSets);
    }

    public static String alignInt(int number, int chars) {
        String str = "" + number;
        for (int addSpaces = str.length(); addSpaces < chars; addSpaces++) {
            str = " " + str;
        }
        return str;
    }

    public static String alignFloat(float number, int chars) {
        String str = "" + number;
        for (int addSpaces = str.length(); addSpaces < chars; addSpaces++) {
            str = " " + str;
        }
        return str;
    }

    public static long timeSupersets(HashSetOfSets set1, HashMathSet set2) {
        long time;
        HashSetOfSets result;

        time = System.currentTimeMillis();
        result = (HashSetOfSets)set1.supersets(set2);
        time = System.currentTimeMillis() - time;
        return time;
    }

    public static void timeSortedArraySupersets(HashSetOfSets set1, HashMathSet set2,
                                         TestResults returnResults) {
        long time;
        HashSetOfSets result;

        returnResults.time = System.currentTimeMillis();
        result = (HashSetOfSets)set1.sortedArraySupersets(set2);
        returnResults.time = System.currentTimeMillis() - returnResults.time;
        returnResults.numSets = result.size();
    }

    public static void addElem(AbstractMathSet set, int numElem,
                               int totalElem, Random gen, boolean vary) {
        boolean changed;
        if (vary) {
            numElem = gen.nextInt(numElem);
        }

        if (numElem < (totalElem / 2)) {
            for (int num = 0; num < numElem; num++) {
                do {
                    changed = set.add(new Integer(gen.nextInt(totalElem)));
                } while (!changed);
            }
        } else {
            for (int num = 0; num < totalElem; num++) {
                changed = set.add(new Integer(num));
            }
            for (int num = totalElem; num > numElem; num--) {
                do {
                    changed = set.remove(new Integer(gen.nextInt(totalElem)));
                } while (!changed);
            }
        }
    }
}

class TestResults {
    public long time;
    public int numSets;
}
