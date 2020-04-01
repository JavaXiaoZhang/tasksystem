package com.zq.feign;

import com.zq.commons.constant.ResultBeanConstant;
import com.zq.commons.pojo.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ZQ
 * @Date 2020/4/1
 */
@FeignClient(name = "task-management",fallback = ITaskService.TaskServiceFallback.class)
public interface ITaskService {
    @GetMapping("task/getUserListByTaskInfoId/{taskInfoId}")
    ResultBean getUserListByTaskInfoId(@PathVariable("taskInfoId") Long taskInfoId);

    @Component
    class TaskServiceFallback implements ITaskService{

        @Override
        public ResultBean getUserListByTaskInfoId(Long taskInfoId) {
            return new ResultBean(ResultBeanConstant.ERROR,"getUserListByTaskInfoId进入熔断处理逻辑");
        }
    }
}
