package org.riseger.protoctl.search.function;

import org.riseger.main.compiler.semantic.SemanticTree;

public interface ProcessorFunction {
    void preHandle(SemanticTree.Node node, int size);
}
