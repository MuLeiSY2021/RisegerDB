package org.riseger.protoctl.compiler.result;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class ResultModelSet {

    @Setter
    private String modelName;

    private final List<ResultElement> resultElements = new LinkedList<>();

    public void add(ResultElement resultElement) {
        this.resultElements.add(resultElement);
    }
}
