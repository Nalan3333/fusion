package mchorse.mappet.items;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.fs.RegisterObject;
import mchorse.mappet.api.regions.shapes.CylinderShape;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomItem extends Item {
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Item item = stack.getItem();
        String itemId = item.getRegistryName().getResourcePath();
        String name = Mappet.objects.load(itemId).getData().getString("name");
        return name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack,
                               World world,
                               List<String> tooltip,
                               ITooltipFlag flag) {
        Item item = stack.getItem();
        String itemId = item.getRegistryName().getResourcePath();
        String itemTooltip = Mappet.objects.load(itemId).getData().getString("tooltip");
        if (!itemTooltip.isEmpty())
            tooltip.add(itemTooltip);
    }
}
