package com.solegendary.reignofnether;

import com.solegendary.reignofnether.guiscreen.TopdownGui;
import com.solegendary.reignofnether.registrars.ContainerRegistrar;
import com.solegendary.reignofnether.registrars.EntityRegistrar;
import com.solegendary.reignofnether.registrars.PacketHandler;
import com.solegendary.reignofnether.unit.units.modelling.*;
import com.solegendary.reignofnether.unit.units.monsters.*;
import com.solegendary.reignofnether.unit.units.piglins.*;
import com.solegendary.reignofnether.unit.units.villagers.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.*;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = ReignOfNether.MOD_ID, bus = Bus.MOD)
public class CommonModEvents {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::init);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers evt) {
        evt.registerEntityRenderer(EntityRegistrar.ZOMBIE_UNIT.get(), ZombieRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.SKELETON_UNIT.get(), SkeletonRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.HUSK_UNIT.get(), HuskRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.STRAY_UNIT.get(), StrayRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.CREEPER_UNIT.get(), CreeperRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.SPIDER_UNIT.get(), SpiderRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.POISON_SPIDER_UNIT.get(), CaveSpiderRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.VILLAGER_UNIT.get(), VillagerUnitRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.ZOMBIE_VILLAGER_UNIT.get(), ZombieVillagerUnitRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.PILLAGER_UNIT.get(), PillagerUnitRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.VINDICATOR_UNIT.get(), VindicatorUnitRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.IRON_GOLEM_UNIT.get(), IronGolemRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.WITCH_UNIT.get(), WitchRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.EVOKER_UNIT.get(), EvokerUnitRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.ENDERMAN_UNIT.get(), EndermanRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.WARDEN_UNIT.get(), WardenRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.RAVAGER_UNIT.get(), RavagerRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.SILVERFISH_UNIT.get(), SilverfishRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.PIGLIN_GRUNT_UNIT.get(), PiglinUnitRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.PIGLIN_BRUTE_UNIT.get(), PiglinUnitRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.PIGLIN_HEADHUNTER_UNIT.get(), PiglinUnitRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.HOGLIN_UNIT.get(), HoglinRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.BLAZE_UNIT.get(), BlazeRenderer::new);
        evt.registerEntityRenderer(EntityRegistrar.WITHER_SKELETON.get(), WitherSkeletonRenderer::new);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent evt) {
        evt.put(EntityRegistrar.ZOMBIE_UNIT.get(), ZombieUnit.createAttributes().build());
        evt.put(EntityRegistrar.SKELETON_UNIT.get(), SkeletonUnit.createAttributes().build());
        evt.put(EntityRegistrar.HUSK_UNIT.get(), HuskUnit.createAttributes().build());
        evt.put(EntityRegistrar.STRAY_UNIT.get(), StrayUnit.createAttributes().build());
        evt.put(EntityRegistrar.CREEPER_UNIT.get(), CreeperUnit.createAttributes().build());
        evt.put(EntityRegistrar.SPIDER_UNIT.get(), SpiderUnit.createAttributes().build());
        evt.put(EntityRegistrar.POISON_SPIDER_UNIT.get(), PoisonSpiderUnit.createAttributes().build());
        evt.put(EntityRegistrar.VILLAGER_UNIT.get(), VillagerUnit.createAttributes().build());
        evt.put(EntityRegistrar.ZOMBIE_VILLAGER_UNIT.get(), VillagerUnit.createAttributes().build());
        evt.put(EntityRegistrar.PILLAGER_UNIT.get(), PillagerUnit.createAttributes().build());
        evt.put(EntityRegistrar.VINDICATOR_UNIT.get(), VindicatorUnit.createAttributes().build());
        evt.put(EntityRegistrar.IRON_GOLEM_UNIT.get(), IronGolemUnit.createAttributes().build());
        evt.put(EntityRegistrar.WITCH_UNIT.get(), WitchUnit.createAttributes().build());
        evt.put(EntityRegistrar.EVOKER_UNIT.get(), EvokerUnit.createAttributes().build());
        evt.put(EntityRegistrar.ENDERMAN_UNIT.get(), EndermanUnit.createAttributes().build());
        evt.put(EntityRegistrar.WARDEN_UNIT.get(), WardenUnit.createAttributes().build());
        evt.put(EntityRegistrar.RAVAGER_UNIT.get(), RavagerUnit.createAttributes().build());
        evt.put(EntityRegistrar.SILVERFISH_UNIT.get(), SilverfishUnit.createAttributes().build());
        evt.put(EntityRegistrar.PIGLIN_GRUNT_UNIT.get(), PiglinGruntUnit.createAttributes().build());
        evt.put(EntityRegistrar.PIGLIN_HEADHUNTER_UNIT.get(), PiglinHeadhunterUnit.createAttributes().build());
        evt.put(EntityRegistrar.PIGLIN_BRUTE_UNIT.get(), PiglinBruteUnit.createAttributes().build());
        evt.put(EntityRegistrar.HOGLIN_UNIT.get(), HoglinUnit.createAttributes().build());
        evt.put(EntityRegistrar.BLAZE_UNIT.get(), BlazeUnit.createAttributes().build());
        evt.put(EntityRegistrar.WITHER_SKELETON.get(), WitherSkeletonUnit.createAttributes().build());
    }

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent evt) {
        MenuScreens.register(ContainerRegistrar.TOPDOWNGUI_CONTAINER.get(), TopdownGui::new);
    }
}

