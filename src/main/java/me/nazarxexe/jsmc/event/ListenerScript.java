package me.nazarxexe.jsmc.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.nazarxexe.jsmc.Script;

@Data
@AllArgsConstructor
public class ListenerScript {
    private RunnableListener function;
    private String event;
    private Script script;
}
