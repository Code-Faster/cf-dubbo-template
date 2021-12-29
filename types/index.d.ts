/**
 * 生成器
 */
declare class CodeGenerator {
  constructor();
  static service: (
    releasePath: string,
    pojo: string,
    vo: string,
    pojoName: string,
    author: string,
    getPackageNameByFileName: (args: string) => string,
    getPackageName: (path: string, filter: string) => string
  ) => string;
  static mapper = (
    extendsPath: string,
    releasePath: string,
    pojo: string,
    vo: string,
    pojoName: string,
    author: string,
    tableColArr: [],
    getPackageNameByFileName: (args: string) => string,
    getPackageName: (path: string, filter: string) => string
  ) => string;
  static serviceImpl = (
    extendsPath: string,
    releasePath: string,
    pojo: string,
    vo: string,
    pojoName: string,
    author: string,
    tableColArr: [],
    getPackageNameByFileName: (args: string) => string,
    getPackageName: (path: string, filter: string) => string
  ) => string;
  static controller = (
    extendsPath: string,
    releasePath: string,
    pojo: string,
    vo: string,
    pojoName: string,
    author: string,
    tableColArr: [],
    getPackageNameByFileName: (args: string) => string,
    getPackageName: (path: string, filter: string) => string
  ) => string;
  static unitTest = (
    projectName: string,
    extendsPath: string,
    pojo: string,
    vo: string,
    pojoName: string,
    author: string,
    getPackageNameByFileName: (args: string) => string,
    getPackageName: (path: string, filter: string) => string
  ) => string;
}
