$(function () {

    vm.getEnum(dictGroupCodes);

    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/user/page?deleteFlag=false',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'userId', index: "userId", width: 100,hidden:true},
            { label: '账户', name: 'account', width: 100,align:"center"},
            { label: '姓名', name: 'userName', width: 100 ,align:"center"},
            { label: '状态', name: 'status', width: 50 ,align:"center",
                formatter:function (cellvalue, options, row) {
                    if(cellvalue==1){
                        return "启用";
                    }else{
                        return "停用";
                    }
                }
            },
            { label: '是否已删除', name: 'deleteFlag', width: 50 ,align:"center",
                formatter:function (cellvalue, options, row) {
                    if(cellvalue==1){
                        return "是";
                    }else{
                        return "否";
                    }
                }
            },
            { label: '创建时间', name: 'createTime', width: 100,align:"center" },
            {
                label: "操作",
                field: "empty",
                width: 100,
                align:"center",
                name : "userId",
                formatter: function (cellvalue, options, row ) {
                    if(row.deleteFlag == 1){
                        return "";
                    }else{
                        var operateHtml = '';
                        if(vm.permissions['sys:user:edit']){
                             operateHtml = "<a  href='javascript:toUpdate(\""+cellvalue+"\")' style='text-decoration:underline;color: #00fbff'>修改</a>";
                            if(row.status == 0){
                                operateHtml +=  "&nbsp;&nbsp;&nbsp;<a  href='javascript:changeStatus(\""+cellvalue+"\",\"1\")' style='text-decoration:underline;color: #00fbff'>启用</a>";
                            }else{
                                operateHtml +=  "&nbsp;&nbsp;&nbsp;<a  href='javascript:changeStatus(\""+cellvalue+"\",\"0\")' style='text-decoration:underline;color: #00fbff'>停用</a>";
                            }
                            operateHtml +=  "&nbsp;&nbsp;&nbsp;<a  href='javascript:deleteUser(\""+cellvalue+"\")' style='text-decoration:underline;color: #00fbff'>删除</a>";
                        }
                        if(vm.permissions['sys:userRole:ref']){
                            operateHtml +=  "&nbsp;&nbsp;&nbsp;<a  href='javascript:roleRef(\""+cellvalue+"\")' style='text-decoration:underline;color: #00fbff'>关联角色</a>";
                        }
                        return operateHtml;
                    }
                }
            }
        ],
        viewrecords: true,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers:true,
        rownumWidth: 50,
        autowidth:true,
        pager: "#jqGridPager",
        jsonReader : {    //jsonReader来跟服务器端返回的数据做对应
            root: "data",
            page: "currPage",
            total: "totalPage",
            records: "totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            $("#jqGrid").css({"width": $("#gbox_jqGrid").width()-1});
            $("#gview_jqGrid").find(".ui-jqgrid-htable").css({"width": $("#gbox_jqGrid").width()});
            $("#gview_jqGrid").find(".ui-jqgrid-hdiv").css({"width": $("#gbox_jqGrid").width()});
            $("#gview_jqGrid").find(".ui-jqgrid-bdiv").css({"width": $("#gbox_jqGrid").width()});
        }
    });




});

var dictGroupCodes = "USER_STATE_DICT";

var vm = new Vue({
    el: '#rrapp',
    data: {
        permissions: {
            "sys:user:add": false,
            "sys:user:edit": false,
            "sys:user:view": false,
            "sys:userRole:ref": false,
        },
        req:{
            status:"",
        },
        userInfo:{},
        roles:{},
        roleIds:[],
        userRoleRef:{},
        userUpdateShow : false,
        userAddShow:false,
        title:"",
        pageEnum:[]
    },
    filters: {
        //字符串过长，截取掉。
        filterFun: function (value) {
            if (value && value.length > 6) {
                value = value.substring(0, 5)+"..." ;
            }
            return value;
        }
    },
    methods:{
        add:function () {
            vm.userUpdateShow = false;
            vm.userAddShow = true;
            vm.title = "新增用户";
            vm.userInfo={};
            layer.open({
                type: 1,
                shade: false,
                title: false, //不显示标题
                closeBtn: 0, //不显示关闭按钮
                anim: 2,
                shadeClose: true, //开启遮罩关闭
                content: $('#userLayer'),
                area: ['500px', '500px'], //宽高
            });
        },

        save:function(){
            if(isEmpty(vm.userInfo.account)){
                alert("请输入账号！");
                return;
            } else if (!checkAccount(vm.userInfo.account)) {
                alert("账户输入不合法，请输入6~30位字母、数字、下划线");
                return;
            }
            if(isEmpty(vm.userInfo.password)){
                alert("请输入密码！");
                return;
            } else if (!checkAccount(vm.userInfo.password)) {
                alert("密码输入不合法，请输入6~30位字母、数字、下划线");
                return;
            }
            if(isEmpty(vm.userInfo.password2)){
                alert("请再次输入密码！");
                return;
            }
            if(isEmpty(vm.userInfo.userName)){
                alert("请输入真实姓名！");
                return;
            }
            var firstPwd = vm.userInfo.password;
            var secondPwd = vm.userInfo.password2;
            if(firstPwd != secondPwd){
                alert("两次输入密码不一致！");
                return;
            }
            $.ajax({
                type: "post",
                url: baseURL+"safeRest/user/save",
                contentType: "application/json",
                data:JSON.stringify(vm.userInfo),
                success: function (r) {
                    if (r.successful) {
                        vm.closeLayer();
                        reload();
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        closeLayer:function(){
            layer.closeAll();
        },
        update:function (flag) {
            $.ajax({
                type: "put",
                url: baseURL+"safeRest/user/updateUserInfo",
                contentType: "application/json",
                data:JSON.stringify(vm.userInfo),
                success: function (r) {
                    if (r.successful) {
                        if(flag){
                            localStorage.removeItem("userId");
                            localStorage.removeItem("token");
                            parent.location.href = '../../modules/common/login.html';
                            if(!isEmpty(layer)){
                                vm.closeLayer();
                            }
                        }else{
                            if(!isEmpty(layer)){
                                vm.closeLayer();
                            }
                            reload();
                        }

                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getEnum: function (dictGroupCode) {
            $.ajax({
                type: "get",
                async:false,
                url: baseURL+"safeRest/dict/getByGroupCode",
                contentType: "application/json",
                data:{dictGroupCode:dictGroupCode},
                success: function (r) {
                    if (r.successful ) {
                        vm.pageEnum = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        saveUserRoleBatch: function () {
            console.log("========="+vm.roleIds);
            vm.userRoleRef = {
                userId:vm.userInfo.userId,
                roleIds:vm.roleIds
            };
            $.ajax({
                type: "post",
                url: baseURL+"safeRest/role/refRoles",
                contentType: "application/json",
                data:JSON.stringify(vm.userRoleRef),
                success: function (r) {
                    if (r.successful ) {
                        if(!isEmpty(layer)){
                            layer.closeAll();
                        }
                        reload();
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getUserInfo :function (id) {
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/user/getByUserId",
                contentType: "application/json",
                data:{userId:id},
                success: function (r) {
                    if (r.successful) {
                        vm.userInfo = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getRoles :function () {
            $.ajax({
                type: "get",
                url: baseURL+'safeRest/role/getRoles',
                contentType: "application/json",
                success: function (r) {
                    if (r.successful) {
                        vm.roles = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getUserRoleIds :function (id) {
            $.ajax({
                type: "get",
                url: baseURL+'safeRest/role/getUserRoleIds',
                contentType: "application/json",
                data:{userId:id},
                success: function (r) {
                    if (r.successful) {
                        vm.roleIds = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },


    },
    mounted: function(){
        getPermission(this);
    },
    created: function () {

    }
});



function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam','page');
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{"account":vm.req.account,"userName":vm.req.userName,"status":vm.req.status},
        page:page
    }).trigger("reloadGrid");
}


function changeStatus(id, status) {
    if(id == userId && status == 0){
        confirm("该用户已处于登录状态，停用后将退出并返回登录页面。确定要停用该用户？",function () {
            vm.userInfo={userId:id,status:status}
            vm.update(true);
        })
    }else{
        vm.userInfo={userId:id,status:status}
        vm.update(false);
    }
}

function deleteUser(id) {
    if(id == userId){
        confirm("该用户已处于登录状态，删除后将退出并返回登录页面。确定要删除该用户？",function () {
            vm.userInfo={userId:id,deleteFlag:true}
            vm.update(true);
        })
    }else{
        confirm("确定要删除该用户？",function () {
            vm.userInfo={userId:id,deleteFlag:true}
            vm.update(false);
        })
    }

}
function roleRef(id) {
    vm.userInfo = {};
    vm.title = "用户管理角色";
  //  initRoleList();
    vm.getUserInfo(id);
    vm.getRoles();
    vm.getUserRoleIds(id);
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#userRoleLayer'),
        area: ['800px', '500px'], //宽高
    });
}

function toUpdate(id){
    vm.userUpdateShow = true;
    vm.userAddShow = false;
    vm.title = "修改用户";
    $.ajax({
        type: "get",
        url: baseURL+"safeRest/user/getByUserId",
        contentType: "application/json",
        data:{userId:id},
        success: function (r) {
            if (r.successful) {
                vm.userInfo = r.data;

                layer.open({
                    type: 1,
                    shade: false,
                    title: false, //不显示标题
                    closeBtn: 0, //不显示关闭按钮
                    anim: 2,
                    shadeClose: true, //开启遮罩关闭
                    content: $('#userLayer'),
                    area: ['500px', '500px'], //宽高
                });
            } else {
                layer.msg(r.msg);
            }
        }
    });
}
