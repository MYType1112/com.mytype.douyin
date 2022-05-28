package com.mytype.douyin.until;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.IplImage;

/**
 * 获取视频缩略图
 * @author liuyazhuang
 *
 */
public class VideoImage {

    private static final String IMAGEMAT = "png";
    private static final String ROTATE = "rotate";

    /**
     * 默认截取视频的中间帧为封面
     */
    public static final int MOD = 2;

    public static void main(String[] args) throws Exception {
        String path = "/D:/work/workspace/douyin/target/classes" +
                "/static/uploadVideo/4a60f6221b8d49aba1e1178b1ac4862f.mp4";
        path = path.substring(1);
        System.out.println(randomGrabberFFmpegImage(path, 2));
    }

    /**
     * 获取视频缩略图
     * @param filePath：视频路径
     * @param mod：视频长度/mod获取第几帧
     * @throws Exception
     */
    public static String randomGrabberFFmpegImage(String filePath, int mod) throws Exception {
        String targetFilePath = "";
        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
        ff.start();
        String rotate = ff.getVideoMetadata(ROTATE);
        int ffLength = ff.getLengthInFrames();
        Frame f;
        int i = 0;
//        int index = ffLength / mod;
        int index = 1;
        while (i < ffLength) {
            f = ff.grabImage();
            if(i == index){
                if (null != rotate && rotate.length() > 1) {
                    OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
                    IplImage src = converter.convert(f);
                    f = converter.convert(rotate(src, Integer.parseInt(rotate)));
                }
                targetFilePath = getImagePath(filePath, i);
                doExecuteFrame(f, targetFilePath);
                break;
            }
            i++;
        }
        ff.stop();
        return targetFilePath;
    }

    /**
     * 根据视频路径生成缩略图存放路径
     * @param filePath：视频路径
     * @param index：第几帧
     * @return：缩略图的存放路径
     */
    private static String getImagePath(String filePath, int index){
        if(filePath.contains(".") && filePath.lastIndexOf(".") < filePath.length() - 1){
            filePath = filePath.substring(0, filePath.lastIndexOf(".")).concat("_").concat(String.valueOf(index)).concat(".").concat(IMAGEMAT);
        }
        return filePath;
    }

    /**
     * 旋转图片
     * @param src
     * @param angle
     * @return
     */
    public static IplImage rotate(IplImage src, int angle) {
        IplImage img = IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
        opencv_core.cvTranspose(src, img);
        opencv_core.cvFlip(img, img, angle);
        return img;
    }

    /**
     * 截取缩略图
     * @param f
     * @param targerFilePath:封面图片
     */
    public static void doExecuteFrame(Frame f, String targerFilePath) {
        if (null == f || null == f.image) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bi = converter.getBufferedImage(f);
        File output = new File(targerFilePath);
        try {
            ImageIO.write(bi, IMAGEMAT, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据视频长度随机生成随机数集合
     * @param baseNum:基础数字，此处为视频长度
     * @param length：随机数集合长度
     * @return:随机数集合
     */
    public static List<Integer> random(int baseNum, int length) {
        List<Integer> list = new ArrayList<Integer>(length);
        while (list.size() < length) {
            Integer next = (int) (Math.random() * baseNum);
            if (list.contains(next)) {
                continue;
            }
            list.add(next);
        }
        Collections.sort(list);
        return list;
    }
}