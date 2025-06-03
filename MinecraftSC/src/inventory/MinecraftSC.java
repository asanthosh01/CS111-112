package inventory;

/**
 * This class implements a separate hashing table without rehashing
 * to store Minecraft items.
 * 
 * @author Kal Pandit
 */
public class MinecraftSC {
    private Item[] table;

    /**
     * Constructor that creates a table with size 26 (letters in alphabet)
     */
    public MinecraftSC() {
        table = new Item[26];
    }

    /**
     * PROVIDED hash function to hash items given the first letter of
     * their item name. 
     * @param name the item name
     * @return a hash representing its letter (0 = A, 1 = B, etc)
     */
    public int hash(String name) {
        char firstLetter = name.charAt(0);

        return (int) firstLetter - 'A';
    }

    /**
     * Insert an item into the END of the list. No duplicates allowed.
     * Items contain a name and recipe (recipes contain items and a resulting count)
     * The item will be added to the list whose index is returned from hash(itemName). 
     * 
     * @param itemName the item name
     * @param recipeItems a 3x3 array representing how to craft this item
     * @param resultingCount how many items this recipe can generate
     */
    public void put(String itemName, String[][] recipeItems, int resultingCount) {
        int index = hash(itemName);

        // check whether the item already exists in the chain
        Item current = table[index];
        while (current != null) {
            if (current.getName().equals(itemName)) {
                // dupe item found so do nothing
                return;
            }
            current = current.getNext();
        }
        
        // no duplicate was found so create a new recipe and item
        Recipe recipe = new Recipe(recipeItems, resultingCount);
        Item newItem = new Item(itemName, recipe, null);

        // add the new item to the end of the linked list
        if (table[index] == null) {
            // the chain is empty so insert
            table[index] = newItem;
        } else {
            // traverse to the end of the linked list and append the new item
            current = table[index];
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newItem);
        }
    }

    /**
     * Given an item name, remove it from the hash table if it exists.
     * @param itemName the item name to search for
     */
    public void delete(String itemName) {
        int index = hash(itemName);
        Item current = table[index];
        Item previous = null;

        // traverse the chain to locate the item to delete
        while (current != null) {
            if (current.getName().equals(itemName)) {
                // if the item is found then remove it based on its position
                if (previous == null) {
                    // item is at the beginning of the chain
                    table[index] = current.getNext();
                } else {
                    // item is in the middle or end
                    previous.setNext(current.getNext());
                }
                return;  // deletion complete
            }
            previous = current;
            current = current.getNext();
        }
        // if the item is not found then nothing happens
    }

    /**
     * Searches for an item given its name, returning the item if present
     * and null otherwise. 
     * @param itemName the item name to search for
     * @return the Item object if found, null otherwise
     */
    public Item search(String itemName) {
        int indexToSearch = hash(itemName);

        Item curr = table[indexToSearch];

        while (curr != null) {
            // existence check
            if (curr.getName().equals(itemName))
                return curr;
            curr = curr.getNext();
        }

        return null;
    }

    /**
     * Inserts all craftable items into the hash table by calling
     * the method put.
     */
    public void putAllCraftableItems(String file) {
        StdIn.setFile(file);
        int n = StdIn.readInt();
        for (int i = 0; i < n; i++) {

            int count = StdIn.readInt();
            // trim just in case
            String itemName = StdIn.readLine().trim();
            int slots = Integer.parseInt(StdIn.readLine());
            String[][] recipe = new String[3][3];
            for (int j = 0; j < slots; j++) {
                int x = StdIn.readInt();
                int y = StdIn.readInt();
                // trim just in case
                recipe[x][y] = StdIn.readLine().trim();
            }
            put(itemName, recipe, count);
        }
    }

    /**
     * PROVIDED - finds the target item's recipe. Used when inserting individual
     * items. Used by driver to get a recipe for an individual item.
     * 
     * @param targetItem the item to find a recipe for
     * @return the recipe, or an empty 3x3 array if not found
     */
    public Recipe findUnknownRecipe(String targetItem, String file) {
        StdIn.setFile(file);
        int n = StdIn.readInt();
        Recipe res = new Recipe();
        for (int i = 0; i < n; i++) {
            int count = StdIn.readInt();
            // trim just in case
            String itemName = StdIn.readLine().trim();
            int slots = Integer.parseInt(StdIn.readLine());
            String[][] recipe = new String[3][3];
            for (int j = 0; j < slots; j++) {
                int x = StdIn.readInt();
                int y = StdIn.readInt();
                String item = StdIn.readLine().trim();
                // trim just in case
                if (itemName.equals(targetItem)) {
                    res.setResultingCount(count);
                    recipe[x][y] = item;
                    res.setItems(recipe);
                }
            }
        }
        return res;
    }

    public Item[] getTable() {
        return this.table;
    }

    public void setTable(Item[] table) {
        this.table = table;
    }

}
