package org.farm2.files.domain.ex;

import lombok.Data;

import java.io.File;
import java.nio.file.Path;

@Data
public class FarmFilePath {
    private Path basepath;

    private Path relativePath;

    private String resourceKey;

    private String fileName;

    public File getFile() {
        return basepath.resolve(relativePath).resolve(fileName).toFile();
    }

    public Path getFullPath() {
        return basepath.resolve(relativePath).resolve(fileName);
    }
}
