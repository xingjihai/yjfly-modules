package com.yjfly.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractCacheProxy implements ICacheProxy {

    protected String region;
    protected long TTL = 0;  //剩余生存时间 (time to live)

    /**
     * @param region 缓存域
     * @param TTL 剩余生存时间(秒) (time to live)   0为不设置生存时间
     */
    public AbstractCacheProxy(String region, long TTL){
        this.region=region;
        this.TTL=TTL;
    }

    /** 拼接
     */
    protected String getKeyName(String key, String...groups){
        for (int i = 0; i < groups.length; i++) {
        }
        StringBuffer nameStr=new StringBuffer();
        for (int i = 0; i < groups.length; i++) {
            if(i==0){
                nameStr.append( groups[i] );
            }else{
                nameStr.append(":"+groups[i] );
            }
        }

        if(key!=null&& !"".equals(key.trim())){
            nameStr.append(":"+key );
        }

        return nameStr.toString();
    }


    /**
     * 根据组获取组下所有的key      -- 性能有点低,考虑优化
     * @param group
     * @return
     */
    protected List<String> getKeyGroup(String group){
        List<String> allKeys=keys();

        List<String> keyGroup=new ArrayList<>();
        for (String key : allKeys) {
            if(key.contains( group+":" )){
                keyGroup.add( key );
            }
        }
        return keyGroup;
    }

    /**
     *获取所有组的相同key
     * @return
     */
    protected List<String> getAllKey(String keyName){
        List<String> allKeys=keys();

        List<String> keyGroup=new ArrayList<>();
        for (String key : allKeys) {
            if(key.contains( ":"+keyName )){
                keyGroup.add( key );
            }
        }
        return keyGroup;
    }

    /** 内部实现方法 根据keys批量获取Map */
    protected abstract <T> Map<String, T> get(Collection<String> keys);
}
