var res = res || {};
res.audio = res.audio || {};

/*
 * Audio
 */
$(document).ready(function(){
	res.audio.volume(res.storage.getItem("Volume")? res.storage.getItem("Volume"):res.config.touchToneVolume);
});

(function(){

	var scales = { C:261.626, Cs:277.183, D:293.665, Ds:311.127, E:329.628, F:349.228, Fs:369.994, G:391.995, Gs:415.305, A:440.000, A:466.164, B:493.883 };
	var octave = 3;
	var duration = 50;	//msec

	var audioBuffer = {};
	var context = undefined;

	res.audio.volume = function(volume){		
		var i, cycle, data;

		if (typeof webkitAudioContext != "undefined") {
			if(!context){
			    context = new webkitAudioContext();
			}
			var sampleRate = context.sampleRate;	// 48000? dependent on platform
			
			audioBuffer["click"] = context.createBuffer(1, sampleRate*duration/1000, sampleRate);
			data = audioBuffer["click"].getChannelData(0);
			for (i = 0; i < data.length; i++){
				cycle = sampleRate/(scales["C"]*Math.pow(2, octave));
				data[i] = Math.sin( 360/180 * Math.PI * (i%cycle)/cycle) * volume;
			}
			audioBuffer["tap"] = context.createBuffer(1, sampleRate*duration/1000, sampleRate);
			data = audioBuffer["tap"].getChannelData(0);
			for (i = 0; i < data.length; i++){
				cycle = sampleRate/(scales["D"]*Math.pow(2, octave));
				data[i] = Math.sin( 360/180 * Math.PI * (i%cycle)/cycle) * volume;
			}		
			audioBuffer["touch"] = context.createBuffer(1, sampleRate*duration/1000, sampleRate);
			data = audioBuffer["touch"].getChannelData(0);
			for (i = 0; i < data.length; i++){
				cycle = sampleRate/(scales["E"]*Math.pow(2, octave));
				data[i] = Math.sin( 360/180 * Math.PI * (i%cycle)/cycle) * volume;
			}	
		}else{
			if (res.console){
				res.console("res.audio.js: webkitAudioContext not supported on this device");
			}else{
				console.log("webkitAudioContext not supported on this device");				
			}
		}		
	};
	
	res.audio.play = function(tone){
		if (!context) return;
		var source = context.createBufferSource();
		source.buffer = audioBuffer[tone];
		source.connect(context.destination);
		if (typeof source.noteOn == "function") {
			source.noteOn(0);	// not available on Google Chrome 36.0.1985.125			
		}else if (typeof source.start == "function"){
			source.start(0);			
		}
	};

})();
