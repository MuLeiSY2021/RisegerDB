package org.riseger.protoctl.search.function.type;

import org.riseger.protoctl.search.function.Function_F;

import java.util.List;

public interface FUNCTIONAL {
    int getId();

    List<Function_F> getFunctions();

    Integer getWeight();

    boolean canSkip();

}
