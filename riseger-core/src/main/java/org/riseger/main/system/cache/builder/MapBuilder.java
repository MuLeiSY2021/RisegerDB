package org.riseger.main.system.cache.builder;

import org.riseger.main.system.cache.component.GeoMap;

import java.io.IOException;

public interface MapBuilder {
    GeoMap build() throws IOException;
}
