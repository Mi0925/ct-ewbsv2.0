var h ;
var sh;
$(function(){
    vm.getMenus();

    /* 计算宽高，自动适应屏幕大小*/
    var winHeight = window.innerHeight;
     h = (winHeight/3)- 38 ;
    var img_width = h*2;
    vm.img_height = "height:"+h+"px;width:"+img_width+"px;";
    var panel_width = (img_width+38)*3 + 10;
    vm.menu_panel_width = "width:"+panel_width+"px;";
    var shs = h-47;
    var subMenus_width = (img_width-62);
    vm.subMenus_style = "height:"+shs+"px;width:"+subMenus_width+"px;";
    var sw = (subMenus_width/3)-10;
  //  sh = sw/2.8+1;
    sh = (shs-30)/3;
    vm.subMenu_style = "height:"+sh+"px;line-height: "+sh+"px;width:"+sw+"px;";


});


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
        img_height:null,
        subMenu_style:null,
        subMenus_style:null,
        menu_panel_width:null,
    },
    mounted: function(){
    },
    watch : {

    },
    created: function () {
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
        subMenuClick : function(url,menuId){
            if(isEmpty(url)){
                return;
            }
            var resources = ['2c7b6378a3014e4aa2d91e628d51f4f1','2c7b6378a3014e4aa2d91e628d51f4b0','2c7b6378a3014e4aa2d91e628d51f489','2c7b6378a3014e4aa2d91e628d51f409','2c7b6378a3014e4aa2d91e628d51f4b1'];
            if($.inArray(menuId, resources)!=-1){
            	window.open(url);
            }else{
            	parent.subMenuClick(url,menuId);
            }
           // $("#mainIframe",window.parent.document).attr("src",baseURI+url);
        },
    }

});