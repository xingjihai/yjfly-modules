package net.oschina.j2cache.redis;

import redis.clients.jedis.*;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NullJedisCommands implements BinaryJedisCommands,MultiKeyCommands {
    @Override
    public String set(byte[] key, byte[] value) {
        return null;
    }

    @Override
    public String set(byte[] key, byte[] value, byte[] nxxx) {
        return null;
    }

    @Override
    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
        return null;
    }

    @Override
    public byte[] get(byte[] key) {
        return new byte[0];
    }

    @Override
    public Boolean exists(byte[] key) {
        return null;
    }

    @Override
    public Long persist(byte[] key) {
        return null;
    }

    @Override
    public String type(byte[] key) {
        return null;
    }

    @Override
    public Long expire(byte[] key, int seconds) {
        return null;
    }

    @Override
    public Long pexpire(String key, long milliseconds) {
        return null;
    }

    @Override
    public Long pexpire(byte[] key, long milliseconds) {
        return null;
    }

    @Override
    public Long expireAt(byte[] key, long unixTime) {
        return null;
    }

    @Override
    public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
        return null;
    }

    @Override
    public Long ttl(byte[] key) {
        return null;
    }

    @Override
    public Boolean setbit(byte[] key, long offset, boolean value) {
        return null;
    }

    @Override
    public Boolean setbit(byte[] key, long offset, byte[] value) {
        return null;
    }

    @Override
    public Boolean getbit(byte[] key, long offset) {
        return null;
    }

    @Override
    public Long setrange(byte[] key, long offset, byte[] value) {
        return null;
    }

    @Override
    public byte[] getrange(byte[] key, long startOffset, long endOffset) {
        return new byte[0];
    }

    @Override
    public byte[] getSet(byte[] key, byte[] value) {
        return new byte[0];
    }

    @Override
    public Long setnx(byte[] key, byte[] value) {
        return null;
    }

    @Override
    public String setex(byte[] key, int seconds, byte[] value) {
        return null;
    }

    @Override
    public Long decrBy(byte[] key, long integer) {
        return null;
    }

    @Override
    public Long decr(byte[] key) {
        return null;
    }

    @Override
    public Long incrBy(byte[] key, long integer) {
        return null;
    }

    @Override
    public Double incrByFloat(byte[] key, double value) {
        return null;
    }

    @Override
    public Long incr(byte[] key) {
        return null;
    }

    @Override
    public Long append(byte[] key, byte[] value) {
        return null;
    }

    @Override
    public byte[] substr(byte[] key, int start, int end) {
        return new byte[0];
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        return null;
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        return new byte[0];
    }

    @Override
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        return null;
    }

    @Override
    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        return null;
    }

    @Override
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        return null;
    }

    @Override
    public Long hincrBy(byte[] key, byte[] field, long value) {
        return null;
    }

    @Override
    public Double hincrByFloat(byte[] key, byte[] field, double value) {
        return null;
    }

    @Override
    public Boolean hexists(byte[] key, byte[] field) {
        return null;
    }

    @Override
    public Long hdel(byte[] key, byte[]... field) {
        return null;
    }

    @Override
    public Long hlen(byte[] key) {
        return null;
    }

    @Override
    public Set<byte[]> hkeys(byte[] key) {
        return null;
    }

    @Override
    public Collection<byte[]> hvals(byte[] key) {
        return null;
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return null;
    }

    @Override
    public Long rpush(byte[] key, byte[]... args) {
        return null;
    }

    @Override
    public Long lpush(byte[] key, byte[]... args) {
        return null;
    }

    @Override
    public Long llen(byte[] key) {
        return null;
    }

    @Override
    public List<byte[]> lrange(byte[] key, long start, long end) {
        return null;
    }

    @Override
    public String ltrim(byte[] key, long start, long end) {
        return null;
    }

    @Override
    public byte[] lindex(byte[] key, long index) {
        return new byte[0];
    }

    @Override
    public String lset(byte[] key, long index, byte[] value) {
        return null;
    }

    @Override
    public Long lrem(byte[] key, long count, byte[] value) {
        return null;
    }

    @Override
    public byte[] lpop(byte[] key) {
        return new byte[0];
    }

    @Override
    public byte[] rpop(byte[] key) {
        return new byte[0];
    }

    @Override
    public Long sadd(byte[] key, byte[]... member) {
        return null;
    }

    @Override
    public Set<byte[]> smembers(byte[] key) {
        return null;
    }

    @Override
    public Long srem(byte[] key, byte[]... member) {
        return null;
    }

    @Override
    public byte[] spop(byte[] key) {
        return new byte[0];
    }

    @Override
    public Set<byte[]> spop(byte[] key, long count) {
        return null;
    }

    @Override
    public Long scard(byte[] key) {
        return null;
    }

    @Override
    public Boolean sismember(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public byte[] srandmember(byte[] key) {
        return new byte[0];
    }

    @Override
    public List<byte[]> srandmember(byte[] key, int count) {
        return null;
    }

    @Override
    public Long strlen(byte[] key) {
        return null;
    }

    @Override
    public Long zadd(byte[] key, double score, byte[] member) {
        return null;
    }

    @Override
    public Long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
        return null;
    }

    @Override
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        return null;
    }

    @Override
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
        return null;
    }

    @Override
    public Set<byte[]> zrange(byte[] key, long start, long end) {
        return null;
    }

    @Override
    public Long zrem(byte[] key, byte[]... member) {
        return null;
    }

    @Override
    public Double zincrby(byte[] key, double score, byte[] member) {
        return null;
    }

    @Override
    public Double zincrby(byte[] key, double score, byte[] member, ZIncrByParams params) {
        return null;
    }

    @Override
    public Long zrank(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public Long zrevrank(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrange(byte[] key, long start, long end) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long end) {
        return null;
    }

    @Override
    public Long zcard(byte[] key) {
        return null;
    }

    @Override
    public Double zscore(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public List<byte[]> sort(byte[] key) {
        return null;
    }

    @Override
    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        return null;
    }

    @Override
    public Long zcount(byte[] key, double min, double max) {
        return null;
    }

    @Override
    public Long zcount(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return null;
    }

    @Override
    public Long zremrangeByRank(byte[] key, long start, long end) {
        return null;
    }

    @Override
    public Long zremrangeByScore(byte[] key, double start, double end) {
        return null;
    }

    @Override
    public Long zremrangeByScore(byte[] key, byte[] start, byte[] end) {
        return null;
    }

    @Override
    public Long zlexcount(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
        return null;
    }

    @Override
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return null;
    }

    @Override
    public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        return null;
    }

    @Override
    public Long linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot, byte[] value) {
        return null;
    }

    @Override
    public Long lpushx(byte[] key, byte[]... arg) {
        return null;
    }

    @Override
    public Long rpushx(byte[] key, byte[]... arg) {
        return null;
    }

    @Override
    public List<byte[]> blpop(byte[] arg) {
        return null;
    }

    @Override
    public List<byte[]> brpop(byte[] arg) {
        return null;
    }

    @Override
    public Long del(byte[] key) {
        return null;
    }

    @Override
    public byte[] echo(byte[] arg) {
        return new byte[0];
    }

    @Override
    public Long move(byte[] key, int dbIndex) {
        return null;
    }

    @Override
    public Long bitcount(byte[] key) {
        return null;
    }

    @Override
    public Long bitcount(byte[] key, long start, long end) {
        return null;
    }

    @Override
    public Long pfadd(byte[] key, byte[]... elements) {
        return null;
    }

    @Override
    public long pfcount(byte[] key) {
        return 0;
    }

    @Override
    public Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        return null;
    }

    @Override
    public Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        return null;
    }

    @Override
    public Double geodist(byte[] key, byte[] member1, byte[] member2) {
        return null;
    }

    @Override
    public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        return null;
    }

    @Override
    public List<byte[]> geohash(byte[] key, byte[]... members) {
        return null;
    }

    @Override
    public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
        return null;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
        return null;
    }

    @Override
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
        return null;
    }

    @Override
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
        return null;
    }

    @Override
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
        return null;
    }

    @Override
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
        return null;
    }

    @Override
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
        return null;
    }

    @Override
    public List<byte[]> bitfield(byte[] key, byte[]... arguments) {
        return null;
    }

    @Override
    public Long del(String... keys) {
        return null;
    }

    @Override
    public Long exists(String... keys) {
        return null;
    }

    @Override
    public List<String> blpop(int timeout, String... keys) {
        return null;
    }

    @Override
    public List<String> brpop(int timeout, String... keys) {
        return null;
    }

    @Override
    public List<String> blpop(String... args) {
        return null;
    }

    @Override
    public List<String> brpop(String... args) {
        return null;
    }

    @Override
    public Set<String> keys(String pattern) {
        return null;
    }

    @Override
    public List<String> mget(String... keys) {
        return null;
    }

    @Override
    public String mset(String... keysvalues) {
        return null;
    }

    @Override
    public Long msetnx(String... keysvalues) {
        return null;
    }

    @Override
    public String rename(String oldkey, String newkey) {
        return null;
    }

    @Override
    public Long renamenx(String oldkey, String newkey) {
        return null;
    }

    @Override
    public String rpoplpush(String srckey, String dstkey) {
        return null;
    }

    @Override
    public Set<String> sdiff(String... keys) {
        return null;
    }

    @Override
    public Long sdiffstore(String dstkey, String... keys) {
        return null;
    }

    @Override
    public Set<String> sinter(String... keys) {
        return null;
    }

    @Override
    public Long sinterstore(String dstkey, String... keys) {
        return null;
    }

    @Override
    public Long smove(String srckey, String dstkey, String member) {
        return null;
    }

    @Override
    public Long sort(String key, SortingParams sortingParameters, String dstkey) {
        return null;
    }

    @Override
    public Long sort(String key, String dstkey) {
        return null;
    }

    @Override
    public Set<String> sunion(String... keys) {
        return null;
    }

    @Override
    public Long sunionstore(String dstkey, String... keys) {
        return null;
    }

    @Override
    public String watch(String... keys) {
        return null;
    }

    @Override
    public String unwatch() {
        return null;
    }

    @Override
    public Long zinterstore(String dstkey, String... sets) {
        return null;
    }

    @Override
    public Long zinterstore(String dstkey, ZParams params, String... sets) {
        return null;
    }

    @Override
    public Long zunionstore(String dstkey, String... sets) {
        return null;
    }

    @Override
    public Long zunionstore(String dstkey, ZParams params, String... sets) {
        return null;
    }

    @Override
    public String brpoplpush(String source, String destination, int timeout) {
        return null;
    }

    @Override
    public Long publish(String channel, String message) {
        return null;
    }

    @Override
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {

    }

    @Override
    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {

    }

    @Override
    public String randomKey() {
        return null;
    }

    @Override
    public Long bitop(BitOP op, String destKey, String... srcKeys) {
        return null;
    }

    @Override
    public ScanResult<String> scan(int cursor) {
        return null;
    }

    @Override
    public ScanResult<String> scan(String cursor) {
        return null;
    }

    @Override
    public ScanResult<String> scan(String cursor, ScanParams params) {
        return null;
    }

    @Override
    public String pfmerge(String destkey, String... sourcekeys) {
        return null;
    }

    @Override
    public long pfcount(String... keys) {
        return 0;
    }
}
