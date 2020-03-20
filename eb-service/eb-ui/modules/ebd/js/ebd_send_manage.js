$(function () {

    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/ebd/page?sendFlag=2',
        datatype: "json",
        colModel: [			
			{ label: '数据包编号', name: 'ebdId', index: "ebdId", width: 200,align:"center"},
			{ label: '目标资源', name: 'destEbrName', width: 200 ,align:"center"},
            { label: '发送时间', name: 'ebdSendTime', width: 150 ,align:"center"},
            { label: '指令类型', name: 'ebdTypeName', width: 150,align:"center"},
            { label: '数据包大小', name: 'fileSize', width: 100 ,align:"center",
                formatter: function (cellvalue, options, row ) {
                    var operateHtml="-"
                    if(cellvalue!=null&&cellvalue!=""){
                        operateHtml= fileChange(cellvalue);
                    }
                    return operateHtml;
                }
             },
            {
                label: "数据包下载",
                name: "fileId",
                width: 75,
                align:"center",
                formatter: function (cellvalue, options, row ) {
                    var operateHtml="-"
                    if(cellvalue!=null&&cellvalue!=""){
                        operateHtml= "<a  href='javascript:downFile(\""+cellvalue+"\")' style='text-decoration:underline;color: #00fbff'>下载</a>";
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
        req:{
            ebdDestEbrId:"",
            ebdId:null,
            startSendTime:null,
            endSendTime:null
        },
        ebrList:[]
    },
    methods:{
        reload : function(){
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            var startTime="",endTime="";
            if(vm.req.startSendTime!=null &&　vm.req.startSendTime!=''){
            	startTime = vm.req.startSendTime+" 00:00:00";
            }
            if(vm.req.endSendTime!=null &&　vm.req.endSendTime!=''){
            	endTime = vm.req.endSendTime+" 23:59:59";
            }
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{"ebdId":vm.req.ebdId
                    ,"ebdDestEbrId":vm.req.ebdDestEbrId
                    ,"startSendTime":startTime
                    ,"endSendTime":endTime
                },
                page:page
            }).trigger("reloadGrid");
        },
        init:function(){
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/ebrView/listChildren",
                contentType: "application/json",
                data:{
                    ebrId:null,
                    ebrName:null,
                    ebrType:null,
                    areaCode:null,
                    ebrState:null
                },
                success: function (r) {
                    if (r.successful ) {
                        vm.ebrList=r.data;
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
    }
});

function initLayDate() {
    var startTimeRender= laydate.render({
        elem: '#startSendTime'
        ,type: 'date'
        ,theme: 'molv'
        , change: function (value, date) {
        }
        , done: function (value, date) {
            if (value == "" || value == null) {
                Vue.set(vm.req, 'startSendTime', '');
            } else {
                Vue.set(vm.req, 'startSendTime', value);
            }

        }
    });

    var  endTimeRender= laydate.render({
        elem: '#endSendTime'
        , type: 'date'
        ,theme: 'molv'
        , change: function (value, date) {
        }
        , done: function (value, date) {
            if (value == "" || value == null) {
                Vue.set(vm.req, 'endSendTime', '');
            } else {
                Vue.set(vm.req, 'endSendTime', value);
            }
        }
    });
}

function downFile(fileId){
    // 模拟表单提交同步方式下载文件,能够弹出保存文件对话框
    var url = baseURL+"safeRest/file/download";
    jQuery('<form action="'+url+'" method="post">' +
        '<input type="text" name="fileId" value="'+fileId+'"/>' +
        '<input type="text" name="fileType" value="1"/>' +
        '<input type="text" name="token" value="'+token+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}

