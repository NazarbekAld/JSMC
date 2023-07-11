package me.nazarxexe.jsmc.command.subcommands;

import me.nazarxexe.jsmc.JSMC;
import me.nazarxexe.jsmc.Project;
import me.nazarxexe.jsmc.SingeScript;
import me.nazarxexe.jsmc.command.JSMCSubCommand;
import me.nazarxexe.jsmc.js.customevents.ScriptDisableEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class Disable implements JSMCSubCommand {
    @Override
    public String name() {
        return "disable";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        SingeScript script = null;
        for (Project activeScript : JSMC.activeScripts) {
            if (!(activeScript.file().getName().equals(args[1]))) return;
            script = (SingeScript) activeScript;
        }

        if (script == null) {
            sender.sendMessage(ChatColor.RED + "404 Not found.");
            return;
        }
        if (!script.isEnabled()) {
            sender.sendMessage(ChatColor.RED + "Script is already disabled.");
            return;
        }

        Bukkit.getServer().getPluginManager().callEvent(new ScriptDisableEvent(script));
        script.stop();
        sender.sendMessage(ChatColor.GREEN + "Disabling the script... Do not forget to check the console.");
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return JSMC.getActiveScripts().stream().map(script -> script.file().getName()).collect(Collectors.toList());
    }
}
