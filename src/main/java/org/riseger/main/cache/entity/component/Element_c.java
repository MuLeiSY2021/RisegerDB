package org.riseger.main.cache.entity.component;

import lombok.Getter;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Type;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Element_c extends MBRectangle_c {
    private final String parentModel;

    private final Model_c model;

    private final Map<String, Object> attributes = new HashMap<>();

    private transient final Database_c db;

    private transient final ElementManager elementManager;

    public Element_c(Element e, Database_c db, ElementManager elementManager, double threshold) {
        super(e.getAttributes(), threshold);
        this.parentModel = e.getParent().getName();
        this.model = db.getModels().getModel(e.getModelName());
        this.db = db;
        this.elementManager = elementManager;
        for (Map.Entry<String, String> a : e.getAttributes().entrySet()) {
            Object k = convert(a.getKey(), a.getValue());
            attributes.put(a.getKey(), k);
        }
    }

    private Object convert(String name, String value) {
        Type type = model.getType(name);
        return type.convert(value);
    }

    //TODO::需要做对模版类和父类合法性的判断
    public boolean legal(Element e) {
        return true;
    }


    public Object getAttribute(String attribute) {
        return attributes.get(attribute);
    }

    public String getModel() {
        return this.model.name;
    }
}
