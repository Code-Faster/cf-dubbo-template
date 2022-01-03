import { FILE_SUFFIX, getPackageName, TemplateTools } from "../index";

/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
const getParamVariableFormat = (str: string) => {
  return str.charAt(0).toLowerCase() + str.substring(1);
};
export default function (
  project: CodeFaster.Project,
  params: CodeFaster.Params
) {
  /**
   * 检验参数是否正常
   */
  if (params.props.pojo == undefined || params.props.pojo == "") {
    throw Error("pojo 必传");
  }
  if (params.props.vo == undefined || params.props.vo == "") {
    throw Error("vo 必传");
  }
  if (params.model == undefined) {
    throw Error("model 必传");
  }
  /**
   * 根据传递的参数生成template需要的参数
   */
  const pojo = params.props.pojo;
  const vo = params.props.vo;
  const voVariable = getParamVariableFormat(vo);

  const author = project.owner;
  /**
   * 获取模版工具类
   */
  const tools = new TemplateTools(project);

  const now = new Date();
  const serviceName = pojo + "Service";

  return (
    `
    package ${getPackageName(params.releasePath, "com")};
    
    import ${tools.getPackageNameByFileName("MybatisDaoImpl" + FILE_SUFFIX)};
    import ${tools.getPackageNameByFileName(pojo + FILE_SUFFIX)};
    import ${tools.getPackageNameByFileName(vo + FILE_SUFFIX)};
    import ${tools.getPackageNameByFileName("BusinessException" + FILE_SUFFIX)};
    import ${tools.getPackageNameByFileName("Grid" + FILE_SUFFIX)};
    import ${tools.getPackageNameByFileName("DataUtils" + FILE_SUFFIX)};
    import ${tools.getPackageNameByFileName("PropertyUtils" + FILE_SUFFIX)};
    import ${tools.getPackageNameByFileName("PageParameter" + FILE_SUFFIX)};
    import ${tools.getPackageNameByFileName(serviceName + FILE_SUFFIX)};
    
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
