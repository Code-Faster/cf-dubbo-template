# codefaster-dubbo-template

code-faster 模版项目，技术栈为 spring boot、dubbo、nacos、redis、mongodb、mysql

### 项目 GUI 地址，提供基于任意语言任意模版的 项目CURD 生成以及测试部署一体化

https://github.com/code-faster/code-faster

## src/index.ts 对外提供以下功能

```
// 初始化项目
init: (params: CodeFaster.Params) => void;
// 更新项目config结构，并返回结构JSON
updateProjectConfig: () => CodeFaster.ConfigJSON | undefined;
// 根据参数生成POJO
generatorPojo: (params: CodeFaster.Params) => void;
// 根据参数生成VO
generatorVO: (params: CodeFaster.Params) => void;
// 根据参数生成Service层
generatorService: (params: CodeFaster.Params) => void;
// 根据参数生成ServiceImpl层
generatorServiceImpl: (params: CodeFaster.Params) => void;
// 根据参数生成Controller层
generatorController: (params: CodeFaster.Params) => void;
// 根据参数生成Mapper层
generatorMapper: (params: CodeFaster.Params) => void;
// 根据参数生成单元测试
generatorUnitTest: (params: CodeFaster.Params) => void;
// 根据POJO路径返回模型类JSON
getModelByPojoPath: (filePath: string) => CodeFaster.SqlTable;
```

## 如何发布自己的模版

### 自定义区域

```
1、package.json
项目的基础信息以及版本信息
2、playground
存放项目初始化文件
3、src/template
项目的 CURD 模版区域
4、.cfignore
项目的初始化文件拷贝需要忽略的地址
```

### package.json 参数

#### name

模版名称，以 codefaster-开始，以-template 结束

#### keywords 第一个关键词将作为模版标记的语言类型，例如：

```
1、java
2、javaScript
```

#### description

```
模版的描述信息
```

#### files

```
指定发布的模版文件
```

#### version

```
当前版本
```

#### license

```
模版 license
```
