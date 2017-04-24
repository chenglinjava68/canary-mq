package com.lanmo.canary.mq.common;

import com.google.common.base.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc 读取配置文件的基础类
 * @date 2017/4/24
 */
public class CanaryConfig {

    private Properties props = new Properties();

    public CanaryConfig(String configPath) throws IOException {
        InputStream is = CanaryConfig.class.getClassLoader().getResourceAsStream(configPath);
        props.load(is);
    }

    public CanaryConfig(Properties props) {
        this.props = props;
    }

    public int getInt(String key, int defaultValue) {
        String value = props.getProperty(key);
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = props.getProperty(key);
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public long getLong(String key, long defaultValue) {
        String value = props.getProperty(key);
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return Long.parseLong(value);
    }

    public float getFloat(String key, float defaultValue) {
        String value = props.getProperty(key);
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return Float.parseFloat(value);
    }

    public double getDouble(String key, double defaultValue) {
        String value = props.getProperty(key);
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return Double.parseDouble(value);
    }

    public String getString(String key, String defaultValue) {
        String value = props.getProperty(key);
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        }
        return value;
    }


}
