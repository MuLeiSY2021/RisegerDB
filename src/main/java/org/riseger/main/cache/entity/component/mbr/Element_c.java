package org.riseger.main.cache.entity.component.mbr;

import org.riseger.main.cache.entity.component.db.Database_c;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.protoctl.struct.entity.Element;

import java.util.HashMap;
import java.util.Map;

public class Element_c extends MBRectangle_c {
    private final String parentModel;

    private final String model;

    private final Map<String, String> attributes = new HashMap<>();

    private final Database_c db;

    private final ElementManager elementManager;

    public Element_c(Element e, Database_c db, ElementManager elementManager, double threshold) {
        super(e.getAttributes(),threshold);
        this.parentModel = e.getParent().getName();
        this.model = e.getModelName();
        this.db = db;
        this.elementManager = elementManager;
        for (Map.Entry<String,String> a : e.getAttributes().entrySet()) {
            String k = a.getKey();
            attributes.put(k, a.getValue());
        }
    }

    //TODO::需要做对模版类和父类合法性的判断
    public boolean legal(Element e) {
        return true;
    }


}
