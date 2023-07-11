package me.nazarxexe.jsmc.command.subcommands.module;

import me.nazarxexe.jsmc.JSMC;
import me.nazarxexe.jsmc.command.JSMCSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Module implements JSMCSubCommand {
    final Plugin plugin;

    public final List<JSMCSubCommand> subCommands = new ArrayList<>();

    public Module(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String name() {
        return "module";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        for (JSMCSubCommand subCommand : subCommands) {
            if (subCommand.name().equals(args[1])) {
                subCommand.execute(sender, args);
            }
        }

    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        if (args.length == 2)
            return subCommands.stream().map((e) -> e.name()).collect(Collectors.toList());

        JSMCSubCommand sub = null;
        for (JSMCSubCommand subCommand : subCommands) {
            if (subCommand.name().equals(args[1])){
                sub = subCommand;
            }
        }
        return sub.tab(sender, args);
    }
}
