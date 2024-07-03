package lol.pyr.znpcsplus.api;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import org.bukkit.Bukkit;

/**
 * Provider for the registered entity property registry instance
 */
public class NpcPropertyRegistryProvider {
    private static EntityPropertyRegistry registry = null;

    private NpcPropertyRegistryProvider() {
        throw new UnsupportedOperationException();
    }

    /**
     * Static method that returns the entity property registry instance of the plugin
     *
     * @return The instance of the entity property registry for the ZNPCsPlus plugin
     */
    public static EntityPropertyRegistry get() {
        if (registry == null) throw new IllegalStateException(
                "ZNPCsPlus plugin isn't enabled yet!\n" +
                        "Please add it to your plugin.yml as a depend or softdepend."
        );
        return registry;
    }

    /**
     * Internal method used to register the main instance of the plugin as the entity property registry provider
     * You probably shouldn't call this method under any circumstances
     *
     * @param api Instance of the ZNPCsPlus entity property registry
     */
    public static void register(EntityPropertyRegistry api) {
        NpcPropertyRegistryProvider.registry = api;
    }

    /**
     * Internal method used to unregister the plugin from the provider when the plugin shuts down
     * You probably shouldn't call this method under any circumstances
     */
    public static void unregister() {
        Bukkit.getServicesManager().unregister(registry);
    }
}
