
$(function () {
    vm.getEnum(dictGroupCodes);
});

var dictGroupCodes = "EBR_TYPE,SEVERITY_DICT,MSG_LANG_DICT,EVENT_TYPE_DICT,MSG_TYPE_DICT,AUXILIARY_TYPE_DICT,plan_math_flow,AUDIT_STATUS_DICT";

var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{
        },
        pageEnum:[],
        schemeId : null,
        areaNames: "",
        schemeInfo:{},
        ebmInfo:{},
        areaNamesBtnShow :false,
        msgDescBtnShow:false,
        flowStyles:[],
        resoList :[],
        refEbrList:[],
        matchedResList:[],
        planMatched: false,
        permissions: {
            "core:scheme:audit": false
        }

    },
    mounted: function(){
        
    },
    created:function(){
        console.log(this.permissions)
        this.ebmId = getQueryString("ebmId");
        getPermission(this);
        this.getEbmInfoAll(this.ebmId);
        this.setFlowWidth();
        this.getResoList();
    },
    filters: {
        //字符串过长，截取掉。
        filterFun: function (value) {
            if(value&& value.length > 30) {
                value= value.substring(0,27)+ '...';
            }
            return value;
        },
        //获取枚举名称
        getEnumName:function (value,dictCode) {
            if(isEmpty(vm.pageEnum) || isEmpty(dictCode)){
                return value;
            }
            return matchAndGetDictValue(vm.pageEnum,value,dictCode);
        },
        filterAreaNamesFun: function (value) {
            if(value&& value.length > 20) {
                value= value.substring(0,17)+ '...';
                vm.areaNamesBtnShow = true;
            }
            return value;
        },
        filterMsgDescFun: function (value) {
            if(value&& value.length > 20) {
                value= value.substring(0,17)+ '...';
                vm.msgDescBtnShow = true;
            }
            return value;
        },
        

    },
    methods:{

        getEnum: function (dictGroupCode) {
            if(isEmpty(dictGroupCode)){
                return
            }
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
        getEbmInfoAll:function (ebmId) {
            if(isEmpty(ebmId)){
                return;
            }
            $.ajax({
                type: "get",
               // async:false,
                url: baseURL+"safeRest/ebm/getEbmInfoAll",
                contentType: "application/json",
                data:{ebmId:ebmId},
                success: function (r) {
                    if (r.successful ) {
                        initVueData(r.data);

                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getArea :function (areaList) {
            for(var i=0;i<areaList.length;i++){
                if(this.areaNames==''){
                    this.areaNames = areaList[i].areaName;
                }else{
                    this.areaNames += ","+ areaList[i].areaName;
                }
            }
        },
        //初始化流程导航的流程节点样式
        setFlowStyle : function (ebmInfo) {
            if(isEmpty(ebmInfo)){
                return;
            }



            if(isEmpty(ebmInfo.schemeInfoAll) || isEmpty(ebmInfo.schemeInfoAll.sysPlanMatchInfo)){
                //没有生成方案或者没有匹配上预案
                vm.flowStyles = getMatchFlowStyleArr();
                vm.flowStepChange(vm.flowStyles[0].flowState);
            }else{
                var dispatchFlowInfo = ebmInfo.dispatchFlowInfo;    //方案当前流程节点
                var currFlowState = dispatchFlowInfo.flowState;
                var flowStage = dispatchFlowInfo.flowStage;

                var sysPlanMatchInfo = ebmInfo.schemeInfoAll.sysPlanMatchInfo;         //匹配的预案信息
                var flowType = sysPlanMatchInfo.flowType;                  //流程类型
                vm.flowStyles = getMatchFlowStyleArr(flowType,currFlowState);
                if(flowStage != 2 && flowStage != 6 ){
                    //如果方案流程节点不在预警响应的阶段，默认选择点击最后一个流程
                    var flowState = "";
                    if(vm.flowStyles.length > 0 ){
                        for(var i=0;i< vm.flowStyles.length;i++){
                            var flowStyle = vm.flowStyles[i];
                            if(flowStyle.flowClass != 'flow-btn-unSelect'){
                                flowState = flowStyle.flowState;
                            }
                        }
                    }
                    if(!isEmpty(flowState)){
                        vm.flowStepChange(flowState);
                    }
                    return;
                }
                vm.flowStepChange(currFlowState);
            }

        },
        //流程节点按钮点击事件
        flowStepChange:function (flowState) {
            for(var i=0;i< vm.flowStyles.length;i++){
                var flowStyle = vm.flowStyles[i];
                if(flowStyle.flowState == flowState && flowStyle.flowClass == "flow-btn-unSelect"){
                    return;
                }
            }
            var dispatchFlowInfo = vm.ebmInfo.dispatchFlowInfo;    //方案当前流程节点
            var currFlowState = dispatchFlowInfo.flowState;
            var sysPlanMatchInfo = vm.ebmInfo.schemeInfoAll.sysPlanMatchInfo;         //匹配的预案信息
            if(!isEmpty(sysPlanMatchInfo)){
                var flowType = sysPlanMatchInfo.flowType;
                vm.flowStyles = getMatchFlowStyleArr(flowType,currFlowState);
            }else{
                vm.flowStyles = getMatchFlowStyleArr('',currFlowState);
            }

            var flowStyles = vm.flowStyles;

            for(var i=0;i<flowStyles.length;i++){
                var flowStyle = flowStyles[i];
                if(flowStyle.flowState == flowState){
                    //当前点击的流程节点
                    if(flowStyle.flowClass != "flow-btn-unSelect"){
                        //点击的流程节点是流程类型中包含的节点
                        flowStyle.navArrClass = "";
                        if(currFlowState == flowStyle.flowState){
                            flowStyle.flowClass = "flow-btn-doing-sel";
                        }else{
                            flowStyle.flowClass = "flow-btn-select"
                        }
                    }
                }else{
                        flowStyle.navArrClass = "nav-arr-hide";
                }
            }
            $(".flow-detail-content").hide();
            $("#flow-detail-"+flowState).show();
        },
        getResoList:function () {
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
        },
        doAudit : function () {
            var schemeId = vm.ebmInfo.schemeInfoAll.schemeId;
           // var auditResult = vm.ebmInfo.schemeInfoAll.auditResult;
            var auditResult = $("#auditResult").val();
            var auditOpinion = $("#auditOpinion").val();
         //   var auditOpinion = vm.ebmInfo.schemeInfoAll.auditOpinion;
            if(isEmpty(schemeId)){
                return
            }
            if(isEmpty(auditResult)){
                return;
            }
            if(auditResult =='0'){
                alert("请选择审核结果！");
                return;
            }
            var data = {
                schemeId:schemeId,
                auditResult:auditResult,
                auditOpinion:auditOpinion,
                refEbrList:vm.refEbrList
            };
            console.log(data);
            $.ajax({
                type: "put",
                url: baseURL+"safeRest/scheme/audit",
                contentType: "application/json",
                data:JSON.stringify(data),
                success: function (r) {
                    if (r.successful ) {
                        vm.ebmInfo.schemeInfoAll.auditResult = auditResult;
                        vm.ebmInfo.schemeInfoAll.auditOpinion = auditOpinion;
                        layer.msg("审核成功！");
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });

        },
        setFlowWidth:function(){
            var len=getFlowStyleArr().length;
            var unitOne=100/(3*len-1);
            this.percentThree=unitOne*3;
            this.percentTwo=unitOne*2;
        },
        reset : function () {
            $("#auditResult").val(vm.ebmInfo.schemeInfoAll.auditResult);
            $("#auditOpinion").val(vm.ebmInfo.schemeInfoAll.auditOpinion);

        },
        changCheck:function(trId){
            if($("#scheme_tiaozheng").find("#"+trId).attr("class")=="layer-reso-name"){
                if($("#scheme_tiaozheng").find("#"+trId).children().eq(1).children().eq(0).attr("src")==""){
                	$("#scheme_tiaozheng").find("#"+trId).children().eq(1).children().eq(0).attr("src","../common/img/selected.png");
                }else{
                	$("#scheme_tiaozheng").find("#"+trId).children().eq(1).children().eq(0).attr("src","");
                }
            }
        },
        changCheck1:function(trId){
            if($("#scheme_optima").find("#"+trId).attr("class")=="layer-reso-name"){
                if($("#scheme_optima").find("#"+trId).children().eq(1).children().eq(0).attr("src")==""){
                	$("#scheme_optima").find("#"+trId).children().eq(1).children().eq(0).attr("src","../common/img/selected.png");
                }else{
                	$("#scheme_optima").find("#"+trId).children().eq(1).children().eq(0).attr("src","");
                }
            }
        },
        saveOrUpdate:function(){
        	alert("操作成功");
            /*var url;
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
            });*/
        },
        selectSome:function(){
            //获取选中的数据添加到refList中
            $("table.select-table-ebrlist").find("img").each(function(){
                if($(this).attr("src")!=""){
                    var ebrId=$(this).attr("ebrId");
                    var ebrName=$(this).attr("ebrName");
                    var ebrType=$(this).attr("ebrType");
                    //增加判断右边是否存在
                    console.log(ebrId);
                    console.log(vm.refEbrList);
                   var obj= vm.getResoByEbrId(ebrId,vm.refEbrList);
                    if(obj==null){
                        vm.refEbrList.push({
                        	ebrId:ebrId,
                        	ebrName:ebrName,
                        	ebrType:ebrType
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
                    var obj= vm.getResoByEbrId(ebrId,vm.refEbrList);
                    if(obj==null){
                        vm.refEbrList.push({
                        	ebrId:ebrId,
                        	ebrName:ebrName,
                        	ebrType:ebrType
                        })
                    }
                }
            }
        },
        cancelSome:function(){
            $("table.choose-table-ebrlist").find("img").each(function(){
                if($(this).attr("src")!=""){
                    var ebrId=$(this).attr("ebrId");
                    console.log(ebrId);
                    //移出当前选中数据
                    vm.removeChoseResoByEbrId(ebrId);
                }
                $(this).attr("src","");
            });
        },
        cancelAll:function(){
            vm.refEbrList=[];
        },
        removeChoseResoByEbrId:function(ebrId){
            var valList=vm.refEbrList;
            for(var i=0;i<valList.length;i++){
                if(ebrId==valList[i].ebrId){
                    vm.refEbrList.splice(i,1);
                }
            }
        },
        getResoByEbrId:function(ebrId,valList){
            for(var i=0;i<valList.length;i++){
                if(ebrId==valList[i].ebrId){
                    return valList[i];
                }
            }
            return null;
        }

    }
});


function initVueData(data) {
    vm.ebmInfo = data;
    vm.schemeInfo = data.schemeInfoAll;
    if(data.schemeInfoAll.schemeEbrInfoList){
       vm.refEbrList = data.schemeInfoAll.schemeEbrInfoList;
    }
    //设置覆盖区域
    if(!isEmpty(vm.ebmInfo) && !isEmpty(vm.ebmInfo.schemeInfoAll)
        && !isEmpty(vm.ebmInfo.schemeInfoAll.sysPlanMatchInfo)
        && !isEmpty(vm.ebmInfo.schemeInfoAll.sysPlanMatchInfo.areaList)){
        var areaList = vm.ebmInfo.schemeInfoAll.sysPlanMatchInfo.areaList;
        vm.getArea(areaList);
    }

    //设置流程导航样式
    vm.setFlowStyle(data);

    //预案匹配，已选资源
    if(!isEmpty(vm.ebmInfo.schemeInfoAll.schemeEbrInfoList)){
        vm.matchedResList = vm.ebmInfo.schemeInfoAll.schemeEbrInfoList;
    }

    //判断预案是否匹配成功
    if(!isEmpty(vm.ebmInfo) && !isEmpty(vm.ebmInfo.schemeInfoAll)
        && !isEmpty(vm.ebmInfo.schemeInfoAll.sysPlanMatchInfo)){
        vm.planMatched = true;
    }
}


function back(){
    window.location.href = document.referrer;
}

function areaNamesBtnClick(obj) {
    $(obj).parent().find("span").html(vm.areaNames);
    $("#areaNames-btn").hide();
}

function msgDescBtnClick(obj) {
    if(vm.ebmInfo != null){
        var desc = vm.ebmInfo.msgDesc;
        $(obj).parent().find("span").html(desc);
        $("#msgDesc-btn").hide();
    }
}

/**
 * 返回所有流程相关信息
 * @returns {Array}
 */
function getFlowStyleArr(){
    var flowStyleArr = [];
    var arr1 = {
        id:"1",
        flowStage:"6",
        flowState:"61",
        flowStageName:"预案匹配",
        flowStateName:"预案匹配",
        navArrClass: "",
        flowArrowStyle: "",
        flowClass : ""
    };
    flowStyleArr.push(arr1);
    var arr2 = {
        id:"2",
        flowStage:"2",
        flowState:"21",
        flowStageName:"预警响应",
        flowStateName:"方案生成",
        navArrClass: "",
        flowArrowStyle: "",
        flowClass : ""
    };
    flowStyleArr.push(arr2);
    var arr3 = {
        id:"3",
        flowStage:"2",
        flowState:"22",
        flowStageName:"预警响应",
        flowStateName:"方案优化",
        navArrClass: "",
        flowArrowStyle: "",
        flowClass : ""
    };
    flowStyleArr.push(arr3);
    var arr4 = {
        id:"4",
        flowStage:"2",
        flowState:"23",
        flowStageName:"预警响应",
        flowStateName:"方案调整",
        navArrClass: "",
        flowArrowStyle: "",
        flowClass : ""
    };
    flowStyleArr.push(arr4);
    var arr5 = {
        id:"5",
        flowStage:"2",
        flowState:"24",
        flowStageName:"预警响应",
        flowStateName:"方案审核",
        navArrClass: "",
        flowArrowStyle: "",
        flowClass : ""
    };
    flowStyleArr.push(arr5);
    return flowStyleArr;
}

function getMatchFlowStyleArr(flowType, currFlowState) {

    var flowStyleArr = getFlowStyleArr();  //所有流程节点

    var reserveFlow =[];
    //没有匹配到预案
    if(isEmpty(flowType)){
        reserveFlow = ['61','21','22','23','24']
    }
    //匹配到预案，流程类型为1
    if(flowType == 1){
        reserveFlow = ['61']
    }else if(flowType == 2){
        reserveFlow = ['61','24']
    }else if(flowType == 3){
    	reserveFlow = ['61','23','24']
    }else if(flowType == 4){
    	reserveFlow = ['61','21','23','24']
    }else if(flowType == 5){
    	reserveFlow = ['61','21','22','23','24']
    }
    for(var i=0;i<flowStyleArr.length;i++){
        var flowStyle = flowStyleArr[i];
        flowStyle.navArrClass = "nav-arr-hide";    //隐藏所有向上的箭头
        if(reserveFlow.indexOf(flowStyle.flowState) > -1){
            if(currFlowState == flowStyle.flowState){
                //此为方案当前的流程节点
                flowStyle.flowClass = "flow-btn-current-sel";
                flowStyle.navArrClass = "";    //当前流程的箭头显示处理
            }else{
                //该流程类型包含的流程节点，但未选中，也不是当前的流程节点
                flowStyle.flowClass = "flow-btn-current" ;
            }
        }else{
            //该流程类型不包含的流程节点，置灰色
            flowStyle.flowClass = "flow-btn-unSelect";
        }
    }

    return flowStyleArr;

}