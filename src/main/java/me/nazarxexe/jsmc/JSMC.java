package me.nazarxexe.jsmc;

import lombok.Getter;
import me.nazarxexe.jsmc.command.JSMCCommand;
import me.nazarxexe.jsmc.command.subcommands.*;
import me.nazarxexe.jsmc.command.subcommands.module.Module;
import me.nazarxexe.jsmc.command.subcommands.module.ModuleImport;
import me.nazarxexe.jsmc.js.JSEventManager;
import me.nazarxexe.jsmc.js.customevents.ScriptDisableEvent;
import me.nazarxexe.jsmc.module.ModuleManager;
import org.apache.maven.model.Repository;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class JSMC extends JavaPlugin {
    @Getter
    private static File scripts;
    @Getter
    private static File libs;

    @Getter
    public final static List<Project> activeScripts = new ArrayList<>();
    public static final Logger logger = LoggerFactory.getLogger("JSMC");

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        logger.info("Loading all events... Please wait.");
        JSEventManager.getInstance()
                .setPlugin(this);
        JSEventManager.registerAllEvents();
        ModuleManager.getInstance().init(this);

        activeScripts.clear();
        scripts = new File(getDataFolder(), "scripts");
        libs = new File(getDataFolder(), "libs");
        boolean exist = libs.mkdir();
        if (exist) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                ModuleManager.getInstance().importAllTo(libs, ModuleManager.getInstance().getKnownModuleByName("std"), null);
            });
        }
        scripts.mkdir();
        Set<File> files = listFilesUsingJavaIO(scripts.getAbsolutePath());

        for (File file : files) {
            SingeScript script = new SingeScript(file, this);
            script.eval();
            activeScripts.add(script);
        }

        JSMCCommand command = new JSMCCommand();

        command.getSubCommands().add(new Disable());
        command.getSubCommands().add(new Enable());
        command.getSubCommands().add(new Scripts());
        command.getSubCommands().add(new Create(this));

        Module moduleCmd = new Module(this);
        moduleCmd.subCommands.add(new ModuleImport(this));
        command.getSubCommands().add(moduleCmd);

        getCommand("jsmc").setExecutor(command);
        getCommand("jsmc").setTabCompleter(command);

    }

    @Override
    public void onDisable() {
        for (Project activeScript : activeScripts) {
            Bukkit.getServer().getPluginManager().callEvent(new ScriptDisableEvent(activeScript));
            activeScript.stop();
        }
    }

    private Set<File> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .collect(Collectors.toSet());
    }


}
