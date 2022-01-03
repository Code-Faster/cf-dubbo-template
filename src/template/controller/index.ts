import {
  FILE_SUFFIX,
  getPackageName,
  getParamVariableFormat,
  getSetFormat,
  TemplateTools,
} from "../index";

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
  const pojoVariable = getParamVariableFormat(pojo);

  const vo = params.props.vo;
  const voVariable = getParamVariableFormat(vo);

  const author = project.owner;
  /**
   * 获取模版工具类
   */
  const tools = new TemplateTools(project);

  const tableColArr = params.model.tableColArr;
  const now = new Date();
  const serviceName = pojo + "Service";
  const serviceNameVariable = getParamVariableFormat(serviceName);
  const controllerName = pojo + "Controller";

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
package ${getPackageName(params.releasePath, "com")};

import ${tools.getPackageNameByFileName(pojo + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName(vo + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName("BusinessException" + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName("Grid" + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName("CheckExistParamUtil" + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName(serviceName + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName("CommonBaseController" + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName("ResultEnum" + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName("StringUtil" + FILE_SUFFIX)};
import ${tools.getPackageNameByFileName("ResultInfo" + FILE_SUFFIX)};

import java.util.HashSet;
import java.util.List;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.dubbo.config.annotation.DubboReference;

/**
 * @author: ${author}
 * @date: ${now}
 * @version V\${app.service.version}
 */
@RestController
@RequestMapping(value = "/api/${pojoVariable}")
@Api(value = "${controllerName}", tags = { "${pojo}操作接口" })
public class ${controllerName} extends CommonBaseController{
  @DubboReference(version = "` +
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
