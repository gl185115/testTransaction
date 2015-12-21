package ncr.res.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.websocket.model.RESMobileParam;
import ncr.res.websocket.model.RESMobileParamNewPOS;
import ncr.res.websocket.model.RESMobileParamOldPOS;

@ServerEndpoint(value = "/resSocket")
public class WebSocketServer {
	
	private static final String STR_SERVER = "server";
	private static final String STR_CLIENT = "client";
	private static final String STR_OLDPOS = "oldpos";
	private static final Logger LOGGER = (Logger) Logger.getInstance(); 
	private static Trace.Printer tp;
	private static Map<String, Session> sessionServerMap = new TreeMap<String, Session>();
	private static Map<String, List<Session>> sessionClientMap = new TreeMap<String, List<Session>>();
	
	public WebSocketServer() {
		tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), this.getClass());
	}
	
	@OnOpen
	public void onOpen(Session session) {
		tp.methodEnter("WebSocketServer.onOpen");
		
		if (tp == null) {
			tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
		}
		
		String deviceID = "";
		List<Session> sessionList = new ArrayList<Session>();
		Map<String, List<String>> uriParameterMap = null;
		
		try {
			Object requestUri = session.getRequestURI();
			uriParameterMap = session.getRequestParameterMap();
			
			if (requestUri == null || requestUri.toString().isEmpty()
					|| (!uriParameterMap.containsKey(STR_SERVER)
					& !uriParameterMap.containsKey(STR_CLIENT))) {
				LOGGER.logAlert(
						"WSktSvr",
						"WebSocketServer.onOpen",
						"99",
						"Query parameter is not valid. Please add query parameter \"server\" or \"client\".");
				tp.methodExit("WebSocketServer.onOpen");
				return;
			}
			
			if (uriParameterMap.containsKey(STR_SERVER) && 
					(!uriParameterMap.get(STR_SERVER).isEmpty())) {
				deviceID = uriParameterMap.get(STR_SERVER).get(0);
				
				// Check if device id is included in Old POS group.
				if (uriParameterMap.containsKey(STR_OLDPOS) && 
						(!uriParameterMap.get(STR_OLDPOS).isEmpty())) {
					if ("true".equals(uriParameterMap.get(STR_OLDPOS).get(0))) {	
						//do nothing
					}
				} else {
					// Check if device id is included in New POS group.
				}
				
				// One group one server.
				if (sessionServerMap.containsKey(deviceID)) {
					if (!sessionServerMap.get(deviceID).isOpen()) {
						sessionServerMap.get(deviceID).close();
					}
					sessionServerMap.remove(deviceID);
				}
				sessionServerMap.put(deviceID, session);
				
			} else if (uriParameterMap.containsKey(STR_CLIENT) && 
					(!uriParameterMap.get(STR_CLIENT).isEmpty())) {
				deviceID = uriParameterMap.get(STR_CLIENT).get(0);
				
				// Check the device is valid.
				
				if (sessionClientMap.containsKey(deviceID)) {
					sessionList.addAll(sessionClientMap.get(deviceID));
					sessionList.add(session);
					sessionClientMap.remove(deviceID);
				} else {
					sessionList.add(session);
				}
				sessionClientMap.put(deviceID, sessionList);
			}
		} catch (IOException e) {
			LOGGER.logAlert("WSktSvr", "WebSocketServer.onOpen", "99",
					"The session which will be closed is happened exception. ");
			tp.methodExit("WebSocketServer.onOpen");
			return;
		}
		
		tp.methodExit("WebSocketServer.onOpen");
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		tp.methodEnter("WebSocketServer.onMessage");
		
		String deviceID = "";
		Session ses = null;
		Iterator<Session> sessiones = null;
		Map<String, List<String>> uriParameterMap = session.getRequestParameterMap();

		if (uriParameterMap.containsKey(STR_SERVER) && 
				(!uriParameterMap.get(STR_SERVER).isEmpty())) {
			deviceID = uriParameterMap.get(STR_SERVER).get(0);
			
			if (sessionClientMap.containsKey(deviceID)) {
				sessiones = sessionClientMap.get(deviceID).iterator();
				while (sessiones.hasNext()) {
					ses = sessiones.next();
					ses.getAsyncRemote().sendText(message);
				}
			}
			
		} else if (uriParameterMap.containsKey(STR_CLIENT) && 
				(!uriParameterMap.get(STR_CLIENT).isEmpty())) {
			deviceID = uriParameterMap.get(STR_CLIENT).get(0);
			sessionServerMap.get(deviceID).getAsyncRemote().sendText(message);
		}
		
		tp.methodExit("WebSocketServer.onMessage");
	}
	
	@OnClose
	public void onClose(Session session) {
		tp.methodEnter("WebSocketServer.onClose");
		
		String deviceID = "";
		List<Session> sessionList = new ArrayList<Session>();
		List<Session> sessionListTemp = new ArrayList<Session>();
		Map<String, List<String>> uriParameterMap = null;

		try {
			uriParameterMap = session.getRequestParameterMap();

			if (uriParameterMap.containsKey(STR_SERVER)
					&& (!uriParameterMap.get(STR_SERVER).isEmpty())) {
				deviceID = uriParameterMap.get(STR_SERVER).get(0);

				if (sessionServerMap.containsKey(deviceID)) {
					if (!sessionServerMap.get(deviceID).isOpen()) {
						sessionServerMap.get(deviceID).close();
					}
					sessionServerMap.remove(deviceID);
				}

			} else if (uriParameterMap.containsKey(STR_CLIENT)
					&& (!uriParameterMap.get(STR_CLIENT).isEmpty())) {
				deviceID = uriParameterMap.get(STR_CLIENT).get(0);

				if (sessionClientMap.containsKey(deviceID)) {
					sessionList = sessionClientMap.get(deviceID);
					sessionListTemp.addAll(sessionList);

					for (Session ses : sessionList) {
						if (ses.getId().equals(session.getId())) {
							if (!ses.isOpen()) {
								ses.close();
							}
							sessionListTemp.remove(ses);
						}
					}
					sessionClientMap.remove(deviceID);
					sessionClientMap.put(deviceID, sessionListTemp);
				}
			}
		} catch (IOException e) {
			LOGGER.logAlert(
					"WSktSvr",
					"WebSocketServer.onClose",
					"99",
					"Query parameter is not valid. Please add query parameter \"server\" or \"client\".");
			tp.methodExit("WebSocketServer.onClose");
			return;
		}
		
		tp.methodExit("WebSocketServer.onClose");
	}
	
}
