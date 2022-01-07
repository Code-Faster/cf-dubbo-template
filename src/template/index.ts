import path from "path";
import fs from "fs";
import parseIgnore from "parse-gitignore";
/** 配置文件默认名称 */
export const TEMPLATE_JSON = "cfconfig.json";
/** 静态目录模版目录名 */
export const TEMPLATE_MODEL_NAME = "createTemplate";
/** 忽略的文件 */
export const EXCLUDE_PATH = parseIgnore(
  fs.readFileSync(path.join(__dirname, ".cfignore"))
);
/** Java文件后缀 */
export const FILE_SUFFIX = ".java";
/** 生成xml后缀 */
export const MAPPER_SUFFIX = "Mapper.xml";

/**
 * 根据文件路径获取包名
 * @param filePath 文件路径
 * @param startFix 包名前缀
 */
export const getPackageName = (filePath: string, startFix: string) => {
  if (filePath.length === 0 || startFix.length === 0) {
    throw new Error("缺少参数!");
  }
  const fileObj = path.parse(filePath);
  const filePathArr = path.join(fileObj.dir, fileObj.name).split(path.sep);
  return filePathArr
    .filter((_ele: unknown, index: number) => {
      return index >= filePathArr.indexOf(startFix);
    })
    .join(".");
};

/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
export const getParamVariableFormat = (str: string) => {
  return str.charAt(0).toLowerCase() + str.substring(1);
};

/** 格式化get set 方法 */
export const getSetFormat = (str: string) => {
  return str.charAt(0).toUpperCase() + str.substring(1);
};

/**
 * 模版工具类，初始化参数为Project 对象
 */
export class TemplateTools {
  private project: CodeFaster.Project = {
    owner: "",
    templateId: 0,
    projectDir: "",
    templateDir: "",
    projectName: "",
    type: 1,
    description: "",
  };
  private keyPathArr: Array<any> = [];
  // 项目最终路径
  private projectPath: string = "";
  // 配置文件路径
  private configPath: string = "";

  private templateDir: string = "";

  constructor(pj: CodeFaster.Project) {
    this.templateDir = path.join(__dirname, "../../playground/createTemplate");
    this.project = pj;
    this.keyPathArr = [];
    this.projectPath = path.join(pj.projectDir, pj.projectName);
    this.configPath = path.join(this.projectPath, TEMPLATE_JSON);
    this.updateProjectDirJson();
  }

  /**
   * 根据文件名、搜索目录获取唯一文件
   * @param fileName
   */
  findOneFileByKey(fileName: string) {
    const filePathArr = this.findByKey(fileName, 1);
    if (filePathArr.length > 1) {
      throw new Error("搜索出错，文件数量超出");
    }
    return filePathArr.length == 1 ? filePathArr[0] : { value: "" };
  }

  /**
   * 根据文件名获取package
   * @param {文件名} file_name
   */
  getPackageNameByFileName(fileName: string) {
    let searchFilePath = this.findOneFileByKey(fileName).value;
    return searchFilePath.length > 0
      ? getPackageName(searchFilePath, "com")
      : "";
  }
  /**
   * 根据关键字获取文件信息
   * @param key
   * @param type 搜索文件夹 还是 文件 默认0 :文件夹 1: 文件 2、模糊搜索文件
   */
  findByKey(key: string, type: number) {
    // 置空
    this.keyPathArr = [];
    const jsonData: CodeFaster.FileObj = this.getJsonFromPath();
    this.serachJSON(jsonData, key, type);
    return this.keyPathArr;
  }

  /**
   * 根据文件转化json结构
   * @param isUpdate 是否强制更新文件
   * @param filePath 如果强制更新，是否指定读取更新文件地址
   */
  getJsonFromPath = (
    isUpdate?: boolean,
    filePath?: string
  ): CodeFaster.FileObj => {
    let configPath = this.configPath;
    // 如果导入的时候指定文件
    if (filePath) {
      configPath = filePath;
    }
    const stats = fs.statSync(configPath);
    if (stats.isFile()) {
      const jsonData: CodeFaster.FileObj = JSON.parse(
        fs.readFileSync(configPath, "utf-8")
      );
      // 处理项目文件目录
      if (isUpdate) {
        // 重新生成
        jsonData.path = path.parse(configPath).dir;
        if (jsonData.formData && jsonData.formData !== undefined) {
          // buildPath 去除项目名称
          const arr = path.parse(configPath).dir.split(path.sep);
          arr.pop();
          jsonData.formData.buildPath = arr.join(path.sep);
        }
        return this.showStructure(jsonData.formData, jsonData);
      }
      return jsonData;
    }
    throw Error("文件地址格式不正确！");
  };

  /**
   * 拷贝模版代码，复制模版代码，内部做关键字替换
   * @param structure
   */
  copyCoding(structure: CodeFaster.FileObj) {
    if (!fs.existsSync(structure.path)) {
      fs.mkdirSync(structure.path);
    }
    if (structure.isDir) {
      if (structure.children.length > 0) {
        // 如果是文件夹
        structure.children.forEach((obj: CodeFaster.FileObj) => {
          // 如果子目录是dir
          if (obj.isDir) this.copyCoding(obj);
          else {
            const data = fs.readFileSync(obj.path || "", "utf8");
            const result = data.replace(
              new RegExp(TEMPLATE_MODEL_NAME, "g"),
              this.project.projectName
            );
            fs.writeFileSync(obj.path, result, "utf8");
          }
        });
      }
    } else {
      // 如果不是文件夹
      const data = fs.readFileSync(structure.path || "", "utf8");
      const result = data.replace(
        new RegExp(TEMPLATE_MODEL_NAME, "g"),
        this.project.projectName
      );
      fs.writeFileSync(structure.path, result, "utf8");
    }
  }

  /**
   * 遍历文件目录结构
   * @param fileObj
   */
  fileDisplay(fileObj: CodeFaster.FileObj) {
    // 根据文件路径读取文件，返回文件列表
    const files = fs.readdirSync(fileObj.path);
    // 遍历读取到的文件列表
    files.forEach((fileName) => {
      // 获取当前文件的绝对路径
      const filedir = path.join(fileObj.path, fileName);
      // 根据文件路径获取文件信息，返回一个fs.Stats对象
      const stats = fs.statSync(filedir);
      const isFile = stats.isFile(); // 是文件
      const isDir = stats.isDirectory(); // 是文件夹
      const isExcludeFlag = EXCLUDE_PATH.filter((ele) => {
        return filedir.indexOf(ele) >= 0;
      });
      if (isExcludeFlag.length > 0) {
        return;
      }
      if (isFile) {
        // 根据 fileObj 判读缓存数据 是否存在父亲目录
        const fileArr = fileObj.children.filter((ele: any) => {
          return ele.path === fileObj.path;
        });
        const obj: CodeFaster.FileObj = {
          fileName,
          path: filedir,
          sortPath: path.relative(this.projectPath, filedir),
          isDir: !isFile,
          children: [],
        };
        // 如果有父级
        if (fileArr.length === 1) {
          fileArr[0].children.push(obj);
        } else {
          fileObj.children.push(obj);
        }
      }
      if (isDir) {
        const obj: CodeFaster.FileObj = {
          fileName,
          path: filedir,
          sortPath: path.relative(this.projectPath, filedir),
          isDir,
          children: [],
        };
        // 根据 fileObj 判读缓存数据 是否存在父亲目录
        const dirArr = fileObj.children.filter((ele: any) => {
          return ele.path === fileObj.path;
        });
        // 如果有父级
        if (dirArr.length === 1) {
          dirArr[0].children.push(obj);
        } else {
          fileObj.children.push(obj);
        }
        this.fileDisplay(obj); // 递归，如果是文件夹，就继续遍历该文件夹下面的文件
      }
    });
  }
  /**
   * 获取模版文件结构
   * @param formData
   * @param obj
   */
  showStructure(formData: any, obj?: any): CodeFaster.FileObj {
    const dirStructure: CodeFaster.FileObj = {
      fileName: obj ? obj.fileName : TEMPLATE_MODEL_NAME,
      path: obj ? obj.path : this.templateDir,
      sortPath: obj
        ? path.relative(this.projectPath, obj.path)
        : path.relative(this.projectPath, this.templateDir),
      formData,
      isDir: true,
      children: [],
    };
    this.fileDisplay(dirStructure);
    return dirStructure;
  }

  /**
   * 更新项目结构
   */
  updateProjectDirJson() {
    try {
      const jsonData: CodeFaster.FileObj = this.getJsonFromPath(true);
      fs.writeFileSync(
        path.join(this.projectPath, TEMPLATE_JSON),
        JSON.stringify(jsonData)
      );
    } catch (error: unknown) {
      throw Error("updateProjectDirJson throw error : " + error);
    }
    return true;
  }

  /**
   * 根据文档结构json 迭代出匹配关键字地址
   * @param jsonData 目录结构json
   * @param key   关键字
   * @param type 搜索文件夹 还是 文件 默认0 :文件夹 1: 文件 2、模糊搜索文件
   */
  serachJSON(jsonData: CodeFaster.FileObj, key: string, type: number) {
    // 如果是文件夹
    if (jsonData.isDir) {
      if (jsonData.fileName === key && type === 0) {
        this.keyPathArr.push({
          label: jsonData.sortPath,
          value: jsonData.path,
          children: jsonData.children,
        });
      }
      // 如果还有子文件, 递归执行
      if (jsonData.children.length > 0) {
        jsonData.children.forEach((obj: CodeFaster.FileObj) => {
          this.serachJSON(obj, key, type);
        });
      }
    } else {
      // 如果搜索文件
      if (type === 1 && jsonData.fileName === key) {
        this.keyPathArr.push({
          label: jsonData.sortPath,
          value: jsonData.path,
        });
      }
      if (type === 2 && jsonData.fileName.includes(key)) {
        this.keyPathArr.push({
          label: jsonData.sortPath,
          value: jsonData.path,
        });
      }
    }
  }
  /**
   * 根据 _ 生成驼峰 , type 默认true 首字母大写,如果没有 _ 分隔符 , 则取第一个大写
   * @param {*} str
   */
  tranformHumpStr(str: string, type = true) {
    if (str.length === 0) {
      return "";
    }
    if (str.indexOf("_") >= 0) {
      let strArr = str.split("_");
      strArr = strArr.map((ele) => {
        return ele.charAt(0).toUpperCase() + ele.substring(1).toLowerCase();
      });
      const result = strArr.join("");
      return type
        ? result
        : result.charAt(0).toLowerCase() + result.substring(1);
    }
    return type
      ? str.charAt(0).toUpperCase() + str.substring(1).toLowerCase()
      : str;
  }
}
