
$(document).ready(function(){
	res.locale.init();
	res.phrase = res.locale;
	window.currency = res.locale.currency;
});

res.locale = {
	language: undefined,
	index: { "en": 0, "ja": 1 }, // 0: English(default), 1: Japanese
	init: function(){
		this.language = res.storage.getItem("Language");
		if (!this.language) this.setLanguage(res.config.initialLanguage);
	},
	setLanguage: function(lang) {
		res.storage.setItem("Language", lang);
		this.language = lang;
	},
	changeLanguage: function(){
		if (this.language == "en"){
			this.setLanguage("ja");
		}else{
			this.setLanguage("en");
		}
	},
	get: function(key) {
		if (!key) return "";
		//if (!res.table.phrases[key]){
		if (!res.config.phrases[key]){
			alert("res.phrase.get: res.table.phrases[" + key + "] not found");
		}else{
			//return res.table.phrases[key][this.index[this.language]];
			return res.config.phrases[key][this.index[this.language]];				
		}
	},
	set: function(phraseID, lang, phrase) {
		if (phraseID in res.table.phrases) {
			//res.table.phrases[phraseID][this.index[lang]] = phrase;
			res.conifg.phrases[phraseID][this.index[lang]] = phrase;
		}
	},
/*	setToday: function(){
		var enMonth = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
		var today = new Date();
		var yyyy = today.getFullYear();
		var mm = today.getMonth();
		var dd = today.getDate();
		this.set("NavigationBar.Today", "ja", yyyy + "年" + (mm+1) + "月" + dd + "日");	
		this.set("NavigationBar.Today", "en", enMonth[mm] + " " + dd + ", " + yyyy );
	},*/
	refresh: function() {
		this.language = res.storage.getItem("language");
		//for (var key in res.table.phrases) {
		for (var key in res.config.phrases) {
			$("*[data-phrase=\'" + key.replace(".","\\.") + "\']")
				//.html(res.table.phrases[key][this.index[this.language]]);
				.html(res.config.phrases[key][this.index[this.language]]);
		}
	},
	html: function(key){
		return ("<p data-phrase=\"" + key + "\">" + this.get(key) + "</p>");
	},
	currency: function(num){
	// insert comma at every 3 decimal digits
		var destStr = num.toString(10);
		var tmpStr = "";
		while (destStr != (tmpStr = destStr.replace(/^([+-]?\d+)(\d\d\d)/,"$1,$2"))) {
		    destStr = tmpStr;
		}
		return destStr;			
	}
};
