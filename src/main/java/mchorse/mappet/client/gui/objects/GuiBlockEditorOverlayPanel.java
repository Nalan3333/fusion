package mchorse.mappet.client.gui.objects;

import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Consumer;

public class GuiBlockEditorOverlayPanel extends GuiOverlayPanel {
    public GuiElement container;
    public GuiTextElement blockMaterial;

    public GuiBlockEditorOverlayPanel(Minecraft mc, NBTTagCompound data, Consumer<NBTTagCompound> redacted) {
        super(mc, IKey.lang("mappet.gui.objects.block_editor.title"));

        blockMaterial = new GuiTextElement(mc, (t) -> {
            data.setString("material", t);
            redacted.accept(data);
        });

        String material = data.hasKey("material") ? data.getString("material") : "";
        blockMaterial.setText(material);

        container = new GuiElement(mc);
        container.flex().relative(this.content).wh(150, 50).xy(0.0F, 0.0F).column(5).vertical().stretch();

        container.add(Elements.label(IKey.lang("mappet.objects.block_editor.material_text")), blockMaterial);

        this.content.add(container);
    }
}
