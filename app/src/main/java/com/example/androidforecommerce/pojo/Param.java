package com.example.androidforecommerce.pojo;

public class Param {
    private int id;
    private int parent_id;
    private String name;
    private boolean status;
    private int sort_order;
    private int level;
    private String created;
    private String update;

    private boolean pressed=false;

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public Param(){ }

    public Param(int id,int parent_id,String name,boolean status,int sort_order,int level,String created,String update){
        this.id=id;
        this.parent_id=parent_id;
        this.name=name;
        this.status=status;
        this.sort_order=sort_order;
        this.level=level;
        this.created=created;
        this.update=update;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public int getLevel() {
        return level;
    }

    public int getSort_order() {
        return sort_order;
    }

    public String getCreated() {
        return created;
    }

    public String getUpdate() {
        return update;
    }

    public boolean getStatus(){ return status; }
}
