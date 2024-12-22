package org.mtr.mapping.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.EntityType;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.EntityExtension;
import org.mtr.mapping.tool.RegistryObject;

import java.util.function.Consumer;

public final class EntityTypeRegistryObject<T extends EntityExtension> extends RegistryObject<EntityType<T>> {

	private final ResourceLocation identifier;

	EntityTypeRegistryObject(Identifier identifier) {
		this.identifier = identifier.data;
	}

	@MappedMethod
	@Override
	public EntityType<T> get() {
		return new EntityType<T>((net.minecraft.world.entity.EntityType<T>) BuiltInRegistries.ENTITY_TYPE.get(identifier));
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
