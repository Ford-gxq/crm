layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    /**
     *营销机会头部工具栏的---添加和删除按钮
     *      --->弹出的Dialog对话框的 确认按钮 绑定事件
     * 监听submit事件
     *  实现营销机会的添加与更新
     *  form.on('event(过滤器值)', callback回调函数);
     *      可以用于监听：select，checkbox，switch，radio，submit 的改变
     */
    form.on("submit(addOrUpdateSaleChance)",function (obj) {//form.on('event(过滤器值)', callback回调函数);
        //'submit(addOrUpdateSaleChance)'--->这是为确认按钮绑定的事件，小括号里的值对应确认框中lay-filter的值
        var dataField= obj.field;
        console.log(dataField+"<<<<");
        // 提交数据时的加载层 （https://layer.layui.com/）
        /**
         * 发送ajax做判断，如果是添加操作对应的把url=ctx + "/sale_chance/save"赋值给它
         * 如果是修改操作对应的把url=ctx+"/sale_chance/update"
         */
        var index = layer.msg("数据提交中,请稍后...",{//弹出层的设置
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });
        // 请求的地址
        var url = ctx + "/sale_chance/save";
        //获取隐藏域信息，决定添加或者是修改
        if ($("input[name=id]").val()){//非空即为真
            url=ctx+"/sale_chance/update";//如果销机会的ID不为空，则是修改
        }
        //否则即为添加操作，发送ajax请求给后台
        $.ajax({
            type:"post",
            url:url,
            data:obj.field,
            dataType:"json",
            success:function(data){//data-->HTTP请求发送成功后，执行成功回调函数要传递的参数对象
                //data-->controll层执行后的ResultInfo对象信息：code、msg、result
                if(data.code == 200){
                    //添加成功了
                    layer.msg("添加成功了",{icon:6});
                    //关闭加载层
                    layer.close(index);
                    //关闭iframe;
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }else{
                    //添加失败
                    layer.ms(data.msg,{icon: 5 });
                }
            }
        });
       //阻止表单提交
        return false;
    })
    /**
     * 取消操作--->为弹出的对话框中的 取消按钮绑定事件
     */
    $("#closeBtn").click(function () {
        //如果是iframe页面
        //Layui内置方法 ---> layer.getFrameIndex( 获取特定iframe层的索引）
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });
    /*动态的追加下拉框*/

    $(function(){
        //获取当前对象的指派人的id
        var assignMan=$("input[name=man]").val();
        $.ajax({
            type:"post",
            url:ctx+"/sale_chance/querySales",
            dataType:"json",
            success:function (data){
                //遍历
                for( var  x in data){
                    if(assignMan==data[x].id){
                        $("#assignMan").append(" <option selected value='"+data[x].id+"'>"+data[x].uname+"</option>");
                    }else{
                        $("#assignMan").append(" <option value='"+data[x].id+"'>"+data[x].uname+"</option>");
                    }
                }
                //重新加载下拉框
                layui.form.render("select");
            }


        });
    });

  });