package org.mtr.mapping.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModLoadingContext;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.tool.RegistryObject;

import java.util.function.Consumer;

public final class ItemRegistryObject extends RegistryObject<Item> {

	private final ResourceLocation identifier;

	ItemRegistryObject(Identifier identifier) {
		this.identifier = identifier.data;
	}

	@MappedMethod
	@Override
	public Item get() {
		return new Item(BuiltInRegistries.ITEM.get(identifier));
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
