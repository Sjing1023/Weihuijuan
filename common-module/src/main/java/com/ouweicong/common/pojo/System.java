package com.ouweicong.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;

import java.text.SimpleDateFormat;
import java.util.Date;

public class System {

    private Date create_time;

    private Date update_time;
    private int sort;
    @TableField(exist = false)
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getCreate_time() {
        if (create_time != null) {
            return simpleDateFormat.format(create_time);
        }
        return null;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        if (update_time != null) {
            return simpleDateFormat.format(update_time);
        }
        return null;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
