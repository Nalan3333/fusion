package mchorse.mappet.api.data;

import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.common.fs.FItemFSOptions;
import net.minecraft.nbt.NBTTagCompound;

public class FItemData extends AbstractData {
    private final FItemFSOptions options;
    private final ScriptTriggerBlock onRightClick = new ScriptTriggerBlock("FItemOnRightClick", "onRightClick");
    private final ScriptTriggerBlock onTick = new ScriptTriggerBlock("FItemOnTick", "onTick");

    public FItemData(FItemFSOptions options) {
        this.options = options;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("onRightClick", onRightClick.serializeNBT());
        tag.setTag("onTick", onTick.serializeNBT());
        tag.setTag("FSOptions", options.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tagCompound) {
        onRightClick.deserializeNBT(tagCompound.getCompoundTag("onRightClick"));
        onTick.deserializeNBT(tagCompound.getCompoundTag("onTick"));
        options.deserializeNBT(tagCompound.getCompoundTag("FSOptions"));
    }
}
