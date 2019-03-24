package Map;

import java.awt.*;
import java.io.Serializable;

public class MapElement implements Serializable {
    private int value;
    private String key;

    public MapElement(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MapElement) {
            return ((MapElement) obj).getKey().equals(this.key);
        }
        return false;
    }
}
