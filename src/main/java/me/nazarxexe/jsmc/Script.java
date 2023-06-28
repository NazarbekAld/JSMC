package me.nazarxexe.jsmc;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.oracle.truffle.js.scriptengine.GraalJSEngineFactory;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.Getter;
import lombok.Setter;
import me.nazarxexe.jsmc.tasks.JSTask;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.codehaus.plexus.util.FileUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.File;
import java.io.IOException;

public class Script {
    public final ScriptEngine engine;
    public final File file;
    public final Plugin plugin;
    public final Logger logger;

    @Getter
    private boolean enabled = false;

    public Script(File file, Plugin plugin) {
        this.engine = GraalJSScriptEngine.create(null, Context.newBuilder("js")
                .allowNativeAccess(true)
                .allowHostClassLoading(true)
                .allowIO(true)
                .allowHostAccess(true)
                .allowHostClassLoading(true)
        );
        this.file = file;
        this.plugin = plugin;

        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        this.logger = LoggerFactory.getLogger(file.getName());
        bindings.put("console", logger);
        bindings.put("server", Bukkit.getServer());
        bindings.put("root", this);
        bindings.put("scheduler", new JSTask(this));
        bindings.put("listener", JSMC.getListener());

    }

    public void eval() {
        logger.info("Evaluating...");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                engine.eval(FileUtils.fileRead(file));
                logger.info("Evaluated.");
                Bukkit.getScheduler().runTask(plugin, this::start);
                enabled = true;
            } catch (ScriptException | IOException e) {
                logger.info("Failed to evaluate.", e);
            }
        });
    }

    public void start() {

        Invocable invocable = (Invocable) engine;
        try {
            invocable.invokeFunction("start");
        } catch (ScriptException | NoSuchMethodException e) {
            logger.info("Failed to start", e);
        }

    }

    public void stop() {
        Invocable invocable = (Invocable) engine;
        try {
            invocable.invokeFunction("stop");
        } catch (ScriptException | NoSuchMethodException e) {
            logger.info("Failed to start", e);
        }
        JSMC.getListener().unRegisterAll(this);
        enabled = false;
    }

}
