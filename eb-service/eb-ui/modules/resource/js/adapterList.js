$(function () {

    vm.getEnum(dictGroupCodes);


    $("#jqGrid").jqGrid({
        url: baseURL+'/safeRest/ebr/ebrAdapter/page',
        datatype: "json",
        colModel: [
            { label: '适配器名称', name: 'ebrAsName',  width: 100,align:"center"},
            { label: '适配器URL', name: 'url', width: 120 ,align:"center",
                formatter:function (cellvalue,options,row) {
                    if(isEmpty(cellvalue)){
                        return "";
                    }
                    if(cellvalue && cellvalue.length > 15) {
                        cellvalue= cellvalue.substring(0,15)+ '...';
                    }
                    return cellvalue;
                }
            },
            {label: '资源编码', name: 'ebrAsId', index: "ebrAsId", width: 100, key: true},
            { label: '资源状态', name: 'adapterState', width: 80,align:"center",
                formatter:function (cellvalue,options,row) {
                    return matchAndGetDictValue(vm.pageEnum,cellvalue,'RES_STATUS_DICT');
            }},
            { label: '汇报时间', name: 'updateTime', width: 100 ,align:"center"},
            { label: '经度', name: 'longitude', width: 50 ,align:"center"},
            { label: '纬度', name: 'latitude', width: 50 ,align:"center"},

        ],
        viewrecords: true,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers:true,
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
            var rowData = $("#jqGrid").jqGrid('getRowData',rowid);
            getAdapterDetail(rowData.ebrAsId);
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


var dictGroupCodes = "RES_LEVEL_DICT,EBR_TYPE,RES_STATUS_DICT,EBR_BROADCAST_SUB_TYPE,EBR_PLATFORM_SUB_TYPE,SYS_RES_SUB_TYPE,SUB_RES_TYPE_DICT";

var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{
            ebrAsName:'',
          //  ebrAsId:'',
            adapterState:''
        },
        pageEnum:[],
        ebrInfo:null
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
        getPlatformName:function (value) {
            if(isEmpty(value)){
                return '无';
            }
            var name;
            $.ajax({
                type: "get",
                async:false,
                url: baseURL+"safeRest/ebrPlatform/"+value,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        name = r.data.psEbrName;
                    } else {
                        name = value;
                    }
                }
            });
            return name;
        },

    },
    methods:{
        reload : function(){
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "ebrAsName":vm.req.ebrAsName
                    ,"adapterState":vm.req.adapterState
                },
                page:page
            }).trigger("reloadGrid");
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
    }
});

function getAdapterDetail(ebrId) {
    if(isEmpty(ebrId)){
        return;
    }
    $.ajax({
        type: "get",
        url: baseURL+"/safeRest/ebr/ebrAdapter/"+ebrId,
        contentType: "application/json",
        success: function (r) {
            if (r.successful ) {
                vm.ebrInfo = r.data;
                //    vm.msgDescBtnShow = false;
                $(".float-div").show();
                $(".right-float-div").css("width",0);
                $(".right-float-div").animate({width:"500px"},300);
            } else {
                layer.msg(r.msg);
            }
        }
    });
}
