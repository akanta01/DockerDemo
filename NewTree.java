package hw5;

/**
 * The class/template for creating our Binary Search Tree to model the community of members.
 * @author Tahsin Nawar Akanda
 *
 * @param <T>: Generic type
 * @version 07 May 2017, 10:20pm
 */
public class NewTree <T extends Comparable<T>>{


	personNode<T> root;
	
	/**
	 * No-argument constructor for CommunityTree BST
	 */
	public NewTree(){
		root = null;
	}
	
	/**
	 * Helper method for insertion. 
	 * @param dataToInsert: The value to be inserted into a Node, then Node into this BST.
	 */
	public void insert(T dataToInsert){
		root = insert(root, dataToInsert);
	}
	
	/**
	 * Method to add in a node
	 * @param insertedNode: Template/Host for Node data and operations.
	 * @param insertedData: Data field for populating the Node.
	 * @return A node that has been inserted into this BST.
	 */
	public personNode<T> insert(personNode<T> insertedNode, T insertedData){
		if(insertedNode == null){
			return new personNode<T>(insertedData);
		}
		int comparisonValue = ((insertedNode.data).compareTo((T) insertedData));
	
		//Current node is less than passed in data, go right
		if (comparisonValue < 0){
			insertedNode.rightChild = insert(insertedNode.rightChild, insertedData);
		}
		
		//Current node data is greater or equal to than passed in data, go left
		else if (comparisonValue >= 0){
			insertedNode.leftChild = insert(insertedNode.leftChild, insertedData);
		}
		return insertedNode;
		
	}
	
	/**
	 * Search method that enables us to access the individual that we have sought in the Community class.
	 * @param referenceNode: The node that enables us to travserse the tree
	 * @param memToS: The data that we use to identify the member to be searched for.
	 * @return The found Member. Null, otherwise.
	 */
	public personNode<T> search (personNode<T> referenceNode, T memToS){
		if(referenceNode == null){
			return null;
		}
		int comparisonValue = (referenceNode.data).compareTo(memToS);
		//Current node is bigger than the node we are looking for, so we need to go left
		if(comparisonValue > 0){
			return search(referenceNode.leftChild, memToS);

		}
		//Current node is less than the node we are looking for, so we need to go right
		else if(comparisonValue < 0){
			return search(referenceNode.rightChild, memToS);
		}
		return referenceNode;
		
	}

	
}

/**
 * Node class to be used to represent each individual Member of the community in the BST.
 * @author Tahsin Nawar Akanda
 *
 * @param <Member>
 */
class personNode<Member>{
	Member data;
	//public Object data;
	personNode<Member> leftChild;
	personNode<Member> rightChild;
	
	public personNode(Member insertData){
		this(insertData, null, null);
	}
	
	public personNode(Member insertData, personNode<Member> insertLeft, personNode<Member> insertRight){
		this.leftChild = insertLeft;
		this.rightChild = insertRight;
		this.data = (Member) insertData;
	}
	
	
}