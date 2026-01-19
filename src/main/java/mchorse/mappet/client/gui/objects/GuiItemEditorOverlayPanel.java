package mchorse.mappet.client.gui.objects;

import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.config.values.ValueInt;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Consumer;

public class GuiItemEditorOverlayPanel extends GuiOverlayPanel {
    public GuiTrackpadElement itemCount;
    public GuiTextElement itemName;
    public GuiTextElement itemTooltip;

    public GuiItemEditorOverlayPanel(Minecraft mc, NBTTagCompound compound, Consumer<NBTTagCompound> redacted, boolean is_item_block) {
        super(mc, IKey.lang("mappet.gui.objects.item_editor.title"));
        itemCount = new GuiTrackpadElement(mc, (t) -> {
            compound.setInteger("count", (int) t.doubleValue());
            redacted.accept(compound);
        });
        itemCount.limit(1, 64, true);
        itemName = new GuiTextElement(mc, (t) -> {
            compound.setString("name", t);
            redacted.accept(compound);
        });
        itemName.setEnabled(true);
        itemTooltip = new GuiTextElement(mc, (t) -> {
            compound.setString("tooltip", t);
            redacted.accept(compound);
        });
        if (compound.hasKey("count")) {
            int count = compound.getInteger("count");
            itemCount.setValue(count);
        }
        if (compound.hasKey("name")) {
            String name = compound.getString("name");
            itemName.setText(name);
        }
        if (compound.hasKey("tooltip")) {
            String tooltip = compound.getString("tooltip");
            itemTooltip.setText(tooltip);
        }
        GuiElement i = new GuiElement(mc);
        i.flex().relative(this.content).x(0F).y(0F, 5).w(100).column(3).vertical().stretch();
        if (!is_item_block)
            i.add(
                Elements.label(IKey.lang("mappet.gui.objects.text_item_count")),
                itemCount,
                Elements.label(IKey.lang("mappet.objects.text_item_name")),
                itemName,
                Elements.label(IKey.lang("mappet.gui.objects.text_item_tooltip")),
                itemTooltip);
        else
            i.add(
                    Elements.label(IKey.lang("mappet.objects.text_item_name")),
                    itemName
            );
        this.content.add(i);
    }
}
