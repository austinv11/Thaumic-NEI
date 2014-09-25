package com.austinv11.thaumicnei.handlers;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.austinv11.thaumicnei.reference.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CrucibleHandler extends TemplateRecipeHandler {

	@Override
	public String getGuiTexture() {
		return "thaumcraft:textures/misc/r_crucible.png";
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal(Reference.MOD_ID+":gui.nei.crucible");
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public void drawBackground(int recipe) {
		super.drawBackground(recipe);
		//GL11.glColor4f(1, 1, 1, 1);
		//GuiDraw.changeTexture(getGuiTexture());
		//GuiDraw.drawTexturedModalRect(0, 0, 11, 10, 0, 0);
	}

	@Override
	public void drawForeground(int recipe) {
		super.drawForeground(recipe);
	}

	@Override
	public void drawExtras(int recipe) {
		CachedCrucibleRecipe r = (CachedCrucibleRecipe) arecipes.get(recipe);
		int coords[] = {0,0,0,0,260,260};
		int textCoords[] = {0,0};
		for (Aspect aspect : r.aspects.getAspects()) {
			if (aspect.isPrimal()) {
				if (aspect.getName().equalsIgnoreCase("Ignis")) {//Oh no, no switch statement! D:
					textCoords[0] = 8;
					textCoords[1] = 101;
				} else if (aspect.getName().equalsIgnoreCase("Aer")) {
					textCoords[0] = 68;
					textCoords[1] = 17;
				} else if (aspect.getName().equalsIgnoreCase("Terra")) {
					textCoords[0] = 8;
					textCoords[1] = 43;
				} else if (aspect.getName().equalsIgnoreCase("Aqua")) {
					textCoords[0] = 68;
					textCoords[1] = 98;
				} else if (aspect.getName().equalsIgnoreCase("Ordo")) {
					textCoords[0] = 104;
					textCoords[1] = 101;
				} else if (aspect.getName().equalsIgnoreCase("Perditio")) {
					textCoords[0] = 104;
					textCoords[1] = 43;
				}
				GuiDraw.drawString(r.aspects.getAmount(aspect) + "", textCoords[0], textCoords[1], 0xFFFFFF, true);
			}
		}
		GL11.glScalef(.065f,.065f,.065f);
		for (Aspect aspect : r.aspects.getAspects()){
			if (aspect.isPrimal()) {
				if (aspect.getName().equalsIgnoreCase("Ignis")) {//Oh no, no switch statement! D:
					coords[0] = 78;
					coords[1] = 1281;
				}else if (aspect.getName().equalsIgnoreCase("Aer")) {
					coords[0] = 808;
					coords[1] = 48;
				}else if (aspect.getName().equalsIgnoreCase("Terra")) {
					coords[0] = 78;
					coords[1] = 387;
				}else if (aspect.getName().equalsIgnoreCase("Aqua")) {
					coords[0] = 808;
					coords[1] = 1621;
				}else if (aspect.getName().equalsIgnoreCase("Ordo")) {
					coords[0] = 1540;
					coords[1] = 1281;
				}else if (aspect.getName().equalsIgnoreCase("Perditio")) {
					coords[0] = 1540;
					coords[1] = 387;
				}
				Color color = new Color(aspect.getColor());
				GL11.glColor4f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1f);
				GuiDraw.changeTexture(aspect.getImage());
				GuiDraw.drawTexturedModalRect(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
			}
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		List recipes = ThaumcraftApi.getCraftingRecipes();
		for (int i = 0; i < recipes.size(); i++){//Sorry, no enhanced for loop here :P
			if (recipes.get(i) instanceof CrucibleRecipe) {
				CrucibleRecipe recipe = (CrucibleRecipe) recipes.get(i);
				if (ThaumcraftApiHelper.isResearchComplete(Reference.PLAYER_NAME, recipe.key)){
					if (recipe.getRecipeOutput().isItemEqual(result)) {
						if (checkDupe(recipe)) {
							this.arecipes.add(new CachedCrucibleRecipe(recipe));
						}
					}
				}
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		List recipes = ThaumcraftApi.getCraftingRecipes();
		for (int i = 0; i < recipes.size(); i++) {//Sorry, no enhanced for loop here again :P
			if (recipes.get(i) instanceof CrucibleRecipe) {
				CrucibleRecipe recipe = (CrucibleRecipe) recipes.get(i);
				if (ThaumcraftApiHelper.isResearchComplete(Reference.PLAYER_NAME, recipe.key)){
					ItemStack item = (ItemStack) recipe.catalyst;
					if (item.isItemEqual(ingredient)) {
						if (checkDupe(recipe)) {
							this.arecipes.add(new CachedCrucibleRecipe(recipe));
						}
					}
				}
			}
		}
	}

	private boolean checkDupe(CrucibleRecipe recipe) {
		for (Object o : this.arecipes.toArray()){
			if (o instanceof CachedCrucibleRecipe){
				CachedCrucibleRecipe r = (CachedCrucibleRecipe) o;
				if (r.recipe.catalyst == recipe.catalyst){
					return false;
				}
			}
		}
		return true;
	}

	public class CachedCrucibleRecipe extends CachedRecipe{
		private final int[] outCoords = {139,54};
		private final int[] inCoords = {29,53};

		private PositionedStack output;
		private PositionedStack inputs;

		public AspectList aspects;
		public CrucibleRecipe recipe;

		public CachedCrucibleRecipe(CrucibleRecipe recipe){//Wow that's a long class name!
			this.aspects = recipe.aspects;
			this.output = new PositionedStack(recipe.getRecipeOutput(), outCoords[0], outCoords[1]);
			this.recipe = recipe;
			this.inputs = new PositionedStack(recipe.catalyst, inCoords[0], inCoords[1]);

		}

		@Override
		public PositionedStack getResult() {
			return this.output;
		}

		@Override
		public PositionedStack getIngredient() {
			return this.inputs;
		}
	}

	@Override
	public String getOverlayIdentifier(){
		return "crucible";
	}
}
