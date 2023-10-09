package org.riseger.main.compiler;

import org.riseger.main.compiler.session.Session;
import org.riseger.main.compiler.session.SessionAdaptor;

public class CompilerMaster {
    public static CompilerMaster INSTANCE;

    private final SessionAdaptor adaptor;

    public CompilerMaster(SessionAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public Session adapt(String text) {
        return adaptor.adapt(text);
    }
}
