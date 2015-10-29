/**
 * 
 */
package com.suneee.base.cache.remote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
/**
 * redis缓存
 * ClassName: RedisClient <br/>  
 * date: 2014年10月9日 下午2:02:25 <br/>  
 * @author Forint  
 * @version v1.0
 */
@Repository
public class RedisClient {

	@Autowired
	private ShardedJedisPool shardedJedisPool;
	
	public ShardedJedis newJedis() {
		return shardedJedisPool.getResource();
	}

	public void disconnected(ShardedJedis shardedJedis) {
		shardedJedisPool.returnResource(shardedJedis);
	}


}
