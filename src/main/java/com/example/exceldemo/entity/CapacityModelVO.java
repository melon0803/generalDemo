package com.example.exceldemo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

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
public class CapacityModelVO {
    /** 设备编号 */
    private String deviceID;

    /** 单日集合 */
    private List<Integer> dayList;

    /** 计划产量集合 */
    private List<Long> planList;

    /** 实际产量集合 */
    private List<Long> actualList;
}
