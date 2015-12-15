package com.cellum.rnd.protoapp.utils.json;

import android.graphics.Rect;

import com.cellum.rnd.protoapp.Action;
import com.cellum.rnd.protoapp.Event;
import com.cellum.rnd.protoapp.Screen;
import com.cellum.rnd.protoapp.ScreenFlow;
import com.cellum.rnd.protoapp.TapEvent;

import roboguice.inject.RoboInjector;

/**
 * Created by oszkotamas on 27/09/15.
 */
public class JsonMapper {

    private final RoboInjector injector;

    public JsonMapper(RoboInjector injector) {
        this.injector = injector;
    }

    private Rect rect(String json) {
        String[] parts = json.trim().split(",");
        if(parts.length == 4) {
            return new Rect(Integer.parseInt(parts[0].trim())
                    ,Integer.parseInt(parts[1].trim())
                    ,Integer.parseInt(parts[2].trim())
                    ,Integer.parseInt(parts[3].trim()));
        }
        return null;
    }

    private Action action(JsonAction json) {
        if("push".equals(json.type)) {
            return new Action.PushScreen(injector, json.param);
        } else if("replace".equals(json.type)) {
            return new Action.ReplaceScreen(injector, json.param);
        } else if("back".equals(json.type)) {
            return new Action.Back(injector);
        } else if("main".equals(json.type)) {
            return new Action.Main(injector);
        }
        return null;
    }

    private Event<?> event(JsonEvent json) {
        if("tap".equals(json.type)) {
            TapEvent event = new TapEvent(injector, rect(json.param));
            for(JsonAction jsonAction : json.actions) {
                event.on(action(jsonAction));
            }
            return event;
        }
        return null;
    }

    private Screen screen(JsonScreen json) {
        Screen.Builder builder = new Screen.Builder().name(json.name);
        for(JsonEvent event : json.events) {
            builder.event(event(event));
        }
        return builder.build();
    }

    public ScreenFlow screenFlow(JsonScreenFlow json) {
        ScreenFlow screenFlow = new ScreenFlow();
        for(JsonScreen screen : json.screens) {
            screenFlow.add(screen(screen));
        }
        return screenFlow;
    }

}
