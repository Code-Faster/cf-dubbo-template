import { FILE_SUFFIX, getPackageName, TemplateTools } from "../index";
import fs from "fs";
import path from "path";

export default function (
  project: CodeFaster.Project,
  params: CodeFaster.Params
) {
  /**
   * 检验参数是否正常
   */
  if (params.model == undefined) {
    throw Error("model 必传");
  }
  /**
   * 根据传递的参数生成template需要的参数
   */
  const author = project.owner;
  /**
   * 获取模版工具类
   */
  const tools = new TemplateTools(project);

  tools.updateProjectDirJson();

  const now = new Date();
  // 类名
  const pojoClassName = tools.tranformHumpStr(params.model.tableName);

  const template = `
package ${getPackageName(params.releasePath, "com")};

import ${tools.getPackageNameByFileName(pojoClassName + FILE_SUFFIX)};

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import ${tools.getPackageNameByFileName("PageParameter" + FILE_SUFFIX)};
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ${author}
 * @version V\${app.service.version}
 * @date: ${now}
 */
@Data
@ApiModel(value = "${params.model.tableComment}")
@SuppressWarnings("serial")
public class ${pojoClassName}VO extends ${pojoClassName} implements Serializable {
    @ApiModelProperty(value = "ID集合，逗号分隔")
    private String ids;
    @ApiModelProperty(value = "当前页")
    private Integer page;
    @ApiModelProperty(value = "每页的条数")
    private Integer rows;
    @ApiModelProperty(value = "分页参数")
    private PageParameter pageParameter;
    @ApiModelProperty(value = "列")
    private String column;
}
    `;
  fs.writeFileSync(
    path.join(params.releasePath, pojoClassName + "VO" + FILE_SUFFIX),
    template
  );
}
