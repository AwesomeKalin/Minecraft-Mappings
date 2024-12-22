package org.mtr.mapping.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.SoundEvent;
import org.mtr.mapping.tool.RegistryObject;

import java.util.function.Consumer;

public final class SoundEventRegistryObject extends RegistryObject<SoundEvent> {

	private final ResourceLocation identifier;

	SoundEventRegistryObject(Identifier identifier) {
		this.identifier = identifier.data;
	}

	@MappedMethod
	@Override
	public SoundEvent get() {
		return new SoundEvent(BuiltInRegistries.SOUND_EVENT.get(identifier));
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
