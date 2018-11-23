package net.readybid.mongodb;

import org.bson.Document;

public class RbDocument extends Document {

    public RbDocument(){
        super();
    }

    public Object putIfNotNull(String key, Object value){
        if(value != null){
            return this.put(key, value);
        }
        return null;
    }
}
