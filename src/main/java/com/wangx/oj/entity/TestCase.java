package com.wangx.oj.entity;

import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("t_testcase")
public class TestCase implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    String id;
    String input;
    String output;
}
