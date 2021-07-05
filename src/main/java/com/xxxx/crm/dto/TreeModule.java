package com.xxxx.crm.dto;

/**
 *DTO（Data Transfer Object）：数据传输对象
 * TreeModule-->资源树,该类模拟资源信息数据表
 */
public class TreeModule {
    private Integer id;
    private String name;
    private Integer  pId;
    //Ztree的是否回显的标志
    private Boolean checked=false;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public TreeModule() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }


}
