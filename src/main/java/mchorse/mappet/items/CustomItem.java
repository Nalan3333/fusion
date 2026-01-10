package mchorse.mappet.items;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.script.ScriptException;
import java.util.Objects;

public class CustomItem extends Item {
    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("scriptId", Objects.requireNonNull(this.getRegistryName()).getResourcePath() + ".js");
        stack.setTagCompound(compound);
        return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote) {
            return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));
        }
        ItemStack stack = player.getHeldItem(hand);
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) tag = new NBTTagCompound();
        if (!tag.hasKey("scriptId")) {
            tag.setString("scriptId", this.getRegistryName().getResourcePath() + ".js");
        }
        stack.setTagCompound(tag);
        String scriptId = tag.getString("scriptId");

        try {
            Mappet.itemScripts.execute(scriptId, "onItemRightClick", new DataContext(player));
        } catch (ScriptException | NoSuchMethodException e) {
            Mappet.logger.error(e.getMessage());
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
        if (player.getEntityWorld().isRemote) {
            return false;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) tag = new NBTTagCompound();
        if (!tag.hasKey("scriptId")) {
            tag.setString("scriptId", Objects.requireNonNull(this.getRegistryName()).getResourcePath() + ".js"); // безопасно, toString включает namespace
        }
        stack.setTagCompound(tag);
        String scriptId = tag.getString("scriptId");

        try {
            Mappet.itemScripts.execute(scriptId, "onDroppedByPlayer", new DataContext(player));
        } catch (ScriptException | NoSuchMethodException e) {
            Mappet.logger.error(e.getMessage());
        }

        return true;
    }
}
