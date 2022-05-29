package com.mytype.douyin;

import com.mytype.douyin.dao.LoginTicketMapper;
import com.mytype.douyin.dao.UserMapper;
import com.mytype.douyin.dao.VideoMapper;
import com.mytype.douyin.entity.LoginTicket;
import com.mytype.douyin.entity.User;
import com.mytype.douyin.entity.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DouyinApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(1);
        System.out.println(user);

        user = userMapper.selectByName("mytest2");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("mytest4");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setFollowCount(0);
        user.setFollowerCount(0);
        user.setCreateTime(new Date());
        user.setAvatar("http://www.nowcoder.com/101.png");
        user.setSignature("大家好！");
//        user.setBackgroundImage("");


        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getUserId());
    }

    @Test
    public void updateUser() {

        int rows = userMapper.updateHeader(1, "http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(2, "hello");
        System.out.println(rows);
    }

    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }

//    @Test
    public void testInsertVideo() {
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            Video video = new Video();
            video.setPlayUrl("http://47.98.196.156:8080/static/10_1653225565_VID_20220504_201616.mp4");
            video.setCoverUrl("http://47.98.196.156:8080/static/cover/10_1653225565_cover.jpg");
            video.setAuthorId(random.nextInt(10));
            video.setFavoriteCount(0);
            video.setCommentCount(0);
            video.setUploadTime(new Date());
            video.setTitle("测试"+i);

            int rows = videoMapper.insertVideo(video);
            System.out.println(rows);
            System.out.println(video.getId());
        }
    }

    @Test
    public void testSelectVideos() {
//        1653795200 1653795462
//        1653795155411
        long currentTime = new Date().getTime();
        Date date = new Date(1653724875000L);//新建一个时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//你要转换成的时间格式,大小写不要变
        String Time = sdf.format(date);//转换你的时间
        List<Video> videos = videoMapper.selectVideos(0, Time,0, 2);
        for (Video video : videos){
            System.out.println(video.getUploadTime());
        }
        long time = videos.get(videos.size() - 1).getUploadTime().getTime();
        System.out.println(time);
    }

}
