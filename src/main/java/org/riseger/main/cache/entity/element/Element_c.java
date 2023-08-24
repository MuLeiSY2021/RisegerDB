package org.riseger.main.cache.entity.element;

import org.riseger.main.cache.entity.manager.ElementManager;
import org.riseger.main.cache.entity.manager.ModelManager;
import org.riseger.protoctl.struct.entity.Element;
import pers.muleisy.rtree.rectangle.MBRectangle;

import java.util.*;

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
            if(k.startsWith("KEY")) {
                String[] keys = k.split("::");
                int i = Integer.parseInt(keys[0]);
                Double[] coords;
                if(coordsSet.get(i) != null) {
                    coords = new Double[2];
                    coordsSet.add(i, coords);
                } else {
                    coords = coordsSet.get(i);
                }

                double v = super.truncateDecimal(Double.parseDouble(a.getValue()));
                if(keys[1].equals("x")) {
                    addX(v);
                    coords[0] = v;
                }else if(keys[1].equals("y")) {
                    addY(v);
                    coords[1] = v;
                }
            } else {
                attributes.put(k, a.getValue());
            }
        }
    }

    //TODO::需要做对模版类和父类合法性的判断
    public boolean legal(Element e) {
        return true;
    }

    @Override
    public void initBMRCoords() {
        setMinX(xKeySet.get(xKeySet.size() - 1));
        setMaxX(xKeySet.get(0));
        setMinY(yKeySet.get(yKeySet.size() - 1));
        setMaxY(yKeySet.get(0));
    }

    private void addX(double x) {
        for (int i = 0; i < xKeySet.size(); i++) {
            if (x < xKeySet.get(i)) {
                xKeySet.add(i, x);
                return;
            }
        }
        xKeySet.add(x);
    }

    private void addY(double y) {
        for (int i = 0; i < yKeySet.size(); i++) {
            if (y < yKeySet.get(i)) {
                yKeySet.add(i, y);
                return;
            }
        }
        yKeySet.add(y);
    }
}
