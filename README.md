# JSMC
JavaScript for Paper/Bukkit

# Commands
```
/jsmc reloadcache - Reload script list.
/jsmc enable <script> - enable script.
/jsmc disable <script> - disable script.
/jsmc scripts - list of scripts
```

# How to add the scripts.
Put in folder ../plugins/JSMC/Scripts/ the scripts.

# Example
Empty:
```javascript
// Async call
function start() { // sync call
    
}
function stop() { // sync call
}
```


Simple lifesteal script:
```javascript
function start() {
    console.info("Enabled.")
    listener.registerListener("PlayerDeathEvent" /* Event name */, "stealHeart" /* Function name */, root /* Script ref */);
}
function stop() {
}

function stealHeart(event) {

    if (!(event.getPlayer() instanceof Java.type("org.bukkit.entity.Player"))) return;
    
    let lost = event.getPlayer();

    if (lost.getLastDamageCause() == null) return;
    if (lost.getLastDamageCause().getEntity() == null) return;

    let gain = lost.getLastDamageCause().getEntity();
    
    lost.sendMessage("You lost 1heart.");
    
    let gain_maxhl = gain.getAttribute(Java.type("org.bukkit.attribute.Attribute").GENERIC_MAX_HEALTH).getValue();
    let lost_maxhl = lost.getAttribute(Java.type("org.bukkit.attribute.Attribute").GENERIC_MAX_HEALTH).getValue();
    gain.setMaxHealth(gain_maxhl + 1);
    lost.setMaxHealth(lost_maxhl - 1);

}
```
Simple broadcast script
```javascript
// Async call
function start() { // sync call
    scheduler.runTaskTimer("hello_world" /* Function name */, 20 /* Ticks */)
}
function stop() { // sync call
}
function hello_world() {
    server.broadcastMessage("Hello World")
}
```

# Global variables
```javascript
const server = ... // Server object
const root = ... // Script object
const scheduler = ... // scheduler for running tasks
const listener = ... // Listener object for listening events.
```
# Extras
Visit engine docs: [GraalVM JavaInteroperability](https://www.graalvm.org/latest/reference-manual/js/JavaInteroperability/)

