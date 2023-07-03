package me.nazarxexe.jsmc.scriptcommands;

import me.nazarxexe.jsmc.Script;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.graalvm.polyglot.Value;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScriptCommandMap {

    private final org.bukkit.command.CommandMap map = Bukkit.getCommandMap();

    private static ScriptCommandMap instance;

    public static ScriptCommandMap getInstance() {
        if (instance == null){
            instance = new ScriptCommandMap();
        }
        return instance;
    }

    private ScriptCommandMap() {}

    public ScriptCommand registerCommand(Value commandName, Script root, Value runnable ) {
        ScriptCommand command = new ScriptCommand(commandName.asString(), runnable, root);
        registerViaBukkitMap(command, root);
        return command;
    }
    public ScriptCommand registerCommand(Value commandName, Script root, Value runnable, Value tab ) {
        ScriptCommand command = new ScriptCommand(commandName.asString(), runnable, tab, root);
        registerViaBukkitMap(command, root);

        return command;
    }


    private void registerViaBukkitMap(ScriptCommand cmd, Script root) {
        boolean success = root.plugin.getServer().getCommandMap().register(root.getLogger().getName(), cmd);
        syncAll();
    }

    private void syncAll() {
        try{
            final Server server = Bukkit.getServer();
            final Method syncCommandsMethod = Bukkit.getServer().getClass().getDeclaredMethod("syncCommands");
            syncCommandsMethod.setAccessible(true);
            syncCommandsMethod.invoke(Bukkit.getServer());

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void unRegisterAll(Script script) {
        Bukkit.getCommandMap().getKnownCommands().remove(script.getLogger().getName());
        syncAll(); // Bro it wont work :skull:
    }

}
