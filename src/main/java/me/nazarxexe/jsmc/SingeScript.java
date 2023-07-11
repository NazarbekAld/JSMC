package me.nazarxexe.jsmc;


import com.caoccao.javet.enums.JSRuntimeType;
import com.caoccao.javet.enums.V8AwaitMode;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetBiConsumer;
import com.caoccao.javet.interfaces.IJavetUniIndexedConsumer;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.converters.JavetBridgeConverter;
import com.caoccao.javet.interop.converters.JavetProxyConverter;

import com.caoccao.javet.interop.engine.IJavetEngine;
import com.caoccao.javet.interop.engine.JavetEnginePool;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.reference.*;
import lombok.Getter;
import me.nazarxexe.jsmc.js.JSLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class SingeScript implements Project {

    V8Runtime runtime;

    public final File file;

    @Getter
    private Logger logger;
    private JSLogger scriptLogger;

    @Getter
    private boolean enabled = false;

    @Getter
    private V8ValueObject config;

    private final Plugin plugin;

    public SingeScript(File file, Plugin plugin) {
        this.file = file;
        this.plugin = plugin;

        logger = LoggerFactory.getLogger(this.file.getName());

    }

    @Override
    public void eval() {
        try {
            runtime = V8Host.getV8Instance().createV8Runtime();

            runtime.setConverter(new JavetProxyConverter());

            scriptLogger = new JSLogger(logger, runtime);
            scriptLogger.register(runtime.getGlobalObject());

            runtime.setConverter(new JavetBridgeConverter());
            runtime.getGlobalObject().set("Bukkit", Bukkit.class);
            runtime.getGlobalObject().set("JavaClass", Class.class);
            runtime.getGlobalObject().set("Loader", this);

            runtime.setV8ModuleResolver((runtime, resourceName, v8ModuleReferrer) -> {
                runtime.setConverter(new JavetProxyConverter());
                runtime.setConverter(new JavetBridgeConverter());
                runtime.getGlobalObject().set("Bukkit", Bukkit.class);
                runtime.getGlobalObject().set("JavaClass", Class.class);
                String newResourceName = removePrefix(resourceName, "..");
                File f = new File(plugin.getDataFolder().getAbsolutePath(), newResourceName);
                try {
                    return runtime.getExecutor(FileUtils.fileRead(f))
                            .setResourceName(f.getAbsolutePath())
                            .compileV8Module();
                } catch (IOException e) {
                    logger.error("Failed to import " + resourceName + "!", e);
                    return null;
                }
            });

            runtime.getExecutor(file)
                    .setModule(true)
                    .setResourceName(file.getAbsolutePath())
                    .executeVoid();

            enabled = true;
        } catch (JavetException e) {
            logger.error("Failed to evaluate the file.", e);
        }
    }

    @Override
    public void stop() {
        try {
            runtime.lowMemoryNotification();

            runtime.await();
            runtime.close();
            enabled = false;
        } catch (JavetException e) {
            logger.error("Failed to close engine.", e);
        }
    }

    @Override
    public File file() {
        return file;
    }

    private String removePrefix(String s, String prefix) {
        if (s != null && prefix != null && s.startsWith(prefix)) {
            return s.substring(prefix.length());
        }
        return s;
    }

}
