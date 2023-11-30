package org.riseger.protoctl.compiler.function;

import org.riseger.main.system.compile.semantic.SemanticTree;

import java.util.List;

public interface ProcessorFunction {

    void stretch(SemanticTree.Node node, int size, List<Function_f> functionList);

    List<Function_f> preprocess();


}
