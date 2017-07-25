package hw5;

import java.util.ArrayList;

/**
 * The template class used for constructing the Member objects to then build our Community [BST].
 * @author Tahsin Nawar Akanda
 * 
 * @version 07 May 2017, 10:21pm
 */
public class Member implements Comparable<Member>{
	
	String firstName;
	String lastName;
	int SSN;
	int father;
	int mother;
	ArrayList<Integer> friends = new ArrayList<Integer>();//Integers of SSNs of all persons with whom THIS 
	
	String fullName = (firstName + " " + lastName);
	
	/**
	 * Constructor for Member object: to be used when reading in individual Member data from input file.
	 * @param inputFirstName: First name of member.
	 * @param inputLastName: Last name of member.
	 * @param inputSSN: SSN/ID of member.
	 * @param inputFather: SSN of member's father.
	 * @param inputMother: SSN of member's mother.
	 * @param inputFriends: SSNs of member's friends in an array form.
	 */
	public Member(String inputFirstName, String inputLastName, int inputSSN, int inputFather, int inputMother, ArrayList<Integer> inputFriends){
		this.firstName = inputFirstName;
		this.lastName = inputLastName;
		this.SSN = inputSSN;
		this.father = inputFather;
		this.mother = inputMother;
		this.friends = inputFriends;
	}
	
	/**
	 * CompareTo override to enable comparisons of Members by their respective SSN IDs.
	 */
	@Override
	public int compareTo(Member m2) {
		if(this.SSN > m2.SSN){
			return 1;
		}
		else if(this.SSN < m2.SSN){
			return -1;
		}
		return 0;
	}

	/**
	 * Determining if the parameterized SSN is the parent of the current Member object on which
	 * it is being invoked.
	 * @param parentInputSSN: Parent in question
	 * @return True if the input SSN is a parent. False, if not.
	 */
	public boolean isParent(int parentInputSSN){
		if(this.mother == parentInputSSN || (this.father == parentInputSSN)){
			return true;
		}
		return false;
	}
	
	/**
	 * Determining if the parameterized SSN is a friend of the current Member.
	 * @param friendToFindSSN: Person to find in this Member's friends list.
	 * @return True if the input person is a friend. False, otherwise.
	 */
	public boolean isYourFriend(int friendToFindSSN){
	
		boolean found  = false;
		if(friends.contains(friendToFindSSN)){
			return found = true;
		}
		return found;
	}	
}