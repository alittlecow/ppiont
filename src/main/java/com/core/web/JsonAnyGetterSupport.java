package com.core.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Huzl
 * @version 1.0.0
 */
public class JsonAnyGetterSupport implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Map<String,Object> attrs = new HashMap<String, Object>();

    @com.fasterxml.jackson.annotation.JsonAnyGetter
    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public JsonAnyGetterSupport putAttr(String key,Object value){
        attrs.put(key,value);
        return this;
    }
}
