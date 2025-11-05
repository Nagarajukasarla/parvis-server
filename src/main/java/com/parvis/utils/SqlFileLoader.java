package com.parvis.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SqlFileLoader {
    /**
     * Loads an SQL file from the classpath and returns its content as a String.
     *
     * @param filePath The relative path to the SQL file from the classpath (e.g., "sql/find-by-email-and-password.sql").
     * @return The content of the SQL file as a String.
     * @throws IOException If the file cannot be read.
     */
    public static String loadSqlFromFile(String filePath) throws IOException, URISyntaxException {
        System.out.println("[Step 1] Input filePath: " + filePath);

        // Step 2: Get the ClassLoader
        ClassLoader classLoader = SqlFileLoader.class.getClassLoader();
        System.out.println("[Step 2] ClassLoader: " + classLoader);

        // Step 3: Try to get resource URL
        URL resourceUrl = classLoader.getResource(filePath);
        System.out.println("[Step 3] Resource URL: " + resourceUrl);

        if (resourceUrl == null) {
            throw new FileNotFoundException("SQL file not found in classpath: " + filePath);
        }

        // Step 4: Convert URL to URI
        URI resourceUri = resourceUrl.toURI();
        System.out.println("[Step 4] Resource URI: " + resourceUri);

        // Step 5: Convert URI to Path
        Path path = Paths.get(resourceUri);
        System.out.println("[Step 5] Path: " + path.toAbsolutePath());

        // Step 6: Read file content
        String sql = Files.readString(path);
        System.out.println("[Step 6] SQL Loaded:\n" + sql);

        return sql;
    }

}
