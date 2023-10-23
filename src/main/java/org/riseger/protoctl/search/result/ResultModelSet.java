package org.riseger.protoctl.search.result;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ResultModelSet {
    @Getter
    @Setter
    private String modelName;

    private List<ResultElement> resultElements;

    public void add(ResultElement resultElement) {
        this.resultElements.add(resultElement);
    }
}
