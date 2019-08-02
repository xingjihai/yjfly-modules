package cn.ejfy.web.tools.properties;

import cn.ejfy.web.tools.context.SpringContextHolder;

/**
 * settings.properties 文件中的参数都从这边获取,便于查询到调用到配置参数的地方，利于维护。
 */
public class Settings {
    
    /**
     * 获取上传文件所在的磁盘路径
     */
    public static String getUpload_disk() {
        String upload_disk= SpringContextHolder.getBean(PropertyUtils.class).getPropertiesString("upload_disk");
        return upload_disk;
    }
    /**
     * 获取上传文件的访问路径(配合tomcat的Context设置访问 upload_disk 路径)
     */
    public static String getStatics() {
        String statics=SpringContextHolder.getBean(PropertyUtils.class).getPropertiesString("statics");
        return statics;
    }
    
}
