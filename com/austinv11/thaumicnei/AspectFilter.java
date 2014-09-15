package com.austinv11.thaumicnei;

import codechicken.nei.NEIClientConfig;
import codechicken.nei.SearchField;
import codechicken.nei.api.ItemFilter;
import com.austinv11.thaumicnei.utils.Logger;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

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
			if (searchText.startsWith("@aspect:") && searchText.length() > 8) {
				pattern = searchText.substring(8);
				aspectSearch = 1;
			}else if (searchText.startsWith("@aspects:") && searchText.length() > 9){
				pattern = searchText.substring(9);
				aspectSearch = 2;
			}else{
				pattern = searchText;
				aspectSearch = 0;
			}
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
									return true;
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
												match.add(true);
											}
										}
									}
								}
								if (match.size() == patterns.length) {
									return true;
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
