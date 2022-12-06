package info.victorchu.commontool.struct;

/**
 * 从对象中抽取Key。重写了equals方法和hashCode方法
 * @Description:
 * @Date:2022/12/6 16:30
 * @Author:victorchutian
 */
import java.util.LinkedHashMap;
import java.util.Map;

public class KeyOfObject {

    private LinkedHashMap<String,Object> fieldMap = new LinkedHashMap<>();

    public LinkedHashMap<String, Object> getFieldMap() {
        return fieldMap;
    }

    public void addField(String key,Object field) {
        if(fieldMap.containsKey(key)){
            throw new IllegalArgumentException();
        }
        this.fieldMap.put(key,fieldMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyOfObject keys = (KeyOfObject) o;
        if(keys.getFieldMap() == null || !keys.getFieldMap().keySet().equals(fieldMap.keySet())){
            return false;
        }
        for (Map.Entry<String, Object> item: fieldMap.entrySet()) {
            String key = item.getKey();
            if(item.getValue() != null ? !(item.getValue().equals(keys.getFieldMap().get(key)))
                    :  keys.getFieldMap().get(key) != null){
                return false;

            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        if(fieldMap.size() == 0){
            return result;
        }
        for (Map.Entry<String, Object> item: fieldMap.entrySet()) {
            result = 31 * result + (item.getValue() != null ? item.getValue().hashCode() :0);
        }
        return result;
    }
}