import path from "path";
import { TemplateTools } from "../../../src/template";
import CodeGenerator from "../../../src/index";
const project = {
  projectName: "test",
  projectDir: path.join(__dirname, "../../../playground/test"),
  owner: "Code Faster",
  type: "1",
  templateId: 1,
  description: "Code Faster createTemplate",
  id: 1,
};
const templateObj = {
  fileName: "createTemplate",
  path: path.join(__dirname, "../../../playground/createTemplate"),
  formData: {},
  isDir: true,
  children: [],
};
const model = {
  // 表名
  tableName: "T_TEST",
  // 注释
  tableComment: "generatorVO",
  // 表单字段数组
  tableCloums: [
    { columnComment: "", columnType: "Long", columnName: "id" },
    {
      columnComment: "用户id",
      columnType: "Long",
      columnName: "personId",
    },
    {
      columnComment: " 加时间",
      columnType: "Date",
      columnName: "inputDate",
    },
  ],
};
describe("TemplateTools", () => {
  it("init should work", () => {
    const tools = new TemplateTools(project);
    tools.init();
  });

  it("getJsonFromPath should work", () => {
    const tools = new TemplateTools(project);
    const obj = tools.getJsonFromPath(true);
    expect(obj.children).toHaveLength(10);
  });

  it("updateProjectConfig should work", () => {
    const tools = new TemplateTools(project);
    const obj = tools.updateProjectConfig();
    expect(obj.children).toHaveLength(10);
  });

  it("fileDisplay should work", () => {
    const tools = new TemplateTools(project);
    tools.fileDisplay(templateObj);
    // console.log(templateObj);
    expect(templateObj.children.length).toBe(10);
  });
  it("findOneFileByKey should work", () => {
    const tools = new TemplateTools(project);
    const obj = tools.findOneFileByKey("PersonServiceImpl.java");
    // console.log(obj);
    expect(obj.label).toBe("PersonServiceImpl.java");
  });
});

describe("CodeGenerator", () => {
  it("getModelByPojoPath should work", () => {
    const tools = new CodeGenerator(project);
    const obj = tools.getModelByPojoPath(
      path.join(
        __dirname,
        "../../../playground/createTemplate/createTemplate-model/src/main/java/com/createTemplate/model/core/pojo/TAccountDetail.java"
      )
    );
    // console.log(obj);
    expect(obj.tableName).toBe("T_ACCOUNT_DETAIL");
  });
  it("generatorPojo should work", () => {
    const tools = new CodeGenerator(project);
    const params = {
      /** 其他参数 */
      props: {},
      /** 输出地址 */
      releasePath: path.join(
        __dirname,
        "../../../playground/createTemplate/createTemplate-model/src/main/java/com/createTemplate/model/core/pojo"
      ),
      model: model,
    };
    const obj = tools.generatorPojo(params);
    expect(obj).toBe(undefined);
  });
  it("generatorVO should work", () => {
    const tools = new CodeGenerator(project);
    const params = {
      /** 其他参数 */
      props: {},
      /** 输出地址 */
      releasePath: path.join(
        __dirname,
        "../../../playground/createTemplate/createTemplate-model/src/main/java/com/createTemplate/model/core/vo"
      ),
      model: model,
    };
    const obj = tools.generatorVO(params);
    expect(obj).toBe(undefined);
  });
});
