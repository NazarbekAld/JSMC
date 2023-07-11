package me.nazarxexe.jsmc.command.subcommands;

import me.nazarxexe.jsmc.JSMC;
import me.nazarxexe.jsmc.Project;
import me.nazarxexe.jsmc.SingeScript;
import me.nazarxexe.jsmc.command.JSMCSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Scripts implements JSMCSubCommand {
    @Override
    public String name() {
        return "scripts";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (JSMC.activeScripts.isEmpty()) {
            sender.sendMessage("Script folder is empty.");
            return;
        }

        sender.sendMessage("List of available scripts:");
        for (Project activeScript : JSMC.getActiveScripts()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', activeScript.isEnabled() ? "&a" : "&c") + ((SingeScript)activeScript).file.getName());
        }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
