package com.califralia.telepotions;

import com.califralia.telepotions.items.RecallPotion;
import com.califralia.telepotions.items.TeleportationPotion;
import com.califralia.telepotions.items.WormholePotion;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Telepotions.MOD_ID,
        name = Telepotions.MOD_NAME,
        version = Telepotions.VERSION
)
public class Telepotions
{

    public static final String MOD_ID = "telepotions";
    public static final String MOD_NAME = "Telepotions";
    public static final String VERSION = "1.0-BETA-2";

    @Mod.Instance(MOD_ID)
    public static Telepotions INSTANCE;

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
          public static final TeleportationPotion teleportationPotion = null;
    }

}
