$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/sys/param/page',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 100,hidden:true},
            { label: '参数名称', name: 'paramName', width: 100,align:"center"},
            { label: '参数值', name: 'paramValue', width: 100 ,align:"center"},
            { label: '参数键', name: 'paramKey', width: 100 ,align:"center"},
            { label: '备注', name: 'description', width: 100 ,align:"center"},
            { label: '排序', name: 'orderNum', width: 50 ,align:"center"},
            {
                label: "操作",
                field: "empty",
                width: 50,
                name: "id",
                align:"center",
                formatter: function (cellvalue, options, row ) {
                    var operateHtml = '';
                    if(vm.permissions['sys:param:edit']){
                        operateHtml =  "<a  href='javascript:toUpdate(\""+cellvalue+"\")' style='text-decoration:underline;color: #00fbff'>修改</a>";
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

var vm = new Vue({
    el: '#rrapp',
    data: {
        permissions: {
            "sys:param:edit": false,
            "sys:param:view": false,
        },
        req:{paramType:""},
        paramAddShow:false,
        paramUpdateShow:false,
        pageEnum:[],
        paramInfo:{},
        title:""
    },
    methods:{
        initLayer:function (type) {
            if(type =="add"){
                vm.paramUpdateShow = false;
                vm.paramAddShow = true;
                vm.title = "新增系统参数";
                vm.paramInfo={};
            }else{
                vm.paramUpdateShow = true;
                vm.paramAddShow = false;
                vm.title = "修改系统参数";
            }
            layer.open({
                type: 1,
                shade: false,
                title: false, //不显示标题
                closeBtn: 0, //不显示关闭按钮
                anim: 2,
                shadeClose: true, //开启遮罩关闭
                content: $('#paramAddLayer'),
                area: ['600px', '550px'], //宽高
            });
        },
        update:function(){
            if(isEmpty(vm.paramInfo.paramName)){
                alert("请输入参数名称");
                return;
            }
            if(isEmpty(vm.paramInfo.paramValue)){
                alert("请选择参数值");
                return;
            }
            $.ajax({
                type: "put",
                url: baseURL+"safeRest/sys/param/update",
                contentType: "application/json",
                data:JSON.stringify(vm.paramInfo),
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

        getEnum: function (dictGroupCode) {
            $.ajax({
                type: "get",
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
        getSysParam : function (paramId) {
            $.ajax({
                type: "get",
                async:false,
                url: baseURL+"safeRest/sys/param/"+paramId,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.paramInfo = r.data;
                    } else {
                        layer.msg(r.msg);
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
        this.getEnum("SYS_PARAM_TYPE");
    },
    created: function () {

    }
});



function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam','page');
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{"paramName":vm.req.paramName,"paramKey":vm.req.paramKey,"paramType":vm.req.paramType},
        page:page
    }).trigger("reloadGrid");
}

function toUpdate(paramId) {
    vm.getSysParam(paramId);
    vm.initLayer();
}

