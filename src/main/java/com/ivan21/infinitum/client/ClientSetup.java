package com.ivan21.infinitum.client;

import com.ivan21.infinitum.Infinitum;
import com.ivan21.infinitum.registry.ModEntities;
import com.ivan21.infinitum.registry.ModItems;
import com.ivan21.infinitum.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Infinitum.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.EXTREME_CRAFTING.get(), ExtremeCraftingScreen::new);

            // анимация натяжения лука
            ItemProperties.register(ModItems.INFINITY_BOW.get(), new ResourceLocation("pull"),
                    (stack, level, entity, seed) -> {
                        if (entity == null || entity.getUseItem() != stack) return 0f;
                        return (stack.getUseDuration() - entity.getUseItemRemainingTicks()) * 3f / 20f;
                    });
            ItemProperties.register(ModItems.INFINITY_BOW.get(), new ResourceLocation("pulling"),
                    (stack, level, entity, seed) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1f : 0f);
        });
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ENDEST_PEARL.get(), ThrownItemRenderer::new);
    }
}
