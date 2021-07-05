layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 修改操作
     */
    form.on("submit(saveBtn)",function(obj){
        var dataField=obj.field;
        //发送ajax修改
        $.ajax({
            type:"post",
            url:ctx+"/user/update",
            data:{
                userName:dataField.username,
                phone:dataField.phone,
                email:dataField.email,
                trueName:dataField.trueName,
                id:dataField.id,
            },
            dataType:"json",
            success:function (data){
                if(data.code == 200){
                    //成功提示
                    layer.msg("保存成功了",function(){
                        //页面分发
                        window.parent.location.href=ctx+"/index";
                    });
                }else{
                    //失败提示
                    layer.msg(data.msg);
                }
            }
        });

        //阻止表单提交
        return false;
    });
});