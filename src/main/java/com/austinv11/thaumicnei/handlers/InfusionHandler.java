package com.austinv11.thaumicnei.handlers;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.austinv11.thaumicnei.reference.Config;
import com.austinv11.thaumicnei.reference.Reference;
import com.austinv11.thaumicnei.utils.Logger;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InfusionHandler extends TemplateRecipeHandler {

	@Override
	public String getGuiTexture() {
		return "thaumcraft:textures/gui/gui_researchbook_overlay.png";
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal(Reference.MOD_ID+":gui.nei.infusion");
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public void drawBackground(int recipe) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glScalef(2f, 2f, 2f);
		//GL11.glColor4f(1, 1, 1, 1);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(18, 13, 205, 75, 45, 45);//Infusion grid thingy

		GL11.glScalef(1f, 1f, 1f);
		GuiDraw.drawTexturedModalRect(34, -8, 21, 0, 15, 20);//Output slot
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void drawForeground(int recipe) {
		super.drawForeground(recipe);
	}

	private HashMap<String,int[]> getAspectCoords(AspectList aspects) {
		int[] rows = {1325,1205,1535};//Y values are as follows: 1 row, 2 rows - row # 1, 2 rows - row #2
		int[] columns = {30,330,630};//X values are as follows: column #1, column #2, column #3
		int[] coords = {0,0};
		HashMap<String,int[]> map = new HashMap<String,int[]>();
		int aspectNum = aspects.getAspects().length;
		int i = 0;
		for (Aspect aspect : aspects.getAspects()) {
			if (aspectNum != 0) {
				if (aspectNum > 0 && aspectNum < 4) {
					coords[1] = rows[0];
					if (aspectNum == 1) {
						coords[0] = columns[1];
					}else if (aspectNum == 2) {
						if (i == 0) {
							coords[0] = columns[0];
						}else {
							coords[0] = columns[2];
						}
					}else if (aspectNum == 3) {
						coords[0] = columns[i];
					}
				}else {
					if (i >= 0 && i < 2) {
						coords[1] = rows[1];
						if (aspectNum == 4 ||aspectNum == 5) {
							if (i == 0) {
								coords[0] = columns[0];
							}else {
								coords[0] = columns[2];
							}
						}else {
							coords[0] = columns[i];
						}
					}else {
						coords[1] = rows[2];
						if (aspectNum == 4) {
							if (i == 2) {
								coords[0] = columns[0];
							}else {
								coords[0] = columns[2];
							}
						}else {
							coords[0] = columns[i];
						}
					}
				}
			}
			map.put(aspect.getName(), coords.clone());
			i++;
		}
		return map;
	}

	private HashMap<String,int[]> getTextCoords(HashMap<String,int[]> map, AspectList aspects) {
		HashMap<String,int[]> rMap = new HashMap<String,int[]>();
		int[] coords2 = {0,0};
		for (Aspect aspect : aspects.getAspects()) {
			int[] coords = map.get(aspect.getName());
			switch (coords[0]){//TODO update coords
				case 30:
					coords2[0] = 11;
					break;
				case 330:
					coords2[0] = 31;
					break;
				case 630:
					coords2[0] = 51;
					break;
			}
			switch (coords[1]){//TODO update coords
				case 1325:
					coords2[1] = 102;
					break;
				case 1205:
					coords2[1] = 90;
					break;
				case 1535:
					coords2[1] = 114;
					break;
			}
			rMap.put(aspect.getName(),coords2.clone());
		}
		return rMap;
	}

	@Override
	public void drawExtras(int recipe) {
		CachedInfusionRecipe r = (CachedInfusionRecipe) arecipes.get(recipe);
		/*HashMap<String,int[]> map = getAspectCoords(r.aspects);
		HashMap<String,int[]> textMap = getTextCoords(map,r.aspects);
		int coords[] = {0,0};
		int coords2[] = {0,0};
		GL11.glScalef(.065f,.065f,.065f);
		for (Aspect aspect : r.aspects.getAspects()) {
			coords = map.get(aspect.getName());
			Color color = new Color(aspect.getColor());
			GL11.glColor4f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1f);
			GuiDraw.changeTexture(aspect.getImage());
			GuiDraw.drawTexturedModalRect(coords[0], coords[1], 0, 0, 260, 260);
		}
		GL11.glScalef(15.625f,15.625f,15.625f);
		for (Aspect aspect : r.aspects.getAspects()){
			coords2 = textMap.get(aspect.getName());
			GuiDraw.drawString(r.aspects.getAmount(aspect)+"",coords2[0],coords2[1],0xFFFFFF, true);
		}*/
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		List recipes = ThaumcraftApi.getCraftingRecipes();
		for (int i = 0; i < recipes.size(); i++){//Sorry, no enhanced for loop here :P
			if (recipes.get(i) instanceof InfusionRecipe) {
				InfusionRecipe recipe = (InfusionRecipe) recipes.get(i);
				if (ThaumcraftApiHelper.isResearchComplete(Reference.PLAYER_NAME, recipe.getResearch()) || Config.cheatMode){
					Object output = recipe.getRecipeOutput();
					if (output instanceof ItemStack) {
						if (((ItemStack)output).isItemEqual(result)) {
							if (checkDupe(recipe)) {
								this.arecipes.add(new CachedInfusionRecipe(recipe));
							}
						}
					}else {
						//TODO
					}
				}
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		List recipes = ThaumcraftApi.getCraftingRecipes();
		for (int i = 0; i < recipes.size(); i++) {//Sorry, no enhanced for loop here again :P
			if (recipes.get(i) instanceof InfusionRecipe) {
				InfusionRecipe recipe = (InfusionRecipe) recipes.get(i);
				if (ThaumcraftApiHelper.isResearchComplete(Reference.PLAYER_NAME, recipe.getResearch()) || Config.cheatMode){
					if (recipe.getComponents() != null) {
						ArrayList<ItemStack> components = new ArrayList<ItemStack>(Arrays.asList(recipe.getComponents()));
						if (recipe.getRecipeInput().isItemEqual(ingredient) || components.contains(ingredient)) {
							if (checkDupe(recipe)) {
								this.arecipes.add(new CachedInfusionRecipe(recipe));
							}
						}
					}
				}
			}
		}
	}

	private boolean checkDupe(InfusionRecipe recipe) {
		for (Object o : this.arecipes.toArray()){
			if (o instanceof CachedInfusionRecipe){
				CachedInfusionRecipe r = (CachedInfusionRecipe) o;
				if (r.recipe.getRecipeInput() == recipe.getRecipeInput()){
					if (r.recipe.getRecipeOutput().equals(recipe.getRecipeOutput())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public class CachedInfusionRecipe extends CachedRecipe{
		private final int[] outCoords = {74,-3};
		private final int[] inCoords1 = {74,65};
		private final int[][] inCoords2 = {{74,25},{0,0},{0,0},{114,65},{0,0},{0,0},
				{74,105},{0,0},{0,0},{34,65},{0,0},{0,0}};//All the positions of items (clockwise)

		private PositionedStack output;
		private List<PositionedStack> inputs = new ArrayList<PositionedStack>();

		public AspectList aspects;
		public InfusionRecipe recipe;
		public int instability;//TODO

		public CachedInfusionRecipe(InfusionRecipe recipe){
			this.aspects = recipe.getAspects();
			this.output = new PositionedStack(recipe.getRecipeOutput(), outCoords[0], outCoords[1]);
			this.recipe = recipe;
			this.instability = recipe.getInstability();
			this.inputs.add(new PositionedStack(recipe.getRecipeInput(), inCoords1[0], inCoords1[1]));
			calcInputPositions(recipe.getComponents());
		}

		private void calcInputPositions(ItemStack[] items) {
			switch (items.length){
				case 1:
					this.inputs.add(new PositionedStack(items[0], inCoords2[6][0], inCoords2[6][1]));
					break;
				case 2:
					this.inputs.add(new PositionedStack(items[0], inCoords2[3][0], inCoords2[3][1]));
					this.inputs.add(new PositionedStack(items[1], inCoords2[9][0], inCoords2[9][1]));
					break;
				case 3:
					this.inputs.add(new PositionedStack(items[0], inCoords2[6][0], inCoords2[6][1]));
					this.inputs.add(new PositionedStack(items[1], inCoords2[3][0], inCoords2[3][1]));
					this.inputs.add(new PositionedStack(items[2], inCoords2[9][0], inCoords2[9][1]));
					break;
				case 4:
					this.inputs.add(new PositionedStack(items[0], inCoords2[6][0], inCoords2[6][1]));
					this.inputs.add(new PositionedStack(items[1], inCoords2[3][0], inCoords2[3][1]));
					this.inputs.add(new PositionedStack(items[2], inCoords2[9][0], inCoords2[9][1]));
					this.inputs.add(new PositionedStack(items[3], inCoords2[0][0], inCoords2[0][1]));
					break;
				case 5:
					this.inputs.add(new PositionedStack(items[0], inCoords2[1][0], inCoords2[1][1]));
					this.inputs.add(new PositionedStack(items[1], inCoords2[4][0], inCoords2[4][1]));
					this.inputs.add(new PositionedStack(items[2], inCoords2[6][0], inCoords2[6][1]));
					this.inputs.add(new PositionedStack(items[3], inCoords2[8][0], inCoords2[8][1]));
					this.inputs.add(new PositionedStack(items[4], inCoords2[0][0], inCoords2[0][1]));
					break;
				case 6:
					this.inputs.add(new PositionedStack(items[0], inCoords2[0][0], inCoords2[0][1]));
					this.inputs.add(new PositionedStack(items[1], inCoords2[2][0], inCoords2[2][1]));
					this.inputs.add(new PositionedStack(items[2], inCoords2[4][0], inCoords2[4][1]));
					this.inputs.add(new PositionedStack(items[3], inCoords2[6][0], inCoords2[6][1]));
					this.inputs.add(new PositionedStack(items[4], inCoords2[8][0], inCoords2[8][1]));
					this.inputs.add(new PositionedStack(items[5], inCoords2[10][0], inCoords2[10][1]));
					break;
				case 7:
					this.inputs.add(new PositionedStack(items[0], inCoords2[1][0], inCoords2[1][1]));
					this.inputs.add(new PositionedStack(items[1], inCoords2[3][0], inCoords2[3][1]));
					this.inputs.add(new PositionedStack(items[2], inCoords2[5][0], inCoords2[5][1]));
					this.inputs.add(new PositionedStack(items[3], inCoords2[6][0], inCoords2[6][1]));
					this.inputs.add(new PositionedStack(items[4], inCoords2[7][0], inCoords2[7][1]));
					this.inputs.add(new PositionedStack(items[5], inCoords2[9][0], inCoords2[9][1]));
					this.inputs.add(new PositionedStack(items[6], inCoords2[11][0], inCoords2[11][1]));
					break;
				case 8:
					this.inputs.add(new PositionedStack(items[0], inCoords2[1][0], inCoords2[1][1]));
					this.inputs.add(new PositionedStack(items[1], inCoords2[2][0], inCoords2[2][1]));
					this.inputs.add(new PositionedStack(items[2], inCoords2[4][0], inCoords2[4][1]));
					this.inputs.add(new PositionedStack(items[3], inCoords2[5][0], inCoords2[5][1]));
					this.inputs.add(new PositionedStack(items[4], inCoords2[7][0], inCoords2[7][1]));
					this.inputs.add(new PositionedStack(items[5], inCoords2[8][0], inCoords2[8][1]));
					this.inputs.add(new PositionedStack(items[6], inCoords2[10][0], inCoords2[10][1]));
					this.inputs.add(new PositionedStack(items[7], inCoords2[11][0], inCoords2[11][1]));
					break;
				case 9:
					this.inputs.add(new PositionedStack(items[0], inCoords2[1][0], inCoords2[1][1]));
					this.inputs.add(new PositionedStack(items[1], inCoords2[2][0], inCoords2[2][1]));
					this.inputs.add(new PositionedStack(items[2], inCoords2[4][0], inCoords2[4][1]));
					this.inputs.add(new PositionedStack(items[3], inCoords2[5][0], inCoords2[5][1]));
					this.inputs.add(new PositionedStack(items[4], inCoords2[6][0], inCoords2[6][1]));
					this.inputs.add(new PositionedStack(items[5], inCoords2[7][0], inCoords2[7][1]));
					this.inputs.add(new PositionedStack(items[6], inCoords2[8][0], inCoords2[8][1]));
					this.inputs.add(new PositionedStack(items[7], inCoords2[10][0], inCoords2[10][1]));
					this.inputs.add(new PositionedStack(items[8], inCoords2[11][0], inCoords2[11][1]));
					break;
				case 10:
					this.inputs.add(new PositionedStack(items[0], inCoords2[1][0], inCoords2[1][1]));
					this.inputs.add(new PositionedStack(items[1], inCoords2[2][0], inCoords2[2][1]));
					this.inputs.add(new PositionedStack(items[2], inCoords2[3][0], inCoords2[3][1]));
					this.inputs.add(new PositionedStack(items[3], inCoords2[4][0], inCoords2[4][1]));
					this.inputs.add(new PositionedStack(items[4], inCoords2[5][0], inCoords2[5][1]));
					this.inputs.add(new PositionedStack(items[5], inCoords2[7][0], inCoords2[7][1]));
					this.inputs.add(new PositionedStack(items[6], inCoords2[8][0], inCoords2[8][1]));
					this.inputs.add(new PositionedStack(items[7], inCoords2[9][0], inCoords2[9][1]));
					this.inputs.add(new PositionedStack(items[8], inCoords2[10][0], inCoords2[10][1]));
					this.inputs.add(new PositionedStack(items[9], inCoords2[11][0], inCoords2[11][1]));
					break;
				case 11:
					this.inputs.add(new PositionedStack(items[0], inCoords2[1][0], inCoords2[1][1]));
					this.inputs.add(new PositionedStack(items[1], inCoords2[2][0], inCoords2[2][1]));
					this.inputs.add(new PositionedStack(items[2], inCoords2[3][0], inCoords2[3][1]));
					this.inputs.add(new PositionedStack(items[3], inCoords2[4][0], inCoords2[4][1]));
					this.inputs.add(new PositionedStack(items[4], inCoords2[5][0], inCoords2[5][1]));
					this.inputs.add(new PositionedStack(items[5], inCoords2[6][0], inCoords2[11][1]));
					this.inputs.add(new PositionedStack(items[6], inCoords2[7][0], inCoords2[7][1]));
					this.inputs.add(new PositionedStack(items[7], inCoords2[8][0], inCoords2[8][1]));
					this.inputs.add(new PositionedStack(items[8], inCoords2[9][0], inCoords2[9][1]));
					this.inputs.add(new PositionedStack(items[9], inCoords2[10][0], inCoords2[10][1]));
					this.inputs.add(new PositionedStack(items[10], inCoords2[11][0], inCoords2[11][1]));
					break;
				case 12:
					this.inputs.add(new PositionedStack(items[0], inCoords2[1][0], inCoords2[1][1]));
					this.inputs.add(new PositionedStack(items[1], inCoords2[2][0], inCoords2[2][1]));
					this.inputs.add(new PositionedStack(items[2], inCoords2[3][0], inCoords2[3][1]));
					this.inputs.add(new PositionedStack(items[3], inCoords2[4][0], inCoords2[4][1]));
					this.inputs.add(new PositionedStack(items[4], inCoords2[5][0], inCoords2[5][1]));
					this.inputs.add(new PositionedStack(items[5], inCoords2[6][0], inCoords2[6][1]));
					this.inputs.add(new PositionedStack(items[6], inCoords2[7][0], inCoords2[7][1]));
					this.inputs.add(new PositionedStack(items[7], inCoords2[8][0], inCoords2[8][1]));
					this.inputs.add(new PositionedStack(items[8], inCoords2[9][0], inCoords2[9][1]));
					this.inputs.add(new PositionedStack(items[9], inCoords2[10][0], inCoords2[10][1]));
					this.inputs.add(new PositionedStack(items[10], inCoords2[11][0], inCoords2[11][1]));
					this.inputs.add(new PositionedStack(items[11], inCoords2[0][0], inCoords2[0][1]));
					break;
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

	@Override
	public String getOverlayIdentifier(){
		return "infusion";
	}
}
