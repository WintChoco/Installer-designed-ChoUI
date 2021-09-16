package installer.page;

import installer.Main;
import me.wincho.choui.rendersystem.CPage;
import me.wincho.choui.rendersystem.CWindow;
import me.wincho.choui.rendersystem.RenderSystem;
import me.wincho.choui.rendersystem.shape.Rectangle;
import me.wincho.choui.utils.FontUtils;
import me.wincho.choui.widget.ButtonWidget;
import me.wincho.choui.widget.LabelWidget;
import me.wincho.choui.dialog.MessageDialog;

import java.awt.Color;
import java.awt.Font;

import static installer.Main.data;

public class TitlePage extends CPage {
    public TitlePage(CWindow window) {
        super(window);
        addWidget(new ButtonWidget(550, 400, 120, 40, target -> {
            window.setPage(new LicensePage(window, this));
        }, "Next >"));
        addWidget(new ButtonWidget(410, 400, 120, 40, target -> Main.cancel(this), "Cancel"));

        RenderSystem.drawString("a", 0, 0, Color.BLACK, FontUtils.DEFAULT_FONT);
        int i = 70;
        addWidget(new LabelWidget(100, i += 20, "Setup will guide you through the installation of " + data.get("name").getAsString() + " " + data.get("version").getAsString()));i += 20;
        addWidget(new LabelWidget(100, i += 20, "It is recommended that you close all other applications"));
        addWidget(new LabelWidget(100, i += 20, "before starting Setup. This will make it possible to update"));
        addWidget(new LabelWidget(100, i += 20, "relevant system files without having to reboot your"));
        addWidget(new LabelWidget(100, i += 20, "computer"));i += 20;
        addWidget(new LabelWidget(100, i += 20, "Click Next to continue."));
    }

    @Override
    public void render() {
        RenderSystem.drawShape(
                new Rectangle(0, 380, 700, 200, new Color(0xC8C8C8))
        );
        RenderSystem.drawString("Welcome to " + data.get("name").getAsString() + " " + data.get("version").getAsString() + " Setup", 100, 50, Color.BLACK, new Font(Font.DIALOG, Font.BOLD, 20));

        super.render();
    }
}
