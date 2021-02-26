package parallelmc.parallelutils.custommobs.events;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import parallelmc.parallelutils.Parallelutils;
import parallelmc.parallelutils.custommobs.nmsmobs.SpawnReason;
import parallelmc.parallelutils.custommobs.registry.EntityRegistry;
import parallelmc.parallelutils.custommobs.registry.SpawnerRegistry;

import java.util.logging.Level;

/**
 * For non-specific entity listeners
 **/
public class CustomMobsGeneralEntityListener implements Listener {

	@EventHandler
	public void onEntityDespawn(final EntityRemoveFromWorldEvent event) {
		CraftEntity entity = (CraftEntity) event.getEntity();
		String UUID = entity.getUniqueId().toString();
		if (EntityRegistry.getInstance().containsEntity(UUID)) {
			Parallelutils.log(Level.ALL, "Removing entity " + UUID + " from world");
			if (EntityRegistry.getInstance().getEntity(UUID).spawnReason == SpawnReason.SPAWNER) {
				Location spawner = EntityRegistry.getInstance().getEntity(UUID).spawnOrigin;
				SpawnerRegistry.getInstance().decrementMobCount(spawner);
				if (SpawnerRegistry.getInstance().getSpawner(spawner).hasLeash()) {
					SpawnerRegistry.getInstance().removeLeashedEntity(spawner, UUID);
				}
			}
			EntityRegistry.getInstance().removeEntity(UUID);
		}
	}
}
