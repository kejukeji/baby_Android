
package com.keju.baby;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Properties;

/**
 * 配置类
 * @author Zhoujun
 */
public class Configuration {
    private static Properties defaultProperty;
    private static boolean DALVIK;
    
    static {
        init();
    }

    static void init() {
        defaultProperty = new Properties();
        defaultProperty.setProperty("elephant.debug", "false");//是否打印调试信息
        defaultProperty.setProperty("elephant4j.http.useSSL", "false");
        defaultProperty.setProperty("elephant4j.user","");
        defaultProperty.setProperty("elephant4j.password","");
        defaultProperty.setProperty("elephant.http.connectionTimeout", "20000");//网络连接超时时间限制
        defaultProperty.setProperty("elephant.http.readTimeout", "120000");//网络读取时间限制
        defaultProperty.setProperty("elephant.http.retryCount", "3");//重试次数
        defaultProperty.setProperty("elephant.http.retryIntervalSecs", "10");//重试间隔时间
        defaultProperty.setProperty("elephant.clientVersion", "0.1");// 客户端版本
        defaultProperty.setProperty("elephant.serverVersion", "0.1");// 服务器端版本
        try {
            // Android platform should have dalvik.system.VMRuntime in the classpath.
            // @see http://developer.android.com/reference/dalvik/system/VMRuntime.html
            Class.forName("dalvik.system.VMRuntime");
            defaultProperty.setProperty("elephant.dalvik", "true");
        } catch (ClassNotFoundException cnfe) {
            defaultProperty.setProperty("elephant.dalvik", "false");
        }
        DALVIK = getBoolean("elephant.dalvik");
        String t4jProps = "elephant.properties";
        boolean loaded = loadProperties(defaultProperty, "." + File.separatorChar + t4jProps) ||
                loadProperties(defaultProperty, Configuration.class.getResourceAsStream("/WEB-INF/" + t4jProps)) ||
                loadProperties(defaultProperty, Configuration.class.getResourceAsStream("/" + t4jProps));//初始化属性信息
    }

    private static boolean loadProperties(Properties props, String path) {
        try {
            File file = new File(path);
            if(file.exists() && file.isFile()){
                props.load(new FileInputStream(file));
                return true;
            }
        } catch (Exception ignore) {
        }
        return false;
    }

    private static boolean loadProperties(Properties props, InputStream is) {
        try {
            props.load(is);
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }

    public static boolean isDalvik() {
        return DALVIK;
    }
    public static boolean useSSL() {
        return getBoolean("elephant4j.http.useSSL");
    }
    public static String getScheme(){
        return useSSL() ? "https://" : "http://";
    }
    public static String getUser() {
        return getProperty("elephant4j.user");
    }

    public static String getUser(String userId) {
        return getProperty("elephant4j.user", userId);
    }

    public static String getPassword() {
        return getProperty("elephant4j.password");
    }

    public static String getPassword(String password) {
        return getProperty("elephant4j.password", password);
    }
    
    public static String getCilentVersion() {
        return getProperty("elephant.clientVersion");
    }

    public static String getCilentVersion(String clientVersion) {
        return getProperty("elephant.clientVersion", clientVersion);
    }
    
    public static String getServerVersion() {
        return getProperty("elephant.serverVersion");
    }

    public static String getServerVersion(String serverVersion) {
        return getProperty("elephant.serverVersion", serverVersion);
    }

    public static int getConnectionTimeout() {
        return getIntProperty("elephant.http.connectionTimeout");
    }

    public static int getConnectionTimeout(int connectionTimeout) {
        return getIntProperty("elephant.http.connectionTimeout", connectionTimeout);
    }

    public static int getReadTimeout() {
        return getIntProperty("elephant.http.readTimeout");
    }

    public static int getReadTimeout(int readTimeout) {
        return getIntProperty("elephant.http.readTimeout", readTimeout);
    }
    
    public static int getRetryCount() {
        return getIntProperty("elephant.http.retryCount");
    }

    public static int getRetryCount(int retryCount) {
        return getIntProperty("elephant.http.retryCount", retryCount);
    }

    public static int getRetryIntervalSecs() {
        return getIntProperty("elephant.http.retryIntervalSecs");
    }

    public static int getRetryIntervalSecs(int retryIntervalSecs) {
        return getIntProperty("elephant.http.retryIntervalSecs", retryIntervalSecs);
    }

    public static boolean getBoolean(String name) {
        String value = getProperty(name);
        return Boolean.valueOf(value);
    }

    public static int getIntProperty(String name) {
        String value = getProperty(name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public static int getIntProperty(String name, int fallbackValue) {
        String value = getProperty(name, String.valueOf(fallbackValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public static long getLongProperty(String name) {
        String value = getProperty(name);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public static String getProperty(String name) {
        return getProperty(name, null);
    }

    public static String getProperty(String name, String fallbackValue) {
        String value;
        try {
            value = System.getProperty(name, fallbackValue);
            if (null == value) {
                value = defaultProperty.getProperty(name);
            }
            if (null == value) {
                String fallback = defaultProperty.getProperty(name + ".fallback");
                if (null != fallback) {
                    value = System.getProperty(fallback);
                }
            }
        } catch (AccessControlException ace) {
            // Unsigned applet cannot access System properties
            value = fallbackValue;
        }
        return replace(value);
    }

    private static String replace(String value) {
        if (null == value) {
            return value;
        }
        String newValue = value;
        int openBrace = 0;
        if (-1 != (openBrace = value.indexOf("{", openBrace))) {
            int closeBrace = value.indexOf("}", openBrace);
            if (closeBrace > (openBrace + 1)) {
                String name = value.substring(openBrace + 1, closeBrace);
                if (name.length() > 0) {
                    newValue = value.substring(0, openBrace) + getProperty(name)
                            + value.substring(closeBrace + 1);

                }
            }
        }
        if (newValue.equals(value)) {
            return value;
        } else {
            return replace(newValue);
        }
    }

    public static boolean getDebug() {
        return getBoolean("elephant.debug");
    }
}
