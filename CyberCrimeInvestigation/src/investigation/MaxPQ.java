package investigation;

/******************************************************************************
 *  Compilation:  javac MaxPQ.java
 *  Execution:    java MaxPQ < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/24pq/tinyPQ.txt
 *
 *  Generic max priority queue implementation with a binary heap.
 *  Can be used with a comparator instead of the natural order,
 *  but the generic Key type must still be Comparable.
 *
 *  % java MaxPQ < tinyPQ.txt
 *  Q X P (6 left on pq)
 *
 *  We use a one-based array to simplify parent and child calculations.
 *
 *  Can be optimized by replacing full exchanges with half exchanges
 *  (ala insertion sort).
 *
 ******************************************************************************/

 import java.util.Comparator;
 import java.util.Iterator;
 import java.util.NoSuchElementException;
 
 /**
  *  The {@code MaxPQ} class represents a priority queue of generic keys.
  *  It supports the usual <em>insert</em> and <em>delete-the-maximum</em>
  *  operations, along with methods for peeking at the maximum key,
  *  testing if the priority queue is empty, and iterating through
  *  the keys.
  *  <p>
  *  This implementation uses a <em>binary heap</em>.
  *  The <em>insert</em> and <em>delete-the-maximum</em> operations take
  *  &Theta;(log <em>n</em>) amortized time, where <em>n</em> is the number
  *  of elements in the priority queue. This is an amortized bound
  *  (and not a worst-case bound) because of array resizing operations.
  *  The <em>min</em>, <em>size</em>, and <em>is-empty</em> operations take
  *  &Theta;(1) time in the worst case.
  *  Construction takes time proportional to the specified capacity or the
  *  number of items used to initialize the data structure.
  *  <p>
  *  For additional documentation, see
  *  <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
  *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
  *
  *  @author Robert Sedgewick
  *  @author Kevin Wayne
  *
  *  @param <Key> the generic type of key on this priority queue
  */
 
 public class MaxPQ<Key> implements Iterable<Key> {
     private Key[] pq;                    // store items at indices 1 to n
     private int n;                       // number of items on priority queue
     private Comparator<Key> comparator;  // optional comparator
 
     /**
      * Initializes an empty priority queue with the given initial capacity.
      *
      * @param  initCapacity the initial capacity of this priority queue
      */
     public MaxPQ(int initCapacity) {
        // IGNORE this type safety error
         pq = (Key[]) new Object[initCapacity + 1];
         n = 0;
     }
 
     /**
      * Initializes an empty priority queue.
      */
     public MaxPQ() {
         this(1);
     }
 
     /**
      * Initializes an empty priority queue with the given initial capacity,
      * using the given comparator.
      *
      * @param  initCapacity the initial capacity of this priority queue
      * @param  comparator the order in which to compare the keys
      */
     public MaxPQ(int initCapacity, Comparator<Key> comparator) {
         this.comparator = comparator;
         pq = (Key[]) new Object[initCapacity + 1];
         n = 0;
     }
 
     /**
      * Initializes an empty priority queue using the given comparator.
      *
      * @param  comparator the order in which to compare the keys
      */
     public MaxPQ(Comparator<Key> comparator) {
         this(1, comparator);
     }
 
     /**
      * Initializes a priority queue from the array of keys.
      * Takes time proportional to the number of keys, using sink-based heap construction.
      *
      * @param  keys the array of keys
      */
     public MaxPQ(Key[] keys) {
         n = keys.length;
         pq = (Key[]) new Object[keys.length + 1];
         for (int i = 0; i < n; i++)
             pq[i+1] = keys[i];
         for (int k = n/2; k >= 1; k--)
             sink(k);
         assert isMaxHeap();
     }
 
 
 
     /**
      * Returns true if this priority queue is empty.
      *
      * @return {@code true} if this priority queue is empty;
      *         {@code false} otherwise
      */
     public boolean isEmpty() {
         return n == 0;
     }
 
     /**
      * Returns the number of keys on this priority queue.
      *
      * @return the number of keys on this priority queue
      */
     public int size() {
         return n;
     }
 
     /**
      * Returns a largest key on this priority queue.
      *
      * @return a largest key on this priority queue
      * @throws NoSuchElementException if this priority queue is empty
      */
     public Key max() {
         if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
         return pq[1];
     }
 
     // resize the underlying array to have the given capacity
     private void resize(int capacity) {
         assert capacity > n;
         Key[] temp = (Key[]) new Object[capacity];
         for (int i = 1; i <= n; i++) {
             temp[i] = pq[i];
         }
         pq = temp;
     }
 
 
     /**
      * Adds a new key to this priority queue.
      *
      * @param  x the new key to add to this priority queue
      */
     public void insert(Key x) {
 
         // double size of array if necessary
         if (n == pq.length - 1) resize(2 * pq.length);
 
         // add x, and percolate it up to maintain heap invariant
         pq[++n] = x;
         swim(n);
         assert isMaxHeap();
     }
 
     /**
      * Removes and returns a largest key on this priority queue.
      *
      * @return a largest key on this priority queue
      * @throws NoSuchElementException if this priority queue is empty
      */
     public Key delMax() {
         if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
         Key max = pq[1];
         exch(1, n--);
         sink(1);
         pq[n+1] = null;     // to avoid loitering and help with garbage collection
         if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length / 2);
         assert isMaxHeap();
         return max;
     }
 
 
    /***************************************************************************
     * Helper functions to restore the heap invariant.
     ***************************************************************************/
 
     private void swim(int k) {
         while (k > 1 && less(k/2, k)) {
             exch(k/2, k);
             k = k/2;
         }
     }
 
     private void sink(int k) {
         while (2*k <= n) {
             int j = 2*k;
             if (j < n && less(j, j+1)) j++;
             if (!less(k, j)) break;
             exch(k, j);
             k = j;
         }
     }
 
    /***************************************************************************
     * Helper functions for compares and swaps.
     ***************************************************************************/
     private boolean less(int i, int j) {
         if (comparator == null) {
             return ((Comparable<Key>) pq[i]).compareTo(pq[j]) < 0;
         }
         else {
             return comparator.compare(pq[i], pq[j]) < 0;
         }
     }
 
     private void exch(int i, int j) {
         Key swap = pq[i];
         pq[i] = pq[j];
         pq[j] = swap;
     }
 
     // is pq[1..n] a max heap?
     private boolean isMaxHeap() {
         for (int i = 1; i <= n; i++) {
             if (pq[i] == null) return false;
         }
         for (int i = n+1; i < pq.length; i++) {
             if (pq[i] != null) return false;
         }
         if (pq[0] != null) return false;
         return isMaxHeapOrdered(1);
     }
 
     // is subtree of pq[1..n] rooted at k a max heap?
     private boolean isMaxHeapOrdered(int k) {
         if (k > n) return true;
         int left = 2*k;
         int right = 2*k + 1;
         if (left  <= n && less(k, left))  return false;
         if (right <= n && less(k, right)) return false;
         return isMaxHeapOrdered(left) && isMaxHeapOrdered(right);
     }
 
 
    /***************************************************************************
     * Iterator.
     ***************************************************************************/
 
     /**
      * Returns an iterator that iterates over the keys on this priority queue
      * in descending order.
      * The iterator doesn't implement {@code remove()} since it's optional.
      *
      * @return an iterator that iterates over the keys in descending order
      */
     public Iterator<Key> iterator() {
         return new HeapIterator();
     }
 
     private class HeapIterator implements Iterator<Key> {
 
         // create a new pq
         private MaxPQ<Key> copy;
 
         // add all items to copy of heap
         // takes linear time since already in heap order so no keys move
         public HeapIterator() {
             if (comparator == null) copy = new MaxPQ<Key>(size());
             else                    copy = new MaxPQ<Key>(size(), comparator);
             for (int i = 1; i <= n; i++)
                 copy.insert(pq[i]);
         }
 
         public boolean hasNext()  { return !copy.isEmpty();                     }
         public void remove()      { throw new UnsupportedOperationException();  }
 
         public Key next() {
             if (!hasNext()) throw new NoSuchElementException();
             return copy.delMax();
         }
     }

 
 }
