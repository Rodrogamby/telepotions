package com.califrlia.telepotions.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

import static com.califrlia.telepotions.util.EffectUtil.playSoundAtPlayer;

public class TeleportationPotionItem extends Item
{
    private static final Food teleportationPotion = new Food.Builder().alwaysEat().nutrition(0).build();

    public TeleportationPotionItem(String name)
    {
        super(new Properties().tab(ItemGroup.TAB_FOOD).stacksTo(1).rarity(Rarity.EPIC).food(teleportationPotion));
        this.setRegistryName(name);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack item = player.getItemInHand(hand);
        if(player.getCommandSenderWorld().dimensionType().natural())
        {
            player.startUsingItem(hand);
            return ActionResult.success(item);
        }
        return ActionResult.fail(item);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity)
    {
        IWorldInfo levelData = world.getLevelData();
        entity.stopRiding();
        if(!world.isClientSide)
        {
            if (entity.randomTeleport(levelData.getXSpawn(), levelData.getYSpawn(), levelData.getZSpawn(), false))
            {
                playSoundAtPlayer(world, entity);
                return new ItemStack(Items.GLASS_BOTTLE, 1);
            }
            entity.sendMessage(new TranslationTextComponent("item.telepotions.teleportation_potion.error"),entity.getUUID());
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
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.appendHoverText(itemStack, world, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.telepotions.wormhole_potion.lore"));
    }
}
