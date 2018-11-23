package net.readybid.web;

import net.readybid.entities.Id;

import java.util.HashMap;
import java.util.Map;

public class RbIdentifiableSingletonViewModel implements RbViewModel {

    private final Map<String, Object> data;

    public RbIdentifiableSingletonViewModel(Id id, String key, Object value){
        data = new HashMap<>();
        data.put("id", id);
        data.put(key, value);
    }

    public RbIdentifiableSingletonViewModel(String id, String key, Object value){
        this(Id.valueOf(id), key, value);
    }

    public Object getData(){
        return data;
    }
}
