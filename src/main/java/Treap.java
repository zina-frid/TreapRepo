import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.jetbrains.annotations.NotNull;

public class Treap <T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {

    private static class TreapNode<T> {

        final T key;
        int priority;

        TreapNode<T> left;
        TreapNode<T> right;
        TreapNode<T> parent = null;

        public TreapNode(T key) {
            this.key = key;
            priority = random.nextInt();
        }
    }

    private static final Random random = new Random();
    private TreapNode<T> root = null;
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private TreapNode<T> find(T key) {
        if (root == null) return null;
        return find(root, key);
    }

    private TreapNode<T> find(TreapNode<T> start, T key) {
        int comparison = key.compareTo(start.key);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, key);
        } else {
            if (start.right == null) return start;
            return find(start.right, key);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
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

    public NodePair<TreapNode<T>, TreapNode<T>> split(TreapNode<T> start, T key) {
        if (start == null) return new NodePair<>(null, null);
        if (key.compareTo(start.key) > 0) {

            NodePair<TreapNode<T>, TreapNode<T>> res = split(start.right, key);
            start.right = res.getFirst();

            if (start.right != null) start.right.parent = start;
            if (res.getSecond() != null) res.getSecond().parent = null;
            return new NodePair<>(start, res.getSecond());

        } else {
            NodePair<TreapNode<T>, TreapNode<T>> res = split(start.left, key);
            start.left = res.getSecond();

            if (start.left != null) start.left.parent = start;
            if (res.getFirst() != null) res.getFirst().parent = null;
            return new NodePair<>(res.getFirst(), start);
        }
    }

    @Override
    public boolean add(T key) {
        TreapNode<T> closest = find(key);

        if (closest != null && closest.key.equals(key)) return false;

        NodePair<TreapNode<T>, TreapNode<T>> twoTreaps = split(root, key);
        TreapNode<T> temp = merge(twoTreaps.getFirst(), new TreapNode<>(key));
        root = merge(temp, twoTreaps.getSecond());

        root.parent = null;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {

        @SuppressWarnings("unchecked")
        T key = (T) o;
        TreapNode<T> closest = find(key);

        if (closest != null && closest.key.equals(key))
            return removeNode(closest);

        return false;
    }

    public boolean removeNode(TreapNode<T> closest) {

        TreapNode<T> parent = closest.parent;

        if (parent != null) {
            if (parent.left != null && parent.left.key.equals(closest.key)) {

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

    @Override
    public T first() {

        if (root == null) throw new NoSuchElementException();

        TreapNode<T> node = root;
        while (node.left != null) node = node.left;

        return node.key;
    }

    @Override
    public T last() {

        if (root == null) throw new NoSuchElementException();

        TreapNode<T> node = root;
        while (node.right != null) node = node.right;

        return node.key;
    }

    @Override
    public Comparator<? super T> comparator() {
        return (Comparator<T>) Comparable::compareTo;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new TreapIterator();
    }

    public class TreapIterator implements Iterator<T> {

        private final Stack<TreapNode<T>> stack = new Stack<>();
        TreapNode<T> currentNode = null;

        private void pushToLeftAndRight(TreapNode<T> node) {
            if (node != null) {
                stack.push(node);
                pushToLeftAndRight(node.left);
            }
        }

        private TreapIterator() {
            pushToLeftAndRight(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();

            TreapNode<T> node = stack.pop();
            currentNode = node;
            pushToLeftAndRight(node.right);

            return node.key;
        }

        @Override
        public void remove() {
            if (currentNode == null) throw new IllegalStateException();
            removeNode(currentNode);
            currentNode = null;
        }
    }

    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {

        if (toElement == null && fromElement == null)
            throw new IllegalArgumentException();
        if (toElement != null && fromElement.compareTo(toElement) >= 0)
            throw new IllegalArgumentException();

        return new SubTreap(fromElement, toElement, this);
    }

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {

        if (toElement == null) throw new IllegalArgumentException();

        return new SubTreap(null, toElement, this);
    }

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {

        if (fromElement == null) throw new IllegalArgumentException();

        return new SubTreap(fromElement, null, this);
    }

    private class SubTreap extends Treap<T> {
        final T bottom;
        final T up;
        Treap<T> treap;

        SubTreap(T bottom, T up, Treap<T> treap) {
            this.bottom = bottom;
            this.up = up;
            this.treap = treap;
        }

        private boolean check(T key) {

            return (bottom != null && up != null &&
                    key.compareTo(bottom) >= 0 && key.compareTo(up) < 0) ||
                    (bottom == null && key.compareTo(up) < 0) ||
                    (up == null && key.compareTo(bottom) >= 0);
        }


        @Override
        public int size() {
            if (treap == null) return 0;
            int count = 0;
            for (T key : treap)
                if (check(key)) count++;
            return count;
        }

        @Override
        public boolean contains(Object o) {

            @SuppressWarnings("unchecked")
            T t = (T) o;
            return check(t) && treap.contains(t);
        }

        @Override
        public boolean add(T key) {
            if (!check(key))
                throw new IllegalArgumentException();
            return treap.add(key);
        }

        @Override
        public boolean remove(Object o) {

            @SuppressWarnings("unchecked")
            T key = (T) o;
            if (!check(key))
                throw new IllegalArgumentException();
            return treap.remove(key);
        }

        @NotNull
        @Override
        public Iterator<T> iterator() {
            return new SubTreapIterator();
        }

        public class SubTreapIterator implements Iterator<T> {

            private final Stack<TreapNode<T>> stack = new Stack<>();
            TreapNode<T> currentNode = null;

            private void pushToLeftAndRight(TreapNode<T> node) {

                if (node.left != null) pushToLeftAndRight(node.left);

                if (check(node.key)) stack.push(node);

                if (node.right != null) pushToLeftAndRight(node.right);
            }

            private SubTreapIterator() {
                if (root != null) {
                    pushToLeftAndRight(root);
                    currentNode = stack.peek();
                }
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();

                TreapNode<T> node = stack.pop();
                currentNode = node;

                return node.key;
            }

            @Override
            public void remove() {
                if (currentNode == null) throw new IllegalStateException();
                treap.remove(currentNode.key);
            }
        }

        @Override
        public T first() {
            for (T key : treap)
                if (check(key)) return key;
            throw new NoSuchElementException();
        }

        @Override
        public T last() {
            T result = null;
            for (T key : treap)
                if (check(key)) result = key;
            if (result == null) throw new NoSuchElementException();
            return result;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T t : c)
            if (!this.add(t)) return false;
        return true;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        if (this.containsAll(c)) {
            for (Object t : this) {
                if (!c.contains(t)) remove(t);
            }
            return true;
        } else return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        if (this.containsAll(c)) {
            for (Object t : c) remove(t);
            return true;
        } else return false;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    public TreapNode<T> getRoot() {
        return root;
    }

    public void printTreap(FileWriter fw) throws IOException {
        printTreap(root, fw);
    }

    private void printTreap(TreapNode<T> root, FileWriter fw) throws IOException {


        List<List<String>> result = new ArrayList<>(); //stores all levels
        List<TreapNode<T>> level = new ArrayList<>(); //nodes on current level
        List<TreapNode<T>> next = new ArrayList<>();

        level.add(root);

        //amount of node on next level
        int count = 1;
        int widestStr = 0;

        while (count != 0) {
            //current line
            List<String> line = new ArrayList<>();
            count = 0;

            for (TreapNode<T> node : level) {
                if (node == null) {
                    line.add(null);
                    next.add(null);
                    next.add(null);

                } else {

                    String strToPrint = "[" + node.key + ":" + node.priority + "]";
                    line.add(strToPrint);
                    if (strToPrint.length() > widestStr) widestStr = strToPrint.length();

                    next.add(node.left);
                    next.add(node.right);

                    if (node.left != null) count++;
                    if (node.right != null) count++;
                }
            }

            if (widestStr % 2 == 1) widestStr++;

            result.add(line);

            List<TreapNode<T>> temp = level;
            level = next;
            next = temp;
            next.clear();
        }

        int numberForWidth = result.get(result.size() - 1).size() * (widestStr);
        for (int i = 0; i < result.size(); i++) {
            List<String> line = result.get(i);
            int numberForItems = (int) Math.floor(numberForWidth / 2.0) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    // "split" node
                    char ch = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            ch = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if ( line.get(j) != null) ch = '└';
                        }
                    }
                    fw.write(ch);

                    // print lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < numberForWidth - 1; k++) {
                            fw.write(" ");
                        }
                    } else {
                        for (int k = 0; k < numberForItems; k++) {
                            fw.write(j % 2 == 0 ? " " : "─");
                        }

                        fw.write(j % 2 == 0 ? "┌" : "┐");

                        for (int k = 0; k < numberForItems; k++) {
                            fw.write(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                fw.write("\n");
            }

            // print spaces and strings
            for (String str : line) {

                if (str == null) str = "";
                double temp = numberForWidth / 2.0 - str.length() / 2.0;
                int gap1 = (int) Math.ceil(temp);
                int gap2 = (int) Math.floor(temp);

                // a number
                for (int k = 0; k < gap1; k++) {
                    fw.write(" ");
                }

                fw.write(str);

                for (int k = 0; k < gap2; k++) {
                    fw.write(" ");

                }
            }
            fw.write("\n");
            numberForWidth /= 2;
        }
    }
}

