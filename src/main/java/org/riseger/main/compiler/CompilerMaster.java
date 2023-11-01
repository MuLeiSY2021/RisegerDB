package org.riseger.main.compiler;

import org.riseger.main.cache.manager.SessionCacheManager;
import org.riseger.main.compiler.compoent.SearchSession;
import org.riseger.main.compiler.compoent.SessionAdaptor;

public class CompilerMaster {
    public static CompilerMaster INSTANCE;

    private final SessionAdaptor adaptor;

    public CompilerMaster(SessionAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public SearchSession adapt(String text, String ipAddress) {
        if (SessionCacheManager.containsKey(ipAddress)) {
            SearchSession session = SessionCacheManager.get(ipAddress);
            session.setSourcecode(text);
            session.reset();
            return session;
        } else {
            return adaptor.adapt(text);
        }
    }
}
