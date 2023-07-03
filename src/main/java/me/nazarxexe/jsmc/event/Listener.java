package me.nazarxexe.jsmc.event;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import me.nazarxexe.jsmc.JSMC;
import me.nazarxexe.jsmc.Script;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.graalvm.polyglot.Value;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Listener implements org.bukkit.event.Listener {

    final List<Script> script = JSMC.activeScripts;
    final Script root;

    final org.bukkit.event.Listener listener = new org.bukkit.event.Listener() {};

    public static final List<Class<? extends Event>> eventClasses = new ArrayList<>();

    public static void scanEventClasses(Plugin plugin) {
        ClassInfoList events = new ClassGraph()
                .enableClassInfo()
                .scan()
                .getClassInfo(Event.class.getName())
                .getSubclasses()
                .filter(info -> !info.isAbstract());

        try {
            for (ClassInfo event : events) {
                Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName(event.getName());

                if (Arrays.stream(eventClass.getDeclaredMethods()).anyMatch(method ->
                        method.getParameterCount() == 0 && method.getName().equals("getHandlers"))) {
                    eventClasses.add(eventClass);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new AssertionError("Scanned class wasn't found", e);
        }
    }


    public Listener(Script root) {
        this.root = root;
    }

    public void registerListener(String event, Value function) {

        Class<? extends Event> eventClass = eventClasses.stream()
                .filter((evc) -> evc.getSimpleName().equals(event))
                .findAny().orElseThrow();

        Bukkit.getPluginManager().registerEvent(eventClass,
                listener, EventPriority.NORMAL, new EventExecutor() {
                            private final Value listenerFunc = function;
                            @Override
                            public void execute(org.bukkit.event.@NotNull Listener listener, @NotNull Event event) throws EventException {
                                listenerFunc.execute(event);
                            }

                        }, root.plugin);
    }

    public void unRegisterAll() {
        HandlerList.unregisterAll(listener);
    }

}
