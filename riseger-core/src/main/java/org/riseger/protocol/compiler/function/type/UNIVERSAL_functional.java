package org.riseger.protocol.compiler.function.type;

import com.google.gson.annotations.JsonAdapter;
import org.riseger.protocol.serializer.FunctionDeserializer;

@JsonAdapter(FunctionDeserializer.class)

public interface UNIVERSAL_functional extends
        BOOL_functional,
        COORD_functional,
        Functional,
        NumberFunctional,
        RECTANGLE_functional {

}
