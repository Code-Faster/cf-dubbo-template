/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
const getParamVariableFormat = (str: string) => {
  return str.charAt(0).toLowerCase() + str.substring(1);
};
/** 格式化get set 方法 */
const getSetFormat = (str: string) => {
  return str.charAt(0).toUpperCase() + str.substring(1);
};
export default function (
  extendsPath: string,
  releasePath: string,
  pojo: string,
  vo: string,
  pojoName: string,
  author: string,
  tableColArr: [],
  getPackageNameByFileName: (args: string) => string,
  getPackageName: (path: string, filter: string) => string
) {
  const now = new Date();
  const voVariable = getParamVariableFormat(vo);
  const pojoVariable = getParamVariableFormat(pojo);
  const serviceName = pojoName + "Service";
  const controllerName = pojoName + "Controller";
  const serviceNameVariable = getParamVariableFormat(serviceName);

  let tableCol = "";
  let tableColCheck = "";
  // 生成非空判断，用于保存接口
  if (tableColArr && tableColArr.length > 0) {
    tableCol = `@ApiImplicitParams({
        ${tableColArr
          .map((ele: any) => {
            return (
              '@ApiImplicitParam(name = "' +
              ele.col +
              '", value = "' +
              (ele.name || ele.col) +
              '", required = true, dataType = "' +
              ele.type +
              '", paramType = "query")'
            );
          })
          .join(",\r\n\t\t")}})`;
    tableColCheck = `CheckExistParamUtil.getInstance().
      ${tableColArr
        .map((ele: any) => {
          return (
            'addCheckParam("' +
            ele.col +
            '", ' +
            voVariable +
            ".get" +
            getSetFormat(ele.col) +
            '(), "' +
            (ele.name || ele.col) +
            '")'
          );
        })
        .join(".\r\n\t\t")}
      .check();`;
  }
  return (
    `
package ${getPackageName(releasePath, "com")};

import ${getPackageNameByFileName(pojo + ".java")};
import ${getPackageNameByFileName(vo + ".java")};
import ${getPackageNameByFileName("BusinessException.java")};
import ${getPackageNameByFileName("Grid.java")};
import ${getPackageNameByFileName("CheckExistParamUtil.java")};
import ${getPackageName(extendsPath, "com")};
import ${getPackageNameByFileName("CommonBaseController.java")};
import ${getPackageNameByFileName("ResultEnum.java")};
import ${getPackageNameByFileName("StringUtil.java")};
import ${getPackageNameByFileName("ResultInfo.java")};

import java.util.HashSet;
import java.util.List;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: ${author}
 * @date: ${now}
 * @version V\${app.service.version}
 */
@RestController
@RequestMapping(value = "/api/${pojoVariable}")
@Api(value = "${controllerName}", tags = { "${pojo}操作接口" })
public class ${controllerName} extends CommonBaseController{
  @Reference(version = "` +
    "${app.service.version}" +
    `", check = false)
  ${serviceName} ${serviceNameVariable};

  /**
   * 保存
   * @param ${voVariable}
   * @author: ${author}
   */
  ${tableCol}
  @ApiOperation(value = "保存信息", notes = "保存信息")
  @RequestMapping(value = "/save${pojo}", method = RequestMethod.POST)
  public ResultInfo<Object> save${pojo} (@RequestBody(required = false) ${vo} ${voVariable}){
      ${tableColCheck}
      ${serviceNameVariable}.save${pojo}(${voVariable});
      return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), "保存成功", null);
  }
  
  /**
   * 更新 
   * @param ${voVariable}
   * @author: ${author}
   */
  @ApiImplicitParams({
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "query")
  })
  @ApiOperation(value = "更新信息", notes = "更新信息")
  @RequestMapping(value = "/update${pojo}", method = RequestMethod.POST)
  public ResultInfo<Object>  update${pojo} (@RequestBody(required = false) ${vo} ${voVariable}){
      CheckExistParamUtil.getInstance()
        .addCheckParam("id", ${voVariable}.getId(), "id")
        .check();
      ${serviceNameVariable}.update${pojo}(${voVariable});
      return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), "更新成功", null);
  }

  /**
   * 根据ID查询详情
   * @param ${voVariable}
   * @author: ${author}
   */
  @ApiImplicitParams({
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "query")
  })
  @ApiOperation(value = "根据ID查询详情", notes = "根据ID查询详情")
  @RequestMapping(value = "/findById", method = RequestMethod.POST)
  public ResultInfo<${pojo}> findById(@RequestBody(required = false) ${vo} ${voVariable}) {
      CheckExistParamUtil.getInstance()
        .addCheckParam("id", ${voVariable}.getId(), "id")
        .check();
      return new ResultInfo<${pojo}>(ResultEnum.SUCCESS.getCode(), "成功", ${serviceNameVariable}.findById(${voVariable}.getId()));
  }

  /**
   * 根据ID查询详情
   * @param ${voVariable}
   * @author: ${author}
   */
  @ApiImplicitParams({
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "query")
  })
  @ApiOperation(value = "根据ID查询详情", notes = "根据ID查询详情")
  @RequestMapping(value = "/findVOById", method = RequestMethod.POST)
  public ResultInfo<${vo}> findvoById(@RequestBody(required = false) ${vo} ${voVariable}){
      CheckExistParamUtil.getInstance()
          .addCheckParam("id", ${voVariable}.getId(), "id")
          .check();
      return new ResultInfo<${vo}>(ResultEnum.SUCCESS.getCode(), "成功", ${serviceNameVariable}.findvoById(${voVariable}.getId()));
  }

  /**
   * 根据ids 查询
   * @param ${voVariable} id集合
   * @author: ${author}
   */
  @ApiImplicitParams({
    @ApiImplicitParam(name = "ids", value = "ID集合 ，分隔", required = true, dataType = "String", paramType = "query")
  })
  @ApiOperation(value = "根据ids 查询", notes = "根据ids 查询")
  @RequestMapping(value = "/findByIds", method = RequestMethod.POST)
  public ResultInfo<List<${pojo}>> findByIds(@RequestBody(required = false) ${vo} ${voVariable}) {
      CheckExistParamUtil.getInstance()
          .addCheckParam("ids", ${voVariable}.getIds(), "ids")
          .check();
      return new ResultInfo<List<${pojo}>>(ResultEnum.SUCCESS.getCode(), "成功", ${serviceNameVariable}.findByIds(new HashSet<Long>(StringUtil.stringToLongList(${voVariable}.getIds()))));
  }

  /**
   * 根据ID删除数据
   * @param ${voVariable}
   * @author: ${author}
   */
  @ApiImplicitParams({
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "query")
  })
  @ApiOperation(value = "根据ID删除数据", notes = "根据ID删除数据")
  @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
  public ResultInfo<Object> deleteById(@RequestBody(required = false) ${vo} ${voVariable}) {
      CheckExistParamUtil.getInstance()
          .addCheckParam("id", ${voVariable}.getId(), "id")
          .check();
      ${serviceNameVariable}.deleteById(${voVariable}.getId());
      return new ResultInfo<Object>(ResultEnum.SUCCESS.getCode(), "成功", null);
  }

  /**
   * 分页查询
   * @param ${voVariable}
   * @author: ${author}
   */
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", value = "当前页数", required = true, dataType = "Integer", paramType = "query"),
    @ApiImplicitParam(name = "rows", value = "条数", required = true, dataType = "Integer", paramType = "query")
  })
  @ApiOperation(value = "分页查询", notes = "分页查询")
  @RequestMapping(value = "/find${pojo}Page", method = RequestMethod.POST)
  public ResultInfo<Grid<${vo}>> find${pojo}Page (@RequestBody(required = false) ${vo} ${voVariable}){
      CheckExistParamUtil.getInstance()
          .addCheckParam("page", ${voVariable}.getPage(), "page")
          .addCheckParam("rows", ${voVariable}.getRows(), "rows")
          .check();
      return new ResultInfo<Grid<${vo}>>(ResultEnum.SUCCESS.getCode(), "查询成功", ${serviceNameVariable}.find${pojo}Page(${voVariable}));
  }
}
      `
  );
}
