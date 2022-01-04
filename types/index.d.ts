declare namespace CodeFaster {
  interface CodeGenerator {
    init: (params: CodeFaster.Params) => void;

    generatorPojo: (params: CodeFaster.Params) => void;

    generatorVO: (params: CodeFaster.Params) => void;

    generatorService: (params: CodeFaster.Params) => void;

    generatorMapper: (params: CodeFaster.Params) => void;

    generatorController: (params: CodeFaster.Params) => void;

    generatorServiceImpl: (params: CodeFaster.Params) => void;

    generatorUnitTest: (params: CodeFaster.Params) => void;

    getModelByPojoPath: (filePath: string) => CodeFaster.Model;
  }

  /** 项目表 */
  type Project = {
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
    /** 模版ID对应的物理地址 */
    templateDir?: string;

    /** Java项目详细参数 */
    defaultPojoPath?: string;
    defaultVoPath?: string;
    defaultServicePath?: string;
    defaultServiceImplPath?: string;
  };
  type FileObj = {
    fileName: string;
    path: string;
    /** 相对于项目根目录的地址 */
    sortPath: string;
    formData?: { [key: string]: any };
    // false 文件 true 文件夹
    isDir: boolean;
    children: Array<FileObj>;
  };
  /**
   * pojo实体
   */
  type Model = {
    /** 表名 */
    tableName: string;
    /** 表注释 */
    tableCnName: string;
    /** 列名 */
    tableColArr: Array<any>;
  };
  /**
   * 生成器参数
   */
  type Params = {
    /** 其他参数 */
    props: { [key: string]: any };
    /** 输出地址 */
    releasePath: string;
    model?: Model;
  };
}
