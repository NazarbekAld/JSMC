package me.nazarxexe.jsmc.js;

import com.caoccao.javet.values.primitive.V8ValueString;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import lombok.Setter;
import me.nazarxexe.jsmc.Project;
import me.nazarxexe.jsmc.js.customevents.ScriptDisableEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JSEventManager {

    private static JSEventManager instance;

    private final List<JSListenerData> listenerDataList = new ArrayList<>();

    @Setter
    private Plugin plugin;

    private static final HashMap<String, Class<? extends Event>> eventClasses = new HashMap<>();

    private JSEventManager() {
    }

    public static JSEventManager getInstance() {
        if (instance == null) {
            instance = new JSEventManager();
        }
        return instance;
    }

    public static void registerAllEvents() {

        Logger logger = LoggerFactory.getLogger("JSEventManager");
        ClassInfoList events = new ClassGraph()
                .enableClassInfo()
                .scan() //you should use try-catch-resources instead
                .getClassInfo(Event.class.getName())
                .getSubclasses()
                .filter(info -> !info.isAbstract());

        try {
            for (ClassInfo event : events) {
                //noinspection unchecked
                Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName(event.getName());

                if (Arrays.stream(eventClass.getDeclaredMethods()).anyMatch(method ->
                        method.getParameterCount() == 0 && method.getName().equals("getHandlers"))) {
                    eventClasses.put(eventClass.getSimpleName(), eventClass);
                    logger.info("Found " + eventClass.getSimpleName() + ".");
                }
            }

        } catch (ClassNotFoundException e) {
            throw new AssertionError("Scanned class wasn't found", e);
        }
    }

    public void register(Project loader, String eventName, JSListener listener, V8ValueString priority) {

        if (eventName.equals("ScriptDisableEvent")) {
            plugin.getServer().getPluginManager().registerEvents(new Listener() {

                final Project project = loader;
                final JSListener executable = listener;

                @EventHandler
                public void onDisable(ScriptDisableEvent e) {
                    System.out.println("Disable");
                    if (!(e.getProject().file().equals(project.file()))) return;
                    executable.listen(e);
                }

            }, plugin);
            return;
        }

        Class<? extends Event> evc = eventClasses.get(eventName);

        Bukkit.getServer().getPluginManager().registerEvent(evc, new Listener() {
        }, priority == null ? EventPriority.NORMAL : EventPriority.valueOf(priority.getValue()), new EventExecutor() {
            final Project project = loader;
            JSListener executable = listener;

            @Override
            public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
                if (!project.isEnabled()) return;
                executable.listen(event);
            }
        }, plugin, false);

    }


}
