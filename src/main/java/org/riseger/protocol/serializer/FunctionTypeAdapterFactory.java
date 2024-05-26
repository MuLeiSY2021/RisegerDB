package org.riseger.protocol.serializer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.log4j.Logger;
import org.riseger.main.system.compile.function.Entity_fc;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.main.system.compile.function.entity.*;
import org.riseger.main.system.compile.function.get.GetDatabases_fc;
import org.riseger.main.system.compile.function.get.GetMaps_fc;
import org.riseger.main.system.compile.function.get.GetModels_fc;
import org.riseger.main.system.compile.function.graphic.In_fc;
import org.riseger.main.system.compile.function.graphic.Out_fc;
import org.riseger.main.system.compile.function.logic.And_fc;
import org.riseger.main.system.compile.function.logic.Not_fc;
import org.riseger.main.system.compile.function.logic.Or_fc;
import org.riseger.main.system.compile.function.loop.Back_fc;
import org.riseger.main.system.compile.function.loop.IfJump_Pass_fc;
import org.riseger.main.system.compile.function.loop.IfJump_fc;
import org.riseger.main.system.compile.function.main.*;
import org.riseger.main.system.compile.function.math.*;
import org.riseger.main.system.compile.function.number.*;
import org.riseger.main.system.compile.function.preload.Preload_fc;
import org.riseger.main.system.compile.token.Token;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FunctionTypeAdapterFactory implements TypeAdapterFactory {
    private static final Logger LOG = Logger.getLogger(FunctionTypeAdapterFactory.class);
    private final Map<String, Class<? extends Function_c>> typeRegistry = new HashMap<>();

    public FunctionTypeAdapterFactory() {
        typeRegistry.put(Entity_fc.class.getName(), Entity_fc.class);
        typeRegistry.put(Attribute_fc.class.getName(), Attribute_fc.class);
        typeRegistry.put(Coord_fc.class.getName(), Coord_fc.class);
        typeRegistry.put(Coords_fc.class.getName(), Coords_fc.class);
        typeRegistry.put(CoordToRect_fc.class.getName(), CoordToRect_fc.class);
        typeRegistry.put(Distance_fc.class.getName(), Distance_fc.class);
        typeRegistry.put(DotStrings_fc.class.getName(), DotStrings_fc.class);
        typeRegistry.put(Rectangle_fc.class.getName(), Rectangle_fc.class);
        typeRegistry.put(Strings_fc.class.getName(), Strings_fc.class);
        typeRegistry.put(TopCoords_fc.class.getName(), TopCoords_fc.class);
        typeRegistry.put(TopDotStrings_fc.class.getName(), TopDotStrings_fc.class);
        typeRegistry.put(TopStrings_fc.class.getName(), TopStrings_fc.class);
        typeRegistry.put(GetDatabases_fc.class.getName(), GetDatabases_fc.class);
        typeRegistry.put(GetMaps_fc.class.getName(), GetMaps_fc.class);
        typeRegistry.put(GetModels_fc.class.getName(), GetModels_fc.class);
        typeRegistry.put(In_fc.class.getName(), In_fc.class);
        typeRegistry.put(Out_fc.class.getName(), Out_fc.class);
        typeRegistry.put(And_fc.class.getName(), And_fc.class);
        typeRegistry.put(Not_fc.class.getName(), Not_fc.class);
        typeRegistry.put(Or_fc.class.getName(), Or_fc.class);
        typeRegistry.put(Back_fc.class.getName(), Back_fc.class);
        typeRegistry.put(IfJump_fc.class.getName(), IfJump_fc.class);
        typeRegistry.put(IfJump_Pass_fc.class.getName(), IfJump_Pass_fc.class);
        typeRegistry.put(PostWhere_fc.class.getName(), PostWhere_fc.class);
        typeRegistry.put(PreWhere_fc.class.getName(), PreWhere_fc.class);
        typeRegistry.put(Search_fc.class.getName(), Search_fc.class);
        typeRegistry.put(Update_fc.class.getName(), Update_fc.class);
        typeRegistry.put(UseDatabase_fc.class.getName(), UseDatabase_fc.class);
        typeRegistry.put(UseMap_fc.class.getName(), UseMap_fc.class);
        typeRegistry.put(UseModel_fc.class.getName(), UseModel_fc.class);
        typeRegistry.put(UseScope_fc.class.getName(), UseScope_fc.class);
        typeRegistry.put(Where_fc.class.getName(), Where_fc.class);
        typeRegistry.put(Big_fc.class.getName(), Big_fc.class);
        typeRegistry.put(BigEqual_fc.class.getName(), BigEqual_fc.class);
        typeRegistry.put(Equal_fc.class.getName(), Equal_fc.class);
        typeRegistry.put(Small_fc.class.getName(), Small_fc.class);
        typeRegistry.put(SmallEqual_fc.class.getName(), SmallEqual_fc.class);
        typeRegistry.put(AddNumber_fc.class.getName(), AddNumber_fc.class);
        typeRegistry.put(DivideNumber_fc.class.getName(), DivideNumber_fc.class);
        typeRegistry.put(MutiNumber_fc.class.getName(), MutiNumber_fc.class);
        typeRegistry.put(NegivateNumber_fc.class.getName(), NegivateNumber_fc.class);
        typeRegistry.put(SubNumber_fc.class.getName(), SubNumber_fc.class);
        typeRegistry.put(Preload_fc.class.getName(), Preload_fc.class);
        typeRegistry.put(Update_fc.class.getName(), Update_fc.class);
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Function_c.class.isAssignableFrom(type.getRawType())) {
            return null;
        }

        TypeAdapter<JsonElement> jsonElementAdapter = gson.getAdapter(JsonElement.class);
        TypeAdapter<Function_c> defaultAdapter = gson.getDelegateAdapter(this, TypeToken.get(Function_c.class));

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                out.beginObject();
                out.name("className").value(value.getClass().getName());
                if (value instanceof Entity_fc) {
                    Object entity = ((Entity_fc) value).get_entity();
                    if (entity instanceof Double) {
                        out.name("entity").value((Double) ((Entity_fc) value).get_entity());
                    } else if (entity instanceof String) {
                        out.name("entity").value((String) ((Entity_fc) value).get_entity());
                    } else if (entity instanceof Integer) {
                        out.name("entity").value((Integer) ((Entity_fc) value).get_entity());
                    } else {
                        LOG.error("Not has suit entity convert type:" + ((Entity_fc) value).get_entity());
                    }
                }
                out.endObject();
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonObject jsonObject = jsonElementAdapter.read(in).getAsJsonObject();
                String className = jsonObject.get("className").getAsString();
                Class<? extends Function_c> clazz = typeRegistry.get(className);
                try {
                    if (clazz == null) {
                        throw new JsonParseException("Unknown element type: " + className);
                    } else if (clazz.equals(Entity_fc.class)) {
                        // 手动解析 entity 字段
                        JsonElement entityElement = jsonObject.get("entity");
                        Object entity = Token.parseEntity(entityElement);
                        return (T) clazz.getConstructor(Object.class).newInstance(entity);
                    } else {
                        // 使用无参数构造函数clazz
                        return (T) clazz.getConstructor().newInstance();
                    }
                } catch (Exception e) {
                    LOG.error(e);
                }
                return null;
            }

        }.nullSafe();
    }
}