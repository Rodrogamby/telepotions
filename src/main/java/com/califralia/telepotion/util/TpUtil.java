package com.califralia.telepotion.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;

public class TpUtil
{

    public static void teleport(EntityLivingBase entity, double x, double y, double z)
    {
        entity.dismountRidingEntity();

        if (entity instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP)entity).connection.setPlayerLocation(x, y, z, entity.rotationYaw, entity.rotationPitch);
        }
        else
        {
            entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
        }
    }

    public static boolean bedTeleport(EntityLivingBase entity, double x, double y, double z, EnumFacing bedRotation)
    {
        switch (bedRotation)
        {
            //Facing north
            case NORTH: return calcBedAndTp(entity, x, y, z, 1, 2, 3,1.5F, -.5F);
            //Facing east
            case EAST: return calcBedAndTp(entity, x, y, z, -1, 3, 2,-1.5F, 1.5F);
            //Facing south
            case SOUTH: return calcBedAndTp(entity, x, y, z, -1, 2, 3,-.5F, 1.5F);
            //Facing west
            case WEST: return calcBedAndTp(entity, x, y, z, 1, 3, 2,2.5F, -.5F);
            //
            default: return false;
        }
    }

    private static boolean calcBedAndTp(EntityLivingBase entity, double x, double y, double z,
                                          int sign, int xIte, int zIte, float xOff, float zOff)
    {
        entity.dismountRidingEntity();
        double aX = x + xOff;
        double aZ = z + zOff;
        for(int i = 0; i < 2; i++)
        {
            for(int g = 0; g < zIte; g++ )
            {
                aZ += sign;
                if(entity.attemptTeleport(aX, y, aZ))
                {
                    return true;
                }
            }
            for(int g = 0; g < xIte; g++ )
            {
                aX -= sign;
                if(entity.attemptTeleport(aX, y, aZ))
                {
                    return true;
                }
            }
            sign = sign * -1;
        }
        return false;
    }
}