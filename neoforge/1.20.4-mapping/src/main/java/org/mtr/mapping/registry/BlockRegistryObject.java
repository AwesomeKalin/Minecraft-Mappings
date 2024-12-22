package org.mtr.mapping.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.Block;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.tool.RegistryObject;

import java.util.Objects;
import java.util.function.Consumer;

public final class BlockRegistryObject extends RegistryObject<Block> {

	private final ResourceLocation identifier;

	BlockRegistryObject(Identifier identifier) {
		this.identifier = identifier.data;
	}

	@MappedMethod
	@Override
	public Block get() {
		return new Block(BuiltInRegistries.BLOCK.get(identifier));
	}

	@MappedMethod
	@Override
	public boolean isPresent() {
		return true;
	}

	@MappedMethod
	@Override
	public void ifPresent(Consumer<Block> consumer) {
		//registryObject.ifPresent(block -> consumer.accept(new Block(block)));
	}
}
