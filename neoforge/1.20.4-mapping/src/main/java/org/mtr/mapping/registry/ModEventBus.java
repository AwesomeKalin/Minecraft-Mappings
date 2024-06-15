package org.mtr.mapping.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.mtr.mapping.holder.Block;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.holder.SoundEvent;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockItemExtension;
import org.mtr.mapping.mapper.EntityExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ModEventBus {

	final Map<Identifier, Supplier<Block>> BLOCKS = new HashMap<>();
	final Map<Identifier, Supplier<BlockItemExtension>> BLOCK_ITEMS = new HashMap<>();
	final Map<Identifier, Supplier<Item>> ITEMS = new HashMap<>();
	final Map<Identifier, Supplier<BlockEntityType<? extends BlockEntityExtension>>> blockEntityTypes = new HashMap<>();
	final Map<Identifier, Supplier<EntityType<? extends EntityExtension>>> entityTypes = new HashMap<>();
	final Map<Identifier, Supplier<ParticleType<?>>> particleTypes = new HashMap<>();
	final Map<Identifier, Supplier<SoundEvent>> soundEvents = new HashMap<>();
	final List<CreativeModeTabHolder> creativeModeTabs = new ArrayList<>();

	@SubscribeEvent
	public void register(RegisterEvent event) {
		event.register(BuiltInRegistries.BLOCK, helper -> BLOCKS.forEach((identifier, supplier) -> helper.register(identifier.data, supplier.get().data)));
		event.register(BuiltInRegistries.ITEM, helper -> {
			BLOCK_ITEMS.forEach((identifier, supplier) -> helper.register(identifier.data, supplier.get()));
			ITEMS.forEach((identifier, supplier) -> helper.register(identifier.data, supplier.get()));
		});
		event.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, helper -> blockEntityTypes.forEach((identifier, supplier) -> helper.register(identifier.data, supplier.get())));
		event.register(BuiltInRegistries.ENTITY_TYPE, helper -> entityTypes.forEach((identifier, supplier) -> helper.register(identifier.data, supplier.get())));
		event.register(BuiltInRegistries.PARTICLE_TYPE, helper -> particleTypes.forEach((identifier, supplier) -> helper.register(identifier.data, supplier.get())));
		event.register(Registries.CREATIVE_MODE_TAB, helper -> creativeModeTabs.forEach(creativeModeTabHolder -> helper.register(creativeModeTabHolder.identifier, CreativeModeTab.builder()
				.title(Component.translatable(String.format("itemGroup.%s.%s", creativeModeTabHolder.identifier.getNamespace(), creativeModeTabHolder.identifier.getPath())))
				.icon(() -> creativeModeTabHolder.iconSupplier.get().data)
				.displayItems((params, output) -> creativeModeTabHolder.itemSuppliers.forEach(itemSupplier -> output.accept(itemSupplier.get().data)))
				.build()
		)));
		event.register(BuiltInRegistries.SOUND_EVENT, helper -> soundEvents.forEach(((identifier, supplier) -> helper.register(identifier.data, supplier.get()))));
	}
}
