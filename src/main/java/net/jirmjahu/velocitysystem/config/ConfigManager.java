package net.jirmjahu.velocitysystem.config;

import com.moandjiezana.toml.Toml;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import net.jirmjahu.velocitysystem.VelocitySystem;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Getter
@Accessors(fluent = true)
public class ConfigManager {

    private final Toml config;

    @SneakyThrows
    public ConfigManager(VelocitySystem plugin, String fileName) {
        var pluginsFolderPath = new File("plugins", "VelocitySystem");
        if (!pluginsFolderPath.exists()) {
            pluginsFolderPath.mkdirs();
        }
        var configFile = new File(pluginsFolderPath, fileName);
        if (!configFile.exists()) {
            Files.copy(plugin.getClass().getResourceAsStream("/" + fileName), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        this.config = new Toml().read(configFile);
    }
}