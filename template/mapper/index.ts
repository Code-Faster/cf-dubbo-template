/**
 * 根据传入实体类获取参数变量
 * @param {*} str
 */
const getParamVariableFormat = (str: string) => {
  return str.charAt(0).toLowerCase() + str.substring(1);
};
export default function (
  releasePath: string,
  pojo: string,
  vo: string,
  pojoName: string,
  tableName: string,
  author: string,
  getPackageNameByFileName: (args: string) => string,
  getPackageName: (path: string, filter: string) => string
) {
  const now = new Date();
  const serviceName = pojoName + "Service";
  const voVariable = getParamVariableFormat(vo);
  const pojoVariable = getParamVariableFormat(pojo);
  return `
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    <mapper namespace="${pojo}">
        <resultMap type="${getPackageNameByFileName(
          pojo + ".java"
        )}" id="${pojoVariable}"/>
      <resultMap type="${getPackageNameByFileName(
        vo + ".java"
      )}" id="${voVariable}"/>
      <resultMap type="java.util.HashMap" id="map"/>
        <parameterMap type="${getPackageNameByFileName(
          pojo + ".java"
        )}" id="${pojoVariable}"/>
        <parameterMap type="${getPackageNameByFileName(
          vo + ".java"
        )}" id="${voVariable}"/>
        <parameterMap type="java.util.HashMap" id="map"/>
        
        <!-- 根据ID查询表信息 create by ${author} -->
        <select id="findById" resultMap="${pojoVariable}" parameterMap="map">
            SELECT * FROM ${tableName} WHERE ID = #{id}
      </select>
        
        <!-- 根据ID查询用户详情 create by ${author} -->
        <select id="findVOById" resultMap="${voVariable}" parameterMap="map">
            SELECT * FROM ${tableName} WHERE ID = #{id}
      </select>
      
        <!-- 根据ID数组查询表信息 create by ${author} -->
        <select id="findByIds" resultMap="${pojoVariable}" parameterMap="map">
            SELECT * FROM ${tableName} WHERE 
            <foreach collection="ids" item="item" open="ID in  (" close=")" separator=",">
          #{item}
        </foreach>
        </select>
        
        <!-- 分页查询表 create by ${author} -->
        <select id="find${pojo}Page" resultMap="${voVariable}" parameterMap="${voVariable}">
            SELECT * FROM ${tableName} WHERE 1=1 ORDER BY ID ASC
        </select>
    
    </mapper>
  `;
}
