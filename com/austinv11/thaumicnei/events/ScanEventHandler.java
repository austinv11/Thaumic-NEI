package com.austinv11.thaumicnei.events;

import com.austinv11.thaumicnei.reference.Reference;
import com.austinv11.thaumicnei.utils.Logger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.research.IScanEventHandler;
import thaumcraft.api.research.ScanResult;

public class ScanEventHandler implements IScanEventHandler{

	@Override
	public ScanResult scanPhenomena(ItemStack stack, World world, EntityPlayer player) {
		if (!Reference.SCANNED_ITEMS.contains(stack)){
			Reference.SCANNED_ITEMS.add(stack);
		}
		return null;
	}
}
