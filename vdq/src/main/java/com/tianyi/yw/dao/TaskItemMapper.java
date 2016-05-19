package com.tianyi.yw.dao;

import com.tianyi.yw.model.TaskItem;
@MyBatisRepository
public interface TaskItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskItem record);

    int insertSelective(TaskItem record);

    TaskItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskItem record);

    int updateByPrimaryKey(TaskItem record);
}