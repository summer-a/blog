package com.hjb.blog.controller.common;

import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: StatusController
 * @projectName blog
 * @description: TODO
 * @date 2019/11/20 11:48
 */
@RestController
@RequestMapping("/status")
public class StatusController {

    private Runtime runtime = Runtime.getRuntime();

    @GetMapping("/memory")
    public JSONObject showMemoryStatus() {
        JSONObject memoryStatus = new JSONObject();

        long maxMemory = runtime.maxMemory();
        long freeMemory = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        memoryStatus.put("freeMemory", bitToMb(freeMemory));
        memoryStatus.put("totalMemory", bitToMb(totalMemory));
        memoryStatus.put("maxMemory", bitToMb(maxMemory));
        memoryStatus.put("actualFreeMemory", bitToMb(maxMemory - totalMemory + freeMemory));

        return memoryStatus;
    }

    /**
     * bit转mb单位
     *
     * @param bit
     * @return
     */
    private String bitToMb(long bit) {
        return bit / (1024 * 1024) + "M";
    }
}
