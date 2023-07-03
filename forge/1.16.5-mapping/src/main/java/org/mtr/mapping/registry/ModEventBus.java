package org.mtr.mapping.registry;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockItem;
import org.mtr.mapping.mapper.ItemExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public final class ModEventBus {

	static final Set<Supplier<BlockExtension>> BLOCKS = new HashSet<>();
	static final Set<Supplier<BlockItem>> BLOCK_ITEMS = new HashSet<>();
	static final Set<Supplier<ItemExtension>> ITEMS = new HashSet<>();
	static final Set<Supplier<TileEntityType<? extends BlockEntityExtension>>> BLOCK_ENTITY_TYPES = new HashSet<>();

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<net.minecraft.block.Block> event) {
		BLOCKS.forEach(supplier -> event.getRegistry().register(supplier.get()));
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<net.minecraft.item.Item> event) {
		BLOCK_ITEMS.forEach(supplier -> event.getRegistry().register(supplier.get()));
		ITEMS.forEach(supplier -> event.getRegistry().register(supplier.get()));
	}

	@SubscribeEvent
	public void registerBlockEntityTypes(RegistryEvent.Register<TileEntityType<?>> event) {
		BLOCK_ENTITY_TYPES.forEach(supplier -> event.getRegistry().register(supplier.get()));
	}
}
