package com.zetta.zScheduler.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zetta.common.utils.zLogger;
import com.zetta.zScheduler.model.schedulerVO;

@Service
public class schedulerSvc {
	@Autowired
	@Resource(name="sqlSession")
	private SqlSession sqlSession;	
	
	public zLogger logger = new zLogger(getClass());
	
    public boolean insertscheduler(String objectId, String type, String json){
    	schedulerVO param = new schedulerVO();
    	param.setOBJECT_ID(objectId);
    	param.setOBJECT_TYPE(type);
    	param.setOBJECT_JSON(json);
    	
    	//먼저 삭제해준다.
    	sqlSession.delete("deleteschedulerOne", param);
    	
    	int result = sqlSession.insert("insertscheduler", param);
    	logger.info("insertscheduler result:" + result);
    	return true;
    }

    public schedulerVO selectschedulerOne(String objectId) throws Exception {
    	schedulerVO param = new schedulerVO();
    	param.setOBJECT_ID(objectId);
    	return sqlSession.selectOne("selectschedulerOne", param);
    }
    
    public List<schedulerVO> selectschedulerList() throws Exception {
    	return sqlSession.selectList("selectschedulerList");
    }
    
    public void deleteObjectJsonOne(schedulerVO param) throws Exception {
    	sqlSession.delete("deleteschedulerOne", param);
    }
}
