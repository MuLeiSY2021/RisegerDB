package org.riseger.main.compiler.function.preload;

import com.google.gson.reflect.TypeToken;
import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
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

    public Preload_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        List<Database> databases = JsonSerializer.deserialize(Utils.getText(new File((String) poll())).getBytes(StandardCharsets.UTF_8),
                new TypeToken<LinkedList<Database>>() {
                }.getType());
        for (Database database : databases) {
            try {
                CacheMaster.INSTANCE.getDatabaseManager().preloadDatabase(database);
            } catch (IOException e) {
                throw new PreloadException(e.getMessage());
            }
        }
    }

}
