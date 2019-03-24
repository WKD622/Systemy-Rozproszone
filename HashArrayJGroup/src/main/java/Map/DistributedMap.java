package Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DistributedMap implements SimpleStringMap, Serializable {
    private List<MapElement> map = new ArrayList<>();

    @Override
    public boolean containsKey(String key) {
        for (MapElement elem : map) {
            if (elem.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer get(String key) {
        for (MapElement elem : map) {
            if (elem.getKey().equals(key)) {
                return elem.getValue();
            }
        }
        return null;
    }

    @Override
    public void put(String key, Integer value) {
        map.add(new MapElement(key, value));
    }

    @Override
    public Integer remove(String key) {
        int value = this.get(key);
        this.map.remove(new MapElement(key, value));
        return value;
    }

    public void clear() {
        map.clear();
    }

    public void addAll(DistributedMap distributedMap) {
        map.addAll(distributedMap.getList());
    }

    public List getList() {
        return this.map;
    }

    @Override
    public String toString() {
        String out = "";
        for (MapElement element : map)
            out = out + Integer.toString(element.getValue()) + " " + element.getKey() + "\n";
        return out;
    }
}
