package com.lixiaohao.poi;

/**
 * Created by lixiaohao on 2017/3/9
 *
 * @Description
 * @Create 2017-03-09 14:28
 * @Company
 */
public class OutBean {
    private String id;


    private String name;
    private InnerBean data;

    public OutBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InnerBean getData() {
        return data;
    }

    public void setData(InnerBean data) {
        this.data = data;
    }

    public OutBean(String id, String name, InnerBean data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    @Override
    public String toString() {
        return "OutBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", data=" + data +
                '}';
    }
}
