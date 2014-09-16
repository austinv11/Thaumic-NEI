package com.austinv11.thaumicnei.handlers;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.austinv11.thaumicnei.reference.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapedArcaneRecipe;

import java.util.ArrayList;
import java.util.List;

import static thaumcraft.api.aspects.Aspect.*;

public class ShapedArcaneWorkbenchHandler extends TemplateRecipeHandler {

	@Override
	public String getGuiTexture() {
		return "thaumicnei:gui/gui_arcaneworkbench.png";
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("thaumicnei:gui.nei.arcaneWorkbench.shaped");
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public void drawBackground(int recipe) {
		GL11.glColor4f(1, 1, 1, 1);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 130);
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
				if (aspect.getName().equalsIgnoreCase("ignis")) {//Oh no, no switch statement! D:

				}else if (aspect.getName().equalsIgnoreCase("ignis")) {

				}
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
		private final int[] outCoords = {150,25};
		private final int[] inCoords = {20,60,90,40,70,100};//3 x coords, then 3 y coords

		private PositionedStack output;
		private List<PositionedStack> inputs = new ArrayList<PositionedStack>();

		public AspectList aspects;

		public CachedShapedArcaneWorkbenchRecipe(ShapedArcaneRecipe recipe){//Wow that's a long class name!
			this.aspects = recipe.getAspects();
			this.output = new PositionedStack(recipe.getRecipeOutput(), outCoords[0], outCoords[1]);
			Object[] input = recipe.getInput();
			int i = 0;
			for (Object inputItem : input){
				if (inputItem != null){
					if (inputItem instanceof ItemStack) {
						switch (i) {
							case 0:
								this.inputs.add(new PositionedStack(inputItem, inCoords[0], inCoords[0]));
								break;
							case 1:
								this.inputs.add(new PositionedStack(inputItem, inCoords[1], inCoords[0]));
								break;
							case 2:
								this.inputs.add(new PositionedStack(inputItem, inCoords[2], inCoords[0]));
								break;
							case 3:
								this.inputs.add(new PositionedStack(inputItem, inCoords[0], inCoords[1]));
								break;
							case 4:
								this.inputs.add(new PositionedStack(inputItem, inCoords[1], inCoords[1]));
								break;
							case 5:
								this.inputs.add(new PositionedStack(inputItem, inCoords[2], inCoords[1]));
								break;
							case 6:
								this.inputs.add(new PositionedStack(inputItem, inCoords[0], inCoords[2]));
								break;
							case 7:
								this.inputs.add(new PositionedStack(inputItem, inCoords[1], inCoords[2]));
								break;
							case 8:
								this.inputs.add(new PositionedStack(inputItem, inCoords[2], inCoords[2]));
								break;
						}
					}
					i++;
				}
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
