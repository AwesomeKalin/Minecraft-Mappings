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

	private final ResourceLocation identifier;

	ParticleTypeRegistryObject(Identifier identifier) {
		this.identifier = identifier.data;
	}

	@MappedMethod
	@Override
	public DefaultParticleType get() {
		return new DefaultParticleType((SimpleParticleType) BuiltInRegistries.PARTICLE_TYPE.get(identifier));
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
