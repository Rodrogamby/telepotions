package com.califralia.telepotion.handlers;

import com.califralia.telepotion.items.RecallPotion;
import com.califralia.telepotion.items.TeleportationPotion;
import com.califralia.telepotion.items.WormholePotion;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ObjectRegistry
{

    private static RecallPotion recallPotion;
    private static WormholePotion wormholePotion;
    private static TeleportationPotion teleportationPotion;

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
                recallPotion = new RecallPotion("recall_potion"),
                wormholePotion = new WormholePotion("wormhole_potion"),
                teleportationPotion = new TeleportationPotion("teleportation_potion")
        );
    }

    @SubscribeEvent //Models are registered after items, so they will not be null on #registerModel()
    public static void onRegisterModels(ModelRegistryEvent event)
    {
        registerModel(recallPotion);
        registerModel(wormholePotion);
        registerModel(teleportationPotion);
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(
                item.getRegistryName(),
                "inventory"));
    }

    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
    {
        //event.getRegistry().register(new MySpecialBlock().setRegistryName(MOD_ID, "mySpecialBlock"));
    }
}
