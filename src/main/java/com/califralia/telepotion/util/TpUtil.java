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
            case NORTH: return calcBedAndTpNS(entity, x, y, z, 1, 1.5F, -.5F);
            //Facing east
            case EAST: return calcBedAndTpEW(entity, x, y, z, -1, 1.5F, 1.5F);
            //Facing south
            case SOUTH: return calcBedAndTpNS(entity, x, y, z, -1, -.5F, 1.5F);
            //Facing west
            case WEST: return calcBedAndTpEW(entity, x, y, z, 1, -.5F, -.5F);
            //
            default: return false;
        }
    }

    //If east, sign should be -, if west -> +
    private static boolean calcBedAndTpNS(EntityLivingBase entity, double x, double y, double z,
                                          int sign, float xOff, float zOff)
    {
        entity.dismountRidingEntity();
        double aX = x + xOff;
        double aZ = z + zOff;
        for(int i = 0; i < 2; i++)
        {
            for(int g = 0; g < 3; g++ )
            {
                aZ += sign;
                if(entity.attemptTeleport(aX, y, aZ))
                {
                    return true;
                }
            }
            for(int g = 0; g < 2; g++ )
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

    //If north, sign should be +, if south -> -
    private static boolean calcBedAndTpEW(EntityLivingBase entity, double x, double y, double z,
                                          int sign, float xOff, float zOff)
    {
        entity.dismountRidingEntity();
        double aX = x + xOff;
        double aZ = z + zOff;
        for(int i = 0; i < 2; i++)
        {
            for(int g = 0; g < 3; g++ )
            {
                aX += sign;
                if(entity.attemptTeleport(aX, y, aZ))
                {
                    return true;
                }
            }
            for(int g = 0; g < 2; g++ )
            {
                aZ += sign;
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