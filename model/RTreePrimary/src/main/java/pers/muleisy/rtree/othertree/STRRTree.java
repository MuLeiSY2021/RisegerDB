package pers.muleisy.rtree.othertree;

import pers.muleisy.rtree.rectangle.MBRectangle;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class STRRTree<R extends MBRectangle> extends RStarTree<R> {

    public STRRTree(int nodeSize, double threshold, Class<? extends R> clazz) {
        super(nodeSize, threshold, clazz);
    }

    @Override
    public void insertAll(List<? extends R> rects) {
        super.insertAll(rects);
        List<SubTree> subTreeList = packing(rects);
        while (subTreeList.size() != 1) {
            subTreeList = packing(subTreeList);
        }
        this.root = subTreeList.get(0);
    }

    public int getChunkPieces(int size) {
        int P = size % M == 0 ? size / M : (size / M) + 1;
        return (int) (Math.sqrt(P) - (int) Math.sqrt(P) == 0 ? Math.sqrt(P) : Math.sqrt(P) + 1);
    }

    public int getChunkSize(int pieces) {
        return pieces * M;
    }

    private List<SubTree> packing(List<? extends MBRectangle> rects) {
        int chunkPieces = getChunkPieces(rects.size());

        List<List<? extends MBRectangle>> tmpList = new LinkedList<>();
        List<SubTree> subTreeList = new LinkedList<>();
        rects = sortByAxis(rects, Axis.X, false);
        int chunkSize = getChunkSize(chunkPieces);

        for (int i = 0; i < chunkPieces; i++) {
            List<? extends MBRectangle> subList = getSubList(chunkSize, i, rects);
            if (subList == null) {
                break;
            }
            tmpList.add(subList);
        }
        int t = 0;
        for (List<? extends MBRectangle> rList : tmpList) {
            rList = sortByAxis(rList, Axis.Y, true);
            for (int i = 0; i < chunkPieces; i++) {
                List<? extends MBRectangle> subList = getSubList(M(), i, rList);
                if (subList == null) {
                    break;
                }
                if (rects.get(0) instanceof RTree.SubTree) {
                    subTreeList.add(new SubTree((Collection<? extends SubTree>) subList));
                } else {
                    subTreeList.add(new Leaf((Collection<? extends R>) subList));
                }
            }
        }
        return subTreeList;
    }

    private List<? extends MBRectangle> getSubList(int chunkSize, int round, List<? extends MBRectangle> list) {
        int fromIndex = Math.min(round * chunkSize, list.size()), toIndex = Math.min((round + 1) * chunkSize, list.size());
        if (toIndex - fromIndex <= 0) {
            return null;
        }
        return list.subList(fromIndex, toIndex);
    }

}
