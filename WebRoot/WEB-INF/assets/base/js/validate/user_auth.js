$(document).ready(function(){
	$('#authForm').bootstrapValidator({
	 	message: 'This value is not valid',
        feedbackIcons: {
        	valid: '',
            invalid: '',
            validating: 'glyphicon glyphicon-refresh'
    	},
    	 fields: {
    		 idCard: {
                validators: {
                    notEmpty: {
                        message: '身份证号未填写！'
                    },
                    regexp: {
                        regexp: /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/,
                        message: '请输入正确身份证号！'
                    }
                }
            },
            comfirmFile: {
                validators: {
                    notEmpty: {
                        message: '验证照片未上传！'
                    }
                }
            }
	 	}
	 });

});