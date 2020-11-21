import java.util.*;
import javafx.util.Pair;

public class Treap <T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {

    private static class TreapNode<T> {
        final T key;
        int priority;

        TreapNode<T> left;
        TreapNode<T> right;
        TreapNode<T> parent = null;

        public TreapNode(T key, int priority) {
            this.key = key;
            this.priority = priority;
        }

    }

    private TreapNode<T> root = null;
    private int size = 0;


    private TreapNode<T> find(T key){
        if (root == null) return null;
        return find(root, key);
    }

    private TreapNode<T> find(TreapNode<T> start, T key){
        int comparison = key.compareTo(start.key);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, key);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, key);
        }
    }

    public boolean contains(Object o) {
        T t = (T) o;
        TreapNode<T> closest = find(t);
        return closest != null && t.compareTo(closest.key) == 0;
    }

    private TreapNode<T> merge(TreapNode<T> first, TreapNode<T> second) {
        if (first == null) return second;
        if (second == null) return first;

        if (first.priority > second.priority) {
            first.right = merge(first.right, second);
            first.right.parent = first;
            return first;
        } else {
            second.left = merge(first, second.left);
            second.left.parent = second;
            return second;
        }
    }

    public Pair<TreapNode<T>, TreapNode<T>> split(TreapNode<T> start, T key){
        if (start == null) return new Pair<>(null, null);
        if (key.compareTo(start.key) > 0) {

            Pair<TreapNode<T>, TreapNode<T>> res = split(start.right, key);
            start.right = res.getKey();

            if (start.right != null) start.right.parent = start;
            if (res.getValue() != null) res.getValue().parent = null;
            return new Pair<>(start, res.getValue());

        } else {
            Pair<TreapNode<T>, TreapNode<T>> res = split(start.left, key);
            start.left = res.getValue();

            if (start.left != null) start.left.parent = start;
            if (res.getKey() != null) res.getKey().parent = null;
            return new Pair<>(res.getKey(), start);
        }


    }

    public boolean add(T key, int priority) {
        TreapNode<T> closest = find(key);

        if (closest != null && closest.key.equals(key)) return false;

        Pair<TreapNode<T>, TreapNode<T>> twoTreaps = split(root, key);
        TreapNode<T> temp = merge(twoTreaps.getKey(), new TreapNode<>(key, priority));
        root = merge(temp, twoTreaps.getValue());

        root.parent = null;
        size++;
        return true;
    }

    public boolean remove(T key) {

        TreapNode<T> closest = find(key);

        if (closest != null && closest.key.equals(key)){

            TreapNode<T> parent = closest.parent;

            if(parent != null){
                if(parent.left != null && parent.left.key.equals(closest.key)){

                    parent.left = merge(closest.left, closest.right);
                    if (parent.left != null) parent.left.parent = parent;

                } else {

                    parent.right = merge(closest.left, closest.right);
                    if (parent.right != null) parent.right.parent = parent;
                }
            } else {
                root = merge(closest.left, closest.right);
                if (root != null)
                    root.parent = null;
            }
            size--;
            return true;
        }
        return false;

    }

    public T first() {
        if (root == null) throw new NoSuchElementException();
        TreapNode<T> node = root;
        while (node.left != null) node = node.left;
        return node.key;
    }

    public T last() {
        if (root == null) throw new NoSuchElementException();
        TreapNode<T> node = root;
        while (node.right != null) node = node.right;
        return node.key;
    }


    public Comparator<? super T> comparator() {
        return null;
    }

    public SortedSet<T> subSet(T fromElement, T toElement) {
        return null;
    }

    public SortedSet<T> headSet(T toElement) {
        return null;
    }

    public SortedSet<T> tailSet(T fromElement) {
        return null;
    }



    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return false;
    }



    public Iterator<T> iterator() {
        return null;
    }

    public Object[] toArray() {
        return new Object[0];
    }

    public <T1> T1[] toArray(T1[] a) {
        return null;
    }




    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public void clear() {

    }

}