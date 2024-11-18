package org.mtr.mapping.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.BlockEntityType;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.RegistryObject;

import java.util.function.Consumer;

public final class BlockEntityTypeRegistryObject<T extends BlockEntityExtension> extends RegistryObject<BlockEntityType<T>> {

	private final DeferredRegister<net.minecraft.world.level.block.entity.BlockEntityType<?>> registryObject;

	BlockEntityTypeRegistryObject(Identifier identifier) {
		registryObject = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, identifier.data.toString());
	}

	@MappedMethod
	@Override
	public BlockEntityType<T> get() {
		return new BlockEntityType<>((net.minecraft.world.level.block.entity.BlockEntityType<T>) registryObject.getRegistry().get().get(registryObject.getRegistryName()));
	}

	@MappedMethod
	@Override
	public boolean isPresent() {
		return true;
	}

	@MappedMethod
	@Override
	public void ifPresent(Consumer<BlockEntityType<T>> consumer) {
		//registryObject.ifPresent(data -> consumer.accept((BlockEntityType<T>) new BlockEntityType<>(data)));
	}
}
