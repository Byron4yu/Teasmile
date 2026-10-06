package com.teasmile.controller.admin;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/6 20:33
 * @注释
 */

import com.teasmile.dto.CategoryDTO;
import com.teasmile.dto.CategoryPageQueryDTO;
import com.teasmile.dto.EmployeeDTO;
import com.teasmile.dto.EmployeePageQueryDTO;
import com.teasmile.entity.Category;
import com.teasmile.entity.Employee;
import com.teasmile.properties.JwtProperties;
import com.teasmile.result.PageResult;
import com.teasmile.result.ApiResult;
import com.teasmile.service.CategoryService;
import com.teasmile.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 饮品管理
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags="分类相关接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public ApiResult<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分类分页查询，参数为：{}",categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return ApiResult.success(pageResult);
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类")
    public ApiResult update(@RequestBody CategoryDTO categoryDTO){// json格式的数据需要加RequestBody注解
        log.info("编辑员工信息:{}",categoryDTO);
        categoryService.update(categoryDTO);
        return ApiResult.success();
    }

    /**
     * 启用禁用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public ApiResult startOrStop(@PathVariable Integer status,Long id){
        log.info("启用禁用分类id:{},{}",status,id);
        categoryService.startOrStop(status,id);
        return ApiResult.success();
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")
    public ApiResult<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类：{}",categoryDTO);
        categoryService.save(categoryDTO);
        return ApiResult.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public ApiResult<String> deleteById(Long id){
        log.info("删除分类:{}",id);
        categoryService.deleteById(id);
        return ApiResult.success();
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public ApiResult<List<Category>> list(Integer type){
        List<Category> list=categoryService.list(type);
        return ApiResult.success(list);

    }
}
