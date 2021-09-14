package installer.page;

import installer.Main;
import me.wincho.choui.rendersystem.CPage;
import me.wincho.choui.rendersystem.CWindow;
import me.wincho.choui.rendersystem.RenderSystem;
import me.wincho.choui.rendersystem.shape.Line;
import me.wincho.choui.rendersystem.shape.Rectangle;
import me.wincho.choui.utils.FontUtils;
import me.wincho.choui.widget.ButtonWidget;

import java.awt.Color;

import static installer.Main.data;

public class LicensePage extends CPage {
    public LicensePage(CWindow window, CPage parent) {
        super(window);
        addWidget(new ButtonWidget(370, 400, 120, 40, target -> Main.cancel(this), "Cancel"));
        addWidget(new ButtonWidget(230, 400, 120, 40, target -> window.setPage(parent), "< Back"));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addWidget(new ButtonWidget(510, 400, 160, 40, target -> {
            window.setPage(new InstallPage(window));
        }, "I Agree & Install"));
    }

    @Override
    public void render() {
        RenderSystem.drawShape(
                new Rectangle(50, 80, 600, 290, new Color(0xB4B4B4))
        );
        RenderSystem.drawShape(
                new Rectangle(0, 380, 700, 200, new Color(0xC8C8C8))
        );
        super.render();
        RenderSystem.drawString("Licence Agreement", 30, 40, Color.BLACK, FontUtils.DEFAULT_FONT);
        RenderSystem.drawString("Please review the license terms before installing " + data.get("name").getAsString() + " " + data.get("version").getAsString(), 50, 60, Color.BLACK, FontUtils.DEFAULT_FONT);
        String[] spl = data.get("license").getAsString().split("\n");
        int i = 90;
        for (String s : spl) {
            RenderSystem.drawString(s, 200, i += 20, Color.BLACK, FontUtils.DEFAULT_FONT);
        }
    }
}
