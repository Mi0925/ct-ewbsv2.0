$(function () {

  //  vm.getEnum(dictGroupCodes);

	initLayDate();

});

function initTables() {
    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/program/unit/list',
        datatype: "json",
        colModel: [
            { label: '节目单编号', name: 'id', index: "id",hidden:true},
            { label: '开始时间', name: 'startTime', width: 100 ,align:"center",cellattr: addCellAttr},
            { label: '结束时间', name: 'endTime', width: 100,align:"center",cellattr: addCellAttr},
            { label: '节目名称', name: 'programName', width: 200 ,align:"center",cellattr: addCellAttr,
                formatter:function (cellvalue,options,row) {
                    var html = cellvalue ;
                    if(row.auditResult == 1){
                        html += '<img style="margin-left: 20px;" src="../../modules/common/img/audited.png">';
                    }
                    return html;
                }},
            { label: '审核状态', name: 'auditResult', width: 100,align:"center",cellattr: addCellAttr,formatter:function (cellvalue,options,row) {
                    return matchAndGetDictValue(vm.pageEnum,cellvalue,"AUDIT_STATUS_DICT");
                }
            },
            { label: '节目单状态', name: 'state', width: 100,align:"center",cellattr: addCellAttr,formatter:function (cellvalue,options,row) {
                    if(cellvalue == 1){
                        return "未确认"
                    }else if(cellvalue == 2){
                        return "已确认"
                    }else if(cellvalue == 3){
                        return "已取消"
                    }
                }
            },
            {
                label: "操作",
                name: "",
                width: 150,
                align:"center",
                formatter: function (cellvalue, options, row ) {
                    var operateHtml="<a  href='javascript:toDetail(\""+row.id+"\")' style='text-decoration:underline;color: #00fbff'>详情</a>";
                    if(vm.permissions["core:programunit:edit"] && row.auditResult != 1 && row.state==1){
                        operateHtml +="&nbsp;&nbsp;&nbsp;<a  href='javascript:toUpdate(\""+row.id+"\",\""+row.startTime+"\",\""+row.endTime+"\")' style='text-decoration:underline;color: #00fbff'>修改</a>";
                    }
                    if(vm.permissions["core:programunit:delete"] && (row.playFlag != 2 || row.playFlag != 3) && row.state != 3){
                        operateHtml +=  "&nbsp;&nbsp;&nbsp;<a  href='javascript:toDelete(\""+row.id+"\")' style='text-decoration:underline;color: #00fbff'>删除</a>";
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
            setAuditButton();
        },
        gridComplete:function(){
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            $("#jqGrid").css({"width": $("#gbox_jqGrid").width()-1});
            $("#gview_jqGrid").find(".ui-jqgrid-htable").css({"width": $("#gbox_jqGrid").width()});
            $("#gview_jqGrid").find(".ui-jqgrid-hdiv").css({"width": $("#gbox_jqGrid").width()});
            $("#gview_jqGrid").find(".ui-jqgrid-bdiv").css({"width": $("#gbox_jqGrid").width()});
        }
    });
}
function setAuditButton() {
    var rowids = $("#jqGrid").getDataIDs();
    var suditShow = false;
    if(!isEmpty(rowids)){
        for(var i=0;i < rowids.length; i++){
            var rowData = $("#jqGrid").jqGrid('getRowData',rowids[i]);
            //存在未审核或者审核不通过的节目单，显示审核按钮
            if(rowData.auditResult =="未审核" && vm.permissions["core:programunit:audit"]){
                suditShow = true;
                break;
            }
        }
        if(suditShow){
            for(var i=0;i < rowids.length; i++){
                var rowData = $("#jqGrid").jqGrid('getRowData',rowids[i]);
                //存在未确认的节目单，不显示审核按钮
                if((rowData.state != "已确认" && rowData.state != "已取消") || !vm.permissions["core:programunit:edit"]){
                    suditShow = false;
                    break;
                }
            }
        }
        //所有节目单都确认完成，显示审核按钮
        vm.auditShow = suditShow;

        if(suditShow){
            vm.commitBtnShow = false;
        }else{
            for(var i=0;i < rowids.length; i++){
                var rowData = $("#jqGrid").jqGrid('getRowData',rowids[i]);
                //存在未确认的节目单，不显示审核按钮
                if((rowData.state != "已确认" && rowData.state != "已取消") || !vm.permissions["core:programunit:edit"]){
                    vm.commitBtnShow = true;
                    break;
                }
            }
        }
    }

}

function addCellAttr(rowId, val, rawObject, cm, rdata) {
    if(rawObject.playFlag == 2){
        //正在播放
        return "style='color:green'";
    }else if(rawObject.playFlag == 3){
        //已播放完成
        return "style='color:rgba(153,153,153,0.5)'";
    }

    if(rawObject.conflicting){
        return "style='color:red'";
    }
}

var dictGroupCodes = "SEVERITY_DICT,AUDIT_STATUS_DICT,EVENT_TYPE_DICT,PROGRAM_TYPE_DICT,PROGRAM_LEVEL_DICT,PROGRAM_STATE_DICT,PROGRAM_CONTENT_TYPE,PROGRAM_STRATEGY_TYPE";

var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{playDate:""},
        pageEnum:[],
        msgTitle:"",
        permissions: {
            "core:programunit:list":false,
            "core:programunit:add":false,
            "core:programunit:audit":false,
            "core:programunit:edit":false,
            "core:programunit:delete":false
        },
        areaNames:null,
        programInfo:null,
        title:"",
        auditShow:false,
        commitBtnShow:false,
        auditResult:"1",
        auditOpinion:"",
        contentBtnShow:false,
        dateList :[],
        weeks:[],
        programUnit:{}
    },
    watch : {
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
        filterDescFun : function (value,len) {
            if(value&& value.length > len) {
                value= value.substring(0,len)+ '...';
            }
            return value;
        }
    },
    methods:{
        getDictEnum: function (dictGroupCode,obj) {
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/dict/getByGroupCode",
                contentType: "application/json",
                data:{dictGroupCode:dictGroupCode},
                success: function (r) {
                    if (r.successful ) {
                        Vue.set(vm,obj,r.data);
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
                        initTables();
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getAreaNames : function (areaList) {
            vm.areaNames="";
            if(areaList==null || areaList.length == 0){
                return "";
            }
            var areaNames = "";
            for(var i=0;i < areaList.length;i++){
                var area = areaList[i];
                if(areaNames.length == 0){
                    areaNames += area.areaName;
                }else{
                    areaNames += ","+ area.areaName;
                }
            }
            vm.areaNames = areaNames;
        },
        getSelectDateOption : function(){
            var curDate = new Date();
            this.req.playDate = curDate.getFullYear()+"-" + (curDate.getMonth()+1) +"-"+curDate.getDate();
            for(var i=0 ;i< 4 ;i++){
                var curDate1 = new Date();
                var nextDate = DateAdd("d",i,curDate1);
                var dateStr = nextDate.getFullYear()+"-" + (nextDate.getMonth()+1) +"-"+nextDate.getDate();
                var d =  {
                    key : dateStr,
                    value : dateStr
                };
                this.dateList.push(d);
            }
        },
        saveProgramUnit : function(){
            if(isEmpty(vm.programUnit)){
                return;
            }
            if(isEmpty(vm.programUnit.startTime) || isEmpty(vm.programUnit.endTime)){
                alert("开始时间和结束时间不能为空！");
                return;
            }
            //设置成未确认状态
            vm.programUnit.state = 1;
            //设置成未审核状态
            vm.programUnit.auditResult = 0;
            $.ajax({
                type: "put",
                url: baseURL + "safeRest/program/unit/update",
                contentType: "application/json",
                data:JSON.stringify(vm.programUnit),
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
        }
    },
    mounted: function(){
        getPermission(this);
        this.getEnum(dictGroupCodes);

    },
    created: function () {
        //初始化日期控件
        this.getSelectDateOption();
    }
});

function initLayDate() {
    var startCreateTimeRender= laydate.render({
        elem: '#startTime_edit'
        ,type: 'time'
        ,theme: 'molv'
        , change: function (value, date) {
        }
        , done: function (value, date) {
            if (value == "" || value == null) {
                Vue.set(vm.programUnit, 'startTime', '');
            } else {
                Vue.set(vm.programUnit, 'startTime', value);
            }

        }
    });
    var  endStartCreateTimeRender= laydate.render({
        elem: '#endTime_edit'
        , type: 'time'
        ,theme: 'molv'
        , change: function (value, date) {
        }
        , done: function (value, date) {
            if (value == "" || value == null) {
                Vue.set(vm.programUnit, 'endTime', '');
            } else {
                Vue.set(vm.programUnit, 'endTime', value);
            }
        }
    });
}

function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam','page');
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{"playDate":vm.req.playDate},
        page:page
    }).trigger("reloadGrid");
}
function onDateChange(obj) {
    var dateStr = $(obj).val()
    vm.req.playDate = dateStr;
    reload();
}

function toDelete(unitId){
    confirm('确定要删除当前记录？', function () {
        $.ajax({
            type: "delete",
            url: baseURL + "safeRest/program/unit/delete/"+unitId,
            contentType: "application/json",
            success: function (r) {
                if (r.successful) {
                    layer.msg('操作成功');
                    reload();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    });
}

function toDetail(unitId){
    vm.msgTitle="";
    $.ajax({
        type: "get",
        url: baseURL + "safeRest/program/unit/"+unitId,
        contentType: "application/json",
        success: function (r) {
            if (r.successful) {
                vm.programInfo=r.data.programInfo;
                vm.getAreaNames(r.data.programInfo.areaList);
                vm.msgTitle=vm.programInfo.programName;
                parseWeekMask(vm.programInfo.programStrategy.weekMask+"");
                $(".float-div").show();
                $(".right-float-div").css("width",0);
                $(".right-float-div").animate({width:"514px"},300);
            } else {
                layer.msg(r.msg);
            }
        }
    });

}

function toUpdate(unitId, startTime, endTime) {
    vm.programUnit = {
        id:unitId,
        startTime:startTime,
        endTime:endTime
    };
    vm.title = "修改节目单";
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#unitEditLayer'),
        area: ['600px', '400px'], //宽高
    });
}

function programUnitCommit() {
    var dataList = [];
    var rowids = $("#jqGrid").getDataIDs();
    if(!isEmpty(rowids)){
        for(var i=0;i < rowids.length; i++){
            var rowData = $("#jqGrid").jqGrid('getRowData',rowids[i]);
            if(rowData.state != "已确认"){
                //已经提交确认的节目单不再提交
                var data = {
                    id:rowData.id,
                    state:2
                };
                dataList.push(data);
            }
        }
    }
    $.ajax({
        type: "put",
        url: baseURL + "safeRest/program/unit/updateBatch",
        contentType: "application/json",
        data:JSON.stringify(dataList),
        success: function (r) {
            if (r.successful) {
                reload();
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

function toAudit() {
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#unitAuditLayer'),
        area: ['600px', '400px'], //宽高
    });
}

function programUnitAudit() {
	vm.closeLayer();
    var ids = [];
    var delDataList = [];
    var rowids = $("#jqGrid").getDataIDs();
    if(!isEmpty(rowids)){
        for(var i=0;i < rowids.length; i++){
            var rowData = $("#jqGrid").jqGrid('getRowData',rowids[i]);
            if((rowData.state == "已确认" || rowData.state == "已取消") && rowData.auditResult !="审核通过"){
                //已经审核通过的节目单不再审核
                ids.push(rowData.id);
            }

            //已取消的节目单列表
            if(rowData.state == "已取消" && rowData.auditResult !="审核通过"){
                var data = {
                    id : rowData.id,
                    state : '4'
                };
                delDataList.push(data);
            }
        }
    }

    //修改节目单审核状态
    if(!isEmpty(ids)){
        var data = {
            ids : ids,
            auditResult:vm.auditResult,
            auditOpinion:vm.auditOpinion
        };
        $.ajax({
            type: "put",
            url: baseURL + "safeRest/program/unit/audit",
            contentType: "application/json",
            data:JSON.stringify(data),
            success: function (r) {
                if (r.successful) {
                    vm.closeLayer();
                    reload();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }

    //修改已取消的节目单 审核通过后，设置节目单状态为删除
    if(!isEmpty(delDataList) && vm.auditResult == 1){
        $.ajax({
            type: "put",
            url: baseURL + "safeRest/program/unit/updateBatch",
            contentType: "application/json",
            data:JSON.stringify(delDataList),
            success: function (r) {
                if (r.successful) {
                    vm.closeLayer();
                    reload();
                    cancelProgramEbm(delDataList);
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }

}

//下发节目单取消播放EBM消息
function cancelProgramEbm(delDataList) {
    var ids = [];
    for(var i=0;i<delDataList.length;i++){
        ids.push(delDataList[i].id);
    }
    if(!isEmpty(ids)){
        $.ajax({
            type: "post",
            url: baseURL + "safeRest/program/unit/cancel",
            contentType: "application/json",
            data:JSON.stringify(ids),
            success: function (r) {
                if (r.successful) {

                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }
}

function msgDescBtnClick(obj) {
    $("#openDesc").show();
    $("#closeDesc").hide();
    if($(obj).html() =='展开全部'){
        $(obj).html("收起详情");
        $("#openDesc").show();
        $("#closeDesc").hide();
    }else{
        $(obj).html("展开全部");
        $("#openDesc").hide();
        $("#closeDesc").show();
    }
}


function parseWeekMask(weekVal) {
    var p_weeks = [];
    var len = weekVal.length;
    if(len < 7){
        for(var i=0;i< 7-len;i++){
            weekVal = "0"+weekVal;
        }
    }
    if(weekVal.charAt(6) == 1){
        p_weeks.push("星期一");
    }
    if(weekVal.charAt(5) == 1){
        p_weeks.push("星期二");
    }
    if(weekVal.charAt(4) == 1){
        p_weeks.push("星期三");
    }
    if(weekVal.charAt(3) == 1){
        p_weeks.push("星期四");
    }
    if(weekVal.charAt(2) == 1){
        p_weeks.push("星期五");
    }
    if(weekVal.charAt(1) == 1){
        p_weeks.push("星期六");
    }
    if(weekVal.charAt(0) == 1){
        p_weeks.push("星期日");
    }
    vm.weeks = p_weeks;
}

