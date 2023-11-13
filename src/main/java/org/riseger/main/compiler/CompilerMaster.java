package org.riseger.main.compiler;

import org.apache.log4j.Logger;
import org.riseger.main.cache.manager.SessionCacheManager;
import org.riseger.main.compiler.compoent.SearchSession;
import org.riseger.main.compiler.compoent.SessionAdaptor;
import org.riseger.protoctl.compiler.CommandTree;

public class CompilerMaster {
    public static CompilerMaster INSTANCE;

    private final SessionAdaptor adaptor;

    private static final Logger LOG = Logger.getLogger(CompilerMaster.class);

    public CompilerMaster(SessionAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public SearchSession adapt(String text, String ipAddress) {
        LOG.debug("ip: " + ipAddress);
        SearchSession session;
        if (SessionCacheManager.containsKey(ipAddress)) {
            session = SessionCacheManager.get(ipAddress);
            session.setSourcecode(text);
            session.reset();
        } else {
            session = adaptor.adapt(text);
            SessionCacheManager.put(ipAddress, session);
        }
        return session;
    }

    public SearchSession adapt(CommandTree commandTree, String ipAddress) {
        if (SessionCacheManager.containsKey(ipAddress)) {
            SearchSession session = SessionCacheManager.get(ipAddress);
            session.setCommandTree(commandTree);
            session.reset();
            return session;
        } else {
            return adaptor.adapt(commandTree);
        }
    }
}
