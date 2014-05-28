package com.people.client;

import android.annotation.SuppressLint;
import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class TransferRequestTag {
	
	//测试->生产  21->23  22->24

	public static final int Login = 1;// 登录
	public static final int Register = 2;// 注册
	public static final int ModifyLoginPwd = 3;// 修改登录密码
	public static final int ForgetLoginPwd = 4;// 忘记登录密码
	public static final int SignIn = 5;// 签到
	public static final int Consume = 6;// 消费
	public static final int ConsumeCancel = 7;// 消费撤销
	public static final int BalanceQuery = 8;// 余额查询
	public static final int FlowQuery = 9;// 流水查询
	public static final int CreditCardApply = 10;// 信用卡额度申请
	public static final int ClearQuery = 11;// 清算查询
	public static final int AppCommend = 12;// 应用推荐
	public static final int ReferenceMsg = 13;// 参考信息
	public static final int ShareTransfer = 14;// 分享交易
	public static final int ExaminePhone = 15;// 检验手机号是否存在
	public static final int CompareOldPwd = 16;// 对比原密码是否正确
	public static final int SmsSend = 17;// 短信发送
	public static final int SmsCheck = 18;// 短信码验证
	public static final int MerchantQuery = 19;// 商户信息查询
	public static final int LoadUpHead = 20;// 上传头像
	public static final int GetDownLoadHead = 21;// 下载头像
	public static final int CashCharge = 22;// 现金记账
	public static final int GetCashCharge = 23;// 获取现金记账列表
	public static final int CashDelete = 24;// 删除现金记账
	
	
	private static HashMap<Integer, String> requestTagMap = null;

	public static HashMap<Integer, String> getRequestTagMap() {
		if (null == requestTagMap) {	
			requestTagMap = new HashMap<Integer, String>();

			requestTagMap.put(Login, "http://211.147.87.24:8092/posm/199002.tran5");
			requestTagMap.put(Register, "http://211.147.87.24:8092/posm/199001.tran5");
			requestTagMap.put(ModifyLoginPwd, "http://211.147.87.24:8092/posm/199003.tran5");
			requestTagMap.put(ForgetLoginPwd, "http://211.147.87.24:8092/posm/199004.tran5");
			requestTagMap.put(SignIn, "http://211.147.87.23:8088/posp/199020.tran");
			requestTagMap.put(Consume, "http://211.147.87.23:8088/posp/199005.tran");
			requestTagMap.put(ConsumeCancel, "http://211.147.87.23:8088/posp/199006.tran");
			requestTagMap.put(BalanceQuery, "http://211.147.87.23:8088/posp/199007.tran");
			requestTagMap.put(FlowQuery, "http://211.147.87.23:8088/posp/199008.tran"); // 流水查询
			requestTagMap.put(CreditCardApply, "http://211.147.87.23:8080/posp/199010.tran");// 信用卡额度申请
			requestTagMap.put(ClearQuery, "http://211.147.87.23:8080/posp/199009.tran");
			requestTagMap.put(AppCommend, "http://211.147.87.24:8092/posm/199011.tran5");
			requestTagMap.put(ReferenceMsg, "http://211.147.87.24:8092/posm/199012.tran5");
			requestTagMap.put(ShareTransfer, "http://211.147.87.24:8092/posm/199015.tran5");
			requestTagMap.put(ExaminePhone, "http://211.147.87.24:8092/posm/199016.tran5");
			requestTagMap.put(CompareOldPwd, "http://211.147.87.24:8092/posm/199017.tran5");
			requestTagMap.put(SmsSend, "http://211.147.87.24:8092/posm/199018.tran5");
			requestTagMap.put(SmsCheck, "http://211.147.87.24:8092/posm/199019.tran5");
			requestTagMap.put(MerchantQuery, "http://211.147.87.23:8088/posp/199011.tran");
			requestTagMap.put(CashCharge, "http://192.168.4.115:8080/zfb/mpos/transProcess.do?operationId=addTransaction");
			requestTagMap.put(GetCashCharge, "http://192.168.4.115:8080/zfb/mpos/transProcess.do?operationId=getTransaction");
			requestTagMap.put(LoadUpHead, "http://59.49.20.154:8586/zfb/mpos/transProcess.do?operationId=setHeadImg");
			requestTagMap.put(GetDownLoadHead, "http://59.49.20.154:8586/zfb/mpos/transProcess.do?operationId=getHeadImg");
			requestTagMap.put(CashDelete, "http://59.49.20.154:8586/zfb/mpos/transProcess.do?operationId=delTransaInfo");
			
			// 192.168.4.115:8080  测试服务器地址
			//59.49.20.154:8586 外网
			
		}

		return requestTagMap;
	}

}
