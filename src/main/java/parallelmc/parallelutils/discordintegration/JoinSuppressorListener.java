package parallelmc.parallelutils.discordintegration;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

/**
 * A Listener to listen for player joins and suppress them if they are in the hidden users array
 */
public class JoinSuppressorListener implements Listener {

	public static ArrayList<String> hiddenUsers = new ArrayList<>();
	public static final Object hiddenUsersLock = new Object();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		synchronized (hiddenUsersLock) { // NOTE: This MIGHT cause lag problems. It shouldn't, but beware
			if (hiddenUsers.contains(player.getName().strip())) {
				event.setJoinMessage(""); // This might need to change, but it needs to be tested
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		synchronized (hiddenUsersLock) { // NOTE: This MIGHT cause lag problems. It shouldn't, but beware
			if (hiddenUsers.contains(player.getName().strip())) {
				event.setQuitMessage(""); // This might need to change, but it needs to be tested
			}
		}
	}
}
