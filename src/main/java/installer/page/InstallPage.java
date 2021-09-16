package installer.page;

import installer.Main;
import me.wincho.choui.rendersystem.CPage;
import me.wincho.choui.rendersystem.CWindow;
import me.wincho.choui.rendersystem.RenderSystem;
import me.wincho.choui.utils.FontUtils;
import me.wincho.choui.widget.LabelWidget;
import me.wincho.choui.widget.ProgressBarWidget;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.progress.ProgressMonitor;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

public class InstallPage extends CPage {

    public InstallPage(CWindow window) {
        super(window);
        ProgressBarWidget progressBar = new ProgressBarWidget(200, 200, 300, 20);
        addWidget(progressBar);
        LabelWidget label = new LabelWidget(10, 150, new Font(Font.DIALOG, Font.PLAIN, 15), "");
        addWidget(label);

        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(Main.data.get("download_from").getAsString());
                Path path = Paths.get(Main.data.get("download_to").getAsString()).toAbsolutePath();
                label.setText("Downloading zip file from web...");
                Files.createDirectories(path);
                Files.copy(url.openStream(), path.resolve(".zip"), StandardCopyOption.REPLACE_EXISTING);

                ZipFile file = new ZipFile(path.resolve(".zip").toString());

                file.setRunInThread(true);

                ProgressMonitor progressMonitor = file.getProgressMonitor();

                file.extractAll(path.toString());
                progressBar.setMax(100);
                while (progressMonitor.getState() == ProgressMonitor.State.BUSY) {
                    progressBar.setValue(progressMonitor.getPercentDone());
                    label.setText("Unzipping " + progressMonitor.getFileName() + "...");
                }

                System.out.println("Result: " + progressMonitor.getResult());

                if (progressMonitor.getResult() == ProgressMonitor.Result.ERROR) {
                    if (progressMonitor.getException() != null) {
                        progressMonitor.getException().printStackTrace();
                    } else {
                        System.err.println("An error occurred without any exception");
                    }
                }

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
