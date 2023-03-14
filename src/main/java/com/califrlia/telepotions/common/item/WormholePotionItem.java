package com.califrlia.telepotions.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

import static com.califrlia.telepotions.util.EffectUtil.playSoundAtPlayer;

public class WormholePotionItem extends Item
{
    private static final Food wormholePotion = new Food.Builder().nutrition(0).alwaysEat().build();

    public WormholePotionItem(String name)
    {
        super(new Properties().tab(ItemGroup.TAB_FOOD).stacksTo(1).food(wormholePotion).rarity(Rarity.EPIC));
        this.setRegistryName(name);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack item = player.getItemInHand(hand);
        String rawDisplayName = item.getDisplayName().getString();
        String targetDisplayName = rawDisplayName.substring(1,rawDisplayName.length()-1); //Removing those []
        if(item.hasCustomHoverName() && !targetDisplayName.equals(player.getDisplayName().getString()))
        {
            if(!world.isClientSide)
            {
                //PlayerList is no-null asserted since this is always triggered by a player...
                if(world.getServer().getPlayerList().getPlayerByName(targetDisplayName)!=null)
                {
                    player.startUsingItem(hand);
                    return ActionResult.success(item);
                }
                //There could be an error message return here but I think it would be redundant.
            }
        }
        return ActionResult.fail(item);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity)
    {
        if(entity instanceof PlayerEntity)
        {
            if (!world.isClientSide)
            {
                String rawDisplayName = stack.getDisplayName().getString();
                String targetDisplayName = rawDisplayName.substring(1,rawDisplayName.length()-1); //Removing those []
                @Nullable ServerPlayerEntity targetPlayer = world.getServer().getPlayerList().getPlayerByName(targetDisplayName);
                if (targetPlayer != null)
                {
                    try
                    {
                        entity.stopRiding();
                        entity.moveTo(world.getPlayerByUUID(targetPlayer.getUUID()).position());
                    } catch(NullPointerException e)
                    {
                        entity.sendMessage(new TranslationTextComponent("item.telepotions.wormhole_potion.error"),entity.getUUID());
                        return stack;
                    }
                    playSoundAtPlayer(world, entity);
                    return new ItemStack(Items.GLASS_BOTTLE,1);
                }
                entity.sendMessage(new TranslationTextComponent("item.telepotions.wormhole_potion.error"),entity.getUUID());
            }
        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack itemStack)
    {
        return 32;
    }

    @Override
    public UseAction getUseAnimation(ItemStack item)
    {
        return UseAction.DRINK;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack item)
    {
        return item.hasCustomHoverName();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.appendHoverText(itemStack, world, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.telepotions.wormhole_potion.lore"));
    }
}
