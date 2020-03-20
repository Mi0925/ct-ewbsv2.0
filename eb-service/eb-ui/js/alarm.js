$(function(){
	var alarm_audio = document.getElementById('alarm_audio'); 
	$('#alarm_div').everyTime('15s','A',function(){
		var type = $("#alarm_div").attr("data-type");
   	    var url = baseURL+'/safeRest/ebrView/checkEbrAlarmVal/'+type;
        $.ajax({
            type: "GET",
            url: url,
            success: function(data) {
                if(data.successful){
	               	 $("#alarm_div").removeClass();
	               	 $("#alarm_div").addClass("red-alarm");
	               	 $('#alarm_div').everyTime('400ms','B',function(){
	               		$("#alarm_div").fadeOut(200).fadeIn(200);
	               	 });
	               	alarm_audio.play();
                }else{
	               	 $("#alarm_div").removeClass();
	               	 $("#alarm_div").addClass("green-alarm");
	               	 $('#alarm_div').stopTime ('B');
	               	alarm_audio.pause();
                }
            }
        });
	});
	
})