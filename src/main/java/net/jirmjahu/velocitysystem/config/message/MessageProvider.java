package net.jirmjahu.velocitysystem.config.message;

import lombok.RequiredArgsConstructor;
import net.jirmjahu.velocitysystem.config.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@RequiredArgsConstructor
public class MessageProvider {

    private final ConfigManager messagesConfig;

    public Component message(String message, boolean prefix) {
        if (this.messagesConfig == null || !this.messagesConfig.config().contains(message)) {
            return Component.text(message);
        }

        var configMessage = this.messagesConfig.config().getString(message);
        if (prefix) {
            configMessage = this.messagesConfig.config().getString("prefix") + configMessage;
        }

        return MiniMessage.miniMessage().deserialize(configMessage);
    }

    public Component message(String message) {
        return this.message(message, true);
    }

    public Component prefix() {
        return message("prefix");
    }

    public Component noPermissionMessage() {
        return message("no-permission");
    }

    public Component noServerMessage() {
        return message("no-server");
    }

    public Component onlyPlayersMessage() {
        return message("only-players");
    }
}
