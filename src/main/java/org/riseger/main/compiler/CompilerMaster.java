package org.riseger.main.compiler;

import org.riseger.main.compiler.compoent.SearchSession;
import org.riseger.main.compiler.compoent.SessionAdaptor;

public class CompilerMaster {
    public static CompilerMaster INSTANCE;

    private final SessionAdaptor adaptor;

    public CompilerMaster(SessionAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public SearchSession adapt(String text) {
        return adaptor.adapt(text);
    }
}
