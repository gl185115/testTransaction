/*** c8: start of update ***/
var res = res || {};
/*** c8: start of update ***/

/*
 * AngularJS filters
 *
 * For using these filters, the application needs to include,
 * 	angular.module("myApp", ["res.filters"]); // where "myApp" is the app name defined with ng-app="myApp"
 */

res.filters = angular.module("res.filters", []);

/*
 * filter: resPhrase {{ phraseID | resPhrase:context }}
 *
 * returns: phrase identified by context.phraseID in the phrase table (res.table.phrase.js)
 */
res.filters.filter("resPhrase", ["$rootScope", function ($rootScope) {
        var index = {"en": 0, "ja": 1}; // 0: English(default), 1: Japanese
        return function (id, context) {
            var key = "", out = "";
            if (context) {
                key += context + ".";
            }
            key += id;
            if (!key)
                return "";
            //if (!res.table.phrases[key]){
            if (!res.config.phrases[key]) {
                out = "[" + key + "]";

            } else {
                //out = res.table.phrases[key][index[$rootScope.language]];
            	var langIndex = ($rootScope.language === 'ja' || $rootScope.language === 'jp') ? "ja" : "en";
            	out = res.config.phrases[key][index[langIndex]];
//                out = res.config.phrases[key][index[$rootScope.language]];
            }
            return out;
        };
    }]);

/*
 * filter: resPhraseInLine {{ phraseObj | resPhraseInLine }}
 *
 * returns: phrase in the current language. phraseObj needs to be like { en:"hello", ja:"おはよう"}
 */
res.filters.filter("resPhraseInLine", ["$rootScope", function ($rootScope) {
        return function (obj) {
            out = "";
            if (typeof obj != "object") {
                out = "";
            } else {
            	var lang = $rootScope.language;
            	if (obj.jp && (lang === 'ja' || lang === 'jp')) {
            		lang = "jp";
            	}

//            	if($rootScope.language=="ja"){
//            		if(obj["jp"]!=undefined){
//            			out =obj["jp"];
//            			return out;
//            		}
//            	};
//            	out = obj[$rootScope.language];
            	out = obj[lang];
            }
            return out;
        };
    }]);

/*
 * filter: resDate {{ date | resDate:'long' }}
 *
 * returns: formatted date in local language
 */
res.filters.filter("resDate", ["$rootScope", function ($rootScope) {
        var enMonth = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        return function (date, option) {
            var out = "", jpYear = "";
            if (date) {
                var y = date.getFullYear();
                var m = date.getMonth();
                var d = date.getDate();
                switch ($rootScope.language) {
                    case "ja":
                        if (option == "long") {
                            if (date > new Date(1989, 1, 7)) {
                                jpYear = "平成";
                                if (y == 1989)
                                    jpYear += "元年";
                                else
                                    jpYear += (y - 1988) + "年";
                            } else if (date > new Date(1926, 12, 24)) {
                                jpYear = "昭和";
                                if (y == 1926)
                                    jpYear += "元年";
                                else
                                    jpYear += (y - 1925) + "年";
                            } else if (date > new Date(1912, 7, 29)) {
                                jpYear = "大正";
                                if (y == 1912)
                                    jpYear += "元年";
                                else
                                    jpYear += (y - 1911) + "年";
                            } else {
                                jpYear = "明治";
                                jpYear += (y - 1867) + "年";
                            }
                            out = y + "年(" + jpYear + ")" + (m + 1) + "月" + d + "日";
                        } else {
                            out = y + "年" + (m + 1) + "月" + d + "日";
                        }
                        break;
                    case "en":
                        out = enMonth[m] + " " + d + ", " + y;
                        break;
                }
            }
            return out;
        };
    }]);

/*
 * filter: resDateTime {{ date | resDateTime }}
 *
 * returns: formatted date in local language
 */
res.filters.filter("resDateTime", ["$rootScope", function ($rootScope) {
        return function (date) {
            var out = "";
            if (date) {
                if (typeof date == "string") {
                	if (date.indexOf("T") != -1) {
                		date = new Date(date.replace("T", " "));
                	} else {
                		date = new Date(date);
                	}
                    date.setHours(date.getHours() + ((new Date()).getTimezoneOffset() / 60) + 9);
//                    date.setHours(date.getHours() + (new Date()).getTimezoneOffset() / 60);
                }
                switch ($rootScope.language) {
                    case "ja":
                        out = date.toLocaleString("ja-JP", {year: "numeric", month: "2-digit", day: "2-digit", hour: "2-digit", minute: "2-digit"});
                        break;
                    case "en":
                        out = date.toLocaleString("en-US", {year: "numeric", month: "short", day: "2-digit", hour: "2-digit", minute: "2-digit"});
                        break;
                }
            }
            return out;
        };
    }]);
/*
 * filter: resNumber {{ number | resNumber }}
 *
 * returns: formatted integer number in 9,999,999 format
 */
res.filters.filter("resNumber", function () {
    return function (number, option) {
        if (number == undefined)
            return "";
        if (isNaN(number))
            return "";
        var out = "", tmpStr = "";
        if (!option)
            option = "Number";
        switch (option) {
            case "Number":
                out = number.toString(10);
                // insert comma at every 3 decimal digits
                while (out != (tmpStr = out.replace(/^([+-]?\d+)(\d\d\d)/, "$1,$2")))
                    out = tmpStr;
                break;
            case "CodeBy4":
                out = number.toString(10);
                // insert space at every 4 decimal digits
                while (out != (tmpStr = out.replace(/^([+-]?\d+)(\d\d\d\d)/, "$1 $2")))
                    out = tmpStr;
                break;
            case "EAN":
                out = number.toString(10);
                // insert space at every 3 decimal digits
                while (out != (tmpStr = out.replace(/^([+-]?\d+)(\d\d\d)/, "$1 $2")))
                    out = tmpStr;
                break;
            case "Currency":
            case "CurrencySymbol":
                var integer = "", fraction = "", tmpStr = "";
                var symbol = res.config.currency.symbol;
                var minus = (number < 0);
                if (minus)
                    number = -number;
                out = number.toString(10);
                if (res.config.currency.name == "JPY") {
                    integer = out;
                    fraction = "";
                } else {
                    while (out.length < 3)
                        out = "0" + out;
                    integer = out.slice(0, -2);
                    fraction = "." + out.slice(-2);
                }
                while (integer != (tmpStr = integer.replace(/^([+-]?\d+)(\d\d\d)/, "$1,$2"))) {
                    integer = tmpStr;
                }
                out = integer + fraction;
                if (option == "CurrencySymbol")
                    out = symbol + out;
                if (minus) {
                    out = "<span class='currency minus'>-" + out + "</span>";
                } else {
                    out = "<span class='currency'>" + out + "</span>";
                }
                break;
            case "Password":
                var maskChar = '*';
                for (var i = 0; i < number.length; i++)
                    out += maskChar;
                break;
            case "PAN":
                var maskChar = 'x';
                var unmask = number.slice(-4);
                var masks = number.length - unmask.length;
                while (masks > 0) {
                    out += maskChar;
                    masks--;
                    if (masks % 4 == 0)
                        out += " ";
                }
                out += unmask;
                break;
        }
        return out;
    };
});

/*
 * filter: resEAN {{ number | resEAN }}
 *
 * returns: formatted integer number in 9,999,999 format
 */
res.filters.filter("resEAN", function () {
    return function (number) {
        if (number == undefined)
            return "";
        var out = "", tmpStr = "";
        out = number.toString(10);
        // insert space at every 3 decimal digits
        while (out != (tmpStr = out.replace(/^([+-]?\d+)(\d\d\d)/, "$1 $2"))) {
            out = tmpStr;
        }
        return out;
    };
});

/*
 * filter: resCurrency {{ amount | resCurrency:option }}
 *
 * returns: formatted currency, with currency symbol if option == true
 * 			if negative amount, wrap with <span class="currency minus">...</span>
 */
res.filters.filter("resCurrency", ["$rootScope", function ($rootScope) {
        return function (number, option) {
            // insert comma at every 3 decimal digits
            if (isNaN(number))
                return "";
            var out = "", integer = "", fraction = "", tmpStr = "";
            var symbol = res.config.currency.symbol;
            var minus = (number < 0);
            if (minus)
                number = -number;
            out = number.toString(10);
            if (res.config.currency.name == "JPY") {
                integer = out;
                fraction = "";
            } else {
                while (out.length < 3)
                    out = "0" + out;
                integer = out.slice(0, -2);
                fraction = "." + out.slice(-2);
            }
            while (integer != (tmpStr = integer.replace(/^([+-]?\d+)(\d\d\d)/, "$1,$2"))) {
                integer = tmpStr;
            }
            out = integer + fraction;
            if (option)
                out = symbol + out;
            if (minus) {
                out = "<span class='currency minus'>-" + out + "</span>";
            } else {
                out = "<span class='currency'>" + out + "</span>";
            }
            return out;
        };
    }]);

/*
 * filter: resPassword
 *
 * returns: masked number (ex. 1234 -> ****)
 */
res.filters.filter("resPassword", function () {
    var maskChar = '*';
    return function (number) {
        var out = "";
        for (var i = 0; i < number.length; i++)
            out += maskChar;
        return out;
    };
});

/*
 * filter: resPAN
 *
 * returns: masked number (ex. 1234567890123456 -> xxxx xxxx xxxx 3456)
 */
res.filters.filter("resPAN", function () {
    var maskChar = 'x';
    return function (number) {
        if (!number)
            return "";
        var unmask = number.slice(-4);
        var masks = number.length - unmask.length;
        var out = "";
        while (masks > 0) {
            out += maskChar;
            masks--;
            if (masks % 4 == 0)
                out += " ";
        }
        out += unmask;
        return out;
    };
});

/*
 *	filter: resWithoutExtension
 *
 *	returns file name without extension
 */
res.filters.filter("resWithoutExtension", function () {
    return function (filename) {
        var result = filename;
        for (var i = filename.length; i > 0; i--) {
            if (filename.charAt(i) == ".") {
                result = filename.substring(0, i);
                break;
            }
        }
        return result;
    };
});
/*
 *	filter: resStoreIdToName
 *
 *	returns translated  Store name  by StoreID.
 */
res.filters.filter("resStoreIdToName", ["$rootScope", function ($rootScope) {
        return function (storeName) {
            var result = storeName;
            if (res.config.allstores) {
                for (var i in res.config.allstores) {
                    var data = res.config.allstores[i];
                    if (storeName.trim() == data.store.en.trim()) {
                        result = ($rootScope.language == "ja") ? (data.store.ja||data.store.jp) : data.store.en;
                        return result;
                    }
                }
            }
            return result;
        };
    }]);
/*
 *	filter: resLongTextEllipses
 *
 *	returns string with "..."
 */
res.filters.filter("resLongTextEllipses", [function () {
        return function (str, MAX) {
            str += "";
            var out = str, ELLIPSES = "...";
            if (str.length > 0) {
                if (str.length > MAX) {
                    out = str.substr(0, MAX) + ELLIPSES;
                }
            }
            return out;
        };
    }]);
/*
 *	filter: resDateValidation
 *
 *	returns true or false
 */
res.filters.filter("resDateValidation", [function () {
        return function (date, TYPE) {
            var flag = false, MONTHS = 12, DAYS = 31, HOURS = 24, MINS = 60;
            switch (TYPE) {
                case "M": //month
                    if (date <= MONTHS) {
                        flag = true;
                    }
                    break;
                case "D": //day
                    if (date <= DAYS) {
                        flag = true;
                    }
                    break;
                case "H": //hour
                    if (date <= HOURS) {
                        flag = true;
                    }
                    break;
                case "m": //minute
                    if (date <MINS) {
                        flag = true;
                    }
                    break;
            }
            return flag;
        };
    }]);
/*
 *	filter: resFormatStripTxt
 *
 *	returns new string with "...
 */
res.filters.filter("resFormatStripTxt", ["$filter", function ($filter) {
        return function (str, MAX_LENGTH) {
        	if(!str)return str;
            var text = str.split("<br>"), out = "";
            if (text.length > 0) {
                for (var i = 0; i < text.length; i++) {
                	text[i] =  text[i].replace(/\&amp;/g, "&").replace(/\&lt;/g, "<").replace(/\&gt;/g, ">");
                	text[i] = $filter("resLongTextEllipses")(text[i], MAX_LENGTH);
                	text[i] = text[i].replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;");
                	out += text[i] + "<br>";
//                    out += $filter("resLongTextEllipses")(text[i], MAX_LENGTH) + "<br/>";
                }
                out = out.substr(0, out.length - "<br>".length);
            } else {
                out = str;
            }
            return out;
        };
    }]);

/*
* filter: resCovertLF
*
* returns: text with \n converted to <br>
*/
res.filters.filter("resConvertLF", function(){
	return function(text){
		var html = "";
		if(!text)return html;
		html = text.replace(/\r?\n/g, "<br />");
		return html;
	};
});
/*
* filter: code html
*
* returns: text with HTML tags <br>
*/
res.filters.filter("codeHtml", function($sce) {
		  return function(htmlCode){
			if(htmlCode)
		    return htmlCode.replace("&lt;", /</g).replace("&gt;", />/g).replace("&amp;", "&");
		  };
	});


