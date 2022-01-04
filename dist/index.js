/*!
  * code-dubbo-template v0.0.9
  * (c) 2022 biqi li
  * @license MIT
  */
(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory(require('path'), require('fs'), require('parse-gitignore')) :
  typeof define === 'function' && define.amd ? define(['path', 'fs', 'parse-gitignore'], factory) :
  (global = typeof globalThis !== 'undefined' ? globalThis : global || self, global.index = factory(global.path, global.fs, global.parseIgnore));
})(this, (function (path, fs, parseIgnore) { 'use strict';

  function _interopDefaultLegacy (e) { return e && typeof e === 'object' && 'default' in e ? e : { 'default': e }; }

  var path__default = /*#__PURE__*/_interopDefaultLegacy(path);
  var fs__default = /*#__PURE__*/_interopDefaultLegacy(fs);
  var parseIgnore__default = /*#__PURE__*/_interopDefaultLegacy(parseIgnore);

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

  /** 配置文件默认名称 */

  var TEMPLATE_JSON = "cfconfig.json";
  /** 静态目录模版目录名 */

  var TEMPLATE_MODEL_NAME = "createTemplate";
  /** 忽略的文件 */

  var EXCLUDE_PATH = parseIgnore__default["default"](fs__default["default"].readFileSync(path__default["default"].join(__dirname, ".cfignore")));
  /** Java文件后缀 */

  var FILE_SUFFIX = ".java";
  /**
   * 根据文件路径获取包名
   * @param filePath 文件路径
   * @param startFix 包名前缀
   */

  var getPackageName = function getPackageName(filePath, startFix) {
    if (filePath.length === 0 || startFix.length === 0) {
      throw new Error("缺少参数!");
    }

    var fileObj = path__default["default"].parse(filePath);
    var filePathArr = path__default["default"].join(fileObj.dir, fileObj.name).split(path__default["default"].sep);
    return filePathArr.filter(function (_ele, index) {
      return index >= filePathArr.indexOf(startFix);
    }).join(".");
  };
  /**
   * 根据传入实体类获取参数变量
   * @param {*} str
   */

  var getParamVariableFormat$3 = function getParamVariableFormat(str) {
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
        templateId: 0,
        projectDir: "",
        projectName: "",
        type: 1,
        description: ""
      };
      this.keyPathArr = []; // 项目最终路径

      this.projectPath = ""; // 配置文件路径

      this.configPath = "";
      this.templateDir = "";
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

        var stats = fs__default["default"].statSync(configPath);

        if (stats.isFile()) {
          var jsonData = JSON.parse(fs__default["default"].readFileSync(configPath, "utf-8")); // 处理项目文件目录

          if (isUpdate) {
            // 重新生成
            jsonData.path = path__default["default"].parse(configPath).dir;

            if (jsonData.formData && jsonData.formData !== undefined) {
              // buildPath 去除项目名称
              var arr = path__default["default"].parse(configPath).dir.split(path__default["default"].sep);
              arr.pop();
              jsonData.formData.buildPath = arr.join(path__default["default"].sep);
            }

            return _this.showStructure(jsonData.formData, jsonData);
          }

          return jsonData;
        }

        throw Error("文件地址格式不正确！");
      };

      this.templateDir = path__default["default"].join(__dirname, "../../playground/createTemplate");
      this.project = pj;
      this.keyPathArr = [];
      this.projectPath = path__default["default"].join(pj.projectDir, pj.projectName);
      this.configPath = path__default["default"].join(this.projectPath, TEMPLATE_JSON);
    }
    /**
     * 根据文件名、搜索目录获取唯一文件
     * @param fileName
     */


    _createClass(TemplateTools, [{
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
      /**
       * 拷贝模版代码，复制模版代码，内部做关键字替换
       * @param structure
       */

    }, {
      key: "copyCoding",
      value: function copyCoding(structure) {
        var _this2 = this;

        if (!fs__default["default"].existsSync(structure.path)) {
          fs__default["default"].mkdirSync(structure.path);
        }

        if (structure.isDir) {
          if (structure.children.length > 0) {
            // 如果是文件夹
            structure.children.forEach(function (obj) {
              // 如果子目录是dir
              if (obj.isDir) { _this2.copyCoding(obj); }else {
                var data = fs__default["default"].readFileSync(obj.path || "", "utf8");
                var result = data.replace(new RegExp(TEMPLATE_MODEL_NAME, "g"), _this2.project.projectName);
                fs__default["default"].writeFileSync(obj.path, result, "utf8");
              }
            });
          }
        } else {
          // 如果不是文件夹
          var data = fs__default["default"].readFileSync(structure.path || "", "utf8");
          var result = data.replace(new RegExp(TEMPLATE_MODEL_NAME, "g"), this.project.projectName);
          fs__default["default"].writeFileSync(structure.path, result, "utf8");
        }
      }
      /**
       * 遍历文件目录结构
       * @param fileObj
       */

    }, {
      key: "fileDisplay",
      value: function fileDisplay(fileObj) {
        var _this3 = this;

        // 根据文件路径读取文件，返回文件列表
        var files = fs__default["default"].readdirSync(fileObj.path); // 遍历读取到的文件列表

        files.forEach(function (fileName) {
          // 获取当前文件的绝对路径
          var filedir = path__default["default"].join(fileObj.path, fileName); // 根据文件路径获取文件信息，返回一个fs.Stats对象

          var stats = fs__default["default"].statSync(filedir);
          var isFile = stats.isFile(); // 是文件

          var isDir = stats.isDirectory(); // 是文件夹

          var isExcludeFlag = EXCLUDE_PATH.filter(function (ele) {
            return filedir.indexOf(ele) >= 0;
          });

          if (isExcludeFlag.length > 0) {
            return;
          }

          if (isFile) {
            // 根据 fileObj 判读缓存数据 是否存在父亲目录
            var fileArr = fileObj.children.filter(function (ele) {
              return ele.path === fileObj.path;
            });
            var obj = {
              fileName: fileName,
              path: filedir,
              sortPath: path__default["default"].relative(_this3.projectPath, filedir),
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
            var _obj = {
              fileName: fileName,
              path: filedir,
              sortPath: path__default["default"].relative(_this3.projectPath, filedir),
              isDir: isDir,
              children: []
            }; // 根据 fileObj 判读缓存数据 是否存在父亲目录

            var dirArr = fileObj.children.filter(function (ele) {
              return ele.path === fileObj.path;
            }); // 如果有父级

            if (dirArr.length === 1) {
              dirArr[0].children.push(_obj);
            } else {
              fileObj.children.push(_obj);
            }

            _this3.fileDisplay(_obj); // 递归，如果是文件夹，就继续遍历该文件夹下面的文件

          }
        });
      }
      /**
       * 获取模版文件结构
       * @param formData
       * @param obj
       */

    }, {
      key: "showStructure",
      value: function showStructure(formData, obj) {
        var dirStructure = {
          fileName: obj ? obj.fileName : TEMPLATE_MODEL_NAME,
          path: obj ? obj.path : this.templateDir,
          sortPath: obj ? path__default["default"].relative(this.projectPath, obj.path) : path__default["default"].relative(this.projectPath, this.templateDir),
          formData: formData,
          isDir: true,
          children: []
        };
        this.fileDisplay(dirStructure);
        return dirStructure;
      }
      /**
       * 更新项目结构
       */

    }, {
      key: "updateProjectDirJson",
      value: function updateProjectDirJson() {
        try {
          var jsonData = this.getJsonFromPath(true);
          fs__default["default"].writeFileSync(path__default["default"].join(this.projectPath, TEMPLATE_JSON), JSON.stringify(jsonData));
        } catch (error) {
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

    }, {
      key: "serachJSON",
      value: function serachJSON(jsonData, key, type) {
        var _this4 = this;

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
              _this4.serachJSON(obj, key, type);
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

  /**
   * 根据传入实体类获取参数变量
   * @param {*} str
   */

  var getParamVariableFormat$2 = function getParamVariableFormat(str) {
    return str.charAt(0).toLowerCase() + str.substring(1);
  };

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
    var mapperName = pojo + ".xml";
    /**
     * 获取模版工具类
     */

    var tools = new TemplateTools(project);
    var template = "\n    <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n    <!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n    <mapper namespace=\"".concat(pojo, "\">\n      <resultMap type=\"").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), "\" id=\"").concat(pojoVariable, "\"/>\n      <resultMap type=\"").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), "\" id=\"").concat(voVariable, "\"/>\n      <resultMap type=\"java.util.HashMap\" id=\"map\"/>\n      <parameterMap type=\"").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), "\" id=\"").concat(pojoVariable, "\"/>\n      <parameterMap type=\"").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), "\" id=\"").concat(voVariable, "\"/>\n      <parameterMap type=\"java.util.HashMap\" id=\"map\"/>\n        \n        <!-- \u6839\u636EID\u67E5\u8BE2\u8868\u4FE1\u606F author by ").concat(author, " -->\n      <select id=\"findById\" resultMap=\"").concat(pojoVariable, "\" parameterMap=\"map\">\n          SELECT * FROM ").concat(params.model.tableName, " WHERE ID = #{id}\n      </select>\n        \n        <!-- \u6839\u636EID\u67E5\u8BE2\u7528\u6237\u8BE6\u60C5 author by ").concat(author, " -->\n      <select id=\"findVOById\" resultMap=\"").concat(voVariable, "\" parameterMap=\"map\">\n          SELECT * FROM ").concat(params.model.tableName, " WHERE ID = #{id}\n      </select>\n      \n      <!-- \u6839\u636EID\u6570\u7EC4\u67E5\u8BE2\u8868\u4FE1\u606F author by ").concat(author, " -->\n      <select id=\"findByIds\" resultMap=\"").concat(pojoVariable, "\" parameterMap=\"map\">\n          SELECT * FROM ").concat(params.model.tableName, " WHERE \n          <foreach collection=\"ids\" item=\"item\" open=\"ID in  (\" close=\")\" separator=\",\">\n        #{item}\n      </foreach>\n      </select>\n      \n      <!-- \u5206\u9875\u67E5\u8BE2\u8868 author by ").concat(author, " -->\n      <select id=\"find").concat(pojo, "Page\" resultMap=\"").concat(voVariable, "\" parameterMap=\"").concat(voVariable, "\">\n          SELECT * FROM ").concat(params.model.tableName, " WHERE 1=1 ORDER BY ID ASC\n      </select>\n    </mapper>\n  ");
    fs__default["default"].writeFileSync(path__default["default"].join(params.releasePath, mapperName), template);
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
    var serviceName = pojo + "Service";
    var now = new Date();
    var template = "\npackage ".concat(getPackageName(params.releasePath, "com"), ";\n\nimport ").concat(tools.getPackageNameByFileName("MybatisDao" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("BusinessException" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("Grid" + FILE_SUFFIX), ";\nimport java.util.Set;\nimport java.util.List;\n\n/**\n * ").concat(serviceName + "服务", "\n * @author: ").concat(author, "\n * @date: ").concat(now, "\n * @version V${app.service.version}\n */\npublic interface ").concat(serviceName, " extends MybatisDao {\n    /**\n     * \u4FDD\u5B58\n     * @param ").concat(voVariable, "\n     * @author: ").concat(author, "\n     */\n    void save").concat(pojo, " (").concat(vo, " ").concat(voVariable, ")throws BusinessException;\n    \n    /**\n     * \u66F4\u65B0 \n     * @param ").concat(voVariable, "\n     * @author: ").concat(author, "\n     */\n    void update").concat(pojo, " (").concat(vo, " ").concat(voVariable, ")throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u67E5\u8BE2pojo\n     * @param id\n     * @author: ").concat(author, "\n     */\n    ").concat(pojo, " findById(Long id) throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u67E5\u8BE2vo\n     * @param id\n     * @author: ").concat(author, "\n     */\n    ").concat(vo, " findVOById(Long id) throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u96C6\u5408\u67E5\u8BE2\n     * @param ids\n     * @author: ").concat(author, "\n     */\n    List<").concat(pojo, "> findByIds(Set<Long> ids) throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u5220\u9664\u6570\u636E\n     * @param id\n     * @author: ").concat(author, "\n     */\n    void deleteById(Long id) throws BusinessException;\n\n    /**\n     * \u5206\u9875\u67E5\u8BE2\n     * @param ").concat(voVariable, "\n     * @author: ").concat(author, "\n     * @return\n     */\n    Grid<").concat(vo, "> find").concat(pojo, "Page (").concat(vo, " ").concat(voVariable, ")throws BusinessException;\n}\n");
    fs__default["default"].writeFileSync(path__default["default"].join(params.releasePath, serviceName + FILE_SUFFIX), template);
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
    var now = new Date();
    var serviceName = pojo + "Service";
    var serviceImplName = pojo + "ServiceImpl";
    var template = "\n    package ".concat(getPackageName(params.releasePath, "com"), ";\n    \n    import ").concat(tools.getPackageNameByFileName("MybatisDaoImpl" + FILE_SUFFIX), ";\n    import ").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), ";\n    import ").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), ";\n    import ").concat(tools.getPackageNameByFileName("BusinessException" + FILE_SUFFIX), ";\n    import ").concat(tools.getPackageNameByFileName("Grid" + FILE_SUFFIX), ";\n    import ").concat(tools.getPackageNameByFileName("DataUtils" + FILE_SUFFIX), ";\n    import ").concat(tools.getPackageNameByFileName("PropertyUtils" + FILE_SUFFIX), ";\n    import ").concat(tools.getPackageNameByFileName("PageParameter" + FILE_SUFFIX), ";\n    import ").concat(tools.getPackageNameByFileName(serviceName + FILE_SUFFIX), ";\n    \n    import com.alibaba.dubbo.config.annotation.Service;\n    import java.util.HashMap;\n    import java.util.List;\n    import java.util.Map;\n    import java.util.Set;\n    \n    /**\n     * ").concat(serviceName + "服务实现类", "\n     * @author: ").concat(author, "\n     * @date: ").concat(now, "\n     * @version V${app.service.version}\n     */\n    @Service(version = \"") + "${app.service.version}" + "\", retries = -1, timeout = 6000)\n    public class ".concat(serviceName + "Impl", " extends MybatisDaoImpl implements ").concat(serviceName, " {\n    \n        public final String className = \"").concat(pojo, "\";\n        \n        /**\n         * \u4FDD\u5B58\n         * @param ").concat(voVariable, "\n         * @author: ").concat(author, "\n         */\n        @Override\n        public void save").concat(pojo, " (").concat(vo, " ").concat(voVariable, "){\n            ").concat(pojo, " ").concat(getParamVariableFormat(pojo), " = new ").concat(pojo, "();\n            DataUtils.copyPropertiesIgnoreNull(").concat(voVariable, ", ").concat(getParamVariableFormat(pojo), ");\n            super.save(").concat(getParamVariableFormat(pojo), ");\n        }\n        \n        /**\n         * \u66F4\u65B0 \n         * @param ").concat(voVariable, "\n         * @author: ").concat(author, "\n         */\n        @Override\n        public void update").concat(pojo, " (").concat(vo, " ").concat(voVariable, "){\n            ").concat(pojo, " dbObj = this.findById(").concat(voVariable, ".getId());\n            if (dbObj == null) {\n                throw new BusinessException(\"\u6570\u636E\u4E0D\u5B58\u5728\");\n            }\n            DataUtils.copyPropertiesIgnoreNull(").concat(voVariable, ", dbObj);\n            super.update(dbObj);\n        }\n    \n        /**\n         * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n         * @param id\n         * @author: ").concat(author, "\n         */\n        @Override\n        public ").concat(pojo, " findById(Long id) {\n            Map<String, Object> map = new HashMap<>();\n            map.put(\"id\", id);\n            map.put(\"column\", PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n            return this.baseDao.selectOne(className + \".findById\", map);\n        }\n    \n        /**\n         * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n         * @param id\n         * @author: ").concat(author, "\n         */\n        @Override\n        public ").concat(vo, " findVOById(Long id){\n            Map<String, Object> map = new HashMap<>();\n            map.put(\"id\", id);\n            map.put(\"column\", PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n            return this.baseDao.selectOne(className + \".findVOById\", map);\n        }\n    \n        /**\n         * \u6839\u636Eids \u67E5\u8BE2\n         * @param ids id\u96C6\u5408\n         * @author: ").concat(author, "\n         */\n        @Override\n        public List<").concat(pojo, "> findByIds(Set<Long> ids) {\n            Map<String, Object> map = new HashMap<String, Object>();\n            map.put(\"ids\", ids);\n            map.put(\"column\", PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n            return this.baseDao.selectList(className + \".findByIds\", map);\n        }\n    \n        /**\n         * \u6839\u636EID\u5220\u9664\u6570\u636E\n         * @param id\n         * @author: ").concat(author, "\n         */\n        @Override\n        public void deleteById(Long id) {\n          ").concat(pojo, " dbObj = this.findById(id);\n          if (dbObj == null) {\n              throw new BusinessException(\"\u6570\u636E\u4E0D\u5B58\u5728\");\n          }\n          this.delete(dbObj);\n        }\n    \n        /**\n         * \u5206\u9875\u67E5\u8BE2\n         * @param ").concat(voVariable, "\n         * @author: ").concat(author, "\n         */\n        @Override\n        public Grid<").concat(vo, "> find").concat(pojo, "Page (").concat(vo, " ").concat(voVariable, "){\n          ").concat(voVariable, ".setColumn(PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n          ").concat(voVariable, ".setPageParameter(new PageParameter(").concat(voVariable, ".getPage(), ").concat(voVariable, ".getRows()));\n          Grid<").concat(vo, "> grid = new Grid<>();\n          List<").concat(vo, "> list = super.baseDao.selectList(className + \".find").concat(pojo, "Page\", ").concat(voVariable, ");\n          grid.setCount(Long.valueOf(").concat(voVariable, ".getPageParameter().getTotalCount()));\n          grid.setList(list);\n          return grid;\n        }\n    }\n    ");
    fs__default["default"].writeFileSync(path__default["default"].join(params.releasePath, serviceImplName + FILE_SUFFIX), template);
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
    var pojoVariable = getParamVariableFormat$3(pojo);
    var vo = params.props.vo;
    var voVariable = getParamVariableFormat$3(vo);
    var author = project.owner;
    /**
     * 获取模版工具类
     */

    var tools = new TemplateTools(project);
    var tableColArr = params.model.tableColArr;
    var now = new Date();
    var serviceName = pojo + "Service";
    var serviceNameVariable = getParamVariableFormat$3(serviceName);
    var controllerName = pojo + "Controller";
    var tableCol = "";
    var tableColCheck = ""; // 生成非空判断，用于保存接口

    if (tableColArr && tableColArr.length > 0) {
      tableCol = "@ApiImplicitParams({\n        ".concat(tableColArr.map(function (ele) {
        return '@ApiImplicitParam(name = "' + ele.col + '", value = "' + (ele.name || ele.col) + '", required = true, dataType = "' + ele.type + '", paramType = "query")';
      }).join(",\r\n\t\t"), "})");
      tableColCheck = "CheckExistParamUtil.getInstance().\n      ".concat(tableColArr.map(function (ele) {
        return 'addCheckParam("' + ele.col + '", ' + voVariable + ".get" + getSetFormat(ele.col) + '(), "' + (ele.name || ele.col) + '")';
      }).join(".\r\n\t\t"), "\n      .check();");
    }

    var template = "\npackage ".concat(getPackageName(params.releasePath, "com"), ";\n\nimport ").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("BusinessException" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("Grid" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("CheckExistParamUtil" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(serviceName + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("CommonBaseController" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("ResultEnum" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("StringUtil" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("ResultInfo" + FILE_SUFFIX), ";\n\nimport java.util.HashSet;\nimport java.util.List;\nimport io.swagger.annotations.ApiImplicitParam;\nimport io.swagger.annotations.ApiImplicitParams;\nimport io.swagger.annotations.Api;\nimport io.swagger.annotations.ApiOperation;\nimport org.springframework.web.bind.annotation.RequestBody;\nimport org.springframework.web.bind.annotation.RequestMapping;\nimport org.springframework.web.bind.annotation.RequestMethod;\nimport org.springframework.web.bind.annotation.RestController;\n\nimport org.apache.dubbo.config.annotation.DubboReference;\n\n/**\n * @author: ").concat(author, "\n * @date: ").concat(now, "\n * @version V${app.service.version}\n */\n@RestController\n@RequestMapping(value = \"/api/").concat(pojoVariable, "\")\n@Api(value = \"").concat(controllerName, "\", tags = { \"").concat(pojo, "\u64CD\u4F5C\u63A5\u53E3\" })\npublic class ").concat(controllerName, " extends CommonBaseController{\n  @DubboReference(version = \"") + "${app.service.version}" + "\", check = false)\n  ".concat(serviceName, " ").concat(serviceNameVariable, ";\n\n  /**\n   * \u4FDD\u5B58\n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  ").concat(tableCol, "\n  @ApiOperation(value = \"\u4FDD\u5B58\u4FE1\u606F\", notes = \"\u4FDD\u5B58\u4FE1\u606F\")\n  @RequestMapping(value = \"/save").concat(pojo, "\", method = RequestMethod.POST)\n  public ResultInfo<Object> save").concat(pojo, " (@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      ").concat(tableColCheck, "\n      ").concat(serviceNameVariable, ".save").concat(pojo, "(").concat(voVariable, ");\n      return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), \"\u4FDD\u5B58\u6210\u529F\", null);\n  }\n  \n  /**\n   * \u66F4\u65B0 \n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"Long\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u66F4\u65B0\u4FE1\u606F\", notes = \"\u66F4\u65B0\u4FE1\u606F\")\n  @RequestMapping(value = \"/update").concat(pojo, "\", method = RequestMethod.POST)\n  public ResultInfo<Object>  update").concat(pojo, " (@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      CheckExistParamUtil.getInstance()\n        .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n        .check();\n      ").concat(serviceNameVariable, ".update").concat(pojo, "(").concat(voVariable, ");\n      return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), \"\u66F4\u65B0\u6210\u529F\", null);\n  }\n\n  /**\n   * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"Long\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\", notes = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\")\n  @RequestMapping(value = \"/findById\", method = RequestMethod.POST)\n  public ResultInfo<").concat(pojo, "> findById(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, ") {\n      CheckExistParamUtil.getInstance()\n        .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n        .check();\n      return new ResultInfo<").concat(pojo, ">(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", ").concat(serviceNameVariable, ".findById(").concat(voVariable, ".getId()));\n  }\n\n  /**\n   * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"Long\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\", notes = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\")\n  @RequestMapping(value = \"/findVOById\", method = RequestMethod.POST)\n  public ResultInfo<").concat(vo, "> findvoById(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n          .check();\n      return new ResultInfo<").concat(vo, ">(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", ").concat(serviceNameVariable, ".findvoById(").concat(voVariable, ".getId()));\n  }\n\n  /**\n   * \u6839\u636Eids \u67E5\u8BE2\n   * @param ").concat(voVariable, " id\u96C6\u5408\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"ids\", value = \"ID\u96C6\u5408 \uFF0C\u5206\u9694\", required = true, dataType = \"String\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636Eids \u67E5\u8BE2\", notes = \"\u6839\u636Eids \u67E5\u8BE2\")\n  @RequestMapping(value = \"/findByIds\", method = RequestMethod.POST)\n  public ResultInfo<List<").concat(pojo, ">> findByIds(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, ") {\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"ids\", ").concat(voVariable, ".getIds(), \"ids\")\n          .check();\n      return new ResultInfo<List<").concat(pojo, ">>(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", ").concat(serviceNameVariable, ".findByIds(new HashSet<Long>(StringUtil.stringToLongList(").concat(voVariable, ".getIds()))));\n  }\n\n  /**\n   * \u6839\u636EID\u5220\u9664\u6570\u636E\n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"Long\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636EID\u5220\u9664\u6570\u636E\", notes = \"\u6839\u636EID\u5220\u9664\u6570\u636E\")\n  @RequestMapping(value = \"/deleteById\", method = RequestMethod.POST)\n  public ResultInfo<Object> deleteById(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, ") {\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n          .check();\n      ").concat(serviceNameVariable, ".deleteById(").concat(voVariable, ".getId());\n      return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", null);\n  }\n\n  /**\n   * \u5206\u9875\u67E5\u8BE2\n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"page\", value = \"\u5F53\u524D\u9875\u6570\", required = true, dataType = \"Integer\", paramType = \"query\"),\n    @ApiImplicitParam(name = \"rows\", value = \"\u6761\u6570\", required = true, dataType = \"Integer\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u5206\u9875\u67E5\u8BE2\", notes = \"\u5206\u9875\u67E5\u8BE2\")\n  @RequestMapping(value = \"/find").concat(pojo, "Page\", method = RequestMethod.POST)\n  public ResultInfo<Grid<").concat(vo, ">> find").concat(pojo, "Page (@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"page\", ").concat(voVariable, ".getPage(), \"page\")\n          .addCheckParam(\"rows\", ").concat(voVariable, ".getRows(), \"rows\")\n          .check();\n      return new ResultInfo<Grid<").concat(vo, ">>(ResultEnum.SUCCESS.getCode(), \"\u67E5\u8BE2\u6210\u529F\", ").concat(serviceNameVariable, ".find").concat(pojo, "Page(").concat(voVariable, "));\n  }\n}\n      ");
    fs__default["default"].writeFileSync(path__default["default"].join(params.releasePath, controllerName + FILE_SUFFIX), template);
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
    var voVariable = getParamVariableFormat$3(vo);
    var author = project.owner;
    /**
     * 获取模版工具类
     */

    var tools = new TemplateTools(project);
    var now = new Date();
    var serviceName = pojo + "Service";
    var serviceNameVariable = getParamVariableFormat$3(serviceName);
    var e2eName = pojo + "Test";
    var template = "\npackage com.".concat(project.projectName, ";\nimport org.junit.Test;\nimport org.junit.runner.RunWith;\nimport org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.boot.test.context.SpringBootTest;\nimport org.springframework.test.context.junit4.SpringRunner;\n\nimport com.alibaba.fastjson.JSON;\nimport ").concat(tools.getPackageNameByFileName(pojo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(vo + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName(serviceName + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("Grid" + FILE_SUFFIX), ";\nimport ").concat(tools.getPackageNameByFileName("StringUtil" + FILE_SUFFIX), ";\nimport java.util.HashSet;\nimport java.util.List;\nimport java.util.Set;\n\n/**\n * ").concat(serviceName, " \u5355\u5143\u6D4B\u8BD5\n * @author: ").concat(author, "\n * @date: ").concat(now, "\n * @version V${app.service.version}\n */\n@RunWith(SpringRunner.class)\n@SpringBootTest(classes = Application.class)\n//@Rollback\n//@Transactional\npublic class ").concat(e2eName, " {\n  private final Logger logger = LoggerFactory.getLogger(getClass());\n  @Autowired\n  private ").concat(serviceName, " ").concat(serviceNameVariable, ";\n\n  /**\n   * \u4FDD\u5B58\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void save").concat(pojo, " (){\n      ").concat(vo, " ").concat(voVariable, " = new ").concat(vo, " ();\n      ").concat(voVariable, ".setId(1L);\n      //TODO: \u751F\u6210\u5FC5\u586B\u5B57\u6BB5\n      ").concat(serviceNameVariable, ".save").concat(pojo, "(").concat(voVariable, ");\n      logger.debug(\"save ------\" + JSON.toJSONString(").concat(voVariable, "));\n  }\n  \n  /**\n   * \u66F4\u65B0 \n   * @author: ").concat(author, "\n   */\n  @Test\n  public void update").concat(pojo, " (){\n      ").concat(vo, " ").concat(voVariable, " = new ").concat(vo, " ();\n      ").concat(voVariable, ".setId(1L);\n      ").concat(serviceNameVariable, ".update").concat(pojo, "(").concat(voVariable, ");\n    logger.debug(\"update ------\" + JSON.toJSONString(").concat(voVariable, "));\n  }\n\n  /**\n   * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void findById() {\n      ").concat(pojo, " db = ").concat(serviceNameVariable, ".findVOById(1L);\n      logger.error(\"findById ------\" + JSON.toJSONString(db));\n  }\n\n  /**\n   * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void findVOById(){\n      ").concat(pojo, " db = ").concat(serviceNameVariable, ".findVOById(1L);\n      logger.error(\"findVOById ------\" + JSON.toJSONString(db));\n  }\n\n  /**\n   * \u6839\u636Eids \u67E5\u8BE2\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void findByIds() {\n      List<").concat(pojo, "> list =").concat(serviceNameVariable, ".findByIds(new HashSet<Long>(StringUtil.stringToLongList(\"1,2,3\")));\n      logger.error(\"findByIds ------\" + JSON.toJSONString(list));\n  }\n\n  /**\n   * \u6839\u636EID\u5220\u9664\u6570\u636E\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void deleteById() {\n      ").concat(serviceNameVariable, ".deleteById(1L);\n      logger.error(\"deleteById ------ success\");\n  }\n\n  /**\n   * \u5206\u9875\u67E5\u8BE2\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void find").concat(pojo, "Page (){\n      ").concat(vo, " ").concat(voVariable, " = new ").concat(vo, "();\n      ").concat(voVariable, ".setPage(1);\n      ").concat(voVariable, ".setRows(10);\n      Grid find").concat(pojo, "Page = ").concat(serviceNameVariable, ".find").concat(pojo, "Page(").concat(voVariable, ");\n      logger.error(\"find").concat(pojo, "Page ------ \" + JSON.toJSONString(find").concat(pojo, "Page));\n  }\n}\n      ");
    fs__default["default"].writeFileSync(path__default["default"].join(params.releasePath, e2eName + FILE_SUFFIX), template);
  }

  /**
   * 根据文件地址读取文件
   * @param filePath
   */

  function readFile(filePath) {
    var stats = fs__default["default"].statSync(filePath);

    if (stats.isFile()) {
      return fs__default["default"].readFileSync(filePath, "utf-8");
    } else {
      throw new Error("传入的参数必须为文件地址");
    }
  }

  var CodeGenerator = /*#__PURE__*/function () {
    function CodeGenerator(project) {
      _classCallCheck(this, CodeGenerator);

      this.project = project;
    }

    _createClass(CodeGenerator, [{
      key: "init",
      value: function init(params) {
        console.log("init ok !");
      }
    }, {
      key: "generatorPojo",
      value: function generatorPojo(params) {
        console.log("generatorPojo ok !");
      }
    }, {
      key: "generatorVO",
      value: function generatorVO(params) {
        console.log("generatorVO ok !");
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
            name: name,
            type: type,
            col: col
          };
        });
        return {
          tableName: tableName,
          tableCnName: tableCnName,
          tableColArr: tableColArr
        };
      }
    }]);

    return CodeGenerator;
  }();

  return CodeGenerator;

}));
