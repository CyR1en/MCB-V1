package us.cyrien.MineCordBotV1.configuration;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

public class MCBYamlConfiguration extends YamlConfiguration {

    @Override
    public void set(String path, Object value) {
        Validate.notEmpty(path, "Cannot set to an empty path");
        Configuration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot use section without a root");
        } else {
            char separator = root.options().pathSeparator();
            int i1 = -1;
            Object section = this;

            int i2;
            String key;
            while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
                key = path.substring(i2, i1);
                ConfigurationSection subSection = ((ConfigurationSection) section).getConfigurationSection(key);
                if (subSection == null) {
                    if (value == null) {
                        return;
                    }

                    section = ((ConfigurationSection) section).createSection(key);
                } else {
                    section = subSection;
                }
            }

            key = path.substring(i2);
            if (section == this) {
                if (value != null)
                    this.map.put(key, value);
            } else {
                ((ConfigurationSection) section).set(key, value);
            }

        }
    }

    public static MCBYamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");
        MCBYamlConfiguration config = new MCBYamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException var3) {
            ;
        } catch (IOException var4) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var4);
        } catch (InvalidConfigurationException var5) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var5);
        }

        return config;
    }
}
