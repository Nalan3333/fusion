package mchorse.mappet.items;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.data.FItemData;
import mchorse.mappet.api.scripts.code.ScriptWorld;
import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.common.fs.FItemFSManager;
import mchorse.mappet.common.fs.FItemFSOptions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.script.ScriptException;

public class CustomItem extends Item {
    public FItemData data;

    public CustomItem(FItemFSOptions options) {
        super();
        if (options != null) {
            this.data = new FItemData(options);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        CustomItem item = (CustomItem) stack.getItem();

        try {
            Mappet.itemScripts.execute(item.data.getId(), "onItemRightClick", new DataContext(player), new ScriptWorld(world), ScriptItemStack.create(stack), hand);
        } catch (ScriptException e) {
            Mappet.logger.error(e.getMessage());
        } catch (NoSuchMethodException e) {
            Mappet.logger.error("Not find method onItemRightClick");
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
