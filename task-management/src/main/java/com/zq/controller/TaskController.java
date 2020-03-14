package com.zq.controller;

import com.zq.commons.pojo.ResultBean;
import com.zq.entity.Task;
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

    //TODO TaskInfoService  groupService

    /**
     * 添加任务
     * @param task
     * @return
     */
    @PostMapping("add")
    public ResultBean addTask(Task task){

        return null;
    }

    @PostMapping("modify")
    public ResultBean modifyTask(Task task){

        return null;
    }

    /**
     * 根据用户id查询用户的个人任务
     * @param userId
     * @return 跳转到个人任务展示界面
     */
    @GetMapping("queryPersonalTaskByUserId/{userId}")
    public String queryPersonalTaskByUserId(@PathVariable("userId") Long userId, ModelMap modelMap){
        List<Task> personalTaskList = taskService.queryPersonalTaskByUserId(userId);
        //跳转到个人任务展示界面
        modelMap.put("personalTaskList",personalTaskList);
        return "personaltaskcontent";
    }

    /**
     * 根据groupId查询组任务
     * @param groupId
     * @param modelMap
     * @return
     */
    @GetMapping("queryGroupTaskById/{id}")
    public String queryGroupTaskById(@PathVariable("id") Long groupId, ModelMap modelMap){
        List<Task> groupTaskList = taskService.queryGroupTaskById(groupId);
        //跳转到组任务展示界面
        modelMap.put("groupTaskList",groupTaskList);
        return "grouptaskcontent";
    }

    /**
     * 根据任务id查询个人任务详情
     * @param taskId
     * @param modelMap
     * @return 跳转到任务详情界面
     */
    @GetMapping("queryPersonalTaskInfoById/{id}")
    public String queryPersonalTaskInfoById(@PathVariable("id") Long taskId, ModelMap modelMap){
        //查询出TaskInfo的list
        List<TaskInfo> personalTaskInfoList = TaskInfoService.queryPersonalTaskInfoById(taskId);
        //跳转到个人任务详情界面
        modelMap.put("personalTaskInfoList",personalTaskInfoList);
        return "personalTaskInfo";
    }

    /**
     * 根据taskId查询组任务详情
     * @param taskId
     * @param modelMap
     * @return
     */
    @GetMapping("queryGroupTaskInfoById/{id}")
    public String queryGroupTaskInfoById(@PathVariable("id") Long taskId, ModelMap modelMap){
        //查询出TaskInfo的list
        List<TaskInfo> groupTaskInfoList = TaskInfoService.queryGroupTaskInfoById(taskId);
        //跳转到组任务详情界面
        modelMap.put("groupTaskInfoList",groupTaskInfoList);
        return "groupTaskInfo";
    }

    /**
     * 根据用户id查询对应group
     * @param userId
     * @param modelMap
     * @return
     */
    @GetMapping("queryGroupByUserId/{userId}")
    public String queryGroupByUserId(@PathVariable("userId") Long userId, ModelMap modelMap){
        List<Group> groupList = groupService.queryGroupByUserId(userId);
        //跳转到group展示界面
        modelMap.put("groupList", groupList);
        return "groupcontent";
    }

    /**
     * 根据groupId查询group详情
     * @param groupId
     * @param modelMap
     * @return
     */
    @GetMapping("queryGroupInfoById/{id}")
    public String queryGroupInfoById(@PathVariable("id") Long groupId, ModelMap modelMap){
        List<GroupInfo> groupInfoList = groupService.queryGroupInfoById(groupId);
        //跳转到group详情界面
        modelMap.put("groupInfoList", groupInfoList);
        return "groupinfo";
    }

}
