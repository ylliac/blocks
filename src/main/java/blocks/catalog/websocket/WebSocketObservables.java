package blocks.catalog.websocket;

import rx.subjects.PublishSubject;

public class WebSocketObservables {

	public static PublishSubject<String> fromWebSocket(int port){
		
		//Exemple client et serveur JS (avec graphique html)
		https://github.com/zehome/rt-websocket-graph/tree/master/server
		
		//Exemple server Java
		https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ChatServer.java
		
		//Exemple webserver en RxJS
		https://github.com/Reactive-Extensions/RxJS/issues/112
		
		var ws = new WebSocket(address, protocol);

		  var observer = Rx.Observer.create(function (data) {
		    if (ws.readyState === WebSocket.OPEN) { ws.send(data); }
		  });

		  // Handle the data
		  var observable = Rx.Observable.create (function (obs) {
		    // Handle open
		    if (openObserver) {
		      ws.onopen = function (e) {
		        openObserver.onNext(e);
		        openObserver.onCompleted();
		      };
		    }

		    // Handle messages  
		    ws.onmessage = obs.onNext.bind(obs);
		    ws.onerror = obs.onError.bind(obs);
		    ws.onclose = obs.onCompleted.bind(obs);

		    // Return way to unsubscribe
		    return ws.close.bind(ws);
		  });

		  return Rx.Subject.create(observer, observable);
		
	}
	
}
