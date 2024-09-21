package org.riseger.main.system.cache.component;

import lombok.Getter;
import org.riseger.main.system.cache.manager.ElementManager;
import org.riseger.protocol.exception.SQLException;
import org.riseger.protocol.struct.entity.Element_p;
import org.riseger.protocol.struct.entity.Type;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Element extends GeoRectangle {
    private final String parentModel;

    private final Model model;

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    private transient final Database db;

    private transient final ElementManager elementManager;

    public Element(Element_p e, Database db, ElementManager elementManager, double threshold) {
        super(e.getAttributes(), threshold);
        this.parentModel = e.getParent().getName();
        this.model = db.getModelManager().getModel(e.getModelName());
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

    public Object getAttribute(String attribute) {
        return attributes.get(attribute);
    }

    public String getModel() {
        return this.model.name;
    }

    public void setAttribute(String attribute, Object value) throws Exception {
        if (!model.isObjectLegal(attribute, value)) {
            //TODO：不应该报这个错
            throw new SQLException("Value is Not Legal");
        }
        if (model.isKey(attribute)) {
            KeyComponent key = (KeyComponent) value;
            this.updateIndex(key.toRectangle(db.getThreshold()));
        }
        this.attributes.remove(attribute);
        this.attributes.put(attribute, value);
    }

    public void updateIndex(Rectangle rectangle) throws Exception {
        super.resetIndex(rectangle);
        this.elementManager.setIndex(this, rectangle);
    }
}
