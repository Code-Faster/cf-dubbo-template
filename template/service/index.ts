/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
const getParamVariableFormat = (str: string) => {
  return str.charAt(0).toLowerCase() + str.substring(1);
};
export default function (
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
  return `
package ${getPackageName(releasePath, "com")};

import ${getPackageNameByFileName("MybatisDao.java")};
import ${getPackageNameByFileName(pojo + ".java")};
import ${getPackageNameByFileName(vo + ".java")};
import ${getPackageNameByFileName("BusinessException.java")};
import ${getPackageNameByFileName("Grid.java")};
import java.util.Set;
import java.util.List;

/**
 * ${serviceName + "服务"}
 * @author: ${author}
 * @date: ${now}
 * @version V\${app.service.version}
 */
public interface ${serviceName} extends MybatisDao {
    /**
     * 保存
     * @param ${voVariable}
     * @author: ${author}
     */
    void save${pojo} (${vo} ${voVariable})throws BusinessException;
    
    /**
     * 更新 
     * @param ${voVariable}
     * @author: ${author}
     */
    void update${pojo} (${vo} ${voVariable})throws BusinessException;

    /**
     * 根据ID查询pojo
     * @param id
     * @author: ${author}
     */
    ${pojo} findById(Long id) throws BusinessException;

    /**
     * 根据ID查询vo
     * @param id
     * @author: ${author}
     */
    ${vo} findVOById(Long id) throws BusinessException;

    /**
     * 根据ID集合查询
     * @param ids
     * @author: ${author}
     */
    List<${pojo}> findByIds(Set<Long> ids) throws BusinessException;

    /**
     * 根据ID删除数据
     * @param id
     * @author: ${author}
     */
    void deleteById(Long id) throws BusinessException;

    /**
     * 分页查询
     * @param ${voVariable}
     * @author: ${author}
     * @return
     */
    Grid<${vo}> find${pojo}Page (${vo} ${voVariable})throws BusinessException;
}
`;
}
