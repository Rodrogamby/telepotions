package com.califralia.telepotions.items;

import com.califralia.telepotions.Telepotions;
import com.califralia.telepotions.util.EffectUtil;
import com.califralia.telepotions.util.TpUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class TeleportationPotion extends ItemFood
{
    public TeleportationPotion(String name)
    {
        super(0, 0, false);
        this.setRegistryName(Telepotions.MOD_ID, name);
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
        if(playerIn.dimension == 0)
        {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }
        return new ActionResult<>(EnumActionResult.FAIL, itemStack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if(entityLiving instanceof EntityPlayer && !worldIn.isRemote)
        {
            EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
            BlockPos spawnPoint = worldIn.getSpawnPoint();
            if(TpUtil.safeVerticalTp(entityLiving, spawnPoint))
            {
                EffectUtil.playSoundAtPlayer(
                        worldIn, entityPlayer,
                        SoundEvents.BLOCK_END_PORTAL_SPAWN,
                        SoundCategory.PLAYERS);
                super.onItemUseFinish(stack, worldIn, entityLiving);
                return new ItemStack(Items.GLASS_BOTTLE, 1);
            }
            else
            {
                entityPlayer.sendMessage(new TextComponentTranslation("item.teleportation_potion.error"));
            }
        }
        return stack;
    }

    @Override @Deprecated @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, @Nullable World world, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add("\u00A75" + I18n.translateToLocal("item.teleportation_potion.lore"));
    }

    @Override @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}