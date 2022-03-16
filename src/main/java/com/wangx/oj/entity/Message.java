package com.wangx.oj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Message implements Serializable {
    private String id;
    private Integer result;
    private String timeCost;
    private String memoryCost;
    private String error;
}
