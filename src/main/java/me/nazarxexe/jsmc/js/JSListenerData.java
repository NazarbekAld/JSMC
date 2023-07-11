package me.nazarxexe.jsmc.js;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.nazarxexe.jsmc.Project;

@Data
@AllArgsConstructor
public class JSListenerData {

    Project loader;
    JSListener listener;
    String eventName;

}
