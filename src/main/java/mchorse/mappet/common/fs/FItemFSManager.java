package mchorse.mappet.common.fs;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.manager.BaseManager;
import mchorse.mappet.api.utils.manager.IManager;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FItemFSManager extends BaseManager<FItemFSOptions> {
    public File folder;
    public final Map<ResourceLocation, FItemFSOptions> fsOptionsMap = new HashMap<>();
    public final Map<ResourceLocation, Item> registeredItems = new HashMap<>();

    public FItemFSManager(File folder) {
        super(folder);
        this.folder = folder;
    }

    @Override
    public FItemFSOptions create(String id) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("scriptId", "none");
        compound.setString("itemName", "Magic Wand");
        compound.setString("itemId", "magic_wand");
        return create(id, compound);
    }

    protected FItemFSOptions createData(ResourceLocation id, String name) {
        File file = new File(folder, name);
        try {
            BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();

            String itemName  = json.has("itemName")  ? json.get("itemName").getAsString()  : "Magic Wand";
            String itemId    = json.has("itemId")    ? json.get("itemId").getAsString()    : "magic_wand";
            String scriptId  = json.has("scriptId")  ? json.get("scriptId").getAsString()  : "magic_wand.js";
            fsOptionsMap.put(id, new FItemFSOptions(itemName, itemId, scriptId, file.getName()));
            return new FItemFSOptions(itemName, itemId, scriptId, file.getName());
        } catch (IOException e) {
            throw new RuntimeException("[Mappet/Fusion] Error in read file. Error: " + e);
        }
    }

    public Collection<Item> getRegisteredItems() {
        return registeredItems.values();
    }

    @Override
    public boolean exists(String name) {
        return Files.exists(Paths.get(folder.getPath(), name));
    }

    @Override
    protected FItemFSOptions createData(String id, NBTTagCompound tag) {
        if (id.isEmpty()) id = "magic_wand.json";
        Path path = Paths.get(folder.getPath(), id);
        String content = "{\"scriptId\": \"none\", \"itemId\": \"" + tag.getString("itemId") + "\"" + ", \"itemName\": \"" + tag.getString("itemName") + "\"}";
        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored) {}
        FItemFSOptions options = new FItemFSOptions(tag.getString("itemName"), tag.getString("itemId"), "none", id);
        fsOptionsMap.put(new ResourceLocation(Mappet.MOD_ID, options.itemId), options);
        return options;
    }

    @Override
    public FItemFSOptions load(String id) {
        return fsOptionsMap.get(new ResourceLocation(Mappet.MOD_ID, id));
    }

    @Override
    public boolean save(String name, NBTTagCompound tag) {
        return false
    }

    @Override
    public boolean rename(String id, String newId) {
        File file = Paths.get(folder.getPath(), id).toFile();
        return file.renameTo(new File(newId));
    }

    @Override
    public boolean delete(String name) {
        Path path = Paths.get(folder.getPath(), name);
        return path.toFile().delete();
    }

    @Override
    public File getFolder() {
        return folder;
    }

    @Override
    public Collection<String> getKeys() {
        HashSet<String> set = new HashSet<>();
        for (ResourceLocation location : fsOptionsMap.keySet()) {
            set.add(location.toString());
        }
        return set;
    }
}
