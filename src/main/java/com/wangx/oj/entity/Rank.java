package com.wangx.oj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_rank")
public class Rank {
    @TableId
    private Integer id;
    private String uid;
    private Long total;
    private Integer pass;
    @TableField(exist = false)
    private Integer level;
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String acRate;
}
