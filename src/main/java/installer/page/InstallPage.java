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
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
        LabelWidget label = new LabelWidget(200, 160);
        addWidget(label);

        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(Main.data.get("download_from").getAsString());
                Path path = Paths.get(Main.data.get("download_to").getAsString()).toAbsolutePath();
                Files.createDirectories(path);
                label.setText("Downloading");
                Files.copy(url.openStream(), path.resolve(".zip"), StandardCopyOption.REPLACE_EXISTING);

                ZipFile file = new ZipFile(path.resolve(".zip").toString());

                file.setRunInThread(true);

                ProgressMonitor progressMonitor = file.getProgressMonitor();

                file.extractAll(path.toString());
                progressBar.setMax(100);
                while (progressMonitor.getState() == ProgressMonitor.State.BUSY) {
                    progressBar.setValue(progressMonitor.getPercentDone());
                    label.setText("Unzipping (" + progressMonitor.getPercentDone() + "%)");
                }

                label.setText("Creating Uninstaller - Downloading Source");
                Files.copy(new URL("https://github.com/installer-designed-ChoUI/UnInstaller/archive/refs/heads/master.zip").openStream(), path.resolve("uninstaller.zip"), StandardCopyOption.REPLACE_EXISTING);
                label.setText("Creating Uninstaller - Unzipping Source");
                ZipFile uninstaller = new ZipFile(path.resolve("uninstaller.zip").toString());
                uninstaller.extractAll(path.toString());
                StringBuilder builder = new StringBuilder();
                Path datajson = path.resolve("UnInstaller-master").resolve("src").resolve("main").resolve("resources").resolve("data.json");
                for (String s : Files.readAllLines(datajson)) {
                    builder.append(s).append("\n");
                }
                Files.write(datajson, builder.toString().replace("TARGET_DIRECTORY", path.toAbsolutePath().toString()).getBytes(StandardCharsets.UTF_8));

                label.setText("Creating Uninstaller - Building");
                Runtime.getRuntime().exec("cmd /c cd " + path.resolve("UnInstaller-master") + " && gradlew shadowJar").waitFor();

                label.setText("Creating Uninstaller - Moving");
                Files.copy(path.resolve("UnInstaller-master").resolve("build").resolve("libs").resolve("UnInstaller-1.0-SNAPSHOT-all.jar"), path.resolve("uninstaller.jar"), StandardCopyOption.REPLACE_EXISTING);

                Files.delete(path.resolve(".zip"));
                window.setPage(new CompletePage(window));
            } catch (IOException | InterruptedException e) {
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
