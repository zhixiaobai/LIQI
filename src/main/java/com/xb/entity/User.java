package com.xb.entity;

import com.zxb.liqi.annotation.TableField;
import com.zxb.liqi.annotation.TableId;
import com.zxb.liqi.annotation.TableName;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description
 */
@TableName("dm")
public class User {
    private String username;
    private String password;
    @TableId("id")
    private int id;
    @TableField("dm_name")
    private String dmName;

    @TableField(exist = false)
    private String a;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDmName() {
        return dmName;
    }

    public void setDmName(String dmName) {
        this.dmName = dmName;
    }
}
