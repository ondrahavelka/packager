package cz.interview.exam.check.packager.model;

import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public final class PackageFile {

    private final FileType fileType;
    private final Path path;

    public PackageFile(String pathname, FileType fileType) {
        this.fileType = fileType;
        this.path = Paths.get(pathname);
    }


}
