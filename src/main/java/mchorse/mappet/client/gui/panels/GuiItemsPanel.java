package mchorse.mappet.client.gui.panels;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.ui.components.UITextareaComponent;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.api.utils.IContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.scripts.GuiTextEditor;
import mchorse.mappet.common.fs.FItemFSOptions;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.utils.Elements;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GuiItemsPanel extends GuiMappetDashboardPanel<FItemFSOptions> {
    public FItemFSOptions data;

    private GuiTextEditor editorItem;
    private GuiTextEditor editorScript;

    private GuiElement settings;

    @Override
    protected void addNewData(String name, FItemFSOptions data) {
        if (name.lastIndexOf(".") == -1)
        {
            name = name + ".json";
        }

        super.addNewData(name, data);
    }

    @Override
    public void fill(FItemFSOptions data, boolean allowed) {
        super.fill(data, allowed);
        settings.setEnabled(data != null);
        editor.setEnabled(data != null);
        File scriptFolder = new File(new File(new File(DimensionManager.getCurrentSaveRootDirectory(), Mappet.MOD_ID), "scripts").toURI());
        if (data != null) {
            Path jsonItem = Paths.get(scriptFolder.getPath(), "fs", data.fileName);
            Path scriptItem = Paths.get(scriptFolder.getPath(), "items", data.itemId + ".js");
            try {
                List<String> linesJSON = Files.readAllLines(jsonItem);
                StringBuilder builder = new StringBuilder();
                for (String line : linesJSON) {
                    builder.append(line).append("\n");
                }
                editorItem.setText(builder.toString());
                List<String> linesScript = Files.readAllLines(scriptItem);
                StringBuilder builder2 = new StringBuilder();
                for (String line : linesScript) {
                    builder2.append(line).append("\n");
                }
                editorScript.setText(builder2.toString());
            } catch (IOException ignored) {}
        }
    }

    public GuiItemsPanel(Minecraft mc, GuiMappetDashboard dashboard) {
        super(mc, dashboard);

        editorItem = new GuiTextEditor(mc, null);
        editorScript = new GuiTextEditor(mc, null);

        settings = Elements.column(mc, 10, 10,
                    this.editorItem,
                    this.editorScript
                );

        this.editor.add(settings);
    }

    @Override
    public IContentType getType() {
        return ContentType.ITEMS;
    }

    @Override
    public String getTitle() {
        return "mappet.gui.panels.items";
    }
}
