package mchorse.mappet.api.fs;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mchorse.mappet.api.utils.manager.BaseManager;
import mchorse.mappet.items.CustomItem;
import mchorse.mappet.utils.NBTToJsonLike;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ManagerRegisteredObject extends BaseManager<RegisterObject> {
    public File folder;

    public final HashMap<String, Item> registeredItems = new HashMap<>();
    public final HashMap<String, Block> registeredBlocks = new HashMap<>();
    private final HashMap<String, RegisterObject> objects = new HashMap<String, RegisterObject>();

    public ManagerRegisteredObject(File folder) {
        super(folder);
        this.folder = folder;
    }

    @Override
    public boolean delete(String name) {
        objects.remove(name);
        registeredItems.remove(name);
        registeredBlocks.remove(name);
        return super.delete(name);
    }

    @Override
    protected RegisterObject createData(String id, NBTTagCompound tag) {
        RegisterObject object = new RegisterObject();
        object.identifier = id;
        if (tag != null) {
            object.deserializeNBT(tag);
        }
        return object;
    }

    @Override
    public boolean save(String id, RegisterObject data) {
        objects.put(id, data);
        super.save(id, data);
        return true;
    }

    public void putObject(String id,RegisterObject object) {
        objects.put(id, object);
    }

    public RegisterObject getObject(String id) { return objects.get(id); }

    public Collection<RegisterObject> getObjects() { return objects.values(); }

    public File getObjectFile(String id) {
        return Paths.get(folder.getPath(), id).toFile();
    }
}
