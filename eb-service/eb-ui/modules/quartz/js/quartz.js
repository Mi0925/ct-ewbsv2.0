$(function () {
    //vm.getEnum(dictGroupCodes);
    $("#jqGrid").jqGrid({
        url: baseURL+'/safeRest/quartz/getAllJobsByPage',
        datatype: "json",
        colModel: [
            { label: '任务分组名称', name: 'jobGroup', width: 100,align:"center"},
            { label: '任务名称', name: 'jobName', width: 100 ,align:"center"},
            { label: '任务描述', name: 'description', width: 150 ,align:"center"},
            { label: '任务状态', name: 'triggerState', width: 50,align:"center",
                formatter:function (cellvalue,options,row) {
                	if(row.jobName == 'HeartBeatJob' && vm.heartBeatState =='1'){
                		return "暂停";
                	}else if(row.jobName == 'HeartBeatJob' && vm.heartBeatState =='2'){
                		return "正常运行";
                	}
                    if(cellvalue&& cellvalue.length > 15) {
                        cellvalue= cellvalue.substring(0,15)+ '...';
                    }
                    var textValue = "";
                    switch (cellvalue) {
	                    case 'PAUSED':textValue = "暂停";break;
	                    case 'NORMAL':textValue = "正常运行";break;
	                    case 'BLOCKED':textValue = "阻塞";break;
	                    case 'COMPLETE':textValue = "完成";break;
	                    case 'ERROR':textValue = "错误";break;
	                    case 'WAITING':textValue = "等待";break;
	                    case 'ACQUIRED':textValue = "正常运行";break;
	                    case 'NONE':textValue = "不存在";break;
                     } 
                    return textValue;
                } },
            { label: '任务类名', name: 'jobClassName', width: 100,align:"center" },
            { label: 'cron表达式', name: 'cronExpression', width: 100,align:"center" },
            { label: '上一次执行时间', name: 'prevFireTime', width: 100,align:"center" },
            { label: '下一次执行时间', name: 'nextFireTime', width: 100,align:"center" },
            {
                label: "操作",
                field: "empty",
                width: 100,
                align:"center",
                formatter: function (cellvalue, options, row ) {
                    var operateHtml="";
                        operateHtml += "<a  href='javascript:pauseJob(\""+row.jobGroup+"\",\""+row.jobName+"\")' style='text-decoration:underline;color: #00fbff'>暂停</a>";
                        operateHtml += "&nbsp;&nbsp;<a  href='javascript:resumeJob(\""+row.jobGroup+"\",\""+row.jobName+"\")' style='text-decoration:underline;color: #00fbff'>恢复</a>";
                        operateHtml += "&nbsp;&nbsp;<a  href='javascript:toUpdateJob(\""+row.jobGroup+"\",\""+row.jobName+"\",\""+row.cronExpression+"\")' style='text-decoration:underline;color: #00fbff'>修改</a>";
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

var dictGroupCodes = "";
var vm = new Vue({
    el: '#rrapp',
    data: {
        permissions: {

        },
        req:{},
        cron:'',
        jobName:'',
        groupName:'',
        pageEnum:[],
        heartBeatState:'1'
    },
    methods:{
        reload : function(){
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{},
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


function pauseJob(groupName, jobName) {
    var data = {jobDetailName:jobName,groupName:groupName};
    $.ajax({
        type: "put",
        async:false,
        url: baseURL+'safeRest/quartz/pauseJob',
        contentType: "application/json",
        data:JSON.stringify(data),
        success: function (r) {
            if (r.successful ) {
            	vm.heartBeatState = '1';
                vm.reload();
            } else {
                layer.msg(r.msg);
            }
        }
    });
}
function resumeJob(groupName, jobName) {
    var data = {jobDetailName:jobName,groupName:groupName};
    $.ajax({
        type: "put",
        async:false,
        url: baseURL+'safeRest/quartz/resumeJob',
        contentType: "application/json",
        data:JSON.stringify(data),
        success: function (r) {
            if (r.successful ) {
            	vm.heartBeatState = '2';
                vm.reload();
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

function toUpdateJob(groupName, jobName, cron) {
    vm.groupName = groupName;
    vm.jobName = jobName;
    vm.cron = cron;
    layer.open({
        type: 1,
        shade: false,
        title: false, //不显示标题
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $('#quartzLayer'),
        area: ['500px', '400px'], //宽高
    });
}
function updateJob() {
    var data = {jobDetailName:vm.jobName,groupName:vm.groupName,jobCronExpression:vm.cron};
    $.ajax({
        type: "put",
        async:false,
        url: baseURL+'safeRest/quartz/updateJob',
        contentType: "application/json",
        data:JSON.stringify(data),
        success: function (r) {
            if (r.successful ) {
                vm.reload();
                vm.closeLayer();
            } else {
                alert(r.msg);
            }
        }
    });

}


