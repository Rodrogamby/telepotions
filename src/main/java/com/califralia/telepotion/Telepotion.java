package com.califralia.telepotion;

import com.califralia.telepotion.items.RecallPotion;
import com.califralia.telepotion.items.WormholePotion;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Telepotion.MOD_ID,
        name = Telepotion.MOD_NAME,
        version = Telepotion.VERSION
)
public class Telepotion
{

    public static final String MOD_ID = "telepotion";
    public static final String MOD_NAME = "Telepotion";
    public static final String VERSION = "1.0-SNAPSHOT";

    @Mod.Instance(MOD_ID)
    public static Telepotion INSTANCE;

    public static Logger logger;
    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.log(Level.INFO, "FMLPreInit event");
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.log(Level.INFO, "FMLInit event");
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
        logger.log(Level.INFO, "FMLPostInit event");
    }

    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Blocks
    {

    }

    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Items
    {
          public static final RecallPotion recallPotion = null;
          public static final WormholePotion wormholePotion = null;
    }

}
