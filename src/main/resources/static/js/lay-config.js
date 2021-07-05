/**
 * date:2019/08/16
 * author:Mr.Chung
 * description:此处放layui自定义扩展
 * JS中 (function(){...})()立即执行函数:两种形式
 *     (function(){...})()
       (function(){...}())
 函数声明：function fname(){...}; 使用function关键字声明一个函数，再指定一个函数名。
         函数表达式：var fname=function(){...}; 使用function关键字声明一个函数，但未给函数命名，最后将匿名函数赋予给一个变量。
 */

window.rootPath = (function (src) {
    src = document.scripts[document.scripts.length - 1].src;
    return src.substring(0, src.lastIndexOf("/") + 1);
})();
layui.config({
    base: rootPath + "lay-module/",
    version: true
}).extend({
    layuimini: "layuimini/layuimini", // layuimini扩展
    step: 'step-lay/step', // 分步表单扩展
    treetable: 'treetable-lay/treetable', //table树形扩展
    tableSelect: 'tableSelect/tableSelect', // table选择扩展
    iconPickerFa: 'iconPicker/iconPickerFa', // fa图标选择扩展
    echarts: 'echarts/echarts', // echarts图表扩展
    echartsTheme: 'echarts/echartsTheme', // echarts图表主题扩展
    wangEditor: 'wangEditor/wangEditor', // wangEditor富文本扩展
    layarea: 'layarea/layarea', //  省市县区三级联动下拉选择器
    jquery_cookie:'jquery-cookie/jquery.cookie',
    bodyTab: "body/bodyTab",
    formSelects:"formSelects/formSelects-v4"
});