package com.austinv11.thaumicnei;

import codechicken.nei.SearchField;
import codechicken.nei.api.ItemFilter;
import com.austinv11.thaumicnei.utils.Logger;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

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
						String[] patterns = pattern.split(",");
						for (Aspect aspect : list.getAspects()) {
							if (aspect != null){
								boolean result = false;
								for (String p : patterns){
									if (p.contains(" ")){
										String[] p2 = p.split(" ");
										boolean isInt;
										try{

											isN
										}catch (Exception e){
											isInt = false;
										}
										if ()
										}else {
										if (p.equalsIgnoreCase(aspect.getName())) {
											result = true;
										}
									}
								}
								return result;
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
