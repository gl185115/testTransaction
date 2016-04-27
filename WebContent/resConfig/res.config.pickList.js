/*
 * Table of Pick List Items
 */

(function(){
	
	if (window.opener && window.opener.res.config.pickList){
		referTo(window.opener.res.config.pickList);
	}else if (window.parent != window && window.parent.res.config.pickList){
		referTo(window.parent.res.config.pickList);
	}else{
		setDefault();
	}

	function referTo(obj){
		res.config.pickList = {};
		$.extend(res.config.pickList, obj);
	}
	
	function setDefault(){

		var directory = "";
		
		res.config.pickList = {};
		
		res.config.pickList.categories = [
			{ ja: "緑色の野菜", en: "Vegetable in Green" },
			{ ja: "他色の野菜", en: "Vegetables in non Green" },
			{ ja: "くだもの&お花", en: "Fruit & Flower" },
			{ ja: "ご飯もの", en: "Rice" },
			{ ja: "お惣菜", en: "Daily" },
			{ ja: "菓子&パン", en: "Confectionary & Bread" },
			{ ja: "お魚", en: "Fish" },
			{ ja: "お肉", en: "Meat" },
			{ ja: "商品一般", en: "Popular" },
		];
		
		res.config.pickList.baseURL = "/pseudoui/resConfig/pickList/";
		res.config.pickList.lists = [
		
		[	// Category 1
			{ 
				itemId: "100",
				picture: directory + "編集用きゅうりバラ.jpg",
				description: { ja: "きゅうり　1本", en: "Cucumber", },
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "101",
				picture: directory + "編集用レタス.jpg",
				description: { ja: "レタス", en: "Lettuce", },
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "102",
				picture: directory + "編集用ブロッコリー.jpg",
				description: { ja: "ブロッコリー", en: "Broccoli", },
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "103",
				picture: directory + "編集用セロリ.jpg",
				description: { ja: "セロリ", en: "Celery", },
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "104",
				picture: directory + "編集用アスパラガス.jpg",
				description: { ja: "アスパラガス　束", en: "Asparagus", },
		//		<Categories>1</Categories>
		
			},
			{ 
				itemId: "105",
				picture: directory + "編集用サラダキャベツＬ１玉.jpg",
				description: { ja: "キャベツ　1個", en: "Cabbage", },
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "111",
				picture: directory + "編集用ほうれん草.jpg",
				description: { ja: "ほうれん草",  en: "Spinach", }
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "112",
				picture: directory + "編集用ニラ.jpg",
				description: { ja: "ニラ", en: "Scallion", }
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "113",
				picture: directory + "編集用大葉.jpg",
				description: { ja: "大葉　1束", en: "Perilla",}
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "114",
				picture: directory + "編集用大根.jpg",
				description: { ja: "だいこん　1本",  en: "Japanese　radish", }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "115",
				picture: directory + "小松菜.jpg",
				description: { ja: "小松菜",  en: "komatsuna", }
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "116",
				picture: directory + "エリンギ.jpg",
				description: { ja: "エリンギ", en: "King trumpet mushroom", }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "118",
				picture: directory + "編集用ピーマンﾊﾞﾗ.jpg",
				description: { ja: "ピーマン　1個",  en: "Green pepper", }
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "126",
				picture: directory + "あおねぎ.jpg",
				description: { ja: "薬味ねぎ", en: "Spice leek", }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "127",
				picture: directory + "長（白）ねぎ１本.jpg",
				description: { ja: "長ねぎ　1本", en: "A long leek", }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "128",
				picture: directory + "長（白）ねぎ束３.jpg",
				description: { ja: "長白ねぎ　束", en: "Long  leek", }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "129",
				picture: directory + "かぶ２.jpg",
				description: { ja: "かぶ", en: "Turnip", }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "134",
				picture: directory + "P5180649.jpg",
				description: { ja: "とうもろこし", en: "Corn", }
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "136",
				picture: directory + "レタス.jpg",
				description: { ja: "サニーレタス・グリーンレタス", en: "Sunny lettuce・Green　lettuce", }
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "137",
				picture: directory + "ゴーヤ２.jpg",
		//		<Categories>1</Categories>
				description: { ja: "ゴーヤー", en: "Bitter cucumber", }
			},
			{ 
				itemId: "138",
				picture: directory + "編集用みずな.jpg",
				description: { ja: "みず菜", en: "Potherb mustard", }
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "139",
				picture: directory + "P6090014.jpg",
				description: { ja: "葉しょうが", en: "hasyoga", }
		//		<Categories>1</Categories>
			},
			{ 
				itemId: "139",
				picture: directory + "たけのこ.jpg",
				description: { ja: "たけのこ", en: "" }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "140",
				picture: directory + "エシャレット.jpg",
				description: { ja: "エシャレット", en: "Shallot", }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "141",
				picture: directory + "カリフラワー.jpg",
				description: { ja: "カリフラワー", en: "cauliflower", }
		//		<Categories>1,2</Categories>
			},
		],
		
		[	// Category 2
			{ 
				itemId: "100",
				picture: directory + "編集用きゅうりバラ.jpg",
				description: { ja: "きゅうり　1本", en: "Cucumber", },
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "106",
				picture: directory + "編集用トマト.jpg",
				description: { ja: "トマト　1個", en: "Tomato", }
		//		<Categories>2</Categories>
		
			},
			{ 
				itemId: "107",
				picture: directory + "編集用パプリカ.jpg",
				description: { ja: "パプリカ", en: "Paprika", }
		//		<Categories>2</Categories>
			},
			{ 
				itemId: "109",
				picture: directory + "編集用レモン.jpg",
				description: { ja: "レモン　1個", en: "Lemon", }
		//		<Categories>2,3</Categories>
			},
			{ 
				itemId: "114",
				picture: directory + "編集用大根.jpg",
				description: { ja: "だいこん　1本",  en: "Japanese　radish", }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "116",
				picture: directory + "編集用生しいたけﾊﾞﾗ.jpg",
				description: { ja: "生しいたけ", en: "Shiitake Mushroom", }
		//		<Categories>2</Categories>
			},
			{ 
				itemId: "117",
				picture: directory + "編集用なすバラ.jpg",
				description: { ja: "なす　1本",  en: "Eggplant", }
		//		<Categories>2</Categories>
			},
			{ 
				itemId: "122",
				picture: directory + "編集用たまねぎバラ.jpg",
				description: { ja: "たまねぎ1個", en: "Onion", }
		//		<Categories>2</Categories>
			},
			{ 
				itemId: "123",
				picture: directory + "編集用ばれいしょバラ.jpg",
				description: { ja: "じゃがいも1個", en: "Potato", }
		//		<Categories>2</Categories>
			},
			{ 
				itemId: "124",
				picture: directory + "編集用さつまいもバラ.jpg",
				description: { ja: "さつまいも1本", en: "Sweet potato", }
		//		<Categories>2</Categories>
			},
			{ 
				itemId: "125",
				picture: directory + "編集用人参ﾊﾞﾗ.jpg",
				description: { ja: "にんじん1本", en: "Carrot", }
		//		<Categories>2</Categories>
			},
			{ 
				itemId: "126",
				picture: directory + "あおねぎ.jpg",
				description: { ja: "薬味ねぎ", en: "Spice leek", }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "127",
				picture: directory + "長（白）ねぎ１本.jpg",
				description: { ja: "長ねぎ　1本", en: "A long leek", }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "128",
				picture: directory + "長（白）ねぎ束３.jpg",
		//		<Categories>1,2</Categories>
				description: { ja: "長白ねぎ　束", en: "Long  leek", }
			},
			{ 
				itemId: "129",
				picture: directory + "かぶ２.jpg",
		//		<Categories>1,2</Categories>
				description: { ja: "かぶ", en: "Turnip", }
			},
			{ 
				itemId: "133",
				picture: directory + "アボカド１.jpg",
				description: { ja: "アボカド　", en: "Avocado", }
		//		<Categories>2,3</Categories>
			},
			{ 
				itemId: "135",
				picture: directory + "トマト（ミニ）.jpg",
				description: { ja: "ミニトマト", en: "Mini tomato", }
		//		<Categories>2</Categories>
			},
			{ 
				itemId: "139",
				picture: directory + "たけのこ.jpg",
				description: { ja: "たけのこ", en: "" }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "140",
				picture: directory + "エシャレット.jpg",
				description: { ja: "エシャレット", en: "Shallot", }
		//		<Categories>1,2</Categories>
			},
			{ 
				itemId: "141",
				picture: directory + "カリフラワー.jpg",
				description: { ja: "カリフラワー", en: "cauliflower", }
		//		<Categories>1,2</Categories>
			},
		],
		
		[	// Category 3
			{ 
				itemId: "108",
				picture: directory + "90b47656.jpg",
				description: { ja: "いよかん", en: "Iyokan　orange", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "108",
				picture: directory + "みかん.jpg",
				description: { ja: "みかん", en: "Mandarin　orange", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "109",
				picture: directory + "編集用レモン.jpg",
				description: { ja: "レモン　1個", en: "Lemon", }
		//		<Categories>2,3</Categories>
			},
			{ 
				itemId: "110",
				picture: directory + "編集用キィーウィ.jpg",
				description: { ja: "キウィフルーツ　1個", en: "Kiwifruit", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "119",
				picture: directory + "編集用オレンジ①.jpg",
				description: { ja: "オレンジ　1個", en: "Orange", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "120",
				picture: directory + "編集用グレープフルーツ.jpg",
				description: { ja: "グレープフルーツ　1個", en: "Grapefruit", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "121",
				picture: directory + "バナナ.jpg",
				description: { ja: "バナナ", en: "Banana", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "130",
				picture: directory + "編集用りんご赤ﾊﾞﾗ.jpg",
				description: { ja: "りんご（赤）", en: "Red apple", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "131",
				picture: directory + "編集用りんご青バラ.jpg",
				description: { ja: "りんご（青）", en: "Green apple", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "132",
				picture: directory + "ﾊﾟｲﾝ.gif",
				description: { ja: "パイナップル", en: "pineapple", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "133",
				picture: directory + "アボカド１.jpg",
				description: { ja: "アボカド　", en: "Avocado", }
		//		<Categories>2,3</Categories>
			},
			{ 
				itemId: "142",
				picture: directory + "もも.jpg",
				description: { ja: "桃", en: "Peach", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "142",
				picture: directory + "洋梨.jpg",
				description: { ja: "洋梨", en: "Pear", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "143",
				picture: directory + "デラウエア.jpg",
				description: { ja: "デラウェア", en: "Delaware", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "143",
				picture: directory + "柿.jpg",
				description: { ja: "柿", en: "Persimmon", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "144",
				picture: directory + "デコポン.jpg",
				description: { ja: "デコポン", en: "DEKOPON", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "145",
				picture: directory + "梨.jpg",
				description: { ja: "梨", en: "Japanese pear", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "146",
				picture: directory + "画像2 003.jpg",
				description: { ja: "ゆず", en: "ｙｕｚｕ", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "146",
				picture: directory + "ポンカン.jpg",
				description: { ja: "ポンカン", en: "Ponkan　orange", }
		//		<Categories>3</Categories>
			},
			{ 
				itemId: "147",
				picture: directory + "編集用花鉢.jpg",
				description: { ja: "花苗", en: "Flower plant", }
		//		<Categories>3</Categories>
			},
		],
		
		[	// Category 4
		
		],
		
		[	// Category 5
		
		],
		
		[	// Category 6
		
		],
		
		[	// Category 7
		
		],
		
		[	// Category 8
		
		],
		
		[	// Category 9
		 	
		],
		
		];	// end of pick lists

	}


})();
