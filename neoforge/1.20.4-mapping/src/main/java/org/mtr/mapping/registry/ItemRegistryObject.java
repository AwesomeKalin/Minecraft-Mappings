package org.mtr.mapping.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.tool.RegistryObject;

import java.util.function.Consumer;

public final class ItemRegistryObject extends RegistryObject<Item> {

	private final DeferredRegister<net.minecraft.world.item.Item> registryObject;

	ItemRegistryObject(Identifier identifier) {
		registryObject = DeferredRegister.create(BuiltInRegistries.ITEM, identifier.data.toString());
	}

	@MappedMethod
	@Override
	public Item get() {
		return new Item(registryObject.getRegistry().get().get(registryObject.getRegistryName()));
	}

	@MappedMethod
	@Override
	public boolean isPresent() {
		return true;
	}

	@MappedMethod
	@Override
	public void ifPresent(Consumer<Item> consumer) {
		//registryObject.ifPresent(item -> consumer.accept(new Item(item)));
	}
}
