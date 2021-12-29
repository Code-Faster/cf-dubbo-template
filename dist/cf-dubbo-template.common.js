/*!
  * cf-dubbo-template v0.0.1
  * (c) 2021 biqi li
  * @license MIT
  */
'use strict';

function _classCallCheck(instance, Constructor) {
  if (!(instance instanceof Constructor)) {
    throw new TypeError("Cannot call a class as a function");
  }
}

/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
var getParamVariableFormat$4 = function getParamVariableFormat(str) {
  return str.charAt(0).toLowerCase() + str.substring(1);
};

function mapper (releasePath, pojo, vo, pojoName, tableName, author, getPackageNameByFileName, getPackageName) {
  var voVariable = getParamVariableFormat$4(vo);
  var pojoVariable = getParamVariableFormat$4(pojo);
  return "\n    <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n    <!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n    <mapper namespace=\"".concat(pojo, "\">\n        <resultMap type=\"").concat(getPackageNameByFileName(pojo + ".java"), "\" id=\"").concat(pojoVariable, "\"/>\n      <resultMap type=\"").concat(getPackageNameByFileName(vo + ".java"), "\" id=\"").concat(voVariable, "\"/>\n      <resultMap type=\"java.util.HashMap\" id=\"map\"/>\n        <parameterMap type=\"").concat(getPackageNameByFileName(pojo + ".java"), "\" id=\"").concat(pojoVariable, "\"/>\n        <parameterMap type=\"").concat(getPackageNameByFileName(vo + ".java"), "\" id=\"").concat(voVariable, "\"/>\n        <parameterMap type=\"java.util.HashMap\" id=\"map\"/>\n        \n        <!-- \u6839\u636EID\u67E5\u8BE2\u8868\u4FE1\u606F create by ").concat(author, " -->\n        <select id=\"findById\" resultMap=\"").concat(pojoVariable, "\" parameterMap=\"map\">\n            SELECT * FROM ").concat(tableName, " WHERE ID = #{id}\n      </select>\n        \n        <!-- \u6839\u636EID\u67E5\u8BE2\u7528\u6237\u8BE6\u60C5 create by ").concat(author, " -->\n        <select id=\"findVOById\" resultMap=\"").concat(voVariable, "\" parameterMap=\"map\">\n            SELECT * FROM ").concat(tableName, " WHERE ID = #{id}\n      </select>\n      \n        <!-- \u6839\u636EID\u6570\u7EC4\u67E5\u8BE2\u8868\u4FE1\u606F create by ").concat(author, " -->\n        <select id=\"findByIds\" resultMap=\"").concat(pojoVariable, "\" parameterMap=\"map\">\n            SELECT * FROM ").concat(tableName, " WHERE \n            <foreach collection=\"ids\" item=\"item\" open=\"ID in  (\" close=\")\" separator=\",\">\n          #{item}\n        </foreach>\n        </select>\n        \n        <!-- \u5206\u9875\u67E5\u8BE2\u8868 create by ").concat(author, " -->\n        <select id=\"find").concat(pojo, "Page\" resultMap=\"").concat(voVariable, "\" parameterMap=\"").concat(voVariable, "\">\n            SELECT * FROM ").concat(tableName, " WHERE 1=1 ORDER BY ID ASC\n        </select>\n    \n    </mapper>\n  ");
}

/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
var getParamVariableFormat$3 = function getParamVariableFormat(str) {
  return str.charAt(0).toLowerCase() + str.substring(1);
};

function service (releasePath, pojo, vo, pojoName, author, getPackageNameByFileName, getPackageName) {
  var now = new Date();
  var voVariable = getParamVariableFormat$3(vo);
  var serviceName = pojoName + "Service";
  return "\npackage ".concat(getPackageName(releasePath, "com"), ";\n\nimport ").concat(getPackageNameByFileName("MybatisDao.java"), ";\nimport ").concat(getPackageNameByFileName(pojo + ".java"), ";\nimport ").concat(getPackageNameByFileName(vo + ".java"), ";\nimport ").concat(getPackageNameByFileName("BusinessException.java"), ";\nimport ").concat(getPackageNameByFileName("Grid.java"), ";\nimport java.util.Set;\nimport java.util.List;\n\n/**\n * ").concat(serviceName + "服务", "\n * @author: ").concat(author, "\n * @date: ").concat(now, "\n * @version V${app.service.version}\n */\npublic interface ").concat(serviceName, " extends MybatisDao {\n    /**\n     * \u4FDD\u5B58\n     * @param ").concat(voVariable, "\n     * @author: ").concat(author, "\n     */\n    void save").concat(pojo, " (").concat(vo, " ").concat(voVariable, ")throws BusinessException;\n    \n    /**\n     * \u66F4\u65B0 \n     * @param ").concat(voVariable, "\n     * @author: ").concat(author, "\n     */\n    void update").concat(pojo, " (").concat(vo, " ").concat(voVariable, ")throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u67E5\u8BE2pojo\n     * @param id\n     * @author: ").concat(author, "\n     */\n    ").concat(pojo, " findById(Long id) throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u67E5\u8BE2vo\n     * @param id\n     * @author: ").concat(author, "\n     */\n    ").concat(vo, " findVOById(Long id) throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u96C6\u5408\u67E5\u8BE2\n     * @param ids\n     * @author: ").concat(author, "\n     */\n    List<").concat(pojo, "> findByIds(Set<Long> ids) throws BusinessException;\n\n    /**\n     * \u6839\u636EID\u5220\u9664\u6570\u636E\n     * @param id\n     * @author: ").concat(author, "\n     */\n    void deleteById(Long id) throws BusinessException;\n\n    /**\n     * \u5206\u9875\u67E5\u8BE2\n     * @param ").concat(voVariable, "\n     * @author: ").concat(author, "\n     * @return\n     */\n    Grid<").concat(vo, "> find").concat(pojo, "Page (").concat(vo, " ").concat(voVariable, ")throws BusinessException;\n}\n");
}

/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
var getParamVariableFormat$2 = function getParamVariableFormat(str) {
  return str.charAt(0).toLowerCase() + str.substring(1);
};

function serviceImpl (extendsPath, releasePath, pojo, vo, pojoName, author, getPackageNameByFileName, getPackageName) {
  var now = new Date();
  var voVariable = getParamVariableFormat$2(vo);
  var serviceName = pojoName + "Service";
  return "\n    package ".concat(getPackageName(releasePath, "com"), ";\n    \n    import ").concat(getPackageNameByFileName("MybatisDaoImpl.java"), ";\n    import ").concat(getPackageNameByFileName(pojo + ".java"), ";\n    import ").concat(getPackageNameByFileName(vo + ".java"), ";\n    import ").concat(getPackageNameByFileName("BusinessException.java"), ";\n    import ").concat(getPackageNameByFileName("Grid.java"), ";\n    import ").concat(getPackageNameByFileName("DataUtils.java"), ";\n    import ").concat(getPackageNameByFileName("PropertyUtils.java"), ";\n    import ").concat(getPackageNameByFileName("PageParameter.java"), ";\n    import ").concat(getPackageName(extendsPath, "com"), ";\n    \n    import com.alibaba.dubbo.config.annotation.Service;\n    import java.util.HashMap;\n    import java.util.List;\n    import java.util.Map;\n    import java.util.Set;\n    \n    /**\n     * ").concat(serviceName + "服务实现类", "\n     * @author: ").concat(author, "\n     * @date: ").concat(now, "\n     * @version V${app.service.version}\n     */\n    @Service(version = \"") + "${app.service.version}" + "\", retries = -1, timeout = 6000)\n    public class ".concat(serviceName + "Impl", " extends MybatisDaoImpl implements ").concat(serviceName, " {\n    \n        public final String className = \"").concat(pojo, "\";\n        \n        /**\n         * \u4FDD\u5B58\n         * @param ").concat(voVariable, "\n         * @author: ").concat(author, "\n         */\n        @Override\n        public void save").concat(pojo, " (").concat(vo, " ").concat(voVariable, "){\n            ").concat(pojo, " ").concat(getParamVariableFormat$2(pojo), " = new ").concat(pojo, "();\n            DataUtils.copyPropertiesIgnoreNull(").concat(voVariable, ", ").concat(getParamVariableFormat$2(pojo), ");\n            super.save(").concat(getParamVariableFormat$2(pojo), ");\n        }\n        \n        /**\n         * \u66F4\u65B0 \n         * @param ").concat(voVariable, "\n         * @author: ").concat(author, "\n         */\n        @Override\n        public void update").concat(pojo, " (").concat(vo, " ").concat(voVariable, "){\n            ").concat(pojo, " dbObj = this.findById(").concat(voVariable, ".getId());\n            if (dbObj == null) {\n                throw new BusinessException(\"\u6570\u636E\u4E0D\u5B58\u5728\");\n            }\n            DataUtils.copyPropertiesIgnoreNull(").concat(voVariable, ", dbObj);\n            super.update(dbObj);\n        }\n    \n        /**\n         * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n         * @param id\n         * @author: ").concat(author, "\n         */\n        @Override\n        public ").concat(pojo, " findById(Long id) {\n            Map<String, Object> map = new HashMap<>();\n            map.put(\"id\", id);\n            map.put(\"column\", PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n            return this.baseDao.selectOne(className + \".findById\", map);\n        }\n    \n        /**\n         * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n         * @param id\n         * @author: ").concat(author, "\n         */\n        @Override\n        public ").concat(vo, " findVOById(Long id){\n            Map<String, Object> map = new HashMap<>();\n            map.put(\"id\", id);\n            map.put(\"column\", PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n            return this.baseDao.selectOne(className + \".findVOById\", map);\n        }\n    \n        /**\n         * \u6839\u636Eids \u67E5\u8BE2\n         * @param ids id\u96C6\u5408\n         * @author: ").concat(author, "\n         */\n        @Override\n        public List<").concat(pojo, "> findByIds(Set<Long> ids) {\n            Map<String, Object> map = new HashMap<String, Object>();\n            map.put(\"ids\", ids);\n            map.put(\"column\", PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n            return this.baseDao.selectList(className + \".findByIds\", map);\n        }\n    \n        /**\n         * \u6839\u636EID\u5220\u9664\u6570\u636E\n         * @param id\n         * @author: ").concat(author, "\n         */\n        @Override\n        public void deleteById(Long id) {\n          ").concat(pojo, " dbObj = this.findById(id);\n          if (dbObj == null) {\n              throw new BusinessException(\"\u6570\u636E\u4E0D\u5B58\u5728\");\n          }\n          this.delete(dbObj);\n        }\n    \n        /**\n         * \u5206\u9875\u67E5\u8BE2\n         * @param ").concat(voVariable, "\n         * @author: ").concat(author, "\n         */\n        @Override\n        public Grid<").concat(vo, "> find").concat(pojo, "Page (").concat(vo, " ").concat(voVariable, "){\n          ").concat(voVariable, ".setColumn(PropertyUtils.getPropertyNames(").concat(pojo, ".class));\n          ").concat(voVariable, ".setPageParameter(new PageParameter(").concat(voVariable, ".getPage(), ").concat(voVariable, ".getRows()));\n          Grid<").concat(vo, "> grid = new Grid<>();\n          List<").concat(vo, "> list = super.baseDao.selectList(className + \".find").concat(pojo, "Page\", ").concat(voVariable, ");\n          grid.setCount(Long.valueOf(").concat(voVariable, ".getPageParameter().getTotalCount()));\n          grid.setList(list);\n          return grid;\n        }\n    }\n    ");
}

/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
var getParamVariableFormat$1 = function getParamVariableFormat(str) {
  return str.charAt(0).toLowerCase() + str.substring(1);
};
/** 格式化get set 方法 */


var getSetFormat = function getSetFormat(str) {
  return str.charAt(0).toUpperCase() + str.substring(1);
};

function controller (extendsPath, releasePath, pojo, vo, pojoName, author, tableColArr, getPackageNameByFileName, getPackageName) {
  var now = new Date();
  var voVariable = getParamVariableFormat$1(vo);
  var pojoVariable = getParamVariableFormat$1(pojo);
  var serviceName = pojoName + "Service";
  var controllerName = pojoName + "Controller";
  var serviceNameVariable = getParamVariableFormat$1(serviceName);
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

  return "\npackage ".concat(getPackageName(releasePath, "com"), ";\n\nimport ").concat(getPackageNameByFileName(pojo + ".java"), ";\nimport ").concat(getPackageNameByFileName(vo + ".java"), ";\nimport ").concat(getPackageNameByFileName("BusinessException.java"), ";\nimport ").concat(getPackageNameByFileName("Grid.java"), ";\nimport ").concat(getPackageNameByFileName("CheckExistParamUtil.java"), ";\nimport ").concat(getPackageName(extendsPath, "com"), ";\nimport ").concat(getPackageNameByFileName("CommonBaseController.java"), ";\nimport ").concat(getPackageNameByFileName("ResultEnum.java"), ";\nimport ").concat(getPackageNameByFileName("StringUtil.java"), ";\nimport ").concat(getPackageNameByFileName("ResultInfo.java"), ";\n\nimport java.util.HashSet;\nimport java.util.List;\nimport io.swagger.annotations.ApiImplicitParam;\nimport io.swagger.annotations.ApiImplicitParams;\nimport io.swagger.annotations.Api;\nimport io.swagger.annotations.ApiOperation;\nimport com.alibaba.dubbo.config.annotation.Reference;\nimport org.springframework.web.bind.annotation.RequestBody;\nimport org.springframework.web.bind.annotation.RequestMapping;\nimport org.springframework.web.bind.annotation.RequestMethod;\nimport org.springframework.web.bind.annotation.RestController;\n\n/**\n * @author: ").concat(author, "\n * @date: ").concat(now, "\n * @version V${app.service.version}\n */\n@RestController\n@RequestMapping(value = \"/api/").concat(pojoVariable, "\")\n@Api(value = \"").concat(controllerName, "\", tags = { \"").concat(pojo, "\u64CD\u4F5C\u63A5\u53E3\" })\npublic class ").concat(controllerName, " extends CommonBaseController{\n  @Reference(version = \"") + "${app.service.version}" + "\", check = false)\n  ".concat(serviceName, " ").concat(serviceNameVariable, ";\n\n  /**\n   * \u4FDD\u5B58\n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  ").concat(tableCol, "\n  @ApiOperation(value = \"\u4FDD\u5B58\u4FE1\u606F\", notes = \"\u4FDD\u5B58\u4FE1\u606F\")\n  @RequestMapping(value = \"/save").concat(pojo, "\", method = RequestMethod.POST)\n  public ResultInfo<Object> save").concat(pojo, " (@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      ").concat(tableColCheck, "\n      ").concat(serviceNameVariable, ".save").concat(pojo, "(").concat(voVariable, ");\n      return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), \"\u4FDD\u5B58\u6210\u529F\", null);\n  }\n  \n  /**\n   * \u66F4\u65B0 \n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"Long\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u66F4\u65B0\u4FE1\u606F\", notes = \"\u66F4\u65B0\u4FE1\u606F\")\n  @RequestMapping(value = \"/update").concat(pojo, "\", method = RequestMethod.POST)\n  public ResultInfo<Object>  update").concat(pojo, " (@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      CheckExistParamUtil.getInstance()\n        .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n        .check();\n      ").concat(serviceNameVariable, ".update").concat(pojo, "(").concat(voVariable, ");\n      return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), \"\u66F4\u65B0\u6210\u529F\", null);\n  }\n\n  /**\n   * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"Long\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\", notes = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\")\n  @RequestMapping(value = \"/findById\", method = RequestMethod.POST)\n  public ResultInfo<").concat(pojo, "> findById(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, ") {\n      CheckExistParamUtil.getInstance()\n        .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n        .check();\n      return new ResultInfo<").concat(pojo, ">(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", ").concat(serviceNameVariable, ".findById(").concat(voVariable, ".getId()));\n  }\n\n  /**\n   * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"Long\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\", notes = \"\u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\")\n  @RequestMapping(value = \"/findVOById\", method = RequestMethod.POST)\n  public ResultInfo<").concat(vo, "> findvoById(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n          .check();\n      return new ResultInfo<").concat(vo, ">(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", ").concat(serviceNameVariable, ".findvoById(").concat(voVariable, ".getId()));\n  }\n\n  /**\n   * \u6839\u636Eids \u67E5\u8BE2\n   * @param ").concat(voVariable, " id\u96C6\u5408\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"ids\", value = \"ID\u96C6\u5408 \uFF0C\u5206\u9694\", required = true, dataType = \"String\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636Eids \u67E5\u8BE2\", notes = \"\u6839\u636Eids \u67E5\u8BE2\")\n  @RequestMapping(value = \"/findByIds\", method = RequestMethod.POST)\n  public ResultInfo<List<").concat(pojo, ">> findByIds(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, ") {\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"ids\", ").concat(voVariable, ".getIds(), \"ids\")\n          .check();\n      return new ResultInfo<List<").concat(pojo, ">>(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", ").concat(serviceNameVariable, ".findByIds(new HashSet<Long>(StringUtil.stringToLongList(").concat(voVariable, ".getIds()))));\n  }\n\n  /**\n   * \u6839\u636EID\u5220\u9664\u6570\u636E\n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"id\", value = \"ID\", required = true, dataType = \"Long\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u6839\u636EID\u5220\u9664\u6570\u636E\", notes = \"\u6839\u636EID\u5220\u9664\u6570\u636E\")\n  @RequestMapping(value = \"/deleteById\", method = RequestMethod.POST)\n  public ResultInfo<Object> deleteById(@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, ") {\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"id\", ").concat(voVariable, ".getId(), \"id\")\n          .check();\n      ").concat(serviceNameVariable, ".deleteById(").concat(voVariable, ".getId());\n      return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), \"\u6210\u529F\", null);\n  }\n\n  /**\n   * \u5206\u9875\u67E5\u8BE2\n   * @param ").concat(voVariable, "\n   * @author: ").concat(author, "\n   */\n  @ApiImplicitParams({\n    @ApiImplicitParam(name = \"page\", value = \"\u5F53\u524D\u9875\u6570\", required = true, dataType = \"Integer\", paramType = \"query\"),\n    @ApiImplicitParam(name = \"rows\", value = \"\u6761\u6570\", required = true, dataType = \"Integer\", paramType = \"query\")\n  })\n  @ApiOperation(value = \"\u5206\u9875\u67E5\u8BE2\", notes = \"\u5206\u9875\u67E5\u8BE2\")\n  @RequestMapping(value = \"/find").concat(pojo, "Page\", method = RequestMethod.POST)\n  public ResultInfo<Grid<").concat(vo, ">> find").concat(pojo, "Page (@RequestBody(required = false) ").concat(vo, " ").concat(voVariable, "){\n      CheckExistParamUtil.getInstance()\n          .addCheckParam(\"page\", ").concat(voVariable, ".getPage(), \"page\")\n          .addCheckParam(\"rows\", ").concat(voVariable, ".getRows(), \"rows\")\n          .check();\n      return new ResultInfo<Grid<").concat(vo, ">>(ResultEnum.SUCCESS.getCode(), \"\u67E5\u8BE2\u6210\u529F\", ").concat(serviceNameVariable, ".find").concat(pojo, "Page(").concat(voVariable, "));\n  }\n}\n      ");
}

/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
var getParamVariableFormat = function getParamVariableFormat(str) {
  return str.charAt(0).toLowerCase() + str.substring(1);
};

function unitTest (projectName, extendsPath, pojo, vo, pojoName, author, getPackageNameByFileName, getPackageName) {
  var now = new Date();
  var voVariable = getParamVariableFormat(vo);
  var serviceName = pojoName + "Service";
  var serviceNameVariable = getParamVariableFormat(serviceName);
  var e2eName = pojoName + "Test";
  return "\npackage com.".concat(projectName, ";\nimport ").concat(getPackageNameByFileName(pojo + ".java"), ";\nimport org.junit.Test;\nimport org.junit.runner.RunWith;\nimport org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.boot.test.context.SpringBootTest;\nimport org.springframework.test.context.junit4.SpringRunner;\n\nimport com.alibaba.fastjson.JSON;\nimport ").concat(getPackageNameByFileName(pojo + ".java"), ";\nimport ").concat(getPackageNameByFileName(vo + ".java"), ";\nimport ").concat(getPackageName(extendsPath, "com"), ";\nimport ").concat(getPackageNameByFileName("Grid.java"), ";\nimport ").concat(getPackageNameByFileName("StringUtil.java"), ";\nimport java.util.HashSet;\nimport java.util.List;\nimport java.util.Set;\n\n/**\n * ").concat(serviceName, " \u6D4B\u8BD5\n * @author: ").concat(author, "\n * @date: ").concat(now, "\n * @version V${app.service.version}\n */\n@RunWith(SpringRunner.class)\n@SpringBootTest(classes = Application.class)\n//@Rollback\n//@Transactional\npublic class ").concat(e2eName, " {\n  private final Logger logger = LoggerFactory.getLogger(getClass());\n  @Autowired\n  private ").concat(serviceName, " ").concat(serviceNameVariable, ";\n\n  /**\n   * \u4FDD\u5B58\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void save").concat(pojo, " (){\n      ").concat(vo, " ").concat(voVariable, " = new ").concat(vo, " ();\n      ").concat(voVariable, ".setId(1L);\n      //TODO: \u751F\u6210\u5FC5\u586B\u5B57\u6BB5\n      ").concat(serviceNameVariable, ".save").concat(pojo, "(").concat(voVariable, ");\n      logger.error(\"save ------\" + JSON.toJSONString(").concat(voVariable, "));\n  }\n  \n  /**\n   * \u66F4\u65B0 \n   * @author: ").concat(author, "\n   */\n  @Test\n  public void update").concat(pojo, " (){\n      ").concat(vo, " ").concat(voVariable, " = new ").concat(vo, " ();\n      ").concat(voVariable, ".setId(1L);\n      ").concat(serviceNameVariable, ".update").concat(pojo, "(").concat(voVariable, ");\n    logger.error(\"update ------\" + JSON.toJSONString(").concat(voVariable, "));\n  }\n\n  /**\n   * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void findById() {\n      ").concat(pojo, " db = ").concat(serviceNameVariable, ".findVOById(1L);\n      logger.error(\"findById ------\" + JSON.toJSONString(db));\n  }\n\n  /**\n   * \u6839\u636EID\u67E5\u8BE2\u8BE6\u60C5\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void findVOById(){\n      ").concat(pojo, " db = ").concat(serviceNameVariable, ".findVOById(1L);\n      logger.error(\"findVOById ------\" + JSON.toJSONString(db));\n  }\n\n  /**\n   * \u6839\u636Eids \u67E5\u8BE2\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void findByIds() {\n      List<").concat(pojo, "> list =").concat(serviceNameVariable, ".findByIds(new HashSet<Long>(StringUtil.stringToLongList(\"1,2,3\")));\n      logger.error(\"findByIds ------\" + JSON.toJSONString(list));\n  }\n\n  /**\n   * \u6839\u636EID\u5220\u9664\u6570\u636E\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void deleteById() {\n      ").concat(serviceNameVariable, ".deleteById(1L);\n      logger.error(\"deleteById ------ success\");\n  }\n\n  /**\n   * \u5206\u9875\u67E5\u8BE2\n   * @author: ").concat(author, "\n   */\n  @Test\n  public void find").concat(pojo, "Page (){\n      ").concat(vo, " ").concat(voVariable, " = new ").concat(vo, "();\n      ").concat(voVariable, ".setPage(1);\n      ").concat(voVariable, ".setRows(10);\n      Grid find").concat(pojo, "Page = ").concat(serviceNameVariable, ".find").concat(pojo, "Page(").concat(voVariable, ");\n      logger.error(\"find").concat(pojo, "Page ------ \" + JSON.toJSONString(find").concat(pojo, "Page));\n  }\n}\n      ");
}

var CodeGenerator = function CodeGenerator() {
  _classCallCheck(this, CodeGenerator);
};
CodeGenerator.service = service;
CodeGenerator.mapper = mapper;
CodeGenerator.serviceImpl = serviceImpl;
CodeGenerator.controller = controller;
CodeGenerator.unitTest = unitTest;

module.exports = CodeGenerator;
