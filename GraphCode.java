/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcode;
import java.util.ArrayList;
import java.lang.Object;
import java.util.Objects;
import java.time.Instant;
import java.time.Duration;

/**
 *
 * @author Samuel
 */

class treeOfTrees {
//This is a data structure for storing realizations of tree-able degree 
//sequences, indexed by the sequences themselves. Insertion and retrieval 
//should be linear-time, relative to the length of the sequence. 
    public ArrayList<treeOfTrees> offspring; 
    //the list of children in the tree. Will be null if this node 
    //contains a sequence and its corresponding realizations. 
    public int childID;
    //the integer representing what "number" in the sequence corresponds to 
    //this node. For leaves, will always be the first node in the corresponding
    //sequence. 
    public ArrayList<ArrayList<ArrayList<Integer>>> listing;
    //for a leaf, the list of realizations corresponding to the degree sequence
    //represented by the leaf. Null for all others. 
    public treeOfTrees(int newChildID) {
        //for creating a branch node, rather than a leaf. 
        //Will only be called once by an outside context, for the root; 
        //the other branches will build themselves via bindNewSequence. 
        offspring = null;
        //will be filled in later 
        listing = null;
        childID = newChildID;
    }
    public treeOfTrees(int[] newSeqID, ArrayList<ArrayList<ArrayList<Integer>>> 
            toStore) {
        //for creating a leaf node, rather than a branch. Should not need to 
        //be called at all by an outside context; will be called instead by 
        //bindNewSequence. 
        offspring = null;
        listing = toStore;
        childID = newSeqID[0];
    }
    public void addChild(treeOfTrees toAdd) {
        //add the specified node as a child to the current node 
        if (this.offspring == null) {
            this.offspring = new ArrayList<treeOfTrees>();
        }
        this.offspring.add(toAdd);
    }
    public void bindNewSequence(int[] newSequence, int curIndex,
            ArrayList<ArrayList<ArrayList<Integer>>> newStorage) {
        //For inserting a sequence into its proper position in the tree, 
        //and its realizations along with it. 
        //Inputs: the sequence, and a value representing the current index in
        //the sequence (initially curIndex.length - 1, eventually 0), and of 
        //course the list of representations to be inserted for [newSequence].
        if (curIndex == 0) {
            //if we've reached the correct position in the tree and it's 
            //time to create the storage leaf 
            treeOfTrees nuovoChild = new treeOfTrees(newSequence, newStorage);
            this.addChild(nuovoChild);
            return;
        }//otherwise: we need to find the next child branch to recurse on
        //or, if there is none yet, create such a child branch 
        int toFindOrCreate = newSequence[curIndex];
        treeOfTrees novaChild;
        if (this.offspring == null) {//if no child branches exist yet 
            novaChild = new treeOfTrees(toFindOrCreate);
            novaChild.bindNewSequence(newSequence, curIndex - 1, newStorage);
            this.addChild(novaChild); 
            return;
        }
        int toSearch = this.offspring.size();
        int searchCount = 0; 
        boolean doneVal = false;
        while (searchCount < toSearch && !doneVal) {
            if (this.offspring.get(searchCount).childID == toFindOrCreate) {
                //if the desired child branch does exist and we've found it 
                doneVal = true; 
                this.offspring.get(searchCount).bindNewSequence(newSequence, 
                        curIndex - 1, newStorage);
            }
            searchCount++;
        }
        if (!doneVal) {//if some child branches already exist, but not the 
            //one we were looking for 
            novaChild = new treeOfTrees(toFindOrCreate);
            novaChild.bindNewSequence(newSequence, curIndex - 1, newStorage);
            this.addChild(novaChild); 
        }
    }
}

public class GraphCode {
//the main body of our code for generating realizations of tree-able 
//degree sequences. Written by S. Stern with advice from D. Krizanc. 
    
    public static String arrayPrint(int[] toPrint) {
        //only returns a string representing the given int array 
        //doesn't actually print it; that's left to the calling context
        String sendback = "{";
        for(int counter = 0; counter < toPrint.length; counter++) {
            sendback = sendback + Integer.toString(toPrint[counter]); 
            if(counter != toPrint.length - 1) {
                sendback = sendback + ",";
            }
        }
        return sendback + "}";
    }
    
    public static String dualPrint(int[][] toPrint, int spot, int size) {
        //only returns a string representing (part of) the given int array 
        //doesn't actually print it; that's left to the calling context
        String sendback = "{";
        for(int counter = 0; counter < size; counter++) {
            sendback = sendback + Integer.toString(toPrint[counter][spot]); 
            if(counter != size - 1) {
                sendback = sendback + ",";
            }
        }
        return sendback + "}";
    }
    
    public static void swap(int[] incoming, int lowBound, int highBound) {
        //switches the elements at indexes [lowBound] and [highBound] 
        //in [incoming]. Constant time. 
        incoming[lowBound] = incoming[lowBound] + incoming[highBound]; 
        incoming[highBound] = incoming[lowBound] - incoming[highBound]; 
        incoming[lowBound] = incoming[lowBound] - incoming[highBound]; 
    }
    
    public static void flipper(int[] givenses, int lowEnd, int endpoint) {
        //part of a quicksort coded for an earlier project
        //not actually used in this version of the thesis but left here 
        //just in case
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
        {
            swap(givenses, lowEnd, (endpoint - 1)); 
        }
        else {lowEnd++;} 
        flipper(givenses, origLow, lowEnd); 
        flipper(givenses, lowEnd + 1, origHigh); 
        }  
    }
    
    public static void quickSorter(int[] toSort) {
        flipper(toSort, 0, toSort.length); //a quicksort coded for an earlier 
    }//project. Not used in this version of the thesis but still here, just 
    //in case

    public static boolean isValidPair(int[] dSeq, int prop1, int prop2) {
        //inputs: a degree sequence, and two ints representing nodes in the 
        //sequence which we're proposing to bind to each other 
        //outputs: the boolean decision of whether or not the proposed bond 
        //is actually valid under our rules (which are described below) 
        if (dSeq[prop1] == 0 || dSeq[prop2] == 0) {return false;}
        //we do not bond 0's
        if (dSeq[prop1] > 1 && dSeq[prop2] > 1) {
            //if we're bonding two non-leaves
            return false;
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
            //independently from those of [model]. Necessary because Java 
            //[arrayLists] are stored as pointers rather than as copiable 
            //elements
            ArrayList<ArrayList<Integer>> model, 
            ArrayList<ArrayList<Integer>> blank) {
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
    
    public static void singleClone(ArrayList<Integer> model, 
            ArrayList<Integer> blank) {
        //similar to cloneMake, but with singly-nested [ArrayList]s instead
        int modelCount = 0;
        while (modelCount < model.size()) {
            blank.add(model.get(modelCount));
            modelCount++;
        }
    }
    
    public static void plusClone(ArrayList<Integer> model, 
            ArrayList<Integer> blank) {
        //similar to singleClone, but adds 1 to all the values it copies 
        //used much later in a function called [addZero] 
        int modelCount = 0;
        while (modelCount < model.size()) {
            blank.add((model.get(modelCount) + 1));
            modelCount++;
        }
    }
    
    public static void tripleClone(ArrayList<ArrayList<ArrayList<Integer>>> 
            model, ArrayList<ArrayList<ArrayList<Integer>>> blank) {
        //similar to cloneMake, but with triply-nested [ArrayList]s instead 
        int outerCount = 0; 
        int middleCount; 
        int middleCap; 
        int innerCount; 
        int innerCap; 
        int outerCap = model.size(); 
        ArrayList<ArrayList<Integer>> middleBlank;
        ArrayList<ArrayList<Integer>> middleHold; 
        ArrayList<Integer> innerBlank;
        ArrayList<Integer> innerHold; 
        while (outerCount < outerCap) {
            middleBlank = new ArrayList<ArrayList<Integer>>(); 
            middleHold = model.get(outerCount);
            middleCount = 0;
            middleCap = middleHold.size(); 
            while (middleCount < middleCap) {
                innerBlank = new ArrayList<Integer>(); 
                innerHold = middleHold.get(middleCount);
                innerCount = 0; 
                innerCap = innerHold.size(); 
                while (innerCount < innerCap) {
                    innerBlank.add(innerHold.get(innerCount));
                    innerCount++;
                }
                middleBlank.add(innerBlank);
                middleCount++;
            }
            blank.add(middleBlank);
            outerCount++;
        }
    }
    
    public static int[] farthestNode(ArrayList<ArrayList<Integer>> vecinos, 
            int currentSpot, int prevSpot, int totalLength){
        //inputs: the neighbors listing, and ints representing: 
        //the current node (initially the start value), the last node visited,
        //and the length of the chain of nodes visited so far (initially 0)
        //outputs: an int[] in which one value is that of the current chain 
        //length, and in which the other is the end-node of the chain
        //such that, in the very end, the output will be the length of the 
        //longest chain, and the farthest node from the starting point
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
        //Outputs: an arrayList<arrayList>> containing the backbone of the 
        //tree, or else a list signifying that the backbone wasn't found
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
        //Outputs: an array containing either the only possible center (as an 
        //int) or both possible centers (hard to say initially which it will 
        //be, so calling contexts must be prepared to handle both cases)
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
            return toBack; //should never actually happen, though; never does
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
        //probably already exists in the java library, but we wanted our own.  
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
        //Essentially insertionSort, which is fine, given the (relatively)
        //small sizes of inputs that this function will normally be called for.
        //Inputs: the String[] to be sorted, and the boolean determining 
        //whether or not there will be a dummy value to remove (the calling 
        //context will know). 
        int totalSize = makeResults.length;
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
                    if (!toCompare.equals("z") && (toPlace.compareTo(toCompare) 
                            > 0)) {
                        //if it's being compared to something besides the parent
                        //and it turns out to be greater 
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
        //with the array in order, we still need to build the string to return
        int horseCount = 0; 
        while (horseCount < sortedStuff.length) {
            rohirrim = rohirrim + sortedStuff[horseCount]; 
            horseCount++;
        }
        return rohirrim;
    }
    
    public static String strictDescript(ArrayList<ArrayList<Integer>> repr, 
            int curNod, int prevNod, int[] origRep) {
        //a function for building canonical names to detect isomorphism 
        //up-to-original-degree. 
        //Inputs: the node which this recursive call should focus on 
        //(initially the "center", or one of them); the node which the 
        //previous recursive call focused on (initially -1, which is to say 
        //"none"), the representation which needs a canonical name, and the 
        //original degree sequence that gave rise to it 
        if (repr.get(curNod).size() == 1) {
            return Integer.toString(origRep[curNod])+"b";
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
        return Integer.toString(origRep[curNod])+sortedStr+"b";
    }
    
    public static String makeDescript(ArrayList<ArrayList<Integer>> repper, 
            int curNode, int prevNode) {
        //a function for building canonical names to detect isomorphism 
        //(not up-to-original-degree, just isomorphism in general). 
        //Inputs: the node which this recursive call should focus on 
        //(initially the "center", or one of them); the node which the 
        //previous recursive call focused on (initially -1, which is to say 
        //"none"), and the representation which needs a canonical name 
        if (repper.get(curNode).size() == 1) {
            //if we've hit a leaf, then the neighbors list will be of size 1
            return "ab";
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
    
    public static String makeARep(ArrayList<ArrayList<Integer>> taken, 
            int[] initiaSeq) {
        //makes a canonical name of the input realization (up-to-original-
        //degree). 
        int[] centers = findCenter(taken);
        String treeRep1 = strictDescript(taken, centers[0], -1, initiaSeq);
        return treeRep1; //inline version
    }
    
    public static String makeBasicRep(ArrayList<ArrayList<Integer>> taken, 
            int[] initiaSeq) {
        //makes a canonical name of the input realization (not up-to-original
        //degree). 
        int[] centers = findCenter(taken);
        String treeRep1 = makeDescript(taken, centers[0], -1);
        return treeRep1; //basic version
    }

    public static String makeOtherRep(ArrayList<ArrayList<Integer>> taken, 
            int[] initiaSeq) {
        //makes the up-to-original-degree canonical name for the input 
        //sequence based on the "second" center, if one exists. Returns "zilch"
        //if it doesn't. 
        int[] centers = findCenter(taken);
        if (centers.length != 2) {return "zilch";}
        String treeRep2 = strictDescript(taken, centers[1], -1, initiaSeq);
        return treeRep2; //inline version
    }
    
    public static String makeOtherBasicRep(ArrayList<ArrayList<Integer>> taken, 
            int[] initiaSeq) {
        //makes the not up-to-original-degree canonical name for the input 
        //sequence based on the "second" center, if one exists. Returns "zilch"
        //if it doesn't. 
        int[] centers = findCenter(taken);
        if (centers.length != 2) {return "zilch";}
        String treeRep2 = makeDescript(taken, centers[1], -1);
        return treeRep2; //basic version
    }
    
    public static ArrayList<ArrayList<ArrayList<Integer>>> fastRemove(
            ArrayList<ArrayList<ArrayList<Integer>>> givens, 
            boolean strictVersion, int[] primaSeq) {
        //a function to remove all isomorphisms from a collection of tree 
        //realizations of a given degree sequence. Named because it runs 
        //much more quickly than our original function for this purpose. 
        //Inputs: an arrayList containing trees, themselves represented as 
        //arrayLists, and a boolean determining whether we want removal
        //up-to-original-degree or removal in general ([false] for general) 
        //and the original sequence (which is only used if [strictVersion] is 
        //true, but we require it regardless)
        //Outputs: a similar arraylist containing trees, but with all 
        //isomorphisms removed, under whatever paradigm was selected by 
        //[strictVersion]
        ArrayList<ArrayList<ArrayList<Integer>>> emptyStart = 
                new ArrayList<ArrayList<ArrayList<Integer>>> (); 
        String[] allUniques = new String[10];
        String[] uniqueDoubles = new String[10];
        int uniqueCount = 0; 
        int realizeCount = 0; 
        int emptyCount; 
        boolean isAnIsomorphism; 
        ArrayList<ArrayList<Integer>> currentRealization; 
        String currentRep;
        String currentRep2; 
        ArrayList<ArrayList<Integer>> toCheck;
        String stringCheck;
        String stringCheck2;
        while (realizeCount < givens.size()) {
            //for every tree in the input arrayList
            isAnIsomorphism = false; 
            emptyCount = 0; 
            currentRealization = givens.get(realizeCount);
            if (strictVersion) {
                currentRep = makeARep(currentRealization, primaSeq);
                currentRep2 = makeOtherRep(currentRealization, primaSeq);
            }
            else {
                currentRep = makeBasicRep(currentRealization, primaSeq);
                currentRep2 = makeOtherBasicRep(currentRealization, primaSeq);
            }
            stringCheck = "Q";
            if (realizeCount == 0) {emptyCount = allUniques.length;}
            while (emptyCount < uniqueCount && (!isAnIsomorphism)) {
                //for every tree we're planning to return as non-isomorphic
                stringCheck = allUniques[emptyCount]; 
                stringCheck2 = uniqueDoubles[emptyCount];
                if (stringCheck.equals(currentRep) || 
                        stringCheck2.equals(currentRep) ||
                        stringCheck.equals(currentRep2)) {
                    //if the current tree is isomorphic to one we're already
                    //planning to return, under the conditions requested: 
                    //flag it as such
                    isAnIsomorphism = true; 
                }
                emptyCount++;
            }
            if (!isAnIsomorphism) {
                //if the tree isn't isomorphic to anything we've seen so far: 
                emptyStart.add(currentRealization);
                allUniques[uniqueCount] = currentRep;
                uniqueDoubles[uniqueCount] = currentRep2;
                uniqueCount++;
                if (allUniques.length - 1 == uniqueCount) {
                    allUniques = stringDouble(allUniques);
                    uniqueDoubles = stringDouble(uniqueDoubles);
                }
            }
            realizeCount++;
        }
        return emptyStart;
    }
    
    public static ArrayList<ArrayList<ArrayList<Integer>>> treeTwo(
            ArrayList<ArrayList<Integer>> inputs, 
            int[] degreeSeq, int[] origSeq) {
        //the function for Algorithm A (and the framework for B). 
        //Inputs: the degree sequence and an empty tree holder
        //and the original sequence, not modified between calls 
        //Outputs: the list of all non-isomorphic trees matching the 
        //input degree sequence 
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
        ArrayList<ArrayList<Integer>> baseClone;
        if (counter == (degreeSeq.length - 2)) {
            //if almost the entire degree sequence is zeroes 
            //and the remainder is a trivial base case which we can hardcode 
            baseClone = new ArrayList<ArrayList<Integer>>(); 
            cloneMake(inputs, baseClone);
            if (degreeSeq[counter] != 1) {System.out.println("Error");}
            if (degreeSeq[counter + 1] != 1) {System.out.println("Error");}
            baseClone.get(counter).add(counter+1);
            baseClone.get(counter+1).add(counter);
            sendback.add(baseClone);
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
                resultCount = 0;
                while (resultCount < results.size()) {
                //for all the ways of making trees out of what's left:
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
            }
                countup++;
        }
        //sendback = fastRemove(sendback, true, origSeq);
        //Include the above line to invoke our implementation of Algorithm B. 
        //Without it, this will run as Algorithm A. The implementation of B 
        //is still error-prone; any attempts at bug-fixing are encouraged. 
        return sendback;
    }
    
    public static ArrayList<ArrayList<ArrayList<Integer>>> searchTheTree(
            int[] orderedSeq, treeOfTrees toFind, int curPlace) {
        //the linear-time retrieval algorithm for class treeOfTrees, 
        //to return the realizations corresponding to the desired degree 
        //sequence. We decided to have this function be separate from class
        //treeOfTrees. 
        //Inputs: the sequence to search for, and the treeOfTrees to search, 
        //and a placeholder value (initially 0, eventually the length of 
        //[orderedSeq] - 1) 
        //Outputs: the realizations corresponding to [orderedSeq], or 
        //a [null] pointer if [orderedSeq] hasn't been properly inserted into 
        //[toFind]. (This will never happen in the contexts we've created.) 
        
        if (toFind.listing != null) {return toFind.listing;}
        //if we're already at the leaf
        //[offspring] is of the type ArrayList<treeOfTrees> 
        int offCount = 0; 
        int offMany = toFind.offspring.size(); 
        int toMatch = orderedSeq[curPlace];
        while (offCount < offMany) {
            if (toMatch == toFind.offspring.get(offCount).childID) {
                return searchTheTree(orderedSeq, 
                        toFind.offspring.get(offCount), curPlace - 1);
            }
            offCount++;
        }
        System.out.println("Child not found error"); //should never get here
        System.out.println("811");
        return toFind.listing;
    }
    
    public static void switchTheTree(
            ArrayList<ArrayList<ArrayList<Integer>>> toSwitch, 
            int firstSwitch, int secondSwitch) {
        //a function for interchanging two elements in a tree realization
        //Inputs: the realization to be switched, and the positions which 
        //should be switched 
        int allCount = 0; 
        int allCap = toSwitch.size(); 
        int firstCap; 
        int firstCount;
        int secondCap; 
        int secondCount;
        ArrayList<Integer> valsHold; 
        while (allCount < allCap) {
            valsHold = new ArrayList<Integer>(); 
            firstCap = toSwitch.get(allCount).get(firstSwitch).size(); 
            firstCount = 0; 
            while (firstCount < firstCap) {
                valsHold.add(toSwitch.get(allCount).
                        get(firstSwitch).get(firstCount));
                firstCount++;
            }//essentially copying the elements of the first list into valHold
            toSwitch.get(allCount).get(firstSwitch).clear();
            secondCount = 0; //that line is fine because the vals are saved 
            secondCap = toSwitch.get(allCount).get(secondSwitch).size();
            while (secondCount < secondCap) {
                toSwitch.get(allCount).get(firstSwitch).add(
                      toSwitch.get(allCount).get(secondSwitch).get(secondCount)
                );
                secondCount++;
            }//move all the second list's elements into list one 
            toSwitch.get(allCount).get(secondSwitch).clear();
            firstCount = 0; 
            while (firstCount < firstCap) {//move all the hold vals into 
                toSwitch.get(allCount).get(secondSwitch). //the second list
                        add(valsHold.get(firstCount));
                firstCount++;
            }
            allCount++;
        }
        
        //the next part switches the references. In other words, if we're 
        //switching vertices 3 and 7, and some other vertex was neighbors 
        //only with vertex 3, it will henceforth be neighbors only with 
        //vertex 7. 
        allCount = 0; 
        int middleCount; 
        int middleCap; 
        int innerCount; 
        int innerCap; 
        while (allCount < allCap) {
            middleCount = 0; 
            middleCap = toSwitch.get(allCount).size(); 
            while (middleCount < middleCap) {
                innerCount = 0; 
                innerCap = toSwitch.get(allCount).get(middleCount).size(); 
                while (innerCount < innerCap) {
                    if (toSwitch.get(allCount).get(middleCount).get(innerCount) 
                            == firstSwitch) {
                        toSwitch.get(allCount).get(middleCount).
                                remove(innerCount); 
                        toSwitch.get(allCount).get(middleCount).add(innerCount, 
                                secondSwitch); 
                    }
                    else if (toSwitch.get(allCount).get(middleCount).
                            get(innerCount) == secondSwitch) {
                        toSwitch.get(allCount).get(middleCount).
                                remove(innerCount); 
                        toSwitch.get(allCount).get(middleCount).add(innerCount, 
                                firstSwitch); 
                    }
                    
                    innerCount++;
                }
                middleCount++;
            }
            allCount++;
        }
    }
    
    public static ArrayList<ArrayList<ArrayList<Integer>>> addZero(
        ArrayList<ArrayList<ArrayList<Integer>>> toBeAdded) {
        //adds a disconnected "zero" vertex to the given realization. 
        //Inputs: the realization to be added to 
        //Outputs: the added realization. 
        int highCount = 0; 
        int highCap = toBeAdded.size(); 
        ArrayList<Integer> neighborClone;  
        int endingSize; 
        int origSize; 
        //int removeSize; 
        while (highCount < highCap) {
            endingSize = toBeAdded.get(highCount).size(); 
            origSize = endingSize; 
            while (endingSize > 0) {
                neighborClone = new ArrayList<Integer>(); 
                plusClone(toBeAdded.get(highCount).get(endingSize - 1), 
                        neighborClone);
                if (endingSize < origSize) {
                    toBeAdded.get(highCount).remove(endingSize); 
                }
                toBeAdded.get(highCount).add(endingSize, neighborClone); 
                endingSize--;
            }
            neighborClone = new ArrayList<Integer>(); 
            toBeAdded.get(highCount).remove(0);
            toBeAdded.get(highCount).add(0, neighborClone);
            endingSize = toBeAdded.get(highCount).size();
            origSize++;
            highCount++;
        }
        return toBeAdded;
    }
    
    public static ArrayList<ArrayList<ArrayList<Integer>>> treeLookUp(
            int[] unorderedSeq, treeOfTrees toSearch) {
        //a helper function for [searchTheTree]. Formats the sequence
        //so that it matches one already computed and stored, then calls 
        //[searchTheTree]. 
        //Inputs: the original sequence (which will, at most, have two 
        //elements out of order, and will include a zero), along with the 
        //treeOfTrees to search. 
        //Outputs: the list of realizations corresponding to [unorderedSeq], 
        //found in [toSearch]. 
        int countUp = 1; 
        int firstPosit = -1; //default values because java complains otherwise
        int secondPosit = -1; //they'll be initialized if/when they're needed 
        if (unorderedSeq[0] != 0) {System.out.println("Error 918");}
        boolean bothFound = false;
        while (countUp < unorderedSeq.length && !bothFound) {
            if (unorderedSeq[countUp] < unorderedSeq[countUp - 1]) {
                secondPosit = countUp; 
                while (!bothFound) {
                    countUp--; 
                    if (countUp == 0) {System.out.println("Error 925");}
                    if (unorderedSeq[countUp] <= unorderedSeq[secondPosit]) {
                        firstPosit = countUp; 
                        bothFound = true;
                    }
                }
            }
            countUp++;
        }
        //by this point, either the array was in nondecreasing order the whole 
        //time, or else we've identified the one place [secondPosit] where it's 
        //not, and the place [firstPosit] where it would need to go so that it 
        //would be in order. The boolean value [bothFound] will be [true] in 
        //that second case, and [false] in the first. 
        if (bothFound) {
            //if the array really was one element out of order
            swap(unorderedSeq, firstPosit + 1, secondPosit); //fix it 
        }
        //by now, [unorderedSeq] will, in theory, be the same as some sequence
        //we've already computed, minus the leading zero, and whose results 
        //we've already stored in [toSearch], minus the leading zero. 
        ArrayList<ArrayList<ArrayList<Integer>>> foundHold = new 
                ArrayList<ArrayList<ArrayList<Integer>>>();
        
        tripleClone(searchTheTree(unorderedSeq, toSearch, 
                unorderedSeq.length - 1), foundHold);
        //by now, [foundHold] is functionally equal to whatever 
        //[searchTheTree] gave us, but can be altered independently. 
        foundHold = addZero(foundHold); 
        if (bothFound) {
            switchTheTree(foundHold, firstPosit + 1, secondPosit); 
        }
        return foundHold; 
    }
    
    public static int[] howManyOfSize(ArrayList<ArrayList<Integer>> toDetermine, 
            int targetSize, int doNotBond) {
        //a function specifically for Algorithm C, to determine how many 
        //vertices there exist in a given realization that match a certain  
        //value, and to return their position indices 
        //Inputs: the realization to search, the value to match, and an [int] 
        //representing a value not to select even if it does match 
        int detCap = toDetermine.size(); 
        int detCount = 0; 
        int totals = 0; 
        int negCount; 
        int[] totalSend = new int[2];
        totalSend[0] = -1; 
        totalSend[1] = -1;
        while (detCount < detCap) {
            if (toDetermine.get(detCount).size() == targetSize && detCount != doNotBond) {
                totalSend[totals] = detCount;
                totals++;
                if (totals == totalSend.length) {
                    negCount = totals; 
                    totalSend = arrayDouble(totalSend); 
                    while (negCount < totalSend.length) {
                        totalSend[negCount] = -1;
                        negCount++;
                    }
                }
            }
            detCount++;
        }
        return totalSend; 
    }
    
    public static ArrayList<ArrayList<ArrayList<Integer>>> treeThree(
            ArrayList<ArrayList<Integer>> inputs, 
            int[] degreeSeq, int[] origSeq, treeOfTrees reference) {
        //Our (correct) implementation of Algorithm C. 
        //Takes a degree sequence and returns its realizations. 
        //inputs: the degree sequence and an empty tree holder
        //and the original sequence, not modified between calls
        //not to mention a tree-of-trees so that some previously computed 
        //information can be saved
        //outputs: the list of all non-isomorphic trees matching the 
        //input degree sequence 
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
        ArrayList<ArrayList<Integer>> baseClone;
        if (counter == (degreeSeq.length - 2)) {
            //if almost the entire degree sequence is zeroes 
            //and the remainder is a trivial base case which we can hardcode 
            baseClone = new ArrayList<ArrayList<Integer>>(); 
            cloneMake(inputs, baseClone);
            if (degreeSeq[counter] != 1) {System.out.println("Error");
                System.out.println("971");
            }
            if (degreeSeq[counter + 1] != 1) {System.out.println("Error");
                System.out.println("974");
            }
            baseClone.get(counter).add(counter+1);
            baseClone.get(counter+1).add(counter);
            sendback.add(baseClone);
            return sendback; 
        }
        ArrayList<ArrayList<Integer>> resultClone = 
                new ArrayList<ArrayList<Integer>>();
        int resultCount; 
        countup = counter + 1;
        int firstSwap = -1; 
        int secondSwap = -1; 
        int targetDegree; 
        boolean swapped; 
        int[] manyTargets; 
        int targetCount;
        int curTarget;
        boolean fini;
        while (countup < degreeSeq.length) {
            if (isValidPair(degreeSeq, counter, countup)){
            //if we've found a valid link to make 
                outClone = degreeSeq.clone();
                outClone[counter] = outClone[counter] - 1; 
                outClone[countup] = outClone[countup] - 1;
                //form the link in our degree sequence
                targetDegree = outClone[countup]; //and keep track of the 
                //newly reduced degree of the node we just bonded to 
                ArrayList<ArrayList<ArrayList<Integer>>> results = 
                        //treeThree(inputs, outClone, origSeq);
                        treeLookUp(outClone, reference); 
                //and get back all the ways of making trees out of what's left
                resultCount = 0;
                while (resultCount < results.size()) {
                //for all the ways of making trees out of what's left:
                    resultClone = new ArrayList<ArrayList<Integer>>();
                    cloneMake(results.get(resultCount), resultClone);
                    manyTargets = howManyOfSize(resultClone, targetDegree, 
                            counter);
                        targetCount = 0;
                        fini = false; 
                        while (targetCount < manyTargets.length & !fini) {
                            resultClone = new ArrayList<ArrayList<Integer>>(); 
                            cloneMake(results.get(resultCount), resultClone); 
                            curTarget = manyTargets[targetCount]; 
                            if(curTarget==-1) {fini = true;}
                            if(!fini) {
                                resultClone.get(curTarget).add(counter);
                                resultClone.get(counter).add(curTarget);
                                sendback.add(resultClone);
                                targetCount++;
                            }
                        }
                    resultCount++;
                }
            }
                countup++;
        }
        return sendback;
    }
    
    public static ArrayList<ArrayList<Integer>> seqMaker(
            int nodeCount, int totalDegrees, int prevCap) {
        //generates all tree-able degree sequences with 
        //as many nodes as nodeCount
        //totalDegrees should originally be set to 2 * (nodeCount - 1)
        //and prevCap should originally be set to nodeCount, though it later
        //varies independently of nodeCount
        //nodeCount <= totalDegrees < 2(nodeCount - 1) over all invocations
        //also all trees have at least two leaves 
        ArrayList<ArrayList<Integer>> conseguir = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> container = new ArrayList<Integer>();
        if (nodeCount == totalDegrees) {
            //if this invocation of seqMaker has reached a point 
            //where the remainder of the sequence can only be filled 
            //with leaves (i.e. if we have have exactly as many degrees 
            //remaining as we have places to put them)
            int degreeCount = 0; 
            while (degreeCount < totalDegrees) {
                container.add(1);
                degreeCount++;
            }
            conseguir.add(container); //only one way of filling in the rest, 
            return conseguir; //so only one sub-realization is returned
        }
        //otherwise: we have options 
        int capClone = prevCap;
        ArrayList<ArrayList<Integer>> holdStuff; //this will hold our options
        ArrayList<Integer> holdNest; //this will hold a single option
        //capClone is essentially a way of conveying to the subcalls what's 
        //already been tried. We don't want any subcall trying any degrees 
        //larger than whatever value of [capClone] we pass to it
        while (capClone > 1) { 
            if (!((totalDegrees - capClone) < (nodeCount - 1))) {
            //basically: in the recursive calls, there's going to be exactly 
            //[nodeCount - 1]-many slots to be filled, so we need at least 
            //[nodecount - 1]-many degrees left to fill them. The recursive 
            //calls must not try to include a single degree which is "too big",
            //and we choose capClone accordingly to prevent it. 
                holdStuff = seqMaker((nodeCount - 1), (totalDegrees - 
                        capClone), capClone);
                //get the recursive result in [holdStuff]
                int holdSize = holdStuff.size();
                int holdCount = 0; 
                while (holdCount < holdSize) {
                    holdNest = new ArrayList<Integer>();
                    singleClone(holdStuff.get(holdCount), holdNest);
                    //[holdStuff] will consist of a list of possibilities
                    //and we use [holdNest] to create independent clones 
                    //for each of them 
                    holdNest.add(capClone);//and, on to the end of each, we add
                    //the single degree which we selected in the context of 
                    //this call. Then we add [holdNest] to the return group
                    conseguir.add(holdNest); 
                    holdCount++;
                }
            }
            capClone--;
        }
        return conseguir;
    }
    
    public static int[] converter(ArrayList<Integer> inputs) {
        //converts an arrayList<Integer> into an int[]
        int totalCount = inputs.size();
        int[] converts = new int[totalCount]; 
        int descender = totalCount - 1; 
        while (descender >= 0) {
            converts[descender] = inputs.get(descender);
            descender--;
        }
        return converts;
    }
    
    public static void arrayClone(int[] model, int[] blank) {
        //creates an int[] clone of the int[] model which can be altered 
        //independently of the original
        int capVal = model.length;
        int curVal = 0; 
        while (curVal < capVal) {
            blank[curVal] = model[curVal];
            curVal++;
        }
    }
    
    public static int[] arrayDouble(int[] base) {
        //returns an array with the same values as input [base]
        //but twice as long 
        //with all the remaining inputs being set to -1 initially
        int baseSize = base.length;
        int[] doubled = new int[baseSize * 2];
        int baseCount = 0; 
        while (baseCount < baseSize) {
            doubled[baseCount] = base[baseCount]; 
            baseCount++;
        }
        baseSize = baseSize * 2; 
        while (baseCount < baseSize) {
            doubled[baseCount] = 0; 
            baseCount++;
        }
        return doubled;
    }
    
    public static String[] stringDouble(String[] base) {
        //returns an array with the same values as input [base]
        //but twice as long 
        //with all the remaining inputs being set to "" initially
        int baseSize = base.length;
        String[] doubled = new String[baseSize * 2];
        int baseCount = 0; 
        while (baseCount < baseSize) {
            doubled[baseCount] = base[baseCount]; 
            baseCount++;
        }
        baseSize = baseSize * 2; 
        while (baseCount < baseSize) {
            doubled[baseCount] = ""; 
            baseCount++;
        }
        return doubled;
    }
    
    public static int tryAllSeqsOfLength(int size) {
        //does what it says on the tin 
        //and prints the sequence with size [size] with the most non-isomorphic
        //representations, and returns the exact count of those representations
        ArrayList<ArrayList<Integer>> allSeqs = (
                seqMaker(size, 2*(size - 1), size)
                );
        //generates all possible tree sequences of length [size]
        int howManySeqs = allSeqs.size(); 
        int bestWeveSeen = -1;
        int seqCount = 0; 
        
        int[] realizeDistrib = {0, 0}; 
        int[] realizeClone; 
        //this will come to contain all the realization counts 
        //such that realizeDistrib[n] will be the number of degree sequences 
        //with n-many realizations, and so forth
        
        ArrayList<ArrayList<Integer>> blankList; 
        ArrayList<Integer> seqList = new ArrayList<Integer>();
        ArrayList<Integer> seqClone;
        ArrayList<ArrayList<ArrayList<Integer>>> holdVals;
        //startup values
        int blankCount;
        int[] sendSeq;
        int[][] printSeq = new int[size][10];
        int negCount; 
        int howMany;
        while (seqCount < howManySeqs) {
            //once for every possible sequence of length [size]
            blankList = new ArrayList<ArrayList<Integer>>();
            //reset the empty two-dimensional arrayList parameter
            //now to initialize the empty two-dimensional arrayList
            //containing a precise amount of empty one-dimensional arrayLists
            blankCount = 0;
            while (blankCount < size) {
                seqClone = new ArrayList(seqList); 
                //have to repeatedly clone seqList due to some Java quirk 
                blankList.add(seqClone);
                blankCount++;
            }
            holdVals = new ArrayList<ArrayList<ArrayList<Integer>>>();
            //reset the tree-representation holder every time
            sendSeq = converter(allSeqs.get(seqCount));
            //and convert the tree sequence of the day into a workable form
            //and away we go
            int[] sendClone = new int[sendSeq.length];
            arrayClone(sendSeq, sendClone);
            holdVals = treeTwo(blankList, sendSeq, sendClone);
            holdVals = fastRemove(holdVals, false, sendSeq); 
            howMany = holdVals.size(); 
            
            if (howMany > bestWeveSeen) {
                //if this tree has more non-isomorphic representations than 
                //the others 
                bestWeveSeen = howMany;
                printSeq = new int[size][10];
                negCount = 0; 
                while (negCount < 10) {
                    printSeq[0][negCount] = -1;
                    negCount++;
                }
                negCount = 0; 
                while (negCount < size) {
                    printSeq[negCount][0] = sendSeq[negCount]; 
                    negCount++;
                }
            }
            else if (howMany == bestWeveSeen) {
                negCount = 0; 
                while (printSeq[0][negCount] != -1) {negCount++;}
                blankCount = negCount; 
                negCount = 0;
                while (negCount < size) {
                    printSeq[negCount][blankCount] = sendSeq[negCount]; 
                    negCount++;
                }
            }
            
            while (howMany >= realizeDistrib.length) {
                //if we're trying to make an entry for the first sequence 
                //with, for example, five realizations, 
                //and we only have room for sequences up to four at the moment, 
                //then double the size of the preexisting array 
                realizeClone = arrayDouble(realizeDistrib);
                realizeDistrib = new int[realizeClone.length];
                arrayClone(realizeClone, realizeDistrib);
            }
            realizeDistrib[howMany] = realizeDistrib[howMany] + 1;
            //increase the number of n-realization sequences by 1 
            
            seqCount++;
        }
        System.out.println("For all sequences of length "+size+":");
        if (printSeq[0][1] == -1) {
        System.out.println("The sequence with the most representations is");
        System.out.println(dualPrint(printSeq, 0, size));
        }
        else {
        System.out.println("The sequences with the most representations are");
        negCount = 0; 
        while (printSeq[0][negCount] != -1) {
            System.out.println(dualPrint(printSeq, negCount, size));
            negCount++;
        }
        }
        System.out.println("With "+bestWeveSeen+" representation(s).");
        System.out.println("Additionally, the distribution is as follows: ");
        System.out.println(arrayPrint(realizeDistrib));
        
        return bestWeveSeen;
    }
    
        public static treeOfTrees tryAllSeqsOfLengthWithTrees(int size, 
                treeOfTrees infoTree) {
        //does what it says on the tin 
        //and prints the sequence with size [size] with the most non-isomorphic
        //representations, and returns the exact count of those representations
        ArrayList<ArrayList<Integer>> allSeqs = (
                seqMaker(size, 2*(size - 1), size)
                );
        //generates all possible tree sequences of length [size]
        int howManySeqs = allSeqs.size(); 
        int bestWeveSeen = -1;
        int seqCount = 0; 
        
        int[] realizeDistrib = {0, 0}; 
        int[] realizeClone; 
        //this will come to contain all the realization counts 
        //such that realizeDistrib[n] will be the number of degree sequences 
        //with n-many realizations, and so forth
        
        ArrayList<ArrayList<Integer>> blankList; 
        ArrayList<Integer> seqList = new ArrayList<Integer>();
        ArrayList<Integer> seqClone;
        ArrayList<ArrayList<ArrayList<Integer>>> holdVals;
        //startup values
        int blankCount;
        int[] sendSeq;
        int[][] printSeq = new int[size][10];
        int negCount; 
        int howMany;
        treeOfTrees infoNova = new treeOfTrees(-1);
        while (seqCount < howManySeqs) {
            //once for every possible sequence of length [size]
            blankList = new ArrayList<ArrayList<Integer>>();
            //reset the empty two-dimensional arrayList parameter
            //now to initialize the empty two-dimensional arrayList
            //containing a precise amount of empty one-dimensional arrayLists
            blankCount = 0;
            while (blankCount < size) {
                seqClone = new ArrayList(seqList); 
                //have to repeatedly clone seqList due to some Java quirk 
                blankList.add(seqClone);
                blankCount++;
            }
            holdVals = new ArrayList<ArrayList<ArrayList<Integer>>>();
            //reset the tree-representation holder every time
            sendSeq = converter(allSeqs.get(seqCount));
            //and convert the tree sequence of the day into a workable form
            //and away we go
            int[] sendClone = new int[sendSeq.length];
            arrayClone(sendSeq, sendClone);
            holdVals = treeThree(blankList, sendSeq, sendClone, infoTree);
            
            sendClone = new int[sendSeq.length]; 
            arrayClone(sendSeq, sendClone);
            ArrayList<ArrayList<ArrayList<Integer>>> howClone; 
            howClone = new ArrayList<ArrayList<ArrayList<Integer>>>();  
            holdVals = fastRemove(holdVals, false, sendSeq); 
            tripleClone(holdVals, howClone); 
            infoNova.bindNewSequence(sendClone, sendClone.length - 1, howClone);
            howMany = holdVals.size(); 
            if (howMany > bestWeveSeen) {
                //if this tree has more non-isomorphic representations than 
                //the others 
                bestWeveSeen = howMany;
                //arrayClone(sendSeq, printClone);
                printSeq = new int[size][10];
                negCount = 0; 
                while (negCount < 10) {
                    printSeq[0][negCount] = -1;
                    negCount++;
                }
                negCount = 0; 
                while (negCount < size) {
                    printSeq[negCount][0] = sendSeq[negCount]; 
                    negCount++;
                }
            }
            else if (howMany == bestWeveSeen) {
                negCount = 0; 
                while (printSeq[0][negCount] != -1) {negCount++;}
                blankCount = negCount; 
                negCount = 0;
                while (negCount < size) {
                    printSeq[negCount][blankCount] = sendSeq[negCount]; 
                    negCount++;
                }
            }
            
            while (howMany >= realizeDistrib.length) {
                //if we're trying to make an entry for the first sequence 
                //with, for example, five realizations, 
                //and we only have room for sequences up to four at the moment, 
                //then double the size of the preexisting array 
                realizeClone = arrayDouble(realizeDistrib);
                realizeDistrib = new int[realizeClone.length];
                arrayClone(realizeClone, realizeDistrib);
            }
            realizeDistrib[howMany] = realizeDistrib[howMany] + 1;
            //increase the number of n-realization sequences by 1 
            
            seqCount++;
        }
        System.out.println("For all sequences of length "+size+":");
        if (printSeq[0][1] == -1) {
        System.out.println("The sequence with the most representations is");
        System.out.println(dualPrint(printSeq, 0, size));
        }
        else {
        System.out.println("The sequences with the most representations are");
        negCount = 0; 
        while (printSeq[0][negCount] != -1) {
            System.out.println(dualPrint(printSeq, negCount, size));
            negCount++;
        }
        }
        System.out.println("With "+bestWeveSeen+" representation(s).");
        System.out.println("Additionally, the distribution is as follows: ");
        System.out.println(arrayPrint(realizeDistrib));
        
        return infoNova;
    }
    
    public static void tryAllLengthsUpTo(int cap) {
        Instant startMoment = Instant.now();
        Instant timeSince; 
        int upCount = 2; 
        int resultHold; 
        while (upCount < cap) {
            //or whatever number
            resultHold = tryAllSeqsOfLength(upCount);
            timeSince = Instant.now(); 
            Duration since = Duration.between(startMoment, timeSince);
            System.out.println("Total time elapsed: "+since.toMillis()+
                    " milliseconds.");
            upCount++;
        }
    }
    
    public static void tryAllLengthsUpToWithTrees(int cap) {
        Instant startMoment = Instant.now();
        Instant timeSince; 
        int upCount = 2; 
        //int resultHold; 
        treeOfTrees basicTree = new treeOfTrees(-1);
        treeOfTrees newTree;
        while (upCount < cap) {
            //or whatever number
            newTree = tryAllSeqsOfLengthWithTrees(upCount, basicTree);
            timeSince = Instant.now(); 
            Duration since = Duration.between(startMoment, timeSince);
            System.out.println("Total time elapsed: "+since.toMillis()+
                    " milliseconds.");
            upCount++;
            basicTree = newTree;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //INSTRUCTIONS FOR USE: de-comment whichever of the lines below 
        //corresponds to the version of the algorithm you want to use, 
        //then alter the argument so that it matches the sequence length 
        //you want to compute up to. 
        //The output will tell you, for each length, what the sequence is 
        //with the most realizations, and how many realizations that is. 
        //It will also give you a distribution of the results. 
        //(A 5 in the third slot means that there exist 5 sequences with 
        //three realizations, and so on. The zeroth slot is always 0.)
        
        
        //tryAllLengthsUpTo(40); //Algorithm A (brute force)
        //tryAllLengthsUpToWithTrees(20); //Algorithm C (dynamic programming)
    }
}