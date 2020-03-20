


$(function(){
    function mainPosition(){
        var contentHeight = document.body.scrollHeight;
        var winHeight = window.innerHeight;//可视窗口高度，不包括浏览器顶部工具栏
        var mainDiv=winHeight-140-40;
        $("#mainIframe").height(mainDiv);
    }
    mainPosition();
    $(window).resize(mainPosition);


    $(".current-position").hover(function(){
        $(".float-menu").animate({height: 'toggle', opacity: 'toggle'}, "slow");
    },function(){
        $(".float-menu").hide();
    });

    vm.getEnum(dictGroupCodes);

    displayTime();
    vm.getMenus();
    vm.getUserInfo();
    initTree("0");
});

var dictGroupCodes = "EBR_TYPE,RES_STATUS_DICT,ORG_TYPE_DICT,RES_LEVEL_DICT";

var vm = new Vue({
    el: '#vueZone',
    data: {
        query: {
            page: 1,
            limit: 1000,
            sidx:"order_num",
            order:"asc",
            pid:null
        },
        menus:[],
        pageEnum:[],
        user:null,
        sysResource:null,
        positionShow:false,
        userSetShow:true,
        ebrInfo:{
            orgType:'',
            platLevel:'',
        },
        sysParam:null,
        localEbrId:null,
        initAreaInfo:null,
        indexTitle:"应急广播平台",
        footerText:" 版权归XX有限公司所属",
        iframeSrc:baseURI+"common/menu.html"

    },
    mounted: function(){
        this.initArea();
     //   this.getPlatfromInfo();
    },
    watch : {
        /*sysResource:{
            handler(sysResource) {
                if(sysResource != null){
                    $(".current-position").show();
                    $(".user-setting").hide();
                    $(".go-home").show();
                }else{
                    $(".current-position").hide();
                    $(".user-setting").show();
                    $(".go-home").hide();
                }
            },
            immediate: true
        }*/
    },
    created: function(){
        this.getStaticParams("index_title_","indexTitle");
        this.getStaticParams("footer_text","footerText");

    },

    methods: {
        getMenus :function () {
            $.ajax({
                type: "get",
                url: baseURL + "safeRest/sysRes/getSysResAndChildren",
                contentType: "application/json",
                // data: JSON.stringify(vm.query),
                data: {
                    page: 1,
                    limit: 1000,
                    sidx:"order_num",
                    order:"asc",
                    type:"menu",
                    pid:null
                },
                success: function (r) {
                    if (Boolean(r.successful)) {
                        vm.menus = r.data;
                    } else {
                        layer.msg("查询菜单为空 ：" + r.msg);
                    }
                }
            });

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
        getStaticParams :function (paramKey,obj) {
            $.ajax({
                type: "get",
                url: baseURL + "openRest/oauth2/getSysParamsInfo",
                contentType: "application/json",
                data: {
                    paramKey: paramKey
                },
                success: function (r) {
                    if (Boolean(r.successful)) {
                        Vue.set(vm,obj,r.data.paramValue);
                    } else {
                        layer.msg("查询系统参数为空 ：" + r.msg);
                    }
                }
            });

        },
        menuClick:function (menuId) {
                var obj = $("#"+menuId);
                var clickSelf = false;
                if(obj.hasClass("first-menu-select")){
                    clickSelf = true;
                }
                $(".float-sub-menu").slideUp(300);
                $(".float-menu-first").removeClass("first-menu-select");
                $(".float-menu-first").find(".menu-tag").find("i").each(function () {
                    if($(this).hasClass("fa-caret-down")){
                        $(this).removeClass("fa-caret-down");
                        $(this).addClass("fa-caret-right");
                    }
                    /* if($(this).hasClass("first-menu-select")){
                         $(this).removeClass("fa-caret-right");
                         $(this).addClass("fa-caret-down")
                     }*/
                });

                if(!clickSelf){
                    if(obj.next(".float-sub-menu").children().length > 0){
                        obj.next(".float-sub-menu").slideDown(300);
                    }
                    obj.addClass("first-menu-select");
                    $(obj).find(".menu-tag").find("i").removeClass("fa-caret-right");
                    $(obj).find(".menu-tag").find("i").addClass("fa-caret-down");
                }else{
                    $(obj).find(".menu-tag").find("i").removeClass("fa-caret-down");
                    $(obj).find(".menu-tag").find("i").addClass("fa-caret-right");
                }


        },
        toMain:function(){
            window.location.href=baseURI+"common/index.html";
        },
        subMenuClick : function(url,menuId){
            if(isEmpty(url)){
                return;
            }
            $.ajax({
                type: "get",
                url: baseURL + "safeRest/sysRes/"+menuId,
                contentType: "application/json",
                success: function (r) {
                    if (Boolean(r.successful)) {
                        vm.sysResource = r.data;
                        vm.positionShow = true;
                        vm.userSetShow = false;
                        $("#mainIframe").attr("src",baseURI+url);
                        $(".float-menu").hide();
                    } else {
                        layer.msg("查询系统资源信息为空 ：" + r.msg);
                    }
                }
            });


        },
        getUserInfo:function () {
            if(isEmpty(userId)){
                return;
            }
            $.ajax({
                type: "get",
                url: baseURL + "safeRest/user/getByUserId",
                contentType: "application/json",
                data: {
                    userId: userId
                },
                success: function (r) {
                    if (Boolean(r.successful)) {
                        vm.user = r.data;
                    } else {
                        layer.msg("查询用户信息为空 ：" + r.msg);
                    }
                }
            });
        },
        logout:function () {
            $.post(baseURL + "openRest/oauth2/logout", function () {
                localStorage.removeItem("userId");
                localStorage.removeItem("token");
                top.location.href =  'login.html';
            });

        },
        getPlatfromInfo :function(){
            $.ajax({
                type: "get",
                url: baseURL + "openRest/oauth2/getSysParamsInfo",
                contentType: "application/json",
                data: {
                    paramKey: "ebr_platform_id_"
                },
                success: function (r) {
                    if (Boolean(r.successful)) {
                        if(isEmpty(r.data) || isEmpty(r.data.paramValue)){
                            console.log("=====localEbrId is empty=====");
                            return;
                        }
                        var ebrId = r.data.paramValue;
                        vm.sysParam = r.data;
                        $.ajax({
                            type: "get",
                            url: baseURL+"safeRest/ebrPlatform/"+ebrId,
                            contentType: "application/json",
                            success: function (r) {
                                if (r.successful ) {
                                    if(isEmpty(r.data)){
                                        layer.open({
                                            type: 1,
                                            title: false, //不显示标题
                                            closeBtn: 0, //隐藏右上角的x号
                                            skin: 'layui-layer-rim', //加上边框
                                            area: ['1100px', '600px'], //宽高
                                            content: $('#ebrLayer')
                                        });
                                    }else{
                                        vm.ebrInfo = r.data;
                                    }
                                } else {
                                    layer.msg(r.msg);
                                }
                            }
                        });
                    } else {
                        layer.msg("查询系统参数为空 ：" + r.msg);
                    }
                }
            });

        },
        savePlatformInfo: function(){
            if(isEmpty(vm.ebrInfo)){
                return;
            }
            vm.ebrInfo.psType = '01';
            vm.ebrInfo.resType = '0101';
            vm.ebrInfo.subResType = '01';
            if(!isEmpty(vm.initAreaInfo)){
                vm.ebrInfo.areaCode = vm.initAreaInfo.areaCode;
            }
            $.ajax({
                type: "post",
                async:false,
                url: baseURL+"safeRest/ebrPlatform/save",
                contentType: "application/json",
                data: JSON.stringify(vm.ebrInfo),
                success: function (r) {
                    if (r.successful ) {
                        layer.closeAll();
                        updateSysParams(r.data.psEbrId);
                    }
                }
            });
        },
        initArea:function(){

            //首先判断区域表中是否有数据，没有数据时弹出全国区域Tree
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/area/rootArea",
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        var areaInfo =r.data;
                        if(isEmpty(areaInfo)){
                            layer.open({
                                type: 1,
                                title: false, //不显示标题
                                closeBtn: 0, //隐藏右上角的x号
                                skin: 'layui-layer-rim', //加上边框
                                area: ['420px', '600px'], //宽高
                                content: $('#initAreaLayer')
                            });
                        }else{
                            vm.initAreaInfo = areaInfo;
                            vm.getPlatfromInfo()
                        }
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        }
    }

});

function headerStyleShow() {
    var sysResource = vm.sysResource;
    if(sysResource != null){
        $(".current-position").show();
        $(".user-setting").hide();
        $(".go-home").show();
    }else{
        $(".current-position").hide();
        $(".user-setting").show();
        $(".go-home").hide();
    }
}

function displayTime() {
    var elt = document.getElementById("clock");
    var now = new Date(); // 得到当前时间
    var year = now.getFullYear().toString();
    var month = addZore((now.getMonth()+1).toString());
    var day = addZore(now.getDate().toString());
    var hour = addZore(now.getHours().toString());
    var min = addZore(now.getMinutes().toString());
    var sec = addZore(now.getSeconds().toString());

    var timeStr = year+"/"+month+"/"+day+" "+hour+":"+min+":"+sec;
    elt.innerHTML = timeStr;
    setTimeout(displayTime,1000);
}

function addZore(str){
    if(str.length < 2){
        str = "0"+str;
    }
    return str
}

function subMenuClick(url,menuId){
    vm.subMenuClick(url,menuId);
}



//全国区域树===============================

var areaTreeUrl = baseURL+"safeRest/area/getNextReginCodeByParentCode";
function getAreaTreeNodesUrl(treeId, treeNode) {
    var param = "parentAreaCode=" + treeNode.areaCode;
    return areaTreeUrl+"?" + param;
}

function filter(treeId, parentNode, childNodes) {
    return changData(childNodes.data);
}

function changData(childNodes){
    var nodeDatas = childNodes;
        for(var i=0;i<nodeDatas.length;i++ ){
            var node = nodeDatas[i];
            if(node.areaLevel!=5){
                node.parent = true;
            }
    }
    return nodeDatas;
}



var setting = {
    async : {
        enable : true,//设置 zTree 是否开启异步加载模式
        url:getAreaTreeNodesUrl,
        type:"get",
        dataFilter: filter
    },
    data : {
        simpleData : {
            enable : true,//是否采用简单数据模式
            idKey : "areaCode",//树节点ID名称
            pIdKey : "parentAreaCode",//父节点ID名称
            rootPId : null//根节点ID
        },
        key : {
            name: "areaName",
            isParent:'parent',
            title : "areaName",
            checked: "checked"
        }
    },
    callback : {

    },
    view:{
        fontCss : {'color':'#00ffff','font-size':"16px",'height':"30px",'line-height':'30px'}
    }
};

var ztree;
function initTree(parentAreaCode) {
    $.ajax({
        url : areaTreeUrl,
        type : "get",
        contentType: "application/json",
        data:{parentAreaCode:parentAreaCode},
        success : function(r) {
            var nodeDatas = changData(r.data);
            ztree= $.fn.zTree.init($("#areaTree"), setting, nodeDatas);//初始化树节点时，添加同步获取的数据
            var nodeList = ztree.getNodes();
            if(nodeList.length>0){
                ztree.expandNode(nodeList[0], true);
            }
        }
    });
}

function sureInitArea(){
    var nodes = ztree.getSelectedNodes();
    if(nodes.length==0){
        layer.msg("请选择需要初始化的区域");
        return;
    }
    var areaNode=nodes[0];
    vm.initAreaInfo = areaNode;
        confirm('确定初始化【'+areaNode.areaName+"】区域信息？", function () {
        $.ajax({
            type: "get",
            url: baseURL + "safeRest/area/sureInitArea?areaCode="+areaNode.areaCode,
            contentType: "application/json",
            success: function (r) {
                if (r.successful) {
                    layer.msg('操作成功');
                    layer.closeAll();
                    vm.getPlatfromInfo();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    });


}

function updateSysParams(erbId) {
    var data = {
            id:vm.sysParam.id,
            paramValue:erbId
        };
    $.ajax({
        type: "put",
        url: baseURL + "safeRest/sys/param/update",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (r) {
            if (r.successful) {
                layer.msg('操作成功');
            } else {
                layer.msg(r.msg);
            }
        }
    });
}




