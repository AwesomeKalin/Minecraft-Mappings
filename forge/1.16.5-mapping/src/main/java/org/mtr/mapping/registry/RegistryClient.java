package org.mtr.mapping.registry;

import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.tool.DummyClass;

import java.util.function.Function;

public final class RegistryClient extends DummyClass {

	@MappedMethod
	public static void init() {
		FMLJavaModLoadingContext.get().getModEventBus().register(ModEventBusClient.class);
	}

	@MappedMethod
	public static <T extends BlockEntityTypeRegistryObject<U>, U extends BlockEntityExtension> void registerBlockEntityRenderer(T blockEntityType, Function<BlockEntityRendererArgument, BlockEntityRenderer<U>> rendererInstance) {
		ModEventBusClient.CLIENT_OBJECTS_TO_REGISTER.add(() -> ClientRegistry.bindTileEntityRenderer(blockEntityType.get().data, dispatcher -> rendererInstance.apply(new BlockEntityRendererArgument(dispatcher))));
	}

	@MappedMethod
	public static void registerBlockRenderType(RenderLayer renderLayer, BlockRegistryObject block) {
		ModEventBusClient.CLIENT_OBJECTS_TO_REGISTER.add(() -> RenderTypeLookup.setRenderLayer(block.get().data, renderLayer.data));
	}

	@MappedMethod
	public static KeyBinding registerKeyBinding(String translationKey, int key, String categoryKey) {
		final net.minecraft.client.settings.KeyBinding keyBinding = new net.minecraft.client.settings.KeyBinding(translationKey, InputMappings.Type.KEYSYM, key, categoryKey);
		ModEventBusClient.CLIENT_OBJECTS_TO_REGISTER.add(() -> ClientRegistry.registerKeyBinding(keyBinding));
		return new KeyBinding(keyBinding);
	}

	@MappedMethod
	public static void registerBlockColors(BlockColorProvider blockColorProvider, BlockRegistryObject... blocks) {
		final net.minecraft.block.Block[] newBlocks = new net.minecraft.block.Block[blocks.length];
		for (int i = 0; i < blocks.length; i++) {
			newBlocks[i] = blocks[i].get().data;
		}
		ModEventBusClient.BLOCK_COLORS.add(event -> event.getBlockColors().register((blockState, blockRenderView, blockPos, tintIndex) -> blockColorProvider.getColor2(new BlockState(blockState), blockRenderView == null ? null : new BlockRenderView(blockRenderView), blockPos == null ? null : new BlockPos(blockPos), tintIndex), newBlocks));
	}

	@MappedMethod
	public static void registerItemColors(ItemColorProvider itemColorProvider, ItemRegistryObject... items) {
		final net.minecraft.item.Item[] newItems = new net.minecraft.item.Item[items.length];
		for (int i = 0; i < items.length; i++) {
			newItems[i] = items[i].get().data;
		}
		ModEventBusClient.ITEM_COLORS.add(event -> event.getItemColors().register(((itemStack, tintIndex) -> itemColorProvider.getColor2(new ItemStack(itemStack), tintIndex)), newItems));
	}

	@MappedMethod
	public static void setupPackets(Identifier identifier) {
	}

	@MappedMethod
	public static <T extends PacketHandler> void sendPacketToServer(T data) {
		if (Registry.simpleChannel != null) {
			Registry.simpleChannel.sendToServer(data);
		}
	}
}
