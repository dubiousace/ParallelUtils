package parallelmc.parallelutils.modules.parallelitems;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import parallelmc.parallelutils.Constants;
import parallelmc.parallelutils.ParallelModule;
import parallelmc.parallelutils.ParallelUtils;
import parallelmc.parallelutils.commands.ParallelCommand;
import parallelmc.parallelutils.commands.permissions.ParallelPermission;

import java.util.*;
import java.util.logging.Level;

/**
 * A command to give someone a ParallelItems custom item.
 * Usage: /pu give <username> <item>
 */
public class ParallelItemsGiveCommand extends ParallelCommand {

    public static final String[] ITEMS = new String[]{"enhanced_fertilizer", "baguette", "space_helmet_red", "candy", "early_supporter_glasses", "pocket_teleporter", "totem_of_the_void"};

    private final String USAGE = "Usage: /pu give <username> <item> [amount]";

    private ParallelItems parallelItems;

    public ParallelItemsGiveCommand(){
        super("give", "Give a player the specified ParallelItems item",
                new ParallelPermission("parallelutils.give"));

        PluginManager manager = Bukkit.getPluginManager();
        Plugin plugin = manager.getPlugin(Constants.PLUGIN_NAME);

        if (plugin == null) {
            ParallelUtils.log(Level.SEVERE, "Unable to find ParallelItems from give command. Plugin "
                    + Constants.PLUGIN_NAME + " does not exist!");
            return;
        }

        ParallelUtils puPlugin = (ParallelUtils) plugin;

        ParallelModule module = puPlugin.getModule("ParallelItems");
        if(module instanceof ParallelItems){
            parallelItems = (ParallelItems) module;
        }
        else{
            ParallelUtils.log(Level.WARNING, "Unable to find ParallelItems module from give command.");
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(USAGE);
            return true;
        } else if (args.length == 2) {
            String options = "Options:\n";
            for (String s : ITEMS) {
                options += s + "\n";
            }
            sender.sendMessage(options);
            return true;
        } else if(args.length == 3 || args.length == 4) {
            Player player = Bukkit.getServer().getPlayer(args[1]);
            if(player == null) {
                sender.sendMessage("No player found with that username!");
                return false;
            }

            PlayerInventory inv = player.getInventory();

            int amt = 1;
            if(args.length == 4) {
                try {
                    amt = Integer.parseInt(args[3]);
                    if(amt > 255){
                        sender.sendMessage("Limiting amount to 255...");
                        amt = 255;
                    }
                }
                catch (NumberFormatException e){
                    sender.sendMessage("Not a valid amount!");
                    sender.sendMessage(USAGE);
                    return false;
                }
            }

            switch (args[2]) {
                case "enhanced_fertilizer" -> {
                    HashMap<Integer, ItemStack> results = inv.addItem(parallelItems.getItem("enhanced_fertilizer")
                            .asQuantity(amt));
                    if (!results.isEmpty()) {
                        World world = player.getWorld();
                        Location loc = player.getLocation();
                        Collection<ItemStack> vals = results.values();
                        for (ItemStack i : vals) {
                            world.dropItemNaturally(loc, i);
                        }
                    }
                }
                case "baguette" -> {
                    HashMap<Integer, ItemStack> results = inv.addItem(parallelItems.getItem("baguette")
                            .asQuantity(amt));
                    if (!results.isEmpty()) {
                        World world = player.getWorld();
                        Location loc = player.getLocation();
                        Collection<ItemStack> vals = results.values();
                        for (ItemStack i : vals) {
                            world.dropItemNaturally(loc, i);
                        }
                    }
                }
                case "space_helmet_red" -> {
                    HashMap<Integer, ItemStack> results = inv.addItem(parallelItems.getItem("space_helmet_red")
                            .asQuantity(amt));
                    if (!results.isEmpty()) {
                        World world = player.getWorld();
                        Location loc = player.getLocation();
                        Collection<ItemStack> vals = results.values();
                        for (ItemStack i : vals) {
                            world.dropItemNaturally(loc, i);
                        }
                    }
                }
                case "candy" -> {
                    HashMap<Integer, ItemStack> results = inv.addItem(parallelItems.getItem("candy")
                            .asQuantity(amt));
                    if (!results.isEmpty()) {
                        World world = player.getWorld();
                        Location loc = player.getLocation();
                        Collection<ItemStack> vals = results.values();
                        for (ItemStack i : vals) {
                            world.dropItemNaturally(loc, i);
                        }
                    }
                }
                case "early_supporter_glasses" -> {
                    HashMap<Integer, ItemStack> results = inv.addItem(parallelItems.getItem("early_supporter_glasses")
                            .asQuantity(amt));
                    if (!results.isEmpty()) {
                        World world = player.getWorld();
                        Location loc = player.getLocation();
                        Collection<ItemStack> vals = results.values();
                        for (ItemStack i : vals) {
                            world.dropItemNaturally(loc, i);
                        }
                    }
                }
                case "pocket_teleporter" -> {
                    HashMap<Integer, ItemStack> results = inv.addItem(parallelItems.getItem("pocket_teleporter")
                            .asQuantity(amt));
                    if (!results.isEmpty()) {
                        World world = player.getWorld();
                        Location loc = player.getLocation();
                        Collection<ItemStack> vals = results.values();
                        for (ItemStack i : vals) {
                            world.dropItemNaturally(loc, i);
                        }
                    }
                }
                case "totem_of_the_void" -> {
                    HashMap<Integer, ItemStack> results = inv.addItem(parallelItems.getItem("totem_of_the_void")
                            .asQuantity(amt));
                    if (!results.isEmpty()) {
                        World world = player.getWorld();
                        Location loc = player.getLocation();
                        Collection<ItemStack> vals = results.values();
                        for (ItemStack i : vals) {
                            world.dropItemNaturally(loc, i);
                        }
                    }
                }
                default -> {
                    sender.sendMessage("No item found with that name!");
                    String options = "Options:\n";
                    for (String s : ITEMS) {
                        options += s + "\n";
                    }
                    sender.sendMessage(options);
                    return false;
                }
            }
        }
        else{
            sender.sendMessage("Too many parameters!");
            sender.sendMessage(USAGE);
            return false;
        }
        return true;
    }

    @Override
    public List<String> getTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if(args.length == 2){
            list.addAll(sender.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).toList());
        }
        else if(args.length == 3){
            list.addAll(Arrays.stream(ITEMS).toList());
        }
        else if(args.length == 4){
            list.add("[amount]");
        }
        return list;
    }
}
