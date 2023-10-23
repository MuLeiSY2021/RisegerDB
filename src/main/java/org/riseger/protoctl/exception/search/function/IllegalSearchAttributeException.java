package org.riseger.protoctl.exception.search.function;

public class IllegalSearchAttributeException extends Exception {
    public IllegalSearchAttributeException(String attribute) {
        super("Element Not_f has this:" + attribute);
    }
}
