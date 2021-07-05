layui.use(['form', 'jquery', 'jquery_cookie', 'table'], function () {
    var form = layui.form,
        layer = layui.layer,
        table = layui.table,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 销售机会列表展示--->table表格的render渲染方法
     */
    var tableIns = table.render({
        elem:'#saleChanceList',//表格绑定的id
        url:ctx+'/sale_chance/list',//访问数据的url地址(数据的来源),即为controller层的对应的handler方法
        cellMinWidth: 95,//单元格最小的宽度
        // page: true, // 开启分页
        page: true,//是否开启分页
        height:"full-125",
        limits:[10,15,20,25],//开启每页可以选择的数据条数的选项
        limit:10,//每页默认显示的数据条数
        toolbar:"#toolbarDemo",//绑定头部工具栏对象
        id:"saleChanceListTable",//此处是渲染后表格的id,为了搜索框绑定事件方便
        title:"用户数据表",
       cols:[[
           //表头
           {type: "checkbox", fixed: "center"},//复选框
           //表头(字段和对应的标题，排版方式)
           {field: "id", title: '编号', fixed: "true"},
           {field: 'chanceSource', title: '机会来源', align: "center"},
           {feild:'custmoerName',title: '客户名称',align:'center'},
           {feild:'cgjl',title: '成功记录',align: 'center'},
           {field: 'overview', title: '概要', align: 'center'},
           {field: 'linkMan', title: '联系人', align: 'center'},
           {field: 'linkPhone', title: '联系电话', align: 'center'},
           {field: 'description', title: '描述', align: 'center'},
           {field: 'createMan', title: '创建人', align: 'center'},
           {field: 'createDate', title: '创建时间', align: 'center'},
           {field: 'uname', title: '指派人', align: 'center'},
           {field: 'assignTime', title: '分配时间', align: 'center'},
           //分配状态里面加载了定义的模板函数-->函数内部调用了格式化分配状态的函数
           {field: 'state', title: '分配状态', align: 'center', templet: function (d) {
                   return formatterState(d.state);}
           },
           {field: 'devResult', title: '开发状态', align: 'center', templet: function (d) {
                   return formatterDevResult(d.devResult);
               }
           },
           {title: '操作', templet: '#saleChanceListBar', fixed: "right", align: "center", minWidth: 150}
       ]],

    });

    /**
     * 格式化分配状态
     *  0 - 未分配
     *  1 - 已分配
     *  其他 - 未知
     * @param state
     * @returns {string}
     */
    function formatterState(state) {
        if (state == 0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if (state == 1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

    /**
     * 格式化开发状态
     *  0 - 未开发
     *  1 - 开发中
     *  2 - 开发成功
     *  3 - 开发失败
     * @param value
     * @returns {string}
     */
    function formatterDevResult(value) {
        if (value == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if (value == 1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if (value == 2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if (value == 3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }

    /**
     * 开发搜索功能-->绑定事件
     */
  $(".search_btn").click(function () {
      //为搜索按钮绑定事件，调用table.reload('#数据表格id',{where:{//异步数据接口的额外参数},
      // page:{curr:1//开始渲染的页面} }  )方法重载数据表格
    tableIns.reload({
        where:{//设定异步数据接口的额外参数
            customerName: $("input[name='customerName']").val(),
            createMan: $("input[name='createMan']").val(),
            state: $("#state").val()
        },
        page: {
            curr: 1 //重新从第 1 页开始
        }

    });//只重载数据
});

    /*第二种方法：通过绑定table表格id重新加载数据
    $(".search_btn").click(function () {
        //上述方法等价于
        table.reload('saleChanceListTable',
            {
                where: { //设定异步数据接口的额外参数，任意设
                    customerName: $("input[name='customerName']").val(),
                    createMan: $("input[name='createMan']").val(),
                    state: $("#state").val()
                },
                 page: {
                    curr: 1 //重新从第 1 页开始
                }
            }
        ); //只重载数据

    });*/

    /**
     * 触发头部工具栏
     * table.on('event(过滤器值)', callback回调函数);
     *    可以用于监听：select，checkbox，switch，radio，submit 的改变
     */
   table.on("toolbar(saleChances)",function (obj) {//这里的obj是整个数据表格的参数对象tableIns
    console.log(obj);
    var checkStatus=table.checkStatus(obj.config.id);
    //这里obj.config.id="saleChanceListTable"-->这个id就是渲染后的整个数据表格的对象
    //obj.config.id.data-->拿到了表格中要删除的数据对象
       switch(obj.event){
           case 'add':
               // 点击添加按钮，调用对应的函数，打开添加营销机会的对话框
               //layer.msg("添加")
               openAddOrupdateDialog();
               break;
           case 'del':
               //删除
               deleteSaleChanceDialog(checkStatus.data);
               //checkStatus.data-->拿到了表格中要删除的某行数据对象
               console.log(checkStatus.data);
               break;
       };

   })
//在js/saleChance 目录下添加 add.update.js 文件，完成销售机会数据添加与更新表单提交操作。监听submit提交
    /**
     *添加和更新--->弹出层的处理函数
     * @param sid-->这里的sid用来区分添加操作和更新操作，如果是更新操作，iframe里面有一个隐藏域含有客户的id
     */
    function openAddOrupdateDialog(sid) {//sid-->表格中要更新的某行数据对象
        var title = "<h2>营销机会--添加操作</h2>";
        var url = ctx + "/sale_chance/addOrUpdateSaleChancePage";
        //非空即为真-->如果iframe表单域中含有sid则对应的是更新操作，拼接url地址后面的参数
        if (sid) {
            title = "<h2>营销机会--更新操作</h2>";
            url = url + "?id=" + sid;
        }
        console.log(url + "<<")
        //弹出层
        layer.open({
            title: title,
            type: 2,//2表示iframe页面
            content: url,
            /*content - 内容   类型：String/DOM/Array，默认：''
                content可传入的值是灵活多变的，不仅可以传入普通的html内容，还可以指定DOM，更可以随着type的不同而不同。*/
            area: ["500px", "620px"],
            maxmin: true
        });
    }

    /**
     *删除按钮-->删除对话框deleteSaleChanceDialog函数
     * @param ids
     */
    function deleteSaleChanceDialog(data) {//data-->表格中要删除的某行数据对象
        //判断
        if (data.length == 0) {
            layer.msg("请选择删除的数据");
            return;
        }
        /*
        *    layer.confirm(  "提示信息",{btn: ['按钮1', '按钮2']}, function () {
		// 按钮1的事件函数}, function(){ // 按钮2的事件函数}   );
        * */
        layer.confirm("你确定要删除此数据吗?", {
            btn: ["确定", "取消"]
        }, function (index) {
            //关闭确认框
            layer.close(index);
            //收集数据数据ids=1&ids=2&ids=3;
            var ids = [];
            //遍历
            for (var x in data) {//从要删除的数据对象中遍历拿到每条数据对象，并把对应的id值存入ids[]数组中
                ids.push(data[x].id);
            }
            console.log(ids.toString() + "<<<")
            //删除
            $.ajax({
                type: "post",
                url: ctx + "/sale_chance/dels",
                data: {"ids": ids.toString()},
                dataType: "json",
                success: function (obj) {
                    if (obj.code == 200) {
                        //重新局部刷新数据表格页面
                        tableIns.reload()
                    } else {
                        //删除失败了
                        layer.msg(obj.msg, {icon: 5});
                    }
                }
            });
        });

    }
    /**行内工具栏
     * 触发行内工具栏--表格行 监听事件
     *        saleChances为table标签的lay-filter 属性值
     * table.on('event(过滤器值)', callback回调函数);
     */
    //工具条事件
    table.on('tool(saleChances)', function (obj) {//obj为整张数据表的对象模型
        //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        console.log(data);
        var layEvent = obj.event; //获得 sale_chance.ftl前端页面的lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行tr 的 DOM 对象（如果有的话）

        if (layEvent === 'edit') { //编辑按钮
            openAddOrupdateDialog(obj.data.id);
        } else if (layEvent === 'del') { //删除按钮
            layer.confirm('真的删除改行数据吗？', function (index) {
                //关闭弹出层
                layer.close(index);
                //向服务端发送删除请求
                $.ajax({
                    type: "post",
                    url: ctx + "/sale_chance/dels",
                    data: {"ids":data.id},
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 200) {
                            //删除成功了,数据表重新刷新加载
                            tableIns.reload();
                        } else {
                            //删除失败
                            layer.msg(data.msg, {icon: 5})
                        }
                    }
                });
            });
        }
    });



});