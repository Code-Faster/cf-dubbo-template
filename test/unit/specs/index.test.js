import path from "path";
import { TemplateTools } from "../../../src/template";
const project = {
  projectName: "feilang",
  projectDir: "/Users/xiangtian/Desktop/git/flJava",
  owner: "Code Faster",
  type: "1",
  templateId: 18,
  description: "飞浪数据",
  templateDir: "",
  id: 1,
  defaultPojoPath:
    "/Users/xiangtian/Desktop/git/flJava/feilang/feilang-model/src/main/java/com/feilang/model/core/pojo",
  defaultVoPath:
    "/Users/xiangtian/Desktop/git/flJava/feilang/feilang-model/src/main/java/com/feilang/model/core/vo",
  defaultServicePath:
    "/Users/xiangtian/Desktop/git/flJava/feilang/feilang-api/src/main/java/com/feilang/api/core/doubbo/service",
  defaultServiceImplPath:
    "/Users/xiangtian/Desktop/git/flJava/feilang/feilang-provider/src/main/java/com/feilang/provider/core/dubbo/service",
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
    expect(tools.showStructure().fileName).toBe("createTemplate");
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
    expect(obj.children).toHaveLength(13);
  });

  it("findOneFileByKey should work", () => {
    const tools = new TemplateTools(project);
    const obj = tools.findOneFileByKey("PersonServiceImpl.java");
    console.log(obj);
    expect(obj.label).toBe(
      "feilang-provider/src/main/java/com/feilang/provider/core/dubbo/service/PersonServiceImpl.java"
    );
  });
});
