$(function () {

    vm.getEnum(dictGroupCodes);


    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/file/page?auditState=0',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'id', index: "id", width: 45, key: true, hidden: true},
            { label: '文件名称', name: 'originName', index: "originName", width: 100,align:"center"},
            { label: '文件类型', name: 'fileType', width: 50,align:"center",
                formatter:function (cellvalue,options,row) {
                    if(cellvalue == 1){
                        return "媒体文件"
                    }else{
                        return "文本文件"
                    }
                }},
            { label: '文件名后缀', name: 'fileExt', width: 50 ,align:"center"},
            { label: '文件大小', name: 'byteSize', width: 50 ,align:"center",
            formatter:function (cellvalue,options,row) {
                return fileChange(cellvalue)
            }},
            { label: '文件夹名称', name: 'libName', width: 100,align:"center"},
            { label: '创建人', name: 'createUser', width: 50 ,align:"center"},
            { label: '创建时间', name: 'createDate', width: 100 ,align:"center"},
            { label: '文件URL', name: 'fileUrl', width: 120 ,align:"center"},
            { label: '操作', name: '', width: 50 ,align:"center" ,
                formatter:function (cellvalue,options,row) {
                    var operateHtml="";
                    if(vm.permissions["res:file:detail"]){
                        operateHtml +=  '&nbsp;&nbsp;&nbsp;<a style=\'text-decoration:underline;cursor:pointer;color: #00fbff\' onclick="getFileDetail(\'' + row.id + '\')">详情</a>'
                    }
                    return   operateHtml;
                }},
        ],
        viewrecords: true,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers:true,
        multiselect: true,
        multiboxonly: true,
        rownumWidth: 50,
        gridview: true,
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
        loadComplete:function(){

        },
        gridComplete:function(){
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            $("#jqGrid").css({"width": $("#gbox_jqGrid").width()-1});
            $("#gview_jqGrid").find(".ui-jqgrid-htable").css({"width": $("#gbox_jqGrid").width()});
            $("#gview_jqGrid").find(".ui-jqgrid-hdiv").css({"width": $("#gbox_jqGrid").width()});
            $("#gview_jqGrid").find(".ui-jqgrid-bdiv").css({"width": $("#gbox_jqGrid").width()});
        },
        //当用户点击当前行在未选择此行时触发。rowid：此行id；e：事件对象。返回值为ture或者false。如果返回true则选择完成，如果返回false则不会选择此行也不会触发其他事件
        beforeSelectRow:function(rowid, e){

        },
        //当点击单元格时触发。rowid：当前行id；iCol：当前单元格索引；cellContent：当前单元格内容；e：event对象
        onCellSelect:function (rowid, iCol, cellcontent, e) {

        },
        //当选择行时触发此事件。rowid：当前行id；status：选择状态，当multiselect 为true时此参数才可用
        onSelectRow:function (rowid, status) {
          //  var rowData = $("#jqGrid").jqGrid('getRowData',rowid);
          //  getFileDetail(rowData.id);
            vm.selectedFileIds = [];
            var rowIds = $("#jqGrid").jqGrid('getGridParam', 'selarrrow');
            if(!isEmpty(rowIds) && rowIds.length > 0) {
                for (var i = 0; i < rowIds.length; i++) {
                    var rowId = rowIds[i];
                    var rowData = $("#jqGrid").jqGrid('getRowData', rowId);
                    vm.selectedFileIds.push(rowData.id);
                }
            }
        },
        //multiselect为ture，且点击头部的checkbox时才会触发此事件。aRowids：所有选中行的id集合，为一个数组。status：boolean变量说明checkbox的选择状态，true选中false不选中。无论checkbox是否选择，aRowids始终有 值
        onSelectAll:function(aRowids,status){

        },
        //在行上右击鼠标时触发此事件。rowid：当前行id；iRow：当前行位置索引；iCol：当前单元格位置索引；e：event对象
        onRightClickRow : function (rowid, iRow, iCol, e) {

        },
        //双击行时触发。rowid：当前行id；iRow：当前行索引位置；iCol：当前单元格位置索引；e:event对象
        ondblClickRow:function (rowid, iRow, iCol, e) {

        }
    });

});


var dictGroupCodes = "";

var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{
            libName:'',
            originName:''
        },
        permissions: {
            "res:file:download":false,
            "res:file:upload":false,
            "res:file:delete":false,
            "res:file:edit":false,
            "res:file:audit":false,
            "res:file:list":false,
            "res:file:detail":false,
            "res:filedir:create":false,
            "res:filedir:edit":false,
            "res:filedir:delete":false
        },
        pageEnum:[],
        selectedFileIds:[],
        title:'',
        auditComment:'',
        fileInfo:null
    },
    mounted: function(){
        getPermission(this);
    },
    created:function(){

    },
    filters: {
        //获取枚举名称
        getEnumName:function (value,dictCode) {
            if(isEmpty(vm.pageEnum) || isEmpty(dictCode)){
                return value;
            }
            return matchAndGetDictValue(vm.pageEnum,value,dictCode);
        },
        //字符串过长，截取掉。
        filterFun: function (value,len) {
            if(value&& value.length > len) {
                value= value.substring(0,len)+ '...';
            }
            return value;
        },
        filterFileSize :function (value) {
            return fileChange(value);
        }


    },
    methods:{
        reload : function(){
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "originName":vm.req.originName
                    ,"libName":vm.req.libName
                },
                page:page
            }).trigger("reloadGrid");
        },

        getEnum: function (dictGroupCode) {
            if(isEmpty(dictGroupCode)){
                return;
            }
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
        closeLayer:function(){
            layer.closeAll();
        },

    }
});

function getFileDetail(fileId) {
    if(isEmpty(fileId)){
        return;
    }
    $.ajax({
        type: "get",
        async:false,
        url: baseURL+"safeRest/file/"+fileId,
        contentType: "application/json",
        success: function (r) {
            if (r.successful ) {
                vm.fileInfo = r.data;
                $(".float-div").show();
                $(".right-float-div").css("width",0);
                $(".right-float-div").animate({width:"440px"},300);
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

function auditFile(state) {
    var ids = vm.selectedFileIds;
    if(isEmpty(ids)){
        alert("请选中需要审核的文件！");
        return;
    }
    var req = [];
    for(var i=0;i<ids.length;i++){
        var data = {id:ids[i],auditState : state,auditComment:vm.auditComment};
        req.push(data);
    }
    $.ajax({
        type: "put",
        async:false,
        url: baseURL+"safeRest/file/audit/batch",
        contentType: "application/json",
        data:JSON.stringify(req),
        success: function (r) {
            if (r.successful ) {
                vm.closeLayer();
                vm.reload();
                $(".left-sidebar").click();
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

function toAuditFile(){
    vm.title="文件审核";
    var ids = vm.selectedFileIds;
    if(isEmpty(ids)){
        alert("请选中需要审核的文件！");
        return;
    }
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
      //  closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#fileAuditLayer'),
        area: ['600px', '350px'], //宽高
    });
}
