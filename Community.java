package hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Extracting data for and modeling the community about which we would like to learn more information. 
 * 
 * @author Tahsin Nawar Akanda
 * @version 07 May 2017, 10:12pm
 *
 */
public class Community {

	//Try to represent friends of a person on a tree
	//Hold Member in a node within the tree
	//Can have a tree of trees->Community tree organized by SSN of Members
	//For each member, create a tree of friends, also organized by SSN
	public static void main(String[] args) throws IOException {
		
		String communityFileInput = args[0];
		String queriesFileInput = args[1];

		ArrayList<String> communityData = extractCommunityData(communityFileInput);
		ArrayList<String> queriesData = extractQueriesInput(queriesFileInput);
		ArrayList<Member> listOfMembers = new ArrayList<>();
		listOfMembers = createMembersFromFile(communityData);
		
		//Take Two
		NewTree<Member> communityBST2 = new NewTree<Member>();
		
		NewTree<Member> BSTofCommunity2 = createNewTree(communityBST2, listOfMembers);
		
		//System.out.println("Using NewTree: " + BSTofCommunity2.root.data.firstName);
		//System.out.println(("Using NewTree: " +BSTofCommunity2.root.leftChild.data.firstName));
		
		//Now that we have created the tree, we need to answer the queries read in through queriesfile.txt
		//Read and process queries currently in array list
		ArrayList<String> outputs = answerQueries(queriesData, BSTofCommunity2, listOfMembers);
		for(String o: outputs){
			System.out.println(o);
		}
		String nameOfFile = "output.txt";
		writeLinesToFile(outputs, nameOfFile);
	}

	/**
	 * Using the array list of responses from answerQueries method to write output to file.
	 * @param array: Array list of output responses to answering the requested queries.
	 * @param filename: Name of the output file.
	 */
	public static void writeLinesToFile(ArrayList<String> array, String filename) { 

        try {
            FileWriter fileWriter = new FileWriter(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            int i = 0;
            for(String line : array){
                bufferedWriter.write(line);
                if(i < array.size()-1){
                    bufferedWriter.newLine();
                }
                i++;
            }
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Oops.");
        }
    }

	/**
	 * Reading in query file to parse requests and match with appropriate methods to provide user 
	 * responses to their queries, as appropriate.
	 * @param inputQueryData: Reference to the query file argument.
	 * @param m: BST that we traverse for community.
	 * @param members: All Member objects for this Community.
	 * @return An array list of type string that is used to output responses.
	 */
	public static ArrayList<String> answerQueries(ArrayList<String> inputQueryData, NewTree<Member> m, ArrayList<Member> members) {
		ArrayList<String> lines = new ArrayList<>();
		for(String q: inputQueryData){
			/*
			
			WHO-HAS-MOST-MUTUAL-FRIENDS
			*/
			//QUERY NO. 1: NAME-OF
			if(q.startsWith("NAME-OF")){
				String[] nameOfArray = q.split(" ");
				//System.out.println(nameOfArray[1]);
				//System.out.println(q+":" + nameOfPerson(1,m));
				int inputSSNid = Integer.valueOf(nameOfArray[1]);
				lines.add(q+":" + nameOfPerson(inputSSNid, m, members));
			}
			//QUERY NO. 2: FATHER-OF
			else if(q.startsWith("FATHER-OF")){
				String [] fatherOfArray = q.split(" ");
				//System.out.println(fatherOfArray[1]);
				int inputSSNid = Integer.valueOf(fatherOfArray[1]);
				lines.add(q+":" + fatherOfPerson(inputSSNid, m, members));
			}
			//QUERY NO. 3: MOTHER-OF
			else if (q.startsWith("MOTHER-OF")){
				String [] motherOfArray = q.split(" ");
				//System.out.println(fatherOfArray[1]);
				int inputSSNid = Integer.valueOf(motherOfArray[1]);
				lines.add(q+":" + motherOfPerson(inputSSNid, m, members));
			}
			//QUERY NO. 5: HALF-SIBLINGS-OF
			else if (q.startsWith("HALF-SIBLINGS-OF")){
				String [] halfSiblingsOfArray = q.split(" ");
				int inputSSNid = Integer.valueOf(halfSiblingsOfArray[1]);
				ArrayList<Member> halfSibs = halfSiblingsOfPerson(inputSSNid, m, members);
				Collections.sort(halfSibs);
				String allHalfies = "";
				//System.out.println("Size of halfSibs array is: " + halfSibs.size());
				int counter = 1;
				for (Member s: halfSibs){
					if(counter != halfSibs.size()){
						allHalfies += (s.firstName + "" + s.lastName + ",");
					}
					else{
						allHalfies += (s.firstName + "" + s.lastName);
						//System.out.println();
					}
					counter++;
					
				}
				
				if(halfSibs.isEmpty()){
					lines.add(q+": " + "UNAVAILABLE");
				}
				else{
					lines.add(q+":" + allHalfies);
				}
				
			}
			//QUERY NO. 6: FULL-SIBLINGS-OF
			else if(q.startsWith("FULL-SIBLINGS-OF")){
				String [] fullSiblingsOfArray = q.split(" ");
				int inputSSNid = Integer.valueOf(fullSiblingsOfArray[1]);
				ArrayList<Member> fullSibs = fullSiblingsOfPerson(inputSSNid, m, members);
				Collections.sort(fullSibs);
				String allFullies = "";
				int counter = 1;
				for (Member s: fullSibs){
					
					if(counter != fullSibs.size()){
						allFullies += (s.firstName + "" + s.lastName + ",");
					}
					else{
						allFullies += (s.firstName + "" + s.lastName);
						//System.out.println();
					}
					counter++;
					
				}
				if (fullSibs.isEmpty()){
					lines.add(q+": " + "UNAVAILABLE");
				}
				else{
					lines.add(q+":" + allFullies);
				}
				
				
			}//endFullSib
			//QUERY NO. 7: CHILDREN-OF
			else if(q.startsWith("CHILDREN-OF")){
				String [] halfSiblingsOfArray = q.split(" ");
				int inputSSNid = Integer.valueOf(halfSiblingsOfArray[1]);
				ArrayList<Member> allKids = childrenOfPerson(inputSSNid, m, members);
				Collections.sort(allKids);
				String allChildren = "";
				//System.out.println("Size of allKids array is: " + halfSibs.size());
				int counter = 1;
				for (Member s: allKids){
					//System.out.println("Testing for loop");
					if(counter != allKids.size()){
						//System.out.println(s.firstName + " " + s.lastName);
						allChildren += (s.firstName + "" + s.lastName + ",");
					}
					else{
						//System.out.println(s.firstName + " " + s.lastName);
						allChildren += (s.firstName + "" + s.lastName);
						//System.out.println();
					}
					counter++;
				}
				if(allChildren.isEmpty()){
					lines.add(q+": " + "UNAVAILABLE");
				}
				else{
					lines.add(q+":" + allChildren);
				}
				
			}//end_Children;
			//QUERY NO. 8: MUTUAL-FRIENDS-OF: DIRECTED, BOTH WAYS
			else if (q.startsWith("MUTUAL-FRIENDS-OF")){
				String [] mutualFriendsOfArray = q.split(" ");
				int inputSSNid = Integer.valueOf(mutualFriendsOfArray[1]);
				ArrayList<Member> mutualFriends = mutualFriendsOfPerson(inputSSNid, m, members);
				Collections.sort(mutualFriends);
				String allMutualFriends = "";
				//System.out.println("Size of allKids array is: " + halfSibs.size());
				int counter = 1;
				for (Member mf: mutualFriends){
					//System.out.println("Testing for loop");
					if(counter != mutualFriends.size()){
						//System.out.println(s.firstName + " " + s.lastName);
						allMutualFriends += (mf.firstName + "" + mf.lastName + ",");
					}
					else{
						//System.out.println(s.firstName + " " + s.lastName);
						allMutualFriends += (mf.firstName + "" + mf.lastName);
						//System.out.println();
					}
					counter++;
				}
				if(allMutualFriends.isEmpty()){
					lines.add(q+": " + "UNAVAILABLE");
				}
				else{
					lines.add(q+":" + allMutualFriends);
				}
				
			}//end_mutual
			//QUERY NO. 9: INVERSE-FRIENDS-OF; ONE DIRECTIONAL EDGE
			else if (q.startsWith("INVERSE-FRIENDS-OF")){
				String [] inverseFriendsOfArray = q.split(" ");
				int inputSSNid = Integer.valueOf(inverseFriendsOfArray[1]);
				ArrayList<Member> inverseFriends = inverseFriendsOfPerson(inputSSNid, m, members);
				Collections.sort(inverseFriends);
				String allInverseFriends = "";
				int counter = 1;
				for (Member inf: inverseFriends){
					//System.out.println("Testing for loop");
					if(counter != inverseFriends.size()){
						//System.out.println(s.firstName + " " + s.lastName);
						allInverseFriends += (inf.firstName + "" + inf.lastName + ",");
					}
					else{
						//System.out.println(s.firstName + " " + s.lastName);
						allInverseFriends += (inf.firstName + "" + inf.lastName);
						//System.out.println();
					}
					counter++;
				}
				if(allInverseFriends.isEmpty()){
					lines.add(q+":" + " UNAVAILABLE");
				}
				else{
					lines.add(q+":" + allInverseFriends);
				}
				
			}//end_InverseFriends
			//QUERY NO. 10: WHO-HAS-MOST MUTUAL FRIENDS
			else if(q.startsWith("WHO-HAS-MOST-MUTUAL-FRIENDS")){
				//ArrayList<Member> totalMutualPerMember = new ArrayList<Member>();
				//sint defaultMax = 0;
				//String defaultMaxPerson = "";
				ArrayList<PersonCount> memberception = new ArrayList<PersonCount>();
//				ArrayList<PersonCount> allMutualFriendsCountPerPerson = new ArrayList<PersonCount>();
				memberception = findAllCountsPerPerson(m, members, memberception);
				//Go through array list of array lists to print out stuff
				//ArrayList<PersonCount> allCounts = new ArrayList<PersonCount>();
				int currentMax =0;
				ArrayList<PersonCount> pc = new ArrayList<PersonCount> ();
				for(PersonCount mem: memberception){
					//System.out.println("The number of mutual friends for "+ mem.name + " is " + mem.numMutualFriends);
					if (mem.numMutualFriends == currentMax){
						pc.add(mem);
					}
					if(mem.numMutualFriends > currentMax){
						currentMax = mem.numMutualFriends;
						pc.clear();
						pc.add(mem);
					}
					
				}
				if (pc.isEmpty()){
					lines.add(q+": UNAVAILABLE");
				}
				else{
					lines.add(q+": " + (pc.get(0)).name);
				}
			}//endelse;
		}
		return lines;	
	}//end_answerQueries;

	

	public static ArrayList<PersonCount> findAllCountsPerPerson(NewTree<Member> tree, ArrayList<Member> allMembers,
			ArrayList<PersonCount> allMutuals) {
		//findAllCountsPerPerson(treeSource.leftChild, allMembers, allMutuals);
		//findAllCountsPerPerson(treeSource.rightChild, allMembers, allMutuals);
		
		//Member currentMember = (Member) tree.root.data;
		//Find mutual friends of person from 1-11
		//int currentMax = 0;
		//ArrayList<Member> prospective = new ArrayList<Member>();
		for(int i=1; i<allMembers.size(); i++){
			ArrayList<Member> currentMutuals = mutualFriendsOfPerson(i, tree, allMembers);
			allMutuals.add(new PersonCount((allMembers.get(i-1).firstName + allMembers.get(i-1).lastName), currentMutuals.size()));
		}
		
		return allMutuals;
	}

	/**
	 * QUERY NO. 8: Helper class to determine friendships in which only one person considers the other a friend.
	 * @param inputSSN: The ID of the person for whom we are finding mutual friends.
	 * @param allMembers: BST of the community that we traverse
	 * @param arrayMem: Backup data in case we need to create a new node for searching.
	 * @return
	 */
	public static ArrayList<Member> inverseFriendsOfPerson(int inputSSN, NewTree<Member> allMembers,
			ArrayList<Member> arrayMem) {
		ArrayList<Member> allInverse = new ArrayList<Member>();
		ArrayList<Member> foundMyInverse = preOrderFindInverse(allMembers, allMembers.root, inputSSN, allInverse);
		return foundMyInverse;
	}

	/**
	 * QUERY NO. 8: Preorder traversal of the community BST to determine inverse members.
	 * @param allMembers: BST to represent community.
	 * @param refNode: Node which enables us to reference the most current traversed part of BST.
	 * @param inputID: SSN for Member of community.
	 * @param allInverse: Array used to hold all Members who are inverse friends for Member with inputID.
	 * @return Array list of all inverse friends.
	 */
	public static ArrayList<Member> preOrderFindInverse(NewTree<Member> allMembers, personNode<Member> refNode,
			int inputID, ArrayList<Member> allInverse) {
		if(refNode != null){
			preOrderFindMutFriends(allMembers, refNode.leftChild, inputID, allInverse);
			
			preOrderFindMutFriends(allMembers, refNode.rightChild, inputID, allInverse);
			
			Member currentMember = refNode.data;
			if(currentMember.friends.contains(inputID)){
				allInverse.add(currentMember);
			}//endif;
		}//end_if;
		return allInverse;
	}

	/**
	 * QUERY NO. 7: Helper class for method to find all mutual friends of a person.
	 * @param inputSSN: The person for whom we are finding all mutual friends.
	 * @param allMembers: BST representing the community.
	 * @param arrayMem: Accessing members in O(1) time in case we need to pass in personNode<Member>
	 * @return
	 */
	public static ArrayList<Member> mutualFriendsOfPerson(int inputSSN, NewTree<Member> allMembers, ArrayList<Member> arrayMem){
		ArrayList<Member> allMutF = new ArrayList<Member>();
		ArrayList<Member> foundMyFriends = preOrderFindMutFriends(allMembers, allMembers.root, inputSSN, allMutF);
		return foundMyFriends;
	}
	
	/**
	 * QUERY NO. 7: Preorder traversal of BST to determine mutual friends; edges connected in both directions.
	 * @param treeOfMembers: BST representation of the community
	 * @param ref: Reference node to traverse through tree
	 * @param inputID: The person for whom we are finding the mutual friends.
	 * @param friends: List containining all Member objects, in case we need to create a node in O(1).
	 * @return
	 */
	public static ArrayList<Member> preOrderFindMutFriends(NewTree<Member> treeOfMembers, personNode<Member> ref,
			int inputID, ArrayList<Member> friends) {
		
		if(ref != null){
			preOrderFindMutFriends(treeOfMembers, ref.leftChild, inputID, friends);
			preOrderFindMutFriends(treeOfMembers, ref.rightChild, inputID, friends);
			
			Member currentMember = ref.data;
			//for(int fNum: currentMember.friends){
				//if(fNum == inputID){
					//friends.add(arrayMemInput.get(fNum-1));
				//}
			//}
			//System.out.println(currentMember.firstName + currentMember.lastName);
			if (currentMember.isYourFriend(inputID)){
				//System.out.println("Found a friend.");
			friends.add(currentMember);	
			}
			
			
		}//end_if;
	
		return friends;
	}//endFindPreOrder;

	/**
	 * QUERY NO. 6: Helper method to identify the children of a given person in the community, if applicable.
	 * @param inputSSN: The ID for which we are finding associated children in BST.
	 * @param allMembers: BST representation of community.
	 * @param arrayMem: Backup structure to assist in creating nodes, if need be.
	 * @return An array list of Member objects that are children of the inputSSN.
	 */
	public static ArrayList<Member> childrenOfPerson(int inputSSN, NewTree<Member> allMembers, ArrayList<Member> arrayMem) {
		//ArrayList<Member> allChildren = new ArrayList<Member>();
		
		//int targetIndex = inputSSN-1;
		//Member targetMember = arrayMem.get(targetIndex);
		ArrayList<Member> lookingForKids = new ArrayList<Member>();
		ArrayList<Member> foundMyKids = preOrderFindChildren(allMembers, allMembers.root, inputSSN, lookingForKids);		
		return foundMyKids;
	}

	/**
	 * Preorder traversal of community BST to find all children of the inputID.
	 * @param allMembersTree: BST representation of community.
	 * @param referenceNode: Node to assist in traversing the tree based on comparisons.
	 * @param inputID: Person for whom we are finding their children.
	 * @param notFullKids: Array list of children found for person so far.
	 * @return Array list of Member objects that are children of the inoutID person.
	 */
	public static ArrayList<Member> preOrderFindChildren(NewTree<Member> allMembersTree, personNode<Member> referenceNode,
			int inputID, ArrayList<Member> notFullKids) {
		
			//ArrayList<Member> allMatchedPersons = new ArrayList<Member>();
			
				if(referenceNode != null){
					preOrderFindChildren(allMembersTree, referenceNode.leftChild, inputID, notFullKids);
					preOrderFindChildren(allMembersTree, referenceNode.rightChild, inputID, notFullKids);
					
					Member currentMember = referenceNode.data;
					if (currentMember.isParent(inputID)){
						//System.out.println("Found a match.");
						notFullKids.add(currentMember);
					}
				}//end_if;
			return notFullKids;
		}
	
	/**
	 * QUERY NO. 5: Determining the full siblings of a person.
	 * @param inputSSN: The person for whom we are finding the full siblings.
	 * @param allMembers: BST of community.
	 * @param arrayMem: List of objects in case we need to create a node to call a method on BST.
	 * @return Array list of all full siblings for inputSSN.
	 */
	public static ArrayList<Member> fullSiblingsOfPerson(int inputSSN, NewTree<Member> allMembers,
			ArrayList<Member> arrayMem) {
		ArrayList<Member> allFullSiblings = new ArrayList<Member>();
		
		int targetIndex = inputSSN-1;
		Member targetMember = arrayMem.get(targetIndex);
		personNode<Member> findMyParents = allMembers.search(allMembers.root, targetMember);
		int mommyIndex = findMyParents.data.mother;
		int daddyIndex = findMyParents.data.father;
		
		//Now we need to go through every person in the community and determine if they are half siblings
		//mother && father
		for (Member m: arrayMem){
			if(m.father == daddyIndex && (m.mother == mommyIndex) && (m.SSN != inputSSN)){
				allFullSiblings.add(m);
			}
			
		}//end_for;
		
		return allFullSiblings;
	}

	/**
	 * QUERY NO. 4: Determining the half siblings of a given person using inputSSN.
	 * @param inputSSN: The person for whom we are finding half siblings in the community.
	 * @param allMembers: BST of community.
	 * @param arrayMem: List of Members, in case we need to create a node to call method on tree.
	 * @return Array list of all half siblings for inputSSN person.
	 */
	public static ArrayList<Member> halfSiblingsOfPerson(int inputSSN, NewTree<Member> allMembers, ArrayList<Member> arrayMem) {
		ArrayList<Member> allHalfSiblings = new ArrayList<Member>();
		
		int targetIndex = inputSSN-1;
		Member targetMember = arrayMem.get(targetIndex);
		personNode<Member> findMyParents = allMembers.search(allMembers.root, targetMember);
		int mommyIndex = findMyParents.data.mother;
		int daddyIndex = findMyParents.data.father;
		
		//Now we need to go through every person in the community and determine if they are half siblings
		//mother ^ father
		for (Member m: arrayMem){
			if(m.father == daddyIndex && (m.mother != mommyIndex) && (m.SSN != inputSSN)){
				allHalfSiblings.add(m);
			}
			else if(m.mother == mommyIndex && (m.father != daddyIndex) && (m.SSN != inputSSN)){
				allHalfSiblings.add(m);
			}
		}//end_for;
		
		return allHalfSiblings;
	}

	/**
	 * QUERY NO. 3: Determining the mother of a given person.
	 * @param inputSSN: The person for whom we are searching for the mother.
	 * @param allMembers: BST of community.
	 * @param arrayMemL List used to reference data for the mother.
	 * @return Name of the mother of inputSSN person.
	 */
	public static String motherOfPerson(int inputSSN, NewTree<Member> allMembers, ArrayList<Member> arrayMem) {
		int targetIndex = inputSSN-1;
		Member targetMember = arrayMem.get(targetIndex);
		personNode<Member> findMyMommy = allMembers.search(allMembers.root, targetMember);
		int mommyIndex = findMyMommy.data.mother;
		int mommyIndexInList = mommyIndex-1;
		return ((arrayMem.get(mommyIndexInList).firstName) + (arrayMem.get(mommyIndexInList).lastName));
	}

	/**
	 * QUERY NO. 2: Determining the father of a given person.
	 * @param inputSSN: The person for whom we are searching for the father.
	 * @param allMembers: BST of community.
	 * @param arrayMemL List used to reference data for the father.
	 * @return Name of the father of inputSSN person.
	 */
	public static String fatherOfPerson(int inputSSN, NewTree<Member> allMembers, ArrayList<Member> arrayMem) {
		int targetIndex = inputSSN-1;
		Member targetMember = arrayMem.get(targetIndex);
		personNode<Member> findMyDaddy = allMembers.search(allMembers.root, targetMember);
		int daddyIndex = findMyDaddy.data.father;
		int daddyIndexInList = daddyIndex-1;
		return ((arrayMem.get(daddyIndexInList).firstName) + (arrayMem.get(daddyIndexInList).lastName));
		//return null;
	}

	/**
	 * QUERY NO. 1: Name of a given person, provided inputSSN.
	 * @param inputSSN: SSN of person for whom we are determining the name.
	 * @param allMembers: BST of community.
	 * @param arrayMem
	 * @return Name of person for whom we input the SSN.
	 */
	public static String nameOfPerson(int inputSSN, NewTree<Member> allMembers, ArrayList<Member> arrayMem) {
		int targetIndex = inputSSN-1;
		//Create a new node to match
		//personNode<Member> memToS = new personNode<Member>(arrayMem.get(targetIndex));
		//Create object to match
		Member memToS = arrayMem.get(targetIndex);
		//pass in search with root
		personNode<Member> requestedPerson = allMembers.search(allMembers.root, memToS);
		//System.out.println("The root: " + allMembers.root.data.firstName);
		//if (requestedPerson == null){
			//System.out.println("oops.");
		//}
		//else{
			//System.out.println("something is in the node");
		//}
		return (requestedPerson.data.firstName + (requestedPerson.data.lastName));//.firstName;
		//return null;
	}

	/**
	 * Using the objects created from the input community file to create the tree that represents 
	 * our community.
	 * @param communityBST2: Empty tree to be populated with tree data
	 * @param listOfMembers: Array list of objects created from the input community file.
	 * @return BST of community
	 */
	public static NewTree<Member> createNewTree(NewTree<Member> communityBST2,
			ArrayList<Member> listOfMembers) {
		int numberInCommunity = listOfMembers.size();
		//System.out.println(numberInCommunity);
		//return null;
		//All members are found by subtracting 1 to find index since index starts at 0
		double rootNumber = Math.ceil(numberInCommunity/2.0);
		int rootIndex = (int)rootNumber -1;
//		System.out.println(rootNumber);
	
		int countNumber = 1;
		
		int outerLeftIndex = 0;
		int outerRightIndex = listOfMembers.size();
		
		int innerLeftIndex= rootIndex-1;
		int innerRightIndex = rootIndex + 1;
		
		boolean outside = false;
		if (countNumber % 2 == 0){//If even, we work from outside
			outside = true;
		}
		else{
			outside = false; //This means we are odd and work from inside +/ 
		}
		
		//The root is the first Member to be processed, which is the middle of the array
		communityBST2.root = new personNode<Member>(listOfMembers.get(rootIndex));
		//Now increment the turn number/counter
		countNumber++;
		
		int numTurns = (listOfMembers.size()/2)+1;
		
		//Need a while loop to make sure that we do not cross the middle of the array
		while(countNumber < numTurns+1){
			//If we are on an even-numbered turn, we work from the outer elements of the array
			if(outside == true){
				communityBST2.insert(listOfMembers.get(outerLeftIndex));
				communityBST2.insert(listOfMembers.get(outerRightIndex));
				outerLeftIndex++;
				outerRightIndex--;
				countNumber++;
			}
			//Need to work from the inside of the array list: middle
			else{
				communityBST2.insert(listOfMembers.get(innerLeftIndex));
				communityBST2.insert(listOfMembers.get(innerRightIndex));
				innerLeftIndex--;
				innerRightIndex++;
				countNumber++;
			}
		}
		
		
			
		return communityBST2;
	}
	
	/**
	 * Creating Member objects from the parsed array list of Strings from the community file.
	 * @param communityData
	 * @return An array list of Member objects for our community.
	 */
	public static ArrayList<Member> createMembersFromFile(ArrayList<String> communityData) {
		ArrayList<Member> members = new ArrayList<Member>();
		
		for (int i=0; i<communityData.size()-1; i+=7){
			//First name
			String[] firstNameArray = communityData.get(i).split(":");
			String inFirst = firstNameArray[1];
			//System.out.println(firstNameArray[1]);
			
			//Last name
			String[] lastNameArray = communityData.get(i+1).split(":");
			String inLast = lastNameArray[1];
			//System.out.println(lastNameArray[1]);
			
			//SSN
			String[] SSNArray = communityData.get(i+2).split(":");
			int inSSN = Integer.valueOf(SSNArray[1].replaceAll("\\s",""));
			//System.out.println(inSSN);
			
			//Father
			String[] fatherArray = communityData.get(i+3).split(":");
			int inFather = Integer.valueOf(fatherArray[1].replaceAll("\\s",""));
			//System.out.println(inFather);
			
			//Mother
			String [] motherArray = communityData.get(i+4).split(":");
			int inMother = Integer.valueOf(motherArray[1].replaceAll("\\s",""));
			//System.out.println(inMother);
			
			//Friends
			String [] friendsArray = communityData.get(i+5).split(":");
			
			//System.out.println(friendsArray[1]);
			String[] fList = friendsArray[1].split(",");
			
			ArrayList<Integer> allFriendsOfPerson = new ArrayList<Integer>();
			for (int j=0; j< fList.length; j++){
				//System.out.println(fList[j].replaceAll("\\s",""));
				allFriendsOfPerson.add(Integer.valueOf(fList[j].replaceAll("\\s","")));
				
			}//endfor;
		//Crate a new member
		Member newMember = new Member(inFirst, inLast, inSSN, inFather, inMother, allFriendsOfPerson);
		members.add(newMember);
		}
		return members;
	}

	/**
	 * Searching for and extracting the requested queries that the user inputs into queriesfile.txt, 
	 * which is within the same package.
	 * @param queriesFileInput
	 * @return 
	 * @throws IOException 
	 */
	public static ArrayList<String> extractQueriesInput(String queriesFileInput) throws IOException {
		//Find absolute path for buffered reader
				String filePath = new File("").getAbsolutePath();
				//System.out.println (filePath);

				//Full pathway to .txt file for customers
				BufferedReader reader = new BufferedReader(new FileReader(filePath + "/src/hw5/"+queriesFileInput));//communityfile.txt"));

				ArrayList<String> queriesArraylist = new ArrayList<>();
				try
				{                           
				    String line = null;         
				    while ((line = reader.readLine()) != null)
				    {
				        if (!(line.startsWith("*")))
				        {
				        	if (line != ""){
				        		//System.out.println(line);
				        		queriesArraylist.add(line);
				        		//Testing - it works
					            //System.out.println(line);
				        	}
				            
				        }
				    }               
				}
				catch (IOException ex)
				{
				    System.out.println("oops.");
					ex.printStackTrace();
				}               

				finally
				{
				    reader.close();
				}          
				
				//query data
				//for (String line: queryArrayList){
					//System.out.println(line);
				//}
				
				return queriesArraylist;
	}

	/**
	 * Searching for and extracting the provided community data provided in communityfile.txt, 
	 * which is within the same package.
	 * @param queriesFileInput
	 * @return A list containing all line-by-line data of the community file.
	 * @throws IOException 
	 */
	public static ArrayList<String> extractCommunityData(String communityFileInput) throws IOException {
		//Find absolute path for buffered reader
		String filePath = new File("").getAbsolutePath();
		//System.out.println (filePath);

		//Full pathway to .txt file for customers
		BufferedReader reader = new BufferedReader(new FileReader(filePath + "/src/hw5/"+communityFileInput));//queriesfile.txt"));

		ArrayList<String> queryArrayList = new ArrayList<>();
		try
		{                           
		    String line = null;         
		    while ((line = reader.readLine()) != null)
		    {
		        if (!(line.startsWith("*")))
		        {
		        	if (line != ""){
		        		//System.out.println(line);
			            queryArrayList.add(line);
			            //Testing - it works
			            //System.out.println(line);
		        	}
		            
		        }
		    }               
		}
		catch (IOException ex)
		{
		    System.out.println("oops.");
			ex.printStackTrace();
		}               

		finally
		{
		    reader.close();
		}          
		
		return queryArrayList;
	}
}
class PersonCount{
	Member person;
	String name;
	ArrayList<Member> allMutualForMember;
	int numMutualFriends;
	public PersonCount(String inputName, int inputMutual){
		this.name = inputName;
		this.numMutualFriends = inputMutual;
	}
}