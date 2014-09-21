package com.austinv11.thaumicnei.overlay;

import com.austinv11.thaumicnei.utils.Logger;
import com.austinv11.thaumicnei.utils.ReflectionHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ArcaneWorkbenchHelper {

	public static Class<? extends Slot> slotCraftMatrix;
	public static Class<? extends Container> arcaneTable;
	public static Class<? extends GuiContainer> guiArcaneTable;

	public static void setUp() {
		try {
			slotCraftMatrix = ReflectionHelper.getClass("thaumcraft.common.container.SlotCraftingArcaneWorkbench", Slot.class);
			arcaneTable = ReflectionHelper.getClass("thaumcraft.common.container.ContainerArcaneWorkbench", Container.class);
			guiArcaneTable = ReflectionHelper.getClass("thaumcraft.client.gui.GuiArcaneWorkbench", GuiContainer.class);
		}catch (Exception e) {
			Logger.error("Exception occurred!");
			e.printStackTrace();
		}
	}
}
