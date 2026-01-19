package mchorse.mappet.api.fs;

import mchorse.mappet.api.utils.AbstractData;
import net.minecraft.nbt.NBTTagCompound;

public class RegisterObject extends AbstractData {
    public String identifier;
    public String type = "item";
    private NBTTagCompound data = new NBTTagCompound();

    public RegisterObject(NBTTagCompound data) {
        this.data = data;
    }
    public RegisterObject() {}
    public NBTTagCompound getData() { return data; }
    public void setData(NBTTagCompound tag) {
        this.data = tag;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("id", identifier);
        compound.setString("type", type);
        compound.setTag("data", data);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        NBTTagCompound data = compound.getCompoundTag("data");
        String identifier = compound.getString("id");
        String type = compound.getString("type");

        this.data = data;
        this.identifier = identifier;
        this.type = type;
    }
}
