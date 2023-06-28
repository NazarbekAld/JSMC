package me.nazarxexe.jsmc.event;

import org.bukkit.event.Event;

@FunctionalInterface
public interface RunnableListener {

    void run(Event event);

}
