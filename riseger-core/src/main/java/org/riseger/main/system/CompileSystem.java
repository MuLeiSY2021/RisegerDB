package org.riseger.main.system;

import org.apache.log4j.Logger;
import org.riseger.main.system.cache.manager.SessionManager;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchSession;
import org.riseger.main.system.compile.compoent.SessionAdaptor;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.compiler.CommandTree;
import org.riseger.protocol.serializer.JsonSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CompileSystem {
    private static final Logger LOG = Logger.getLogger(CompileSystem.class);

    private final SessionAdaptor adaptor;
    public static CompileSystem INSTANCE;

    public CompileSystem(SessionAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public SearchSession adapt(String text, String ipAddress) {
        LOG.debug("ip: " + ipAddress);
        Integer sessionId = ipAddress.hashCode();
        SearchSession session;
        if (SessionManager.containsKey(sessionId)) {
            session = SessionManager.get(sessionId);
            session.setSourcecode(text);
            session.reset();
        } else {
            session = adaptor.adapt(text, sessionId);
            SessionManager.put(sessionId, session);
        }
        return session;
    }

    public SearchSession adapt(CommandTree commandTree, String ipAddress) {
        Integer sessionId = ipAddress.hashCode();

        if (SessionManager.containsKey(sessionId)) {
            SearchSession session = SessionManager.get(sessionId);
            session.setCommandTree(commandTree);
            session.reset();
            return session;
        } else {
            return adaptor.adapt(commandTree, sessionId);
        }
    }

    public void processLog(File logFile, int sessionId) {
        try (BufferedReader br = new BufferedReader(new FileReader(logFile.getPath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                SearchSession session;
                CommandList commandList = new CommandList(JsonSerializer.deserialize(line, Function_c[].class));
                if (SessionManager.containsKey(sessionId)) {
                    session = SessionManager.get(sessionId);
                    session.setCommandList(commandList);
                    session.reset();
                } else {
                    session = adaptor.adapt(commandList, sessionId);
                }
                session.process(true);
            }
        } catch (Exception e) {
            LOG.error("can't process log file " + logFile.getName(), e);
        }
    }
}
