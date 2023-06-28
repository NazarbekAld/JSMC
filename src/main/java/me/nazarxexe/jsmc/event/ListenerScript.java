package me.nazarxexe.jsmc.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.nazarxexe.jsmc.Script;

@Data
@AllArgsConstructor
public class ListenerScript {
    private String function;
    private String event;
    private Script script;
}
