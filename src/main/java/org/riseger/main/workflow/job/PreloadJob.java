package org.riseger.main.workflow.job;

import org.riseger.protoctl.request.PreloadRequest;
import org.riseger.protoctl.struct.model.Database;

import java.util.List;

public class PreloadJob implements Job<PreloadRequest> {
    private List<Database> databases;

    @Override
    public PreloadJob warp(PreloadRequest request) {
        this.databases = request.getDatabases();
        return this;
    }

    @Override
    public void run() {

    }
}
