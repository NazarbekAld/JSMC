package me.nazarxexe.jsmc.js.customevents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.nazarxexe.jsmc.Project;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class ScriptDisableEvent extends Event {

    @Getter
    private Project project;

    @Getter
    private static HandlerList handlerList = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
