package org.riseger.main.cache.manager;

import org.riseger.main.compiler.compoent.SearchSession;

import java.util.HashMap;
import java.util.Map;

public class SessionCacheManager {
    private static final Map<String, SearchSession> sessionCache = new HashMap<>();

    public static void put(String key, SearchSession session) {
        sessionCache.put(key, session);
    }

    public static SearchSession get(String key) {
        return sessionCache.get(key);
    }

    public static boolean containsKey(String ipAddress) {
        return sessionCache.containsKey(ipAddress);
    }
}
