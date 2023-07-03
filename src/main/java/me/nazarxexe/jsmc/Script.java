package me.nazarxexe.jsmc;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.Getter;
import me.nazarxexe.jsmc.event.Listener;
import me.nazarxexe.jsmc.scriptcommands.ScriptCommandMap;
import me.nazarxexe.jsmc.tasks.JSTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Script {
    @Getter
    private GraalJSScriptEngine engine;
    public final File file;
    public final Plugin plugin;
    @Getter
    private Logger logger;

    @Getter
    private boolean enabled = false;

    private final Listener listener;

    private Value props;

    public Script(File file, Plugin plugin) {
        this.file = file;
        this.plugin = plugin;

        this.listener = new Listener(this);
//        try {
//            Require.enable(engine, FilesystemFolder.create(plugin.getDataFolder(), "UTF-8"));
//        } catch (ScriptException e) {
//            logger.error("Failed to apply requirejs to the script.");
//        }

    }

    public void eval() {

        engine = GraalJSScriptEngine.create(null, Context.newBuilder("js")
                .allowNativeAccess(true)
                .allowHostClassLoading(true)
                .allowIO(true)
                .allowHostAccess(true)
                .allowHostClassLoading(true)
        );
        logger = LoggerFactory.getLogger(file.getName());

        HashMap<String, Object> bindings = new HashMap<>();
        bindings.put("console", logger);
        bindings.put("server", Bukkit.getServer());
        bindings.put("root", this);
        bindings.put("w", new JSTask(this));
        bindings.put("listener", listener);
        bindings.put("commandMap", ScriptCommandMap.getInstance());

        bindings.forEach((k,v) -> {
            engine.getPolyglotContext().getPolyglotBindings().putMember(k,v);
        });

        JSMC.logger.info("Evaluating " + file.getName() + "...");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {

                props = engine.getPolyglotContext().eval(Source.newBuilder("js", file).build());
                logger = LoggerFactory.getLogger(props.getMember("name").asString());

            } catch (IOException e) {
                JSMC.logger.error("Failed to execute script " + file.getName() + " !", e);
            }
            JSMC.logger.info("Successfully evaluated " + file.getName() + ".");
            Bukkit.getScheduler().runTask(plugin, this::start);
        });
    }

    public void start() {
        logger.info("Starting...");

        try {
            Value value = props.getMember("onEnable");

            if (value.isNull()) {
                throw new ScriptException("Please specify the enable prop of script.");
            }
            value.executeVoid();
            logger.info("Started.");
            enabled = true;
        }catch (Exception e){
            logger.info("Failed to start the script.", e);
        }

    }

    public void stop() {
        logger.info("Disabling...");

        try {
            Value value = props.getMember("onDisable");

            if (value.isNull()) {
                logger.warn("Please specify disable prop of script");
                listener.unRegisterAll();
                ScriptCommandMap.getInstance().unRegisterAll(this);
                enabled = false;
                engine.close();
                return;
            }
            value.executeVoid();
            logger.info("Disabled.");
            enabled = false;
            listener.unRegisterAll();
            ScriptCommandMap.getInstance().unRegisterAll(this);
            engine.close();
        }catch (Exception e){
            logger.info("Failed to stop the script.", e);
        }
    }
}
