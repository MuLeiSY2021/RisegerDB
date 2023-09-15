package org.riseger.main.search.function.type;

import com.google.gson.annotations.JsonAdapter;
import org.riseger.protoctl.serializer.FunctionDeserializer;

@JsonAdapter(FunctionDeserializer.class)

public interface BOOL_FUNCTIONBLE extends FUNCTIONBLE {

}
