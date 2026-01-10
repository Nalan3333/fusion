package mchorse.mappet.common.fs;

import net.minecraft.nbt.NBTTagCompound;

public class FItemFSOptions extends FSOptions {
    public String itemName;
    public String itemId;
    public String scriptId;
    public String fileName;

    public FItemFSOptions(String itemName, String itemId, String scriptId, String fileName) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.scriptId = scriptId;
        this.fileName = fileName;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("itemName", itemName);
        tag.setString("itemId", itemId);
        tag.setString("scriptId", scriptId);
        tag.setString("fileName", fileName);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tagCompound) {
        itemName = tagCompound.getString("itemName");
        itemId = tagCompound.getString("itemId");
        scriptId = tagCompound.getString("scriptId");
        fileName = tagCompound.getString("fileName");
    }
}
