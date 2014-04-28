package com.people.qpos;

import com.people.util.Utils;

import android.content.Context;
import android.os.Handler;
import dspread.voicemodem.CardReader;
import dspread.voicemodem.util;

/**
 * 更新密钥
 * 
 * @author Fncat
 * 
 */
public class ThreadUpDataKey extends Thread {
	private Handler mHandler;
	private Context mContext;
	private CardReader c;

	public ThreadUpDataKey(Handler mHandler, Context mContext, CardReader c) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.c = c;
	}

	@Override
	public void run() {
		// update key
		String string = "500100001594f08e332baefba17cebe1d3885ab2077d72f08ccc33edcaaa469b367ed9fb19e8f86da07236e5a9b33d5a058580500268a780845b358ad90232c0fc7e979a2b35acd6f2f87fe0d6bc1e1c8042650f112913e83f4fd749e4e086c8b5d0b50a3eb2b1cf3cab7314cb97c3a9980b1a082572c752de7e04987c8f666ac16c7bb322bb227fd55397438f90e61768d1e83a450c39876bc42033eb7f883d4091c2fbcd88d08a0ae64fb07876ef98573f8101fabd3f7395292ff8b568dc2feeaa5e1f361c8b5c4d615926bd40191126fbf9513cc06cc95acc8245b8bbc279c2282bc1a3147ecaca1de8f6e8cb2cb51acd169c5e64b6e11979dc5f50ba44c332505dc829db021c456bd2da4cc46717f281098b12bd1c31f8ce2338597e52d1dcb867250f46b301794337047ca159cbf5a38cbc12708e476d7a1759d2cb7af931869405067b56398435b1e7dbef6f2aabf1f7bcffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
		string = "60010000546808BF96004289388D2A2646A11C22826E82A61B5EFE996776307A71698F6425A587769050BBF84D194919F5FB575461DF717A269BD92A0D58AAEA492FE684B2C06B812358D2FF8010C46F7C4B405D6510AEE66BA7D300C3685B70E050E5B32720DC81B253CE8C7EE13D900841F85C8E761B12B54F7350889D3CC994482566ABD949E9BD3A2F4D491951E7D3E9C2E349E6E2C467032FAF385C975CEB1BDDAB6FF16D4500B37E8238F468030C625C9BA2958F59CDF1E01CBBA5CBC716841DFCB5DA236365562AE7CE4E5422D1F4D3A90000000000000000000000000000000039422FA869449E9FA04682739007EC1F2F8B40F6173B51B3874B741A17CAF0A0BDDE0BE5984E99FAACF504D6BA52D116B3AAF70DCE79710751001601BD9DCE844850A37F629EA2064553470610B1C000400D64BF69C04C46C428EA8D5499A020BAEA8120A14FC91AEF6BA966E9D10A1306700A6C72E01FB43BCD77A374CF4445FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
		string = "500100000722ffe551a55762776bfeab0793de1719ede46b28630ba43886fcbba7719094d1eb9b9b66ee7f02787b4357eb5386ced62cdb16235b584ecb5c4b82a50e64f96b00caf123e0ff58e1dd052548f647902125663f563da964734707fb8193170f9f4a14a560be8d56635b318912ab55d1552552fc9309199def2da54f2ccf9f5c22bb227fd55397438f90e61768d1e83a450c39876bc42033eb7f883d4091c2fbcd88d08a0ae64fb07876ef98573f8101fabd3f7395292ff8b568dc2feeaa5e1f361c8b5c4d615926bd40191126fbf9519785640e0d333c455a9b06f29f37226e8e0e601bc0902586f57d31e33e069b084cb52f1406620a38b8b77fb52e67b39a7bdbb80690c8fca08a31d420c862ad147c5a445a8507cbd93e39a10c0d9fc685f3e5c1939cb4ba1a0355da78ebd0ae577c2189d5c1824e09c267ae27d899d2d40f4169590593c3a7b2ae221c53a1b421ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
		string = "5001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000b85959fb8abb7d447b508f1411dd0ee622bb227fd55397438f90e61768d1e83a450c39876bc42033eb7f883d4091c2fbcd88d08a0ae64fb07876ef98573f8101fabd3f7395292ff8b568dc2feeaa5e1f361c8b5c4d615926bd40191126fbf9514a0202ab1ac536b92d0cb7ffc2e11caaf125b6d8d2343336e0da08742ec93c15c7f2f2d3b3490a2d3729fc21cee3764beb1e2a72c73754a4652c2689b6907ef16a28e9b1e1f7cf5a6fa971aeeefe3157a5faeb83e89f070e5ce18f135a16a97e9d6b8526552de36f9797b1ce45aee66fb070e5c5dbbd59e969078a531b2d3000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
		byte[] cmdBytes = util.HexStringToByteArray(string);
		int r = c.doSecurityCommand(cmdBytes);
		if (r == CardReader.SUCCESS) {
			Utils.HandData(mHandler, "Update key success", 0);
		} else if (r == CardReader.TIMEOUT) {
			Utils.HandData(mHandler, "Update key time out", 0);
		} else if (r == CardReader.FAILURE) {
			Utils.HandData(mHandler, "Update key fail", 0);
		}
	}

}
