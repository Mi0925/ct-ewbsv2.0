$(function () {
    vm.getEnum(dictGroupCodes);

    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/role/page',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 100,hidden:true},
            { label: '角色名称', name: 'name', width: 100,align:"center"},
            { label: '角色描述', name: 'description', width: 100 ,align:"center"},
            { label: '角色类型', name: 'type', width: 50 ,align:"center",
                formatter:function (cellvalue, options, row) {
                    if(cellvalue==1){
                        return "用户自定义";
                    }else{
                        return "系统默认";
                    }
                }
            },
            { label: '创建时间', name: 'createTime', width: 100,align:"center" },
            {
                label: "操作",
                field: "empty",
                name:"id",
                width: 100,
                align:"center",
                formatter: function (cellvalue, options, row ) {
                    var operateHtml="-";
                    if(vm.permissions['sys:role:edit']){
                        operateHtml = "<a  href='javascript:toUpdate(\""+cellvalue+"\")' style='text-decoration:underline;color: #00fbff'>修改</a>";
                    }
                    if(vm.permissions['sys:role:delete']){
                        if(row.type==1){
                            operateHtml +=  "&nbsp;&nbsp;&nbsp;<a  href='javascript:deleteRole(\""+cellvalue+"\")' style='text-decoration:underline;color: #00fbff'>删除</a>";
                        }
                    }
                    if(vm.permissions['sys:roleRes:ref']){
                        operateHtml +=  "&nbsp;&nbsp;&nbsp;<a  href='javascript:author(\""+cellvalue+"\",\""+row.name+"\")' style='text-decoration:underline;color: #00fbff'>角色授权</a>";
                    }
                    return operateHtml;
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

var dictGroupCodes = "ROLE_TYPE_DICT";
var pid = '0';
var vm = new Vue({
    el: '#rrapp',
    data: {
        permissions: {
            "sys:role:add": false,
            "sys:role:edit": false,
            "sys:role:view": false,
            "sys:role:delete": false,
            "sys:roleRes:ref": false,
        },
        req:{status:""},
        roleInfo:{
            type:"1"
        },
        roleAddShow:false,
        roleUpdateShow:false,
        pageEnum:[],
        roleResources:[],
        treeData:[],
        title:"",
        roleName:"",
        roleId:"",
        resourcesByRoleId:[]

    },
    methods:{
        initLayer:function (type) {
            if(type =="add"){
                vm.roleUpdateShow = false;
                vm.roleAddShow = true;
                vm.title = "新增角色";
                vm.roleInfo={type:"1"};
            }else{
                vm.roleUpdateShow = true;
                vm.roleAddShow = false;
                vm.title = "编辑角色";
            }
            layer.open({
                type: 1,
                shade: false,
                title: false, //不显示标题
                closeBtn: 0, //不显示关闭按钮
                anim: 2,
                shadeClose: true, //开启遮罩关闭
                content: $('#roleAddLayer'),
                area: ['500px', '400px'], //宽高
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
        getRoleInfo:function (roleId) {
            $.ajax({
                type: "get",
                async:false,
                url: baseURL+"safeRest/role/"+roleId,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.roleInfo = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getRoleResources:function (roleId) {
            $.ajax({
                type: "get",
                async:false,
                url: baseURL+"safeRest/role/getRoleResources",
                contentType: "application/json",
                data:{roleId : roleId},
                success: function (r) {
                    if (r.successful ) {
                        vm.roleResources = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        save:function () {
            if(isEmpty(vm.roleInfo.name)){
                alert("请输入角色名称");
                return;
            }
            if(isEmpty(vm.roleInfo.type)){
                alert("请选择角色类型");
                return;
            }
            $.ajax({
                type: "post",
                async:false,
                url: baseURL+"safeRest/role/save",
                contentType: "application/json",
                data:JSON.stringify(vm.roleInfo),
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
        update:function () {
            if(isEmpty(vm.roleInfo.name)){
                alert("请输入角色名称");
                return;
            }
            if(isEmpty(vm.roleInfo.type)){
                alert("请选择角色类型");
                return;
            }
            $.ajax({
                type: "put",
                async:false,
                url: baseURL+"safeRest/role/update",
                contentType: "application/json",
                data:JSON.stringify(vm.roleInfo),
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
        delete:function (roleId) {
            $.ajax({
                type: "delete",
                url: baseURL+"safeRest/role/delete/"+roleId,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                       reload();
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        sureGrant:function(){
           var resourceIds=new Array();
           var tt= getAllChecked();
            $.each(tt,function(index, item){
                resourceIds.push(item.resourceId);
            });
            if (resourceIds == null || resourceIds.length < 1) {
                alert("请至少添加一个权限！");
                return;
            }
             var obj=new Object();
             obj.roleId=vm.roleId;
             obj.resourceIdList=resourceIds;
            $.ajax({
                type: "post",
                async:false,
                url: baseURL+"safeRest/roleRes/saveRoleResBatch",
                contentType: "application/json",
                data:JSON.stringify(obj),
                success: function (r) {
                    if (r.successful ) {
                        leftSidebarClick();
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },

        closeLayer:function(){
            layer.closeAll();
        }
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
        postData:{"name":vm.req.name},
        page:page
    }).trigger("reloadGrid");
}


function toUpdate(roleId) {
    vm.getRoleInfo(roleId);
    vm.getRoleResources(roleId);
    vm.initLayer('update');
}

function deleteRole(roleId) {
    confirm("确定要删除该角色？",function () {
        vm.delete(roleId);
    })
}

function getAllWithRecursion() {
    $.ajax({
        type: "get",
        async:false,
        url: baseURL+"safeRest/sysRes/getAllWithRecursion",
        contentType: "application/json",
        success: function (r) {
            if (r.successful ) {
                createTreeData(r.data);
            } else {
                layer.msg(r.msg);
            }
        }
    });
}
var  treeData = new Array();
function createTreeData(resData) {
    treeData = new Array();
    if(isEmpty(resData)){
        return [];
    }


    $.each(resData,function(index, item){
        //需要递归
        var nodes = getNodes(item);
        var flag= isHas(item.id);
        var obj=new Object();
        obj.text=item.name;
        obj.resourceId=item.id;
        if(nodes.length!=0){
            obj.nodes=nodes;
        }
        if(flag){
            obj.state={checked:true};
        }
        treeData.push(obj);
    });
}
function getNodes(item) {
    var nodes = [];
    if(item.subList != null && item.subList.length > 0){
        for(var i=0;i<item.subList.length;i++){
            var subItem = item.subList[i];
            var nodes1 = getNodes(subItem);
            var flag= isHas(subItem.id);
            var node =new Object();
            node.text=subItem.name;
            node.resourceId=subItem.id;
            if(nodes1.length!=0){
                node.nodes=nodes1;
            }
            if(flag){
                node.state={checked:true};
            }
            nodes.push(node);
        }
    }
    return nodes;
}

function author(roleId,roleName) {
    //获取菜单树
    vm.roleId=roleId;
    vm.roleName=roleName;
    getDefault();
    getAllWithRecursion();
    initTree("resTree",treeData);
    $(".float-div").show();
    $(".right-float-div").css("width",0);
    $(".right-float-div").animate({width:"360px"},300);
}

function leftSidebarClick() {
    $(".right-float-div").animate({width:"0px"},300,function () {
        $(".float-div").hide();
    });
}

//获取默认的值
function getDefault() {
    vm.resourcesByRoleId=[];
    $.ajax({
        type: "get",
        async:false,
        url: baseURL+"safeRest/role/getRoleResources?roleId="+vm.roleId,
        contentType: "application/json",
        success: function (r) {
            if (r.successful ) {
                vm.resourcesByRoleId=r.data;
            }
        }
    });
}

function isHas(id) {
    var flag=false;
    $.each(vm.resourcesByRoleId,function(index, item){
            if(item.resourceId==id){
                flag=true;
                return false;
            }
    });
    return flag;
}