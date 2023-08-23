package org.riseger.main.api.compile;

import com.google.gson.Gson;
import lombok.Data;
import org.riseger.main.api.compile.context.Context;
import org.riseger.main.api.compile.syntax.Function;
import org.riseger.main.api.compile.syntax.Interpreter;
import org.riseger.protoctl.utils.Utils;

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
        System.out.println(sourcecode);
    }
}
