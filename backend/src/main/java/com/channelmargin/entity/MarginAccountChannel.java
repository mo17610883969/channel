package com.channelmargin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("margin_account_channel")
public class MarginAccountChannel {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long marginAccountId;

    private Long channelId;

    private String code;

    private String name;

    private String cityArea;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}