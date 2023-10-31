package org.riseger.protoctl.compiler.function.type;

import com.google.gson.annotations.JsonAdapter;
import org.riseger.protoctl.serializer.FunctionDeserializer;

@JsonAdapter(FunctionDeserializer.class)

public interface COORD_functional extends Functional {
}
