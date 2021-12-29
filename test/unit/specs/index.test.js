import CodeGenerator from "../../../src/index";

describe("service", () => {
  it("should work", () => {
    const serviceStr = CodeGenerator.service(
      "releasePath",
      "pojo",
      "vo",
      "pojoName",
      "author",
      (args) => "getPackageNameByFileName",
      (path, filter) => "getPackageName"
    );
    console.log(serviceStr);
    expect(serviceStr).toBe(
      `package getPackageName;
    
      import getPackageNameByFileName;
      import getPackageNameByFileName;
      import getPackageNameByFileName;
      import getPackageNameByFileName;
      import getPackageNameByFileName;
      import java.util.Set;
      import java.util.List;
      
      /**
       * pojoNameService服务
       * @author: author
       * @date: Tue Dec 28 2021 23:52:02 GMT+0800 (中国标准时间)
       * @version V\${app.service.version}
       */
      public interface pojoNameService extends MybatisDao {
          /**
           * 保存
           * @param vo
           * @author: author
           */
          void savepojo (vo vo)throws BusinessException;
          
          /**
           * 更新 
           * @param vo
           * @author: author
           */
          void updatepojo (vo vo)throws BusinessException;
      
          /**
           * 根据ID查询pojo
           * @param id
           * @author: author
           */
          pojo findById(Long id) throws BusinessException;
      
          /**
           * 根据ID查询vo
           * @param id
           * @author: author
           */
          vo findVOById(Long id) throws BusinessException;
      
          /**
           * 根据ID集合查询
           * @param ids
           * @author: author
           */
          List<pojo> findByIds(Set<Long> ids) throws BusinessException;
      
          /**
           * 根据ID删除数据
           * @param id
           * @author: author
           */
          void deleteById(Long id) throws BusinessException;
      
          /**
           * 分页查询
           * @param vo
           * @author: author
           * @return
           */
          Grid<vo> findpojoPage (vo vo)throws BusinessException;
      }`
    );
  });
});
