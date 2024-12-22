package org.mtr.mapping.registry;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.netty.buffer.Unpooled;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockItemExtension;
import org.mtr.mapping.mapper.EntityExtension;
import org.mtr.mapping.networking.PacketObject;
import org.mtr.mapping.tool.DummyClass;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Registry extends DummyClass {

	private final MainEventBus mainEventBus = new MainEventBus();
	private final ModEventBus modEventBus = new ModEventBus();
	public static final Map<String, Function<PacketBufferReceiver, ? extends PacketHandler>> packets = new HashMap<>();
	public final EventRegistry eventRegistry = new EventRegistry(mainEventBus);
	public static final String PROTOCOL_VERSION = "1";
	public static final Map<String, DeferredRegister<net.minecraft.world.level.block.Block>> BLOCK_REGISTERS = new HashMap<String, DeferredRegister<net.minecraft.world.level.block.Block>>();
	public static final Map<String, DeferredRegister<net.minecraft.world.item.Item>> ITEM_REGISTERS = new HashMap<String, DeferredRegister<net.minecraft.world.item.Item>>();
	public static final Map<String, DeferredRegister<BlockEntityType<?>>> BLOCK_ENTITY_TYPE_REGISTERS = new HashMap<String, DeferredRegister<BlockEntityType<?>>>();
	public static final Map<String, DeferredRegister<net.minecraft.world.entity.EntityType<?>>> ENTITY_TYPE_REGISTERS = new HashMap<String, DeferredRegister<net.minecraft.world.entity.EntityType<?>>>();
	public static final Map<String, DeferredRegister<ParticleType<?>>> PARTICLE_TYPE_REGISTERS = new HashMap<String, DeferredRegister<ParticleType<?>>>();
	public static final Map<String, DeferredRegister<CreativeModeTab>> CREATIVE_TAB_REGISTERS = new HashMap<String, DeferredRegister<CreativeModeTab>>();
	public static final Map<String, DeferredRegister<SoundEvent>> SOUND_EVENT_REGISTERS = new HashMap<String, DeferredRegister<SoundEvent>>();

	@MappedMethod
	public void init() {
		IEventBus eventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
		assert eventBus != null;

		BLOCK_REGISTERS.forEach((id, register) -> {
			register.register(eventBus);
		});

		ITEM_REGISTERS.forEach((id, register) -> {
			register.register(eventBus);
		});

		BLOCK_ENTITY_TYPE_REGISTERS.forEach((id, register) -> {
			register.register(eventBus);
		});

		ENTITY_TYPE_REGISTERS.forEach((id, register) -> {
			register.register(eventBus);
		});

		PARTICLE_TYPE_REGISTERS.forEach((id, register) -> {
			register.register(eventBus);
		});

		CREATIVE_TAB_REGISTERS.forEach((id, register) -> {
			register.register(eventBus);
		});

		SOUND_EVENT_REGISTERS.forEach((id, register) -> {
            register.register(eventBus);
		});
		//NeoForge.EVENT_BUS.register(modEventBus);
	}

	@MappedMethod
	public BlockRegistryObject registerBlock(Identifier identifier, Supplier<Block> supplier) {
		if (!BLOCK_REGISTERS.containsKey(identifier.getNamespace())) {
			BLOCK_REGISTERS.put(identifier.getNamespace(), DeferredRegister.create(BuiltInRegistries.BLOCK, identifier.getNamespace()));
		}

		BLOCK_REGISTERS.get(identifier.getNamespace()).register(identifier.getPath(), () -> supplier.get().data);
		return new BlockRegistryObject(identifier);
	}

	@MappedMethod
	public BlockRegistryObject registerBlockWithBlockItem(Identifier identifier, Supplier<Block> supplier, CreativeModeTabHolder... creativeModeTabHolders) {
		return registerBlockWithBlockItem(identifier, supplier, BlockItemExtension::new, creativeModeTabHolders);
	}

	@MappedMethod
	public BlockRegistryObject registerBlockWithBlockItem(Identifier identifier, Supplier<Block> supplier, BiFunction<Block, ItemSettings, BlockItemExtension> function, CreativeModeTabHolder... creativeModeTabHolders) {
		if (!BLOCK_REGISTERS.containsKey(identifier.getNamespace())) {
			BLOCK_REGISTERS.put(identifier.getNamespace(), DeferredRegister.create(BuiltInRegistries.BLOCK, identifier.getNamespace()));
		}

		if (!ITEM_REGISTERS.containsKey(identifier.getNamespace())) {
			ITEM_REGISTERS.put(identifier.getNamespace(), DeferredRegister.create(BuiltInRegistries.ITEM, identifier.getNamespace()));
		}

		BLOCK_REGISTERS.get(identifier.getNamespace()).register(identifier.getPath(), () -> supplier.get().data);
		final BlockRegistryObject blockRegistryObject = new BlockRegistryObject(identifier);
		ITEM_REGISTERS.get(identifier.getNamespace()).register(identifier.getPath(), () -> function.apply(blockRegistryObject.get(), new ItemSettings()));
		for (final CreativeModeTabHolder creativeModeTabHolder : creativeModeTabHolders) {
			creativeModeTabHolder.itemSuppliers.add(new ItemRegistryObject(identifier)::get);
		}
		return blockRegistryObject;
	}

	@MappedMethod
	public ItemRegistryObject registerItem(Identifier identifier, Function<ItemSettings, Item> function, CreativeModeTabHolder... creativeModeTabHolders) {
		if (!ITEM_REGISTERS.containsKey(identifier.getNamespace())) {
			ITEM_REGISTERS.put(identifier.getNamespace(), DeferredRegister.create(BuiltInRegistries.ITEM, identifier.getNamespace()));
		}

		ITEM_REGISTERS.get(identifier.getNamespace()).register(identifier.getPath(), () -> function.apply(new ItemSettings()).data);
		final ItemRegistryObject itemRegistryObject = new ItemRegistryObject(identifier);
		for (final CreativeModeTabHolder creativeModeTabHolder : creativeModeTabHolders) {
			creativeModeTabHolder.itemSuppliers.add(itemRegistryObject::get);
		}
		return itemRegistryObject;
	}

	@MappedMethod
	public <T extends BlockEntityExtension> BlockEntityTypeRegistryObject<T> registerBlockEntityType(Identifier identifier, BiFunction<BlockPos, BlockState, T> function, Supplier<Block>... blockSuppliers) {
		if (!BLOCK_ENTITY_TYPE_REGISTERS.containsKey(identifier.getNamespace())) {
			BLOCK_ENTITY_TYPE_REGISTERS.put(identifier.getNamespace(), DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, identifier.getNamespace()));
		}

		BLOCK_ENTITY_TYPE_REGISTERS.get(identifier.getNamespace()).register(identifier.getPath(), () -> BlockEntityType.Builder.of((pos, state) -> function.apply(new BlockPos(pos), new BlockState(state)), HolderBase.convertArray(blockSuppliers, net.minecraft.world.level.block.Block[]::new)).build(null));
		return new BlockEntityTypeRegistryObject<>(identifier);
	}

	@MappedMethod
	public <T extends EntityExtension> EntityTypeRegistryObject<T> registerEntityType(Identifier identifier, BiFunction<EntityType<?>, World, T> function, float width, float height) {
		if (!ENTITY_TYPE_REGISTERS.containsKey(identifier.getNamespace())) {
			ENTITY_TYPE_REGISTERS.put(identifier.getNamespace(), DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, identifier.getNamespace()));
		}

		ENTITY_TYPE_REGISTERS.get(identifier.getNamespace()).register(identifier.getPath(), () -> net.minecraft.world.entity.EntityType.Builder.of(getEntityFactory(function), MobCategory.MISC).sized(width, height).build(identifier.toString()));
		return new EntityTypeRegistryObject<>(identifier);
	}

	private <T extends EntityExtension> net.minecraft.world.entity.EntityType.EntityFactory<T> getEntityFactory(BiFunction<EntityType<?>, World, T> function) {
		return (entityType, world) -> function.apply(new EntityType<>(entityType), new World(world));
	}

	@MappedMethod
	public ParticleTypeRegistryObject registerParticleType(Identifier identifier) {
		return registerParticleType(identifier, false);
	}

	@MappedMethod
	public ParticleTypeRegistryObject registerParticleType(Identifier identifier, boolean alwaysSpawn) {
		if (!PARTICLE_TYPE_REGISTERS.containsKey(identifier.getNamespace())) {
			PARTICLE_TYPE_REGISTERS.put(identifier.getNamespace(), DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, identifier.getNamespace()));
		}

		PARTICLE_TYPE_REGISTERS.get(identifier.getNamespace()).register(identifier.getPath(), () -> new SimpleParticleType(alwaysSpawn));
		return new ParticleTypeRegistryObject(identifier);
	}

	@MappedMethod
	public CreativeModeTabHolder createCreativeModeTabHolder(Identifier identifier, Supplier<ItemStack> iconSupplier) {
		if (!CREATIVE_TAB_REGISTERS.containsKey(identifier.getNamespace())) {
			CREATIVE_TAB_REGISTERS.put(identifier.getNamespace(), DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, identifier.getNamespace()));
		}

		final CreativeModeTabHolder creativeModeTabHolder = new CreativeModeTabHolder(identifier.data, iconSupplier);
		CREATIVE_TAB_REGISTERS.get(identifier.getNamespace()).register(identifier.getPath(), () -> CreativeModeTab.builder()
				.title(Component.translatable(String.format("itemGroup.%s.%s", creativeModeTabHolder.identifier.getNamespace(), creativeModeTabHolder.identifier.getPath())))
				.icon(() -> creativeModeTabHolder.iconSupplier.get().data.copyWithCount(1))
				.displayItems((params, output) -> creativeModeTabHolder.itemSuppliers.forEach(itemSupplier -> {
					output.accept(itemSupplier.get().data);
				}))
				.build()
		);
		return creativeModeTabHolder;
	}

	@MappedMethod
	public SoundEventRegistryObject registerSoundEvent(Identifier identifier) {
		if (!SOUND_EVENT_REGISTERS.containsKey(identifier.getNamespace())) {
			SOUND_EVENT_REGISTERS.put(identifier.getNamespace(), DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, identifier.getNamespace()));
		}

		SOUND_EVENT_REGISTERS.get(identifier.getNamespace()).register(identifier.getPath(), () -> new org.mtr.mapping.holder.SoundEvent(SoundEvent.createVariableRangeEvent(identifier.data)).data);
		return new SoundEventRegistryObject(identifier);
	}

	@MappedMethod
	public void registerCommand(String command, Consumer<CommandBuilder<?>> buildCommand, String... redirects) {
		mainEventBus.commands.add(dispatcher -> {
			final CommandBuilder<LiteralArgumentBuilder<CommandSourceStack>> commandBuilder = new CommandBuilder<>(Commands.literal(command));
			buildCommand.accept(commandBuilder);
			final LiteralCommandNode<CommandSourceStack> literalCommandNode = dispatcher.register(commandBuilder.argumentBuilder);
			for (final String redirect : redirects) {
				dispatcher.register(Commands.literal(redirect).redirect(literalCommandNode));
			}
		});
	}

	@MappedMethod
	public void setupPackets(Identifier identifier) {
		MainEventBus.packetIdentifier = identifier.getNamespace();
	}

	@MappedMethod
	public <T extends PacketHandler> void registerPacket(Class<T> classObject, Function<PacketBufferReceiver, T> getInstance) {
		packets.put(classObject.getName(), getInstance);
	}

	@MappedMethod
	public <T extends PacketHandler> void sendPacketToClient(ServerPlayerEntity serverPlayerEntity, T data) {
			final PacketBufferSender packetBufferSender = new PacketBufferSender(Unpooled::buffer);
			packetBufferSender.writeString(data.getClass().getName());
			data.write(packetBufferSender);
			packetBufferSender.send(byteBuf -> PacketDistributor.PLAYER.with(serverPlayerEntity.data).send(new PacketObject(new FriendlyByteBuf(byteBuf))), serverPlayerEntity.getServerMapped()::execute);
	}
}
