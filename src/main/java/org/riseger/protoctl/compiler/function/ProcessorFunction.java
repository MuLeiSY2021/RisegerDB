package org.riseger.protoctl.compiler.function;

import org.riseger.main.compiler.semantic.SemanticTree;

import java.util.List;

public interface ProcessorFunction {
    void preHandle(SemanticTree.Node node, int size, List<Function_f> functionList);
}
