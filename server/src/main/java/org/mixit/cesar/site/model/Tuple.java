package org.mixit.cesar.site.model;

public class Tuple {

    private Object key;
    private Object value;

    public Object getKey() {
        return key;
    }

    public Tuple setKey(Object key) {
        this.key = key;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public Tuple setValue(Object value) {
        this.value = value;
        return this;
    }
}
