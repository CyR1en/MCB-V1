package us.cyrien.MineCordBotV1.configuration;

import org.apache.commons.collections4.map.HashedMap;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class LocalizationFile {

    private HashedMap<String, File> languages;
    private MineCordBot mcb;

    public LocalizationFile(MineCordBot p, boolean copy) {
        mcb = p;
        languages = new HashedMap<>();
        languages.put("en", new File(p.getDataFolder().toString() + "/lang/en.yml"));
        languages.put("es", new File(p.getDataFolder().toString() + "/lang/es.yml"));
        languages.put("pl", new File(p.getDataFolder().toString() + "/lang/pl.yml"));
        for (String key : languages.keySet()) {
            if (copy) {
                saveResource(p, key + ".yml", true);
            } else {
                try {
                    languages.get(key).createNewFile();
                } catch (IOException ex) {
                }
            }
        }
    }

    public void saveResource(MineCordBot mcb, String resourcePath, boolean replace) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = mcb.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource \'" + resourcePath + "\' cannot be found");
            } else {
                File outFile = new File(mcb.getDataFolder() + "/lang", resourcePath);
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(mcb.getDataFolder() + "/lang", resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }
                try {
                    if (outFile.exists() && !replace) {
                        mcb.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        FileOutputStream ex = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
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

    public YamlConfiguration getLocalization(String lang) {
        File lang1 = new File(mcb.getDataFolder() + "/lang/" + lang + ".yml");
        return YamlConfiguration.loadConfiguration(lang1);
    }

    public HashedMap<String, File> getLanguages() {
        return languages;
    }

}

