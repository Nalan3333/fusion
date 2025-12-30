package mchorse.mappet.common.fs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.manager.BaseManager;
import mchorse.mappet.utils.FSInterpreter;
import mchorse.mclib.utils.JsonUtils;
import net.minecraft.nbt.NBTTagCompound;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FItemFSManager extends BaseManager<FItemFSOptions> {
    public final Map<String, FItemFSOptions> fsOptionsMap = new HashMap<>();

    public FItemFSManager(File folder) {
        super(folder);
    }

    @Override
    protected FItemFSOptions createData(String id, NBTTagCompound tag) {
        File file = new File(folder, id);
        try {
            BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();

            String itemName  = json.has("itemName")  ? json.get("itemName").getAsString()  : "Magic Wand";
            String itemId    = json.has("itemId")    ? json.get("itemId").getAsString()    : "magic_wand";
            String scriptId  = json.has("scriptId")  ? json.get("scriptId").getAsString()  : "magic_wand.js";
            fsOptionsMap.put(itemId, new FItemFSOptions(itemName, itemId, scriptId));
            return new FItemFSOptions(itemName, itemId, scriptId);
        } catch (IOException e) {
            throw new RuntimeException("[Mappet/Fusion] Error in read file. Error: " + e);
        }
    }

    public void initiateFItemsFSOptions() {
        if (folder == null) {
            System.out.println("FItemFSManager folder == null!");
            return;
        }

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("[Mappet/Fusion]: Folder is empty: " + folder.getAbsolutePath());
            return;
        }

        for (File file : files) {
            createData(file.getName(), new NBTTagCompound());
            System.out.println("[Mappet/Fusion]: Created Data: " + file.getName());
        }
    }
}
