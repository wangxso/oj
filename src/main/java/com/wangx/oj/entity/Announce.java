package com.wangx.oj.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_announce")
public class Announce implements Serializable {
    @TableId
    private Integer aid;
    private String uid;
    private String title;
    private String contentHtml;
    private String contentMd;
    private Date createTime;
    private Date updateTime;
}
