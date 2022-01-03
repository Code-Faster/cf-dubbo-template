package com.createTemplate.api.core.doubbo.service;

import com.createTemplate.api.base.dao.MybatisDao;
import com.createTemplate.model.core.pojo.TAccountDetail;
import com.createTemplate.model.core.vo.TAccountDetailVO;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.Grid;

import java.util.List;
import java.util.Set;

public interface TAccountDetailService extends MybatisDao {
    /**
     * 保存
     *
     * @param newTAccountDetailOne
     * @author: 李必琪
     */
    void saveTAccountDetail(TAccountDetail newTAccountDetailOne) throws BusinessException;

    /**
     * 更新
     *
     * @param tAccountDetailVO
     * @author: 李必琪
     */
    void updateTAccountDetail(TAccountDetailVO tAccountDetailVO) throws BusinessException;

    /**
     * 根据ID查询pojo
     *
     * @param id
     * @author: 李必琪
     */
    TAccountDetail findById(Long id) throws BusinessException;

    /**
     * 根据ID查询vo
     *
     * @param id
     * @author: 李必琪
     */
    TAccountDetailVO findVOById(Long id) throws BusinessException;

    /**
     * 根据ID集合查询
     *
     * @param ids
     * @author: 李必琪
     */
    List<TAccountDetail> findByIds(Set<Long> ids) throws BusinessException;

    /**
     * 根据ID删除数据
     *
     * @param id
     * @author: 李必琪
     */
    void deleteById(Long id) throws BusinessException;

    /**
     * 分页查询
     *
     * @param tAccountDetailVO
     * @return
     * @author: 李必琪
     */
    Grid<TAccountDetailVO> findTAccountDetailPage(TAccountDetailVO tAccountDetailVO) throws BusinessException;
}