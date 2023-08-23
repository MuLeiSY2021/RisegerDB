package org.riseger.main.storage.filesystem;

import org.riseger.main.api.workflow.workflow.CommonWorkFlow;
import org.riseger.main.cache.entity.element.Database_c;
import org.riseger.main.storage.request.PreloadDBFSRequest;

public class RDBFielSystem {
    public RDBFielSystem INSTANCE = new RDBFielSystem();

    private CommonWorkFlow workFlow = new CommonWorkFlow();

    public void serializeWholeDB(Database_c database) {
        workFlow.push(new PreloadDBFSRequest(database));
    }
}
