import { FILE_SUFFIX, getParamVariableFormat, TemplateTools } from "../index";
import fs from "fs";
import path from "path";

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

  const mapperName = pojo + "Mapper.xml";
  /**
   * 获取模版工具类
   */
  const tools = new TemplateTools(project);

  const template = `
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${pojo}">
  <resultMap type="${tools.getPackageNameByFileName(
    pojo + FILE_SUFFIX
  )}" id="${pojoVariable}"/>
  <resultMap type="${tools.getPackageNameByFileName(
    vo + FILE_SUFFIX
  )}" id="${voVariable}"/>
  <resultMap type="java.util.HashMap" id="map"/>
  <parameterMap type="${tools.getPackageNameByFileName(
    pojo + FILE_SUFFIX
  )}" id="${pojoVariable}"/>
  <parameterMap type="${tools.getPackageNameByFileName(
    vo + FILE_SUFFIX
  )}" id="${voVariable}"/>
  <parameterMap type="java.util.HashMap" id="map"/>
    
    <!-- 根据ID查询表信息 author by ${author} -->
  <select id="findById" resultMap="${pojoVariable}" parameterMap="map">
      SELECT * FROM ${params.model.tableName} WHERE ID = #{id}
  </select>
    
    <!-- 根据ID查询用户详情 author by ${author} -->
  <select id="findVOById" resultMap="${voVariable}" parameterMap="map">
      SELECT * FROM ${params.model.tableName} WHERE ID = #{id}
  </select>
  
  <!-- 根据ID数组查询表信息 author by ${author} -->
  <select id="findByIds" resultMap="${pojoVariable}" parameterMap="map">
      SELECT * FROM ${params.model.tableName} WHERE 
      <foreach collection="ids" item="item" open="ID in  (" close=")" separator=",">
        #{item}
      </foreach>
  </select>
  
  <!-- 分页查询表 author by ${author} -->
  <select id="find${pojo}Page" resultMap="${voVariable}" parameterMap="${voVariable}">
      SELECT * FROM ${params.model.tableName} WHERE 1=1 ORDER BY ID ASC
  </select>
</mapper>
  `;

  fs.writeFileSync(path.join(params.releasePath, mapperName), template);
}
