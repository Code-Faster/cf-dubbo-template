import service from "../service/index";

/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
const getParamVariableFormat = (str: string) => {
  return str.charAt(0).toLowerCase() + str.substring(1);
};
export default function (
  projectName: string,
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
  const serviceNameVariable = getParamVariableFormat(serviceName);
  const e2eName = pojoName + "Test";
  return `
package com.${projectName};
import ${getPackageNameByFileName(pojo + ".java")};
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import ${getPackageNameByFileName(pojo + ".java")};
import ${getPackageNameByFileName(vo + ".java")};
import ${getPackageName(extendsPath, "com")};
import ${getPackageNameByFileName("Grid.java")};
import ${getPackageNameByFileName("StringUtil.java")};
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ${serviceName} 测试
 * @author: ${author}
 * @date: ${now}
 * @version V\${app.service.version}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
//@Rollback
//@Transactional
public class ${e2eName} {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ${serviceName} ${serviceNameVariable};

  /**
   * 保存
   * @author: ${author}
   */
  @Test
  public void save${pojo} (){
      ${vo} ${voVariable} = new ${vo} ();
      ${voVariable}.setId(1L);
      //TODO: 生成必填字段
      ${serviceNameVariable}.save${pojo}(${voVariable});
      logger.error("save ------" + JSON.toJSONString(${voVariable}));
  }
  
  /**
   * 更新 
   * @author: ${author}
   */
  @Test
  public void update${pojo} (){
      ${vo} ${voVariable} = new ${vo} ();
      ${voVariable}.setId(1L);
      ${serviceNameVariable}.update${pojo}(${voVariable});
    logger.error("update ------" + JSON.toJSONString(${voVariable}));
  }

  /**
   * 根据ID查询详情
   * @author: ${author}
   */
  @Test
  public void findById() {
      ${pojo} db = ${serviceNameVariable}.findVOById(1L);
      logger.error("findById ------" + JSON.toJSONString(db));
  }

  /**
   * 根据ID查询详情
   * @author: ${author}
   */
  @Test
  public void findVOById(){
      ${pojo} db = ${serviceNameVariable}.findVOById(1L);
      logger.error("findVOById ------" + JSON.toJSONString(db));
  }

  /**
   * 根据ids 查询
   * @author: ${author}
   */
  @Test
  public void findByIds() {
      List<${pojo}> list =${serviceNameVariable}.findByIds(new HashSet<Long>(StringUtil.stringToLongList("1,2,3")));
      logger.error("findByIds ------" + JSON.toJSONString(list));
  }

  /**
   * 根据ID删除数据
   * @author: ${author}
   */
  @Test
  public void deleteById() {
      ${serviceNameVariable}.deleteById(1L);
      logger.error("deleteById ------ success");
  }

  /**
   * 分页查询
   * @author: ${author}
   */
  @Test
  public void find${pojo}Page (){
      ${vo} ${voVariable} = new ${vo}();
      ${voVariable}.setPage(1);
      ${voVariable}.setRows(10);
      Grid find${pojo}Page = ${serviceNameVariable}.find${pojo}Page(${voVariable});
      logger.error("find${pojo}Page ------ " + JSON.toJSONString(find${pojo}Page));
  }
}
      `;
}
