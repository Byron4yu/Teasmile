package com.teasmile.controller.user;

import com.teasmile.context.LoginContext;
import com.teasmile.entity.AddressBook;
import com.teasmile.result.ApiResult;
import com.teasmile.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/12 13:45
 * @地址管理
 */
@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "C端-地址管理接口")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前用户所有地址
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public ApiResult<List<AddressBook>> list(){
        AddressBook addressBook=new AddressBook();
        addressBook.setUserId(LoginContext.getUserId());
        List<AddressBook> list=addressBookService.list(addressBook);
        return ApiResult.success(list);

    }

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public ApiResult save(@RequestBody AddressBook addressBook){
        addressBookService.save(addressBook);
        return ApiResult.success();
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public ApiResult<AddressBook> getById(@PathVariable Long id){
        AddressBook addressBook=addressBookService.getById(id);
        return ApiResult.success(addressBook);
    }

    /**
     * 根据id修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public ApiResult update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return ApiResult.success();
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public ApiResult setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return ApiResult.success();
    }
    /**
     * 根据id删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public ApiResult deleteById(Long id) {
        addressBookService.deleteById(id);
        return ApiResult.success();
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public ApiResult<AddressBook> getDefault(){
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(LoginContext.getUserId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return ApiResult.success(list.get(0));
        }

        return ApiResult.error("没有查询到默认地址");
    }


}
