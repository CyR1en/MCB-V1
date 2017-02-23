package us.cyrien.MineCordBotV1.configuration;

import us.cyrien.MineCordBotV1.main.MineCordBot;
import org.apache.commons.collections4.map.HashedMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class PluginFile {

    private MCBYamlConfiguration file;
    private File f;
    private HashedMap<String, File> languages;
    private MineCordBot mcb;

    public PluginFile(MineCordBot p, String name, boolean copy) {
        mcb = p;
        f = new File(p.getDataFolder().toString() + "/" + name + ".yml");
        languages = new HashedMap<>();
        languages.put("en", new File(p.getDataFolder().toString() + "/lang/en.yml"));
        languages.put("es", new File(p.getDataFolder().toString() + "/lang/es.yml"));
        for (String key : languages.keySet()) {
            if (!languages.get(key).exists())
                if (copy) {
                    saveResource(p, key + ".yml", true);
                } else {
                    try {
                        languages.get(key).createNewFile();
                    } catch (IOException ex) {
                    }
                }
        }
        if (!f.exists()) {
            if (copy) {
                p.saveResource(name + ".yml", true);
            } else {
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                }
            }
        }
        file = MCBYamlConfiguration.loadConfiguration(this.f);
    }

    public void renew(MineCordBot p, String name, boolean copy) {
        f = new File(p.getDataFolder().toString() + "/" + name + ".yml");
        for (String key : languages.keySet()) {
            if (!languages.get(key).exists())
                if (copy) {
                    languages.get(key).delete();
                    p.saveResource(key + ".yml", true);
                } else {
                    try {
                        languages.get(key).createNewFile();
                    } catch (IOException ex) {
                    }
                }
        }
        if (f.exists()) {
            if (copy) {
                f.delete();
                p.saveResource(name + ".yml", true);
            } else {
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                }
            }
        }
        file = MCBYamlConfiguration.loadConfiguration(this.f);
    }

    public void saveResource(MineCordBot mcb,String resourcePath, boolean replace)
    {
        if(resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = mcb.getResource(resourcePath);
            if(in == null) {
                throw new IllegalArgumentException("The embedded resource \'" + resourcePath + "\' cannot be found in " + this.file);
            } else {
                File outFile = new File(mcb.getDataFolder() + "/lang", resourcePath);
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(mcb.getDataFolder() + "/lang", resourcePath.substring(0, lastIndex >= 0?lastIndex:0));
                if(!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if(outFile.exists() && !replace) {
                        mcb.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        FileOutputStream ex = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];

                        int len;
                        while((len = in.read(buf)) > 0) {
                            ex.write(buf, 0, len);
                        }

                        ex.close();
                        in.close();
                    }
                } catch (IOException var10) {
                    mcb.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

    public File saveResource(File outputDirectory, String name, boolean replace) throws IOException {
        File out = new File(outputDirectory, name);
        if (!replace && out.exists())
            return out;
        InputStream resource = this.getClass().getResourceAsStream(name);
        if (resource == null)
            throw new FileNotFoundException(name + " (resource not found)");
        try (InputStream in = resource;
             OutputStream writer = new BufferedOutputStream(
                     new FileOutputStream(out))) {
            byte[] buffer = new byte[1024 * 4];
            int length;
            while ((length = in.read(buffer)) >= 0) {
                writer.write(buffer, 0, length);
            }
        }
        return out;
    }

    public ConfigurationSection get(final String path) {
        return file.getConfigurationSection(path);
    }

    public MCBYamlConfiguration getConfig() {
        return file;
    }

    public MCBYamlConfiguration getLang(String lang) {
        File lang1 = new File(mcb.getDataFolder() + "/lang/" + lang + ".yml");
        return MCBYamlConfiguration.loadConfiguration(lang1);
    }

    public HashedMap<String, File> getLanguages() {
        return languages;
    }

    public void save() throws IOException {
        file.save(f);
        file = MCBYamlConfiguration.loadConfiguration(f);
    }

    public void reload() {
        file = MCBYamlConfiguration.loadConfiguration(f);
    }
}

