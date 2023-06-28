package me.nazarxexe.jsmc.event;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import me.nazarxexe.jsmc.JSMC;
import me.nazarxexe.jsmc.Script;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.graalvm.polyglot.Value;
import org.jetbrains.annotations.NotNull;

import javax.script.Invocable;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Listener implements org.bukkit.event.Listener {

    final List<Script> script = JSMC.activeScripts;
    final Plugin plugin;
    @Getter
    List<ListenerScript> functions;

    public Listener(Plugin plugin) {
        this.plugin = plugin;

        this.functions = new ArrayList<>();

        org.bukkit.event.Listener listener = new org.bukkit.event.Listener() {};
        EventExecutor executor = new EventExecutor() {
            @Override
            public void execute(org.bukkit.event.@NotNull Listener listener, @NotNull Event event) throws EventException {
                for (ListenerScript function : functions) {
                    if(!(event.getEventName().equals(function.getEvent()))) continue;
                    Invocable invocable = (Invocable) function.getScript().engine;

                    try {
                        invocable.invokeFunction(function.getFunction(), event);
                    } catch (ScriptException | NoSuchMethodException e) {
                        function.getScript().logger.error("Failed to execute function listener.", e);
                    }
                }
            }
        };

         ClassInfoList events = new ClassGraph()
                .enableClassInfo()
                 .verbose()
                .scan() //you should use try-catch-resources instead
                .getClassInfo(Event.class.getName())
                .getSubclasses()
                .filter(info -> !info.isAbstract());

        try {
            for (ClassInfo event : events) {
                Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName(event.getName());

                if (Arrays.stream(eventClass.getDeclaredMethods()).anyMatch(method ->
                        method.getParameterCount() == 0 && method.getName().equals("getHandlers"))) {
                    Bukkit.getPluginManager().registerEvent(eventClass, listener,
                            EventPriority.NORMAL, executor, plugin);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new AssertionError("Scanned class wasn't found", e);
        }

    }

    public void registerListener(String event, String function, Script script) {
        functions.add(new ListenerScript(function, event, script));
    }

    public void unRegisterAll(Script script) {
        functions.removeIf((func) -> func.getScript().file.getName().equals(script.file.getName()));
    }

}
