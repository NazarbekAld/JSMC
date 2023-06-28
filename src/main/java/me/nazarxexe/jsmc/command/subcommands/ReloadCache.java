package me.nazarxexe.jsmc.command.subcommands;

import me.nazarxexe.jsmc.JSMC;
import me.nazarxexe.jsmc.Script;
import me.nazarxexe.jsmc.command.JSMCSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReloadCache implements JSMCSubCommand {

    final Plugin plugin;

    public ReloadCache(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String name() {
        return "ReloadCache";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        for (File file : listFilesUsingJavaIO(JSMC.getScripts().getAbsolutePath())) {
            for (Script script : JSMC.activeScripts) {
                if (script.file.getName().equals(file.getName())) return;

                JSMC.activeScripts.add(new Script(file, plugin));
            }}

    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    private Set<File> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .collect(Collectors.toSet());
    }
}
