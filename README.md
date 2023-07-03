# JSMC
JavaScript for Paper/Bukkit

# Commands
```
/jsmc reloadcache - Reload script list.
/jsmc enable <script> - enable script.
/jsmc disable <script> - disable script.
/jsmc create <project name> - create new script.
/jsmc scripts - list of scripts
```

# How to add the scripts.
Put in folder ../plugins/JSMC/Scripts/ the scripts.

# Example

```javascript
const root = Polyglot.import("root"); // root
const console = Polyglot.import("console");
const commandMap = Polyglot.import("commandMap"); // commandMap
const listener = Polyglot.import("listener"); // Listener
const scheduler = Polyglot.import("scheduler"); // scheduler
const server = Polyglot.import("server") // Server

// Script props:
const config = {

    name: "hello_world", // More for logger/command_prefix
    
    onEnable: start,
    onDisable: stop // Optional

}

function start() {

    console.info("Executed :).")

    commandMap.registerCommand("test" /* Name of command */, root /* Root */, 
    (sender, args) => { sender.sendMessage("Hi"); }, /* Command execution handler */
    () => { return ["hello", "world"] } /* Autocompleter handler */
    );

    listener.registerListener("PlayerChatEvent" /* Event name */, (event) => {
        console.info("OK.")
    } /* Handler */)

    scheduler.runTaskTimer(() => { server.broadcastMessage("Hi") }, 20); // Broadcast "Hi" every 20 ticks (1second).

}
function stop() {
    console.info("Disabled :(.");
}


config // Give props to the backend to be processed.
```

# Extras
Visit engine docs: [GraalVM JavaInteroperability](https://www.graalvm.org/latest/reference-manual/js/JavaInteroperability/)

