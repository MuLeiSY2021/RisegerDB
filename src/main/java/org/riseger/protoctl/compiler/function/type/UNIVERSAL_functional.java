package org.riseger.protoctl.compiler.function.type;

import com.google.gson.annotations.JsonAdapter;
import org.riseger.protoctl.serializer.FunctionDeserializer;

@JsonAdapter(FunctionDeserializer.class)

public interface UNIVERSAL_functional extends
        BOOL_functional,
        COORD_functional,
        Functional,
        NUMBER_functional,
        RECTANGLE_functional {

}
