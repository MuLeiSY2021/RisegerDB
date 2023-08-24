package pers.muleisy.rtree.othertree;

import pers.muleisy.rtree.rectangle.CommonRectangle;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RStarTree<R extends CommonRectangle> extends RTree<R> {

    public RStarTree(int nodeSize,double threshold) {
        super(nodeSize,threshold);
    }

    @Override
    public void insert(R rectangle) {
        super.insert(rectangle);
        ConcurrentHashMap<SubTree, Boolean> roundTable = new ConcurrentHashMap<>();
        Leaf subTree = chooseSubtree(rectangle);
        subTree.add(rectangle);

        if (subTree.isOverFlow()) {
            overflowTreatment(subTree, roundTable);
        }
    }

    @Override
    public void insertAll(List<? extends R> rects) {
        super.insertAll(rects);
        for (R rect : rects) {
            this.insert(rect);
        }
    }

    private boolean isLeafParent(SubTree subTree) {
        return Leaf.class.isInstance(subTree.getSubTrees().get(0));
    }

    public int compareOverlap(CommonRectangle rect, CommonRectangle tuple1, CommonRectangle tuple2, List<? extends CommonRectangle> tuples) {
        int res = Double.compare(getOverlap(rect, tuple1, tuples),getOverlap(rect, tuple1, tuples));

        if (res == 0) {
            res = compareAreaEnlargement(rect, tuple1, tuple2);
        }
        return res;
    }


    private Leaf chooseSubtree(R rect) {
        SubTree root = this.root;

        while (!(Leaf.class.isInstance(root))) {
            if (isLeafParent(root)) {
                final List<SubTree> subtrees = root.getSubTrees();
                Optional<SubTree> tuple = root.getSubTrees().stream().min((x,y) -> compareOverlap(rect,x,y,subtrees));
                if (tuple.isPresent()) {
                    root = tuple.get();
                } else {
//                    System.out.println("is Leaf:" + isLeafParent(root));
                    throw new IllegalArgumentException("No such child");
                }
            } else {
                Optional<SubTree> tuple = root.getSubTrees().stream().min((x,y) -> compareAreaEnlargement(rect,x,y));
                if (tuple.isPresent()) {
                    root = tuple.get();
                } else {
//                    System.out.println("is Leaf:" + isLeafParent(root));
                    throw new IllegalArgumentException("No such child");
                }
            }
        }
        return (Leaf) root;
    }

    private void overflowTreatment(SubTree subtree, ConcurrentHashMap<SubTree, Boolean> roundTable) {
        if (subtree == null || !subtree.isOverFlow()) {
            return;
        }
        SubTree parent = subtree.getParent();
        if (!this.root.equals(subtree) && roundTable.containsKey(subtree)) {
            reInsert(subtree);
        } else {
            roundTable.put(subtree, true);
            split(subtree);
            overflowTreatment(parent, roundTable);
        }

    }

    private void split(SubTree subtree) {
        if (Leaf.class.isInstance(subtree)) {
            Leaf subR = (Leaf) subtree;

            Object[] splitSet = split(subR.getElements());
            SubTree parent = subtree.getParent();
            if (parent == null) {
                this.root = new SubTree();
                parent = root;
            }
            parent.delete(subtree);
            parent.addAll(new Leaf((Collection<? extends R>) splitSet[0]),new Leaf((Collection<? extends R>) splitSet[1]));
        } else {
            Object[] splitSet = split(subtree.getSubTrees());
            SubTree parent = subtree.getParent();
            if (parent == null) {
                this.root = new SubTree();
                parent = root;
            }
            parent.delete(subtree);
            parent.addAll(new SubTree((Collection<? extends RTree<R>.SubTree>) splitSet[0]),new SubTree((Collection<? extends RTree<R>.SubTree>) splitSet[1]));

        }
    }

    private void reInsert(SubTree subtree) {
        List<List<? extends CommonRectangle>> reinsertSet = getReinsertSet(subtree, subtree.getSubTrees());
        subtree.setSubTrees((LinkedList<SubTree>) reinsertSet.get(1));
        insertAll((List<? extends R>) reinsertSet.get(1));
    }

    public Double getOverlap(CommonRectangle rect, CommonRectangle tuple1, List<? extends CommonRectangle> tuples) {
        double overlap = 0;
        CommonRectangle newTuple = new CommonRectangle(tuple1, rect);
        for (CommonRectangle tuple : tuples) {
            overlap += newTuple.overlap(tuple).area();
        }
        return overlap;
    }

    public int compareAreaEnlargement(CommonRectangle rect, CommonRectangle tuple1, CommonRectangle tuple2) {
        int res = Double.compare(getAreaEnlargement(rect, tuple1),getAreaEnlargement(rect, tuple2));
        if (res == 0) {
            res = compareAreaWithRect(rect, tuple1, tuple2);
        }
        return res;
    }

    public Double getAreaEnlargement(CommonRectangle rect, CommonRectangle tuple) {
        return new CommonRectangle(tuple, rect).area()- tuple.area();
    }

    public int compareAreaWithRect(CommonRectangle rect, CommonRectangle tuple1, CommonRectangle tuple2) {
        return Double.compare(new CommonRectangle(tuple1, rect).area(),new CommonRectangle(tuple2, rect).area());
    }

    //-------------------Split---------------------//
    public int M = RStarTree.this.M();

    public int m = (int) (M * 0.4);

    private Double getAreaValue(List<? extends CommonRectangle> group1,
                   List<? extends CommonRectangle> group2) {
        return new CommonRectangle(group1).area()+new CommonRectangle(group2).area();
    }

    private Double getMarginValue(List<? extends CommonRectangle> group1,
                     List<? extends CommonRectangle> group2) {
        return new CommonRectangle(group1).margin()+new CommonRectangle(group2).margin();
    }

    private Double getOverlapValue(List<? extends CommonRectangle> group1,
                      List<? extends CommonRectangle> group2) {
        return new CommonRectangle(group1).overlap(new CommonRectangle(group2)).area();
    }

    protected enum Axis {
        X,Y
    }

    private Axis chooseSplitAxis(List<? extends CommonRectangle> tuples) {
        double xValue = 0,yValue = 0;
        //from x
        tuples = sortByAxis(tuples,Axis.X,false);
        for (int k = 1; k <= M - (m << 1) + 2; k++) {
            xValue += getMarginValue(tuples.subList(0, m - 1 + k), tuples.subList(m - 1 + k, tuples.size()));
        }

        //from Y
        tuples = sortByAxis(tuples,Axis.Y,false);
        for (int k = 1; k <= M - (m << 1) + 2; k++) {
            yValue += getMarginValue(tuples.subList(0, m - 1 + k), tuples.subList(m - 1 + k, tuples.size()));
        }

        if(Double.compare(xValue,yValue) < 0) {
            return Axis.X;
        } else {
            return Axis.Y;
        }
    }

    protected List<? extends CommonRectangle> sortByAxis(List<? extends CommonRectangle> tuples, Axis axis, boolean reversed) {
        if(axis.equals(Axis.X)) {
            if(reversed) {
                return tuples.stream().sorted(Comparator.comparing(CommonRectangle::minX, Double::compare)
                        .thenComparing(CommonRectangle::maxX, Double::compare).reversed()).collect(Collectors.toCollection(LinkedList::new));
            }
            return tuples.stream().sorted(Comparator.comparing(CommonRectangle::minX, Double::compare)
                    .thenComparing(CommonRectangle::maxX, Double::compare)).collect(Collectors.toCollection(LinkedList::new));
        } else {
            if(reversed) {
                return tuples.stream().sorted(Comparator.comparing(CommonRectangle::minY, Double::compare)
                        .thenComparing(CommonRectangle::maxX, Double::compare).reversed()).collect(Collectors.toCollection(LinkedList::new));
            }
            return tuples.stream().sorted(Comparator.comparing(CommonRectangle::minY, Double::compare)
                    .thenComparing(CommonRectangle::maxY, Double::compare)).collect(Collectors.toCollection(LinkedList::new));
        }
    }

    private int chooseSplitIndex(List<? extends CommonRectangle> tuples) {
        LinkedList<Integer> indexList = new LinkedList<>();
        for (int k = 1; k < tuples.size(); k++) {
            indexList.add(k);
        }
        Optional<Integer> index = indexList.stream().sorted().min((Integer x,Integer y) -> compareOverlapValue(x,y,tuples));

        if(index.isPresent()) {
            return index.get();
        }else {
            throw new IllegalArgumentException("Invalid index");
        }
    }


    public int compareOverlapValue(Integer k1, Integer k2, List<? extends CommonRectangle> sortedTuples) {
        int res= Double.compare(getOverlapValue(sortedTuples.subList(0,k1),sortedTuples.subList(k1,sortedTuples.size())),getOverlapValue(sortedTuples.subList(0,k2),sortedTuples.subList(k2 + 1,sortedTuples.size())));
        if(res == 0) {
            res = Double.compare(getAreaValue(sortedTuples.subList(0,k1),sortedTuples.subList(k1,sortedTuples.size())),getAreaValue(sortedTuples.subList(0,k2),sortedTuples.subList(k2 + 1,sortedTuples.size())));
        }
        return res;
    }





    public Object[] split(List<? extends CommonRectangle> tuples) {
        Object[] res = new Object[2];
        Axis axis = chooseSplitAxis(tuples);
        if(axis.equals(Axis.X)) {
            tuples = sortByAxis(tuples,Axis.X,false);
        } else {
            tuples = sortByAxis(tuples,Axis.Y,false);
        }

        int index = chooseSplitIndex(tuples);
        res[0] = tuples.subList(0,index);
        res[1] = tuples.subList(index,tuples.size());
        return res;
    }





    //-----------------ReInsert------------------//
    int p = (int) (M * 0.3);

    public List<List<? extends CommonRectangle>> getReinsertSet(Rectangle subtree, Collection<? extends CommonRectangle> set){
        LinkedList<? extends CommonRectangle> sortedSet =
                set.stream().sorted(
                        ((x, y) -> -Double.compare(x.distance_compare(subtree), y.distance_compare(subtree)))
                ).collect(Collectors.toCollection(LinkedList::new));
        List<List<? extends CommonRectangle>> res = new LinkedList<>();
        res.add(sortedSet.subList(p,sortedSet.size() - 1));
        res.add(sortedSet.subList(0,p));

        return res;
    }

}
