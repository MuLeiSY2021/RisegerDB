package org.riseger.main.system.compile.function.interfaces;

import org.riseger.main.system.compile.semantic.SemanticTree;
import org.riseger.protocol.compiler.function.Function_f;

import java.util.List;

public interface StretchFunction {
    void stretch(SemanticTree.Node node, int size, List<Function_f> functionList);
}
