package me.zpsw.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class SysProperties {


    private static Properties SysLocalPropObject = null;

    //配置文件路径
    private static String defaultPropFileName = "/conf/generator.properties";
    //文件更新标识
    protected long lastModifiedData = -1;

    private static SysProperties instance;

    public static SysProperties getInstance(){
        if(instance == null){
            instance = new SysProperties();
        }
        return instance;
    }

    /**
     * @description 私有构造器启动一个线程实时监控配置文件
     */
    private SysProperties() {
        SysLocalPropObject = new Properties();
        String tempPath = System.getProperty("user.dir")+File.separator+defaultPropFileName;
        File tempFile = new File(tempPath);
        final String filePath;
        if(tempFile.exists()) {
            filePath = tempPath;
        } else {
            filePath = "system.properties";
        }

        final SysProperties self = this;
        File propertyFile = new File(filePath);
        if (propertyFile.exists()) reloadFile(propertyFile);

        //循环监控配置文件的变化，一旦发现文件发生变化了则重读配置文件
        Thread t = new Thread(() -> {
            while (true) {
                //间隔1秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("监控配置文件发生错误:",e);
                }

                try {
                    File propertyFile1 = new File(filePath);
                    if (self.lastModifiedData != propertyFile1.lastModified()) {
                        if(self.lastModifiedData > 0) {
                            log.info("配置文件发生变化，重新加载");
                        }
                        self.reloadFile(propertyFile1);
                        self.lastModifiedData = propertyFile1.lastModified();
                    }
                } catch (Exception e) {
                    log.error("重新加载配置文件发生错误:",e);
                }
            }
        });
        t.start();
    }

    /**
     * 重新加载文件
     */
    private void reloadFile(File propertyFile) {
        FileInputStream inputStreamLocal = null;
        try {
            inputStreamLocal = new FileInputStream(propertyFile);
            SysLocalPropObject.load(inputStreamLocal);
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                log.info("No Local Properties File Found");
                SysLocalPropObject = null;
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                if (inputStreamLocal != null)
                    inputStreamLocal.close();
            } catch (IOException e) {
                log.info("Exception is happened when to close file stream");
            }
        }
    }


    /**
     * 根据key获取value
     */
    public String getProperty(String property) {
        String val = null;

        if (SysLocalPropObject != null)
            val = SysLocalPropObject.getProperty(property);

        return (val);

    }

    /**
     * 根据key获取value
     */
    public String getProperty(String property, String defaultValue) {
        String val = null;

        if (SysLocalPropObject != null) {
            val = SysLocalPropObject.getProperty(property, defaultValue);
        } else {
            val = defaultValue;
        }

        return (val);
    }

    /**
     * 根据key获取value
     */
    public Integer getIntProperty(String property) {
        String val = getProperty(property);
        Integer nVal = null;
        if (val != null) {
            try {
                nVal = Integer.parseInt(val);
            } catch (Exception e) {

            }
        }
        return nVal;

    }

    /**
     * 根据key获取value
     */
    public Integer getIntProperty(String property, Integer defaultValue) {
        Integer val = getIntProperty(property);

        if (val == null) {
            val = defaultValue;
        }

        return (val);
    }

    /**
     * 根据key获取value
     */
    public Long getLongProperty(String property) {
        String val = getProperty(property);
        Long nVal = null;
        try {
            nVal = Long.parseLong(val);
        } catch (Exception e) {

        }
        return nVal;

    }

    /**
     * 根据key获取value
     */
    public Long getLongProperty(String property, Long defaultValue) {
        Long val = getLongProperty(property);

        if (val == null) {
            val = defaultValue;
        }

        return (val);
    }

    /**
     * 根据key获取value
     */
    public boolean getBooleanProperty(String property, boolean defaultValue) {
        boolean retval = false;
        String val = getProperty(property);

        if (val == null || val.equals(""))
            retval = defaultValue;
        else if (val.trim().equalsIgnoreCase("true") || val.trim().equals("1"))
            retval = true;

        return (retval);
    }

    /**
     * 根据key获取value
     */
    public Double getDoubleProperty(String property) {
        String val = getProperty(property);
        Double nVal = null;
        try {
            nVal = Double.parseDouble(val);
        } catch (Exception e) {
        }
        return nVal;
    }


    /**
     * 根据key获取value
     */
    public Double getDoubleProperty(String property, Double defaultValue) {
        Double val = getDoubleProperty(property);
        if (val == null) {
            val = defaultValue;
        }
        return (val);
    }
}
