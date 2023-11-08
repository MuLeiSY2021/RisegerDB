package org.riseger.main.compiler.semantic;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.compiler.syntax.Syntax;
import org.riseger.main.compiler.syntax.SyntaxForest;
import org.riseger.main.compiler.token.Token;
import org.riseger.utils.tree.Equable;

@Data
public class SyntaxEqualPack implements Equable {
    private static final Logger LOG = Logger.getLogger(SyntaxEqualPack.class);
    SemanticTree tree;
    SemanticTree.Node node;
    Token token;
    CopyableIterator<SyntaxEqualPack> iterator;
    SyntaxForest forest;

    public SyntaxEqualPack(SemanticTree tree, SemanticTree.Node node, Token token, CopyableIterator<SyntaxEqualPack> iterator, SyntaxForest forest) {
        this.tree = tree;
        this.node = node;
        this.token = token;
        this.iterator = iterator;
        this.forest = forest;
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
            SemanticTree.Node tmp = new SemanticTree.Node();

            if (forest.isEnd(syntax.getId())) {
                LOG.debug("语法终结点：" + forest.getEndType(syntax.getId()));
                if (tree.getEndNode(tmp, token, syntax.getId(), forest)) {
                    LOG.debug("语法终结点" + token.getSourceCode() + " 匹配成功");
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
            if (tree.suitTree(tmp, syntax.getId(), iterator, forest)) {
                LOG.debug("非关键词 '" + syntax.getSymbol() + "' 匹配成功");
                node.add(tmp);
                return true;
            } else {
                LOG.debug("非关键词" + syntax.getSymbol() + "匹配失败");
                return false;
            }
        }
    }
}
