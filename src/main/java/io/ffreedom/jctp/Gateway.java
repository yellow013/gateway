package io.ffreedom.jctp;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;

import ctp.thostapi.CThostFtdcDepthMarketDataField;
import ctp.thostapi.CThostFtdcInputOrderActionField;
import ctp.thostapi.CThostFtdcInputOrderField;
import ctp.thostapi.CThostFtdcMdApi;
import ctp.thostapi.CThostFtdcOrderField;
import ctp.thostapi.CThostFtdcQryInstrumentField;
import ctp.thostapi.CThostFtdcQryInvestorPositionField;
import ctp.thostapi.CThostFtdcQrySettlementInfoField;
import ctp.thostapi.CThostFtdcQryTradingAccountField;
import ctp.thostapi.CThostFtdcReqUserLoginField;
import ctp.thostapi.CThostFtdcRspUserLoginField;
import ctp.thostapi.CThostFtdcSpecificInstrumentField;
import ctp.thostapi.CThostFtdcTradeField;
import ctp.thostapi.CThostFtdcTraderApi;
import ctp.thostapi.CThostFtdcTradingAccountField;
import ctp.thostapi.THOST_TE_RESUME_TYPE;
import io.ffreedom.common.collect.ECollections;
import io.ffreedom.common.datetime.DateTimeUtil;
import io.ffreedom.common.log.CommonLoggerFactory;
import io.ffreedom.common.queue.api.Queue;
import io.ffreedom.common.queue.impl.ArrayBlockingMPSCQueue;
import io.ffreedom.common.utils.ThreadUtil;
import io.ffreedom.jctp.bean.CtpUserInfo;
import io.ffreedom.jctp.bean.rsp.RspMsg;

public class Gateway {

	private static final Logger logger = CommonLoggerFactory.getLogger(Gateway.class);

	private static void loadWin64Library() {
		logger.info("Load win 64bit library...");
		System.loadLibrary("lib/win64/thosttraderapi");
		System.loadLibrary("lib/win64/thostmduserapi");
		System.loadLibrary("lib/win64/thostapi_wrap");
	}

	private static void loadLinux64Library() {
		logger.info("Load linux 64bit library...");
		System.loadLibrary("lib/linux64/thosttraderapi");
		System.loadLibrary("lib/linux64/thostmduserapi");
		System.loadLibrary("lib/linux64/thostapi_wrap");
	}

	static {
		try {
			// 根据操作系统选择加载不同库文件
			if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
				loadWin64Library();
			else
				loadLinux64Library();
			logger.info("Load libs success...");
		} catch (Throwable e) {
			logger.error("Load libs error...", e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private String gatewayId;
	private CtpUserInfo userInfo;
	private CThostFtdcTraderApi traderApi;
	private CThostFtdcMdApi mdApi;

	public volatile boolean isInit = false;
	private Queue<RspMsg> inboundQueue;

	private int mdRequestId = -1;
	private int traderRequestId = -1;

	private boolean isMdLogin;
	private boolean isTraderLogin;

	public Gateway(String gatewayId, CtpUserInfo userInfo, Queue<RspMsg> inboundQueue) {
		this.gatewayId = gatewayId;
		this.userInfo = userInfo;
		this.inboundQueue = inboundQueue;
	}

	private File getTempDir() {
		// 创建临时文件存储目录
		String tempFileHome = System.getProperty("user.home") + File.separator + "jctp";
		File tempFileDir = new File(
				tempFileHome + File.separator + DateTimeUtil.getCurrentDate() + File.separator + gatewayId);
		if (!tempFileDir.exists())
			tempFileDir.mkdirs();
		return tempFileDir;
	}

	public void initAndJoin() {
		if (!isInit) {
			// 获取临时文件目录
			File tempDir = getTempDir();

			// 指定trader临时文件地址
			String traderTempFilePath = new File(tempDir, "trader").getAbsolutePath();
			logger.info("{} trader use temp file path : {}", gatewayId, traderTempFilePath);
			// 创建traderApi
			this.traderApi = CThostFtdcTraderApi.CreateFtdcTraderApi(traderTempFilePath);
			// 创建traderSpi
			TraderSpiImpl traderSpiImpl = new TraderSpiImpl(this);
			// 将traderSpi注册到traderApi
			traderApi.RegisterSpi(traderSpiImpl);
			// 注册到trader前置机
			traderApi.RegisterFront(userInfo.getTradeAddress());
			// 订阅公有流
			traderApi.SubscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
			// 订阅私有流
			traderApi.SubscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
			// 初始化traderApi
			traderApi.Init();
			logger.info("Call traderApi.Init()...");
			ThreadUtil.sleep(3000);

			// 指定md临时文件地址
			String mdTempFilePath = new File(tempDir, "md").getAbsolutePath();
			logger.info("{} md use temp file path : {}", gatewayId, mdTempFilePath);
			// 创建mdApi
			this.mdApi = CThostFtdcMdApi.CreateFtdcMdApi(mdTempFilePath);
			// 创建mdSpi
			MdSpiImpl mdSpiImpl = new MdSpiImpl(this);
			// 将mdSpi注册到mdApi
			mdApi.RegisterSpi(mdSpiImpl);
			// 注册到md前置机
			mdApi.RegisterFront(userInfo.getMdAddress());
			// 初始化mdApi
			mdApi.Init();
			logger.info("Call mdApi.Init()...");
			ThreadUtil.sleep(3000);

			this.isInit = true;
			// 阻塞当前线程
			logger.info("Current thread join...");
			ThreadUtil.join(Thread.currentThread());
		}
	}

	private Set<String> subscribeInstruementSet = ECollections.newUnifiedSet();

	public void subscribeMarketData(Set<String> inputInstruementSet) {
		subscribeInstruementSet.addAll(inputInstruementSet);
		logger.info("Add Subscribe Instruement set -> addCount==[{}]", inputInstruementSet.size());
		if (isMdLogin && !subscribeInstruementSet.isEmpty()) {
			String[] instruementIdList = new String[subscribeInstruementSet.size()];
			Iterator<String> iterator = subscribeInstruementSet.iterator();
			for (int i = 0; i < instruementIdList.length; i++) {
				instruementIdList[i] = iterator.next();
				logger.info("Add Subscribe Instruement -> instruementCode==[{}]", instruementIdList[i]);
			}
			mdApi.SubscribeMarketData(instruementIdList, instruementIdList.length);
			subscribeInstruementSet.clear();
			logger.info("Send SubscribeMarketData -> count==[{}]", instruementIdList.length);
		} else
			logger.info("Cannot SubscribeMarketData -> isMdLogin==[{}] subscribeInstruementSet.isEmpty==[{}]",
					isMdLogin, subscribeInstruementSet.isEmpty());
	}

	public void newOrder(CThostFtdcInputOrderField inputOrder) {
		traderApi.ReqOrderInsert(inputOrder, ++traderRequestId);
	}

	public void cancelOrder(CThostFtdcInputOrderActionField inputOrderAction) {
		traderApi.ReqOrderAction(inputOrderAction, ++traderRequestId);
	}

	public void qureyAccount() {
		CThostFtdcQryTradingAccountField qryTradingAccount = new CThostFtdcQryTradingAccountField();
		qryTradingAccount.setBrokerID(userInfo.getBrokerId());
		qryTradingAccount.setInvestorID(userInfo.getInvestorId());
		qryTradingAccount.setCurrencyID(userInfo.getCurrencyId());
		traderApi.ReqQryTradingAccount(qryTradingAccount, ++traderRequestId);
		logger.info("ReqQryTradingAccount OK");
	}

	public void qureyPosition() {
		CThostFtdcQryInvestorPositionField qryInvestorPosition = new CThostFtdcQryInvestorPositionField();
		qryInvestorPosition.setBrokerID(userInfo.getBrokerId());
		qryInvestorPosition.setInvestorID(userInfo.getInvestorId());
		traderApi.ReqQryInvestorPosition(qryInvestorPosition, ++traderRequestId);
	}

	void onMdFrontConnected() {
		CThostFtdcReqUserLoginField reqUserLogin = new CThostFtdcReqUserLoginField();
		reqUserLogin.setBrokerID(userInfo.getBrokerId());
		reqUserLogin.setUserID(userInfo.getUserId());
		reqUserLogin.setPassword(userInfo.getPassword());
		mdApi.ReqUserLogin(reqUserLogin, ++mdRequestId);
		logger.info("Send Md ReqUserLogin OK");
	}

	void onMdRspUserLogin(CThostFtdcRspUserLoginField rspUserLogin) {
		logger.info("Md UserLogin Success -> Brokerid==[{}] UserID==[{}]", rspUserLogin.getBrokerID(),
				rspUserLogin.getUserID());
		this.isMdLogin = true;
		this.subscribeMarketData(new HashSet<>());
	}

	void onRspSubMarketData(CThostFtdcSpecificInstrumentField specificInstrument) {
		logger.info("SubscribeMarketData Success -> InstrumentCode==[{}]", specificInstrument);
	}

	void onRtnDepthMarketData(CThostFtdcDepthMarketDataField depthMarketData) {
		inboundQueue.enqueue(RspMsg.ofDepthMarketData(depthMarketData));
	}

	void onTraderFrontConnected() {
		CThostFtdcReqUserLoginField reqUserLogin = new CThostFtdcReqUserLoginField();
		reqUserLogin.setBrokerID(userInfo.getBrokerId());
		reqUserLogin.setUserID(userInfo.getUserId());
		reqUserLogin.setPassword(userInfo.getPassword());
		reqUserLogin.setUserProductInfo(userInfo.getUserProductInfo());
		traderApi.ReqUserLogin(reqUserLogin, ++traderRequestId);
		logger.info("Send Trader ReqUserLogin OK");
	}

	void onTraderRspUserLogin(CThostFtdcRspUserLoginField rspUserLogin) {
		logger.info("Trader UserLogin Success -> Brokerid==[{}] UserID==[{}]", rspUserLogin.getBrokerID(),
				rspUserLogin.getUserID());
		this.isTraderLogin = true;
		qureyAccount();
		qureyPosition();

		CThostFtdcQrySettlementInfoField qrySettlementInfo = new CThostFtdcQrySettlementInfoField();
		qrySettlementInfo.setBrokerID(userInfo.getBrokerId());
		qrySettlementInfo.setInvestorID(userInfo.getInvestorId());
		qrySettlementInfo.setTradingDay(userInfo.getTradingDay());
		qrySettlementInfo.setAccountID(userInfo.getAccountId());
		qrySettlementInfo.setCurrencyID(userInfo.getCurrencyId());
		traderApi.ReqQrySettlementInfo(qrySettlementInfo, ++traderRequestId);
		logger.info("ReqQrySettlementInfo OK");

		CThostFtdcQryInstrumentField qryInstrument = new CThostFtdcQryInstrumentField();
		traderApi.ReqQryInstrument(qryInstrument, ++traderRequestId);
		logger.info("ReqQryInstrument OK");
	}

	void onQryTradingAccount(CThostFtdcTradingAccountField tradingAccount) {
		logger.info("OnRspQryTradingAccount -> Balance==[{}] Available==[{}] WithdrawQuota==[{}] Credit==[{}]",
				tradingAccount.getBalance(), tradingAccount.getAvailable(), tradingAccount.getWithdrawQuota(),
				tradingAccount.getCredit());
	}

	void onRtnOrder(CThostFtdcOrderField order) {
		inboundQueue.enqueue(RspMsg.ofRtnOrder(order));
	}

	void onRtnTrade(CThostFtdcTradeField trade) {
		inboundQueue.enqueue(RspMsg.ofRtnTrade(trade));
	}

	/**
	 * TEST MAIN
	 */

	private static String TradeAddress = "tcp://180.168.146.187:10000";
	private static String MdAddress = "tcp://180.168.146.187:10010";

	private static String BrokerId = "9999";
	private static String InvestorId = "005853";
	private static String UserId = "005853";
	private static String AccountId = "005853";
	private static String Password = "jinpengpass101";

	private static String TradingDay = "20190201";
	private static String CurrencyId = "CNY";

	private static String GatewayId = "simnow_test";

	public static void main(String[] args) {

		CtpUserInfo simnowUserInfo = CtpUserInfo.newEmpty().setTradeAddress(TradeAddress).setMdAddress(MdAddress)
				.setBrokerId(BrokerId).setInvestorId(InvestorId).setUserId(UserId).setAccountId(AccountId)
				.setPassword(Password).setTradingDay(TradingDay).setCurrencyId(CurrencyId);

		Gateway gateway = new Gateway(GatewayId, simnowUserInfo,
				ArrayBlockingMPSCQueue.autoRunQueue("Simnow-Handle-Queue", 1024, msg -> {
					switch (msg.getType()) {
					case DepthMarketData:
						CThostFtdcDepthMarketDataField depthMarketData = msg.getDepthMarketData();
						logger.info(
								"Handle CThostFtdcDepthMarketDataField -> InstrumentID==[{}] UpdateMillisec==[{}] UpdateTime==[{}] AskPrice1==[{}] BidPrice1==[{}]",
								depthMarketData.getInstrumentID(), depthMarketData.getUpdateMillisec(),
								depthMarketData.getUpdateTime(), depthMarketData.getAskPrice1(),
								depthMarketData.getBidPrice1());
						break;
					case RtnOrder:
						CThostFtdcOrderField order = msg.getRtnOrder();
						logger.info("Handle Order -> OrderRef==[{}]", order.getOrderRef());
						break;
					case RtnTrade:
						CThostFtdcTradeField trade = msg.getRtnTrade();
						logger.info("Handle Order -> OrderRef==[{}]", trade.getOrderRef());
						break;
					default:
						break;
					}
				}));
		ThreadUtil.startNewThread(() -> gateway.initAndJoin(), "Gateway-Thread");

		Set<String> instruementIdSet = new HashSet<>();
		instruementIdSet.add("rb1910");
		gateway.subscribeMarketData(instruementIdSet);
	}

}