package io.ipoli.android.app.services.analytics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 1/7/16.
 */

public class EventParams {
    private Map<String, String> params = new HashMap<>();

    private EventParams() {
    }

    public static EventParams create() {
        return new EventParams();
    }

    public static EventParams of(String key, String value) {
        EventParams eventParams = new EventParams();
        eventParams.params.put(key, value);
        return eventParams;
    }

    public EventParams add(String key, String value) {
        params.put(key, value);
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }
}