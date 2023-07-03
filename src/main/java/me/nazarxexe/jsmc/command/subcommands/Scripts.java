package me.nazarxexe.jsmc.command.subcommands;

import me.nazarxexe.jsmc.JSMC;
import me.nazarxexe.jsmc.Script;
import me.nazarxexe.jsmc.command.JSMCSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.script.ScriptEngineFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        ScriptEngineFactory info = JSMC.activeScripts.get(0).getEngine().getFactory();

        sender.sendMessage(ChatColor.GRAY + "JSEngine: " + info.getEngineName(), "V" + info.getEngineVersion(), " | JS-" + info.getLanguageVersion() + "V");
        sender.sendMessage("List of available scripts:");
        for (Script activeScript : JSMC.getActiveScripts()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', (activeScript.isEnabled() ? "&a" : "&c") + activeScript.file.getName()));
        }
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
