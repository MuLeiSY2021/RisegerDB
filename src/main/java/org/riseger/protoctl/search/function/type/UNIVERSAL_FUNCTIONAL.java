package org.riseger.protoctl.search.function.type;

import com.google.gson.annotations.JsonAdapter;
import org.riseger.protoctl.serializer.FunctionDeserializer;

@JsonAdapter(FunctionDeserializer.class)

public interface UNIVERSAL_FUNCTIONAL extends
        BOOL_FUNCTIONAL,
        COORD_FUNCTIONAL,
        FUNCTIONAL,
        NUMBER_FUNCTIONAL,
        RECTANGLE_FUNCTIONAL {

}
