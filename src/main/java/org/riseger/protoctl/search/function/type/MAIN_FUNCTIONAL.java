package org.riseger.protoctl.search.function.type;

import com.google.gson.annotations.JsonAdapter;
import org.riseger.protoctl.serializer.FunctionDeserializer;

@JsonAdapter(FunctionDeserializer.class)
public interface MAIN_FUNCTIONAL extends FUNCTIONAL {

}