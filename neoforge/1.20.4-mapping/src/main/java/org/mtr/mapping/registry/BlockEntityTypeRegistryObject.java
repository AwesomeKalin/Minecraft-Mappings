package org.mtr.mapping.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.BlockEntityType;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.RegistryObject;

import java.util.function.Consumer;

public final class BlockEntityTypeRegistryObject<T extends BlockEntityExtension> extends RegistryObject<BlockEntityType<T>> {

	private final ResourceLocation identifier;

	BlockEntityTypeRegistryObject(Identifier identifier) {
		this.identifier = identifier.data;
	}

	@MappedMethod
	@Override
	public BlockEntityType<T> get() {
		return new BlockEntityType<>((net.minecraft.world.level.block.entity.BlockEntityType<T>) BuiltInRegistries.BLOCK_ENTITY_TYPE.get(identifier));
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
