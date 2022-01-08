declare namespace CodeFaster {
  /**
   * 表SQL列对象
   */
  interface SqlColumn {
    columnComment: string;
    columnType: string;
    columnName: string;
  }
  /**
   * 表结构与pojo的双向json化
   */
  interface SqlTable {
    // 库名
    dbName: string;
    // 表名
    tableName: string;
    // 注释
    tableComment: string;
    // 表单字段数组
    tableCloums: SqlColumn[];
    // 表执行创建SQL
    tableSql: string;
  }
  /**
   * 生成模型的表单数据
   */
  interface ModelForm {
    buildPath: string;
    buildPathVo: string;
    tableArray: SqlTable[];
  }
  /**
   * curd 的表单数据
   */
  interface CURDForm {
    pojo: string;
    vo: string;
    pojoPath: string;
    voPath: string;
    servicePath: string;
    serviceImplPath: string;
    controllerPath: string;
    unitTestPath: string;
    mapperPath: string;
  }

  /** 项目表 */
  interface Project {
    id?: number;
    /** 项目名称 */
    projectName: string;
    /** 项目路径 */
    projectDir: string;
    /** 作者 */
    owner?: string;
    /** 语言类型 1、Java 2、JavaScript */
    type?: number;
    /** 简介 */
    description?: string;
    /** 项目模版 */
    templateId?: number;

    /** Java项目详细参数 */
    defaultPojoPath?: string;
    defaultVoPath?: string;
    defaultServicePath?: string;
    defaultServiceImplPath?: string;
    defaultControllerPath?: string;
    defaultMapperPath?: string;
    defaultUnitTestPath?: string;
  }
  /**
   * 代码生成器
   */
  interface CodeGenerator {
    /** 公用方法：初始化项目 */
    init: (params: CodeFaster.Params) => void;
    /** 公用方法：更新项目配置文件 */
    updateProjectConfig: () => void;
  }
  /**
   * Java生成器，Java模版私有化方法
   */
  interface JavaCodeGenerator extends CodeGenerator {
    generatorPojo: (params: CodeFaster.Params) => void;

    generatorVO: (params: CodeFaster.Params) => void;

    generatorService: (params: CodeFaster.Params) => void;

    generatorMapper: (params: CodeFaster.Params) => void;

    generatorController: (params: CodeFaster.Params) => void;

    generatorServiceImpl: (params: CodeFaster.Params) => void;

    generatorUnitTest: (params: CodeFaster.Params) => void;

    getModelByPojoPath: (filePath: string) => CodeFaster.SqlTable;
  }
  type ConfigJSON = {
    fileName: string;
    path: string;
    // 拷贝项目使用
    fromPath?: string;
    /** 相对于项目根目录的地址 */
    sortPath: string;
    project?: CodeFaster.Project;
    // false 文件 true 文件夹
    isDir: boolean;
    children: Array<ConfigJSON>;
  };
  /**
   * 生成器参数
   */
  type Params = {
    /** 其他参数 */
    props: { [key: string]: any };
    /** 输出地址 */
    releasePath: string;
    model?: SqlTable;
  };
}
