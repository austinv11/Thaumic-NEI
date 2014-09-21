package com.austinv11.thaumicnei.overlay;

import codechicken.nei.*;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.IRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

public class OverlayHandler implements IOverlayHandler{
	private int xOffset, yOffset;
	private Class<? extends Slot> slot;

	public OverlayHandler(int xOff, int yOff, Class<? extends Slot> slot) {
		super();
		xOffset = xOff;
		yOffset = yOff;
		this.slot = slot;
	}

	private Slot findCorrespondingSlot(GuiContainer container, PositionedStack stack) {
		for (Object slot : container.inventorySlots.inventorySlots) {
			Slot slot2 = (Slot) slot;
			if ((slot2.xDisplayPosition == stack.relx + xOffset) && (slot2.yDisplayPosition == stack.rely + yOffset)) {
				return slot2;
			}
		}
		return null;
	}

	private Slot findStartingSlot(GuiContainer container, PositionedStack stack) {
		for (ItemStack item : stack.items) {
			for (Object slot : container.inventorySlots.inventorySlots) {
				Slot slot2 = (Slot) slot;
				if ((slot2.inventory == Minecraft.getMinecraft().thePlayer.inventory) || (slot.getClass() == Slot.class)) {
					ItemStack item2 = slot2.getStack();
					if (item != null && NEIServerUtils.areStacksSameType(item, item2)) {
						return slot2;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void overlayRecipe(GuiContainer guiContainer, IRecipeHandler recipeHandler, int index, boolean shift) {
		List<PositionedStack> ingredients = recipeHandler.getIngredientStacks(index);
		if (!shift) {
			IStackPositioner positioner = new OffsetPositioner(xOffset, yOffset);
			LayoutManager.overlayRenderer = new DefaultOverlayRenderer(ingredients, positioner);
		}else {
			if (NEIClientUtils.getHeldItem() != null) {
				return;
			}
			GuiContainerManager manager = GuiContainerManager.getManager(guiContainer);
			for (Object slot : guiContainer.inventorySlots.inventorySlots) {
				if (this.slot.isInstance(slot)) {
					Slot slot2 = (Slot) slot;
					manager.handleSlotClick(slot2.slotNumber, 0, 1);
				}
			}
			for (PositionedStack stack : ingredients) {
				if (stack != null) {
					Slot slotTo = findCorrespondingSlot(guiContainer, stack);
					if (slotTo != null) {
						Slot slotFrom = findStartingSlot(guiContainer, stack);
						if (slotFrom != null) {
							manager.handleSlotClick(slotFrom.slotNumber, 0, 0);
							manager.handleSlotClick(slotTo.slotNumber, 1, 0);
							manager.handleSlotClick(slotFrom.slotNumber, 0, 0);
						}
					}
				}
			}
		}
	}
}
