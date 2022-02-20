package com.califralia.telepotion.items;

import com.califralia.telepotion.Telepotion;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

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
}
