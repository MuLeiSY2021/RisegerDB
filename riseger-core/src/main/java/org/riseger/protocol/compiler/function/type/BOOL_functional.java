package org.riseger.protocol.compiler.function.type;

import com.google.gson.annotations.JsonAdapter;
import org.riseger.protocol.serializer.FunctionDeserializer;

@JsonAdapter(FunctionDeserializer.class)

public interface BOOL_functional extends Functional {

}
