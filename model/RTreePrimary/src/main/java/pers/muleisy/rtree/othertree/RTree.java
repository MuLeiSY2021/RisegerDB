package pers.muleisy.rtree.othertree;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.apache.log4j.Logger;
import pers.muleisy.rtree.RTreeDao;
import pers.muleisy.rtree.rectangle.MBRectangle;
import pers.muleisy.rtree.rectangle.Rectangle;
import pers.muleisy.rtree.utils.JsonSerializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class RTree <R extends MBRectangle> implements RTreeDao<R> {
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

    public List<R> getElements() {
        LinkedList<R> res = new LinkedList<>();
        LinkedList<SubTree> tuples1 = new LinkedList<>();
        LinkedList<SubTree> tuples2 = new LinkedList<>();
        tuples1.add(root);
        if(!Leaf.class.isInstance(root)) {
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


    protected class SubTree extends MBRectangle {

        private LinkedList<SubTree> subTrees = new LinkedList<>();

        private SubTree parent;

        public SubTree() {
            super(RTree.this.threshold);
        }

        public SubTree(ByteBuf byteBuf,SubTree parent) {
            super(RTree.this.threshold);
            this.parent = parent;
            super.deserialize(byteBuf);
            int i = byteBuf.readInt();
            for (int j = 0; j < i; j++) {
                char r = byteBuf.readChar();
                if(r == 's') {
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

        public ByteBuf serialize() throws IOException {
            ByteBuf byteBuf = Unpooled.buffer();
            byteBuf.writeBytes(MBRectangle.serialize(this));
            byteBuf.writeInt(subTrees.size());
            if(Leaf.class.isInstance(this.subTrees.getFirst())) {
                for (SubTree subTree : subTrees) {
                    Leaf leaf = (Leaf) subTree;
                    byteBuf.writeChar('l');
                    byteBuf.writeBytes(leaf.serialize());
                }
            } else {
                for (SubTree subTree : subTrees) {
                    byteBuf.writeChar('s');
                    byteBuf.writeBytes(MBRectangle.serialize(subTree));
                    byteBuf.writeBytes(subTree.serialize());
                }
            }
            return byteBuf;
        }

        public LinkedList<SubTree> getSubTrees() {
            return subTrees;
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
                    if(Leaf.class.isInstance(parent)) {
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
            if(flg) {
                super.expand(subTree);
            }
            SubTree parent = this.getParent();
            if(parent != null && flg) {
                parent.adjust(this);
            }
        }

        public final void adjust(Rectangle... subTrees) {
            boolean flg = super.expandAll(subTrees);
            SubTree parent = this.getParent();
            if(parent != null && flg) {
                parent.adjust(this);
            }
        }

        public void adjust(Collection<? extends Rectangle> subTrees) {
            boolean flg = super.expandAll(subTrees);
            SubTree parent = this.getParent();
            if(parent != null && flg) {
                parent.adjust(this);
            }
        }

        public void setSubTrees(LinkedList<SubTree> subTrees) {
            this.subTrees = subTrees;
            super.adjustAll(subTrees);
        }

        public SubTree getParent() {
            return parent;
        }

        public void delete(SubTree subTree) {
            this.subTrees.remove(subTree);
            subTree.parent = null;
        }

        public boolean isTooFew() {
            return this.subTrees.size() < M >> 1;
        }

        public boolean isOverFlow() {
            return this.subTrees.size() > M;
        }

        @Override
        public String toString() {
            return super.toString().substring(1,super.toString().length() - 1) + "|";
        }
    }

    protected class Leaf extends SubTree{
        private final LinkedList<R> elements = new LinkedList<>();

        public Leaf(ByteBuf byteBuf, SubTree parent) {
            super();
            super.parent = parent;
            super.deserialize(byteBuf);
            int i = byteBuf.readInt();
            Gson g = new Gson();
            try {
                Class<? extends R> clazz = (Class<? extends R>) Class.forName(byteBuf
                        .readCharSequence(byteBuf.readInt(), StandardCharsets.UTF_8)
                        .toString());

                for (int j = 0; j < i; j++) {
                    this.elements.add(g.fromJson(byteBuf.readCharSequence(byteBuf.readInt(), StandardCharsets.UTF_8).toString(),
                            clazz));
                }
            } catch (ClassNotFoundException e) {
                Logger.getLogger(this.getClass()).error(e.getMessage());
            }
        }

        // Serialize a Leaf object
        public ByteBuf serialize() throws IOException {
            ByteBuf byteBuf = Unpooled.buffer();
            byteBuf.writeBytes(MBRectangle.serialize(this));
            byteBuf.writeInt(elements.size());
            byteBuf.writeBytes(RTree.this.getClass()
                    .getTypeParameters()[0].getTypeName()
                    .getBytes(StandardCharsets.UTF_8));
            for (R r:elements) {
                byteBuf.writeBytes(JsonSerializer.serialize(r));
            }
            return byteBuf;
        }

        public Leaf() {
        }

        public Leaf(Collection<? extends R> elements) {
            this.elements.addAll(elements);
            super.adjust(elements);
        }

        public LinkedList<R> getElements() {
            return elements;
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
            for (R e:elements) {
                if(e.intersects(rectangle)) {
                    result.add(e);
                }
            }
            return result;
        }

        public R strictGet(Rectangle rectangle) {
            for (R e:elements) {
                if(e.match(rectangle)) {
                    return e;
                }
            }
            return null;
        }

        public void delete(Rectangle rectangle) {
            this.elements.remove(rectangle);
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

    protected SubTree root = new Leaf();

    private final int M;

    private final int m;

    protected final double threshold;

    public RTree(int nodeSize,double threshold) {
        this.M = nodeSize;
        this.m  = (int) (M * 0.4);
        this.threshold = threshold;
    }

    @Override
    public void insert(R rectangle) {
        rectangle.setThreshold(threshold);
    }

    @Override
    public void insertAll(List<? extends R> rects) {
        for (R r :rects) {
            r.setThreshold(threshold);
        }
    }

    @Override
    public int delete(Rectangle rectangle) {
        //Find node containing record.
        List<Leaf> leafList = findLeaf(rectangle);

        if(leafList.size() == 0) {
            return 0;
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
        return leafList.size();
    }

    @Override
    public void deleteStrict(Rectangle rectangle) {
        //Find node containing record.
        Leaf leaf = strictFindLeaf(rectangle);
        if (leaf == null) {
            return;
        }
        //[Delete record.]
        leaf.delete(rectangle);
        condenseTree(leaf);

        //[Shorten tree.]
        if (root.getSubTrees().size() == 1) {
            root = root.getSubTrees().get(0);
        }
    }

    protected List<Leaf> findLeaf(Rectangle rect) {
        LinkedList<Leaf> res = new LinkedList<>();
        LinkedList<SubTree> tuples1 = new LinkedList<>();
        LinkedList<SubTree> tuples2 = new LinkedList<>();
        tuples1.add(root);

        while (!tuples1.isEmpty()) {
            for (SubTree parent : tuples1) {
                for (SubTree child : parent.getSubTrees()) {
                    if (child.intersects(rect)) {
                        if(Leaf.class.isInstance(child)) {
                            res.add((Leaf) child);
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
        return res;
    }

    protected Leaf strictFindLeaf(Rectangle rect) {
        LinkedList<SubTree> tuples1 = new LinkedList<>();
        LinkedList<SubTree> tuples2 = new LinkedList<>();
        tuples1.add(root);

        while (!tuples1.isEmpty()) {
            for (SubTree parent : tuples1) {
                for (SubTree child : parent.getSubTrees()) {
                    if (Leaf.class.isInstance(child) && child.inner(rect)) {
                        return (Leaf) child;
                    } else if (child.inner(rect)) {
                        tuples2.add(child);
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
        while (!node.equals(root)) {
            //[Find parent entry.]
            parent = node.getParent();

            if (node.isTooFew()) {
                //[Eliminate under-full node.]
                parent.delete(node);
                for (Leaf l:node.getLeave()) {
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
        while (!( subtreeList1.get(0).parent == null)) {
            for (SubTree node : subtreeList1) {
                //[Find parent entry.]
                parent = node.getParent();

                if (node.isTooFew()) {
                    //[Eliminate under-full node.]
                    parent.delete(node);
                    for (Leaf l:node.getLeave()) {
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

//    public List<MBRectangle> getAllNode4Test() {
//        LinkedList<MBRectangle> res = new LinkedList<>();
//        LinkedList<SubTree> tuples1 = new LinkedList<>();
//        LinkedList<SubTree> tuples2 = new LinkedList<>();
//        LinkedList<Leaf> leaves = new LinkedList<>();
//        tuples1.add(root);
//        res.add(root);
//        res.add(new TestSpace());
//        if(!(Leaf.class.isInstance(root))) {
//            while (!tuples1.isEmpty()) {
//                for (SubTree parent : tuples1) {
//                    for (SubTree child : parent.getSubTrees()) {
//                        if (Leaf.class.isInstance(child)) {
//                            leaves.add((Leaf) child);
//                        } else {
//                            tuples2.add(child);
//                        }
//                        res.add(child);
//                    }
//                }
//                LinkedList<SubTree> tmp = tuples1;
//                tuples1 = tuples2;
//                tmp.clear();
//                tuples2 = tmp;
//                if (!tuples1.isEmpty()) {
//                    res.add(new TestSpace());
//                }
//            }
//            res.add(new TestSpace());
//        } else {
//            leaves.add((Leaf) root);
//        }
//        for (Leaf leaf:leaves) {
//            res.addAll(leaf.getElements());
//        }
//        return res;
//    }
//
//    class TestSpace extends MBRectangle {
//        public TestSpace() {
//            super(threshold);
//        }
//
//        @Override
//        public void initBMRCoords() {
//            throw new UnsupportedOperationException();
//        }
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        List<MBRectangle> allNode = getAllNode4Test();
//        int i = 1;
//        HashMap<MBRectangle,Integer> hashMap = new HashMap<>();
//        for (MBRectangle node:allNode) {
//            hashMap.put(node,i);
//            i++;
//
//            if(node instanceof RTree.TestSpace) {
//                sb.append("\n");
//            }else if (node instanceof RTree<?>.SubTree) {
//                SubTree subTree = (SubTree)node;
//                if (node instanceof RTree<?>.Leaf) {
//                    sb.append("|L:id=").append(hashMap.get(node)).append(",");
//                } else {
//                    sb.append("|ST:id=").append(hashMap.get(node)).append(",");
//                }
//                sb.append(subTree.toString(), 0, subTree.toString().length() - 1).append(",P:id=").append(hashMap.get(((SubTree) node).parent)).append("|");
//            } else {
//                sb.append("|R:id=").append(hashMap.get(node)).append(",").append(node.toString().substring(1));
//            }
//
//        }
//        return sb.toString();
//    }

    //------------Test----------------//



    public int[] getMapSize() {
        return new int[]{this.root.maxX().intValue(), this.root.maxY().intValue()};
    }



    protected int M() {
        return M;
    }

    @Override
    public int getDeep() {
        int deep = 1;
        if(Leaf.class.isInstance(root)&&((Leaf) root).getElements().isEmpty()) {
            return 0;
        }
        SubTree root = this.root;
        while (!root.getSubTrees().isEmpty()) {
            root = root.getSubTrees().get(0);
            deep++;
        }
        return deep;
    }

    public ByteBuf serialize() throws IOException {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeInt(this.M);
        buffer.writeDouble(threshold);
        //添加root
        buffer.writeBytes(root.serialize());
        return buffer;
    }

    public static RTree<?> deserializeStar(ByteBuf buffer) {
        int M = buffer.readInt();
        double threshold = buffer.readDouble();
        RTree<?> tree = new RStarTree<>(M,threshold);
        tree.deserializeSubTree(buffer);
        return tree;
    }

    public static RTree<?> deserializeSTR(ByteBuf buffer) {
        int M = buffer.readInt();
        double threshold = buffer.readDouble();
        RTree<?> tree = new STRRTree<>(M,threshold);
        tree.deserializeSubTree(buffer);
        return tree;
    }

    // Deserialize a SubTree object
    public void deserializeSubTree(ByteBuf byteBuf) {
        this.root = new SubTree(byteBuf,null);
    }
}
