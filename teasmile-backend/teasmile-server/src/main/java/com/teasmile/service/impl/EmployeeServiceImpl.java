package com.teasmile.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.teasmile.constant.MessageConstant;
import com.teasmile.constant.PasswordConstant;
import com.teasmile.constant.StatusConstant;
import com.teasmile.context.LoginContext;
import com.teasmile.dto.EmployeeDTO;
import com.teasmile.dto.EmployeeLoginDTO;
import com.teasmile.dto.EmployeePageQueryDTO;
import com.teasmile.dto.PasswordEditDTO;
import com.teasmile.entity.Employee;
import com.teasmile.exception.AccountLockedException;
import com.teasmile.exception.AccountNotFoundException;
import com.teasmile.exception.PasswordEditFailedException;
import com.teasmile.exception.PasswordErrorException;
import com.teasmile.mapper.EmployeeMapper;
import com.teasmile.result.PageResult;
import com.teasmile.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //明文密码md5加密
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    public void save(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);

        //设置其他属性
        employee.setStatus(StatusConstant.ENABLE);//状态
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));//密码


        employeeMapper.insert(employee);

    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // limit 0,10
        //分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        // TODO 安装mybatisx
        Page<Employee> page=employeeMapper.pageQuery(employeePageQueryDTO);
        long total = page.getTotal();
        List<Employee> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 启用禁用员工操作
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        // update status
        Employee employee=Employee.builder().status(status).id(id).build();

        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    public Employee getById(Long id) {
        Employee employee=employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    public void update(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);//因为update接收的是Employee类型，先进行转换



        employeeMapper.update(employee);
    }

    /**
     * 修改密码：校验原密码后更新为新密码（MD5 加密存储）
     * @param passwordEditDTO
     */
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        // 从登录上下文取当前操作员工 id，防止越权修改他人密码
        Long empId = LoginContext.getUserId();
        Employee employee = employeeMapper.getById(empId);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 校验原密码
        String oldMd5 = DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes());
        if (!oldMd5.equals(employee.getPassword())) {
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_EDIT_FAILED);
        }
        // 加密新密码并更新
        String newMd5 = DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes());
        employeeMapper.updatePassword(empId, newMd5, LocalDateTime.now(), empId);
    }

}
