# JSMC
JavaScript for Paper/Bukkit

# Commands
```
/jsmc reloadcache - Reload script list.
/jsmc enable <script> - enable script.
/jsmc disable <script> - disable script.
/jsmc create <project name> - create new script.
/jsmc module <import> <module_name>
/jsmc scripts - list of scripts
```

# How to add the scripts.
Put in folder ../plugins/JSMC/Scripts/ the scripts.

# Example

```javascript
/**
    Note that .. refer to plugin datafolder path.
*/
import * as Std from "../libs/std/std.mjs";

// Where Loader got from?
// Ans: Loader registered manualy in script by plugin.

console.info("Hi");

Std.Scheduler.task(() => {

    // Sync task that executes after start of the script.

    Std.Listener.on(Loader, "PlayerJumpEvent", (event) => 
    { event.getPlayer().sendMessage("You jumped.") })
    
    Std.Listener.on(Loader, "ScriptDisableEvent", (event) => {console.info("BYE")});

    class OwnCommand extends Std.Command {

        constructor() {
            super("owncommand")
        }

        execute(sender, args) {
            sender.sendMessage("Yo")

            return true;
        }
        tab(sender, args) {
            return [ "Nope", "No" ]
        }

    }

    let owncommand = new OwnCommand();
    owncommand.register();
});

```

# Extras
Visit engine docs: [Javet](https://www.caoccao.com/Javet/index.html)

