function checkAttributeRelation(ArrayList) {
	var NgMessage = [
	                 'プリンターと属性３の整合性がとれていません。',
	                 'プリンターと属性３の整合性がとれていません。',
	                 'ドロワーと属性３の整合性がとれていません。',
	                 'ドロワーと属性３の整合性がとれていません。',
	                 'ドロワーと釣銭機と属性３の整合性がとれていません。',
	                 '釣銭機と属性３の整合性がとれていません。',
	                 '属性２と属性３の整合性がとれていません。',
	                 'ドロワーと釣銭機と属性２の整合性がとれていません。',
	                 'MSRと属性１の整合性がとれていません。',
	                 'MSRと属性１の整合性がとれていません。',
	                 'MSRと属性１の整合性がとれていません。',
	                 'MSRと属性１の整合性がとれていません。',
	                 'MSRと属性４の整合性がとれていません。',
	                 'MSRと属性４の整合性がとれていません。',
	                 'MSRと属性５の整合性がとれていません。',
	                 'MSRと属性５の整合性がとれていません。',
	                 'MSRと属性６の整合性がとれていません。',
	                 'MSRと属性６の整合性がとれていません。',
	                 'MSRと属性７の整合性がとれていません。',
	                 'MSRと属性７の整合性がとれていません。'
	                 ];
	var relationNG = [
	                  // Printer, Till, CreditTerminal, MSR, CashChanger, Attribute1, Attribute2, Attribute3, Attribute4, Attribute5, Attribute6, Attribute7
	                  ['1', null, null, null, null, null, null, '0', null, null, null, null],
                      ['0', null, null, null, null, null, null, '1', null, null, null, null],
                      [null, 'Manual', null, null, null, null, null, '0', null, null, null, null],
                      [null, 'Auto', null, null, null, null, null, '0', null, null, null, null],
                      [null, 'None', null, null, '0', null, null, '1', null, null, null, null],
                      [null, null, null, null, '1', null, null, '0', null, null, null, null],
                      [null, null, null, null, null, null, '1', '0', null, null, null, null],
                      [null, 'None', null, null, '0', null, '1', null, null, null, null, null],
                      //
                      [null, null, null, '1', null, '2', null, null, null, null, null, null],
                      [null, null, null, '1', null, '3', null, null, null, null, null, null],
                      [null, null, null, '0', null, '1', null, null, null, null, null, null],
                      [null, null, null, '2', null, '1', null, null, null, null, null, null],
                      [null, null, null, '0', null, null, null, null, '1', null, null, null],
                      [null, null, null, '2', null, null, null, null, '1', null, null, null],
                      [null, null, null, '0', null, null, null, null, null, '1', null, null],
                      [null, null, null, '2', null, null, null, null, null, '1', null, null],
                      [null, null, null, '0', null, null, null, null, null, null, '1', null],
                      [null, null, null, '2', null, null, null, null, null, null, '1', null],
                      [null, null, null, '0', null, null, null, null, null, null, null, '1'],
                      [null, null, null, '2', null, null, null, null, null, null, null, '1']
                      ];
	
	for(var i=0;i<relationNG.length; i++) {
	    var isOK = false;
		for(var j=0; j<relationNG[i].length; j++) {
			if (relationNG[i][j] == null) {
				continue;
			}
			
			if (relationNG[i][j] != ArrayList[j]) {
				isOK = true;
				break;
			}
		}
		if (isOK == false) {
			return NgMessage[i];
		}
	}
	
	return '';
}