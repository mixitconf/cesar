package org.mixit.cesar.site.model;

import com.fasterxml.jackson.annotation.JsonView;

public class Tuple {
    @JsonView({FlatView.class, ListView.class})
    private Object key;
    @JsonView({FlatView.class, ListView.class})
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
