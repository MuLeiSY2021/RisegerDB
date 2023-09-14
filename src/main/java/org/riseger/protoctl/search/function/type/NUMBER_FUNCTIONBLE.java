package org.riseger.protoctl.search.function.type;

import com.google.gson.annotations.JsonAdapter;
import org.riseger.protoctl.serializer.FunctionDeserializer;

@JsonAdapter(FunctionDeserializer.class)

public interface NUMBER_FUNCTIONBLE extends FUNCTIONBLE {
}
