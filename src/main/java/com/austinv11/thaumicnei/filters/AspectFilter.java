package com.austinv11.thaumicnei.filters;

import codechicken.nei.NEIClientConfig;
import codechicken.nei.SearchField;
import codechicken.nei.api.ItemFilter;
import com.austinv11.thaumicnei.reference.Config;
import com.austinv11.thaumicnei.reference.Reference;
import com.austinv11.thaumicnei.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.research.ScanManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class AspectFilter implements SearchField.ISearchProvider {

	@Override
	public ItemFilter getFilter(String searchText) {
		return new Filter(searchText);
	}

	public static class Filter implements ItemFilter {
		String pattern;
		int aspectSearch = 0;

		public Filter(String searchText) {
			//Logger.info("Searching! :D");
			if (searchText.startsWith("@"+StatCollector.translateToLocal(Reference.MOD_ID+":filter.aspect.0")+":") && searchText.length() > 8) {
				pattern = searchText.substring(8);
				aspectSearch = 1;
			}else if (searchText.startsWith("@"+StatCollector.translateToLocal(Reference.MOD_ID+":filter.aspect.1")+":") && searchText.length() > 9){
				pattern = searchText.substring(9);
				aspectSearch = 2;
			}else{
				pattern = searchText;
				aspectSearch = 0;
			}
		}

		private boolean isScanned(ItemStack item) {
			try{
				return ScanManager.hasBeenScanned(Minecraft.getMinecraft().thePlayer,new ScanResult((byte)1, Item.getIdFromItem(item.getItem()),item.getItem().getMetadata(0),null, ""));
			}catch (Exception e) {

			}
			return false;
		}

		@Override
		public boolean matches(ItemStack itemStack) {
			if (pattern == null || pattern == ""){
				return true;
			}else{
				if (aspectSearch == 1) {//@aspect:
					AspectList list = new AspectList(itemStack);
					if (list != null) {
						for (Aspect aspect : list.getAspects()) {
							if (aspect != null) {
								if (pattern.equalsIgnoreCase(aspect.getName())) {
									if (Config.cheatMode || isScanned(itemStack)) {
										return true;
									}
								}
							}
						}
					}
				}else if (aspectSearch == 2) {//@aspectS:
					AspectList list = new AspectList(itemStack);
					if (list != null) {
						pattern = pattern.replace(" ","");
						if (pattern.contains(",")){
							String[] patterns = pattern.split(",");
							if (patterns.length > 1) {
								List<Boolean> match = new ArrayList<Boolean>();
								for (Aspect aspect : list.getAspects()) {
									for (String p : patterns) {
										if (aspect != null){
											if (p.equalsIgnoreCase(aspect.getName())) {
												if (Config.cheatMode || isScanned(itemStack)) {
													match.add(true);
												}
											}
										}
									}
								}
								if (match.size() == patterns.length) {
									if (Config.cheatMode || isScanned(itemStack)) {
										return true;
									}
								}else {
									return false;
								}
							}
						}else{
							aspectSearch = 1;
							pattern = pattern.replace(",","");
							return this.matches(itemStack);
						}
					}else{
						aspectSearch = 1;
						return this.matches(itemStack);
						}
				}else{
					return true;
				}
			}
			return false;
		}
	}
}
