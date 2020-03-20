
$(function () {
    document.onkeydown = function(e){
        if(!e){
            e = window.event;
        }
        if((e.keyCode || e.which) == 13){
            vm.toLogin();
        }
    }
});
var clientType = "web";
var vm = new Vue({
    el: '#vueZone',
    data: {
        oauthData: {
            password: null,
            username: null,
            clientType:clientType
        },
        indexTitle:"应急广播平台",
        captcha:null,
        captchaSrc:baseURL+"openRest/oauth2/captcha.jpg?t="+ new Date().getMilliseconds()
    },
    mounted: function(){


    },
    watch : {

    },
    created:function(){
    this.getStaticParams("index_title_")
    },
    methods: {
        toLogin:function(){
            if(isEmpty(vm.captcha)){
                afterLoginFail("验证码不能为空！");
                return;
            }
            $.ajax({
                type: "get",
                url: baseURL + "openRest/oauth2/verifyCaptcha",
                contentType: "application/json",
                data: {captcha:vm.captcha},
                success: function (r) {
                    if (r.successful) {
                       vm.doLogin();
                    } else {
                        afterLoginFail(r.msg);
                    }
                }
            });
        },
        getStaticParams :function (paramKey) {
            $.ajax({
                type: "get",
                url: baseURL + "openRest/oauth2/getSysParamsInfo",
                contentType: "application/json",
                // data: JSON.stringify(vm.query),
                data: {
                    paramKey: paramKey
                },
                success: function (r) {
                    if (Boolean(r.successful)) {
                        vm.indexTitle = r.data.paramValue;
                    } else {
                        layer.msg("查询系统参数为空 ：" + r.msg);
                    }
                }
            });

        },
        doLogin :function () {
            if(isEmpty(vm.oauthData.username)){
                afterLoginFail("用户名不能为空！");
                return;
            }
            if(isEmpty(vm.oauthData.password)){
               // alert("用户密码不能为空！");
                afterLoginFail("用户密码不能为空！");
                return;
            }
            $.ajax({
                type: "post",
                url: baseURL + "openRest/oauth2/authorize",
                contentType: "application/json",
                data: JSON.stringify(vm.oauthData),
                success: function (r) {
                    if (r.successful) {
                        localStorage.setItem("userId",r.data.userId);
                        localStorage.setItem("accessToken",r.data.accessToken);
                        localStorage.setItem("token",r.data.accessToken);
                        localStorage.setItem("refreshToken",r.data.refreshToken);
                        window.location.href = baseURI+"common/index.html";
                    } else {
                        afterLoginFail(r.msg);
                    }
                }
            });

        },
        refreshCaptcha:function () {
            vm.captchaSrc = baseURL+"openRest/oauth2/captcha.jpg?t="+ new Date().getMilliseconds();
        }
    },



});

function afterLoginFail(msg) {
    $("#error_msg_div").show();
    $("#errorMsg").html(msg);
    vm.refreshCaptcha();
}