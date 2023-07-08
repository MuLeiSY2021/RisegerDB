package org.risegerdb.compile.syntax;

import org.risegerdb.compile.lextcal.Lexicon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session {
    private PriorityTree.Node tmpNnode;

    private final Map<String,Integer> prefixMapping = new HashMap<>();

    public Session(PriorityTree.Node tmpNode) {
        this.tmpNnode = tmpNode;
    }

    public List<Lexicon> getLine() {
        return tmpNnode.getParent() == null ?
                        null :
                tmpNnode.equals(tmpNnode.getParent().getLChild()) ?
                        (tmpNnode.getParent().getRChild() == null ?
                                null : tmpNnode.getParent().getRChild().getLine())
                        : tmpNnode.getParent().getLine();

    }

    public void response(String result) {
        prefixMapping.put(result, prefixMapping.containsKey(result) ? prefixMapping.get(result) + 1 : 0);
        tmpNnode.getParent().getLine().add(tmpNnode.getPrevIndex(),
                new Lexicon(result + prefixMapping.get(result),result));
    }
}
