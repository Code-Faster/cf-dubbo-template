# codefaster-dubbo-template

code-faster 模版项目，技术栈为 spring boot、dubbo、nacos、redis、mongodb、mysql

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
