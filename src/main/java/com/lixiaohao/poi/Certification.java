package com.lixiaohao.poi;

import java.util.Arrays;

/**
 * Created by lixiaohao on 2017/2/8
 *
 * @Description
 * @Create 2017-02-08 13:14
 * @Company
 */
public class Certification {
    private String name;

    private byte[] img;

    private String imgId;

    public Certification() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    @Override
    public String toString() {
        return "Certification{" +
                "name='" + name + '\'' +
                ", img=" + Arrays.toString(img) +
                ", imgId='" + imgId + '\'' +
                '}';
    }
}
