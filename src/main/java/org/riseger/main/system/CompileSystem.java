package org.riseger.main.system;

import org.apache.log4j.Logger;
import org.riseger.main.system.cache.manager.SessionManager;
import org.riseger.main.system.compile.compoent.SearchSession;
import org.riseger.main.system.compile.compoent.SessionAdaptor;
import org.riseger.protocol.compiler.CommandTree;

public class CompileSystem {
    private static final Logger LOG = Logger.getLogger(CompileSystem.class);

    private final SessionAdaptor adaptor;
    public static CompileSystem INSTANCE;

    public CompileSystem(SessionAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public SearchSession adapt(String text, String ipAddress) {
        LOG.debug("ip: " + ipAddress);
        SearchSession session;
        if (SessionManager.containsKey(ipAddress)) {
            session = SessionManager.get(ipAddress);
            session.setSourcecode(text);
            session.reset();
        } else {
            session = adaptor.adapt(text, ipAddress.hashCode());
            SessionManager.put(ipAddress, session);
        }
        return session;
    }

    public SearchSession adapt(CommandTree commandTree, String ipAddress) {
        if (SessionManager.containsKey(ipAddress)) {
            SearchSession session = SessionManager.get(ipAddress);
            session.setCommandTree(commandTree);
            session.reset();
            return session;
        } else {
            return adaptor.adapt(commandTree, ipAddress.hashCode());
        }
    }
}
