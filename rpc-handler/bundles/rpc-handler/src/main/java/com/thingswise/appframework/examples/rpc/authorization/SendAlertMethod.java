package com.thingswise.appframework.examples.rpc.authorization;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.osgi.service.component.annotations.Component;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.Gson;
import com.thingswise.appframework.services.rpc.RPCHandlerException;
import com.thingswise.appframework.services.rpc.RPCMethodHandler;

public class SendAlertMethod implements RPCMethodHandler {

	private final String scope;
	
	private final String project;
	
	private static ThreadLocal<Gson> gson = new ThreadLocal<Gson>() {
		@Override
		protected Gson initialValue() {
			return new Gson();
		}		
	};
	
	private static Gson getGson() {
		return gson.get();
	}
	
	public SendAlertMethod(String scope, String project) {
		this.scope = scope;
		this.project = project;
	}

	@Override
	public ListenableFuture<List<Object>> handle(List<Object> request) {
		if (request.size() != 1) {
			return Futures.immediateFailedFuture(new RPCHandlerException("example:invalidSyntax", "Invalid input request"));
		}
		
		URI backendUri = this.backendUri;
		
		if (backendUri == null) {
			return Futures.immediateFailedFuture(new RPCHandlerException(RPCHandlerException.INTERNAL_ERROR, "Backend not configured"));
		}
		
		HttpPost post = new HttpPost();
		post.setEntity(new StringEntity(getGson().toJson(request.get(0)), "application/json"));
		post.setURI(backendUri);
		
		final SettableFuture<List<Object>> result = SettableFuture.create();
		final Future<HttpResponse> invocation = HttpAsyncClients.createDefault().execute(post, new FutureCallback<HttpResponse>() {
			@Override
			public void cancelled() {
				
			}
			@Override
			public void completed(HttpResponse resp) {
				if (resp.getStatusLine().getStatusCode() != 200) {
					result.setException(new RPCHandlerException("example:backendError", String.format("Backend repoied with error: %s", resp.getStatusLine())));
				} else {
					result.set(Collections.emptyList());
				}
			}
			@Override
			public void failed(Exception err) {
				result.setException(new RPCHandlerException("example:backendInvocationError", "Backend invocation error", err));
			}			
		});
		
		result.addListener(new Runnable() {
			@Override
			public void run() {
				if (result.isCancelled()) {
					// if cancelled then cleanup
					invocation.cancel(true);
				}
			}			
		}, MoreExecutors.sameThreadExecutor());
		
		return result;
	}
	
	private URI backendUri;

	@Override
	public void configure(Map<String, ?> properties) throws Exception {
		String backendUri = (String) properties.get("backend.uri");
		if (backendUri != null) {
			this.backendUri = new URI(backendUri);
		}
	}

}
