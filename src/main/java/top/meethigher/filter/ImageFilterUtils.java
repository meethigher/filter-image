package top.meethigher.filter;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * 图片过滤
 *
 * @author chenchuancheng
 * @since 2023/4/7 15:51
 */
@Slf4j
public final class ImageFilterUtils {

    private static Scanner scanner = new Scanner(System.in);


    /**
     * @param dir      文件夹
     * @param ext      扩展名 如png/jpg/jpeg
     * @param fileSize 文件大小 eg 1KB 1MB 1B
     * @param open     是否自动打开
     */
    public static void filter(String dir, String ext, String fileSize, boolean open) throws Exception {
        long length = convertFileSizeToBytes(fileSize);
        List<String> list = new LinkedList<>();
        Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                File toFile = file.toFile();
                String fileName = toFile.getName();
                String[] split = fileName.split("\\.");
                if (split.length > 1 && ext.contains(split[split.length - 1]) && toFile.length() >= length) {
                    list.add(file.toString());
                }
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return super.postVisitDirectory(dir, exc);
            }
        });
        log.info("超过 {} 的 {} 文件有 {} 个", fileSize, ext, list.size());
        for (String s : list) {
            log.info(s);
            if (open) {
                System.out.print("是否要打开该文件? (y/n) ");
                char c = scanner.next().charAt(0);
                if (c == 'y') {
                    Desktop.getDesktop().open(new File(s));
                }
            }
        }
    }


    /**
     * @param fileSize 文件大小 eg 1KB 1MB 1B
     * @return 以byte为单位
     */
    private static long convertFileSizeToBytes(String fileSize) {
        fileSize = fileSize.toUpperCase(Locale.ROOT);
        long bytes;
        if (fileSize.contains("KB")) {
            bytes = Long.parseLong(fileSize.replace("KB", "").trim()) * 1024;
        } else if (fileSize.contains("MB")) {
            bytes = Long.parseLong(fileSize.replace("MB", "").trim()) * 1024 * 1024;
        } else {
            bytes = Long.parseLong(fileSize.replace("B", "").trim());
        }
        return bytes;
    }
}
