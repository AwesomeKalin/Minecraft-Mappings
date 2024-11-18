package org.mtr.mapping.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import org.mtr.mapping.mapper.BlockItemExtension;
import org.mtr.mapping.mapper.EntityExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ModEventBus {

	public static String packetIdentifier;
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
		/*event.register(ResourceKey.createRegistryKey((ResourceLocation) BuiltInRegistries.BLOCK), registry -> {
			BLOCKS.forEach(((identifier, blockSupplier) -> {
				registry.register(identifier.data, blockSupplier.get().data);
			}));
		});

		event.register(ResourceKey.createRegistryKey((ResourceLocation) BuiltInRegistries.ITEM), registry -> {
			ITEMS.forEach(((identifier, itemSupplier) -> {
				registry.register(identifier.data, itemSupplier.get().data);
			}));
			BLOCK_ITEMS.forEach((((identifier, blockItemExtensionSupplier) -> {
				registry.register(identifier.data, blockItemExtensionSupplier.get());
			})));
		});

		event.register(ResourceKey.createRegistryKey((ResourceLocation) BuiltInRegistries.BLOCK_ENTITY_TYPE), registry -> {
			blockEntityTypes.forEach((((identifier, blockEntityTypeSupplier) -> {
				registry.register(identifier.data, blockEntityTypeSupplier.get());
			})));
		});

		event.register(ResourceKey.createRegistryKey((ResourceLocation) BuiltInRegistries.ENTITY_TYPE), registry -> {
			entityTypes.forEach(((identifier, entityTypeSupplier) -> {
				registry.register(identifier.data, entityTypeSupplier.get());
			}));
		});

		event.register(ResourceKey.createRegistryKey((ResourceLocation) BuiltInRegistries.PARTICLE_TYPE), registry -> {
			particleTypes.forEach(((identifier, particleTypeSupplier) -> {
				registry.register(identifier.data, particleTypeSupplier);
			}));
		});

		event.register(Registries.CREATIVE_MODE_TAB, helper -> creativeModeTabs.forEach(creativeModeTabHolder -> helper.register(creativeModeTabHolder.identifier, CreativeModeTab.builder()
				.title(Component.translatable(String.format("itemGroup.%s.%s", creativeModeTabHolder.identifier.getNamespace(), creativeModeTabHolder.identifier.getPath())))
				.icon(() -> creativeModeTabHolder.iconSupplier.get().data)
				.displayItems((params, output) -> creativeModeTabHolder.itemSuppliers.forEach(itemSupplier -> output.accept(itemSupplier.get().data)))
				.build()
		)));

		event.register(ResourceKey.createRegistryKey((ResourceLocation) BuiltInRegistries.SOUND_EVENT), registry -> {
			soundEvents.forEach(((identifier, soundEventSupplier) -> {
				registry.register(identifier.data, soundEventSupplier);
			}));
		});*/
	}
}
