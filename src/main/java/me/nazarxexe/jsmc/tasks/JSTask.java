package me.nazarxexe.jsmc.tasks;

import me.nazarxexe.jsmc.Script;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class JSTask {
    final Script script;

    public JSTask(Script script) {
        this.script = script;
    }

    public BukkitTask runTask(Runnable function) {
        return Bukkit.getScheduler().runTask(script.plugin, function);
    }

    public BukkitTask runAsyncTask(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(script.plugin, runnable);
    }

    public BukkitTask runTaskTimer(Runnable runnable, long ticks) {
        return Bukkit.getScheduler().runTaskTimer(script.plugin, runnable, 0L, ticks);
    }

    public BukkitTask runTaskTimerAsync(Runnable runnable, long ticks) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(script.plugin, runnable, 0L, ticks);
    }




}
