package com.example.exceldemo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 *
 * @author Carmelo
 * @date 2018/8/14
 * @since 1.0.0
 */
@ToString
@Getter
@Setter
@Accessors(chain = true)
public class CapacityDO {
    /** 主键ID */
    private Long id;

    /** 设备编号 */
    private String deviceID;

    /** 年 */
    private Integer year;

    /** 月 */
    private Integer month;

    /** 日 */
    private Integer day;

    /** 计划产量 */
    private Long planCapacity;

    /** 实际产量 */
    private Long actualCapacity;
}
