/*!
  * code-dubbo-template v0.0.21
  * (c) 2022 biqi li
  * @license MIT
  */
import path from 'path';
import fs from 'fs';
import parseIgnore from 'parse-gitignore';

function ownKeys(object, enumerableOnly) {
  var keys = Object.keys(object);

  if (Object.getOwnPropertySymbols) {
    var symbols = Object.getOwnPropertySymbols(object);

    if (enumerableOnly) {
      symbols = symbols.filter(function (sym) {
        return Object.getOwnPropertyDescriptor(object, sym).enumerable;
      });
    }

    keys.push.apply(keys, symbols);
  }

  return keys;
}

function _objectSpread2(target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i] != null ? arguments[i] : {};

    if (i % 2) {
      ownKeys(Object(source), true).forEach(function (key) {
        _defineProperty(target, key, source[key]);
      });
    } else if (Object.getOwnPropertyDescriptors) {
      Object.defineProperties(target, Object.getOwnPropertyDescriptors(source));
    } else {
      ownKeys(Object(source)).forEach(function (key) {
        Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key));
      });
    }
  }

  return target;
}

function _classCallCheck(instance, Constructor) {
  if (!(instance instanceof Constructor)) {
    throw new TypeError("Cannot call a class as a function");
  }
}

function _defineProperties(target, props) {
  for (var i = 0; i < props.length; i++) {
    var descriptor = props[i];
    descriptor.enumerable = descriptor.enumerable || false;
    descriptor.configurable = true;
    if ("value" in descriptor) descriptor.writable = true;
    Object.defineProperty(target, descriptor.key, descriptor);
  }
}

function _createClass(Constructor, protoProps, staticProps) {
  if (protoProps) _defineProperties(Constructor.prototype, protoProps);
  if (staticProps) _defineProperties(Constructor, staticProps);
  return Constructor;
}

function _defineProperty(obj, key, value) {
  if (key in obj) {
    Object.defineProperty(obj, key, {
      value: value,
      enumerable: true,
      configurable: true,
      writable: true
    });
  } else {
    obj[key] = value;
  }

  return obj;
}

/** 配置文件默认名称 */

var TEMPLATE_JSON = "cfconfig.json";
var TEMPLATE_DIR = path.join(process.cwd(), "./playground/createTemplate"); // 实现build时候替换参数

{
  TEMPLATE_DIR = path.join(__dirname, "./playground/createTemplate");
}
/** 静态目录模版目录名 */


var TEMPLATE_MODEL_NAME = "createTemplate";
/** 忽略的文件 */

var EXCLUDE_PATH = parseIgnore(fs.readFileSync(path.join(__dirname, ".cfignore")));
/** Java文件后缀 */

var FILE_SUFFIX = ".java";
/**
 * 根据 _ 生成驼峰 , type 默认true 首字母大写,如果没有 _ 分隔符 , 则取第一个大写
 * @param {*} str
 */

var tranformHumpStr = function tranformHumpStr(str) {
  var type = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : true;

  if (str.length === 0) {
    return "";
  }

  if (str.indexOf("_") >= 0) {
    var strArr = str.split("_");
    strArr = strArr.map(function (ele) {
      return ele.charAt(0).toUpperCase() + ele.substring(1).toLowerCase();
    });
    var result = strArr.join("");
    return type ? result : result.charAt(0).toLowerCase() + result.substring(1);
  }

  return type ? str.charAt(0).toUpperCase() + str.substring(1).toLowerCase() : str;
};
/**
 * 根据文件路径获取包名
 * @param filePath 文件路径
 * @param startFix 包名前缀
 */

var getPackageName = function getPackageName(filePath, startFix) {
  if (filePath.length === 0 || startFix.length === 0) {
    throw new Error("缺少参数!");
  }

  var fileObj = path.parse(filePath);
  var filePathArr = path.join(fileObj.dir, fileObj.name).split(path.sep);
  return filePathArr.filter(function (_ele, index) {
    return index >= filePathArr.indexOf(startFix);
  }).join(".");
};
/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */

var getParamVariableFormat$2 = function getParamVariableFormat(str) {
  return str.charAt(0).toLowerCase() + str.substring(1);
};
/** 格式化get set 方法 */

var getSetFormat = function getSetFormat(str) {
  return str.charAt(0).toUpperCase() + str.substring(1);
};
/**
 * 模版工具类，初始化参数为Project 对象
 */

var TemplateTools = /*#__PURE__*/function () {
  function TemplateTools(pj) {
    var _this = this;

    _classCallCheck(this, TemplateTools);

    this.project = {
      owner: "",
      // 目录最终路径
      projectDir: "",
      projectName: "",
      type: 1,
      description: "",
      templateName: ""
    };
    this.keyPathArr = []; // 配置文件路径

    this.configPath = "";
    /**
     * 根据文件转化json结构
     * @param isUpdate 是否强制更新文件
     * @param filePath 如果强制更新，是否指定读取更新文件地址
     */

    this.getJsonFromPath = function (isUpdate, filePath) {
      var configPath = _this.configPath; // 如果导入的时候指定文件

      if (filePath) {
        configPath = filePath;
      }

      var stats = fs.statSync(configPath);

      if (stats.isFile()) {
        var jsonData = JSON.parse(fs.readFileSync(configPath, "utf-8")); // 处理项目文件目录

        if (isUpdate) {
          // 重新生成
          jsonData.path = path.parse(configPath).dir;

          if (jsonData.project && jsonData.project !== undefined) {
            if (_this.project) {
              jsonData.project = _objectSpread2({}, _this.project);
            } // buildPath 去除项目名称


            var arr = path.parse(configPath).dir.split(path.sep);
            jsonData.project.projectDir = arr.join(path.sep);
          } else {
            throw Error("config缺少project属性");
          }

          return _this.showStructure(jsonData.project);
        }

        return jsonData;
      }

      throw Error("文件地址格式不正确！");
    };

    this.project = pj;
    this.keyPathArr = [];
    this.configPath = path.join(pj.projectDir, TEMPLATE_JSON);
  }
  /** 初始化项目 */


  _createClass(TemplateTools, [{
    key: "init",
    value: function init() {
      // 1、获取模版目录结构
      var templateConfig = this.getTemplateConfig();
      this.fileDisplay(templateConfig); // 2、生成新项目结构目录文件

      var projectConfig = this.getInitConfig(this.project);
      projectConfig.children = templateConfig.children;
      projectConfig.fromPath = templateConfig.path;
      this.replaceStructure(projectConfig); // 3、将模版修改后输出到产出目录

      this.copyCoding(projectConfig); // 4、将生成的目录文件copy到输出目录项目下

      fs.writeFileSync(path.join(this.project.projectDir, TEMPLATE_JSON), JSON.stringify(projectConfig));
    }
    /**
     * 替换掉目录结构
     * @param structure 项目目录结构
     */

  }, {
    key: "replaceStructure",
    value: function replaceStructure(structure) {
      var _this2 = this;

      // TODO: 测试windows平台是否效果一致
      var releasePath = this.project.projectDir;
      var fromPath = structure.path;
      structure.fromPath = fromPath;
      structure.path = fromPath.replaceAll(TEMPLATE_DIR, releasePath).replaceAll(TEMPLATE_MODEL_NAME, this.project.projectName);

      if (structure.children.length > 0) {
        structure.children.forEach(function (obj) {
          var fromPath = obj.fromPath;
          obj.fromPath = fromPath;
          fromPath && (obj.path = fromPath.replaceAll(TEMPLATE_DIR, releasePath).replaceAll(TEMPLATE_MODEL_NAME, _this2.project.projectName));

          _this2.replaceStructure(obj);
        });
      }
    }
    /**
     * 根据文件名、搜索目录获取唯一文件
     * @param fileName
     */

  }, {
    key: "findOneFileByKey",
    value: function findOneFileByKey(fileName) {
      var filePathArr = this.findByKey(fileName, 1);

      if (filePathArr.length > 1) {
        throw new Error("搜索出错，文件数量超出");
      }

      return filePathArr.length == 1 ? filePathArr[0] : {
        value: ""
      };
    }
    /**
     * 根据文件名获取package
     * @param {文件名} file_name
     */

  }, {
    key: "getPackageNameByFileName",
    value: function getPackageNameByFileName(fileName) {
      var searchFilePath = this.findOneFileByKey(fileName).value;
      return searchFilePath.length > 0 ? getPackageName(searchFilePath, "com") : "";
    }
    /**
     * 根据关键字获取文件信息
     * @param key
     * @param type 搜索文件夹 还是 文件 默认0 :文件夹 1: 文件 2、模糊搜索文件
     */

  }, {
    key: "findByKey",
    value: function findByKey(key, type) {
      // 置空
      this.keyPathArr = [];
      var jsonData = this.getJsonFromPath();
      this.serachJSON(jsonData, key, type);
      return this.keyPathArr;
    }
  }, {
    key: "showStructure",
    value: function showStructure(project) {
      var dirStructure = this.getInitConfig(project);
      this.fileDisplay(dirStructure);
      return dirStructure;
    }
    /**
     * 拷贝模版代码，复制模版代码，内部做关键字替换
     * @param structure
     */

  }, {
    key: "copyCoding",
    value: function copyCoding(structure) {
      var _this3 = this;

      if (!fs.existsSync(structure.path)) {
        fs.mkdirSync(structure.path);
      }

      if (structure.isDir) {
        if (structure.children.length > 0) {
          // 如果是文件夹
          structure.children.forEach(function (obj) {
            // 如果子目录是dir
            if (obj.isDir) _this3.copyCoding(obj);else {
              var data = fs.readFileSync(obj.fromPath || "", "utf8");
              var result = data.replace(new RegExp(TEMPLATE_MODEL_NAME, "g"), _this3.project.projectName);
              fs.writeFileSync(obj.path, result, "utf8");
            }
          });
        }
      } else {
        // 如果不是文件夹
        var data = fs.readFileSync(structure.fromPath || "", "utf8");
        var result = data.replace(new RegExp(TEMPLATE_MODEL_NAME, "g"), this.project.projectName);
        fs.writeFileSync(structure.path, result, "utf8");
      }
    }
    /**
     * 遍历文件目录结构
     * @param fileObj
     */

  }, {
    key: "fileDisplay",
    value: function fileDisplay(fileObj) {
      var _this4 = this;

      // 根据文件路径读取文件，返回文件列表
      var files = fs.readdirSync(fileObj.path); // 遍历读取到的文件列表

      files.forEach(function (fileName) {
        // 获取当前文件的绝对路径
        var filedir = path.join(fileObj.path, fileName); // 根据文件路径获取文件信息，返回一个fs.Stats对象

        var stats = fs.statSync(filedir);
        var isFile = stats.isFile(); // 是文件

        var isDir = stats.isDirectory(); // 是文件夹

        var isExcludeFlag = EXCLUDE_PATH.filter(function (ele) {
          return fileName.includes(ele); // 注意不能使用路径，因为可能在GUI里包含到忽略文件
        });

        if (isExcludeFlag.length > 0) {
          return;
        }

        if (isFile) {
          var _fileObj$project, _fileObj$project2;

          // 根据 fileObj 判读缓存数据 是否存在父亲目录
          var fileArr = fileObj.children.filter(function (ele) {
            return ele.path === fileObj.path;
          });
          var obj = {
            fileName: fileName,
            path: filedir,
            sortPath: (_fileObj$project = fileObj.project) !== null && _fileObj$project !== void 0 && _fileObj$project.projectDir ? path.relative((_fileObj$project2 = fileObj.project) === null || _fileObj$project2 === void 0 ? void 0 : _fileObj$project2.projectDir, filedir) : fileName,
            isDir: !isFile,
            children: []
          }; // 如果有父级

          if (fileArr.length === 1) {
            fileArr[0].children.push(obj);
          } else {
            fileObj.children.push(obj);
          }
        }

        if (isDir) {
          var _fileObj$project3, _fileObj$project4;

          var _obj = {
            fileName: fileName,
            path: filedir,
            sortPath: (_fileObj$project3 = fileObj.project) !== null && _fileObj$project3 !== void 0 && _fileObj$project3.projectDir ? path.relative((_fileObj$project4 = fileObj.project) === null || _fileObj$project4 === void 0 ? void 0 : _fileObj$project4.projectDir, filedir) : fileName,
            isDir: isDir,
            children: [],
            project: fileObj.project
          }; // 根据 fileObj 判读缓存数据 是否存在父亲目录

          var dirArr = fileObj.children.filter(function (ele) {
            return ele.path === fileObj.path;
          }); // 如果有父级

          if (dirArr.length === 1) {
            dirArr[0].children.push(_obj);
          } else {
            fileObj.children.push(_obj);
          }

          _this4.fileDisplay(_obj); // 递归，如果是文件夹，就继续遍历该文件夹下面的文件

        }
      });
    }
    /**
     * 获取初始化config
     * @param project 项目参数
     * @returns
     */

  }, {
    key: "getInitConfig",
    value: function getInitConfig(project) {
      return {
        fileName: project.projectName,
        path: project.projectDir,
        sortPath: path.relative(project.projectDir, project.projectDir),
        isDir: true,
        project: project,
        children: []
      };
    }
    /**
     * 获取模版config
     * @returns
     */

  }, {
    key: "getTemplateConfig",
    value: function getTemplateConfig() {
      return {
        fileName: TEMPLATE_MODEL_NAME,
        path: TEMPLATE_DIR,
        sortPath: path.relative(TEMPLATE_DIR, TEMPLATE_DIR),
        isDir: true,
        children: []
      };
    }
    /**
     * 更新项目目录结构
     */

  }, {
    key: "updateProjectConfig",
    value: function updateProjectConfig() {
      try {
        if (fs.existsSync(this.configPath)) {
          var configJSON = this.getJsonFromPath(true);
          fs.writeFileSync(this.configPath, JSON.stringify(configJSON));
          return configJSON;
        }
      } catch (error) {
        throw Error("updateProjectConfig throw error : " + error);
      }
    }
    /**
     * 根据文档结构json 迭代出匹配关键字地址
     * @param jsonData 目录结构json
     * @param key   关键字
     * @param type 搜索文件夹 还是 文件 默认0 :文件夹 1: 文件 2、模糊搜索文件
     */

  }, {
    key: "serachJSON",
    value: function serachJSON(jsonData, key, type) {
      var _this5 = this;

      // 如果是文件夹
      if (jsonData.isDir) {
        if (jsonData.fileName === key && type === 0) {
          this.keyPathArr.push({
            label: jsonData.sortPath,
            value: jsonData.path,
            children: jsonData.children
          });
        } // 如果还有子文件, 递归执行


        if (jsonData.children.length > 0) {
          jsonData.children.forEach(function (obj) {
            _this5.serachJSON(obj, key, type);
          });
        }
      } else {
        // 如果搜索文件
        if (type === 1 && jsonData.fileName === key) {
          this.keyPathArr.push({
            label: jsonData.sortPath,
            value: jsonData.path
          });
        }

        if (type === 2 && jsonData.fileName.includes(key)) {
          this.keyPathArr.push({
            label: jsonData.sortPath,
            value: jsonData.path
          });
        }
      }
    }
  }]);

  return TemplateTools;
}();

function pojo (project, params) {
  /**
   * 检验参数是否正常
   */
  if (params.model == undefined) {
    throw Error("model 必传");
  }
  /**
   * 根据传递的参数生成template需要的参数
   */


  project.owner;
  /**
   * 获取模版工具类
   */

  var tools = new TemplateTools(project);
  tools.updateProjectConfig();

  var pojoClassName = tranformHumpStr(params.model.tableName);
  var template = "\npackage ".concat(getPackageName(params.releasePath, "com"), ";\n\nimport java.io.Serializable;\nimport javax.persistence.*;\n\n").concat(params.model.tableCloums.map(function (ele) {
    if (ele.columnType == "Date") {
      return "import java.util.Date;";
    } else if (ele.columnType == "List") {
      return "import java.util.List;";
    } else if (ele.columnType == "BigDecimal") {
      return "import java.math.BigDecimal;";
    } else return "";
  }).join(""), "\n\nimport io.swagger.annotations.ApiModelProperty;\nimport io.swagger.annotations.ApiModel;\nimport lombok.Data;\n\n@Data\n@Entity(name = \"").concat(params.model.tableName, "\")\n").concat(params.model.tableComment && '@ApiModel(value = "' + params.model.tableComment + '")', "\n@SuppressWarnings(\"serial\")\npublic class ").concat(pojoClassName, " implements Serializable {\n    ").concat(params.model.tableCloums.map(function (ele) {
    if ("id" === ele.columnName) {
      return "\n    @Id\n    @ApiModelProperty(value = \"".concat(ele.columnComment, "\")\n    private ").concat(ele.columnType, " ").concat(ele.columnName, ";\n          ");
    }

    return "\n    @ApiModelProperty(value = \"".concat(ele.columnComment, "\")\n    private ").concat(ele.columnType, " ").concat(ele.columnName, ";\n    ");
  }).join(""), "\n}");
  fs.writeFileSync(path.join(params.releasePath, pojoClassName + FILE_SUFFIX), template);
}

function vo (project, params) {
  /**
   * 检验参数是否正常
   */
  if (params.model == undefined) {
    throw Error("model 必传");
  }
  /**
   * 根据传递的参数生成template需要的参数
   */


  project.owner;
  /**
   * 获取模版工具类
   */

  var tools = new TemplateTools(project);
  tools.updateProjectConfig();

  var pojoClassName = tranformHumpStr(params.model.tableName);
  var template = "\npackage ".concat(getPackageName(params.releasePath, "com"), ";\n\nimport ").concat(tools.getPackageNameByFileName(pojoClassName + FILE_SUFFIX), ";\n\nimport java.io.Serializable;\n\nimport io.swagger.annotations.ApiModel;\nimport ").concat(tools.getPackageNameByFileName("PageParameter" + FILE_SUFFIX), ";\nimport io.swagger.annotations.ApiModelProperty;\nimport lombok.Data;\n\n@Data\n@ApiModel(value = \"").concat(params.model.tableComment, "\")\n@SuppressWarnings(\"serial\")\npublic class ").concat(pojoClassName, "VO extends ").concat(pojoClassName, " implements Serializable {\n    @ApiModelProperty(value = \"ID\u96C6\u5408\uFF0C\u9017\u53F7\u5206\u9694\")\n    private String ids;\n    @ApiModelProperty(value = \"\u5F53\u524D\u9875\")\n    private Integer page;\n    @ApiModelProperty(value = \"\u6BCF\u9875\u7684\u6761\u6570\")\n    private Integer rows;\n    @ApiModelProperty(value = \"\u5206\u9875\u53C2\u6570\")\n    private PageParameter pageParameter;\n    @ApiModelProperty(value = \"\u5217\")\n    private String column;\n}");
  fs.writeFileSync(path.join(params.releasePath, pojoClassName + "VO" + FILE_SUFFIX), template);
}

function mapper (project, params) {
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


  var pojo = params.props.pojo;
  var pojoVariable = getParamVariableFormat$2(pojo);
  var vo = params.props.vo;
  var voVariable = getParamVariableFormat$2(vo);
  var author = project.owner;
  var mapperName = pojo + "Mapper.xml";
  /**
   * 获取模版工具类
   */

  var tools = new TemplateTools(project);
  var template = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n<mapper namespace=\"".concat(pojo, "\">\n  <resultMap type=\"").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), "\" id=\"").concat(pojoVariable, "\"/>\n  <resultMap type=\"").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), "\" id=\"").concat(voVariable, "\"/>\n  <resultMap type=\"java.util.HashMap\" id=\"map\"/>\n  <parameterMap type=\"").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), "\" id=\"").concat(pojoVariable, "\"/>\n  <parameterMap type=\"").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), "\" id=\"").concat(voVariable, "\"/>\n  <parameterMap type=\"java.util.HashMap\" id=\"map\"/>\n    \n    <!-- \u6839\u636EID\u67E5\u8BE2\u8868\u4FE1\u606F author by ").concat(author, " -->\n  <select id=\"findById\" resultMap=\"").concat(pojoVariable, "\" parameterMap=\"map\">\n      SELECT * FROM ").concat(params.model.tableName, " WHERE ID = #{id}\n  </select>\n    \n    <!-- \u6839\u636EID\u67E5\u8BE2\u7528\u6237\u8BE6\u60C5 author by ").concat(author, " -->\n  <select id=\"findVOById\" resultMap=\"").concat(voVariable, "\" parameterMap=\"map\">\n      SELECT * FROM ").concat(params.model.tableName, " WHERE ID = #{id}\n  </select>\n  \n  <!-- \u6839\u636EID\u6570\u7EC4\u67E5\u8BE2\u8868\u4FE1\u606F author by ").concat(author, " -->\n  <select id=\"findByIds\" resultMap=\"").concat(pojoVariable, "\" parameterMap=\"map\">\n      SELECT * FROM ").concat(params.model.tableName, " WHERE \n      <foreach collection=\"ids\" item=\"item\" open=\"ID in  (\" close=\")\" separator=\",\">\n        #{item}\n      </foreach>\n  </select>\n  \n  <!-- \u5206\u9875\u67E5\u8BE2\u8868 author by ").concat(author, " -->\n  <select id=\"find").concat(pojo, "Page\" resultMap=\"").concat(voVariable, "\" parameterMap=\"").concat(voVariable, "\">\n      SELECT * FROM ").concat(params.model.tableName, " WHERE 1=1 ORDER BY ID ASC\n  </select>\n</mapper>");
  fs.writeFileSync(path.join(params.releasePath, mapperName), template);
}

/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */

var getParamVariableFormat$1 = function getParamVariableFormat(str) {
  return str.charAt(0).toLowerCase() + str.substring(1);
};

function service (project, params) {
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


  var pojo = params.props.pojo;
  var vo = params.props.vo;
  var voVariable = getParamVariableFormat$1(vo);
  var author = project.owner;
  /**
   * 获取模版工具类
   */

  var tools = new TemplateTools(project);
  tools.updateProjectConfig();
  var serviceName = pojo + "Service";
  var now = new Date(); // 获取ID的类型

  var ID = params.model.tableCloums.filter(function (ele) {
    return ele.columnName === "id";
  });
  var IDType = ID[0] && ID[0].columnType;
  var template = "\npackage ".concat(getPackageName(params.releasePath, "com"), ";\n\nimport ").concat(tools.getPackageNameByFileName("MybatisDao" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("BusinessException" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("Grid" + FILE_SUFFIX), ";\nimport java.util.Set;\nimport java.util.List;\n\n/**\n * ").concat(serviceName + "服务", "\n * @author ").concat(author, "\n * @date ").concat(now, "\n * @version V${app.service.version}\n */\npublic interface ").concat(serviceName, " extends MybatisDao {\n    /**\n     * \u4FDD\u5B58\n     * @param ").concat(voVariable, "\n     * @author ").concat(author, "\n     */\n    void save").concat(pojo, " (").concat(vo, " ").concat(voVariable, ")throws BusinessException;\n    \n    /**\n     * \u66F4\u65B0 \n     * @param ").concat(voVariable, "\n     * @author ").concat(author, "\n     */\n    void update").concat(pojo, " (").concat(vo, " ").concat(voVariable, ")throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u67E5\u8BE2pojo\n     * @param id\n     * @author ").concat(author, "\n     */\n    ").concat(pojo, " findById(").concat(IDType, " id) throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u67E5\u8BE2vo\n     * @param id\n     * @author ").concat(author, "\n     */\n    ").concat(vo, " findVOById(").concat(IDType, " id) throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u96C6\u5408\u67E5\u8BE2\n     * @param ids\n     * @author ").concat(author, "\n     */\n    List<").concat(pojo, "> findByIds(Set<").concat(IDType, "> ids) throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u5220\u9664\u6570\u636E\n     * @param id\n     * @author ").concat(author, "\n     */\n    void deleteById(").concat(IDType, " id) throws BusinessException;\n\n    /**\n     * \u5206\u9875\u67E5\u8BE2\n     * @param ").concat(voVariable, "\n     * @author ").concat(author, "\n     * @return\n     */\n    Grid<").concat(vo, "> find").concat(pojo, "Page (").concat(vo, " ").concat(voVariable, ")throws BusinessException;\n}");
  fs.writeFileSync(path.join(params.releasePath, serviceName + FILE_SUFFIX), template);
}

/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */

var getParamVariableFormat = function getParamVariableFormat(str) {
  return str.charAt(0).toLowerCase() + str.substring(1);
};

function serviceImpl (project, params) {
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


  var pojo = params.props.pojo;
  var vo = params.props.vo;
  var voVariable = getParamVariableFormat(vo);
  var author = project.owner;
  /**
   * 获取模版工具类
   */

  var tools = new TemplateTools(project);
  tools.updateProjectConfig();
  tools.updateProjectConfig();
  var now = new Date();
  var serviceName = pojo + "Service";
  var serviceImplName = pojo + "ServiceImpl"; // 获取ID的类型

  var ID = params.model.tableCloums.filter(function (ele) {
    return ele.columnName === "id";
  });
  var IDType = ID[0] && ID[0].columnType;
  var template = "\npackage ".concat(getPackageName(params.releasePath, "com"), ";\n\nimport ").concat(tools.getPackageNameByFileName("MybatisDaoImpl" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("BusinessException" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("Grid" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("DataUtils" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("PropertyUtils" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("PageParameter" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(serviceName + FILE_SUFFIX), ";\n\nimport org.apache.dubbo.config.annotation.DubboService;\nimport java.util.HashMap;\nimport java.util.List;\nimport java.util.Map;\nimport java.util.Set;\n\n/**\n * ").concat(serviceName + "服务实现类", "\n * @author ").concat(author, "\n * @date ").concat(now, "\n * @version V${app.service.version}\n */\n@DubboService(version = \"") + "${app.service.version}" + "\", retries = -1, timeout = 6000)\npublic class ".concat(serviceName + "Impl", " extends MybatisDaoImpl implements ").concat(serviceName, " {\n\n    public final String className = \"").concat(pojo, "\";\n    \n    /**\n     * \u4FDD\u5B58\n     * @param ").concat(voVariable, "\n     * @author ").concat(author, "\n     */\n    @Override\n    public void save").concat(pojo, " (").concat(vo, " ").concat(voVariable, "){\n        ").concat(pojo, " ").concat(getParamVariableFormat(pojo), " = new ").concat(pojo, "();\n        DataUtils.copyPropertiesIgnoreNull(").concat(voVariable, ", ").concat(getParamVariableFormat(pojo), ");\n        super.save(").concat(getParamVariableFormat(pojo), ");\n    }\n    \n    /**\n     * \u66F4\u65B0 \n     * @param ").concat(voVariable, "\n     * @author ").concat(author, "\n     */\n    @Override\n    public void update").concat(pojo, " (").concat(vo, " ").concat(voVariable, "){\n        ").concat(pojo, " dbObj = this.findById(").concat(voVariable, ".getId());\n        if (dbObj == null) {\n            throw new BusinessException(\"\u6570\u636E\u4E0D\u5B58\u5728\");\n        }\n        DataUtils.copyPropertiesIgnoreNull(").concat(voVariable, ", dbObj);\n        super.update(dbObj);\n    }\n\n    /**\n     * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n     * @param id\n     * @author ").concat(author, "\n     */\n    @Override\n    public ").concat(pojo, " findById(").concat(IDType, " id) {\n        Map<String, Object> map = new HashMap<>();\n        map.put(\"id\", id);\n        map.put(\"column\", PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n        return this.baseDao.selectOne(className + \".findById\", map);\n    }\n\n    /**\n     * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n     * @param id\n     * @author ").concat(author, "\n     */\n    @Override\n    public ").concat(vo, " findVOById(").concat(IDType, " id){\n        Map<String, Object> map = new HashMap<>();\n        map.put(\"id\", id);\n        map.put(\"column\", PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n        return this.baseDao.selectOne(className + \".findVOById\", map);\n    }\n\n    /**\n     * \u6839\u636Eids \u67E5\u8BE2\n     * @param ids id\u96C6\u5408\n     * @author ").concat(author, "\n     */\n    @Override\n    public List<").concat(pojo, "> findByIds(Set<").concat(IDType, "> ids) {\n        Map<String, Object> map = new HashMap<String, Object>();\n        map.put(\"ids\", ids);\n        map.put(\"column\", PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n        return this.baseDao.selectList(className + \".findByIds\", map);\n    }\n\n    /**\n     * \u6839\u636EID\u5220\u9664\u6570\u636E\n     * @param id\n     * @author ").concat(author, "\n     */\n    @Override\n    public void deleteById(").concat(IDType, " id) {\n      ").concat(pojo, " dbObj = this.findById(id);\n      if (dbObj == null) {\n          throw new BusinessException(\"\u6570\u636E\u4E0D\u5B58\u5728\");\n      }\n      this.delete(dbObj);\n    }\n\n    /**\n     * \u5206\u9875\u67E5\u8BE2\n     * @param ").concat(voVariable, "\n     * @author ").concat(author, "\n     */\n    @Override\n    public Grid<").concat(vo, "> find").concat(pojo, "Page (").concat(vo, " ").concat(voVariable, "){\n      ").concat(voVariable, ".setColumn(PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n      ").concat(voVariable, ".setPageParameter(new PageParameter(").concat(voVariable, ".getPage(), ").concat(voVariable, ".getRows()));\n      Grid<").concat(vo, "> grid = new Grid<>();\n      List<").concat(vo, "> list = super.baseDao.selectList(className + \".find").concat(pojo, "Page\", ").concat(voVariable, ");\n      grid.setCount(Long.valueOf(").concat(voVariable, ".getPageParameter().getTotalCount()));\n      grid.setList(list);\n      return grid;\n    }\n}");
  fs.writeFileSync(path.join(params.releasePath, serviceImplName + FILE_SUFFIX), template);
}

function controller (project, params) {
  /**
   * 检验参数是否正常
   */
  if (params.props.pojo == undefined || params.props.pojo == "") {
    throw new Error("pojo 必传");
  }

  if (params.props.vo == undefined || params.props.vo == "") {
    throw new Error("vo 必传");
  }

  if (params.model == undefined) {
    throw new Error("model 必传");
  }
  /**
   * 根据传递的参数生成template需要的参数
   */


  var pojo = params.props.pojo;
  var pojoVariable = getParamVariableFormat$2(pojo);
  var vo = params.props.vo;
  var voVariable = getParamVariableFormat$2(vo);
  var author = project.owner;
  /**
   * 获取模版工具类
   */

  var tools = new TemplateTools(project);
  tools.updateProjectConfig();
  var tableColArr = params.model.tableCloums;
  var now = new Date();
  var serviceName = pojo + "Service";
  var serviceNameVariable = getParamVariableFormat$2(serviceName);
  var controllerName = pojo + "Controller"; // 获取ID的类型

  var ID = params.model.tableCloums.filter(function (ele) {
    return ele.columnName === "id";
  });
  var IDType = ID[0] && ID[0].columnType;
  var tableCol = "";
  var tableColCheck = ""; // 生成非空判断，用于保存接口

  if (tableColArr && tableColArr.length > 0) {
    tableCol = "@ApiImplicitParams({\n        ".concat(tableColArr.map(function (ele) {
      return '@ApiImplicitParam(name = "' + ele.columnName + '", value = "' + (ele.columnComment || ele.columnName) + '", required = true, dataType = "' + ele.columnType + '", paramType = "query")';
    }).join(",\r\n\t\t"), "})");
    tableColCheck = "CheckExistParamUtil.getInstance().\n      ".concat(tableColArr.map(function (ele) {
      return 'addCheckParam("' + ele.columnName + '", ' + voVariable + ".get" + getSetFormat(ele.columnName) + '(), "' + (ele.columnComment || ele.columnName) + '")';
    }).join(".\r\n\t\t"), "\n      .check();");
  }

  var template = "\npackage ".concat(getPackageName(params.releasePath, "com"), ";\n\nimport ").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("BusinessException" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("Grid" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("CheckExistParamUtil" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(serviceName + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("CommonBaseController" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("ResultEnum" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("StringUtil" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("ResultInfo" + FILE_SUFFIX), ";\n\nimport java.util.HashSet;\nimport java.util.List;\nimport io.swagger.annotations.ApiImplicitParam;\nimport io.swagger.annotations.ApiImplicitParams;\nimport io.swagger.annotations.Api;\nimport io.swagger.annotations.ApiOperation;\nimport org.springframework.web.bind.annotation.RequestBody;\nimport org.springframework.web.bind.annotation.RequestMapping;\nimport org.springframework.web.bind.annotation.RequestMethod;\nimport org.springframework.web.bind.annotation.RestController;\n\nimport org.apache.dubbo.config.annotation.DubboReference;\n\n/**\n * @author ").concat(author, "\n * @date ").concat(now, "\n * @version V${app.service.version}\n */\n@RestController\n@RequestMapping(value = \"/api/").concat(pojoVariable, "\")\n@Api(value = \"").concat(controllerName, "\", tags = { \"").concat(pojo, "\u64CD\u4F5C\u63A5\u53E3\" })\npublic class ").concat(controllerName, " extends CommonBaseController{\n  @DubboReference(version = \"") + "${app.service.version}" + "\", check = false)\n  ".concat(serviceName, " ").concat(serviceNameVariable, ";\n\n  /**\n   * \u4FDD\u5B58\n   * @param ").concat(voVariable, "\n   * @author ").concat(author, "\n   */\n  ").concat(tableCol, "\n  @ApiOperation(value = \"\u4FDD\u5B58\u4FE1\u606F\", notes = \"\u4FDD\u5B58\u4FE1\u606F\")\n  @RequestMapping(value = \"/save").concat(pojo, "\", method = RequestMethod.POST)\n  public ResultInfo<Object> save").concat(pojo, " (@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      ").concat(tableColCheck, "\n      ").concat(serviceNameVariable, ".save").concat(pojo, "(").concat(voVariable, ");\n      return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), \"\u4FDD\u5B58\u6210\u529F\", null);\n  }\n  \n  /**\n   * \u66F4\u65B0 \n   * @param ").concat(voVariable, "\n   * @author ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"").concat(IDType, "\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u66F4\u65B0\u4FE1\u606F\", notes = \"\u66F4\u65B0\u4FE1\u606F\")\n  @RequestMapping(value = \"/update").concat(pojo, "\", method = RequestMethod.POST)\n  public ResultInfo<Object>  update").concat(pojo, " (@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      CheckExistParamUtil.getInstance()\n        .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n        .check();\n      ").concat(serviceNameVariable, ".update").concat(pojo, "(").concat(voVariable, ");\n      return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), \"\u66F4\u65B0\u6210\u529F\", null);\n  }\n\n  /**\n   * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n   * @param ").concat(voVariable, "\n   * @author ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"").concat(IDType, "\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\", notes = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\")\n  @RequestMapping(value = \"/findById\", method = RequestMethod.POST)\n  public ResultInfo<").concat(pojo, "> findById(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, ") {\n      CheckExistParamUtil.getInstance()\n        .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n        .check();\n      return new ResultInfo<").concat(pojo, ">(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", ").concat(serviceNameVariable, ".findById(").concat(voVariable, ".getId()));\n  }\n\n  /**\n   * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n   * @param ").concat(voVariable, "\n   * @author ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"").concat(IDType, "\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\", notes = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\")\n  @RequestMapping(value = \"/findVOById\", method = RequestMethod.POST)\n  public ResultInfo<").concat(vo, "> findvoById(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n          .check();\n      return new ResultInfo<").concat(vo, ">(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", ").concat(serviceNameVariable, ".findVOById(").concat(voVariable, ".getId()));\n  }\n\n  /**\n   * \u6839\u636Eids \u67E5\u8BE2\n   * @param ").concat(voVariable, " id\u96C6\u5408\n   * @author ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"ids\", value = \"ID\u96C6\u5408 \uFF0C\u5206\u9694\", required = true, dataType = \"String\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636Eids \u67E5\u8BE2\", notes = \"\u6839\u636Eids \u67E5\u8BE2\")\n  @RequestMapping(value = \"/findByIds\", method = RequestMethod.POST)\n  public ResultInfo<List<").concat(pojo, ">> findByIds(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, ") {\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"ids\", ").concat(voVariable, ".getIds(), \"ids\")\n          .check();\n      return new ResultInfo<List<").concat(pojo, ">>(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", ").concat(serviceNameVariable, ".findByIds(new HashSet<").concat(IDType, ">(StringUtil.stringTo").concat(IDType, "List(").concat(voVariable, ".getIds()))));\n  }\n\n  /**\n   * \u6839\u636EID\u5220\u9664\u6570\u636E\n   * @param ").concat(voVariable, "\n   * @author ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"").concat(IDType, "\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636EID\u5220\u9664\u6570\u636E\", notes = \"\u6839\u636EID\u5220\u9664\u6570\u636E\")\n  @RequestMapping(value = \"/deleteById\", method = RequestMethod.POST)\n  public ResultInfo<Object> deleteById(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, ") {\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n          .check();\n      ").concat(serviceNameVariable, ".deleteById(").concat(voVariable, ".getId());\n      return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", null);\n  }\n\n  /**\n   * \u5206\u9875\u67E5\u8BE2\n   * @param ").concat(voVariable, "\n   * @author ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"page\", value = \"\u5F53\u524D\u9875\u6570\", required = true, dataType = \"Integer\", paramType = \"query\"),\n    @ApiImplicitParam(name = \"rows\", value = \"\u6761\u6570\", required = true, dataType = \"Integer\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u5206\u9875\u67E5\u8BE2\", notes = \"\u5206\u9875\u67E5\u8BE2\")\n  @RequestMapping(value = \"/find").concat(pojo, "Page\", method = RequestMethod.POST)\n  public ResultInfo<Grid<").concat(vo, ">> find").concat(pojo, "Page (@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"page\", ").concat(voVariable, ".getPage(), \"page\")\n          .addCheckParam(\"rows\", ").concat(voVariable, ".getRows(), \"rows\")\n          .check();\n      return new ResultInfo<Grid<").concat(vo, ">>(ResultEnum.SUCCESS.getCode(), \"\u67E5\u8BE2\u6210\u529F\", ").concat(serviceNameVariable, ".find").concat(pojo, "Page(").concat(voVariable, "));\n  }\n}\n      ");
  fs.writeFileSync(path.join(params.releasePath, controllerName + FILE_SUFFIX), template);
}

function unitTest (project, params) {
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


  var pojo = params.props.pojo;
  var vo = params.props.vo;
  var voVariable = getParamVariableFormat$2(vo);
  var author = project.owner;
  /**
   * 获取模版工具类
   */

  var tools = new TemplateTools(project);
  tools.updateProjectConfig();
  var now = new Date();
  var serviceName = pojo + "Service";
  var serviceNameVariable = getParamVariableFormat$2(serviceName);
  var e2eName = pojo + "Test";
  var sortName = params.releasePath.substring(0, params.releasePath.indexOf("/src/"));
  var application = tools.findByKey("Application.java", 1).filter(function (ele) {
    return ele.value.indexOf(sortName) >= 0;
  });
  var template = "\npackage com.".concat(project.projectName, ";\nimport ").concat(getPackageName(application[0].value, "com"), ";\nimport org.junit.Test;\nimport org.junit.runner.RunWith;\nimport org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.boot.test.context.SpringBootTest;\nimport org.springframework.test.context.junit4.SpringRunner;\n\nimport com.alibaba.fastjson.JSON;\nimport ").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(serviceName + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("Grid" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("StringUtil" + FILE_SUFFIX), ";\nimport java.util.HashSet;\nimport java.util.List;\nimport java.util.Set;\n\n/**\n * ").concat(serviceName, " \u5355\u5143\u6D4B\u8BD5\n * @author ").concat(author, "\n * @date ").concat(now, "\n * @version V${app.service.version}\n */\n@RunWith(SpringRunner.class)\n@SpringBootTest(classes = Application.class)\n//@Rollback\n//@Transactional\npublic class ").concat(e2eName, " {\n    private final Logger logger = LoggerFactory.getLogger(getClass());\n    @Autowired\n    private ").concat(serviceName, " ").concat(serviceNameVariable, ";\n\n    /**\n     * \u4FDD\u5B58\n     * @author ").concat(author, "\n     */\n    @Test\n    public void save").concat(pojo, " (){\n        ").concat(vo, " ").concat(voVariable, " = new ").concat(vo, " ();\n        ").concat(voVariable, ".setId(1L);\n        //TODO: \u751F\u6210\u5FC5\u586B\u5B57\u6BB5\n        ").concat(serviceNameVariable, ".save").concat(pojo, "(").concat(voVariable, ");\n        logger.debug(\"save ------\" + JSON.toJSONString(").concat(voVariable, "));\n    }\n    \n    /**\n     * \u66F4\u65B0 \n     * @author ").concat(author, "\n     */\n    @Test\n    public void update").concat(pojo, " (){\n        ").concat(vo, " ").concat(voVariable, " = new ").concat(vo, " ();\n        ").concat(voVariable, ".setId(1L);\n        ").concat(serviceNameVariable, ".update").concat(pojo, "(").concat(voVariable, ");\n      logger.debug(\"update ------\" + JSON.toJSONString(").concat(voVariable, "));\n    }\n\n    /**\n     * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n     * @author ").concat(author, "\n     */\n    @Test\n    public void findById() {\n        ").concat(pojo, " db = ").concat(serviceNameVariable, ".findVOById(1L);\n        logger.error(\"findById ------\" + JSON.toJSONString(db));\n    }\n\n    /**\n     * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n     * @author ").concat(author, "\n     */\n    @Test\n    public void findVOById(){\n        ").concat(pojo, " db = ").concat(serviceNameVariable, ".findVOById(1L);\n        logger.error(\"findVOById ------\" + JSON.toJSONString(db));\n    }\n\n    /**\n     * \u6839\u636Eids \u67E5\u8BE2\n     * @author ").concat(author, "\n     */\n    @Test\n    public void findByIds() {\n        List<").concat(pojo, "> list =").concat(serviceNameVariable, ".findByIds(new HashSet<Long>(StringUtil.stringToLongList(\"1,2,3\")));\n        logger.error(\"findByIds ------\" + JSON.toJSONString(list));\n    }\n\n    /**\n     * \u6839\u636EID\u5220\u9664\u6570\u636E\n     * @author ").concat(author, "\n     */\n    @Test\n    public void deleteById() {\n        ").concat(serviceNameVariable, ".deleteById(1L);\n        logger.error(\"deleteById ------ success\");\n    }\n\n    /**\n     * \u5206\u9875\u67E5\u8BE2\n     * @author ").concat(author, "\n     */\n    @Test\n    public void find").concat(pojo, "Page (){\n        ").concat(vo, " ").concat(voVariable, " = new ").concat(vo, "();\n        ").concat(voVariable, ".setPage(1);\n        ").concat(voVariable, ".setRows(10);\n        Grid find").concat(pojo, "Page = ").concat(serviceNameVariable, ".find").concat(pojo, "Page(").concat(voVariable, ");\n        logger.error(\"find").concat(pojo, "Page ------ \" + JSON.toJSONString(find").concat(pojo, "Page));\n    }\n}");
  fs.writeFileSync(path.join(params.releasePath, e2eName + FILE_SUFFIX), template);
}

/**
 * 根据文件地址读取文件
 * @param filePath
 */

function readFile(filePath) {
  var stats = fs.statSync(filePath);

  if (stats.isFile()) {
    return fs.readFileSync(filePath, "utf-8");
  } else {
    throw new Error("传入的参数必须为文件地址");
  }
}

var CodeGenerator = /*#__PURE__*/function () {
  function CodeGenerator(project) {
    _classCallCheck(this, CodeGenerator);

    if (project.type !== 1) {
      throw Error("模版类型不一致");
    }

    this.project = project;
  }

  _createClass(CodeGenerator, [{
    key: "init",
    value: function init(params) {
      var tools = new TemplateTools(this.project);
      tools.init();
    }
  }, {
    key: "generatorPojo",
    value: function generatorPojo(params) {
      pojo(this.project, params);
    }
  }, {
    key: "generatorVO",
    value: function generatorVO(params) {
      vo(this.project, params);
    }
  }, {
    key: "generatorService",
    value: function generatorService(params) {
      service(this.project, params);
    }
  }, {
    key: "generatorMapper",
    value: function generatorMapper(params) {
      mapper(this.project, params);
    }
  }, {
    key: "generatorController",
    value: function generatorController(params) {
      controller(this.project, params);
    }
  }, {
    key: "generatorServiceImpl",
    value: function generatorServiceImpl(params) {
      serviceImpl(this.project, params);
    }
  }, {
    key: "generatorUnitTest",
    value: function generatorUnitTest(params) {
      unitTest(this.project, params);
    }
    /**
     * 根据pojo文件地址 逆向解析模型类 model -> json 格式
     * @param filePath
     */

  }, {
    key: "getModelByPojoPath",
    value: function getModelByPojoPath(filePath) {
      //读取 filePath 文件
      var fileText = readFile(filePath); //解析 fileText

      var typeArr = ["Long", "Integer", "String", "Date", "Double", "List", "BigDecimal"];
      var tableName = fileText.substring(fileText.search(/@+/), fileText.search(/public+/)).split("@").find(function (ele) {
        return ele.indexOf("Entity") >= 0;
      }).replace(/\s/g, "").split("=")[1].replace(/\"|\)/g, "");
      var tableCnName = fileText.substring(fileText.search(/@+/), fileText.search(/public+/)).split("@").find(function (ele) {
        return ele.indexOf("ApiModel") >= 0;
      }).replace(/\s/g, "").split("=")[1].replace(/\"|\)/g, "");
      var tableColArr = fileText.substring(fileText.search(/{+/) + 1, fileText.length - 1).split(/public+(\S*)/g)[0].trim().replace(/\r\n/g, "").split(";").filter(function (ele) {
        // 过滤空数据
        if (ele.length >= 0) {
          return ele;
        }
      }).map(function (ele) {
        var name = "";
        var result = ele.trim().split(/\)/).filter(function (ele) {
          if (ele.indexOf("ApiModelProperty") >= 0) {
            // 处理下第一行
            ele = ele.replace(/\s/g, "");
            name = ele && ele.length > 0 ? ele.split("=")[1].replace(/\"|\)/g, "") : "";
            return false;
          } else {
            return true;
          }
        });
        result = result[result.length - 1].split(/\s/g); // 处理单条数据

        var type = result.find(function (ele) {
          return typeArr.indexOf(ele) >= 0;
        }); // 类型后面

        var col = result[result.indexOf(type) + 1];
        return {
          columnComment: name,
          columnType: type,
          columnName: col
        };
      });
      return {
        tableName: tableName,
        tableComment: tableCnName,
        tableCloums: tableColArr
      };
    }
  }, {
    key: "updateProjectConfig",
    value: function updateProjectConfig() {
      var tools = new TemplateTools(this.project);
      return tools.updateProjectConfig();
    }
  }]);

  return CodeGenerator;
}();

export { CodeGenerator as default };
