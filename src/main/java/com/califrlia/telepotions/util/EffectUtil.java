package com.califrlia.telepotions.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class EffectUtil
{
    public static void playSoundAtPlayer(World world, LivingEntity player)
    {
        //There are other methods on <World> for this, but they seem to ignore the pitch argument.
        world.playSound(null, player.position().x, player.position().y, player.position().z,
                SoundEvents.END_PORTAL_SPAWN, SoundCategory.PLAYERS,
                0.5F, world.getRandom().nextFloat() * 0.1F + 0.9F); //For random pitch.
    }
}
