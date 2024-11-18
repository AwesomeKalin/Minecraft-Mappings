package org.mtr.mapping.registry;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.DefaultParticleType;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.tool.RegistryObject;

import java.util.function.Consumer;

public final class ParticleTypeRegistryObject extends RegistryObject<DefaultParticleType> {

	final Identifier identifier;
	private final DeferredRegister<SimpleParticleType> registryObject;

	ParticleTypeRegistryObject(Identifier identifier) {
		this.identifier = identifier;
		registryObject = DeferredRegister.create((ResourceLocation) BuiltInRegistries.PARTICLE_TYPE, identifier.data.toString());
	}

	@MappedMethod
	@Override
	public DefaultParticleType get() {
		return new DefaultParticleType(registryObject.getRegistry().get().get(registryObject.getRegistryName()));
	}

	@MappedMethod
	@Override
	public boolean isPresent() {
		return true;
	}

	@MappedMethod
	@Override
	public void ifPresent(Consumer<DefaultParticleType> consumer) {
		//registryObject.ifPresent(data -> consumer.accept(new DefaultParticleType(data)));
	}
}
