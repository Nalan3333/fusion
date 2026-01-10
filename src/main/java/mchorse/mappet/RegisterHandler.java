package mchorse.mappet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import mchorse.mappet.api.crafting.CraftingManager;
import mchorse.mappet.api.dialogues.DialogueManager;
import mchorse.mappet.api.events.EventManager;
import mchorse.mappet.api.factions.FactionManager;
import mchorse.mappet.api.huds.HUDManager;
import mchorse.mappet.api.npcs.NpcManager;
import mchorse.mappet.api.quests.QuestManager;
import mchorse.mappet.api.quests.chains.QuestChainManager;
import mchorse.mappet.api.schematics.SchematicManager;
import mchorse.mappet.api.scripts.ScriptManager;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.blocks.BlockConditionModel;
import mchorse.mappet.blocks.BlockEmitter;
import mchorse.mappet.blocks.BlockRegion;
import mchorse.mappet.blocks.BlockTrigger;
import mchorse.mappet.client.KeyboardHandler;
import mchorse.mappet.client.RenderingHandler;
import mchorse.mappet.common.fs.FItemFSManager;
import mchorse.mappet.common.fs.FItemFSOptions;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.items.CustomItem;
import mchorse.mappet.items.ItemNpcTool;
import mchorse.mappet.tile.TileConditionModel;
import mchorse.mappet.tile.TileEmitter;
import mchorse.mappet.tile.TileRegion;
import mchorse.mappet.tile.TileTrigger;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Objects;

public class RegisterHandler
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        if (!event.isLocal())
        {
            Mappet.quests = new QuestManager(null);
            Mappet.schematics = new SchematicManager(null);
            Mappet.crafting = new CraftingManager(null);
            Mappet.events = new EventManager(null);
            Mappet.dialogues = new DialogueManager(null);
            Mappet.npcs = new NpcManager(null);
            Mappet.factions = new FactionManager(null);
            Mappet.chains = new QuestChainManager(null);
            Mappet.scripts = new ScriptManager(null);
            Mappet.itemScripts = new ScriptManager(null);
            Mappet.FItemFsManager = new FItemFSManager(null);
            Mappet.huds = new HUDManager(null);
        }

        KeyboardHandler.clientPlayerJournal = true;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        Mappet.quests = null;
        Mappet.crafting = null;
        Mappet.events = null;
        Mappet.dialogues = null;
        Mappet.npcs = null;
        Mappet.factions = null;
        Mappet.chains = null;
        Mappet.scripts = null;
        Mappet.huds = null;

        KeyboardHandler.hotkeys.clear();
        RenderingHandler.reset();
    }

    @SubscribeEvent
    public void onBlocksRegister(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(Mappet.emitterBlock = new BlockEmitter());
        event.getRegistry().register(Mappet.triggerBlock = new BlockTrigger());
        event.getRegistry().register(Mappet.regionBlock = new BlockRegion());
        event.getRegistry().register(Mappet.conditionModelBlock = new BlockConditionModel());
    }

    @SubscribeEvent
    public void onItemsRegister(RegistryEvent.Register<Item> event)
    {
        Mappet.itemScripts.initiateAllScripts();
        for (File file : Objects.requireNonNull(Mappet.FItemFsManager.folder.listFiles())) {
            try {
                BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
                JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
                String itemName  = json.has("itemName")  ? json.get("itemName").getAsString()  : "Magic Wand";
                String itemId = json.has("itemId") ? json.get("itemId").getAsString() : "magic_wand";
                String scriptId = json.has("scriptId") ? json.get("scriptId").getAsString() : "magic_wand.js";
                CustomItem item = new CustomItem();
                ResourceLocation location = new ResourceLocation(Mappet.MOD_ID, itemId);
                Mappet.FItemFsManager.fsOptionsMap.put(location, new FItemFSOptions(itemName, itemId, scriptId, file.getName()));
                item.setRegistryName(location);
                item.setUnlocalizedName(itemName);
                Mappet.FItemFsManager.registeredItems.put(location, item);
                event.getRegistry().register(item);
            } catch (IOException e) {
                System.err.println("Error in reader: " + e.getMessage());
            }
        }
//        for (FItemFSOptions option : Mappet.FItemFsManager.fsOptionsMap.values()) {
//            CustomItem item = new CustomItem();
//            item.setRegistryName(
//                    new ResourceLocation(Mappet.MOD_ID, option.itemId)
//            );
//
//            item.setUnlocalizedName(
//                    Mappet.MOD_ID + "." + option.itemId
//            );
//
//            event.getRegistry().register(item);
//            System.out.println("[Mappet/Fusion]: Registered FItem: " + option.itemId);
//        }
        event.getRegistry().register(Mappet.npcTool = new ItemNpcTool()
                .setRegistryName(new ResourceLocation(Mappet.MOD_ID, "npc_tool"))
                .setUnlocalizedName(Mappet.MOD_ID + ".npc_tool"));

        event.getRegistry().register(new ItemBlock(Mappet.emitterBlock)
                .setRegistryName(new ResourceLocation(Mappet.MOD_ID, "emitter"))
                .setUnlocalizedName(Mappet.MOD_ID + ".emitter"));

        event.getRegistry().register(new ItemBlock(Mappet.triggerBlock)
                .setRegistryName(new ResourceLocation(Mappet.MOD_ID, "trigger"))
                .setUnlocalizedName(Mappet.MOD_ID + ".trigger"));

        event.getRegistry().register(new ItemBlock(Mappet.regionBlock)
                .setRegistryName(new ResourceLocation(Mappet.MOD_ID, "region"))
                .setUnlocalizedName(Mappet.MOD_ID + ".region"));

        event.getRegistry().register(new ItemBlock(Mappet.conditionModelBlock)
                .setRegistryName(new ResourceLocation(Mappet.MOD_ID, "condition_model"))
                .setUnlocalizedName(Mappet.MOD_ID + ".condition_model"));
    }

    @SubscribeEvent
    public void onEntityRegister(RegistryEvent.Register<EntityEntry> event)
    {
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(EntityNpc.class)
                .name(Mappet.MOD_ID + ".npc")
                .id(new ResourceLocation(Mappet.MOD_ID, "npc"), 0)
                .tracker(EntityNpc.RENDER_DISTANCE, 3, false)
                .build());

        GameRegistry.registerTileEntity(TileEmitter.class, Mappet.MOD_ID + ":emitter");
        GameRegistry.registerTileEntity(TileTrigger.class, Mappet.MOD_ID + ":trigger");
        GameRegistry.registerTileEntity(TileRegion.class, Mappet.MOD_ID + ":region");
        GameRegistry.registerTileEntity(TileConditionModel.class, Mappet.MOD_ID + ":condition_model");
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelRegistry(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(Mappet.npcTool, 0, getNpcToolTexture());
        for (Item item : Mappet.FItemFsManager.getRegisteredItems()) {
            if (item instanceof CustomItem) {
                ResourceLocation rl = item.getRegistryName();
                assert rl != null;
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(rl, "inventory"));
            }
        }

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Mappet.emitterBlock), 0, new ModelResourceLocation(Mappet.MOD_ID + ":emitter", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Mappet.triggerBlock), 0, new ModelResourceLocation(Mappet.MOD_ID + ":trigger", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Mappet.regionBlock), 0, new ModelResourceLocation(Mappet.MOD_ID + ":region", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Mappet.conditionModelBlock), 0, new ModelResourceLocation(Mappet.MOD_ID + ":condition_model", "inventory"));
    }

    public ModelResourceLocation getNpcToolTexture()
    {
        String postfix = "";
        Calendar calendar = Calendar.getInstance();

        if (isWinter(calendar))
        {
            postfix = "_winter";
        }

        if (isChristmas(calendar))
        {
            postfix = "_christmas";
        }
        else if (isEaster(calendar))
        {
            postfix = "_easter";
        }
        else if (isAprilFoolsDay(calendar))
        {
            postfix = "_april";
        }
        else if (isHalloween(calendar))
        {
            postfix = "_halloween";
        }

        return new ModelResourceLocation(Mappet.MOD_ID + ":npc_tool" + postfix, "inventory");
    }

    public boolean isChristmas(Calendar calendar)
    {
        return calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DATE) >= 24 && calendar.get(Calendar.DATE) <= 26;
    }

    public boolean isAprilFoolsDay(Calendar calendar)
    {
        return calendar.get(Calendar.MONTH) == Calendar.APRIL && calendar.get(Calendar.DATE) <= 2;
    }

    public boolean isWinter(Calendar calendar)
    {
        int month = calendar.get(Calendar.MONTH);

        return month == Calendar.DECEMBER || month == Calendar.JANUARY || month == Calendar.FEBRUARY;
    }

    public boolean isEaster(Calendar calendar)
    {
        Calendar easterDate = getEasterDate(calendar.get(Calendar.YEAR));

        return calendar.get(Calendar.MONTH) == easterDate.get(Calendar.MONTH) && calendar.get(Calendar.DATE) == easterDate.get(Calendar.DATE);
    }

    public boolean isHalloween(Calendar calendar)
    {
        return calendar.get(Calendar.MONTH) == Calendar.OCTOBER && calendar.get(Calendar.DATE) >= 24;
    }

    public Calendar getEasterDate(int year)
    {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int month = (h + l + 114) / 31;
        int day = (h + l + 114) % 31;

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month - 1, day + 1);

        return calendar;
    }
}