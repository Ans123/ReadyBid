package net.readybid.app.core.entities.rfp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class QuestionnaireConfigurationItem {

    public String id;
    public Map<String, Object> data;

    public QuestionnaireConfigurationItem() {}

    public QuestionnaireConfigurationItem(String id, Map<String, Object> data) {
        this.id = id;
        this.data = data;
    }

    public boolean includes(Map<String, Object> forFilters) {
        if(forFilters == null) return true;
        for(String filter : forFilters.keySet()){
            if(!data.containsKey(filter)) return false;
            final List<?> configList = ((List) data.get(filter));
            final Object forFilter = forFilters.get(filter);
            if(!normalizedContains(configList, forFilter)) return false;
        }
        return true;
    }

    private boolean normalizedContains(List<?> configList, Object forFilter) {
        try{
            return containsAsNumeric(configList, forFilter);
        } catch (Exception e){
            return containsAsString(configList, forFilter);
        }
    }

    private boolean containsAsString(List<?> configList, Object forFilter) {
        final String ff = String.valueOf(forFilter);
        for(Object o : configList){
            if(ff.equalsIgnoreCase(String.valueOf(o))) return true;
        }
        return false;
    }

    private boolean containsAsNumeric(List<?> configList, Object forFilter) {
        final BigDecimal ff = new BigDecimal(String.valueOf(forFilter));
        for(Object o : configList){
            if(ff.compareTo(new BigDecimal(String.valueOf(o))) == 0) return true;
        }
        return false;
    }
}
