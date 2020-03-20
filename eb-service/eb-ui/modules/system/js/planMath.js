$(function () {
	vm.getPlateform();
    $("#jqGrid").jqGrid({
        url: baseURL+'safeRest/plan/math/page',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'planId', index: "planId", width: 100,hidden:true},
            { label: '预案名称', name: 'planName', width: 80,align:"center"},
            { label: '事件类型', name: 'eventTypeName', width: 80,align:"center"},
            { label: '事件级别', name: 'severityList', width: 150 ,align:"center",formatter:function(cellvalue, options, row){

                    return getSeverityName(cellvalue);
                }},
            { label: '目标区域', name: 'areaList', width: 150 ,align:"center",formatter:function(cellvalue, options, row){

                    return getAreaName(cellvalue);
                }},
            { label: '预案流程', name: 'flowTypeName', width: 120 ,align:"center"},
            { label: '调用资源', name: 'refList', width: 150 ,align:"center",formatter:function(cellvalue, options, row) {
                    return getResoName(cellvalue);
                }},
            { label: '状态', name: 'status', width: 50 ,align:"center",
                formatter:function (cellvalue, options, row) {
                    if(cellvalue==1){
                        return "启用";
                    }else{
                        return "停用";
                    }
                }
            },
            { label: '创建时间', name: 'createTime', width: 100,align:"center" },
            {
                label: "操作",
                field: "empty",
                width: 80,
                align:"center",
                formatter: function (cellvalue, options, row ) {
                    var operateHtml = '';
                    if(vm.permissions['sys:plan:edit']){
                        operateHtml = "<a  href='javascript:upd(\""+row.planId+"\")' style='text-decoration:underline;color: #00fbff'>修改</a>";
                    }
                    if(vm.permissions['sys:plan:delete']){
                        operateHtml +=  "&nbsp;&nbsp;&nbsp;<a  href='javascript:del(\""+row.planId+"\")' style='text-decoration:underline;color: #00fbff'>删除</a>";
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

var vm = new Vue({
    el: '#rrapp',
    data: {
        permissions: {
            "sys:plan:add": false,
            "sys:plan:edit": false,
            "sys:plan:view": false,
            "sys:plan:delete": false,
        },
        req:{severity:""},
        planFlowList:[],
        resoList:[],
        areaList:[],
        severityList:[],
        ebrViewList:[],
        sysPlanMath:{"flowType":"","severity":"",refList:[],severityList:[],areaList:[],eventType:"00000",srcEbrId:""},
        areaDiv:false,
        eventTypeList:[],
        topArea:[]
    },
    methods:{
        init:function () {
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/dict/getSeverityFromSysDict",
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.severityList=r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });

            $.ajax({
                type: "get",
                url: baseURL+"safeRest/ebrView/listForType",
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.resoList=r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });

            $.ajax({
                type: "get",
                url: baseURL+"safeRest/dict/getPlanFlowFromSysDict",
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.planFlowList=r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });

            $.ajax({
                type: "get",
                url: baseURL+"safeRest/area/getNextAreaByCurPlatArea",
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.areaList=r.data;
                        vm.topArea=vm.areaList[0];
                        vm.areaList.splice(0,1);

                    } else {
                        layer.msg(r.msg);
                    }
                }
            });

            $.ajax({
                type: "get",
                url: baseURL+"safeRest/event/type/getNextByParentCode?parentCode=11000",
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.eventTypeList=r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });


        },
        getPlateform:function(){
            $.ajax({
                type: "get",
                async:false,
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
        },
        getPlanMathInfo:function(planId){
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/plan/math/"+planId,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.sysPlanMath=r.data;
                        //设置区域的checkbox
                        var areaList=vm.sysPlanMath.areaList;
                        var areaName="";
                        if(areaList.length==1){
                            areaName=areaList[0].areaName;
                        }
                        if(areaName!="全区域"){
                            if(areaList != null&&areaList.length>0) {
                                $.each(areaList, function(key, value) {
                                    if(value!=undefined&&value.areaCode!=""){
                                        $("input[name='targetArea'][value='"+value.areaCode+"']").prop("checked", true);
                                    }
                                });
                            }
                            vm.areaDiv=true;
                            $('input[name=allArea]').prop('checked',false);
                        }else{
                            vm.areaDiv=false;
                            $('input[name=allArea]').prop('checked',true);
                        }

                        //设置事件级别
                        var severityList=vm.sysPlanMath.severityList;
                        $.each(severityList, function(key, value) {
                            var t=value.severity+"";
                            if(t!=""){
                                $("input[name='severityCheckbox'][value='"+value.severity+"']").prop("checked", true);
                            }
                        });

                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        del:function(planId){
            var obj=new Object();
            obj.planId=planId;
            confirm('确定要删除当前记录？', function () {
                $.ajax({
                    type: "delete",
                    url: baseURL + "safeRest/plan/math/delete",
                     contentType: "application/json",
                    data: JSON.stringify(obj),
                    success: function (r) {
                        if (r.successful) {
                            layer.msg('操作成功');
                            vm.reload();
                        } else {
                            layer.msg(r.msg);
                        }
                    }
                });
            });
        },
        add:function(){
            vm.areaDiv=false;
            $('input[name=allArea]').prop('checked',true);
            layer.open({
                type: 1,
                shade: false,
                title: false, //不显示标题
                anim: 2,
                closeBtn: 0, //隐藏右上角的x号
                shadeClose: true, //开启遮罩关闭
                content: $('#planMathLayer'),
                area: ['1280px', '580px'], //宽高
            });
        },
        selectSome:function(){
            //获取选中的数据添加到refList中
            $("#select-table").find("img").each(function(){
                if($(this).attr("src")!=""){
                    var ebrId=$(this).attr("ebrId");
                    var ebrName=$(this).attr("ebrName");
                    var ebrType=$(this).attr("ebrType");
                    //增加判断右边是否存在
                    console.log(ebrId);
                    console.log(vm.sysPlanMath.refList);
                   var obj= vm.getResoByEbrId(ebrId,vm.sysPlanMath.refList);
                    if(obj==null){
                        vm.sysPlanMath.refList.push({
                            resoCode:ebrId,
                            resoName:ebrName,
                            resoType:ebrType
                        })
                    }
                    $(this).attr("src","");
                }
            });
        },
        selectAll:function(){
            var resoList=vm.resoList;
            for(var i=0;i<resoList.length;i++){
                var ebrInfoList=resoList[i].ebrInfoList;
                var ebrType=resoList[i].ebrType;
                for(var j=0;j<ebrInfoList.length;j++){
                    var ebrId=ebrInfoList[j].ebrId;
                    var ebrName=ebrInfoList[j].ebrName;
                    //增加判断右边是否存在
                    var obj= vm.getResoByEbrId(ebrId,vm.sysPlanMath.refList);
                    if(obj==null){
                        vm.sysPlanMath.refList.push({
                            resoCode:ebrId,
                            resoName:ebrName,
                            resoType:ebrType
                        })
                    }
                }
            }
        },
        cancelSome:function(){
            $("#choose-table").find("img").each(function(){
                if($(this).attr("src")!=""){
                    var ebrId=$(this).attr("resoCode");
                    console.log(ebrId);
                    //移出当前选中数据
                    vm.removeChoseResoByEbrId(ebrId);
                }
                $(this).attr("src","");
            });
        },
        cancelAll:function(){
            vm.sysPlanMath.refList=[];
        },
        removeChoseResoByEbrId:function(ebrId){
            var valList=vm.sysPlanMath.refList;
            for(var i=0;i<valList.length;i++){
                if(ebrId==valList[i].resoCode){
                    vm.sysPlanMath.refList.splice(i,1);
                }
            }
        },
        getResoByEbrId:function(ebrId,valList){
            for(var i=0;i<valList.length;i++){
                if(ebrId==valList[i].resoCode){
                    return valList[i];
                }
            }
            return null;
        },
        changCheck:function(trId){
            if($("#"+trId).attr("class")=="layer-reso-name"){
                if($("#"+trId).children().eq(1).children().eq(0).attr("src")==""){
                    $("#"+trId).children().eq(1).children().eq(0).attr("src","../common/img/selected.png");
                }else{
                    $("#"+trId).children().eq(1).children().eq(0).attr("src","");
                }
            }
        },
        saveOrUpdate:function(){

            vm.sysPlanMath.severityList=[];
            vm.sysPlanMath.areaList=[];
            //事件级别处理
            $.each($("input:checkbox[name=severityCheckbox]:checked"),function (key,box) {
                vm.sysPlanMath.severityList.push(
                    {
                        "severity":$(this).val()
                    }
                )
            });
            
             //区域信息处理
            if(vm.areaDiv){
                $.each($("input:checkbox[name=targetArea]:checked"),function (key,box) {
                    vm.sysPlanMath.areaList.push(
                        {
                            "areaCode":$(this).val(),
                            "areaName":$(this).attr("areaName")
                        }
                    )
                });
            }else{
                //获取全区域后的value值
                var areCode= $("#allArea").val();
                vm.sysPlanMath.areaList.push(
                    {
                        "areaCode":areCode,
                        "areaName":"全区域"
                    }
                );
            }
            var msg = "";
            if (isEmpty(vm.sysPlanMath.planName)) {
                msg += "预案名称不能为空";
            }
            if (vm.sysPlanMath.severityList == null || vm.sysPlanMath.severityList.length < 1) {
                if (!isEmpty(msg)) {
                    msg += "，事件级别不能为空";
                } else {
                    msg += "事件级别不能为空";
                }
            }
            if (isEmpty(vm.sysPlanMath.flowType)) {
                if (!isEmpty(msg)) {
                    msg += "，处理流程不能为空";
                } else {
                    msg += "处理流程不能为空";
                }
            }
            if (vm.sysPlanMath.refList == null || vm.sysPlanMath.refList < 1) {
                if (!isEmpty(msg)) {
                    msg += "，使用资源不能为空";
                } else {
                    msg += "使用资源不能为空";
                }
            }
            if (!isEmpty(msg)) {
                alert(msg);
                return;
            }

            var url;
            var ty;
            if (vm.sysPlanMath.planId == null) {
                url = "safeRest/plan/math/save";
                ty = "POST";
            } else {
                url = "safeRest/plan/math/update";
                ty = "PUT";
            }
            $.ajax({
                type: ty,
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.sysPlanMath),
                success: function (r) {
                    if (r.successful) {
                        alert("操作成功", function () {
                           vm.reload();
                           layer.closeAll();
                            vm.clean();
                        });
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        closeLayer:function(){
            layer.closeAll();
            vm.clean();
        },
        areaToggle:function(){
            if(vm.areaDiv){
                vm.areaDiv=false;
                $('input[name=allArea]').prop('checked',true);
            }else{
                vm.areaDiv=true;
                $('input[name=allArea]').prop('checked',false);
                $('input[name=targetArea]').prop('checked',false);
            }
        },
        clean:function(){
            vm.sysPlanMath={"flowType":"","severity":"",refList:[],severityList:[],areaList:[],eventType:"00000"};
            $('input[type=checkbox]').prop('checked',false);
        },
        reload:function(){
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{"planName":vm.req.planName,"severity":vm.req.severity},
                page:page
            }).trigger("reloadGrid");
        }
    },
    mounted: function(){
        getPermission(this);
    },
    created: function () {
        this.init();
    }
});


function del(planId) {
    vm.del(planId);
}

function upd(planId) {
    vm.clean();
    vm.getPlanMathInfo(planId);
    vm.add();
}


function reload() {
    vm.reload();
}

function getResoName(refs) {
    var resoName="";
    for(var i=0;i<refs.length;i++){
        resoName=resoName+refs[i].resoName+" , ";
    }
    if(resoName.length>0){
        resoName=resoName.substring(0,resoName.length-2);
    }
    return resoName;
}

function  getAreaName(areas) {
    var areaName="";
    for(var i=0;i<areas.length;i++){
        areaName=areaName+areas[i].areaName+" , ";
    }
    if(areaName.length>0){
        areaName=areaName.substring(0,areaName.length-2);
    }
    return areaName;
}

function getSeverityName(severitys) {
    var severityName="";
    for(var i=0;i<severitys.length;i++){
        severityName=severityName+severitys[i].severityName+" , ";
    }
    if(severityName.length>0){
        severityName=severityName.substring(0,severityName.length-2);
    }
    return severityName;
}




