package org.risegerdb.compile;

import com.google.gson.Gson;
import lombok.Data;
import org.risegerdb.compile.context.Context;
import org.risegerdb.compile.syntax.Function;
import org.risegerdb.compile.syntax.Interpreter;
import org.risegerdb.utils.Utils;

import java.io.File;

@Data
public class Session {
    private final Context context;

    private final Compiler compiler;

    private final Interpreter interpreter;

    public Session(String sourcecode) {
        this.context = new Context(sourcecode);
        this.compiler = new Compiler(context);
        this.interpreter = new Interpreter(context);
    }

    public void run() {
        compiler.compile();
    }

    public static void main(String[] args) {
        String sourcecode = Utils.getText(new File("src/main/resources/aaa.txt"));
        Session session = new Session(sourcecode);
        session.run();

        System.out.println(new Gson().toJson(Function.FUNCTIONS));
    }
}
