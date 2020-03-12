//package com.carson.vboot.core.controller;
//
//import com.carson.vboot.core.base.VBootController;
//import com.carson.vboot.core.base.VbootService;
//import com.carson.vboot.core.entity.Department;
//import com.carson.vboot.core.service.DepartmentService;
//import io.swagger.annotations.Api;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author oufuhua
// */
//@Api(description = "部门管理")
//@RestController
//@RequestMapping("/department")
//public class DepartmentController extends VBootController<Department> {
//
//    @Autowired
//    private DepartmentService departmentService;
//
//    /**
//     * 获取service
//     *
//     * @return
//     */
//    @Override
//    public VbootService<Department> getService() {
//        return departmentService;
//    }
//}
