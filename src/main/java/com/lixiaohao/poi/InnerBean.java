package com.lixiaohao.poi;

/**
 * Created by lixiaohao on 2017/3/9
 *
 * @Description
 * @Create 2017-03-09 14:29
 * @Company
 */
public class InnerBean {
    private String dataSource;
    private String modelClassName;
    private String name;
    private String contact;

    public InnerBean(String dataSource, String modelClassName, String name, String contact) {
        this.dataSource = dataSource;
        this.modelClassName = modelClassName;
        this.name = name;
        this.contact = contact;
    }

    public InnerBean() {
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getModelClassName() {
        return modelClassName;
    }

    public void setModelClassName(String modelClassName) {
        this.modelClassName = modelClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "InnerBean{" +
                "dataSource='" + dataSource + '\'' +
                ", modelClassName='" + modelClassName + '\'' +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
