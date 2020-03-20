    $('#login-form').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            password:{
                validators: {
                    notEmpty: {
                        message: '用户密码不能为空！'
                    }
                }
            },
            username:{
                validators: {
                    notEmpty: {
                        message: '用户名不能为空！'
                    },
                    stringLength: {
                        min: 6,
                        max: 30,
                        message: '用户名必须为3~30长度的字符串'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '用户名只能是数字，字母和下划线组合'
                    }
                }
            }
        }
    });
