package com.austinv11.thaumicnei;

import com.austinv11.thaumicnei.proxy.IProxy;
import com.austinv11.thaumicnei.reference.Reference;
import com.austinv11.thaumicnei.utils.ConfigurationHandler;
import com.austinv11.thaumicnei.utils.Logger;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

//FIXME mcmod.info
@Mod(modid= Reference.MOD_ID,name = Reference.MOD_NAME,version = Reference.VERSION, dependencies = "after:NotEnoughItems;after:Thaumcraft")
public class ThaumicNEI {

	@Mod.Instance(Reference.MOD_ID)
	public static ThaumicNEI instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		//Logger.info(Reference.VERSION);
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
