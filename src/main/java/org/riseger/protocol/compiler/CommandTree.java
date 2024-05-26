package org.riseger.protocol.compiler;

import lombok.Data;
import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;

import java.util.LinkedList;
import java.util.List;

@Getter
public class CommandTree {

    private final Node root = new Node();

    @Data
    public static class Node {
        private final List<Node> children = new LinkedList<>();
        Function_f function;
        Object cons;
    }
}
