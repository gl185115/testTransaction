
res.config.storage = {};

(function(){
	
	if (window.opener && window.opener.res.config.storage){
		referTo(window.opener.res.config.storage);
	}else if (window.parent != window && window.parent.res.config.storage){
		referTo(window.parent.res.config.storage);
	}else{
		setDefault();
	}

	function referTo(obj){
		$.extend(res.config.storage, obj);
	}
	
	function setDefault(){
		var directory = "";
	
		res.config.storage.pickLists = 
			[{ 	title: "2014年10月 初版", 
				items: 
					[
					 { itemId: "100", description: { ja: "きゅうり　1本", en: "Cucumber", }, picture: directory + "編集用きゅうりバラ.jpg", },
					 { itemId: "101", description: { ja: "レタス", en: "Lettuce", }, picture: directory + "編集用レタス.jpg", },
					 { itemId: "102", description: { ja: "ブロッコリー", en: "Broccoli", }, picture: directory + "編集用ブロッコリー.jpg", },
					 { itemId: "103", description: { ja: "セロリ", en: "Celery", }, picture: directory + "編集用セロリ.jpg", },
					 { itemId: "104", description: { ja: "アスパラガス　束", en: "Asparagus", }, picture: directory + "編集用アスパラガス.jpg", },
					 { itemId: "105", description: { ja: "キャベツ　1個", en: "Cabbage", }, picture: directory + "編集用サラダキャベツＬ１玉.jpg", },
					 { itemId: "111", description: { ja: "ほうれん草",  en: "Spinach", }, picture: directory + "編集用ほうれん草.jpg", },
					 { itemId: "112", description: { ja: "ニラ", en: "Scallion", }, picture: directory + "編集用ニラ.jpg", },
					 { itemId: "113", description: { ja: "大葉　1束", en: "Perilla",}, picture: directory + "編集用大葉.jpg", },
					 { itemId: "114", description: { ja: "だいこん　1本",  en: "Japanese　radish", }, picture: directory + "編集用大根.jpg", },
					 { itemId: "115", description: { ja: "小松菜",  en: "komatsuna", }, picture: directory + "小松菜.jpg", },
					 { itemId: "116", description: { ja: "エリンギ", en: "King trumpet mushroom", }, picture: directory + "エリンギ.jpg", },
					 { itemId: "118", description: { ja: "ピーマン　1個",  en: "Green pepper", }, picture: directory + "編集用ピーマンﾊﾞﾗ.jpg", },
					 { itemId: "126", description: { ja: "薬味ねぎ", en: "Spice leek", }, picture: directory + "あおねぎ.jpg", },
					 { itemId: "127", description: { ja: "長ねぎ　1本", en: "A long leek", }, picture: directory + "長（白）ねぎ１本.jpg", },
					 { itemId: "128", description: { ja: "長白ねぎ　束", en: "Long  leek", }, picture: directory + "長（白）ねぎ束３.jpg", },
					 { itemId: "129", description: { ja: "かぶ", en: "Turnip", }, picture: directory + "かぶ２.jpg", },
					 { itemId: "134", description: { ja: "とうもろこし", en: "Corn", }, picture: directory + "P5180649.jpg", },
					 { itemId: "136", description: { ja: "サニーレタス・グリーンレタス", en: "Sunny lettuce・Green　lettuce", }, picture: directory + "レタス.jpg", },
					 { itemId: "137", description: { ja: "ゴーヤー", en: "Bitter cucumber", }, picture: directory + "ゴーヤ２.jpg", },
					 { itemId: "138", description: { ja: "みず菜", en: "Potherb mustard", }, picture: directory + "編集用みずな.jpg", },
					 { itemId: "139", description: { ja: "葉しょうが", en: "hasyoga", }, picture: directory + "P6090014.jpg", },
					 { itemId: "139", description: { ja: "たけのこ", en: "" }, picture: directory + "たけのこ.jpg", },
					 { itemId: "140", description: { ja: "エシャレット", en: "Shallot", }, picture: directory + "エシャレット.jpg", },
					 { itemId: "141", description: { ja: "カリフラワー", en: "cauliflower", }, picture: directory + "カリフラワー.jpg", },
					 ],
				categories:
					[
					 { ja: "緑色の野菜", en: "Vegetable in Green" },
					 { ja: "他色の野菜", en: "Vegetables in non Green" },
					 { ja: "くだもの&お花", en: "Fruit & Flower" },
					 { ja: "ご飯もの", en: "Rice" },
					 { ja: "お惣菜", en: "Daily" },
					 { ja: "菓子&パン", en: "Confectionary & Bread" },
					 { ja: "お魚", en: "Fish" },
					 ],
				layout:
					[
					 [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 ],
					 [ 15, 16, 0, 17 ],
					],
			 },
			 { title: "2014年11月 第2版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
						 [],
					    ],
			 },
			 { title: "2015年1月 第3版 商品削除",
					items: [],
					categories: [],
					layout: 
						[
						 [],
					    ],
			 },
			 { title: "2015年1月 第4版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
					    ],
			 },
			 { title: "2015年2月 第5版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
					    ],
			 },
			 { title: "2015年2月 第6版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
					    ],
			 },
			 { title: "2015年3月 第7版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
					    ],
			 },
			 { title: "2015年3月 第8版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
					    ],
			 },
			 { title: "2015年4月 第9版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
					    ],
			 },
			 { title: "2015年4月 第10版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
					    ],
			 },
			 { title: "2015年5月 第11版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
					    ],
			 },
			 { title: "2015年5月 第12版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
					    ],
			 },
			 { title: "2015年6月 第13版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
					    ],
			 },
			 { title: "2015年6月 第14版 商品追加",
					items: [],
					categories: [],
					layout: 
						[
					    ],
			 },
			 ];
	
		res.config.storage.notices = 
			[
			 { title: "2014年 業務連絡1号 商品について"},
			 { title: "2014年 業務連絡2号 話法について"},
			 { title: "2014年 業務連絡3号 店舗について"},
			 { title: "2014年 業務連絡4号 営業時間について"},
			 { title: "2014年 業務連絡5号 商品について"},
			 { title: "2014年 業務連絡6号 話法について"},
			 { title: "2014年 業務連絡7号 店舗について"},
			 { title: "2014年 業務連絡8号 営業時間について"},
			 { title: "2014年 業務連絡9号 商品について"},
			 { title: "2014年 業務連絡10号 話法について"},
			 { title: "2014年 業務連絡11号 店舗について"},
			 { title: "2014年 業務連絡12号 営業時間について"},
			 ];

		res.config.storage.options = [],
		res.config.storage.usability = [],
	
		res.config.storage.tasks = 
			[ { label: "PickList", folders:
				[ { label: "ToDeploy", list:
					[
					 {date: "2014/11/15 00:00", to: "東京", subject: "構成02: 新商品追加", total: 88888, toDeploy: 88888, toApply: 88888,},
					 {date: "2014/11/15 00:00", to: "神奈川", subject: "構成02: 新商品追加", total: 100, toDeploy: 100, toApply: 100,},
					 {date: "2014/11/15 00:00", to: "埼玉", subject: "構成02: 新商品追加", total: 80, toDeploy: 80, toApply: 80,},
					 {date: "2014/11/15 00:00", to: "千葉", subject: "構成02: 新商品追加", total: 70, toDeploy: 70, toApply: 70,},
					 ],				
				}, { label: "Deployed", list:
					[
					 {date: "2014/11/01 00:00", to: "東京", subject: "構成01: 初期導入版", total: 150, toDeploy: 0, toApply: 0, },
					 {date: "2014/11/01 00:00", to: "神奈川", subject: "構成01: 初期導入版", total: 150, toDeploy: 0, toApply: 0, },
					 {date: "2014/11/01 00:00", to: "埼玉", subject: "構成01: 初期導入版", total: 150, toDeploy: 0, toApply: 0, },
					 {date: "2014/11/01 00:00", to: "千葉", subject: "構成01: 初期導入版", total: 150, toDeploy: 0, toApply: 0, },
					 ]
				}, { label: "Draft", list: 
					[
					 {date: "", to: "全店共通", subject: "構成03: 秋の商品一新",toApply: 0,},
				     ]
				}, { label: "Deleted", list: 
					[
				     ]
				},
				]
			}, { label: "Notice",  folders:
				[ { label: "ToDeploy", list:
					[
					 {date: "2014/11/15 00:00", to: "東京", subject: res.config.storage.notices[1].title, total: 150, toDeploy: 150, toOpen: 150, },
					 {date: "2014/11/15 00:00", to: "神奈川", subject: res.config.storage.notices[1].title, total: 100, toDeploy: 100, toOpen: 100, },
					 {date: "2014/11/15 00:00", to: "埼玉", subject: res.config.storage.notices[1].title, total: 80, toDeploy: 80, toOpen: 80, },
					 {date: "2014/11/15 00:00", to: "千葉", subject: res.config.storage.notices[1].title, total: 70, toDeploy: 70, toOpen: 70, },
					 {date: "2014/11/20 00:00", to: "全店", subject: res.config.storage.notices[2].title, total: 400, toDeploy: 400, toOpen: 400, },
					 ],				
				}, { label: "Deployed", list:
					[
					 {date: "2014/11/01 00:00", to: "東京", subject: res.config.storage.notices[0].title, total: 150, toDeploy: 0, toOpen: 5, },
					 {date: "2014/11/01 00:00", to: "神奈川", subject: res.config.storage.notices[0].title, total: 100, toDeploy: 0, toOpen: 3, },
					 {date: "2014/11/01 00:00", to: "埼玉", subject: res.config.storage.notices[0].title, total: 80, toDeploy: 0, toOpen: 0, },
					 {date: "2014/11/01 00:00", to: "千葉", subject: res.config.storage.notices[0].title, total: 70, toDeploy: 0, toOpen: 0, },
					 ]
				}, { label: "Draft", list: 
					[
					 {date: "2014/11/01 00:00", to: "全店対象", subject: res.config.storage.notices[0].title, },
				     ]
				}, { label: "Deleted", list: 
					[
				     ]
				},
				]
			}, { label: "Options",  folders:
				[ { label: "ToDeploy", list:
					[
					 {date: "2014/11/01 00:00", to: "東京", subject: "割引率の変更", total: 150, toDeploy: 150, toApply: 150,},
					 {date: "2014/11/01 00:00", to: "神奈川", subject: "割引率の変更", total: 100, toDeploy: 100, toApply: 100,},
					 {date: "2014/11/01 00:00", to: "埼玉", subject: "割引率の変更", total: 80, toDeploy: 80, toApply: 80,},
					 {date: "2014/11/01 00:00", to: "千葉", subject: "割引率の変更", total: 70, toDeploy: 70, toApply: 70,},
					 ],				
				}, { label: "Deployed", list:
					[
					 {date: "2014/11/20 00:00", to: "東京", subject: "割引率の変更", total: 150, toDeploy: 0, toApply: 0,},
					 {date: "2014/11/20 00:00", to: "神奈川", subject: "割引率の変更", total: 100, toDeploy: 0, toApply: 0,},
					 {date: "2014/11/20 00:00", to: "埼玉", subject: "割引率の変更", total: 80, toDeploy: 0, toApply: 0,},
					 {date: "2014/11/20 00:00", to: "千葉", subject: "割引率の変更", total: 70, toDeploy: 0, toApply: 0,},
					 ]
				}, { label: "Draft", list: 
					[
					 {date: "2014/11/30 00:00", to: "東京", subject: "割引率の変更", toApply: 0,},
					 {date: "2014/11/30 00:00", to: "神奈川", subject: "割引率の変更", toApply: 0,},
					 {date: "2014/11/30 00:00", to: "埼玉", subject: "割引率の変更", toApply: 0,},
					 {date: "2014/11/30 00:00", to: "千葉", subject: "割引率の変更", toApply: 0,},
				     ]
				}, { label: "Deleted", list: 
					[
				     ]
				},
				]
			}
//			{ label: "Usability",  folders:
//				[ { label: "ToDeploy", list:
//					[
//					 {date: "2014/11/01 00:00", to: "東京", subject: "画面構成変更", total: 150, toDeploy: 150, },
//					 {date: "2014/11/01 00:00", to: "神奈川", subject: "画面構成変更", total: 100, toDeploy: 100, },
//					 {date: "2014/11/01 00:00", to: "埼玉", subject: "画面構成変更", total: 80, toDeploy: 80, },
//					 {date: "2014/11/01 00:00", to: "千葉", subject: "画面構成変更", total: 70, toDeploy: 70, },
//					 ],				
//				}, { label: "Deployed", list:
//					[
//					 {date: "2014/11/01 00:00", to: "東京", subject: "画面構成変更", total: 150, toDeploy: 0,},
//					 {date: "2014/11/01 00:00", to: "神奈川", subject: "画面構成変更", total: 100, toDeploy: 0,},
//					 {date: "2014/11/01 00:00", to: "埼玉", subject: "画面構成変更", total: 80, toDeploy: 0,},
//					 {date: "2014/11/01 00:00", to: "千葉", subject: "画面構成変更", total: 70, toDeploy: 0,},
//					 ]
//				}, { label: "Draft", list: 
//					[
//					 {date: "2014/11/01 00:00", to: "東京", subject: "画面構成変更"},
//					 {date: "2014/11/01 00:00", to: "神奈川", subject: "画面構成変更"},
//					 {date: "2014/11/01 00:00", to: "埼玉", subject: "画面構成変更"},
//					 {date: "2014/11/01 00:00", to: "千葉", subject: "画面構成変更"},
//				     ]
//				}, { label: "Deleted", list: 
//					[
//				     ]
//				},
//				]
//			}
			];

	}
})();

