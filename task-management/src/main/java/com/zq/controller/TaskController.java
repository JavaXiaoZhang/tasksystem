package com.zq.controller;

import com.zq.commons.pojo.ResultBean;
import com.zq.entity.Group;
import com.zq.entity.Task;
import com.zq.entity.TaskInfo;
import com.zq.entity.User;
import com.zq.service.IGroupService;
import com.zq.service.ITaskInfoService;
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

    @Autowired
    private ITaskInfoService taskInfoService;

    @Autowired
    private IGroupService groupService;

    //TODO 注入userService

    @GetMapping("index/{userId}")
    public String index(@PathVariable("userId") Long userId, ModelMap modelMap){
        modelMap.put("userId",userId);
        return "index";
    }

    @GetMapping("querytask.html")
    public String queryTask(){
        return "querytask";
    }


    /**
     * 添加任务
     * @param name
     * @param desc
     * @param userId
     * @return
     */
    @PostMapping("insertPTaskWithReturn/{name}/{desc}/{type}/{userId}")
    @ResponseBody
    public Long insertPTaskWithReturn(@PathVariable("name") String name, @PathVariable("desc") String desc,
                                      @PathVariable("type") String type, @PathVariable("userId") Long userId){
        return taskService.insertPTaskWithReturn(name, desc, type, userId);
    }

    @PostMapping("insertGroupWithReturn/{name}/{userId}")
    @ResponseBody
    public Long insertGroupWithReturn(@PathVariable("name") String name, @PathVariable("userId") Long userId){
        return groupService.insertGroupWithReturn(name,userId);
    }

    @PostMapping("modify")
    public ResultBean modifyTask(Task task){

        return null;
    }

    @DeleteMapping("delPersonalTask/{userId}/{taskId}")
    public String deleteTask(@PathVariable("userId") Long userId, @PathVariable("taskId") Long taskId){
        taskService.deleteTask(userId, taskId);
        return "redirect:task/queryPersonalTaskByUserId/"+userId;
    }

    /**
     * 根据用户id查询用户的个人任务
     * @param userId
     * @return
     */
    @GetMapping("queryPersonalTaskByUserId/{userId}")
    @ResponseBody
    public List<Task> queryPersonalTaskByUserId(@PathVariable("userId") Long userId){
        List<Task> personalTaskList = taskService.queryPersonalTaskByUserId(userId);
        //返回list
        return personalTaskList;
    }

    /**
     * 根据groupId查询组任务
     * @param groupId
     * @param modelMap
     * @return
     */
    /*@GetMapping("queryGroupTaskById/{groupId}")
    public String queryGroupTaskById(@PathVariable("groupId") Long groupId, ModelMap modelMap){
        List<Task> groupTaskList = taskService.queryGroupTaskById(groupId);
        //跳转到组任务展示界面
        modelMap.put("groupTaskList",groupTaskList);
        return "grouptaskcontent";
    }*/

    /**
     * 根据任务id查询个人任务详情
     * @param taskId
     * @param modelMap
     * @return 跳转到任务详情界面
     */
    @GetMapping("queryPersonalTaskInfoById/{taskId}")
    public String queryPersonalTaskInfoById(@PathVariable("taskId") Long taskId, ModelMap modelMap){
        //查询出TaskInfo的list
        List<TaskInfo> personalTaskInfoList = taskInfoService.queryPersonalTaskInfoById(taskId);
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
    @GetMapping("queryGroupTaskInfoById/{taskId}")
    public String queryGroupTaskInfoById(@PathVariable("taskId") Long taskId, ModelMap modelMap){
        //查询出TaskInfo的list
        List<TaskInfo> groupTaskInfoList = taskInfoService.queryGroupTaskInfoById(taskId);
        //跳转到组任务详情界面
        modelMap.put("groupTaskInfoList",groupTaskInfoList);
        return "groupTaskInfo";
    }

    /**
     * 根据用户id查询对应group
     * @param userId
     * @return
     */
    @GetMapping("queryGroupByUserId/{userId}")
    @ResponseBody
    public List<Group> queryGroupByUserId(@PathVariable("userId") Long userId){
        List<Group> groups = groupService.queryGroupByUserId(userId);
        for (Group group : groups){
            //查询groupId对应的团队任务
            group.setTaskList(taskService.queryGroupTaskByGroupId(group.getId()));
        }
        return groups;
    }

    /**
     * 根据groupId查询组内用户
     * @param groupId
     * @param modelMap
     * @return
     */
    /*@GetMapping("queryGroupInfoById/{groupId}")
    public String queryGroupInfoById(@PathVariable("groupId") Long groupId, ModelMap modelMap){
        List<User> userList = userService.queryUserByGroupId(groupId);
        //跳转到group详情界面
        modelMap.put("userList", userList);
        return "groupinfo";
    }*/

}
