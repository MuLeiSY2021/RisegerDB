package org.riseger.main.init;

import org.riseger.main.compiler.CompilerMaster;
import org.riseger.main.compiler.lextcal.KeywordsTree;
import org.riseger.main.compiler.lextcal.Lexicator;
import org.riseger.main.compiler.session.SessionAdaptor;
import org.riseger.main.compiler.syntax.Parser;
import org.riseger.main.compiler.syntax.SyntaxRule;
import org.riseger.main.compiler.syntax.SyntaxTree;
import org.riseger.main.compiler.token.Tokenizer;
import org.riseger.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;

public class CompilerInitialize implements Initializer {
    public void init() throws FileNotFoundException {
        //获取文法规则
        String rule = Utils.getText(new File("src/main/resources/rule/syntaxrule.txt"));
        if (rule == null) {
            throw new FileNotFoundException("Syntax rule");
        }

        //文法规则解析
        SyntaxRule syntaxRule = SyntaxRule.newSyntaxRule(rule);

        //生成文法规则树
        SyntaxTree syntaxTree = new SyntaxTree(syntaxRule);

        //生成关键词树
        KeywordsTree keywordsTree = KeywordsTree.newKeywordsTree();

        //生成分词器
        Tokenizer tokenizer = new Tokenizer(keywordsTree);

        //生成词法解析器
        Lexicator lexicator = new Lexicator(keywordsTree);

        //生成语法解析器
        Parser parser = new Parser(syntaxTree);
        CompilerMaster.INSTANCE = new CompilerMaster(new SessionAdaptor(tokenizer, lexicator, parser));
    }
}
