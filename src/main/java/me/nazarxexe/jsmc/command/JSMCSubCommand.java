package me.nazarxexe.jsmc.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface JSMCSubCommand {

    String name();
    void execute(CommandSender sender, String[] args);
    List<String> tab(CommandSender sender, String[] args);

}
