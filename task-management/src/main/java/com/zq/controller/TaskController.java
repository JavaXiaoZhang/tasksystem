package com.zq.controller;

import com.zq.commons.pojo.ResultBean;
import com.zq.entity.Group;
import com.zq.entity.Task;
import com.zq.entity.TaskInfo;
import com.zq.service.IGroupService;
import com.zq.service.ITaskInfoService;
import com.zq.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("index/{userId}")
    public String index(@PathVariable("userId") Long userId, ModelMap modelMap){
        modelMap.put("userId",userId);
        return "index";
    }

    @GetMapping("url/{url}")
    public String url(@PathVariable String url){
        return url;
    }

    /**
     * 添加任务
     * @param name
     * @param desc
     * @param userId
     * @return
     */
    @PostMapping("insertPersonalTaskWithReturn/{name}/{desc}/{userId}")
    @ResponseBody
    public Long insertPersonalTaskWithReturn(@PathVariable("name") String name, @PathVariable("desc") String desc,
                                     @PathVariable("userId") Long userId){
        return taskService.insertTaskWithReturn(new Task(null,name,desc,"0",-1L,"",userId,null));
    }

    @PostMapping("insertGroupTaskWithReturn")
    @ResponseBody
    public void insertGroupTaskWithReturn(Task task){
        if (task.getName()!=null && !("").equals(task.getName().trim())) {
            taskService.insertTaskWithReturn(task);
        }
    }

    @PostMapping("insertGroupWithReturn/{name}/{userId}")
    @ResponseBody
    public Long insertGroupWithReturn(@PathVariable("name") String name, @PathVariable("userId") Long userId){
        return groupService.insertGroupWithReturn(name, userId);
    }

    @GetMapping("queryGroupTask/{userId}/{groupId}")
    @ResponseBody
    public Task queryGroupTask(@PathVariable Long userId, @PathVariable Long groupId){
        return taskService.queryGroupTask(userId,groupId);
    }

    @PutMapping("modifyGroupName/{groupId}/{groupName}/{userId}")
    @ResponseBody
    public void modifyGroupName(@PathVariable Long groupId, @PathVariable String groupName, @PathVariable Long userId){
        groupService.modifyGroupName(groupId, groupName, userId);
    }

    @GetMapping("delTask/{taskId}/{userId}")
    public String deleteTask(@PathVariable("userId") Long userId, @PathVariable("taskId") Long taskId){
        taskService.deleteTask(userId, taskId);
        return "redirect:/task/index/"+userId;
    }

    @PutMapping("delGroup/{groupId}/{userId}")
    @ResponseBody
    public void delGroup(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId){
        groupService.deleteGroup(userId, groupId);
    }

    /**
     * 根据用户id查询用户的个人任务
     * @param userId
     * @return
     */
    @GetMapping("queryPersonalTaskByUserId/{userId}")
    @ResponseBody
    public List<Task> queryPersonalTaskByUserId(@PathVariable("userId") Long userId){
        return taskService.queryPersonalTaskByUserId(userId);
    }

    /**
     * 根据任务id查询任务详情
     * @param taskId
     * @param userId
     * @param modelMap
     * @return 跳转到任务详情界面
     */
    @GetMapping("queryTaskInfoById/{taskId}/{isAdmin}/{userId}")
    public String queryTaskInfoById(@PathVariable("taskId") Long taskId, @PathVariable String isAdmin,
                                    @PathVariable String userId, ModelMap modelMap){
        //查询出TaskInfo的list
        List<TaskInfo> taskInfoList = taskInfoService.queryTaskInfoById(taskId);
        //添加userList属性，分类list
        List<TaskInfo> todoList = new ArrayList<>();
        List<TaskInfo> doingList = new ArrayList<>();
        List<TaskInfo> doneList = new ArrayList<>();
        List<TaskInfo> fileList = new ArrayList<>();
        for (TaskInfo taskInfo: taskInfoList) {
            if ("Todo".equals(taskInfo.getStatus())){
                todoList.add(taskInfo);
            }else if ("Doing".equals(taskInfo.getStatus())){
                doingList.add(taskInfo);
            }else if ("Done".equals(taskInfo.getStatus())){
                doneList.add(taskInfo);
            }else if ("File".equals(taskInfo.getStatus())){
                fileList.add(taskInfo);
            }
        }
        //跳转到个人任务详情界面
        modelMap.put("userId",userId);
        modelMap.put("taskId",taskId);
        modelMap.put("isAdmin",isAdmin);
        modelMap.put("todoList",todoList);
        modelMap.put("doingList",doingList);
        modelMap.put("doneList",doneList);
        modelMap.put("fileList",fileList);

        return "taskinfo";
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
    @GetMapping("queryGroupInfoByGroupId/{groupId}/{userId}")
    public String queryGroupInfoById(@PathVariable("groupId") Long groupId, @PathVariable Long userId, ModelMap modelMap){
        Group group = groupService.queryGroupInfoByGroupId(groupId,userId);
        //跳转到group详情界面
        modelMap.put("group", group);
        modelMap.put("userId",userId);
        return "groupinfo";
    }

    @PostMapping("addGroupUser/{groupId}/{username}/{updateUser}")
    @ResponseBody
    public void addGroupUser(@PathVariable Long groupId, @PathVariable String username, @PathVariable Long updateUser){
        groupService.addGroupUser(groupId,username,updateUser);
    }

    @PutMapping("modifyIsAdmin/{groupId}/{userId}/{updateUser}")
    @ResponseBody
    public void modifyIsAdmin(@PathVariable Long groupId, @PathVariable Long userId, @PathVariable Long updateUser){
        groupService.modifyIsAdmin(groupId, userId, updateUser);
    }

    @PutMapping("delUserById/{groupId}/{userId}/{updateUser}")
    @ResponseBody
    public void delUserById(@PathVariable Long groupId, @PathVariable Long userId, @PathVariable Long updateUser){
        groupService.delUserById(groupId, userId, updateUser);
    }

    @PostMapping("addTaskInfo")
    public String addTaskInfo(TaskInfo taskInfo){
        taskInfoService.insertSelective(taskInfo);
        return "redirect:/task/queryTaskInfoById/"+taskInfo.getTaskId()+"/"+taskInfo.getIsAdmin()+"/"+taskInfo.getUpdateUser();
    }
}
