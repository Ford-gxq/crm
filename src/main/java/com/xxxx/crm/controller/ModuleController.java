package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
@Autowired
private ModuleService moduleService;
    /**
     * 进入资源模块页面
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "module/module";
    }

    /**
     * 资源展示列表
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> moduleList(){
        return moduleService.queryAllModules();
    }


}
