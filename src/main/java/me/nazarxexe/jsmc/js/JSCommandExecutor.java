package me.nazarxexe.jsmc.js;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface JSCommandExecutor {
    boolean execute(CommandSender sender, String[] args);
    List<String> tab(CommandSender sender, String[] args);
}
