package com.teasmile.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.teasmile.constant.MessageConstant;
import com.teasmile.constant.StatusConstant;
import com.teasmile.context.LoginContext;
import com.teasmile.dto.CategoryDTO;
import com.teasmile.dto.CategoryPageQueryDTO;
import com.teasmile.entity.Category;
import com.teasmile.entity.Employee;
import com.teasmile.exception.DeletionNotAllowedException;
import com.teasmile.mapper.CategoryMapper;
import com.teasmile.mapper.DrinkMapper;
import com.teasmile.mapper.EmployeeMapper;
import com.teasmile.mapper.SetmealMapper;
import com.teasmile.result.PageResult;
import com.teasmile.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/6 20:36
 * @注释
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private DrinkMapper drinkMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        // limit 0,10
        //分页查询
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());


        Page<Category> page=categoryMapper.pageQuery(categoryPageQueryDTO);
        long total = page.getTotal();
        List<Category> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    public void update(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);//因为update接收的是Category类型，先进行转换



        categoryMapper.update(category);
    }

    /**
     * 启用禁用分类
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Category category=Category.builder().status(status).id(id).build();
        categoryMapper.update(category);
    }

    /**
     * 新增分类
     * @param categoryDTO
     */
    public void save(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);

        //设置其他属性
        category.setStatus(StatusConstant.DISABLE);


        categoryMapper.insert(category);
    }

    /**
     * 删除分类
     * @param id
     */
    public void deleteById(Long id) {
        //查询当前分类是否关联了饮品
        Integer count=drinkMapper.countByCategoryId(id);
        if(count>0){
            //当前分类下有饮品,不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DRINK);
        }
        //查询当前分类是否关联套餐
        count=setmealMapper.countByCategoryId(id);
        if(count>0){
            //当前分类下有套餐,不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        //删除分类
        categoryMapper.deleteById(id);

    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
