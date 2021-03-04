package pro.inmost.vacancydiary.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

public class JsonReaderUtil {

    public static String getJsonFieldValue(String filePath, String fieldName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(filePath);

        InputStream inputStream = classPathResource.getInputStream();
        File file = File.createTempFile("temp", ".json");

        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        JsonNode json = JsonLoader.fromFile(file);

        return json.get(fieldName).textValue();
    }
}
