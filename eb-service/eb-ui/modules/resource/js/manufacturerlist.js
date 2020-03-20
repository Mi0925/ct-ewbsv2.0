$(function () {

    vm.getEnum(dictGroupCodes);

    $("#jqGrid").jqGrid({
        url: baseURL+'/safeRest/ebr/EbrManufact/page',
        datatype: "json",
        colModel: [
        	{label: 'ID', name: 'id', index: "id", width: 100, key: true},
            { label: '单位名称', name: 'companyName',  width: 100,align:"center"},
            { label: '主要联系人名称', name: 'mainContactName', width: 120 ,align:"center"},
            { label: '主要联系人电话', name: 'mainContactTel', width: 80,align:"center",},
            { label: '技术人员名称', name: 'technologyName', width: 100 ,align:"center"},
            { label: '技术人员电话', name: 'technologyTel', width: 50 ,align:"center"},
            { label: '创建时间', name: 'createTime', width: 100 ,align:"center"},
            {
                label: "操作",
                field: "empty",
                width: 100,
                align:"center",
                name : "id",
                formatter: function (cellvalue, options, row ) {
                       var  operateHtml = "<a  href='javascript:toManuUpdate(\""+cellvalue+"\")' style='text-decoration:underline;color: #00fbff'>修改</a>";
                           operateHtml +=  "&nbsp;&nbsp;&nbsp;<a  href='javascript:toManuDelete(\""+cellvalue+"\")' style='text-decoration:underline;color: #00fbff'>删除</a>";
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
            vm.title = "新增厂商";
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
        	if(isEmpty(vm.userInfo.companyName)){
                alert("请输入厂商名称！");
                return;
            }
        	var url = '';
        	var type = "post";
        	if(vm.userUpdateShow){
        		url = baseURL+"/safeRest/ebr/EbrManufact/update";
        		type = "put";
        	}else{
        		url = baseURL+"/safeRest/ebr/EbrManufact/save";
        	}
            $.ajax({
                type: type,
                url: url,
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
        getUserInfo :function (id) {
            $.ajax({
                type: "get",
                url: baseURL+"/safeRest/ebr/EbrManufact/"+id,
                contentType: "application/json",
                data:{'ebrId':id},
                success: function (r) {
                    if (r.successful) {
                        vm.userInfo = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
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
        postData:{"companyName":vm.req.companyName},
        page:page
    }).trigger("reloadGrid");
}


function toManuDelete(id) {
    $.ajax({
        type: "delete",
        url: baseURL+"/safeRest/ebr/EbrManufact/delete/"+id,
        contentType: "application/json",
        data:{'id':id},
        success: function (r) {
            if (r.successful ) {
               reload();
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

function toManuUpdate(id){
    vm.userUpdateShow = true;
    vm.userAddShow = true;
    vm.title = "修改厂商";
    $.ajax({
        type: "get",
        url: baseURL+"/safeRest/ebr/EbrManufact/"+id,
        contentType: "application/json",
        data:{'ebrId':id},
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