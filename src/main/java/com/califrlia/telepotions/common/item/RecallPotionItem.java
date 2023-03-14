package com.califrlia.telepotions.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.califrlia.telepotions.util.EffectUtil.playSoundAtPlayer;

public class RecallPotionItem extends Item
{
    private static final Food recallPotion = new Food.Builder().alwaysEat().nutrition(0).build();

    public RecallPotionItem(String registryName)
    {
        super(new Properties().tab(ItemGroup.TAB_FOOD).rarity(Rarity.EPIC).stacksTo(1).food(recallPotion));
        this.setRegistryName(registryName);
    }

    @Override
    public int getUseDuration(ItemStack itemStack)
    {
        return 32;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!world.isClientSide)
        {
            //No null asserted, since an instance of PlayerEntity is triggering this. (getPlayerList())
            ServerPlayerEntity serverPlayer = player.getServer().getPlayerList().getPlayer(player.getUUID());
            @Nullable BlockPos respawnPos = serverPlayer.getRespawnPosition();
            if(respawnPos != null)
            {
                player.startUsingItem(hand);
                return ActionResult.success(itemStack);
            }

            serverPlayer.sendMessage(new TranslationTextComponent("block.minecraft.spawn.not_valid"),player.getUUID());
        }
        return ActionResult.fail(itemStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity)
    {
        if(entity instanceof PlayerEntity && !world.isClientSide)
        {
            PlayerEntity player = (PlayerEntity) entity;

            //No null asserted, since an instance of PlayerEntity is triggering this. (getPlayerList()
            ServerPlayerEntity serverPlayer = player.getServer().getPlayerList().getPlayer(player.getUUID());
            ServerWorld serverWorld = serverPlayer.getLevel().getWorldServer();
            @Nullable BlockPos respawnPos = serverPlayer.getRespawnPosition();
            if(respawnPos != null)
            {
                //Fourth argument in findRespawnPositionAndUSeSpawnBlock might be obfuscated:
                //If set to true, it won't take a charge off the anchor.
                Optional<Vector3d> spawnPos = serverPlayer.findRespawnPositionAndUseSpawnBlock(serverWorld,respawnPos,0,false,false);
                if(spawnPos.isPresent())
                {
                    entity.stopRiding();
                    serverPlayer.moveTo(spawnPos.get());
                    //Play anchor charge consumption sound
                    if(!serverWorld.getBlockState(respawnPos).getBlock().is(BlockTags.BEDS))
                    {
                        serverPlayer.connection.send(new SPlaySoundEffectPacket(SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundCategory.BLOCKS, respawnPos.getX(), respawnPos.getY(), respawnPos.getZ(), 1.0F, 1.0F));
                    }
                    playSoundAtPlayer(world, entity);
                    return new ItemStack(Items.GLASS_BOTTLE,1);
                }
                serverPlayer.sendMessage(new TranslationTextComponent("block.minecraft.spawn.not_valid"),player.getUUID());
            }

        }
        return stack;
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
        tooltip.add(new TranslationTextComponent("item.telepotions.recall_potion.lore"));
    }
}
