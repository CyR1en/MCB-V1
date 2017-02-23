package us.cyrien.MineCordBotV1.main;

import us.cyrien.MineCordBotV1.configuration.PluginFile;
import org.bukkit.configuration.file.YamlConfiguration;

public class Language {

	private String language;
    private MineCordBot mcb;
    private PluginFile pf;
    private YamlConfiguration lang;

	public Language(MineCordBot mcb, String language, PluginFile pf) {
	    this.mcb = mcb;
	    this.pf = pf;
        if(this.pf.getLanguages().containsKey(language))
            this.language = language;
        else {
            mcb.getLogger().warning("language configuration invalid. Loading default language.");
            this.language = "en";
        }
        lang = pf.getLang(language);
    }

    public String getLanguage() {
	    return language;
    }

    public String getTranslatedMessage(String messagePath) {
	    return lang.getString(messagePath);
    }
}