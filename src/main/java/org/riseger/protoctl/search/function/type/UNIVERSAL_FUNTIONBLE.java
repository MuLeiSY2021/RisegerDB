package org.riseger.protoctl.search.function.type;

import com.google.gson.annotations.JsonAdapter;
import org.riseger.protoctl.serializer.FunctionDeserializer;

@JsonAdapter(FunctionDeserializer.class)

public interface UNIVERSAL_FUNTIONBLE extends
        BOOL_FUNCTIONBLE,
        COORD_FUNCTIONBLE,
        FUNCTIONBLE,
        NUMBER_FUNCTIONBLE,
        RECTANGLE_FUNCTIONBLE {

}
