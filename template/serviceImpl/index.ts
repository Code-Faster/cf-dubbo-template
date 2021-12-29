/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
const getParamVariableFormat = (str: string) => {
  return str.charAt(0).toLowerCase() + str.substring(1);
};
export default function (
  extendsPath: string,
  releasePath: string,
  pojo: string,
  vo: string,
  pojoName: string,
  author: string,
  getPackageNameByFileName: (args: string) => string,
  getPackageName: (path: string, filter: string) => string
) {
  const now = new Date();
  const voVariable = getParamVariableFormat(vo);
  const serviceName = pojoName + "Service";
  return (
    `
    package ${getPackageName(releasePath, "com")};
    
    import ${getPackageNameByFileName("MybatisDaoImpl.java")};
    import ${getPackageNameByFileName(pojo + ".java")};
    import ${getPackageNameByFileName(vo + ".java")};
    import ${getPackageNameByFileName("BusinessException.java")};
    import ${getPackageNameByFileName("Grid.java")};
    import ${getPackageNameByFileName("DataUtils.java")};
    import ${getPackageNameByFileName("PropertyUtils.java")};
    import ${getPackageNameByFileName("PageParameter.java")};
    import ${getPackageName(extendsPath, "com")};
    
    import com.alibaba.dubbo.config.annotation.Service;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.Set;
    
    /**
     * ${serviceName + "服务实现类"}
     * @author: ${author}
     * @date: ${now}
     * @version V\${app.service.version}
     */
    @Service(version = "` +
    "${app.service.version}" +
    `", retries = -1, timeout = 6000)
    public class ${
      serviceName + "Impl"
    } extends MybatisDaoImpl implements ${serviceName} {
    
        public final String className = "${pojo}";
        
        /**
         * 保存
         * @param ${voVariable}
         * @author: ${author}
         */
        @Override
        public void save${pojo} (${vo} ${voVariable}){
            ${pojo} ${getParamVariableFormat(pojo)} = new ${pojo}();
            DataUtils.copyPropertiesIgnoreNull(${voVariable}, ${getParamVariableFormat(
      pojo
    )});
            super.save(${getParamVariableFormat(pojo)});
        }
        
        /**
         * 更新 
         * @param ${voVariable}
         * @author: ${author}
         */
        @Override
        public void update${pojo} (${vo} ${voVariable}){
            ${pojo} dbObj = this.findById(${voVariable}.getId());
            if (dbObj == null) {
                throw new BusinessException("数据不存在");
            }
            DataUtils.copyPropertiesIgnoreNull(${voVariable}, dbObj);
            super.update(dbObj);
        }
    
        /**
         * 根据ID查询详情
         * @param id
         * @author: ${author}
         */
        @Override
        public ${pojo} findById(Long id) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("column", PropertyUtils.getPropertyNames(${pojo}.class));
            return this.baseDao.selectOne(className + ".findById", map);
        }
    
        /**
         * 根据ID查询详情
         * @param id
         * @author: ${author}
         */
        @Override
        public ${vo} findVOById(Long id){
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("column", PropertyUtils.getPropertyNames(${pojo}.class));
            return this.baseDao.selectOne(className + ".findVOById", map);
        }
    
        /**
         * 根据ids 查询
         * @param ids id集合
         * @author: ${author}
         */
        @Override
        public List<${pojo}> findByIds(Set<Long> ids) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ids", ids);
            map.put("column", PropertyUtils.getPropertyNames(${pojo}.class));
            return this.baseDao.selectList(className + ".findByIds", map);
        }
    
        /**
         * 根据ID删除数据
         * @param id
         * @author: ${author}
         */
        @Override
        public void deleteById(Long id) {
          ${pojo} dbObj = this.findById(id);
          if (dbObj == null) {
              throw new BusinessException("数据不存在");
          }
          this.delete(dbObj);
        }
    
        /**
         * 分页查询
         * @param ${voVariable}
         * @author: ${author}
         */
        @Override
        public Grid<${vo}> find${pojo}Page (${vo} ${voVariable}){
          ${voVariable}.setColumn(PropertyUtils.getPropertyNames(${pojo}.class));
          ${voVariable}.setPageParameter(new PageParameter(${voVariable}.getPage(), ${voVariable}.getRows()));
          Grid<${vo}> grid = new Grid<>();
          List<${vo}> list = super.baseDao.selectList(className + ".find${pojo}Page", ${voVariable});
          grid.setCount(Long.valueOf(${voVariable}.getPageParameter().getTotalCount()));
          grid.setList(list);
          return grid;
        }
    }
    `
  );
}
