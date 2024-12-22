package org.mtr.mapping.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.holder.WorldChunk;
import org.mtr.mapping.networking.ClientPayloadHandler;
import org.mtr.mapping.networking.PacketObject;
import org.mtr.mapping.networking.ServerPayloadHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.mtr.mapping.registry.Registry.PROTOCOL_VERSION;

public final class MainEventBus {

	Consumer<MinecraftServer> serverStartingConsumer = minecraftServer -> {
	};
	Consumer<MinecraftServer> serverStartedConsumer = minecraftServer -> {
	};
	Consumer<MinecraftServer> serverStoppingConsumer = minecraftServer -> {
	};
	Consumer<MinecraftServer> serverStoppedConsumer = minecraftServer -> {
	};
	Runnable startServerTickRunnable = () -> {
	};
	Runnable endServerTickRunnable = () -> {
	};
	Consumer<ServerWorld> startWorldTickRunnable = world -> {
	};
	Consumer<ServerWorld> endWorldTickRunnable = world -> {
	};
	BiConsumer<MinecraftServer, ServerPlayerEntity> playerJoinRunnable = (minecraftServer, serverPlayerEntity) -> {
	};
	BiConsumer<MinecraftServer, ServerPlayerEntity> playerDisconnectRunnable = (minecraftServer, serverPlayerEntity) -> {
	};
	BiConsumer<ServerWorld, WorldChunk> chunkLoadConsumer = (world, chunk) -> {
	};
	BiConsumer<ServerWorld, WorldChunk> chunkUnloadConsumer = (world, chunk) -> {
	};
	final List<Consumer<CommandDispatcher<CommandSourceStack>>> commands = new ArrayList<>();
	public static String packetIdentifier;

	@SubscribeEvent
	public void serverStarting(ServerStartingEvent event) {
		serverStartingConsumer.accept(new MinecraftServer(event.getServer()));
	}

	@SubscribeEvent
	public void serverStarted(ServerStartedEvent event) {
		serverStartedConsumer.accept(new MinecraftServer(event.getServer()));
	}

	@SubscribeEvent
	public void serverStopping(ServerStoppingEvent event) {
		serverStoppingConsumer.accept(new MinecraftServer(event.getServer()));
	}

	@SubscribeEvent
	public void serverStopped(ServerStoppedEvent event) {
		serverStoppedConsumer.accept(new MinecraftServer(event.getServer()));
	}

	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent event) {
		switch (event.phase) {
			case START -> startServerTickRunnable.run();
			case END -> endServerTickRunnable.run();
		}
	}

	@SubscribeEvent
	public void worldTick(TickEvent.LevelTickEvent event) {
		if (event.side == LogicalSide.SERVER && event.level instanceof ServerLevel) {
			switch (event.phase) {
				case START -> startWorldTickRunnable.accept(new ServerWorld((ServerLevel) event.level));
				case END -> endWorldTickRunnable.accept(new ServerWorld((ServerLevel) event.level));
			}
		}
	}

	@SubscribeEvent
	public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		final Player playerEntity = event.getEntity();
		if (playerEntity instanceof ServerPlayer serverPlayerEntity) {
			playerJoinRunnable.accept(new MinecraftServer(serverPlayerEntity.server), new ServerPlayerEntity(serverPlayerEntity));
		}
	}

	@SubscribeEvent
	public void playerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
		final Player playerEntity = event.getEntity();
		if (playerEntity instanceof ServerPlayer serverPlayerEntity) {
			playerDisconnectRunnable.accept(new MinecraftServer(serverPlayerEntity.server), new ServerPlayerEntity(serverPlayerEntity));
		}
	}

	@SubscribeEvent
	public void chunkLoad(ChunkEvent.Load event) {
		if (event.getLevel() instanceof ServerLevel && event.getChunk() instanceof LevelChunk) {
			chunkLoadConsumer.accept(new ServerWorld((ServerLevel) event.getLevel()), new WorldChunk((LevelChunk) event.getChunk()));
		}
	}

	@SubscribeEvent
	public void chunkUnload(ChunkEvent.Load event) {
		if (event.getLevel() instanceof ServerLevel && event.getChunk() instanceof LevelChunk) {
			chunkUnloadConsumer.accept(new ServerWorld((ServerLevel) event.getLevel()), new WorldChunk((LevelChunk) event.getChunk()));
		}
	}

	@SubscribeEvent
	public void registerCommands(RegisterCommandsEvent event) {
		commands.forEach(consumer -> consumer.accept(event.getDispatcher()));
	}

	@SubscribeEvent
	public void registerPayloads(final RegisterPayloadHandlerEvent event) {
		final IPayloadRegistrar registrar = event.registrar(packetIdentifier).versioned(PROTOCOL_VERSION);
		registrar.play(new ResourceLocation(packetIdentifier, "packet"), PacketObject::new, handler -> handler
				.client(ClientPayloadHandler.getInstance()::handleData)
				.server(ServerPayloadHandler.getInstance()::handleData));
	}
}
