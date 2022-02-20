package com.califralia.telepotion.items;

import com.califralia.telepotion.Telepotion;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

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
        if(!worldIn.isRemote)
        {
            if(bedLoc != null && playerIn.canEat(true))
            {
                playerIn.setActiveHand(handIn);
                playerIn.sendMessage(new TextComponentString("Your bed: " + bedLoc));
                return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
            }
        }
        return new ActionResult<>(EnumActionResult.FAIL, itemStack);
    }

    @Override @Deprecated
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            worldIn.playSound(null, entityplayer.posX, entityplayer.posY,
                    entityplayer.posZ, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS,
                    0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F
            );

            if(!worldIn.isRemote)
            {
                @Nullable BlockPos bedLoc = getBedLocation(entityplayer);
                if(bedLoc != null)
                {
                    entityLiving.setPositionAndUpdate(bedLoc.getX(), bedLoc.getY(), bedLoc.getZ());
                }
                else
                {
                    entityplayer.sendMessage(new TextComponentString(I18n.translateToLocalFormatted("item.recall_potion.error")));
                }
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override @Deprecated
    public void addInformation(ItemStack item, @Nullable World world, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add("\u00A75" + I18n.translateToLocal("item.recall_potion.lore"));
    }

    private static BlockPos getBedLocation(EntityPlayer player)
    {
        final int dim = player.dimension;
        return player.getBedLocation(dim);
    }
}
