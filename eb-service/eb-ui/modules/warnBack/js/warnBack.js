$(function () {

    vm.getDictEnum("SEVERITY_DICT","severityEnum");
    vm.getDictEnum("ebd_broadcast_state","broadcastStateEnum");

    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/ebm/page?sendFlag=1',
        datatype: "json",
        colModel: [			
			{ label: 'EBM编号', name: 'ebmId', index: "ebmId", width: 200,align:"center"},
			{ label: '来源单位', name: 'sendName', width: 200 ,align:"center"},
            { label: '事件级别', name: 'severity', width: 150 ,align:"center",formatter:function (cellvalue,options,row) {
                return matchAndGetDictValue(vm.severityEnum,cellvalue,"SEVERITY_DICT");
            }},
            { label: '事件标题', name: 'msgTitle', width: 200 ,align:"center"},
            { label: '播发状态', name: 'broadcastState', width: 100,align:"center",formatter:function (cellvalue,options,row) {
                    return matchAndGetDictValue(vm.broadcastStateEnum,cellvalue,"ebd_broadcast_state");
                }},
            { label: '发布日期', name: 'createTime', width: 150,align:"center"},
            {
                label: "操作",
                name: "ebmId",
                width: 150,
                align:"center",
                formatter: function (cellvalue, options, row ) {
                    var operateHtml = "<a  href='javascript:backDetail(\""+cellvalue+"\",\""+row.msgTitle+"\")' style='text-decoration:underline;color: #00fbff'>反馈详情</a>";
                    operateHtml +=  "&nbsp;&nbsp;&nbsp;<a  href='javascript:handBack(\""+cellvalue+"\")' style='text-decoration:underline;color: #00fbff'>手动反馈</a>";
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
        onSelectRow: function(id){



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
        req:{"ebdSrcEbrId":""},
        permissions: {
            "core:ebm:warnBack": false,
            "core:ebm:query": false,
        },
        ebrViewList:[],
        severityEnum:[],
        broadcastStateEnum:[],
        ebmStateBackList:[],
        ebmId:"",
        msgTitle:""

    },
    methods:{
        getDictEnum: function (dictGroupCode,obj) {
            $.ajax({
                type: "get",
                async:false,
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
        init:function(){
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/ebrView/fromEbrlist",
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.ebrViewList=r.data;
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
       //初始化数据
        this.init();
    },
    filters: {


    }
});

function initLayDate() {
    var startTimeRender= laydate.render({
        elem: '#startReceiveTime'
        ,type: 'date'
        ,theme: 'molv'
        , change: function (value, date) {
        }
        , done: function (value, date) {
            if (value == "" || value == null) {
                Vue.set(vm.req, 'startReceiveTime', '');
            } else {
                Vue.set(vm.req, 'startReceiveTime', value);
            }

        }
    });

    var  endTimeRender= laydate.render({
        elem: '#endReceiveTime'
        , type: 'date'
        ,theme: 'molv'
        , change: function (value, date) {
        }
        , done: function (value, date) {
            if (value == "" || value == null) {
                Vue.set(vm.req, 'endReceiveTime', '');
            } else {
                Vue.set(vm.req, 'endReceiveTime', value);
            }
        }
    });
}

function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam','page');
    var startTime="",endTime="";
    if(vm.req.startReceiveTime!=null &&　vm.req.startReceiveTime!=''){
    	startTime = vm.req.startReceiveTime+" 00:00:00";
    }
    if(vm.req.endReceiveTime!=null &&　vm.req.endReceiveTime!=''){
    	endTime = vm.req.endReceiveTime+" 23:59:59";
    }
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{"ebmId":vm.req.ebmId,"msgTitle":vm.req.msgTitle,"senderCode":vm.req.ebdSrcEbrId,"startTime":startTime,"endTime":endTime},
        page:page
    }).trigger("reloadGrid");
}

function handBack(ebmId){
    var obj=new Object();
    obj.ebmId=ebmId;
    $.ajax({
        type: "post",
        url: baseURL+"safeRest/ebm/handBack",
        contentType: "application/json",
        data:JSON.stringify(obj),
        success: function (r) {
            if (r.successful ) {
                layer.msg("手动反馈已提交，请稍后查询！");
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

function backDetail(ebmId,msgTitle){
    vm.ebmStateBackList=[];
    vm.msgTitle="";
    vm.ebmId="";
    //1.获取反馈详细信息  2.展开数据
    $.ajax({
        type: "get",
        url: baseURL+"safeRest/ebmStateBack/list",
        contentType: "application/json",
        data:{"ebmId":ebmId},
        success: function (r) {
            if (r.successful ) {
                vm.ebmStateBackList=r.data;
                vm.msgTitle=msgTitle;
                vm.ebmId=ebmId;
                $(".float-div").show();
                $(".right-float-div").css("width",0);
                $(".right-float-div").animate({width:"440px"},300);
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

