package rocks.learnercouncil.cameronmc.common;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import static net.md_5.bungee.api.ChatColor.*;

public class CommandResult {

    private static ComponentBuilder prefix() {
        return new ComponentBuilder("[Cameron]").color(AQUA);
    }
    private static BaseComponent[] error(String message) {
        return prefix().append(message).color(RED).create();
    }
    private static BaseComponent[] result(String message) {
        return prefix().append(message).color(GREEN).create();
    }

    public static final BaseComponent[]
            RELOADED = result("Config Reloaded.");

    public static BaseComponent[] locationSet(String world, String x, String y, String z, String yaw, String pitch) {
        return result("Navicator location set. (World: " + world + ", XYZ: " + x + ", " + y + ", " + z + ", Angle: " + yaw + ", " + pitch + ")");
    }
    
    public static final BaseComponent[]
            NO_PERMISSION = error("Sorry, but you don't have permission to execute this command."),
            TOO_MANY_ARGS = error("Too many arguments."),
            NOT_ENOUGH_ARGS = error("Not enough arguments."),
            INVALID_ARGS = error("Invalid arguments."),
            MUST_SPECIFY_LOCATION = error("You must specify a location."),
            SERVER_OFFLINE = error("That server is offline.");

    public static BaseComponent[] needsPlayer(String command) {
        return error("'" + command + "' needs to be executed by a player.");
    }
    public static BaseComponent[] locationNotExist(String location) {
        return error("The location \"" + location + "\" does not exist.");
    }

}
