package com.yjfly.cache.j2Cache;

import com.yjfly.cache.AbstractCacheProxy;
import debugger.TimeGap;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import net.oschina.j2cache.J2CacheBuilder;
import net.oschina.j2cache.J2CacheConfig;

import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class J2CacheImpl extends AbstractCacheProxy {
    private static Logger logger = LoggerFactory.getLogger(J2CacheImpl.class);

    private CacheChannel cache;

    public J2CacheImpl(String region, Integer TTL){
        super(region,TTL);

        try {
            // 引入配置
            J2CacheConfig config = J2CacheConfig.initFromConfig("/j2cache.properties");
            // 生成 J2CacheBuilder
            J2CacheBuilder builder= J2CacheBuilder.init(config);
            this.cache = builder.getChannel();
        } catch (IOException e) {
            logger.error( "初始化j2cache配置错误",e );
        }
    }

    @Override
    public List<String> keys() {
        Collection<String> keys=cache.keys(this.region);  //性能可能极其低下，谨慎使用??
        if(keys!=null){
            return new ArrayList(keys);
        }else{
            return new ArrayList();
        }
    }


    @Override
    public <T> T get(String key, String...groups) {

        String keyName=this.getKeyName("",groups);

        CacheObject obj = cache.get(this.region, keyName); //从缓存读取数据
        debugInfo(obj);
        return (T)obj.getValue();
    }


    @Override
    public <T> Map<String, T> getMap(String...groups) {

        TimeGap timeGap = new TimeGap();
        timeGap.start("【J2Cache性能测试】 获取组"+groups[0]+"的时长");

        String groupName=this.getKeyName("",groups);
        List<String> keys=this.getKeyGroup(groupName);
        Map<String, T> map=this.get(keys);

        timeGap.end();
        timeGap.print();

        return  map;
    }

    @Override
    protected <T> Map<String, T> get(Collection<String> keys) {
        Map<String, T> cacheMap=null;
        Map<String, CacheObject> objs = cache.get( this.region, keys ); //从缓存读取数据
        for (String key : objs.keySet()) {
            if(cacheMap==null){
                cacheMap=new HashMap();
            }
            debugInfo( objs.get(key) );
            cacheMap.put( key, (T) ( objs.get(key).getValue() )  );
        }
        return cacheMap;
    }

    @Override
    public void put(String key, Object value, String...groups) {

        String keyName=this.getKeyName(key,groups);

        cache.set(this.region,keyName,value, TTL, true);
    }

    @Override
    public void put(Map<String, Object> elements) {
        cache.set(this.region,elements, TTL, true);
    }


    @Override
    public void removeGroup(String...groups) {
        String groupName=this.getKeyName("",groups);
        List<String> keys=this.getKeyGroup(groupName);
        cache.evict( this.region, keys.toArray(new String[keys.size()] ) );
    }

    @Override
    public void remove(String key, String...groups) {
        String keyName=this.getKeyName(key,groups);
        if(keyName!=null&& !"".equals(keyName.trim())){
            cache.evict( this.region, keyName);
        }
    }

    @Override
    public void removeKey(String key) {
        List<String> keys=this.getAllKey(key);
        if(keys!=null&&keys.size()>0){
            cache.evict( this.region, keys.toArray(new String[keys.size()] )  );
        }
    }


    @Override
    public boolean exists(String key) {
        return cache.exists( this.region,key );
    }

    @Override
    public void clear() {
        cache.clear( this.region );
    }


    private void debugInfo(CacheObject obj){
        try {
            String info= String.format( "获取缓存  [region >> %s,key >> %s,cacheLevel >> %d]=>value:%s", obj.getRegion(), obj.getKey(), obj.getLevel(), obj.getValue()  );
            logger.debug( info );
        } catch (Exception e) {
        }
    }
}
