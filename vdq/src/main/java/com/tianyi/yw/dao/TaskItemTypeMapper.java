package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.TaskItemType;
@MyBatisRepository
public interface TaskItemTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskItemType record);

    int insertSelective(TaskItemType record);

    TaskItemType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskItemType record);

    int updateByPrimaryKey(TaskItemType record);
    
    List<TaskItemType> getTaskItemTypeList(TaskItemType taskItemType);
    

}