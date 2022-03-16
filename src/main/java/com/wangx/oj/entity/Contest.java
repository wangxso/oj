package com.wangx.oj.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_contest")
public class Contest {
    @TableId
    private String cid;
    private String title;
    private Date startDate;
    private Date endDate;
    private Date createDate;
    private String tags;
    private String problems;
}
