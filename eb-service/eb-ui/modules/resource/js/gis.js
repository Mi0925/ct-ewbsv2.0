
var map;
var mass;

var style = [
    {
    url: 'https://a.amap.com/jsapi_demos/static/images/mass0.png',
    anchor: new AMap.Pixel(6, 6),
    size: new AMap.Size(11, 11)
    },
    {
        url: 'img/zd/zd1.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/zd/zd2.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/zd/zd3.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/zd/zd4.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/zd/zd5.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/pt/pt1.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/pt/pt2.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/pt/pt3.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/pt/pt4.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/pt/pt5.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/bc/bc1.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/bc/bc2.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/bc/bc3.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/bc/bc4.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    },
    {
        url: 'img/bc/bc5.png',
        anchor: new AMap.Pixel(4, 4),
        size: new AMap.Size(14, 19)
    }
];

$(function () {
    setMapHeight();
    initMap();
});

$(window).resize(function() {
    setMapHeight();
});
function setMapHeight(){
    $("#container").css("height",$(this).height()-105);
}

function initMap() {
    map = new AMap.Map('container', {
        zoom: 4,
        center: [102.342785, 35.312316],
        mapStyle: 'amap://styles/darkblue'
    });

    //根据配置参数，展示地图位置以及缩放级别
    setZoomAndCenter();
}

function setZoomAndCenter(){
    $.ajax({
        type: "get",
        url: baseURL+"safeRest/ebrView/gis/initParame",
        contentType: "application/json",
        success: function (r) {
            if (r.successful ) {
                map.setZoomAndCenter(r.data.zoom, r.data.center);
            } else {
                layer.msg(r.msg);
            }
        }
    });
}



var vm = new Vue({
    el: '#rrapp',
    data: {
        req:{
            status:"",
            relatedPsEbrId:"",
            ebrType:""
        },
        resDatas:null,
        ebrStatusCount:[],
        ebrTypeList:[],
        ebrStatuList:[],
        platformList:[],
        enumKey:{
            "ebrType":"EBR_TYPE",
            "erbStatu":"RES_STATUS_DICT"
        },
        showEbrStateCount:false
    },
    mounted: function(){

    },
    created:function(){
        this.getData();
        this.initSearchData();
    },
    methods:{
        reload : function(){
            vm.getData();
            setZoomAndCenter();
        },
        initDataToMap:function(data){
            if(mass!=undefined) mass.clear();//清除原有海量点
            if(isEmpty(data)){
                return;
            }
            mass = new AMap.MassMarks(data, {
                opacity: 0.8,
                zIndex: 111,
                cursor: 'pointer',
                style: style
            });
            var marker = new AMap.Marker({content: ' ', map: map});
            mass.on('mouseover', function (e) {
                marker.setPosition(e.data.lnglat);
                marker.setLabel({content: e.data.name})
                marker.show( );
            }).on('mouseout',function (e) {
                marker.hide( );
            });
            mass.setMap(map);
        },
        initDataStatusToMap:function(){
            var url=baseURL+"safeRest/ebrView/gis/getEbrGisStatusCount?1=1";
            if(this.req.status!=""){
                url+="&status="+this.req.status;
            }

            if(this.req.ebrType!=""){
                url+="&ebrType="+this.req.ebrType;
            }

            if(this.req.relatedPsEbrId!=""){
                url+="&relatedPsEbrId="+this.req.relatedPsEbrId;
            }

            $.ajax({
                type: "get",
                url: url,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.ebrStatusCount=r.data;
                        vm.showEbrStateCount=true;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });


        },
        getData:function(){
            var url=baseURL+"safeRest/ebrView/gis/page?limit=100000";
            if(this.req.status!=""){
                url+="&status="+this.req.status;
            }

            if(this.req.ebrType!=""){
                url+="&ebrType="+this.req.ebrType;
            }

            if(this.req.relatedPsEbrId!=""){
                url+="&relatedPsEbrId="+this.req.relatedPsEbrId;
            }

            $.ajax({
                type: "get",
                url: url,
                contentType: "application/json",
                success: function (r) {
                    if (r.successful ) {
                        vm.initDataToMap(r.data);
                        vm.initDataStatusToMap();
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        initSearchData:function(){
            this.getEbrType();
            this.getEbrStatu();
            this.getPlatformList();
        },
        getEbrStatu:function(){
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/dict/getByGroupCode",
                contentType: "application/json",
                data:{dictGroupCode:this.enumKey.erbStatu},
                success: function (r) {
                    if (r.successful ) {
                        vm.ebrStatuList = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getEbrType:function(){
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/dict/getByGroupCode",
                contentType: "application/json",
                data:{dictGroupCode:this.enumKey.ebrType},
                success: function (r) {
                    if (r.successful ) {
                        vm.ebrTypeList = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        },
        getPlatformList:function(){
            $.ajax({
                type: "get",
                url: baseURL+"safeRest/ebrPlatform/page?limit=100&queryType=2",
                contentType: "application/json",
                data:{dictGroupCode:this.enumKey.ebrType},
                success: function (r) {
                    if (r.successful ) {
                        vm.platformList = r.data;
                    } else {
                        layer.msg(r.msg);
                    }
                }
            });
        }
    }
});






