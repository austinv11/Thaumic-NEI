package com.austinv11.thaumicnei;

import codechicken.nei.NEIClientConfig;
import codechicken.nei.SearchField;
import codechicken.nei.api.ItemFilter;
import com.austinv11.thaumicnei.utils.Logger;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class AspectFilter implements SearchField.ISearchProvider {

	@Override
	public ItemFilter getFilter(String searchText) {
		return new Filter(searchText);
	}

	public static class Filter implements ItemFilter {
		String pattern;
		boolean aspectSearch = false;

		public Filter(String searchText) {
			//Logger.info("Searching! :D");
			if (searchText.startsWith("@aspect:") && searchText.length() > 8){
				pattern = searchText.substring(8);
				aspectSearch = true;
			}else{
				pattern = searchText;
				aspectSearch = true;
			}
		}

		@Override
		public boolean matches(ItemStack itemStack) {
			if (pattern == null || pattern == ""){
				return true;
			}else{
				if (aspectSearch){
					AspectList list = new AspectList(itemStack);
					if (list != null){
						for (Aspect aspect : list.getAspects()) {
							if (aspect != null){
								if (pattern.equalsIgnoreCase(aspect.getName())) {
									return true;
								}
							}
						}
					}
				}else{
					return true;
				}
			}
			return false;
		}
	}
}
