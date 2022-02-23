package com.califralia.telepotion.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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

    public static boolean safeVerticalTp(EntityLivingBase entity, BlockPos tpPos) {
        if (entity.attemptTeleport(tpPos.getX()+.5F, tpPos.getY(), tpPos.getZ()+.5F))
        {
            return true;
        }
        else
        {
            World world = entity.getEntityWorld();
            final Vec3d pos = new Vec3d(tpPos.getX(), world.getActualHeight(), tpPos.getZ());
            final RayTraceResult rayTraceResult = world.rayTraceBlocks(
                    pos,
                    pos.subtract(0, world.getActualHeight(), 0),
                    false,
                    true,
                    false);

            if(rayTraceResult != null)
            {
                final BlockPos topBlock = rayTraceResult.getBlockPos();
                return entity.attemptTeleport(topBlock.getX()+.5F, topBlock.getY()+1, topBlock.getZ()+.5F);
            }
        }
        return false;
    }

    public static boolean tryBedTeleport(EntityLivingBase entity, double x, double y, double z, EnumFacing bedRotation)
    {
        switch (bedRotation)
        {
            case NORTH: return calcBedAndTp(entity, x, y, z, 1, 2, 3,1.5F, -.5F);

            case EAST: return calcBedAndTp(entity, x, y, z, -1, 3, 2,-1.5F, 1.5F);

            case SOUTH: return calcBedAndTp(entity, x, y, z, -1, 2, 3,-.5F, 1.5F);

            case WEST: return calcBedAndTp(entity, x, y, z, 1, 3, 2,2.5F, -.5F);

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