package StoredDataManager.DataStruct.rTree;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;


public class R_Tree<R extends Rectangle> {
    private final HeadTuple<R> head = new HeadTuple<>(this);

    private int size = 0;


    //for print to image

    public int[] getOffsetPoint() {
        int[] res = new int[2];
        res[0] = this.head.child.getMinX();
        res[1] = this.head.child.getMinY();
        return res;
    }

    public int[] getMapSize() {
        int[] res = new int[2];
        res[0] = this.head.child.getMaxX();
        res[1] = this.head.child.getMaxY();
        return res;
    }

    public int getDepth() {
        int depth = 0;
        Tuple<R> tmpHead = head.child;
        if (tmpHead.rectangleList.isEmpty()) {
            return depth;
        }
        while (!tmpHead.type.isLeaf()) {
            tmpHead = (Tuple<R>) tmpHead.rectangleList.toArray(new Rectangle[0])[0];
            depth++;
        }
        depth += 2;
        return depth;
    }

    public ConcurrentLinkedQueue<Rectangle> toQueue4Layer() {
        ConcurrentLinkedQueue<Rectangle> res = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Tuple<R>> tuples1 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Tuple<R>> tuples2 = new ConcurrentLinkedQueue<>();
        tuples1.add(head.child);

        while (!tuples1.isEmpty()) {
            for (Tuple<R> parent : tuples1) {
                res.add(parent);
                for (Tuple<R> child :
                        parent.rectangleList) {
                    if (parent.type.isNode()) {
                        res.add(child);
                    } else {
                        tuples2.add(child);
                    }
                }
            }
            ConcurrentLinkedQueue<Tuple<R>> tmp = tuples1;
            tuples1 = tuples2;
            tmp.clear();
            tuples2 = tmp;
            res.add(new Tuple<R>());
        }
        return res;
    }

    public boolean delete(R rectangle) {
        LinkedList<NodeTuple<R>> rectangleList = getNode(rectangle);
        if (rectangleList.size() == 1) {
            this.delete(rectangleList.getFirst());
        } else if (rectangleList.size() > 1) {
            for (NodeTuple<R> rectangle1 : rectangleList) {
                this.delete(rectangle1);
            }
        } else {
            return false;
        }
        return true;
    }

    private void delete(NodeTuple<R> rectangle) {
        if (!rectangle.type.isNode()) {
            throw new IllegalArgumentException("Fault parent");
        }

        LinkedList<R> resultLists = rectangle.parent.checkDelete(rectangle);

        if (!resultLists.isEmpty()) {
            size -= resultLists.size();
            this.insert(resultLists);
        }
    }



    //add
    public void insert(R r) {
        Tuple<R> tgr = chooseLeaf(r);
        tgr.add(r);

        size++;
    }

    public void insert(Collection<? extends R> collection) {
        for (R r : collection) {
            this.insert(r);
        }
    }

    private Tuple<R> chooseLeaf(R r) {
        Tuple<R> res = head.child;
        int minSquare = Integer.MAX_VALUE;
        while (!res.type.isLeaf()) {
            Tuple<R> minExtendTuple = null;
            for (Rectangle rectangle :
                    res.rectangleList) {
                if (RectangleUtils.getSquare(rectangle, r) - rectangle.getSquare() < minSquare) {
                    minExtendTuple = (Tuple<R>) rectangle;
                }
            }
            res = minExtendTuple;
        }

        return res;
    }

    //search

    public LinkedList<R> get(R rectangle) {
        LinkedList<R> res = new LinkedList<>();
        ConcurrentLinkedQueue<Tuple<R>> tuples1 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Tuple<R>> tuples2 = new ConcurrentLinkedQueue<>();
        tuples1.add(head.child);

        while (!tuples1.isEmpty()) {
            for (Tuple<R> parent : tuples1) {
                for (Tuple<R> child :
                        parent.rectangleList) {
                    if (child.type.isNode() && rectangle.isIntersection(child)) {
                        res.add(Type.toNode(child).getRectangle());
                    } else if (rectangle.isIntersection(child)) {
                        tuples2.add(child);
                    }
                }
            }
            ConcurrentLinkedQueue<Tuple<R>> tmp = tuples1;
            tuples1 = tuples2;
            tmp.clear();
            tuples2 = tmp;
        }
        return res;
    }

    public LinkedList<NodeTuple<R>> getNode(R rectangle) {
        LinkedList<NodeTuple<R>> res = new LinkedList<>();
        ConcurrentLinkedQueue<Tuple<R>> tuples1 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Tuple<R>> tuples2 = new ConcurrentLinkedQueue<>();
        tuples1.add(head.child);

        while (!tuples1.isEmpty()) {
            for (Tuple<R> parent : tuples1) {
                for (Tuple<R> child : parent.rectangleList) {
                    if (child.type.isNode() && child.isIntersection(rectangle)) {
                        res.add(Type.toNode(child));
                    } else if (rectangle.isIntersection(child)) {
                        tuples2.add(child);
                    }
                }
            }
            ConcurrentLinkedQueue<Tuple<R>> tmp = tuples1;
            tuples1 = tuples2;
            tmp.clear();
            tuples2 = tmp;
        }
        return res;
    }

    public boolean update(R prevNode,R newNode) {
        this.delete(prevNode);
        this.insert(newNode);
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ConcurrentLinkedQueue<Tuple<R>> tuples1 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Tuple<R>> tuples2 = new ConcurrentLinkedQueue<>();
        tuples1.add(head.child);

        while (!tuples1.isEmpty()) {
            for (Tuple<R> parent : tuples1) {
                sb.append(parent.getID()).append(": ").append(parent).append(" Parent:").append(parent.parent.getID()).append(" ");
                if(!parent.rectangleList.isEmpty()) {
                    sb.append("[");
                    for (Tuple<R> child : parent.rectangleList) {
                        sb.append(child.getID()).append(",");
                        tuples2.add(child);
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append("]");
                    sb.append("  ||  ");
                }
            }
            sb.append("\n");
            ConcurrentLinkedQueue<Tuple<R>> tmp = tuples1;
            tuples1 = tuples2;
            tmp.clear();
            tuples2 = tmp;
        }
        return sb.toString();
    }

    private static class HeadTuple<R extends Rectangle> extends Tuple<R> {
        private Tuple<R> child;

        private final R_Tree<R> tree;

        public HeadTuple(R_Tree<R> tree) {
            super("H0");
            super.type = Type.HEAD;
            this.tree = tree;
            initialize();
        }

        public Tuple<R> getChild() {
            return child;
        }

        public void setChild(Tuple<R> child) {
            this.child = child;
        }

        public Tuple<R> delete() {
            this.child = new Tuple<>(this);
            return this.child;
        }

        public void initialize() {
            this.child = new Tuple<>(this);
            this.child.setLeaf();
        }
    }

    private enum Type {
        HEAD,
        NORMAL,
        LEAF,
        NODE;

        public static <R extends Rectangle> HeadTuple<R> toHead(Tuple<R> tuple) {
            if(tuple instanceof HeadTuple){
                if(tuple.type != HEAD) {
                    tuple.type = HEAD;
                }
                return (HeadTuple<R>) tuple;
            }
            return null;
        }

        public static<R extends Rectangle> NodeTuple<R> toNode(Tuple<R> tuple) {
            if(tuple instanceof NodeTuple){
                if(tuple.type != NODE) {
                    tuple.type = NODE;
                }
                return (NodeTuple<R>) tuple;
            }
            return null;
        }

        public boolean isHead() {
            return this.equals(HEAD);
        }

        public boolean isLeaf() {
            return this.equals(LEAF);
        }

        public boolean isNode() {
            return this.equals(NODE);
        }

    }

    private static class NodeTuple<R extends Rectangle> extends Tuple<R> {
        private final R rectangle;

        private static int NUM = 0;

        public NodeTuple(Tuple<R> parent, R rectangle) {
            super(rectangle,parent);
            super.setNode();
            this.rectangle = rectangle;
                super.setID("N" + NUM++);
        }

        public R getRectangle() {
            return rectangle;
        }
    }

    private static class Tuple<R extends Rectangle> extends Rectangle {
        public static final short LENGTH = 4;

        public Type type = Type.NORMAL;
        Tuple<R> parent = null;

        private final ConcurrentLinkedQueue<Tuple<R>> rectangleList = new ConcurrentLinkedQueue<>();

        public Tuple() {
        }

        public Tuple(String ID) {
            this.setID(ID);
        }

        public Tuple(Tuple<R> parent) {
            setID();
            this.parent = parent;
        }

        public Tuple(String ID, Rectangle self) {
            super(self);
            this.setID(ID);
        }

        public Tuple(Rectangle element, Tuple<R> parent) {
            super(element);
            this.parent = parent;
            setID();
        }

        @Override
        protected void setCoordination() {
        }

        public void setLeaf() {
            this.type = Type.LEAF;
        }

        public void setNode() {
            this.type = Type.NODE;
        }

        public LinkedList<NodeTuple<R>> getAllNode() {
            ConcurrentLinkedQueue<Tuple<R>> tuples1 = new ConcurrentLinkedQueue<>();
            ConcurrentLinkedQueue<Tuple<R>> tuples2 = new ConcurrentLinkedQueue<>();
            tuples1.add(this);

            LinkedList<NodeTuple<R>> res = new LinkedList<>();
            while (!tuples1.isEmpty()) {
                for (Tuple<R> tuple : tuples1) {
                    for (Tuple<R> child : tuple.rectangleList) {
                        if (child.type.isNode()) {
                            res.add(Type.toNode(child));
                        } else {
                            tuples2.add(child);
                        }
                    }
                }
                ConcurrentLinkedQueue<Tuple<R>> tmp = tuples1;
                tuples1 = tuples2;
                tmp.clear();
                tuples2 = tmp;
            }
            return res;
        }


        private void checkDelete(Tuple<R> child, LinkedList<NodeTuple<R>> willBeDeleted) {
            if (this.type.isHead()) {
                HeadTuple<R> head_ = Type.toHead(this);
                head_.initialize();
                return ;
            }
            this.delete(child);
            if (this.rectangleList.size() < Tuple.LENGTH / 2) {
                willBeDeleted.addAll(this.getAllNode());
                this.parent.checkDelete(this, willBeDeleted);
            } else {
                this.shrunkRectangle(this.rectangleList);
            }
        }

        private LinkedList<R> checkDelete(Tuple<R> child) {
            LinkedList<NodeTuple<R>> resNodes = new LinkedList<>();
            checkDelete(child, resNodes);

            LinkedList<R> res = new LinkedList<>();
            for (NodeTuple<R> node: resNodes) {
                res.add(node.getRectangle());
            }
            return res;
        }

        public void delete(Tuple<R> tuple) {
            this.rectangleList.remove(tuple);
        }

        public String getID() {
            return super.getID();
        }



        private void setID() {
            this.setID("R" + getNum());
            numUpper();
        }

        public void add(R rectangle) {
            this.add(new NodeTuple<>(this, rectangle));
            this.setLeaf();
        }

        public void add(Tuple<R> tuple) {
            this.rectangleList.add(tuple);
            if (!checkSpill() && isOutBound(tuple)) {
                super.extendRectangle(tuple);
            }
        }



        public void add(Collection<? extends Tuple<R>> collection) {
            for (Tuple<R> r :
                    collection) {
                this.add(r);
            }
        }


        private boolean isOutBound(Rectangle rectangle) {
            return rectangle.getMinX() < super.getMinX() || rectangle.getMinY() < super.getMinY() || rectangle.getMaxX() > super.getMaxX() || rectangle.getMaxY() > super.getMaxY();
        }

        private boolean checkSpill() {
            boolean res = rectangleList.size() > Tuple.LENGTH;
            if (res) {
                split();
                if (!this.isHead()) {
                    parent.checkSpill();
                }
            }
            return res;
        }

        private LinkedList<Tuple<R>> findBestSplitMethod(Tuple<R>[] rectangles, Tuple<R> parent) {
            LinkedList<LinkedList<Tuple<R>>> tmp_res = new LinkedList<>();
            int minSquare = 0;

            for (int i = 0; i < rectangleList.size() - 1; i++) {
                for (int j = i + 1; j < rectangleList.size(); j++) {
                    LinkedList<Tuple<R>> allRects = new LinkedList<>(Arrays.asList(rectangles));
                    LinkedList<Tuple<R>> partRects = new LinkedList<>();
                    allRects.remove(rectangles[i]);
                    allRects.remove(rectangles[j]);

                    partRects.add(rectangles[i]);
                    partRects.add(rectangles[j]);
                    int tmpSquare = RectangleUtils.getSquare(partRects) + RectangleUtils.getSquare(allRects);
                    if (minSquare == 0 || tmpSquare < minSquare) {
                        minSquare = tmpSquare;
                        tmp_res.clear();
                        tmp_res.add(allRects);
                        tmp_res.add(partRects);
                    }
                }
            }
            LinkedList<Tuple<R>> res = new LinkedList<>();
            Tuple<R> tmp1 = new Tuple<R>(parent);
            tmp1.add(tmp_res.get(0));
            Tuple<R> tmp2 = new Tuple<R>(parent);
            tmp2.add(tmp_res.get(1));
            res.add(tmp1);
            res.add(tmp2);
            return res;
        }

        private boolean isHead() {
            return this.type == Type.HEAD;
        }

        private void split() {
            Tuple<R> parent_;
            if (this.parent.isHead()) {
                parent_ = Type.toHead(this.parent).delete();
            } else {
                parent_ = this.parent;
                this.parent.delete(this);
            }

            LinkedList<Tuple<R>> res = findBestSplitMethod(rectangleList.toArray(new Tuple[0]), parent_);
            if (this.type.isLeaf()) {
                for (Tuple<R> t : res) {
                    t.setLeaf();
                }
            }
            for (Tuple<R> parentTuple : res) {
                for (Tuple<R> tmpRect : parentTuple.rectangleList) {
                    tmpRect.parent = parentTuple;
                }
            }
            parent_.add(res);
        }

    }
}
