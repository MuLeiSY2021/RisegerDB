package org.riseger.protoctl.job;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.*;
import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.main.search.SQLTree;
import org.riseger.main.search.SQLTreeIterator;
import org.riseger.main.search.function.Function_c;
import org.riseger.protoctl.message.SearchMessage;
import org.riseger.protoctl.search.command.SEARCH;
import org.riseger.protoctl.search.command.USE;
import org.riseger.protoctl.search.command.WHERE;

import java.util.*;

public class SearchJob implements Job {
    private static final Logger LOG = Logger.getLogger(SearchJob.class);

    private final USE sql;

    private final TransponderHandler<SearchMessage, List<Element_c>> transponder;

    public Database_c database;

    public List<MapDB_c> maps = new LinkedList<>();

    public String scope;

    public List<String> scopeRoute;

    public SQLTree tree;

    private boolean inScope = false;

    private int level = 0;

    private List<SearchSet> searchSets = new LinkedList<>();

    public SearchJob(USE sql, TransponderHandler<SearchMessage, List<Element_c>> transponder) {
        this.sql = sql;
        this.transponder = transponder;
    }

    @Override
    public void run() {
        preprocessUse(sql);
        preprocessSearch(sql.getSearch());
        preprocessWhere(sql.getSearch().getWhere());
        List<Element_c> result = process();
        transponder.setE(result);
    }

    private List<Element_c> process() {
        List<Element_c> result = new LinkedList<>();
        if (!inScope) {
            List<MapDB_c> tmps = new LinkedList<>();
            for (MapDB_c map : maps) {
                try {
                    while (!inScope) {
                        if (level > scopeRoute.size()) {
                            inScope = true;
                        }
                        Layer_c layer = map.getSubmapLayer(scopeRoute.get(level));
                        tmps.addAll(whereScope(layer.getElementManager()));
                        level++;
                    }
                } catch (NullPointerException e) {
                    LOG.error(e);
                    e.printStackTrace();
                }
            }
            this.maps = tmps;
        } else {
            for (MapDB_c map : maps) {
                try {
                    for (SearchSet set : searchSets) {
                        Layer_c layer = map.getElementLayer(set.getName());
                        result.addAll(whereElement(layer.getElementManager()));
                    }
                } catch (NullPointerException e) {
                    LOG.error(e);
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private List<MapDB_c> whereScope(ElementManager elementManager) {
        SQLTreeIterator iterator = tree.submapIterator();
        List<MapDB_c> maps = new LinkedList<>();
        while (iterator.hasNext()) {
            Function_c function = iterator.next();
            if (maps.isEmpty()) {
                List<MBRectangle_c> result = function.getResult(elementManager);
                for (MBRectangle_c mbr : result) {
                    if (!(result instanceof MapDB_c)) {
                        LOG.error("奇怪的子图查询时错误");
                    } else {
                        maps.add((MapDB_c) mbr);
                    }
                }
            } else {
                List<MapDB_c> result = new LinkedList<>();

                for (MapDB_c mb : maps) {
                    if (function.isResult(mb)) {
                        result.add(mb);
                    }
                }
                maps = result;
            }
            iterator.next();
        }

        return maps;
    }

    private List<Element_c> whereElement(ElementManager elementManager) {
        SQLTreeIterator iterator = tree.submapIterator();
        List<Element_c> maps = new LinkedList<>();
        while (iterator.hasNext()) {
            Function_c function = iterator.next();
            if (maps.isEmpty()) {
                List<MBRectangle_c> result = function.getResult(elementManager);
                for (MBRectangle_c mbr : result) {
                    if (!(result instanceof Element_c)) {
                        LOG.error("奇怪的元素查询时错误");
                    } else {
                        maps.add((Element_c) mbr);
                    }
                }
            } else {
                List<Element_c> result = new LinkedList<>();

                for (Element_c mb : maps) {
                    if (function.isResult(mb)) {
                        result.add(mb);
                    }
                }
                maps = result;
            }
            iterator.next();
        }

        return maps;
    }

    private void preprocessUse(USE use) {
        this.database = CacheMaster.INSTANCE.getDatabase(use.getDatabase());
        this.maps.add(database.getMap(use.getMap()));
        this.scopeRoute = new LinkedList<>();
        this.scopeRoute.addAll(Arrays.asList(use.getScope().split("\\.")));
    }

    private void preprocessSearch(SEARCH search) {
        this.searchSets = new LinkedList<>();
        Map<String, SearchSet> searchSetMap = new HashMap<>();
        for (String list : search.getContent()) {
            String[] sets = list.split("\\.");
            SearchSet searchSet;
            if (searchSetMap.containsKey(sets[0])) {
                searchSet = searchSetMap.get(sets[0]);
            } else {
                searchSet = new SearchSet(sets);
                searchSets.add(searchSet);
                searchSetMap.put(sets[0], searchSet);
            }
            searchSet.add(sets);
        }
    }

    private void preprocessWhere(WHERE where) {
        this.tree = new SQLTree(where);
    }

    @Data
    private static class SearchSet {
        String name;

        List<String[]> child = new LinkedList<>();

        public SearchSet(String[] sets) {
            name = sets[0];
            child.add(Arrays.copyOfRange(sets, 1, sets.length));
        }

        public void add(String[] sets) {
            child.add(Arrays.copyOfRange(sets, 1, sets.length));
        }
    }
}
