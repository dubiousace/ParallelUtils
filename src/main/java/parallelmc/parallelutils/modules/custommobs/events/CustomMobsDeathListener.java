package parallelmc.parallelutils.modules.custommobs.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import parallelmc.parallelutils.modules.custommobs.bukkitmobs.CraftFireWisp;
import parallelmc.parallelutils.modules.custommobs.bukkitmobs.CraftWisp;
import parallelmc.parallelutils.modules.custommobs.nmsmobs.EntityData;
import parallelmc.parallelutils.modules.custommobs.registry.EntityRegistry;

/**
 * For listening for deaths of players and entities
 */
public class CustomMobsDeathListener implements Listener {

	// Death Messages
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		EntityDamageEvent lastDamage = player.getLastDamageCause();

		if (lastDamage instanceof EntityDamageByEntityEvent) {
			org.bukkit.entity.Entity killer = ((EntityDamageByEntityEvent) lastDamage).getDamager();

			if (EntityRegistry.getInstance().containsEntity(killer.getUniqueId().toString())) {
				EntityData pair = EntityRegistry.getInstance().getEntity(killer.getUniqueId().toString());

				switch (pair.type) {
					case "wisp" -> event.setDeathMessage(player.getDisplayName() + " was slain by Wisp");
					case "fire_wisp" -> event.setDeathMessage(player.getDisplayName() + " was slain by Fire Wisp");
				}
			}
		}
	}

	// Handle custom entity deaths
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		EntityData pair = EntityRegistry.getInstance().getEntity(event.getEntity().getUniqueId().toString());

		if (pair != null) {
			switch (pair.type) {
				case "wisp" -> CraftWisp.deathLoot(event);
				case "fire_wisp" -> CraftFireWisp.deathLoot(event);
			}
		}
	}
}