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

    @GetMapping("queryPersonalTaskByUserId/{userId}")
    public String queryPersonalTaskByUserId(@PathVariable("userId") Long userId){

        return "taskcontent";
    }

    @GetMapping("queryGroupByUserId/{userId}")
    public String queryGroupByUserId(@PathVariable("userId") Long userId, ModelMap modelMap){
        List<GroupInfo> grouplist = taskService.queryGroupInfoById(groupId);

        //跳转到任务详情界面
        modelMap.put("grouplist", grouplist);
        return "groupcontent";
    }

    @GetMapping("queryTaskByGroupId/{groupId}")
    public String queryTaskByGroupId()

    @PostMapping("join/{userId}/{taskId}")
    public ResultBean joinTask(@PathVariable("userId")Long userId, @PathVariable("taskId") Long taskId){

        return null;
    }

}
