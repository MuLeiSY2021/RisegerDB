package org.riseger.protoctl.search.function.type;

import org.riseger.protoctl.search.function.FUNCTION;

import java.util.List;

public interface FUNCTIONBLE {
    int getId();

    List<FUNCTION> getFunctions();

    Integer getWeight();

    boolean canSkip();

}
