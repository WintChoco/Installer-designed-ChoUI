package installer.page;

import me.wincho.choui.rendersystem.CPage;
import me.wincho.choui.rendersystem.CWindow;
import me.wincho.choui.rendersystem.RenderSystem;
import me.wincho.choui.utils.FontUtils;

import java.awt.Color;

public class CompletePage extends CPage {
    public CompletePage(CWindow window) {
        super(window);
        window.setCloseEvent(() -> System.exit(0));
    }

    @Override
    public void render() {
        super.render();
        RenderSystem.drawString("Install Complete!", 700 / 2 - RenderSystem.getStringWidth("Install Complete!") / 2, 600 / 2 - RenderSystem.getStringHeight() / 2, Color.BLACK, FontUtils.DEFAULT_FONT);
    }
}
