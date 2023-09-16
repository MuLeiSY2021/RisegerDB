package org.riseger.protoctl.response;

import lombok.Getter;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.message.BasicMessage;

import java.util.List;
import java.util.Map;

@Getter
public class SearchResponse extends BasicMessage {

    private boolean success;

    private Map<String, List<Element_c>> response;

    private Exception exception;


    public SearchResponse() {
        super(SearchResponse.class);
    }

    public void success(Map<String, List<Element_c>> response) {
        this.success = true;
        this.response = response;
    }

    public void failed(Exception e) {
        this.success = false;
        this.exception = e;
    }

    @Override
    public Job warp() {
        return null;
    }
}
