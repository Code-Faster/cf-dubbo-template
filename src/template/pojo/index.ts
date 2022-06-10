import {
  FILE_SUFFIX,
  getPackageName,
  TemplateTools,
  tranformHumpStr,
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
  tools.updateProjectConfig();

  const now = new Date();
  // 类名
  const pojoClassName = tranformHumpStr(params.model.tableName);

  const template = `
package ${getPackageName(params.releasePath, "com")};

import java.io.Serializable;
import javax.persistence.*;

${params.model.tableCloums
  .map((ele) => {
    if (ele.columnType == "Date") {
      return "import java.util.Date;";
    } else if (ele.columnType == "List") {
      return "import java.util.List;";
    } else if (ele.columnType == "BigDecimal") {
      return "import java.math.BigDecimal;";
    } else return "";
  })
  .join("")}

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@Entity(name = "${params.model.tableName}")
${
  params.model.tableComment &&
  '@ApiModel(value = "' + params.model.tableComment + '")'
}
@SuppressWarnings("serial")
public class ${pojoClassName} implements Serializable {
    ${params.model.tableCloums
      .map((ele) => {
        if ("id" === ele.columnName) {
          return `
    @Id
    @ApiModelProperty(value = "${ele.columnComment}")
    private ${ele.columnType} ${ele.columnName};
          `;
        }
        return `
    @ApiModelProperty(value = "${ele.columnComment}")
    private ${ele.columnType} ${ele.columnName};
    `;
      })
      .join("")}
}`;
  fs.writeFileSync(
    path.join(params.releasePath, pojoClassName + FILE_SUFFIX),
    template
  );
}
