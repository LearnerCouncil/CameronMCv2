package rocks.learnercouncil.cameronmc.common;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;

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
            NONE = {},
            RELOADED = result("Config reloaded."),
            PORTAL_CREATED = result("Portal created."),
            PORTAL_REMOVED = result("Portal removed.");

    public static BaseComponent[] locationSet(String world, String x, String y, String z, String yaw, String pitch) {
        return result("Navicator location set. (World: " + world + ", XYZ: " + x + ", " + y + ", " + z + ", Angle: " + yaw + ", " + pitch + ")");
    }
    public static BaseComponent[] speedSet(String speed) {
        return prefix().append("Set your flying speed to '").color(GREEN).append(speed).color(YELLOW).append("'.").color(GREEN).create();
    }
    public static BaseComponent[] selectorGiven() {
        KeybindComponent placeKeybind = new KeybindComponent("key.use");
        placeKeybind.setColor(YELLOW);
        KeybindComponent destroyKeybind = new KeybindComponent("key.attack");
        destroyKeybind.setColor(YELLOW);
        return prefix().append("You have now been given a portal selector.\n Press ").color(GREEN)
                .append("[").color(YELLOW).append(destroyKeybind).append("]").color(YELLOW)
                .append(" to select the first position and ")
                .append("[").color(YELLOW).append(placeKeybind).append("]").color(YELLOW)
                .append(" to select the second.").create();
    }

    public static final BaseComponent[]
            NO_PERMISSION = error("Sorry, but you don't have permission to execute this command."),
            TOO_MANY_ARGS = error("Too many arguments."),
            NOT_ENOUGH_ARGS = error("Not enough arguments."),
            INVALID_ARGS = error("Invalid arguments."),
            MUST_SPECIFY_LOCATION = error("You must specify a location."),
            SERVER_OFFLINE = error("That server is offline."),
            MUST_SPECIFY_SPEED = error("You must specify a flight speed."),
            MUST_SPECIFY_PORTAL_NAME = error("You must sepcify a name for this portal."),
            MUST_SPECIFY_PORTAL_DESTINATION = error("You must specify a destination for this portal."),
            MUST_SPECIFY_PORTAL = error("YOu must specify the portal you want to remove."),
            MUST_SELECT_AREA = error("You must select an area for the portal."),
            NO_PORTAL = error("That portal doesn't exist.");

    public static BaseComponent[] needsPlayer(String command) {
        return error("'" + command + "' needs to be executed by a player.");
    }
    public static BaseComponent[] locationNotExist(String location) {
        return error("The location \"" + location + "\" does not exist.");
    }
    public static BaseComponent[] invalidNumber(String number) {
        return prefix().append("'").color(RED).append(number).color(YELLOW).append("' is not a valid number.").color(RED).create();
    }

}
