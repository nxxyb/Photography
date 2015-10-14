$(document).ready(function(){
	$('#projectCreateForm').bootstrapValidator({
	 	message: 'This value is not valid',
        feedbackIcons: {
        	valid: '',
            invalid: '',
            validating: 'glyphicon glyphicon-refresh'
    	},
    	 fields: {
    		 name: {
                validators: {
                    notEmpty: {
                        message: '活动标题未填写！'
                    }
                }
            },
            photoPics: {
                validators: {
                    notEmpty: {
                        message: '滚动图片未上传！'
                    }
                }
            },
            place: {
                validators: {
                    notEmpty: {
                        message: '出发地未填写！'
                    }
                }
            },
            venuePlace: {
                validators: {
                    notEmpty: {
                        message: '集合地未填写！'
                    }
                }
            },
            destinationPlace: {
                validators: {
                    notEmpty: {
                        message: '目的地未填写！'
                    }
                }
            },
            contact: {
                validators: {
                    notEmpty: {
                        message: '联系方式未填写！'
                    }
                }
            },
            "trips[0].title": {
                validators: {
                    notEmpty: {
                        message: '行程标题未填写！'
                    }
                }
            },
            "trips[0].des": {
                validators: {
                    notEmpty: {
                        message: '行程简介未填写！'
                    }
                }
            },
            cost: {
                validators: {
                    notEmpty: {
                        message: '费用未填写！'
                    }
                }
            },
            feeDes: {
                validators: {
                    notEmpty: {
                        message: '费用说明未填写！'
                    }
                }
            }
	 	}
	 });

});