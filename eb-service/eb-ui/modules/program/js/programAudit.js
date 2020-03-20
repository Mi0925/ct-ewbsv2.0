$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/program/page?auditResult=0&state=2',
        datatype: "json",
        colModel: [			
			{ label: '节目编号', name: 'programId', index: "programId",hidden:true},
			{ label: '节目名称', name: 'programName', width: 200 ,align:"center"},
            { label: '事件级别', name: 'programLevel', width: 150 ,align:"center",formatter:function (cellvalue,options,row) {
                    return matchAndGetDictValue(vm.pageEnum,cellvalue,"SEVERITY_DICT");
             }},
            { label: '节目类型', name: 'programType', width: 100,align:"center",formatter:function (cellvalue,options,row) {
                    return matchAndGetDictValue(vm.pageEnum,cellvalue,"PROGRAM_TYPE_DICT");
                }},
            { label: '创建人', name: 'createUser', width: 100,align:"center"},
            { label: '创建时间', name: 'createTime', width: 150,align:"center"},
            {
                label: "操作",
                name: "programId",
                width: 60,
                align:"center",
                formatter: function (cellvalue, options, row ) {
                    var operateHtml="";
                    if(vm.permissions['core:program:audit']){
                        operateHtml += "<a  href='javascript:toAudit(\""+cellvalue+"\",\""+row.programName+"\")' style='text-decoration:underline;color: #00fbff'>审核</a>";
                    }
                    return operateHtml;
                }
            }
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
        }
    });
    initLayDate();
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{"programName":"","programType":"","auditResult":""},
        pageEnum:[],
        msgTitle:"",
        permissions: {
            "core:program:list": false,
            "core:program:audit": false
        },
        areaNames:null,
        programInfo:null,
        contentBtnShow:false,
        weeks:[],
    },
    watch : {
        "programInfo.programContent":function(val,oldVal){
                if(val!=null){
                    var content=val.content;
                    if(content.length>100){
                        vm.contentBtnShow=true;
                    }
                }
        }
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
        doAudit : function () {
            var programId = vm.programInfo.programId;
            var auditResult = $("#auditResult").val();
            var auditOpinion = $("#auditOpinion").val();
            if(isEmpty(programId)){
                return
            }
            if(isEmpty(auditResult)){
                return;
            }
            if(auditOpinion ==''){
                alert("请输入审核意见");
                return;
            }
            var data = {
                programId:programId,
                auditResult:auditResult,
                auditOpinion:auditOpinion
            };
            $.ajax({
                type: "put",
                url: baseURL+"safeRest/program/audit",
                contentType: "application/json",
                data:JSON.stringify(data),
                success: function (r) {
                    if (r.successful ) {
                        layer.msg("审核成功！");
                        leftSidebarClick();
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
        downFile:function (fileId){
            // 模拟表单提交同步方式下载文件,能够弹出保存文件对话框
            var url = baseURL+"safeRest/file/download";
            jQuery('<form action="'+url+'" method="post">' +
                '<input type="text" name="fileId" value="'+fileId+'"/>' +
                '<input type="text" name="fileType" value="2"/>' +
                '<input type="text" name="token" value="'+token+'"/>' +
                '</form>')
                .appendTo('body').submit().remove();
        }

    },
    mounted: function(){
        getPermission(this);
        var dictGroupCodes = "SEVERITY_DICT,AUDIT_STATUS_DICT,PROGRAM_TYPE_DICT,PROGRAM_LEVEL_DICT,PROGRAM_STATE_DICT,PROGRAM_CONTENT_TYPE,PROGRAM_STRATEGY_TYPE,EVENT_TYPE_DICT";
        this.getEnum(dictGroupCodes);
    },
    created: function () {
    }
});

function initLayDate() {
    var startCreateTimeRender= laydate.render({
        elem: '#startCreateTime'
        ,type: 'date'
        ,theme: 'molv'
        , change: function (value, date) {
        }
        , done: function (value, date) {
            if (value == "" || value == null) {
                Vue.set(vm.req, 'startCreateTime', '');
            } else {
                Vue.set(vm.req, 'startCreateTime', value);
            }

        }
    });
    var  endStartCreateTimeRender= laydate.render({
        elem: '#endStartCreateTime'
        , type: 'date'
        ,theme: 'molv'
        , change: function (value, date) {
        }
        , done: function (value, date) {
            if (value == "" || value == null) {
                Vue.set(vm.req, 'endStartCreateTime', '');
            } else {
                Vue.set(vm.req, 'endStartCreateTime', value);
            }
        }
    });
}

function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam','page');
    var startTime="",endTime="";
    if(vm.req.startCreateTime!=null &&　vm.req.startCreateTime!=''){
    	startTime = vm.req.startCreateTime+" 00:00:00";
    }
    if(vm.req.endStartCreateTime!=null &&　vm.req.endStartCreateTime!=''){
    	endTime = vm.req.endStartCreateTime+" 23:59:59";
    }
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{"programName":vm.req.programName,"programType":vm.req.programType,"startCreateTime":startTime,"endStartCreateTime":endTime},
        page:page
    }).trigger("reloadGrid");
}


function toAudit(programId,msgTitle){
    vm.msgTitle="";
    //1.获取详细信息  2.展开数据
    $.ajax({
        type: "get",
        url: baseURL+"safeRest/program/"+programId,
        contentType: "application/json",
        success: function (r) {
            if (r.successful ) {
                vm.programInfo=r.data;
                vm.getAreaNames(r.data.areaList);
                parseWeekMask(vm.programInfo.programStrategy.weekMask+"");
                vm.msgTitle=msgTitle;
                $(".float-div").show();
                $(".right-float-div").css("width",0);
                $(".right-float-div").animate({width:"514px"},300);
            } else {
                layer.msg(r.msg);
            }
        }
    });
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


