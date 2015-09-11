$(document).ready(function(){
	$('#loginForm').bootstrapValidator({
	 	message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
    	},
    	 fields: {
    		 mobile: {
                validators: {
                    notEmpty: {
                        message: '手机号未填写'
                    },
                    stringLength: {
                        max: 11,
                        min: 11,
                        message: '请输入正确手机号！'
                	}
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: '密码未填写'
                    }
                }
            }
	 	}
	 });
});