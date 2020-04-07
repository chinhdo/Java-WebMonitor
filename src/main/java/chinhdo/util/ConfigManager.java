package chinhdo.util;

import java.util.List;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

/** Config support */
public class ConfigManager {
    /**
     * Constructor
     */
    public ConfigManager(final String configFile) {
        try {
            config = new XMLConfiguration(configFile);
        } catch (final ConfigurationException ex) {
            throw new RuntimeException("config.xml not found. See README.md for instructions.", ex);
        }
    }

    /** Gets a boolean value */
    public Boolean getBoolean(final String key) {
        return getBoolean(key, false);
    }

    /** Gets a boolean value */
    public Boolean getBoolean(final String key, final Boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    /** Get integer */
    public int getInt(final String key) {
        return config.getInt(key);
    }

    /** Returns list of matching objects */
    public List<Object> getList(final String key) {
        return config.getList(key);
    }

    /** Gets a string value */
    public String getString(final String key) {
        return config.getString(key);
    }

    private XMLConfiguration config;
}