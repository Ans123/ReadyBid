package net.readybid.utils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by DejanK on 2/2/2017.
 *
 */
public class RbMapUtils {

    public static Object getObject(Map map, String path) {
        if(map == null || map.isEmpty() || path == null || path.isEmpty()) return null;
        final String[] keys = path.split("\\.");
        Map nestedMap = map;
        for(int i = 0, l=keys.length-1; i<l; i++){
            final String key = keys[i];
            if(nestedMap.containsKey(key)){
                nestedMap = (Map) nestedMap.get(key);
            } else {
                return null;
            }
        }
        String lastKey = keys[keys.length-1];
        return (nestedMap != null && nestedMap.containsKey(lastKey)) ? nestedMap.get(lastKey) : null;
    }

    public static Integer readInteger(Object value, Integer defaultValue){
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException nfe){
            return defaultValue;
        }
    }

    public static BigDecimal readBigDecimal(Object value) {
        return readBigDecimal(value, BigDecimal.ZERO);
    }

    public static BigDecimal readBigDecimal(Object value, BigDecimal defaultValue) {
        try {
            return new BigDecimal(String.valueOf(value));
        } catch (NumberFormatException nfe){
            return defaultValue;
        }
    }

    public static Boolean readBoolean(Object o) {
        if(o instanceof Boolean) return (Boolean) o;
        return Boolean.parseBoolean(String.valueOf(o));
    }

    public static String getString(Map<String, Object> map, String path) {
        final Object o = getObject(map, path);
        return null == o ? null : String.valueOf(o);
    }
}
