package me.nazarxexe.jsmc.command.subcommands.module;

import me.nazarxexe.jsmc.JSMC;
import me.nazarxexe.jsmc.command.JSMCSubCommand;
import me.nazarxexe.jsmc.module.ModuleManager;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleImport implements JSMCSubCommand {

    final Plugin plugin;

    public ModuleImport(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String name() {
        return "import";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            sender.sendMessage("Downloading...");
            if (args.length == 3) {
                ModuleManager.getInstance().importAllTo(JSMC.getLibs(), ModuleManager.getInstance().getKnownModuleByName(args[2]), null);
            }else {
                ModuleManager.getInstance().importAllTo(JSMC.getLibs(), ModuleManager.getInstance().getKnownModuleByName(args[2]), args[3]);
            }
            sender.sendMessage("Processed done. Check console for any errors.");

        });

    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return ModuleManager.getInstance().cachedModules.stream().map((e) -> e.getName()).collect(Collectors.toList());
    }

    private Path download(String sourceURL, String targetDirectory) throws IOException {
        URL url = new URL(sourceURL);
        String fileName = sourceURL.substring(sourceURL.lastIndexOf('/') + 1, sourceURL.length());
        Path targetPath = new File(targetDirectory + File.separator + fileName).toPath();
        Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetPath;
    }
}
