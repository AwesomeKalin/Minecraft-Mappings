package org.mtr.mapping.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.SoundEvent;
import org.mtr.mapping.tool.RegistryObject;

import java.util.function.Consumer;

public final class SoundEventRegistryObject extends RegistryObject<SoundEvent> {

	private final DeferredRegister<net.minecraft.sounds.SoundEvent> registryObject;

	SoundEventRegistryObject(Identifier identifier) {
		registryObject = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, identifier.data.toString());
	}

	@MappedMethod
	@Override
	public SoundEvent get() {
		return new SoundEvent(registryObject.getRegistry().get().get(registryObject.getRegistryName()));
	}

	@MappedMethod
	@Override
	public boolean isPresent() {
		return true;
	}

	@MappedMethod
	@Override
	public void ifPresent(Consumer<SoundEvent> consumer) {
		//registryObject.ifPresent(soundEvent -> consumer.accept(new SoundEvent(soundEvent)));
	}
}
