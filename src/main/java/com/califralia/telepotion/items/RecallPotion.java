package com.califralia.telepotion.items;

import com.califralia.telepotion.Telepotion;
import com.califralia.telepotion.util.EffectUtil;
import com.califralia.telepotion.util.TpUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class RecallPotion extends ItemFood
{
    public RecallPotion(String name)
    {
        super(0, 0, false);
        this.setRegistryName(Telepotion.MOD_ID, name);
        this.setTranslationKey(name);
        this.setMaxStackSize(1);
        this.setAlwaysEdible();
        this.setCreativeTab(CreativeTabs.FOOD);
    }

    @Override
    public int getHealAmount(ItemStack itemStack)
    {
        return 0;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack)
    {
        return EnumAction.DRINK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack)
    {
        return 32;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        @Nullable BlockPos bedLoc = getBedLocation(playerIn);
        if(!worldIn.isRemote && bedLoc != null)
        {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }
        return new ActionResult<>(EnumActionResult.FAIL, itemStack);
    }

    @Override @Deprecated
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer && !worldIn.isRemote)
        {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
            @Nullable BlockPos bedLoc = getBedLocation(entityplayer);
            if (bedLoc != null)
            {
                final IBlockState bedBlockState = worldIn.getBlockState(bedLoc);
                final Block bedBlock = bedBlockState.getBlock();
                if(bedBlock.isBed(bedBlockState, worldIn, bedLoc, null)) {
                    final EnumFacing rotation = bedBlock.getBedDirection(bedBlockState, worldIn, bedLoc);
                    if (TpUtil.tryBedTeleport(entityLiving, bedLoc.getX(), bedLoc.getY(), bedLoc.getZ(), rotation))
                    {
                        EffectUtil.playSoundAtPlayer(
                                worldIn,
                                entityplayer,
                                SoundEvents.BLOCK_END_PORTAL_SPAWN,
                                SoundCategory.PLAYERS);
                        super.onItemUseFinish(stack, worldIn, entityLiving);
                        return new ItemStack(Items.GLASS_BOTTLE, 1);
                    }
                }
            }
            entityplayer.sendMessage(new TextComponentTranslation("tile.bed.notValid"));
        }
        return stack;
    }

    @Override @Deprecated @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, @Nullable World world, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add("\u00A75" + I18n.translateToLocal("item.recall_potion.lore"));
    }

    @Override @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    private static BlockPos getBedLocation(EntityPlayer player)
    {
        final int dim = player.dimension;
        return player.getBedLocation(dim);
    }
}
