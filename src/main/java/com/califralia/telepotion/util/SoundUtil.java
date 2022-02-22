package com.califralia.telepotion.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class SoundUtil
{
    public static void playSoundAtPlayer(World world, EntityPlayer player, SoundEvent sound, SoundCategory category)
    {
        world.playSound(null, player.posX, player.posY, player.posZ, sound, category,
                0.5F, world.rand.nextFloat() * 0.1F + 0.9F //Random pitch
        );
    }
}
