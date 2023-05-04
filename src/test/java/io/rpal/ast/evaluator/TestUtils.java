package io.rpal.ast.evaluator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtils {
    private static final Path resourceDir = Path.of("src", "test", "resources");

    public static File getASTFile(String filename) throws IOException {
        return new File(resourceDir.resolve("astFiles").resolve(filename+".txt").toString());
    }

    public static String getSTContent(String filename) throws IOException {
        return readContent(resourceDir.resolve("expectedSTs").resolve(filename+".txt"));
    }

    private static String readContent(Path path) throws IOException {
        Stream<String> lines = Files.lines(path);
        String output = lines.collect(Collectors.joining(System.lineSeparator()));
        lines.close();
        return output.replaceAll(System.lineSeparator(), "\n");
    }
}
