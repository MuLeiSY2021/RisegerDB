package org.riseger.main.system.cache.manager;

import org.riseger.main.system.compile.compoent.SearchSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<Integer, SearchSession> sessionCache = new ConcurrentHashMap<>();

    public static void put(Integer key, SearchSession session) {
        sessionCache.put(key, session);
    }

    public static SearchSession get(Integer key) {
        return sessionCache.get(key);
    }

    public static boolean containsKey(Integer key) {
        return sessionCache.containsKey(key);
    }
}
