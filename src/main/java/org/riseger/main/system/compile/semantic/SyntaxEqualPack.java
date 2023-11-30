package org.riseger.main.system.compile.semantic;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.system.compile.syntax.Syntax;
import org.riseger.main.system.compile.syntax.SyntaxForest;
import org.riseger.main.system.compile.token.Token;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.utils.tree.Equable;

import java.util.Stack;

@Data
public class SyntaxEqualPack implements Equable {
    private static final Logger LOG = Logger.getLogger(SyntaxEqualPack.class);
    private SemanticTree tree;

    private Token token;

    private CopyableIterator<SyntaxEqualPack> iterator;

    private SyntaxForest forest;

    private Stack<TreeIterator<Function_f>> nodeStack;

    public SyntaxEqualPack(SemanticTree tree, Token token, CopyableIterator<SyntaxEqualPack> iterator, SyntaxForest forest, Stack<TreeIterator<Function_f>> nodeStack) {
        this.tree = tree;
        this.token = token;
        this.iterator = iterator;
        this.forest = forest;
        this.nodeStack = nodeStack;
    }

    @Override
    public boolean equal(Object o) throws Exception {
        Syntax syntax = (Syntax) o;
        if (syntax.isKeyword()) {
            if (!syntax.equals(token)) {
                LOG.debug("匹配失败,源代码：\"" + token.getSourceCode() + "\":" + token.getId() + " 不匹配语法：\"" + syntax.getSymbol() + "\":" + syntax.getId());
                return false;
            } else {
                LOG.debug("匹配成功，源代码为：\"" + token.getSourceCode() + "\" 匹配代码为:\"" + syntax.getSymbol() + "\"");
                return true;
            }
        } else {
            TreeIterator<Function_f> parent = nodeStack.peek();
            TreeIterator<Function_f> tmp = tree.iteratorEmpty();
            nodeStack.push(tmp);
            if (forest.isEnd(syntax.getId())) {
                LOG.debug("语法终结点：" + forest.getEndType(syntax.getId()));
                if (tree.getEndNode(nodeStack, token, syntax.getId(), forest)) {
                    LOG.debug("语法终结点" + token.getSourceCode() + " 匹配成功");
                    parent.add(tmp);
                    return true;
                } else {
                    LOG.debug("语法终结点" + token.getSourceCode() + " 匹配失败");
                    return false;
                }
            }
            LOG.debug("非关键词 '" + syntax.getSymbol() + "' 匹配");
            if (iterator.hasPrevious()) {
                iterator.previous();
            }
            if (tree.suitTree(nodeStack, syntax.getId(), iterator, forest)) {
                LOG.debug("非关键词 '" + syntax.getSymbol() + "' 匹配成功");
                parent.add(tmp);
                return true;
            } else {
                LOG.debug("非关键词" + syntax.getSymbol() + "匹配失败");
                return false;
            }
        }
    }
}
