package me.nazarxexe.jsmc.command;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JSMCCommand implements CommandExecutor, TabCompleter {

    @Getter
    List<JSMCSubCommand> subCommands = new ArrayList<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (JSMCSubCommand subCommand : subCommands) {
            if (args[0].equals(subCommand.name())) {
                subCommand.execute(sender, args);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return subCommands.stream().map(JSMCSubCommand::name).collect(Collectors.toList());
        }
        List<String> res = new ArrayList<>();
        for (JSMCSubCommand subCommand : subCommands) {
            if (args[0].equals(subCommand.name())) {
                res = subCommand.tab(sender, args);
            }
        }
        return res;
    }
}
