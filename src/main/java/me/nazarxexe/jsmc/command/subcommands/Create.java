package me.nazarxexe.jsmc.command.subcommands;

import me.nazarxexe.jsmc.JSMC;
import me.nazarxexe.jsmc.Script;
import me.nazarxexe.jsmc.command.JSMCSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Create implements JSMCSubCommand {

    final Plugin plugin;

    public Create(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String name() {
        return "create";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            File file = new File(JSMC.getScripts(), args[1] + ".js");


            try {
                if (!file.createNewFile()) {
                    sender.sendMessage("Project already exist.");
                    return;
                }
                FileWriter writer = new FileWriter(file);

                writer.write("\n" +
                        "// const root = Polyglot.import(\"root\"); // root\n" +
                        "const console = Polyglot.import(\"console\");\n" +
                        "// const commandMap = Polyglot.import(\"commandMap\"); // commandMap\n" +
                        "// const listener = Polyglot.import(\"listener\"); // Listener\n" +
                        "// const scheduler = Polyglot.import(\"scheduler\"); // scheduler\n" +
                        "// const server = Polyglot.import(\"server\") // Server\n" +
                        "\n" +
                        "// Script props:\n" +
                        "const config = {\n" +
                        "\n" +
                        "    name: \"" + args[1] + "\", // More for logger/command_prefix\n" +
                        "    \n" +
                        "    onEnable: start,\n" +
                        "    onDisable: stop // Optional\n" +
                        "\n" +
                        "}\n" +
                        "\n" +
                        "function start() {\n" +
                        "\n" +
                        "    console.info(\"Executed :).\")\n" +
                        "\n" +
                        "    // commandMap.registerCommand(\"test\" /* Name of command */, root /* Root */, \n" +
                        "    // (sender, args) => { sender.sendMessage(\"Hi\"); }, /* Command execution handler */\n" +
                        "    // () => { return [\"hello\", \"world\"] } /* Autocompleter handler */\n" +
                        "    // );\n" +
                        "\n" +
                        "    // listener.registerListener(\"PlayerChatEvent\" /* Event name */, (event) => {\n" +
                        "    //     console.info(\"OK.\")\n" +
                        "    // } /* Handler */)\n" +
                        "\n" +
                        "    // scheduler.runTaskTimer(() => { server.broadcastMessage(\"Hi\") }, 20); // Broadcast \"Hi\" every 20 ticks (1second).\n" +
                        "\n" +
                        "}\n" +
                        "function stop() {\n" +
                        "    console.info(\"Disabled :(.\");\n" +
                        "}\n" +
                        "\n" +
                        "\n" +
                        "config // Give props to the backend to be processed.");
                        writer.flush();
                        writer.close();

                        JSMC.activeScripts.add(new Script(file, plugin));
                        sender.sendMessage("Project created. Enable it via command.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return new ArrayList<>() {{ add("<{project name}>"); }};
    }
}
