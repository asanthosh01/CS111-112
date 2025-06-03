package braille;

import java.util.ArrayList;

/**
 * Contains methods to translate Braille to English and English to Braille using
 * a BST.
 * Reads encodings, adds characters, and traverses tree to find encodings.
 * 
 * @author Seth Kelley
 * @author Kal Pandit
 */
public class BrailleTranslator {

    private TreeNode treeRoot;

    /**
     * Default constructor, sets symbols to an empty ArrayList
     */
    public BrailleTranslator() {
        treeRoot = null;
    }

    /**
     * Reads encodings from an input file as follows:
     * - One line has the number of characters
     * - n lines with character (as char) and encoding (as string) space-separated
     * USE StdIn.readChar() to read character and StdIn.readLine() after reading
     * encoding
     * 
     * @param inputFile the input file name
     */
    public void createSymbolTree(String inputFile) {

        /* PROVIDED, DO NOT EDIT */

        StdIn.setFile(inputFile);
        int numberOfChars = Integer.parseInt(StdIn.readLine());
        for (int i = 0; i < numberOfChars; i++) {
            Symbol s = readSingleEncoding();
            addCharacter(s);
        }
    }

    /**
     * Reads one line from an input file and returns its corresponding
     * Symbol object
     * 
     * ONE line has a character and its encoding (space separated)
     * 
     * @return the symbol object
     */
    public Symbol readSingleEncoding() {
        char character = StdIn.readChar(); // read the character
        String encoding = StdIn.readString(); // read the encoding
        StdIn.readLine(); // read the newline character to move to the next line
    
        return new Symbol(character, encoding); // return the created symbol object
    }

    /**
     * Adds a character into the BST rooted at treeRoot.
     * Traces encoding path (0 = left, 1 = right), starting with an empty root.
     * Last digit of encoding indicates position (left or right) of character within
     * parent.
     * 
     * @param newSymbol the new symbol object to add
     */
    public void addCharacter(Symbol newSymbol) {
        String encoding = newSymbol.getEncoding();
        TreeNode currentNode = treeRoot;
    
        // if the tree is empty, initialize the root with an empty encoding
        if (currentNode == null) {
            treeRoot = new TreeNode(new Symbol(""), null, null);
            currentNode = treeRoot;
        }
    
        String partialEncoding = "";
        
        // traverse the encoding path and create intermediate nodes if necessary
        for (int i = 0; i < encoding.length(); i++) {
            char direction = encoding.charAt(i);
            partialEncoding += direction; // build partial encoding string
    
            if (direction == 'L') {
                // create a left child if it does not exist
                if (currentNode.getLeft() == null) {
                    currentNode.setLeft(new TreeNode(new Symbol(partialEncoding), null, null));
                }
                currentNode = currentNode.getLeft();
            } else if (direction == 'R') {
                // create a left child if it does not exist
                if (currentNode.getRight() == null) {
                    currentNode.setRight(new TreeNode(new Symbol(partialEncoding), null, null));
                }
                currentNode = currentNode.getRight();
            }
        }
    
        // assign the final leaf node with the character and full encoding
        currentNode.setSymbol(newSymbol);
    }

    /**
     * Given a sequence of characters, traverse the tree based on the characters
     * to find the TreeNode it leads to
     * 
     * @param encoding Sequence of braille (Ls and Rs)
     * @return Returns the TreeNode of where the characters lead to, or null if there is no path
     */
    public TreeNode getSymbolNode(String encoding) {
        TreeNode currentNode = treeRoot; // start traversal from the root
    
        // traverse the tree following the encoding path
        for (int i = 0; i < encoding.length(); i++) {
            if (currentNode == null) {
                return null; // if we hit a null node before reaching the end, then encoding doesn't exist
            }
    
            char direction = encoding.charAt(i);
            if (direction == 'L') {
                currentNode = currentNode.getLeft(); // move left
            } else if (direction == 'R') {
                currentNode = currentNode.getRight(); // move right
            } else {
                return null; // if there is an invalid encoding character, return null
            }
        }
    
        return currentNode; // return the node even if it doesn’t contain a character
    }

    /**
     * Given a character to look for in the tree will return the encoding of the
     * character
     * 
     * @param character The character that is to be looked for in the tree
     * @return Returns the String encoding of the character
     */
    public String findBrailleEncoding(char character) {
        return findBrailleEncodingHelper(treeRoot, character);
    }

    private String findBrailleEncodingHelper(TreeNode node, char character) {
        if (node == null) {
            return null; // reached a null node and character was not found (base case)
        }

        Symbol symbol = node.getSymbol();
        if (symbol.getCharacter() == character) {
            return symbol.getEncoding(); // found the character and return its encoding
        }

        // recursively search the left subtree
        String leftResult = findBrailleEncodingHelper(node.getLeft(), character);
        if (leftResult != null) {
            return leftResult; // character found in left subtree
        }

        // recursively search the right subtree
        return findBrailleEncodingHelper(node.getRight(), character);
    }

    /**
     * Given a prefix to a Braille encoding, return an ArrayList of all encodings that start with
     * that prefix
     * 
     * @param start the prefix to search for
     * @return all Symbol nodes which have encodings starting with the given prefix
     */
    public ArrayList<Symbol> encodingsStartWith(String start) {
        ArrayList<Symbol> result = new ArrayList<>();
        
        // find the starting node using getSymbolNode
        TreeNode startNode = getSymbolNode(start);
        
        // if no such node exists, then return an empty list
        if (startNode == null) {
            return result;
        }

        // do a preorder traversal to collect all leaf nodes or characters
        collectLeafSymbols(startNode, result);
        
        return result;
    }

    private void collectLeafSymbols(TreeNode node, ArrayList<Symbol> result) {
        if (node == null) {
            return;
        }
    
        Symbol symbol = node.getSymbol();
        
        // add any node that has a valid character even if it isnt a leaf
        if (symbol != null && symbol.hasCharacter()) {
            result.add(symbol);
        }
    
        // recursively traverse left and right subtrees
        collectLeafSymbols(node.getLeft(), result);
        collectLeafSymbols(node.getRight(), result);
    }

    /**
     * Reads an input file and processes encodings six chars at a time.
     * Then, calls getSymbolNode on each six char chunk to get the
     * character.
     * 
     * Return the result of all translations, as a String.
     * @param input the input file
     * @return the translated output of the Braille input
     */
    public String translateBraille(String input) {

        StdIn.setFile(input); // set the standard input to the provided file name

        String brailleString = StdIn.readString(); // read entire string of braille encodings from the file
        
        StringBuilder result = new StringBuilder();
        
        // process the input string in chunks of 6 characters
        for (int i = 0; i < brailleString.length(); i += 6) {
            String chunk = brailleString.substring(i, i + 6); // extract a six-character encoding
            
            TreeNode node = getSymbolNode(chunk); // find the corresponding TreeNode
            
            if (node != null) {
                result.append(node.getSymbol().getCharacter()); // append the character to result
            }
        }
        
        return result.toString(); // return the final translated word
    }


    /**
     * Given a character, delete it from the tree and delete any encodings not
     * attached to a character (ie. no children).
     * 
     * @param symbol the symbol to delete
     */
    public void deleteSymbol(char symbol) {
        String encoding = findBrailleEncoding(symbol); // get the encoding for the character
        if (encoding == null) {
            return; // if the character isn't found, there's nothing to delete
        }
    
        treeRoot = deleteSymbolHelper(treeRoot, encoding, 0);
    }
    
    private TreeNode deleteSymbolHelper(TreeNode node, String encoding, int depth) {
        if (node == null) {
            return null;
        }
    
        if (depth == encoding.length()) {
            // reached the node that contains the symbol
            if (node.getSymbol() != null && node.getSymbol().hasCharacter()) {
                node.setSymbol(new Symbol("")); // remove character, keep structure for now
            }
        } else {
            char direction = encoding.charAt(depth);
            if (direction == 'L') {
                node.setLeft(deleteSymbolHelper(node.getLeft(), encoding, depth + 1));
            } else if (direction == 'R') {
                node.setRight(deleteSymbolHelper(node.getRight(), encoding, depth + 1));
            }
        }
    
        // remove the node if it's empty and has no children
        if (node.getSymbol() != null && !node.getSymbol().hasCharacter()
                && node.getLeft() == null && node.getRight() == null) {
            return null; // delete the node if it's completely empty
        }

        return node; // return the updated node
    }



    public TreeNode getTreeRoot() {
        return this.treeRoot;
    }

    public void setTreeRoot(TreeNode treeRoot) {
        this.treeRoot = treeRoot;
    }

    public void printTree() {
        printTree(treeRoot, "", false, true);
    }

    private void printTree(TreeNode n, String indent, boolean isRight, boolean isRoot) {
        StdOut.print(indent);

        // Print out either a right connection or a left connection
        if (!isRoot)
            StdOut.print(isRight ? "|+R- " : "--L- ");

        // If we're at the root, we don't want a 1 or 0
        else
            StdOut.print("+--- ");

        if (n == null) {
            StdOut.println("null");
            return;
        }
        // If we have an associated character print it too
        if (n.getSymbol() != null && n.getSymbol().hasCharacter()) {
            StdOut.print(n.getSymbol().getCharacter() + " -> ");
            StdOut.print(n.getSymbol().getEncoding());
        }
        else if (n.getSymbol() != null) {
            StdOut.print(n.getSymbol().getEncoding() + " ");
            if (n.getSymbol().getEncoding().equals("")) {
                StdOut.print("\"\" ");
            }
        }
        StdOut.println();

        // If no more children we're done
        if (n.getSymbol() != null && n.getLeft() == null && n.getRight() == null)
            return;

        // Add to the indent based on whether we're branching left or right
        indent += isRight ? "|    " : "     ";

        printTree(n.getRight(), indent, true, false);
        printTree(n.getLeft(), indent, false, false);
    }

}
