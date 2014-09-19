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
import thaumcraft.api.crafting.ShapedArcaneRecipe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShapedArcaneWorkbenchHandler extends TemplateRecipeHandler {

	@Override
	public String getGuiTexture() {
		return "thaumcraft:textures/gui/gui_arcaneworkbench.png";
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal(Reference.MOD_ID+":gui.nei.arcaneWorkbench.shaped");
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public void drawBackground(int recipe) {
		GL11.glColor4f(1, 1, 1, 1);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 11, 10, 123, 126);
		GuiDraw.drawTexturedModalRect(126, 0, 147, 10, 75, 100);//FIXME: Add support to show wands
	}

	@Override
	public void drawForeground(int recipe) {
		super.drawForeground(recipe);
	}

	@Override
	public void drawExtras(int recipe) {
		CachedShapedArcaneWorkbenchRecipe r = (CachedShapedArcaneWorkbenchRecipe) arecipes.get(recipe);
		for (Aspect aspect : r.aspects.getAspects()){
			if (aspect.isPrimal()) {
				Color color = new Color(aspect.getColor());
				GL11.glColor4f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1f);
				GuiDraw.changeTexture(aspect.getImage());
				GL11.glScalef(.065f,.065f,.065f);
				int coords[] = {0,0,0,0,260,260};
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
				GuiDraw.drawTexturedModalRect(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
			}
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		List recipes = ThaumcraftApi.getCraftingRecipes();
		for (int i = 0; i < recipes.size(); i++){//Sorry, no enhanced for loop here :P
			if (recipes.get(i) instanceof ShapedArcaneRecipe) {
				ShapedArcaneRecipe recipe = (ShapedArcaneRecipe) recipes.get(i);
				//if (ThaumcraftApiHelper.isResearchComplete("", recipe.getResearch())){TODO
					if (recipe.getRecipeOutput().isItemEqual(result)) {
						this.arecipes.add(new CachedShapedArcaneWorkbenchRecipe(recipe));
					}
				//}
			}
		}
	}

	public class CachedShapedArcaneWorkbenchRecipe extends CachedRecipe{
		private final int[] outCoords = {139,54};
		private final int[] inCoords = {29,53,77,56,63,85};//3 x coords, then 3 y coords

		private PositionedStack output;
		private List<PositionedStack> inputs = new ArrayList<PositionedStack>();
		//private PositionedStack extra = new PositionedStack()TODO add wand extras

		public AspectList aspects;

		public CachedShapedArcaneWorkbenchRecipe(ShapedArcaneRecipe recipe){//Wow that's a long class name!
			this.aspects = recipe.getAspects();
			this.output = new PositionedStack(recipe.getRecipeOutput(), outCoords[0], outCoords[1]);
			Object[] input = recipe.getInput();
			int i = 0;
			for (Object inputItem : input){
				//if (inputItem != null){
					if (inputItem instanceof ItemStack) {
						switch (i) {
							case 0:
								if (inputItem != null) {

										this.inputs.add(new PositionedStack(inputItem, inCoords[0], inCoords[0]));
								}
								break;
							case 1:
								if (inputItem != null){
										this.inputs.add(new PositionedStack(inputItem, inCoords[1], inCoords[0]));
								}
								break;
							case 2:
								if (inputItem != null){
										this.inputs.add(new PositionedStack(inputItem, inCoords[2], inCoords[0]));

								}
								break;
							case 3:
								if (inputItem != null){
										this.inputs.add(new PositionedStack(inputItem, inCoords[0], inCoords[1]));

								}
								break;
							case 4:
								if (inputItem != null){
										this.inputs.add(new PositionedStack(inputItem, inCoords[1], inCoords[1]));

								}
								break;
							case 5:
								if (inputItem != null){
										this.inputs.add(new PositionedStack(inputItem, inCoords[2], inCoords[1]));

								}
								break;
							case 6:
								if (inputItem != null){
										this.inputs.add(new PositionedStack(inputItem, inCoords[0], inCoords[2]));

								}
								break;
							case 7:
								if (inputItem != null){
										this.inputs.add(new PositionedStack(inputItem, inCoords[1], inCoords[2]));

								}
								break;
							case 8:
								if (inputItem != null){

										this.inputs.add(new PositionedStack(inputItem, inCoords[2], inCoords[2]));

								}
								break;
						}
					}
					i++;
				//}
			}
		}

		@Override
		public PositionedStack getResult() {
			return this.output;
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return this.inputs;
		}
	}
}
