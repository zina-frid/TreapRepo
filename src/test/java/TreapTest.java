import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class TreapTest {

    @Test
    public void testForAddRemoveContains() {
    Treap<Integer> treap1 = new Treap<>();


        assertTrue(treap1.isEmpty());

        //Тест для add()
        assertTrue(treap1.add(7));
        assertTrue(treap1.add(4));
        assertTrue(treap1.add(2));
        assertTrue(treap1.add(0));
        assertTrue(treap1.add(3));
        assertTrue(treap1.add(6));
        assertTrue(treap1.add(5));

        assertEquals(7, treap1.size());

        assertTrue(treap1.add(13));
        assertTrue(treap1.add(9));
        assertTrue(treap1.add(14));
        assertTrue(treap1.add(11));

        assertEquals(11, treap1.size());

        //Тест для remove()
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

        //Тест для contains()
        assertFalse(treap1.contains(7));
        assertFalse(treap1.contains(3));
        assertFalse(treap1.contains(16));

        assertTrue(treap1.contains(4));
        assertTrue(treap1.contains(0));
        assertTrue(treap1.contains(6));


        //Тест для first() и last()
        assertEquals(0, (int) treap1.first());
        assertEquals(13, (int) treap1.last());

        //Тест для isEmpty()
        assertFalse(treap1.isEmpty());
    }

    @Test
    public void testForIterator() {

        SortedSet<Integer> treap2 = new Treap<>();

        assertTrue(treap2.add(7));
        assertTrue(treap2.add(4));
        assertTrue(treap2.add(2));
        assertTrue(treap2.add(0));
        assertTrue(treap2.add(3));
        assertTrue(treap2.add(6));
        assertTrue(treap2.add(5));
        assertTrue(treap2.add(13));
        assertTrue(treap2.add(9));
        assertTrue(treap2.add(14));
        assertTrue(treap2.add(11));

        Iterator<Integer> iterator1 = treap2.iterator();
        Iterator<Integer> iterator2 = treap2.iterator();

        while (iterator1.hasNext()) {
            assertEquals(iterator2.next(), iterator1.next());
        }


        Integer key;
        Integer[] keys = new Integer[11];
        treap2.toArray(keys);

        List<Integer> list = new ArrayList<>();

        Iterator<Integer> treapIter = treap2.iterator();
        while (treapIter.hasNext()) {
            key = treapIter.next();
            list.add(key);
            treapIter.remove();
        }
        assertTrue(treap2.isEmpty());
        assertArrayEquals(keys, list.toArray(new Integer[11]));
    }

}


