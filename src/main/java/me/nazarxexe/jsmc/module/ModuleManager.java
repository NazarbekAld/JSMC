package me.nazarxexe.jsmc.module;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.bukkit.plugin.Plugin;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    public final static int CENTRAL_ID = 664824347;

    private static ModuleManager instance;

    private final Logger logger = LoggerFactory.getLogger("JSMC-ModuleManager");

    public final List<Module> cachedModules = new ArrayList<>();
    private Plugin plugin;

    public static ModuleManager getInstance() {
        if (instance == null) {
            instance = new ModuleManager();
        }
        return instance;
    }

    public void init(Plugin plugin) {
        this.plugin = plugin;

        logger.info("Getting known modules...");

        Gson gson = new Gson();
        String data = null;
        try {
            try (InputStream stream = GitHub.connectAnonymously().getRepositoryById(CENTRAL_ID)
                    .getFileContent("knownModules.json")
                    .read()) {
                data = new String(stream.readAllBytes());
            }
        } catch (IOException e) {
            logger.error("Failed to download known modules.", e);
            return;
        }

        List<Module> modules = gson.fromJson(data, new TypeToken<ArrayList<Module>>() {
        });

        cachedModules.addAll(modules);

    }

    public Module getKnownModuleByName(String name) {
        Module module = null;

        for (Module cachedModule : cachedModules) {
            if (!(cachedModule.name.equals(name))) continue;
            module = cachedModule;
        }
        return module;
    }

    public void importAllTo(File to, Module module, String versionTag) {
        try {
            Path path = null;
            String tag = versionTag;
            if (tag == null) {
                tag = "";
            }
            if (tag.isEmpty()) {
                path = download(GitHub.connectAnonymously().getRepositoryById(module.repoID)
                        .getLatestRelease().getZipballUrl(), to.getAbsolutePath());
            } else {
                path = download(GitHub.connectAnonymously().getRepositoryById(module.repoID)
                        .getReleaseByTagName(tag).getZipballUrl(), to.getAbsolutePath());
            }
            ZipFile zipFile = new ZipFile(path.toFile());

            List<FileHeader> fileHeaders = zipFile.getFileHeaders();

            int skip = 1;
            String skipDir = "";
            File toMod = new File(to, module.name);
            for (FileHeader fileHeader : fileHeaders) {

                if (skip > 0) {
                    skipDir = fileHeader.getFileName();
                    skip--;
                    continue;
                }
                fileHeader.setFileName(fileHeader.getFileName().replaceFirst("^" + skipDir, ""));

                zipFile.extractFile(fileHeader, toMod.getAbsolutePath());
            }

            path.toFile().delete();
        } catch (IOException e) {
            logger.error("Failed to import module(s)!", e);
        }
    }

    private Path download(String sourceURL, String targetDirectory) throws IOException {
        URL url = new URL(sourceURL);
        String fileName = sourceURL.substring(sourceURL.lastIndexOf('/') + 1, sourceURL.length());
        Path targetPath = new File(targetDirectory + File.separator + fileName).toPath();
        Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetPath;
    }


}
