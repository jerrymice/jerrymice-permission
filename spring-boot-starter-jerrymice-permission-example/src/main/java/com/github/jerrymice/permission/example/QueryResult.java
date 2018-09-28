package com.github.jerrymice.permission.example;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tumingjian
 * 说明:
 */
@Data
@Accessors(chain = true)
public class QueryResult {
    private String id;
    private String ordernum;
    private String amount;
    private String status;

}
