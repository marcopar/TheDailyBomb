package eu.flatworld.android.thedailybomb;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by marcopar on 31/12/17.
 */

public class Bomb implements Serializable {

    String id;
    Long timestamp;

    public Bomb() {

    }

    public Bomb(String json) {
        fromJson(json);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void fromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            id = null;
            timestamp = null;
            return;
        }
        try {
            JSONObject jso = new JSONObject(json);
            id = jso.getString("id");
            timestamp = jso.getLong("timestamp");
        } catch (Exception ex) {
            id = null;
            timestamp = null;
        }
    }

    public JSONObject toJson() {
        try {
            JSONObject jso = new JSONObject();
            jso.put("id", id);
            jso.put("timestamp", timestamp);
            return jso;
        } catch (Exception ex) {
            return null;
        }
    }

}
