/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcode;
import java.util.ArrayList;
import java.lang.Object;
import java.util.Objects;

/**
 *
 * @author Samuel
 */

public class GraphCode {
    
    public static String arrayPrint(int[] toPrint) {
        String sendback = "{";
        for(int counter = 0; counter < toPrint.length; counter++) {
            sendback = sendback + Integer.toString(toPrint[counter]); 
            sendback = sendback + ",";
        }
        return sendback + "}";
    }
    
    public static void swap(int[] incoming, int lowBound, int highBound) {
        incoming[lowBound] = incoming[lowBound] + incoming[highBound]; 
        incoming[highBound] = incoming[lowBound] - incoming[highBound]; 
        incoming[lowBound] = incoming[lowBound] - incoming[highBound]; 
    }
    
    public static void flipper(int[] givenses, int lowEnd, int endpoint) {
        if (endpoint - lowEnd >= 2) {
        int origLow = lowEnd; 
        int origHigh = endpoint; 
        int pivoter = givenses[endpoint - 1]; 
        int highEnd = endpoint - 2; 
        while (lowEnd < highEnd) {
            while ((givenses[lowEnd] <= pivoter)&&(lowEnd < highEnd)) {
                lowEnd++;
            }
            while ((givenses[highEnd] >= pivoter)&&(lowEnd < highEnd)) {
                highEnd--; 
            }
            if (lowEnd < highEnd) {
                swap(givenses, lowEnd, highEnd);
            }
        }
        if (givenses[lowEnd] >= givenses[endpoint - 1]) 
        {//not sure why the [if] is necessary
            swap(givenses, lowEnd, (endpoint - 1)); 
        }//it should happen regardless
        else {lowEnd++;} //really don't know why this part is necessary
        flipper(givenses, origLow, lowEnd); 
        flipper(givenses, lowEnd + 1, origHigh); 
        }  
    }
    
    public static void quickSorter(int[] toSort) {
        flipper(toSort, 0, toSort.length); //a quicksort I coded a while ago
    }

    public static boolean isValidPair(int[] dSeq, int prop1, int prop2) {
        if (dSeq[prop1] == 0 || dSeq[prop2] == 0) {return false;}
        //we do not bond 0's
        //if (dSeq[prop1] > 1 || dSeq[prop2] > 1) {return true;}
        //if we're bonding a leaf and a non-leaf
        if (dSeq[prop1] > 1 && dSeq[prop2] > 1) {
            //if we're bonding two non-leaves
            //System.out.println("Shouldn't be happening.");
            //maybe not the end of the world after all? Perhaps an 
            //artifact from an earlier plan. Should return false, of course,
            //but isn't necessarily supposed to never happen. 
            //System.out.println(arrayPrint(dSeq));
            //System.out.println(prop1+","+prop2+".");
            //System.out.println("AAAAAAAAAAAAAA.");
            //System.out.println("\n\n\n\n\n\n");
            return false;
            //this is "trying" to happen, and letting it happen was what was 
            //causing the zero-calls. Original cause still unclear 1/9/2017
        }
        if (dSeq[prop1] > 1 || dSeq[prop2] > 1) {return true;}
        //if we're bonding a leaf and a non-leaf
        
        for(int seqCount = 0; seqCount < dSeq.length; seqCount++) {
            if(dSeq[seqCount] > 1) {return false;}
            //if we're proposing bonding two leaves and there's any non-leaf 
            //left in the sequence: abort
        }
        
        return true; //if we're bonding two leaves and there's nothing else left
    }
    
    public static void cloneMake(
            //copies the elements of [model] into the empty list [blank]
            //in such a way that the elements of [blank] can be altered 
            //independently from those of [model]. 
            ArrayList<ArrayList<Integer>> model, 
            ArrayList<ArrayList<Integer>> blank
    ) {
        int outerCount = 0; 
        int innerCount;
        while (outerCount < model.size()) {
        //for every nested arrayList in the clone model
            innerCount = 0;
            ArrayList<Integer> rower = new ArrayList<Integer>();
            blank.add(rower);
            while(innerCount < model.get(outerCount).size()) {
                //for every element in the nested arrayList
                blank.get(outerCount).add(model.get(outerCount).get(innerCount));
                innerCount++;
            }
            outerCount++;
        }
    }
    
    public static int[] farthestNode(ArrayList<ArrayList<Integer>> vecinos, 
            int currentSpot, int prevSpot, int totalLength){
        //a program to find the farthest node from a given node 
        ArrayList<Integer> currents = vecinos.get(currentSpot);
        //get all the neighbors for the current location 
        int currentCount = 0;
        int[] sendUp = new int[2];
        int currentLength = currents.size(); 
        if ((currentLength == 1)  && (currents.get(0) == prevSpot)) {
            //if the current location has only one neighbor 
            //and that neighbor is the previous location
            //which it always will, in the end, as long as it's a tree
            sendUp[0] = totalLength; 
            sendUp[1] = currentSpot; 
            return sendUp;
        }
        int nextStop;
        int[] protoSend = new int[2];
        int bestToDate = -1;
        //otherwise, multiple neighbors means multiple routes 
        while (currentCount < currentLength) {
            nextStop = currents.get(currentCount);
            if (nextStop != prevSpot) {
            //if we're not backtracking
                protoSend = farthestNode(vecinos, nextStop, currentSpot, 
                        totalLength + 1);
                if (protoSend[0] > bestToDate) {
                    //if the recursive call beats all other recursive calls
                    //seen so far, in terms of length
                    sendUp = protoSend.clone(); 
                    bestToDate = protoSend[0]; 
                }
            }
            currentCount++;
        }
    return sendUp; 
    }
    
    public static void miniClone(
            ArrayList<Integer> thing1, 
            ArrayList<Integer> thing2) {
        //copies the elements of thing1 into thing2, in order
        int thingCount = 0; 
        int oneSize = thing1.size(); 
        int oneHold; 
        while (thingCount < oneSize) {
            oneHold = thing1.get(thingCount);
            thing2.add(oneHold);
            thingCount++;
        }
    }
    
    public static ArrayList<ArrayList<Integer>> findBackbone(
            ArrayList<ArrayList<Integer>> hasSpine, int start, int finish,
            int previousSpot, ArrayList<Integer> soFar) {
        //Inputs: the neighbors-list representing the tree, and the two ends 
        //of the backbone (pre-determined), as well as a value representing
        //the previously-visited node (initially set to -1) and an arrayList
        //containing the backbone being built so far (initially empty)
        ArrayList<Integer> locals = hasSpine.get(start);
        soFar.add(start); //adding the present node to the chain
        int localCount = 0; 
        int localHold;
        int localSize = locals.size();
        ArrayList<Integer> farClone;// = new ArrayList<Integer>(); 
        if (localSize == 1 && start == finish) {
            //if we're at the other end, and the correct other end at that
            ArrayList<ArrayList<Integer>> answers = (
                    new ArrayList<ArrayList<Integer>>()
                    ); 
            ArrayList<Integer> successVal = new ArrayList<Integer>(); 
            successVal.add(6); 
            answers.add(soFar); 
            answers.add(successVal); //so that it will be apparent to the 
            //calling context that this chain actually found the finish line
            return answers;
        }
        ArrayList<ArrayList<Integer>> resultHold = new ArrayList<ArrayList<Integer>>();
        while (localCount < localSize) {
            //for every neighbor of the current node
            farClone = new ArrayList<Integer>();
            miniClone(soFar, farClone); 
            //make an independent copy of farClone
            localHold = locals.get(localCount);
            if (localHold != previousSpot) {
                //if we're not backtracking
                resultHold = findBackbone(hasSpine, localHold, finish, start, farClone);
                if (resultHold.get(1).size() > 0) {
                    //if there's anything at all in the second position of the
                    //arrayList returned by the recursive call, which will be
                    //there only if the recursive call found the other end:
                    return resultHold;
                }
            }
            localCount++;
        }
        //if we've made it this far, then none of the recursive calls actually
        //reached the finish line: return an arrayList signifying failure
        ArrayList<Integer> failVal = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> answerFail = (
                new ArrayList<ArrayList<Integer>>()
                ); 
        answerFail.add(soFar);
        answerFail.add(failVal);
        return answerFail;
    }
    
    public static int[] findCenter(ArrayList<ArrayList<Integer>> toFind) {
        //Inputs: an arrayList containing the neighbors of each node 
        //Outputs: an array containing either the only possible center or 
        //both possible centers (hard to say initially which it will be, so 
        //calling contexts must be prepared to handle both cases)
        int findNotEmpty = 0; 
        while (toFind.get(findNotEmpty).isEmpty() && 
                findNotEmpty < toFind.size()) {
            findNotEmpty++;
        }
        //keep incrementing until we hit a node with actual neighbors
        //any will do
        if (findNotEmpty == toFind.size()) {
        //if the entire neighbors list is empty 
            int[] toBack = {-1}; //return an error code of sorts
            return toBack; //should never happen 
        }
        int end1 = farthestNode(toFind, findNotEmpty, -1, 0)[1];
        //find the farthest node from whatever random node we just selected
        int end2 = farthestNode(toFind, end1, -1, 0)[1];
        //and then find the farthest node from that. These two farthest nodes
        //will be the endpoints of our backbone. 
        ArrayList<Integer> blank = new ArrayList<Integer>(); 
        ArrayList<Integer> path = (
                findBackbone(toFind, end1, end2, -1, blank).get(0)
                );
        //and, armed with those endpoints, we may call a function that 
        //yields the backbone in question, in the form of an arrayList of nodes
        int pathSize = path.size();
        if (pathSize % 2 != 0) {//if it's a path of odd length
            int[] toBack = {path.get(pathSize / 2)}; 
            return toBack; //get the middle element and return it
        }
        else {//if it's a path of even length, things are more difficult
            int[] toBack = {path.get(pathSize / 2), (path.get((pathSize / 2) - 1))};
            return toBack; //we'll take both of the "middle" nodes
        }
    }
    
    public static boolean arrayEquals(String[] array1, String[] array2) {
        //a function to determine if two String[] arrays are equal
        //probably already exists in the java library, but I wanted my own.  
        int length1 = array1.length; 
        int length2 = array2.length; 
        if (length1 != length2) {return false;}
        int elemCount = 0; 
        while (elemCount < length1) {
            if (!(array1[elemCount].equals(array2[elemCount]))) {
                return false;
            }
            elemCount++;
        }
        return true;
    }
    
    public static String stringSort(String[] makeResults, boolean hasZ) {
        //a function to sort the String[] given, and optionally to remove 
        //one of its elements from the final result
        int totalSize = makeResults.length;
        //if (totalSize == 0) {System.out.println("Zero call.");}
        //if (totalSize == 0) {return "";} //really don't know why this is 
        //necessary, since the function should never be called under this 
        //circumstance, but somewhere along the line, it is. Will figure out 
        //where and why (seems to no longer be a problem 01/26/17)
        //also: not weeding out all isomorphisms? Most, but not all 
        //(we know why this is now; see below node 01/26/17)
        //FOR TESTING PURPOSES
        /*System.out.println("Before sorting: ");
        int preCheck = 0;
        while (preCheck < totalSize) {
            System.out.print(makeResults[preCheck] + "_"); 
            preCheck++;
        }//END TESTS
        System.out.println("");*/
        int arraySize;
        if (hasZ) {arraySize = totalSize - 1;}
        else {arraySize = totalSize;}
        String[] sortedStuff = new String[arraySize];
        int fillerCount = 0; 
        while (fillerCount < arraySize) {
            sortedStuff[fillerCount] = "empty";
            fillerCount++;
        }
        int sortCount = 0; 
        String toPlace;
        String toCompare;
        int placeCount;
        int finalCount;
        while (sortCount < totalSize) {
            //for every element in the unsorted array, including the parent
            toPlace = makeResults[sortCount]; 
            placeCount = 0;
            finalCount = 0;
            if (!toPlace.equals("z")) {
                //if it's not the parent
                while (placeCount < totalSize) {
                    toCompare = makeResults[placeCount];
                    if (!toCompare.equals("z") && (toPlace.compareTo(toCompare) > 0)) {
                        //if it's being compared to some string other than the parent
                        //and it proves greater 
                        finalCount++;
                    }//then augment its place in the final array.  
                    placeCount++; //This always increases. 
                }
                
                while (!sortedStuff[finalCount].equals("empty")) {
                    finalCount++;
                    //necessary because there may be identical entries 
                    //and the insertion sort would put them all right on top 
                    //of each other. In order to avoid this, all values in the 
                    //final array start out as "empty" and we only place new 
                    //elements in places that are still "empty". 
                }
                sortedStuff[finalCount] = toPlace;
            }//put this round's element in the place we determined it should go
            sortCount++;
        }
        String rohirrim = "";
        //FOR TESTING PURPOSES (though not exclusively so)
        int horseCount = 0; 
        while (horseCount < sortedStuff.length) {
            rohirrim = rohirrim + sortedStuff[horseCount]; 
            horseCount++;
        }
        //System.out.println("After sorting: "+rohirrim+"."); 
        //END TESTS
        return rohirrim;
    }
    
    public static String strictDescript(ArrayList<ArrayList<Integer>> repr, 
            int curNod, int prevNod, int[] origRep) {
        if (repr.get(curNod).size() == 1) {
            return "1b";
        }
        ArrayList<Integer> locality = repr.get(curNod); 
        int allaKinder = locality.size();
        int kinderCount = 0; 
        int lokiHold;
        boolean hasNote = false;
        String[] answers = new String[allaKinder];
        while (kinderCount < allaKinder) {
            lokiHold = locality.get(kinderCount);
            if (lokiHold == prevNod) {
                answers[kinderCount] = "z";
                hasNote = true;
            }
            else {
                answers[kinderCount] = strictDescript(repr, lokiHold, curNod, 
                        origRep);
            }
            kinderCount++;
        }
        String sortedStr = stringSort(answers, hasNote);
        //and here lies the main difference between this and makeDescript
        return Integer.toString(origRep[curNod])+sortedStr+"b";
    }
    
    public static String makeDescript(ArrayList<ArrayList<Integer>> repper, 
            int curNode, int prevNode) {
        if (repper.get(curNode).size() == 1) {
            //if we've hit a leaf, then the neighbors list will be of size 1
            return "ab";
            //yet this may not be catching all the leaves somehow? 
            //now it is, it seems like 01/26/17
        }
        //otherwise: recursive calls on all the children nodes
        //and we'll need to arrange them in some semblance of order
        ArrayList<Integer> locals = repper.get(curNode);
        int allChildren = locals.size();
        //one of the neighbors will be the parent, but we'll know it when 
        //we see it
        int childCount = 0; 
        int localHold;
        boolean hasMarker = false;
        String[] results = new String[allChildren];
        while (childCount < allChildren) {
            localHold = locals.get(childCount);
            if (localHold == prevNode) {
                //if the neighbor in question is the parent
                results[childCount] = "z";
                hasMarker = true;
                //we give it a special marker, which will be discarded later
            }
            else {
                //as long as the neighbor in question isn't the parent
                results[childCount] = makeDescript(repper, localHold, curNode);
                //we perform a recursive call here
            }
            childCount++;
        }
        //now all that remains is to sort the results[], remove the 
        //result tagged as being the parent, and return the concatenated string
        //with its own "a" and "b" added on
        String sortedString = stringSort(results, hasMarker);
        return "a"+sortedString+"b";
    }
    
    public static boolean areIsomorphic(ArrayList<ArrayList<Integer>> thing1, 
            ArrayList<ArrayList<Integer>> thing2, boolean isStrict, 
            int[] startSeq) {
        //determines if the tree represented by [thing1] 
        //is isomorphic to that represented by [thing2]
        //but: as it turns out, doing in-line isomorphism removal closes 
        //off possibilities. Must design a stricter qualification for that
        /*boolean testCase = false;
        if (thing1.get(7).size() > 1 && thing1.get(7).get(1) == 5) {
            if (thing1.get(5).size() > 1 && thing1.get(5).get(1) == 4) {
                if (thing1.get(4).size() > 1 && thing1.get(4).get(1) == 3) {
                    testCase = true; 
                }
            }
        }*/

        int[] center1 = findCenter(thing1); 
        int[] center2 = findCenter(thing2);
        if (center1.length != center2.length) {
            //if one tree has one center but the other has two 
            return false; 
        }
        String treeRep1; 
        String treeRep2; 
        if (isStrict) {
            treeRep1 = strictDescript(thing1, center1[0], -1, startSeq);
            treeRep2 = strictDescript(thing2, center2[0], -1, startSeq);
        }
        else {
            treeRep1 = makeDescript(thing1, center1[0], -1); 
            treeRep2 = makeDescript(thing2, center2[0], -1);
        }
        String treeRep1part2 = "";
        
        //given the trees as arrayLists, the center nodes, and a default 
        //value of -1, we get a String for each tree which should be unique 
        //to that tree and all its isomorphisms
        /*if (testCase) {
            System.out.println("TEST CASE /n/n/n/n/n/n"); 
            System.out.println(thing1);
            System.out.println(thing2);
            System.out.println(arrayPrint(center1));
            System.out.println(arrayPrint(center2));
            System.out.println(treeRep1);
            System.out.println(treeRep2);
        }*/
        if (treeRep1.equals(treeRep2)) {
            return true;
        }
        if (center1.length > 1) {
            //if both trees have two centers, its possible that we tested 
            //the wrong ones against each other 
            if (isStrict) {
                treeRep1part2 = strictDescript(thing1, center1[1], -1, 
                        startSeq);
            }
            else {
                treeRep1part2 = makeDescript(thing1, center1[1], -1);
            }
            //so we'll test the other possible pairing 
            if (treeRep1part2.equals(treeRep2)) {
                return true; 
            }
        }
        return false; 
    }
    
    public static ArrayList<ArrayList<ArrayList<Integer>>> isomorphRemove(
            ArrayList<ArrayList<ArrayList<Integer>>> givens, 
            boolean strictVersion, int[] primaSeq) {
        //inputs: an arrayList containing trees, themselves represented as 
        //arraylists
        //Outputs: a similar arraylist containing trees, but with all 
        //isomorphisms removed 
        ArrayList<ArrayList<ArrayList<Integer>>> emptyStart = 
                new ArrayList<ArrayList<ArrayList<Integer>>> (); 
        int realizeCount = 0; 
        int emptyCount; 
        boolean isAnIsomorphism; 
        ArrayList<ArrayList<Integer>> currentRealization; 
        ArrayList<ArrayList<Integer>> toCheck;
        while (realizeCount < givens.size()) {
            //for every tree in the input arrayList
            isAnIsomorphism = false; 
            emptyCount = 0; 
            currentRealization = givens.get(realizeCount);
            while (emptyCount < emptyStart.size() && (!isAnIsomorphism)) {
                //for every tree we're planning to return as non-isomorphic
                toCheck = emptyStart.get(emptyCount); 
                if (areIsomorphic(currentRealization, toCheck, strictVersion, 
                        primaSeq)) {
                    //if the current tree is isomorphic to one we're already
                    //planning to return, under the conditions requested: 
                    //flag it as such
                    System.out.println("");
                    System.out.println(currentRealization);
                    System.out.println("is isomorphic to"); 
                    System.out.println(toCheck);
                    System.out.println("under conditions "+strictVersion);
                    isAnIsomorphism = true; 
                }
                emptyCount++;
            }
            if (!isAnIsomorphism) {
                //if the tree isn't isomorphic to anything we've seen so far: 
                emptyStart.add(currentRealization);
            }
            realizeCount++;
        }
        return emptyStart;
    }
    
    public static ArrayList<ArrayList<ArrayList<Integer>>> treeTwo(
            ArrayList<ArrayList<Integer>> inputs, 
            int[] degreeSeq, int[] origSeq) {
        //inputs: the degree sequence and an empty tree holder
        //outputs: the list of all non-isomorphic trees matching the 
        //input degree sequence 
        if (degreeSeq.length >= 8 && degreeSeq[7] == 1) {
            System.out.println(arrayPrint(degreeSeq));
        }
        int counter = 0;
        int countup; 
        int[] outClone;
        ArrayList<ArrayList<ArrayList<Integer>>> sendback = 
                new ArrayList<ArrayList<ArrayList<Integer>>>();
        //to contain the results. You'll see
        while (counter < degreeSeq.length && degreeSeq[counter] == 0) {
            counter++;//finding the first non-zero entry in the degree sequence
        }
        if (counter == degreeSeq.length) {
            //if the entire degree sequence is zeroes 
            sendback.add(inputs);
            //will always be adding a blank arrayList, while it calls downwards
            return sendback;
        }
        ArrayList<ArrayList<Integer>> resultClone = 
                new ArrayList<ArrayList<Integer>>();
        int resultCount; 
        countup = counter + 1;
        while (countup < degreeSeq.length) {
            if (isValidPair(degreeSeq, counter, countup)){
            //if we've found a valid link to make 
                outClone = degreeSeq.clone();
                outClone[counter] = outClone[counter] - 1; 
                outClone[countup] = outClone[countup] - 1;
                //form the link in our degree sequence
                ArrayList<ArrayList<ArrayList<Integer>>> results = 
                        treeTwo(inputs, outClone, origSeq);
                //and get back all the ways of making trees out of what's left
                //here would be the place to target isomorphisms
                //results = isomorphRemove(results); //PUT THIS BACK IN
                //Removing isomorphisms as we go doesn't work. The reason for 
                //this is that lower-level trees are not the platonic ideal of 
                //such trees: they have specific numbers attached, which upper-
                //level calling contexts may interpret in different ways, 
                //closing off possibilities while doing nothing but removing
                //isomorphisms. Removing isomorphisms at the "end" still 
                //works. Avenues forward include: making the lower-level trees 
                //closer to platonic, making the upper-level bonds closer to 
                //platonic, and finding some "stricter" qualifier for 
                //isomorphism that is clever enough not to trim branches on 
                //the tree of possibilities. 
                resultCount = 0;
                System.out.println("This round's results: ");
                while (resultCount < results.size()) {
                //for all the ways of making trees out of what's left:
                    System.out.println(results.get(resultCount));
                    resultClone = new ArrayList<ArrayList<Integer>>();
                    cloneMake(results.get(resultCount), resultClone);
                    //make a clone of that particular way 
                    resultClone.get(counter).add(countup);
                    resultClone.get(countup).add(counter);
                    //and add the pairing to that particular way 
                    sendback.add(resultClone);
                    //and add the updated way to the return clump 
                    resultCount++;
                }
                System.out.println("End results.");
            }
                countup++;
        }
        //In-line removal seems to be working with the modified isomorphism
        //check, and working noticeably faster at that 01/28/17
        sendback = isomorphRemove(sendback, true, origSeq);
        return sendback;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[] start = {1, 1, 1, 1, 2, 2, 2, 4};
        quickSorter(start);
        ArrayList<Integer> basicList = new ArrayList<Integer>();
        ArrayList<Integer> basicClone;
        ArrayList<ArrayList<Integer>> toPass = new ArrayList<ArrayList<Integer>>();
        int startCount = 0; 
        while (startCount < start.length) {
            basicClone = new ArrayList(basicList);
            toPass.add(basicClone);
            startCount++;
        }
        ArrayList<Integer> blankList = new ArrayList<Integer>();
        ArrayList<ArrayList<ArrayList<Integer>>> toGive = new ArrayList<ArrayList<ArrayList<Integer>>>();
        toGive = treeTwo(toPass, start, start);
        System.out.println("Function call over.");
        //toGive = isomorphRemove(toGive, false, start); //not sure why this is necessary.
        //Could define a helper function to remove it. 
        //The concerning part is that this is seeming to weed out things that 
        //aren't isomorphic after all, or else that not all possibilities
        //are being enumerated in the first place. 1/12/17 
        //See above note; fixed and no longer necessary 01/28/17
        //System.out.println("Function call over.");
        Integer countTree = 0; 
        while (countTree < toGive.size()) {
            System.out.println(countTree+": "+toGive.get(countTree));
            countTree++;
        }
    }
}
