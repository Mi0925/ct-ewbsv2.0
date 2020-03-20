$(function () {

    vm.getEnum(dictGroupCodes);
    vm.initLayDate();


    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/scheme/page',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'schemeId', index: "schemeId", width: 45, key: true, hidden: true},
            { label: '方案名称', name: 'schemeTitle', index: "schemeName", width: 120,align:"center"},
            { label: '广播类型', name: 'severity', width: 80 ,align:"center",
                formatter:function (cellvalue,options,row) {
                    if(cellvalue == "4"){
                        return "日常广播";
                    }else{
                        return "应急广播";
                    }
                }
            },
            { label: '来源单位', name: 'sendName', width: 120,align:"center"},
            { label: '广播时间', name: 'startTime', width: 100 ,align:"center"},
            { label: '审核状态', name: 'auditResult', width: 80 ,align:"center",
                formatter:function (cellvalue,options,row) {
                	if(cellvalue==null){
                		return "未审核";
                	}
                    return matchAndGetDictValue(vm.pageEnum,cellvalue,'AUDIT_STATUS_DICT');
                }
            },
            { label: '关联信息/节目', name: 'msgTitle', width: 120 ,align:"center"},
            { label: '方案专题', name: 'schemeId', width: 100 ,align:"center"
                ,formatter:function (cellvalue,options,row) {
                    if(vm.permissions["core:scheme:detail"]){
                        return '<a style=\'text-decoration:underline;cursor:pointer;color: #00fbff\' onclick="showSchemeDetail(\'' + row.ebmId + '\')">方案专题</a>'
                    }
                    return "";
                }
            },
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


var dictGroupCodes = "AUDIT_STATUS_DICT,BROADCAST_TYPE_DICT";

var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{
            schemeTitle:null,
            bcType:'',
            auditResult:'',
            endTime:null,
            startTime:null
        },
        permissions: {
            "core:scheme:detail":false
        },
        pageEnum:[]
    },
    mounted: function(){
        getPermission(this);
    },
    created:function(){

    },
    filters: {


    },
    methods:{
        reload : function(){
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{"auditResult":vm.req.auditResult
                    ,"startTime":vm.req.startTime
                    ,"endTime":vm.req.endTime
                    ,"bcType":vm.req.bcType
                    ,"schemeTitle":vm.req.schemeTitle
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

        initLayDate: function () {
            var startTimeRender= laydate.render({
                elem: '#startTime'
                ,type: 'date'
                ,theme: 'molv'
                , change: function (value, date) {
                }
                , done: function (value, date) {
                    if (value == "" || value == null) {
                        Vue.set(vm.req, 'startTime', '');
                    } else {
                        Vue.set(vm.req, 'startTime', value);
                    }

                }
            });

            var  endTimeRender= laydate.render({
                elem: '#endTime'
                , type: 'date'
                ,theme: 'molv'
                , change: function (value, date) {
                }
                , done: function (value, date) {
                    if (value == "" || value == null) {
                        Vue.set(vm.req, 'endTime', '');
                    } else {
                        Vue.set(vm.req, 'endTime', value);
                    }
                }
            });
        }
    }
});

function showSchemeDetail(ebmId) {
    window.location.href = 'schemeDetail.html?ebmId='+ebmId;
}

