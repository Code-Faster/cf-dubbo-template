import {
  FILE_SUFFIX,
  getPackageName,
  getParamVariableFormat,
  TemplateTools,
} from "../index";
import fs from "fs";
import path from "path";

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
  tools.updateProjectDirJson();

  const now = new Date();
  const serviceName = pojo + "Service";
  const serviceNameVariable = getParamVariableFormat(serviceName);
  const e2eName = pojo + "Test";
  const sortName = params.releasePath.substring(
    0,
    params.releasePath.indexOf("/src/")
  );
  const application = tools
    .findByKey("Application.java", 1)
    .filter((ele: any) => {
      return ele.value.indexOf(sortName) >= 0;
    });
  const template = `
package com.${project.projectName};
import ${getPackageName(application[0].value, "com")};
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import ${tools.getPackageNameByFileName(pojo + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName(vo + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName(serviceName + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName("Grid" + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName("StringUtil" + FILE_SUFFIX)};
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ${serviceName} 单元测试
 * @author ${author}
 * @date ${now}
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
     * @author ${author}
     */
    @Test
    public void save${pojo} (){
        ${vo} ${voVariable} = new ${vo} ();
        ${voVariable}.setId(1L);
        //TODO: 生成必填字段
        ${serviceNameVariable}.save${pojo}(${voVariable});
        logger.debug("save ------" + JSON.toJSONString(${voVariable}));
    }
    
    /**
     * 更新 
     * @author ${author}
     */
    @Test
    public void update${pojo} (){
        ${vo} ${voVariable} = new ${vo} ();
        ${voVariable}.setId(1L);
        ${serviceNameVariable}.update${pojo}(${voVariable});
      logger.debug("update ------" + JSON.toJSONString(${voVariable}));
    }

    /**
     * 根据ID查询详情
     * @author ${author}
     */
    @Test
    public void findById() {
        ${pojo} db = ${serviceNameVariable}.findVOById(1L);
        logger.error("findById ------" + JSON.toJSONString(db));
    }

    /**
     * 根据ID查询详情
     * @author ${author}
     */
    @Test
    public void findVOById(){
        ${pojo} db = ${serviceNameVariable}.findVOById(1L);
        logger.error("findVOById ------" + JSON.toJSONString(db));
    }

    /**
     * 根据ids 查询
     * @author ${author}
     */
    @Test
    public void findByIds() {
        List<${pojo}> list =${serviceNameVariable}.findByIds(new HashSet<Long>(StringUtil.stringToLongList("1,2,3")));
        logger.error("findByIds ------" + JSON.toJSONString(list));
    }

    /**
     * 根据ID删除数据
     * @author ${author}
     */
    @Test
    public void deleteById() {
        ${serviceNameVariable}.deleteById(1L);
        logger.error("deleteById ------ success");
    }

    /**
     * 分页查询
     * @author ${author}
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
  fs.writeFileSync(
    path.join(params.releasePath, e2eName + FILE_SUFFIX),
    template
  );
}
