import pojo from "./template/pojo/index";
import vo from "./template/vo/index";
import mapper from "./template/mapper/index";
import service from "./template/service/index";
import serviceImpl from "./template/serviceImpl/index";
import controller from "./template/controller/index";
import unitTest from "./template/unitTest/index";
import fs from "fs";
import { TemplateTools } from "./template/index";
/**
 * 根据文件地址读取文件
 * @param filePath
 */
function readFile(filePath: string): string {
  const stats = fs.statSync(filePath);
  if (stats.isFile()) {
    return fs.readFileSync(filePath, "utf-8");
  } else {
    throw new Error("传入的参数必须为文件地址");
  }
}
export default class CodeGenerator implements CodeFaster.JavaCodeGenerator {
  project: CodeFaster.Project;
  constructor(project: CodeFaster.Project) {
    this.project = project;
  }

  init(params: CodeFaster.Params) {
    const tools = new TemplateTools(this.project);
    tools.init();
  }

  generatorPojo(params: CodeFaster.Params) {
    pojo(this.project, params);
  }

  generatorVO(params: CodeFaster.Params) {
    vo(this.project, params);
  }

  generatorService(params: CodeFaster.Params) {
    service(this.project, params);
  }

  generatorMapper(params: CodeFaster.Params) {
    mapper(this.project, params);
  }

  generatorController(params: CodeFaster.Params) {
    controller(this.project, params);
  }

  generatorServiceImpl(params: CodeFaster.Params) {
    serviceImpl(this.project, params);
  }

  generatorUnitTest(params: CodeFaster.Params) {
    unitTest(this.project, params);
  }

  /**
   * 根据pojo文件地址 逆向解析模型类 model -> json 格式
   * @param filePath
   */
  getModelByPojoPath(filePath: string) {
    //读取 filePath 文件
    let fileText: string | any = readFile(filePath);
    //解析 fileText
    let typeArr = [
      "Long",
      "Integer",
      "String",
      "Date",
      "Double",
      "List",
      "BigDecimal",
    ];
    const tableName = fileText
      .substring(fileText.search(/@+/), fileText.search(/public+/))
      .split("@")
      .find((ele: any) => {
        return ele.indexOf("Entity") >= 0;
      })
      .replace(/\s/g, "")
      .split("=")[1]
      .replace(/\"|\)/g, "");
    const tableCnName = fileText
      .substring(fileText.search(/@+/), fileText.search(/public+/))
      .split("@")
      .find((ele: any) => {
        return ele.indexOf("ApiModel") >= 0;
      })
      .replace(/\s/g, "")
      .split("=")[1]
      .replace(/\"|\)/g, "");

    const tableColArr = fileText
      .substring(fileText.search(/{+/) + 1, fileText.length - 1)
      .split(/public+(\S*)/g)[0]
      .trim()
      .replace(/\r\n/g, "")
      .split(";")
      .filter((ele: any) => {
        // 过滤空数据
        if (ele.length >= 0) {
          return ele;
        }
      })
      .map((ele: any) => {
        let name = "";
        let result = ele
          .trim()
          .split(/\)/)
          .filter((ele: any) => {
            if (ele.indexOf("ApiModelProperty") >= 0) {
              // 处理下第一行
              ele = ele.replace(/\s/g, "");
              name =
                ele && ele.length > 0
                  ? ele.split("=")[1].replace(/\"|\)/g, "")
                  : "";
              return false;
            } else {
              return true;
            }
          });
        result = result[result.length - 1].split(/\s/g);
        // 处理单条数据
        let type = result.find((ele: any) => {
          return typeArr.indexOf(ele) >= 0;
        });
        // 类型后面
        let col = result[result.indexOf(type) + 1];
        return {
          columnComment: name,
          columnType: type,
          columnName: col,
        } as CodeFaster.SqlColumn;
      });
    return {
      tableName: tableName,
      tableComment: tableCnName,
      tableCloums: tableColArr,
    } as CodeFaster.SqlTable;
  }

  updateProjectConfig() {
    const tools = new TemplateTools(this.project);
    return tools.updateProjectDirJson();
  }
}
