/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.siddhi.extension.io.grpc.utils;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.siddhi.extension.io.grpc.source.GrpcEventServiceServer;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

/**
 * Server interceptor to receive headers in Test Server
 */
public class TestServerInterceptor implements ServerInterceptor {
  private static final Logger logger = Logger.getLogger(TestServer.class.getName());

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                               Metadata metadata,
                                                               ServerCallHandler<ReqT, RespT> serverCallHandler) {
    Set<String> metadataKeys = metadata.keys();
    Map<String, String> metaDataMap = new HashMap<>();
    for (String key: metadataKeys) {
      metaDataMap.put(key, metadata.get(Metadata.Key.of(key, io.grpc.Metadata.ASCII_STRING_MARSHALLER)));
    }
    GrpcEventServiceServer.metaDataMap.set(metaDataMap);
    for (String key: metadataKeys) {
      if (logger.isDebugEnabled()) {
        logger.debug("Metadata received: " + key + ": " + metadata.get(Metadata.Key.of(key, ASCII_STRING_MARSHALLER)));
      }
    }
    return Contexts.interceptCall(Context.ROOT, serverCall, metadata, serverCallHandler);
  }
}
