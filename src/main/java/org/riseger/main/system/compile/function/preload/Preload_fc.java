package org.riseger.main.system.compile.function.preload;

import com.google.gson.reflect.TypeToken;
import org.riseger.main.system.CacheSystem;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.PreloadException;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.serializer.JsonSerializer;
import org.riseger.protoctl.struct.entity.Database;
import org.riseger.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Preload_fc extends Function_c {

    public Preload_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        List<Database> databases = JsonSerializer.deserialize(Utils.getText(new File((String) searchMemory.poll())).getBytes(StandardCharsets.UTF_8),
                new TypeToken<LinkedList<Database>>() {
                }.getType());
        for (Database database : databases) {
            try {
                CacheSystem.INSTANCE.getDatabasesManager().preloadDatabase(database);
            } catch (IOException e) {
                throw new PreloadException(e.getMessage());
            }
        }
    }

}
