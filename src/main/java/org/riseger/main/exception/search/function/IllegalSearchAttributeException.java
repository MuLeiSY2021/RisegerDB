package org.riseger.main.exception.search.function;

public class IllegalSearchAttributeException extends Exception {
    public IllegalSearchAttributeException(String attribute) {
        super("Element not has this:" + attribute);
    }
}
