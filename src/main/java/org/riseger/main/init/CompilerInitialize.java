package org.riseger.main.init;

import org.apache.log4j.Logger;
import org.riseger.main.compiler.CompilerMaster;
import org.riseger.main.compiler.compoent.SessionAdaptor;
import org.riseger.main.compiler.lextcal.Keyword;
import org.riseger.main.compiler.lextcal.Lexicator;
import org.riseger.main.compiler.syntax.Parser;
import org.riseger.main.compiler.syntax.SyntaxForest;
import org.riseger.main.compiler.syntax.SyntaxRule;
import org.riseger.main.compiler.token.Tokenizer;
import org.riseger.utils.Utils;
import org.riseger.utils.tree.MultiBranchesTree;

import java.io.File;
import java.io.FileNotFoundException;

public class CompilerInitialize extends Initializer {
    private static final Logger LOG = Logger.getLogger(CompilerInitialize.class);

    public CompilerInitialize(String rootPath) {
        super(rootPath);
    }

    public boolean init() {
        try {
            //获取文法规则
            String rule = Utils.getText(new File(rootPath + "/resources/rule/syntaxrule.rule"));
            if (rule == null) {
                throw new FileNotFoundException("Syntax rule");
            }

            //文法规则解析
            SyntaxRule syntaxRule = new SyntaxRule(rule);

            //生成文法规则树
            SyntaxForest syntaxForest = new SyntaxForest(syntaxRule);

            //生成关键词树
            MultiBranchesTree<Keyword> keywordsTree = new MultiBranchesTree<>();
            //填充关键词
            for (Keyword keyword : Keyword.getKeywords()) {
                keywordsTree.insert(keyword);
            }

            //生成分词器
            Tokenizer tokenizer = new Tokenizer(keywordsTree);

            //生成词法解析器
            Lexicator lexicator = new Lexicator(keywordsTree);

            //生成语法解析器
            Parser parser = new Parser(syntaxForest);

            //
            CompilerMaster.INSTANCE = new CompilerMaster(new SessionAdaptor(tokenizer, lexicator, parser));
        } catch (Exception e) {
            LOG.error("Failed to initialize Compiler", e);
            return false;
        }
        return true;
    }
}
