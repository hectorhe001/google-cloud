/*
 * Copyright © 2018 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.cdap.plugin.gcp.publisher.source;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.pubsub.v1.ReceivedMessage;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.dstream.ReceiverInputDStream;
import org.apache.spark.streaming.receiver.Receiver;

import javax.annotation.Nullable;

/**
 * Input Stream used to subscribe to a Pub/Sub topic and pull messages.
 */
public class PubSubInputDStream extends ReceiverInputDStream<ReceivedMessage> {

  protected String project;
  protected String topic;
  protected String subscription;
  protected ServiceAccountCredentials credentials;
  protected StorageLevel storageLevel;
  protected boolean autoAcknowledge;

  /**
   * Constructor Method
   *
   * @param streamingContext Spark Streaming Context
   * @param project          Project Name
   * @param topic            Topic Name
   * @param subscription     Subscription Name
   * @param credentials      Google Cloud credentials
   * @param storageLevel     Spark Storage Level for received messages
   * @param autoAcknowledge  Acknowledge messages
   */
  PubSubInputDStream(StreamingContext streamingContext, String project, @Nullable String topic,
                     String subscription, ServiceAccountCredentials credentials, StorageLevel storageLevel,
                     boolean autoAcknowledge) {
    super(streamingContext, scala.reflect.ClassTag$.MODULE$.apply(ReceivedMessage.class));
    this.project = project;
    this.topic = topic;
    this.subscription = subscription;
    this.credentials = credentials;
    this.storageLevel = storageLevel;
    this.autoAcknowledge = autoAcknowledge;
  }



  @Override
  public Receiver<ReceivedMessage> getReceiver() {
    return new PubSubReceiver(this.project,
                              this.topic,
                              this.subscription,
                              this.credentials,
                              this.autoAcknowledge,
                              this.storageLevel);
  }
}