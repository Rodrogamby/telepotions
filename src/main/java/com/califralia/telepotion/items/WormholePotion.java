package com.califralia.telepotion.items;

import com.califralia.telepotion.Telepotion;
import com.califralia.telepotion.util.SoundUtil;
import com.califralia.telepotion.util.TpUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class WormholePotion extends ItemFood
{
    public WormholePotion(String name)
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
        if(!worldIn.isRemote)
        {
            if(itemStack.hasDisplayName())
            {
                String displayName = itemStack.getDisplayName();
                EntityPlayer target = worldIn.getPlayerEntityByName(displayName);
                if(target != null && playerIn.world.equals(target.world))
                {
                    playerIn.setActiveHand(handIn);
                    return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
                }
            }
        }
        return new ActionResult<>(EnumActionResult.FAIL, itemStack);
    }

    @Override @Deprecated
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayerMP && !worldIn.isRemote)
        {
            EntityPlayerMP entityPlayer = (EntityPlayerMP)entityLiving;
            final String targetName = stack.getDisplayName();
            @Nullable EntityPlayer target = worldIn.getPlayerEntityByName(targetName);
            if(target != null && entityLiving.world.equals(target.world))
            {
                TpUtil.teleport(entityLiving, target.posX, target.posY, target.posZ);
                SoundUtil.playSoundAtPlayer(
                        worldIn,
                        entityPlayer,
                        SoundEvents.BLOCK_END_PORTAL_SPAWN,
                        SoundCategory.PLAYERS);
            }
            else
            {
                entityLiving.sendMessage(new TextComponentTranslation("item.wormhole_potion.error"));
                return stack;
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack item)
    {
        return item.hasDisplayName();
    }

    @Override @Deprecated @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, @Nullable World world, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add("\u00A75" + I18n.translateToLocal("item.wormhole_potion.lore"));
    }

}
