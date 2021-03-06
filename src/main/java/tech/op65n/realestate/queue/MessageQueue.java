package tech.op65n.realestate.queue;

import com.github.frcsty.frozenactions.wrapper.ActionHandler;
import tech.op65n.realestate.RealEstatePlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class MessageQueue {

    private static final Map<UUID, List<String>> MESSAGE_QUEUE = new HashMap<>();

    /**
     * Adds a message to the queue when a user sells a claim and they're offline
     *
     * @param receiver {@link UUID} of the message receiver (seller)
     * @param message {@link List} of the message to be sent
     */
    public static void addToQueue(final UUID receiver, final List<String> message) {
        final List<String> queueMessage = MESSAGE_QUEUE.getOrDefault(receiver, new ArrayList<>());

        queueMessage.addAll(message);
        MESSAGE_QUEUE.put(receiver, queueMessage);
    }

    /**
     * Executes queued messages for a {@link Player}
     *
     * @param handler {@link ActionHandler} Loaded ActionHandler
     * @param player {@link Player} message receiver (seller)
     */
    public static void executeForReceiver(final ActionHandler handler, final Player player) {
        final UUID uuid = player.getUniqueId();

        handler.execute(player,
                MESSAGE_QUEUE.getOrDefault(uuid, new ArrayList<>())
        );

        MESSAGE_QUEUE.remove(uuid);
    }

    /**
     * Loads queued messages from file into our {@link Map}
     *
     * @param plugin Our plugin instance
     */
    public static void loadFromFile(final RealEstatePlugin plugin) {
        final File file = new File(plugin.getDataFolder(), "message-queue.yml");
        if (!file.exists()) return;

        final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        final ConfigurationSection section = configuration.getConfigurationSection("queue");
        if (section == null) return;

        for (final String key : section.getKeys(false)) {
            final UUID uuid = UUID.fromString(key);

            MESSAGE_QUEUE.put(uuid, section.getStringList(key));
        }

        file.delete();
    }

    /**
     * Saves queued messages to a file
     *
     * @param plugin Our plugin instance
     * @throws IOException if a new file could not be created
     */
    public static void saveToFile(final RealEstatePlugin plugin) throws IOException {
        final File file = new File(plugin.getDataFolder(), "message-queue.yml");
        if (!file.exists())
            file.createNewFile();

        final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        for (final UUID uuid : MESSAGE_QUEUE.keySet()) {
            configuration.set("queue." + uuid.toString(), MESSAGE_QUEUE.get(uuid));
        }

        configuration.save(file);
    }

}
