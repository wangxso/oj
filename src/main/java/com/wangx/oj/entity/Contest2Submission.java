package com.wangx.oj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_contest_submission")
public class Contest2Submission {
    @TableId(value = "sid",  type = IdType.INPUT)
    private String sid;
    private String cid;
    private String uid;
}
