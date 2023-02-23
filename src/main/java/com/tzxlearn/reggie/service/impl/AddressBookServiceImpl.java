package com.tzxlearn.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzxlearn.reggie.domain.AddressBook;
import com.tzxlearn.reggie.service.AddressBookService;
import com.tzxlearn.reggie.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author 20357
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2023-02-22 13:29:34
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




