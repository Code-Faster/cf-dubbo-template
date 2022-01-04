import path from "path";
import { TemplateTools } from "../../../src/template";
import CodeGenerator from "../../../src/index";
const project = {
  projectName: "createTemplate",
  projectDir: path.join(__dirname, "../../../playground"),
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
describe("TemplateTools", () => {
  it("updateProjectDirJson should work", () => {
    const tools = new TemplateTools(project);
    const obj = tools.updateProjectDirJson();
    expect(obj).toBe(true);
  });

  it("showStructure should work", () => {
    const tools = new TemplateTools(project);
    // console.log(tools.showStructure());
    expect(tools.showStructure(project).fileName).toBe(
      "createTemplate"
    );
  });

  it("fileDisplay should work", () => {
    const tools = new TemplateTools(project);
    tools.fileDisplay(templateObj);
    // console.log(templateObj);
    expect(templateObj.children.length).toBe(10);
  });

  it("getJsonFromPath should work", () => {
    const tools = new TemplateTools(project);
    const obj = tools.getJsonFromPath(true);
    // console.log(obj);
    expect(obj.children).toHaveLength(10);
  });

  it("findOneFileByKey should work", () => {
    const tools = new TemplateTools(project);
    const obj = tools.findOneFileByKey("PersonServiceImpl.java");
    // console.log(obj);
    expect(obj.label).toBe(
      "createTemplate-provider/src/main/java/com/createTemplate/provider/core/dubbo/service/PersonServiceImpl.java"
    );
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
    expect(obj.tableName).toBe("T_ACCOUNT_DETAIL");
  });
});
