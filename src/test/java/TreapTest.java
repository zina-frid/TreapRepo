import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.*;

public class TreapTest {

    @Test
    public void testForAddRemoveContains() {

        SortedSet<Integer> treap1 = new Treap<>();

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

        //Тест для containsAll()
        List<Integer> list1 = new ArrayList<>();
        list1.add(4);
        list1.add(9);
        list1.add(13);
        assertTrue(treap1.containsAll(list1));
        list1.add(7);
        assertFalse(treap1.containsAll(list1));

        //Тест для addAll()
        list1.add(3);
        list1.add(8);
        List<Integer> list2 = new ArrayList<>();
        list2.add(7);
        list2.add(3);
        list2.add(8);
        assertTrue(treap1.addAll(list2));
        assertTrue(treap1.containsAll(list1));

        //Тест для retainAll()
        list2.add(13);
        list2.add(9);
        assertTrue(treap1.retainAll(list2));
        assertTrue(treap1.containsAll(list2));
        assertEquals(treap1.size(), list2.size());

        //Тест для removeAll()
        assertTrue(treap1.removeAll(list2));
        assertTrue(treap1.isEmpty());

        //Тест для clear()
        assertTrue(treap1.add(3));
        assertTrue(treap1.add(5));
        assertEquals(2, treap1.size());
        assertFalse(treap1.isEmpty());
        treap1.clear();
        assertTrue(treap1.isEmpty());
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


    @Test
    public void testForSubTreaps() {

        SortedSet<Integer> treap3 = new Treap<>();

        assertTrue(treap3.add(7));
        assertTrue(treap3.add(2));
        assertTrue(treap3.add(0));
        assertTrue(treap3.add(5));
        assertTrue(treap3.add(10));
        assertTrue(treap3.add(14));

        //Тесты для SubSet
        SortedSet<Integer> subSetTreap = treap3.subSet(3, 11);
        //Попадут 5, 7, 10
        assertEquals(3, subSetTreap.size());
        assertFalse(subSetTreap.contains(2));
        assertFalse(subSetTreap.contains(13));
        assertTrue(subSetTreap.contains(7));
        assertTrue(subSetTreap.contains(10));

        assertTrue(subSetTreap.add(4));
        assertTrue(treap3.contains(4));
        assertEquals(4, subSetTreap.size());
        assertTrue(subSetTreap.remove(4));
        assertEquals(3, subSetTreap.size());
        assertEquals(5, (int) subSetTreap.first());
        assertEquals(10, (int) subSetTreap.last());


        List<Integer> expectedSubTreap = new ArrayList<>();
        expectedSubTreap.add(5);
        expectedSubTreap.add(7);
        expectedSubTreap.add(10);
        expectedSubTreap.add(6);
        expectedSubTreap.add(9);

        assertTrue(treap3.add(3));
        assertTrue(treap3.add(6));
        assertTrue(treap3.add(9));
        assertTrue(treap3.add(11));

        assertTrue(subSetTreap.containsAll(expectedSubTreap));
        assertTrue(subSetTreap.contains(3));
        assertFalse(subSetTreap.contains(11));

        try {
            subSetTreap.add(12);
        } catch (IllegalArgumentException e) {
            assertFalse(subSetTreap.contains(12));
        }

        //Тесты для итератора SubTreap
        Iterator<Integer> iterator1 = subSetTreap.iterator();
        Iterator<Integer> iterator2 = subSetTreap.iterator();

        while (iterator1.hasNext()) {
            assertEquals(iterator2.next(), iterator1.next());
        }

        Integer key;
        Integer[] keys = new Integer[6];
        subSetTreap.toArray(keys);

        List<Integer> list = new ArrayList<>();

        Iterator<Integer> subTreapIter = subSetTreap.iterator();
        while (subTreapIter.hasNext()) {
            key = subTreapIter.next();
            list.add(key);
            subTreapIter.remove();
        }
        assertTrue(subSetTreap.isEmpty());
        assertArrayEquals(keys, list.toArray(new Integer[6]));

        assertFalse(treap3.isEmpty());
        assertEquals(0, subSetTreap.size());
    }


    @Test
    public void testForHeadAndTail() {

        SortedSet<Integer> treap4 = new Treap<>();

        assertTrue(treap4.add(7));
        assertTrue(treap4.add(2));
        assertTrue(treap4.add(0));
        assertTrue(treap4.add(5));
        assertTrue(treap4.add(10));
        assertTrue(treap4.add(14));
        assertTrue(treap4.add(3));
        assertTrue(treap4.add(6));
        assertTrue(treap4.add(9));
        assertTrue(treap4.add(11));

        //Тесты для HeadSet
        SortedSet<Integer> headSetTreap = treap4.headSet(7);
        //Попадут 2, 0, 5, 3, 6

        assertFalse(headSetTreap.contains(11));
        assertFalse(headSetTreap.contains(15));
        assertTrue(headSetTreap.contains(5));
        assertTrue(headSetTreap.contains(0));
        assertTrue(headSetTreap.tailSet(6).contains(6));

        assertTrue(headSetTreap.add(4));
        assertEquals(6, headSetTreap.size());
        assertTrue(headSetTreap.remove(4));
        assertEquals(5, headSetTreap.size());

        try {
            headSetTreap.add(12);
        } catch (IllegalArgumentException e) {
            assertFalse(headSetTreap.contains(12));
        }

        //Тесты для TailSet
        SortedSet<Integer> tailSetTreap = treap4.tailSet(7);
        //Попадут 7, 10, 14, 9, 11

        assertFalse(tailSetTreap.contains(4));
        assertFalse(tailSetTreap.contains(0));
        assertTrue(tailSetTreap.contains(7));
        assertTrue(tailSetTreap.contains(14));

        assertFalse(tailSetTreap.headSet(2).contains(1));
        assertTrue(tailSetTreap.headSet(10).contains(9));

        assertTrue(tailSetTreap.add(12));
        assertEquals(6, tailSetTreap.size());
        assertTrue(tailSetTreap.remove(12));
        assertEquals(5, tailSetTreap.size());
    }

    @Test
    public void testForTreapOfStrings() {

        SortedSet<String> treap5 = new Treap<>();

        assertTrue(treap5.isEmpty());

        //Тест для add()
        assertTrue(treap5.add("c"));
        assertTrue(treap5.add("a"));
        assertTrue(treap5.add("t"));
        assertTrue(treap5.add("y"));
        assertEquals(4, treap5.size());
        assertFalse(treap5.add("a"));

        //Тест для remove()
        assertTrue(treap5.remove("y"));
        assertFalse(treap5.remove("x"));
        assertEquals(3, treap5.size());

        //Тест для contains()
        assertFalse(treap5.contains("b"));
        assertFalse(treap5.contains("d"));
        assertTrue(treap5.contains("c"));
        assertTrue(treap5.contains("t"));

        //Тест для first() и last()
        assertEquals("a", treap5.first());
        assertEquals("t", treap5.last());
        assertNotEquals("T", treap5.last());

        StringBuilder string = new StringBuilder();
        for (String s : treap5) string.append(s);
        assertEquals("act", string.toString());


        assertTrue(treap5.add("i"));
        assertTrue(treap5.add("u"));

        //Тесты для subSet
        SortedSet<String> subSetString = treap5.subSet("c", "t");
        //Попадут "c", "i"
        assertFalse(subSetString.contains("t"));
        assertTrue(subSetString.contains("i"));
        assertEquals("c", subSetString.first());
        assertEquals("i", subSetString.last());

        assertTrue(subSetString.add("e"));
        assertEquals(3, subSetString.size());
        assertTrue(subSetString.remove("e"));
        assertEquals(2, subSetString.size());

        //Тесты для headSet
        SortedSet<String> headSetString = treap5.headSet("t");
        //Попадут "a", "c", "i"
        assertFalse(headSetString.contains("t"));
        assertTrue(headSetString.contains("i"));
        assertEquals("a", headSetString.first());
        assertEquals("i", headSetString.last());

        assertTrue(headSetString.add("b"));
        assertEquals(4, headSetString.size());
        assertTrue(headSetString.remove("b"));
        assertEquals(3, headSetString.size());

        //Тесты для tailSet
        SortedSet<String> tailSetString = treap5.tailSet("c");
        //Попадут "c", "i", "t", "u"
        assertFalse(tailSetString.contains("a"));
        assertTrue(tailSetString.contains("t"));
        assertEquals("c", tailSetString.first());
        assertEquals("u", tailSetString.last());

        assertTrue(tailSetString.add("s"));
        assertEquals(5, tailSetString.size());
        assertTrue(tailSetString.remove("s"));
        assertEquals(4, tailSetString.size());


        assertTrue(treap5.remove("c"));
        assertTrue(treap5.remove("a"));
        assertFalse(treap5.remove("T"));
        assertTrue(treap5.remove("t"));
        assertTrue(treap5.remove("i"));
        assertTrue(treap5.remove("u"));

        assertTrue(treap5.isEmpty());
    }

    @Test
    public void testForPrint() throws IOException {

        FileWriter fw = new FileWriter("src/test/resources/output.txt");
        Treap<Integer> treap1 = new Treap<>();

        assertTrue(treap1.add(7));
        assertTrue(treap1.add(4));
        assertTrue(treap1.add(2));
        assertTrue(treap1.add(0));
        assertTrue(treap1.add(3));
        assertTrue(treap1.add(6));
        assertTrue(treap1.add(5));
        assertTrue(treap1.add(13));
        assertTrue(treap1.add(9));
        assertTrue(treap1.add(14));
        assertTrue(treap1.add(11));
        treap1.printTreap(fw);
        fw.close();



    }
}
