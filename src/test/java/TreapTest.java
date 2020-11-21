import org.junit.Test;

import static org.junit.Assert.*;

public class TreapTest {

    @Test
    public void testForAddRemoveContains() {
    Treap<Integer> treap1 = new Treap<>();

    /* Добавим эти узлы в treap1
                                [7, 10]
                            /            \
                      [4, 6]                [13, 8]
                     /      \              /       \
                [2, 4]       [6, 2]   [9, 7]       [14, 4]
               /      \       /             \
           [0, 3]   [3, 3]  [5, 1]          [11, 3]

     */

        assertEquals(0, treap1.size());

        assertTrue(treap1.add(7, 10));
        assertTrue(treap1.add(4, 6));
        assertTrue(treap1.add(2, 4));
        assertTrue(treap1.add(0, 3));
        assertTrue(treap1.add(3, 3));
        assertTrue(treap1.add(6, 2));
        assertTrue(treap1.add(5, 1));

        assertEquals(7, treap1.size());

        assertTrue(treap1.add(13, 8));
        assertTrue(treap1.add(9,7));
        assertTrue(treap1.add(14,4));
        assertTrue(treap1.add(11,3));

        assertEquals(11, treap1.size());


        assertTrue(treap1.remove(7));
        assertTrue(treap1.remove(2));
        assertTrue(treap1.remove(3));
        assertTrue(treap1.remove(5));
        assertTrue(treap1.remove(14));

        assertEquals(6, treap1.size());

        assertFalse(treap1.remove(7));
        assertFalse(treap1.remove(1));
        assertFalse(treap1.remove(15));

        assertEquals(6, treap1.size());


        assertFalse(treap1.contains(7));
        assertFalse(treap1.contains(3));
        assertFalse(treap1.contains(16));

        assertTrue(treap1.contains(4));
        assertTrue(treap1.contains(0));
        assertTrue(treap1.contains(6));


        assertEquals(0, (int) treap1.first());
        assertEquals(13, (int) treap1.last());
    }

}
