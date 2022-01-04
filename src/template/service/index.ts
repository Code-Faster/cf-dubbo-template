import { getPackageName, TemplateTools, FILE_SUFFIX } from "../index";
import fs from "fs";
import path from "path";

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

  const serviceName = pojo + "Service";
  const now = new Date();

  const template = `
package ${getPackageName(params.releasePath, "com")};

import ${tools.getPackageNameByFileName("MybatisDao" + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName(pojo + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName(vo + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName("BusinessException" + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName("Grid" + FILE_SUFFIX)};
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
  fs.writeFileSync(
    path.join(params.releasePath, serviceName + FILE_SUFFIX),
    template
  );
}
