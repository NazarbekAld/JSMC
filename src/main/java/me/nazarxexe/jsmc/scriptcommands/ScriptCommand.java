package me.nazarxexe.jsmc.scriptcommands;

import me.nazarxexe.jsmc.Script;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.graalvm.polyglot.Value;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ScriptCommand extends BukkitCommand {
    private final Value runnable;
    private final Script root;
    private Value tab = null;

    public ScriptCommand(@NotNull String name, Value runnable, Script root) {
        super(name);
        this.runnable = runnable;
        this.root = root;

        this.setPermission("me.nazarxexe.js.command." + root.getLogger().getName() + "." + name);

    }

    public ScriptCommand(@NotNull String name, Value runnable, Value tab, Script root) {
        super(name);
        this.tab = tab;
        this.runnable = runnable;
        this.root = root;

        this.setPermission("me.nazarxexe.js.command." + root.getLogger().getName() + "." + name);

    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if (!root.isEnabled()){
            sender.sendMessage(ChatColor.RED + "Command is disabled.");
            return false;
        }

        runnable.execute(sender, args);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {

        if (!root.isEnabled()) return new ArrayList<>();
        if (tab == null) return new ArrayList<>();

        List<String> parse = new ArrayList<>();

        final Value val = tab.execute(sender, args);
        final long size = val.getArraySize();

        for (long i=0; i<size; i++) {
            parse.add(val.getArrayElement(i).asString());
        }

        return parse;
    }

}
