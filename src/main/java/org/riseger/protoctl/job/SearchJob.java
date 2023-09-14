package org.riseger.protoctl.job;

import lombok.Data;
import org.riseger.main.api.workflow.revoke.revocable;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.Layer_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.search.SQLTree;
import org.riseger.main.search.SQLTreeIterator;
import org.riseger.protoctl.search.command.SEARCH;
import org.riseger.protoctl.search.command.USE;
import org.riseger.protoctl.search.command.WHERE;
import org.riseger.protoctl.search.function.FUNCTION;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SearchJob extends revocable<USE> implements Job {
    private final USE sql;
    public Database_c database;
    public MapDB_c map;
    public String scope;
    public List<String> scopeRoute;
    public SQLTree tree;
    private boolean inScope = false;
    private int level = 0;
    private List<SearchSet> searchSets = new LinkedList<>();

    public SearchJob(USE sql) {
        this.sql = sql;
    }

    @Override
    public void run() {
        //TODO: 查询handler
        preprocessUse(sql);
        preprocessSearch(sql.search());
        preprocessWhere(sql.search().where());
        process();
    }

    private void process() {
        if (inScope) {
            for (SearchSet set : searchSets) {
                Layer_c layer = map.getLayer(set.getName());
                where(layer);
            }
        } else {
            Layer_c layer = map.getLayer(scopeRoute.get(level));
            this.map = whereScope(layer);
        }
    }

    private MapDB_c whereScope(Layer_c layer) {
        SQLTreeIterator iterator = tree.iterator();
        FUNCTION function = iterator.next();
        //TODO: 返回子图查询结果

        return null;
    }

    private void where(Layer_c layer) {
        SQLTreeIterator iterator = tree.iterator();
        FUNCTION function = iterator.next();
        //TODO: 返回查询结果

    }

    private void preprocessUse(USE use) {
        this.database = CacheMaster.INSTANCE.getDatabase(use.getDatabase());
        this.map = database.getMap(use.getMap());
        this.scopeRoute = new LinkedList<>();
        this.scopeRoute.addAll(Arrays.asList(use.getScope().split("\\.")));
    }

    private void preprocessSearch(SEARCH search) {
        //TODO:生成查询集合
    }

    private void preprocessWhere(WHERE where) {
        this.tree = new SQLTree(where);
    }

    @Override
    public USE getE() {
        return sql;
    }

    @Override
    public void setE(USE sql) {
        throw new UnsupportedOperationException();
    }

    @Data
    private static class SearchSet {
        String name;

        List<String> child = new LinkedList<>();
    }
}
