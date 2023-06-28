package me.nazarxexe.jsmc.tasks;

import lombok.Getter;
import lombok.Setter;
import me.nazarxexe.jsmc.Script;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import javax.script.Invocable;
import javax.script.ScriptException;

public class JSTask {
    final Script script;

    public JSTask(Script script) {
        this.script = script;
    }

    public BukkitTask runTask(String function) {
        return Bukkit.getScheduler().runTask(script.plugin, () -> {
            Invocable invocable = (Invocable) script.engine;
            try {
                invocable.invokeFunction(function);
            } catch (ScriptException | NoSuchMethodException e) {
                script.logger.error("Failed to run task!", e);
            }
        });
    }

    public BukkitTask runAsyncTask(String function) {
        return Bukkit.getScheduler().runTaskAsynchronously(script.plugin, () -> {
            Invocable invocable = (Invocable) script.engine;
            try {
                invocable.invokeFunction(function);
            } catch (ScriptException | NoSuchMethodException e) {
                script.logger.error("Failed to run task!", e);
            }
        });
    }

    public BukkitTask runTaskTimer(String function, long ticks) {
        return Bukkit.getScheduler().runTaskTimer(script.plugin, () -> {
            Invocable invocable = (Invocable) script.engine;
            try {
                invocable.invokeFunction(function);
            } catch (ScriptException | NoSuchMethodException e) {
                script.logger.error("Failed to run task!", e);
            }
        }, 0L, ticks);
    }

    public BukkitTask runTaskTimerAsync(String function, long ticks) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(script.plugin, () -> {
            Invocable invocable = (Invocable) script.engine;
            try {
                invocable.invokeFunction(function);
            } catch (ScriptException | NoSuchMethodException e) {
                script.logger.error("Failed to run task!", e);
            }
        }, 0L, ticks);
    }




}
