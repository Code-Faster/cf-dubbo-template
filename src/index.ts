import mapper from "./template/mapper/index";
import service from "./template/service/index";
import serviceImpl from "./template/serviceImpl/index";
import controller from "./template/controller/index";
import unitTest from "./template/unitTest/index";
import fs from "fs";
/**
 * 根据文件地址读取文件
 * @param filePath
 */
export function readFile(filePath: string): string {
  const stats = fs.statSync(filePath);
  if (stats.isFile()) {
    return fs.readFileSync(filePath, "utf-8");
  } else {
    throw new Error("传入的参数必须为文件地址");
  }
}
export default class CodeGenerator {
  /** 项目参数 */
  project: CodeFaster.Project;

  params: CodeFaster.Params;

  constructor(project: CodeFaster.Project, params: CodeFaster.Params) {
    this.project = project;
    this.params = params;
  }
  getService() {
    fs.writeFileSync(
      this.params.releasePath,
      service(this.project, this.params)
    );
  }

  getMapper() {
    fs.writeFileSync(
      this.params.releasePath,
      mapper(this.project, this.params)
    );
  }

  getController() {
    fs.writeFileSync(
      this.params.releasePath,
      controller(this.project, this.params)
    );
  }

  getServiceImpl() {
    fs.writeFileSync(
      this.params.releasePath,
      serviceImpl(this.project, this.params)
    );
  }

  getUnitTest() {
    fs.writeFileSync(
      this.params.releasePath,
      unitTest(this.project, this.params)
    );
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
          name: name,
          type: type,
          col: col,
        };
      });
    return {
      tableName: tableName,
      tableCnName: tableCnName,
      tableColArr: tableColArr,
    } as CodeFaster.Model;
  }
}
