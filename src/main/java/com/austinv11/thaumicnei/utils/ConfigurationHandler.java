package com.austinv11.thaumicnei.utils;

import com.austinv11.thaumicnei.reference.Config;
import com.austinv11.thaumicnei.reference.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler {
	public static Configuration config;

	public static void init(File configFile){
		if (config == null) {
			config = new Configuration(configFile);
		}
		loadConfiguration();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
		if (event.modID.equalsIgnoreCase(Reference.MOD_ID)){
			loadConfiguration();
		}
	}

	private static void loadConfiguration(){
		try{
			config.load();
			boolean hideNonResearched = config.get(Configuration.CATEGORY_GENERAL, "hideNonResearchedItems", true, "If set to true, all non-researched items will be hidden from NEI").getBoolean(true);
			boolean cheatMode = config.get(Configuration.CATEGORY_GENERAL, "cheatMode", false, "If set to true, you could search by aspect and see recipes of all items without needing to have scanned/researched them").getBoolean(false);
			reSyncConfig(hideNonResearched, cheatMode);
		}catch (Exception e){
			Logger.warn("Config exception!");
			Logger.warn(e.getStackTrace());
		}finally {
			if (config.hasChanged()) {
				config.save();
			}
		}
	}

	private static void reSyncConfig(boolean v1, boolean v2){
		Config.hideNonResearched = v1;
		Config.cheatMode = v2;
	}
}
