package org.riseger.main.cache.entity.component.mbr;

import org.riseger.main.cache.entity.manager.ElementManager;
import org.riseger.main.cache.entity.manager.ModelManager;
import org.riseger.protoctl.struct.entity.Element;

import java.util.HashMap;
import java.util.Map;

public class Element_c extends MBRectangle_c {
    private final String parentModel;

    private final String model;

    private final Map<String, String> attributes = new HashMap<>();

    private final ModelManager modelManager;

    private final ElementManager elementManager;

    private Element_c(Element e,ModelManager modelManager,ElementManager elementManager, double threshold) {
        super(e.getAttributes(),threshold);
        this.parentModel = e.getParent().getName();
        this.model = e.getModelName();
        this.modelManager = modelManager;
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
