package installer.page;

import installer.Main;
import me.wincho.choui.rendersystem.CPage;
import me.wincho.choui.rendersystem.CWindow;
import me.wincho.choui.rendersystem.RenderSystem;
import me.wincho.choui.utils.FontUtils;
import me.wincho.choui.widget.EndlessProgressRingWidget;
import net.lingala.zip4j.ZipFile;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

public class InstallPage extends CPage {

    public InstallPage(CWindow window) {
        super(window);
        EndlessProgressRingWidget progressRing = new EndlessProgressRingWidget(325, 200, 50, 50);
        addWidget(progressRing);

        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(Main.data.get("download_from").getAsString());
                Path path = Paths.get(Main.data.get("download_to").getAsString());
                Files.createDirectories(path);
                Files.copy(url.openStream(), path.resolve(".zip"), StandardCopyOption.REPLACE_EXISTING);
                ZipFile file = new ZipFile(path.resolve(".zip").toString());
                file.extractAll(path.toString());
                file.close();
                Files.delete(path.resolve(".zip"));
                window.setPage(new CompletePage(window));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void render() {
        super.render();
        RenderSystem.drawString("Installing..", 30, 40, Color.BLACK, FontUtils.DEFAULT_FONT);
    }
}
