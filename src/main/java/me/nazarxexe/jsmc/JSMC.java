package me.nazarxexe.jsmc;

import lombok.Getter;
import lombok.Setter;
import me.nazarxexe.jsmc.command.JSMCCommand;
import me.nazarxexe.jsmc.command.subcommands.Disable;
import me.nazarxexe.jsmc.command.subcommands.Enable;
import me.nazarxexe.jsmc.command.subcommands.ReloadCache;
import me.nazarxexe.jsmc.command.subcommands.Scripts;
import me.nazarxexe.jsmc.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class JSMC extends JavaPlugin {
    @Getter
    private static File scripts;
    @Getter
    @Deprecated // DO NOT USE FOR NOW.
    private static File libs;
    @Getter
    public final static List<Script> activeScripts = new ArrayList<>();
    @Getter
    private static Listener listener;

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        getLogger().info("Loading all events...");
        listener=new Listener(this);
        activeScripts.clear();
        scripts = new File(getDataFolder(), "scripts");
//        libs = new File(getDataFolder(), "libs");
//        libs.mkdir();
        scripts.mkdir();
        Set<File> files = listFilesUsingJavaIO(scripts.getAbsolutePath());

        for (File file : files) {
            Script script = new Script(file, this);
            script.eval();
            activeScripts.add(script);
        }

        JSMCCommand command = new JSMCCommand();

        command.getSubCommands().add(new Disable());
        command.getSubCommands().add(new Enable());
        command.getSubCommands().add(new ReloadCache(this));
        command.getSubCommands().add(new Scripts());

        getCommand("jsmc").setExecutor(command);
        getCommand("jsmc").setTabCompleter(command);

    }

    @Override
    public void onDisable() {
        for (Script activeScript : activeScripts) {
            activeScript.stop();
        }
    }

    private Set<File> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .collect(Collectors.toSet());
    }


}
