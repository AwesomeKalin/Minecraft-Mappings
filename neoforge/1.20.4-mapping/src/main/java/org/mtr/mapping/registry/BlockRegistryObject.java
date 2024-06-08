package org.mtr.mapping.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.Block;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.tool.RegistryObject;

import java.util.function.Consumer;

public final class BlockRegistryObject extends RegistryObject<Block> {

	private final DeferredRegister<net.minecraft.world.level.block.Block> registryObject;

	BlockRegistryObject(Identifier identifier) {
		registryObject = DeferredRegister.create(BuiltInRegistries.BLOCK, identifier.data.toString());
	}

	@MappedMethod
	@Override
	public Block get() {
		return new Block((net.minecraft.world.level.block.Block) registryObject.getEntries());
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
