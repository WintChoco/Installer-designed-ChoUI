package installer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import installer.page.TitlePage;
import me.wincho.choui.rendersystem.CPage;
import me.wincho.choui.rendersystem.CWindow;
import me.wincho.choui.widget.dialog.MessageDialog;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    public static JsonObject data;

    public static void main(String[] args) {
        try {
            loadProgramData();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        if (data == null) {
            return;
        }
        CWindow window = new CWindow(data.get("name").getAsString() + " " + data.get("version").getAsString() + " Setup");
        window.setResizable(false);
        TitlePage page = new TitlePage(window);
        window.setPage(page);
        window.setSize(700, 500);
        window.show();
    }

    private static void loadProgramData() throws URISyntaxException, IOException {
        data = JsonParser.parseReader(Files.newBufferedReader(Paths.get(Objects.requireNonNull(Main.class.getResource("/data.json")).toURI()))).getAsJsonObject();
    }

    public static void cancel(CPage page) {
        page.openDialog(new MessageDialog(target1 -> {
            page.closeDialog();
        }, "No", target1 -> {
            System.exit(0);
        }, "Yes"));
    }
}
