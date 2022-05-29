package com.mytype.douyin.service;

import com.mytype.douyin.entity.User;
import com.mytype.douyin.entity.Video;
import com.mytype.douyin.until.CommunityConstant;
import com.mytype.douyin.until.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LikeService implements CommunityConstant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private VideoService videoService;

    // 点赞
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                String favoriteKey = RedisKeyUtil.getFavoriteKey(userId, entityType);

                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);

                operations.multi();

                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForZSet().remove(favoriteKey, entityId);
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForZSet().add(favoriteKey, entityId, System.currentTimeMillis());
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });
    }

    // 查询某用户点赞的实体的数量
    public long findFavoriteCount(int userId, int entityType) {
        String favoriteKey = RedisKeyUtil.getFavoriteKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(favoriteKey);
    }

    // 查询某用户点赞的视频
    public List<Video> findFavorite(int userId, int offset, int limit) {
        String favoriteKey = RedisKeyUtil.getFavoriteKey(userId, ENTITY_TYPE_VIDEO);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(favoriteKey, offset, offset + limit - 1);

        if (targetIds == null) {
            return null;
        }

        List<Video> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Video video = videoService.findVideoById(targetId);
            if(video!=null){
                list.add(video);
            }
        }

        return list;
    }

    // 查询某实体点赞的数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    // 查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    // 查询某个用户获得的赞
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count;
    }

}
