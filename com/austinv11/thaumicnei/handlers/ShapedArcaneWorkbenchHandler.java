package com.austinv11.thaumicnei.handlers;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.austinv11.thaumicnei.reference.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.ShapedArcaneRecipe;

import java.util.List;

public class ShapedArcaneWorkbenchHandler extends TemplateRecipeHandler {

	@Override
	public String getGuiTexture() {
		return Reference.MOD_ID+":textures/gui/gui_arcaneworkbench.png";
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("gui.nei.arcaneWorkbench.shaped");
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public void drawBackground(int recipe) {
		GL11.glColor4f(1, 1, 1, 1);
		GuiDraw.changeTexture(this.getGuiTexture());
		GuiDraw.drawTexturedModalRect(6, 32, 13, 0, 13, 13);
		GuiDraw.drawTexturedModalRect(6, 67, 0, 0, 13, 20);
	}

	@Override
	public void drawForeground(int recipe) {
		super.drawForeground(recipe);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		List recipes = ThaumcraftApi.getCraftingRecipes();
		for (int i = 0; i < recipes.size(); i++){//Sorry, no enhanced for loop here :P
			if (recipes.get(i) instanceof ShapedArcaneRecipe){

			}
		}
	}

	public class CachedShapedArcaneWorkbenchRecipe extends CachedRecipe{
		private final int[] outCoords = {0,0};
		private final int[] inCoords = {0,0,0,0,0,0};//3 x coords, then 3 y coords

		private PositionedStack output;
		private List<PositionedStack> inputs;
		private List<PositionedStack> otherInputs;

		public CachedShapedArcaneWorkbenchRecipe(ShapedArcaneRecipe recipe){//Wow that's a long class name!
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

		@Override
		public List<PositionedStack> getOtherStacks(){
			return this.otherInputs;
		}
	}
}
