package codeexamples;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public static Properties load(final String filename) throws IOException {

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        Properties properties = new Properties();

        if (is != null) {
            properties.load(is);
        } else {
            throw new FileNotFoundException("Property file not found in the classpath");
        }

        return properties;
    }

}
