package com.zq.controller;

import com.zq.commons.pojo.ResultBean;
import com.zq.entity.TTask;
import com.zq.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZQ
 * @Date 2020/2/29
 */
@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    /**
     * 添加任务
     * @param task
     * @return
     */
    @PostMapping("add")
    public ResultBean addTask(TTask task){

        return null;
    }

    @PostMapping("modify")
    public ResultBean modifyTask(TTask task){

        return null;
    }

    @GetMapping("query/{taskId}")
    public ResultBean queryTaskByTaskId(@PathVariable("taskId") Long taskId){

        return null;
    }

    @GetMapping("query/{userId}")
    public ResultBean queryTaskByUserId(@PathVariable("userId") Long userId){

        return null;
    }

    @PostMapping("join/{userId}/{taskId}")
    public ResultBean joinTask(@PathVariable("userId")Long userId, @PathVariable("taskId") Long taskId){

        return null;
    }

}
