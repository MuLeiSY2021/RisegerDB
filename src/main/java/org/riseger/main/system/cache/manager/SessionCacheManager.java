package org.riseger.main.system.cache.manager;

import org.riseger.main.system.compile.compoent.SearchSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionCacheManager {
    private static final Map<String, SearchSession> sessionCache = new ConcurrentHashMap<>();

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
