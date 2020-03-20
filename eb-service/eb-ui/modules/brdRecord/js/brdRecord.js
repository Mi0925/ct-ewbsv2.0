$(function () {

    vm.getDictEnum("ebd_broadcast_state","broadcastStateEnum");
    var ebmId = getQueryString("ebmId");
    if(ebmId==null){
        ebmId="";
        vm.showBack=false;
    }else{
        vm.showBack=true;
    }
    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/ebmBrdRecord/page?ebmId='+ebmId,
        datatype: "json",
        colModel: [			
			{ label: '编号', name: 'brdItemId', index: "brdItemId", width: 200,align:"center"},
            { label: '方案名称', name: 'schemeTitle', width: 150 ,align:"center"},
			{ label: '目标单位', name: 'ebrName', width: 150 ,align:"center"},
            { label: '播发状态', name: 'brdStateCode', width: 100,align:"center",formatter:function (cellvalue,options,row) {
                    return matchAndGetDictValue(vm.broadcastStateEnum,cellvalue);
                }},
            { label: '广播效果(%)', name: 'coverageRate', width: 80,align:"center"},
            {
                label: "操作",
                name: "ebmId",
                width: 100,
                align:"center",
                formatter: function (cellvalue, options, row ) {
                    var operateHtml = "<a  href='javascript:backDetail(\""+row.brdItemId+"\",\""+row.schemeTitle+"\")' style='text-decoration:underline;color: #00fbff'>反馈详情</a>";
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

});

var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{"senderCode":"",brdStateCode:""},
        permissions: {
            "core:brdRecord:query": false
        },
        ebrList:[],
        broadcastStateEnum:[],
        ebmStateBackList:[],
        brdItemId:"",
        msgTitle:"",
        ebmId:"",
        showBack:false

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
        },
        goBack:function () {
            history.go(-1);
        }
    },
    created: function () {
       //初始化数据
        this.init();
    }
});



function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam','page');
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{"brdStateCode":vm.req.brdStateCode,"senderCode":vm.req.senderCode},
        page:page
    }).trigger("reloadGrid");
}


function backDetail(brdItemId,schemeTitle){
    vm.ebmStateBackList=[];
    vm.msgTitle="";
    vm.brdItemId="";
    //1.获取反馈详细信息  2.展开数据
    $.ajax({
        type: "get",
        url: baseURL+"safeRest/ebmStateBack/list",
        contentType: "application/json",
        data:{"brdItemId":brdItemId},
        success: function (r) {
            if (r.successful ) {
                vm.ebmStateBackList=r.data;
                vm.msgTitle=schemeTitle;
                vm.brdItemId=brdItemId;
                $(".float-div").show();
                $(".right-float-div").css("width",0);
                $(".right-float-div").animate({width:"440px"},300);
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

