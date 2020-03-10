package com.zq.controller;

import com.zq.commons.pojo.ResultBean;
import com.zq.entity.TTask;
import com.zq.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZQ
 * @Date 2020/2/29
 */
@Controller
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
    public String queryTaskInfoByTaskId(@PathVariable("taskId") Long taskId, ModelMap modelMap){
        //查询出taskInfo的list
        List<TaskInfo> list = taskService.queryTaskInfoByTaskId(taskId);
        //跳转到任务详情界面
        modelMap.put("list",list);
        return "taskinfo";
    }

    @GetMapping("queryTask/{userId}")
    public ResultBean queryTaskByUserId(@PathVariable("userId") Long userId){

        return null;
    }

    @GetMapping("queryGroupInfoById/{id}")
    public String queryGroupInfoById(@PathVariable("id") Long groupId, ModelMap modelMap){
        List<GroupInfo> list = taskService.queryGroupInfoById(groupId);
        //TODO 查询出groupId下的taskList,并set到GroupInfo对象中

        //跳转到任务详情界面
        modelMap.put("list",list);
        return "groupinfo";
    }

    @PostMapping("join/{userId}/{taskId}")
    public ResultBean joinTask(@PathVariable("userId")Long userId, @PathVariable("taskId") Long taskId){

        return null;
    }

}
