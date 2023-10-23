package org.riseger.protoctl.search.function.type;

import org.riseger.protoctl.search.function.Function_f;

import java.util.List;

public interface FUNCTIONAL {
    int getId();

    List<Function_f> getFunctions();

    Integer getWeight();

    boolean canSkip();

}
