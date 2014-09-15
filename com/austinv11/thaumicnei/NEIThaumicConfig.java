package com.austinv11.thaumicnei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.austinv11.thaumicnei.reference.Config;
import com.austinv11.thaumicnei.reference.Reference;

public class NEIThaumicConfig implements IConfigureNEI {

	 @Override
	public void loadConfig(){
		 //API.registerRecipeHandler();
		 if (Config.cheatMode) {
			 API.addSearchProvider(new AspectFilter());
		 }
	 }

	@Override
	public String getName() {
		return Reference.MOD_NAME;
	}

	@Override
	public String getVersion() {
		return Reference.VERSION;
	}
}
