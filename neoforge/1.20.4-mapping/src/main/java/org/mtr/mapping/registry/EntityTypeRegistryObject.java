package org.mtr.mapping.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.EntityType;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.EntityExtension;
import org.mtr.mapping.tool.RegistryObject;

import java.util.function.Consumer;

public final class EntityTypeRegistryObject<T extends EntityExtension> extends RegistryObject<EntityType<T>> {

	private final DeferredRegister<net.minecraft.world.entity.EntityType<?>> registryObject;

	EntityTypeRegistryObject(Identifier identifier) {
		registryObject = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, identifier.data.toString());
	}

	@MappedMethod
	@Override
	public EntityType<T> get() {
		return new EntityType<T>((net.minecraft.world.entity.EntityType<T>) registryObject.getRegistry().get().get(registryObject.getRegistryName()));
	}

	@MappedMethod
	@Override
	public boolean isPresent() {
		return true;
	}

	@MappedMethod
	@Override
	public void ifPresent(Consumer<EntityType<T>> consumer) {
		//registryObject.ifPresent(data -> consumer.accept(new EntityType<>(data)));
	}
}
