package com.createTemplate.provider.core.dubbo.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.createTemplate.api.core.doubbo.service.TAccountDetailService;
import com.createTemplate.api.util.DataUtils;
import com.createTemplate.api.util.PropertyUtils;
import com.createTemplate.model.core.pojo.TAccountDetail;
import com.createTemplate.model.core.vo.TAccountDetailVO;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.mybatis.page.PageParameter;
import com.createTemplate.model.resultvo.Grid;
import com.createTemplate.provider.base.dao.impl.MybatisDaoImpl;

import java.util.*;

/**
 * @ClassName TAccountDetailServiceImpl
 * @Author libiqi
 * @Description TODO
 * @Date 2019/11/6 3:49 下午
 * @Version 1.0
 */
@Service(version = "${app.service.version}", retries = -1, timeout = 6000)
public class TAccountDetailServiceImpl extends MybatisDaoImpl implements TAccountDetailService {

    public final String className = "TAccountDetail";

    /**
     * 保存
     *
     * @param tAccountDetail
     * @author: 李必琪
     */
    @Override
    public void saveTAccountDetail(TAccountDetail tAccountDetail) {
        tAccountDetail.setId(null);
        tAccountDetail.setInputDate(new Date());
        if(tAccountDetail.getStatus()==null) {
            tAccountDetail.setStatus(TAccountDetail.STATUS_NORMAL);
        }
        this.save(tAccountDetail);
    }

    /**
     * 更新
     *
     * @param tAccountDetailVO
     * @author: 李必琪
     */
    @Override
    public void updateTAccountDetail(TAccountDetailVO tAccountDetailVO) {
        TAccountDetail dbObj = this.findById(tAccountDetailVO.getId());
        if (dbObj == null) {
            throw new BusinessException("数据不存在");
        }
        DataUtils.copyPropertiesIgnoreNull(tAccountDetailVO, dbObj);
        super.update(dbObj);
    }

    /**
     * 根据ID查询详情
     *
     * @param id
     * @author: 李必琪
     */
    @Override
    public TAccountDetail findById(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("column", PropertyUtils.getPropertyNames(TAccountDetail.class));
        return this.baseDao.selectOne(className + ".findById", map);
    }

    /**
     * 根据ID查询详情
     *
     * @param id
     * @author: 李必琪
     */
    @Override
    public TAccountDetailVO findVOById(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("column", PropertyUtils.getPropertyNames(TAccountDetail.class));
        return this.baseDao.selectOne(className + ".findVOById", map);
    }

    /**
     * 根据ids 查询
     *
     * @param ids id集合
     * @author: 李必琪
     */
    @Override
    public List<TAccountDetail> findByIds(Set<Long> ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        map.put("column", PropertyUtils.getPropertyNames(TAccountDetail.class));
        return this.baseDao.selectList(className + ".findByIds", map);
    }

    /**
     * 根据ID删除数据
     *
     * @param id
     * @author: 李必琪
     */
    @Override
    public void deleteById(Long id) {
        TAccountDetail dbObj = this.findById(id);
        if (dbObj == null) {
            throw new BusinessException("数据不存在");
        }
        this.delete(dbObj);
    }

    /**
     * 分页查询
     *
     * @param tAccountDetailVO
     * @author: 李必琪
     */
    @Override
    public Grid<TAccountDetailVO> findTAccountDetailPage(TAccountDetailVO tAccountDetailVO) {
        tAccountDetailVO.setColumn(PropertyUtils.getPropertyNames(TAccountDetail.class));
        tAccountDetailVO.setPageParameter(new PageParameter(tAccountDetailVO.getPage(), tAccountDetailVO.getRows()));
        Grid<TAccountDetailVO> grid = new Grid<>();
        List<TAccountDetailVO> list = super.baseDao.selectList(className + ".findTAccountDetailPage", tAccountDetailVO);
        grid.setCount(Long.valueOf(tAccountDetailVO.getPageParameter().getTotalCount()));
        grid.setList(list);
        return grid;
    }
}