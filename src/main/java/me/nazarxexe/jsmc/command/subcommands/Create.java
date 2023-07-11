package me.nazarxexe.jsmc.command.subcommands;

import me.nazarxexe.jsmc.JSMC;
import me.nazarxexe.jsmc.SingeScript;
import me.nazarxexe.jsmc.command.JSMCSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Create implements JSMCSubCommand {

    final Plugin plugin;

    public Create(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String name() {
        return "create";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            File file = new File(JSMC.getScripts(), args[1] + ".js");


            try {
                if (!file.createNewFile()) {
                    sender.sendMessage("Project already exist.");
                    return;
                }

                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        FileWriter writer = new FileWriter(file);
                        sender.sendMessage("Downloading sample from central...");
                        try (InputStream stream = GitHub.connectAnonymously().getRepositoryById(664824347)
                                     .getFileContent("sample/example.js")
                                     .read()) {
                            writer.write(new String(stream.readAllBytes()));
                        }
                        writer.flush();
                        writer.close();
                        sender.sendMessage("Project created. Enable it via command.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return new ArrayList<>() {{ add("<{project name}>"); }};
    }
}
