package pers.muleisy.rtree.othertree;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Getter;
import org.apache.log4j.Logger;
import pers.muleisy.rtree.RTreeDao;
import pers.muleisy.rtree.rectangle.MBRectangle;
import pers.muleisy.rtree.rectangle.Rectangle;
import pers.muleisy.rtree.test.TestRectangle;
import pers.muleisy.rtree.utils.JsonSerializer;

import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class RTree<R extends MBRectangle> implements RTreeDao<R> {

    protected final double threshold;
    private final int M;

    private final String saveClassName;

    protected SubTree root;

    public RTree(int nodeSize, double threshold, Class<? extends R> clazz) {
        this.M = nodeSize;
        this.threshold = threshold;
        this.root = new Leaf();
        this.saveClassName = clazz.getName();
    }

    public RTree(int nodeSize, double threshold, String clazz) {
        this.M = nodeSize;
        this.threshold = threshold;
        this.saveClassName = clazz;
    }

    public static String getStringBuilder(MBRectangle rectangles, HashMap<MBRectangle, Integer> hashMap) {
        StringBuilder sb = new StringBuilder();
        if (rectangles instanceof RTree<?>.SubTree) {
            RTree<?>.SubTree subTree = (RTree<?>.SubTree) rectangles;
            if (rectangles instanceof RTree<?>.Leaf) {
                sb.append("|L").append(hashMap.get(rectangles)).append(",");
            } else {
                sb.append("|ST").append(hashMap.get(rectangles)).append(",");
            }
            sb.append(subTree.toString(), 0, subTree.toString().length() - 1).append(",P").append(hashMap.get(((RTree<?>.SubTree) rectangles).getParent())).append("|");
        } else {
            sb.append("|R").append(hashMap.get(rectangles)).append(",").append(rectangles.toString().substring(1));
        }
        return sb.toString();
    }

    public static RTree<?> deserializeStar(ByteBuf buffer) throws Exception {
        int M = buffer.readInt();
        double threshold = buffer.readDouble();
        int size = buffer.readInt();
        byte[] bytes = new byte[size];
        buffer.readBytes(bytes, 0, size);
        String saveClassName = new String(bytes, StandardCharsets.UTF_8);
        RTree<?> tree = new RStarTree<>(M, threshold, saveClassName);
        tree.deserializeSubTree(buffer);
        return tree;
    }

    public static RTree<?> deserializeSTR(ByteBuf buffer) throws Exception {
        int M = buffer.readInt();
        double threshold = buffer.readDouble();
        RTree<?> tree = new STRRTree<>(M, threshold, null);
        tree.deserializeSubTree(buffer);
        return tree;
    }

    public ByteBuf serialize() throws Throwable {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeInt(this.M);
        buffer.writeDouble(threshold);
        byte[] bytes = saveClassName.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        //添加root
        if (Leaf.class.isInstance(root)) {
            buffer.writeChar('l');
            Leaf root_ = (Leaf) root;
            root_.serialize(buffer);
        } else {
            buffer.writeChar('s');
            root.serialize(buffer);
        }
        return buffer;
    }

    // Deserialize a SubTree object
    public void deserializeSubTree(ByteBuf byteBuf) throws Exception {
        char c = byteBuf.readChar();
        if (c == 'r') {
            this.root = new SubTree(byteBuf, null);
        } else {
            this.root = new Leaf(byteBuf, null);
        }
    }

    public List<R> getElements() {
        LinkedList<R> res = new LinkedList<>();
        LinkedList<SubTree> tuples1 = new LinkedList<>();
        LinkedList<SubTree> tuples2 = new LinkedList<>();
        tuples1.add(root);
        if (!Leaf.class.isInstance(root)) {
            while (!tuples1.isEmpty()) {
                for (SubTree parent : tuples1) {
                    for (SubTree child : parent.getSubTrees()) {
                        if (Leaf.class.isInstance(child)) {
                            Leaf leaf = (Leaf) child;
                            res.addAll(leaf.elements);
                        } else {
                            tuples2.add(child);
                        }
                    }
                }
                LinkedList<SubTree> tmp = tuples1;
                tuples1 = tuples2;
                tmp.clear();
                tuples2 = tmp;
            }
        } else {
            Leaf leaf = (Leaf) root;
            res.addAll(leaf.elements);
        }
        return res;
    }

    @Override
    public void insert(R rectangle) {
        rectangle.setThreshold(threshold);
    }

    @Override
    public void insertAll(List<? extends R> rects) {
        for (R r : rects) {
            r.setThreshold(threshold);
        }
    }

    @Override
    public void delete(Rectangle rectangle) {
        //Find node containing record.
        List<Leaf> leafList = findLeaf(rectangle);

        if (leafList.size() == 0) {
            return;
        }

        //[Delete record.]
        for (Leaf leaf : leafList) {
            leaf.delete(rectangle);
        }
        condenseTree(leafList);

        //[Shorten tree.]
        if (root.getSubTrees().size() == 1) {
            root = root.getSubTrees().get(0);
        }
    }

    @Override
    public int deleteStrict(Rectangle rectangle) {
        if (!Leaf.class.isInstance(root) && root.isEmpty() ||
                Leaf.class.isInstance(root) && ((Leaf) root).isLeafEmpty()) {
            return 0;
        }

        //Find node containing record.
        Leaf leaf = strictFindLeaf(rectangle);
        if (leaf == null) {
            return 0;
        }
        //[Delete record.]
        leaf.delete(rectangle);
        condenseTree(leaf);

        //[Shorten tree.]
        if (root.getSubTrees().size() == 1) {
            root = root.getSubTrees().get(0);
        }
        return 1;
    }

    protected List<Leaf> findLeaf(Rectangle rect) {
        LinkedList<Leaf> res = new LinkedList<>();
        LinkedList<SubTree> tuples1 = new LinkedList<>();
        LinkedList<SubTree> tuples2 = new LinkedList<>();
        tuples1.add(root);

        while (!tuples1.isEmpty()) {
            for (SubTree parent : tuples1) {
                if (Leaf.class.isInstance(parent)) {
                    for (Leaf child : parent.getLeave()) {
                        if (child.intersects(rect)) {
                            res.add(child);
                        } else if (child.intersects(rect)) {
                            tuples2.add(child);
                        }
                    }
                } else {
                    for (SubTree child : parent.getSubTrees()) {
                        if (Leaf.class.isInstance(child) && child.intersects(rect)) {
                            res.add((Leaf) child);
                        } else if (child.intersects(rect)) {
                            tuples2.add(child);
                        }
                    }
                }
            }
            LinkedList<SubTree> tmp = tuples1;
            tuples1 = tuples2;
            tmp.clear();
            tuples2 = tmp;
        }
        return res;
    }

    protected Leaf strictFindLeaf(Rectangle rect) {
        LinkedList<SubTree> tuples1 = new LinkedList<>();
        LinkedList<SubTree> tuples2 = new LinkedList<>();
        tuples1.add(root);

        while (!tuples1.isEmpty()) {
            for (SubTree parent : tuples1) {
                if (Leaf.class.isInstance(parent)) {
                    for (Leaf child : parent.getLeave()) {
                        if (child.match(rect)) {
                            return child;
                        } else if (child.match(rect)) {
                            tuples2.add(child);
                        }
                    }
                } else {
                    for (SubTree child : parent.getSubTrees()) {
                        if (Leaf.class.isInstance(child) && child.inner(rect)) {
                            return (Leaf) child;
                        } else if (child.inner(rect)) {
                            tuples2.add(child);
                        }
                    }
                }
            }
            LinkedList<SubTree> tmp = tuples1;
            tuples1 = tuples2;
            tmp.clear();
            tuples2 = tmp;
        }
        return null;
    }

    private void condenseTree(Leaf leaf) {
        if (leaf == null || !leaf.isTooFew()) {
            return;
        }
        SubTree node = leaf;
        List<R> eliminatedNodes = new LinkedList<>();
        SubTree parent;
        while (node != null && !node.equals(root)) {
            //[Find parent entry.]
            parent = node.getParent();

            if (node.isTooFew()) {
                //[Eliminate under-full node.]
                parent.delete(node);
                for (Leaf l : node.getLeave()) {
                    eliminatedNodes.addAll(l.getElements());
                }
            } else {
                //[Adjust covering rectangle.]
                if (Leaf.class.isInstance(node)) {
                    Leaf tmp = (Leaf) node;
                    tmp.adjustAll(tmp.getElements());
                } else {
                    node.adjustAll(node.getSubTrees());
                }

            }

            //[Move up one level in tree.]
            node = node.getParent();
        }
        insertAll(eliminatedNodes);
    }

    private void condenseTree(List<Leaf> leafList) {
        List<SubTree> subtreeList1 = new LinkedList<>(leafList);
        List<SubTree> subtreeList2 = new LinkedList<>();
        List<R> eliminatedNodes = new LinkedList<>();
        SubTree parent;
        while (!(subtreeList1.get(0).parent == null)) {
            for (SubTree node : subtreeList1) {
                //[Find parent entry.]
                parent = node.getParent();

                if (node.isTooFew()) {
                    //[Eliminate under-full node.]
                    parent.delete(node);
                    for (Leaf l : node.getLeave()) {
                        eliminatedNodes.addAll(l.getElements());
                    }
                } else {
                    //[Adjust covering rectangle.]
                    if (Leaf.class.isInstance(node)) {
                        Leaf tmp = (Leaf) node;
                        tmp.adjustAll(tmp.getElements());
                    } else {
                        node.adjustAll(node.getSubTrees());
                    }

                }

                //[Move up one level in tree.]
                if (!subtreeList2.contains(parent)) {
                    subtreeList2.add(parent);
                }
            }
            subtreeList1 = subtreeList2;
            subtreeList2 = new LinkedList<>();

        }
        insertAll(eliminatedNodes);
    }

    @Override
    public List<R> search(Rectangle rectangle) {
        LinkedList<R> res = new LinkedList<>();
        List<Leaf> leafList = findLeaf(rectangle);
        for (Leaf leaf : leafList) {
            res.addAll(leaf.get(rectangle));
        }
        return res;
    }

    @Override
    public R selectStrict(Rectangle rectangle) {
        Leaf leaf = strictFindLeaf(rectangle);
        if (leaf == null) {
            return null;
        }
        return leaf.strictGet(rectangle);
    }


    public int[] getMapSize() {
        return new int[]{this.root.maxX().intValue(), this.root.maxY().intValue()};
    }


    protected int M() {
        return M;
    }

    @Override
    public int getDeep() {
        int deep = 1;
        if (Leaf.class.isInstance(root) && ((Leaf) root).getElements().isEmpty()) {
            return 0;
        }
        SubTree root = this.root;
        while (!root.getSubTrees().isEmpty()) {
            root = root.getSubTrees().get(0);
            deep++;
        }
        return deep;
    }

    public MBRectangle getSquareRect() {
        return new TestRectangle(
                this.root.minX(),
                this.root.maxX(),
                this.root.minY(),
                this.root.maxY(),
                this.threshold);
    }

    @Override
    public boolean willShrunk(R rect) throws CloneNotSupportedException {
        RTree tree = (RTree) this.clone();
        tree.deleteStrict(rect);
        return this.getSquareRect().equals(tree.getSquareRect());
    }

    @Getter
    protected class SubTree extends MBRectangle {

        private LinkedList<SubTree> subTrees = new LinkedList<>();

        private SubTree parent;

        public SubTree() {
            super(RTree.this.threshold);
        }

        public SubTree(ByteBuf byteBuf, SubTree parent) throws Exception {
            super(RTree.this.threshold);
            this.parent = parent;
            super.deserialize(byteBuf);
            int i = byteBuf.readInt();
            for (int j = 0; j < i; j++) {
                char r = byteBuf.readChar();
                if (r == 's') {
                    subTrees.add(new SubTree(byteBuf, this));
                } else {
                    subTrees.add(new Leaf(byteBuf, this));
                }
            }
        }

        public SubTree(Collection<? extends SubTree> subTrees) {
            super(RTree.this.threshold);
            addAll(subTrees);
        }

        public void serialize(ByteBuf byteBuf) throws Throwable {
            MBRectangle.serialize(byteBuf, this);
            byteBuf.writeInt(subTrees.size());
            if (this.subTrees.isEmpty()) {
                return;
            }
            if (Leaf.class.isInstance(this.subTrees.getFirst())) {
                for (SubTree subTree : subTrees) {
                    Leaf leaf = (Leaf) subTree;
                    byteBuf.writeChar('l');
                    leaf.serialize(byteBuf);
                }
            } else {
                for (SubTree subTree : subTrees) {
                    byteBuf.writeChar('s');
                    subTree.serialize(byteBuf);
                }
            }
        }

        public void setSubTrees(LinkedList<SubTree> subTrees) {
            this.subTrees = subTrees;
            super.adjustAll(subTrees);
        }

        public void add(SubTree subTree) {
            this.subTrees.add(subTree);
            subTree.parent = this;
            this.adjust(subTree);
        }

        public List<Leaf> getLeave() {
            List<Leaf> leaves = new LinkedList<>();
            LinkedList<SubTree> tuples1 = new LinkedList<>();
            LinkedList<SubTree> tuples2 = new LinkedList<>();
            tuples1.add(this);

            while (!tuples1.isEmpty()) {
                for (SubTree parent : tuples1) {
                    if (Leaf.class.isInstance(parent)) {
                        leaves.add((Leaf) parent);
                    } else {
                        for (SubTree child : parent.getSubTrees()) {
                            if (Leaf.class.isInstance(child)) {
                                leaves.add((Leaf) child);
                            } else {
                                tuples2.add(child);
                            }
                        }
                    }
                }
                LinkedList<SubTree> tmp = tuples1;
                tuples1 = tuples2;
                tmp.clear();
                tuples2 = tmp;
            }
            return leaves;
        }

        protected void condense() {
            super.adjustAll(this.subTrees);
            if (this.parent != null) {
                this.parent.condense();
            }
        }

        public void addAll(Collection<? extends SubTree> subTrees) {
            this.subTrees.addAll(subTrees);
            for (SubTree subTree : subTrees) {
                subTree.parent = this;
            }
            this.adjust(subTrees);
        }

        @SafeVarargs
        public final void addAll(SubTree... subTrees) {
            Collections.addAll(this.subTrees, subTrees);
            for (SubTree subTree : subTrees) {
                subTree.parent = this;
            }
            this.adjust(subTrees);
        }

        public final void adjust(Rectangle subTree) {
            boolean flg = willBeExpand(subTree);
            if (flg) {
                super.expand(subTree);
            }
            SubTree parent = this.getParent();
            if (parent != null && flg) {
                parent.adjust(this);
            }
        }

        public final void adjust(Rectangle... subTrees) {
            boolean flg = super.expandAll(subTrees);
            SubTree parent = this.getParent();
            if (parent != null && flg) {
                parent.adjust(this);
            }
        }

        public void adjust(Collection<? extends Rectangle> subTrees) {
            boolean flg = super.expandAll(subTrees);
            SubTree parent = this.getParent();
            if (parent != null && flg) {
                parent.adjust(this);
            }
        }

        public void delete(SubTree subTree) {
            if (this.isEmpty()) {
                return;
            }
            ListIterator<SubTree> iterator = this.subTrees.listIterator();

            for (SubTree e = iterator.next(); ; e = iterator.next()) {
                if (e.match(subTree)) {
                    iterator.remove();
                }

                if (!iterator.hasNext()) {
                    break;
                }
            }
            subTree.parent = null;
            condense();
        }

        public boolean isTooFew() {
            return this.subTrees.size() < M >> 1;
        }

        public boolean isOverFlow() {
            return this.subTrees.size() > M;
        }

        @Override
        public String toString() {
            return super.toString().substring(1, super.toString().length() - 1) + "|";
        }

        public boolean isEmpty() {
            return this.subTrees.isEmpty();
        }
    }

    @Getter
    protected class Leaf extends SubTree {
        private final LinkedList<R> elements = new LinkedList<>();


        public Leaf() {
            super();
        }

        public Leaf(Collection<? extends R> elements) {
            this.elements.addAll(elements);
            super.adjust(elements);
        }

        public Leaf(ByteBuf byteBuf, SubTree parent) throws Exception {
            super();
            super.parent = parent;
            super.deserialize(byteBuf);
            int i = byteBuf.readInt();
            try {
                Class<? extends R> clazz = (Class<? extends R>)
                        Class.forName(saveClassName);
                for (int j = 0; j < i; j++) {
                    byte[] bytes = new byte[byteBuf.readInt()];
                    byteBuf.readBytes(bytes);
                    this.elements.add(JsonSerializer.deserialize(bytes, clazz));
                }
            } catch (Exception e) {
                Logger.getLogger(this.getClass()).error(e.getMessage());
                throw e;
            }
        }

        public boolean isLeafEmpty() {
            return this.elements.isEmpty();
        }

        // Serialize a Leaf object
        public void serialize(ByteBuf byteBuf) throws Throwable {
            MBRectangle.serialize(byteBuf, this);
            byteBuf.writeInt(elements.size());
            for (R r : elements) {
                byte[] bytes = JsonSerializer.serialize(r);
                byteBuf.writeInt(bytes.length);
                byteBuf.writeBytes(bytes);
            }
        }

        public void add(R element) {
            this.elements.add(element);
            super.adjust(element);
        }

        public void add(Collection<? extends R> elements) {
            this.elements.addAll(elements);
            super.adjust(elements);
        }

        public List<R> get(Rectangle rectangle) {
            List<R> result = new LinkedList<>();
            for (R e : elements) {
                if (e.intersects(rectangle)) {
                    result.add(e);
                }
            }
            return result;
        }

        public R strictGet(Rectangle rectangle) {
            for (R e : elements) {
                if (e.match(rectangle)) {
                    return e;
                }
            }
            return null;
        }

        public void delete(Rectangle rectangle) {
            ListIterator<R> iterator = this.elements.listIterator();

            for (R e = iterator.next(); ; e = iterator.next()) {
                if (e.match(rectangle)) {
                    iterator.remove();
                }

                if (!iterator.hasNext()) {
                    break;
                }
            }
            super.adjustAll(this.elements);
            if (super.getParent() != null) {
                super.getParent().condense();
            }
        }

        @Override
        public boolean isTooFew() {
            return this.elements.size() < M >> 1;
        }

        @Override
        public boolean isOverFlow() {
            return this.elements.size() > M;
        }


    }
}
