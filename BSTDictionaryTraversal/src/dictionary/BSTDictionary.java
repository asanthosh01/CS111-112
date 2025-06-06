package dictionary;

import java.util.ArrayList;

public class BSTDictionary {

    // The root node of this BST
    private WordNode root;

    /**
     * This method is provided for you, do not edit it.
     * 
     * This will call your recursive postOrder() method, give it an ArrayList of
     * WordNodes, and then return that list. Your recursive method should fill
     * this list with WordNodes, in pre-order.
     * 
     * @return arraylist containing WordNodes of this tree, ordered via pre-order
     */
    public ArrayList<WordNode> preOrder() {
        // DO NOT EDIT
        ArrayList<WordNode> traversal = new ArrayList<>();
        preOrderHelper(root, traversal);
        return traversal;
    }

    /**
     * This is a recursive helper method for post-order traversal.
     * 
     * You should:
     * 1) return if the curr WordNode is null
     * 2) Add curr to the ArrayList
     * 3) Recursively call this method on curr's left child
     * 4) Recursively call this method on curr's right child
     */
    private void preOrderHelper(WordNode curr, ArrayList<WordNode> list) {
        if (curr == null) return; // base case
        list.add(curr); // visit current node
        preOrderHelper(curr.getLeft(), list); // recur on the left child
        preOrderHelper(curr.getRight(), list); // recur on the right child
    }

    /**
     * This method is provided for you, do not edit it.
     * 
     * This will call your recursive postOrder() method, give it an ArrayList of
     * WordNodes, and then return that list. Your recursive method should fill this
     * list with WordNodes, in post-order.
     * 
     * @return An arraylist containing WordNodes of this tree, ordered via post
     *         order
     */
    public ArrayList<WordNode> postOrder() {
        // DO NOT EDIT
        ArrayList<WordNode> traversal = new ArrayList<>();
        postOrderHelper(root, traversal);
        return traversal;
    }

    /**
     * This is a recursive helper method for post-order traversal.
     * 
     * You should:
     * 1) return if the curr WordNode is null
     * 2) Recursively call this method on curr's left child
     * 3) Recursively call this method on curr's right child
     * 4) Add curr to the ArrayList
     */
    private void postOrderHelper(WordNode curr, ArrayList<WordNode> list) {
        if (curr == null) return; // base case
        postOrderHelper(curr.getLeft(), list); // recur on the left child
        postOrderHelper(curr.getRight(), list); // recur on the right child
        list.add(curr); // visit the current node
    }

    /**
     * This method is provided for you, do not edit it.
     * 
     * This will call your recursive postOrder() method, give it an ArrayList of
     * WordNodes, and then return that list. Your recursive method should fill
     * this list with WordNodes, ordered via in-order.
     * 
     * @return arraylist containing WordNodes of this tree, ordered via in-order
     */
    public ArrayList<WordNode> inOrder() {
        // DO NOT EDIT
        ArrayList<WordNode> traversal = new ArrayList<>();
        inOrderHelper(root, traversal);
        return traversal;
    }

    /**
     * This is a recursive helper method for post-order traversal.
     * 
     * You should:
     * 1) return if the curr WordNode is null
     * 2) Recursively call this method on curr's left child
     * 3) Add curr to the ArrayList
     * 4) Recursively call this method on curr's right child
     */
    private void inOrderHelper(WordNode curr, ArrayList<WordNode> list) {
        if (curr == null) return; // base case
        inOrderHelper(curr.getLeft(), list); // recur on the left child
        list.add(curr); // visit the current node 
        inOrderHelper(curr.getRight(), list); // recur on the right child   
    }


    /**
     * This method should iteratively traverse the tree, using a Queue.
     * It should fill an ArrayList with WordNodes, ordered via level-order.
     * Finally, return that array list.
     * 
     * To complete this method:
     * 1) Create an ArrayList of WordNodes and a Queue of WordNodes
     * 2) enqueue the root of the tree
     * 3) while the queue is NOT empty:
     * -dequeue a node, add it to the arraylist
     * -if the left child of that node is not null, enqueue it
     * -if the right child of that node is not null, enqueue it
     * 4) return your ArrayList
     * 
     * @return arraylist containing WordNodes of this tree, ordered via level-order
     */
    public ArrayList<WordNode> levelOrder() {
        ArrayList<WordNode> result = new ArrayList<>(); // create an arraylist
        Queue<WordNode> queue = new Queue<>(); // create a queue
    
        if (this.root == null) {
            return result; // return an empty list if the tree is empty
        }
    
        queue.enqueue(this.root); // enqueue the root
    
        while (!queue.isEmpty()) { // while the queue is NOT empty
            WordNode current = queue.dequeue(); // dequeue a node
            result.add(current); // add it to the arraylist
    
            if (current.getLeft() != null) {
                queue.enqueue(current.getLeft()); // enqueue the left child if not null
            }
            if (current.getRight() != null) {
                queue.enqueue(current.getRight()); // enqueue the right child if not null
            }
        }
    
        return result; // return the arraylist
    }

    /**
     * This method is provided for you, do not edit it.
     * 
     * This inserts a new WordNode in this BST, containing the given
     * word and the given definition
     * 
     * @param word       The word to add
     * @param definition The definition of the word
     */
    public void addWord(String word, String definition) {
        // DO NOT EDIT
        if (root == null) {
            root = new WordNode(word, definition);
            return;
        }
        WordNode ptr = root;
        while (ptr != null) {
            if (word.compareTo(ptr.getWord()) < 0) {
                if (ptr.getLeft() == null) {
                    ptr.setLeft(new WordNode(word, definition));
                    return;
                }
                ptr = ptr.getLeft();
            } else if (word.compareTo(ptr.getWord()) > 0) {
                if (ptr.getRight() == null) {
                    ptr.setRight(new WordNode(word, definition));
                    return;
                }
                ptr = ptr.getRight();
            } else {
                return;
            }
        }
    }

    /**
     * This method is provided for you, do not edit it.
     * 
     * @return the root node of this BST
     */
    public WordNode getRoot() {
        // DO NOT EDIT
        return this.root;
    }
}
