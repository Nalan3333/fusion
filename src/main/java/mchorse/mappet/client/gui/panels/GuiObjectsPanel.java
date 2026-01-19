package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.fs.RegisterObject;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.api.utils.IContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.objects.GuiBlockEditorOverlayPanel;
import mchorse.mappet.client.gui.objects.GuiItemEditorOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.utils.MPIcons;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.Objects;

public class GuiObjectsPanel extends GuiMappetDashboardPanel<RegisterObject> {
    public GuiTextElement type;
    GuiElement i;
    GuiButtonElement openEditor;

    public GuiObjectsPanel(Minecraft mc, GuiMappetDashboard dashboard) {
        super(mc, dashboard);
        this.namesList.setFileIcon(MPIcons.STAR);
        i = new GuiElement(mc);
        type = new GuiTextElement(mc, (t) -> {
            this.data.type = t;
        });
        openEditor = new GuiButtonElement(mc, IKey.lang("mappet.gui.objects.open_editor"), (t) -> {
            if (Objects.equals(this.data.type, "item") || Objects.equals(this.data.type, "item_block")) {
                GuiItemEditorOverlayPanel overlayPanel = new GuiItemEditorOverlayPanel(mc, this.data.getData(), (n) -> {
                    this.data.setData(n);
                }, this.data.type.equals("item_block"));
                GuiOverlay.addOverlay(GuiBase.getCurrent(), overlayPanel);
            }
            else if (Objects.equals(this.data.type, "block")) {
                GuiBlockEditorOverlayPanel overlayPanel = new GuiBlockEditorOverlayPanel(mc, this.data.getData(), (n) -> {
                    this.data.setData(n);
                });
                GuiOverlay.addOverlay(GuiBase.getCurrent(), overlayPanel);
            }
        });
        type.setEnabled(true);

        i.flex().relative(this.editor).xy(0.5F, 0.5F).wh(150, 50).column(5).vertical().stretch();

        i.add(Elements.label(IKey.lang("mappet.gui.objects.text_type")), type, openEditor);

        this.editor.add(i);

        i.setVisible(false);
    }

    @Override
    protected void fillDefaultData(RegisterObject data) {
        super.fillDefaultData(data);
        data.identifier = "magic_wand";
    }

    @Override
    public void fill(RegisterObject data, boolean allowed) {
        super.fill(data, allowed);

        this.editor.setVisible(data != null);
        i.setVisible(data != null);

        if (data != null) {
            this.type.setText(data.type);
        }
    }

    @Override
    public IContentType getType() {
        return ContentType.OBJECTS;
    }

    @Override
    public String getTitle() {
        return "mappet.gui.panels.objects";
    }
}
