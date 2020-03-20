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
            { label: '播放日期', name: 'playDate', width: 100 ,align:"center",cellattr: addCellAttr,
                formatter:function (cellvalue,options,row) {
                    return cellvalue.substr(0,cellvalue.indexOf(" "));
                }
            },
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
        ],
        viewrecords: true,
        rowNum: 10,
        rowList : [10,30,50,100],
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
        onSelectRow:function(rowid, status){
            var rowData = $("#jqGrid").jqGrid('getRowData',rowid);
            toDetail(rowData.id);
        }
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

var dictGroupCodes = "SEVERITY_DICT,EVENT_TYPE_DICT,AUDIT_STATUS_DICT,PROGRAM_TYPE_DICT,PROGRAM_LEVEL_DICT,PROGRAM_STATE_DICT,PROGRAM_CONTENT_TYPE,PROGRAM_STRATEGY_TYPE";

var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{
       //     month:"",
            playDate:""
        },
        pageEnum:[],
        msgTitle:"",
        permissions: {
        },
        areaNames:null,
        programInfo:null,
        title:"",
        auditShow:false,
        commitBtnShow:false,
        auditResult:"1",
        auditOpinion:"",
        contentBtnShow:false,
        monthList :[],
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
       /* getSelectMonthOption : function(){
            var curDate = new Date();
            this.req.month = curDate.getMonth()+1;
            for(var i=1 ;i<= parseInt(this.req.month) ;i++){
                var d =  {
                    key : i,
                    value : i+"月"
                };
                this.monthList.push(d);
            }
        },
*/
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
        //this.initLayDate();
       // this.getSelectMonthOption();
    }
});

function initLayDate() {
    var curDate = new Date();
    var maxDate = curDate.getFullYear()+"-" + (curDate.getMonth()+1) +"-"+curDate.getDate();
    var startCreateTimeRender= laydate.render({
        elem: '#req_playDate'
        ,type: 'date'
        ,theme: 'molv'
        , change: function (value, date) {
        	
        }
        , done: function (value, date) {
            if (value == "" || value == null) {
                Vue.set(vm.req, 'playDate', '');
            } else {
                Vue.set(vm.req, 'playDate', value);
            }
            reload();
        }
    });
    /*var  endStartCreateTimeRender= laydate.render({
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
    });*/
}

function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam','page');
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{playDate:vm.req.playDate},
        page:page
    }).trigger("reloadGrid");
}
/*function onMonthChange(obj) {
    var dateStr = $(obj).val()
    vm.req.month = dateStr;
    reload();
}*/

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

