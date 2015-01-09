/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package examples;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.eventbus.MessageConsumer;

/**
 * Created by tim on 09/01/15.
 */
public class EventBusExamples {

  public void example1(Vertx vertx) {
    EventBus eb = vertx.eventBus();

    eb.consumer("news.uk.sport", message -> {
      System.out.println("I have received a message: " + message.body());
    });
  }

  public void example2(Vertx vertx) {
    EventBus eb = vertx.eventBus();

    MessageConsumer<String> consumer = eb.consumer("news.uk.sport");
    consumer.handler(message -> {
      System.out.println("I have received a message: " + message.body());
    });
  }

  public void example3(MessageConsumer<String> consumer) {
    consumer.completionHandler(res -> {
      if (res.succeeded()) {
        System.out.println("The handler registration has reached all nodes");
      } else {
        System.out.println("Registration failed!");
      }
    });
  }

  public void example4(MessageConsumer<String> consumer) {
    consumer.unregister(res -> {
      if (res.succeeded()) {
        System.out.println("The handler un-registration has reached all nodes");
      } else {
        System.out.println("Un-registration failed!");
      }
    });
  }

  public void example5(EventBus eventBus) {
    eventBus.publish("news.uk.sport", "Yay! Someone kicked a ball");
  }

  public void example6(EventBus eventBus) {
    eventBus.send("news.uk.sport", "Yay! Someone kicked a ball");
  }

  public void example7(EventBus eventBus) {
    DeliveryOptions options = new DeliveryOptions();
    options.addHeader("some-header", "some-value");
    eventBus.send("news.uk.sport", "Yay! Someone kicked a ball");
  }

  public void example8(EventBus eventBus) {
    MessageConsumer<String> consumer = eventBus.consumer("news.uk.sport");
    consumer.handler(message -> {
      System.out.println("I have received a message: " + message.body());
      message.reply("how interesting!");
    });
  }

  public void example9(EventBus eventBus) {
    eventBus.send("news.uk.sport", "Yay! Someone kicked a ball across a patch of grass", ar -> {
      if (ar.succeeded()) {
        System.out.println("Received reply: " + ar.result().body());
      }
    });
  }

  public void example10(EventBus eventBus, MessageCodec myCodec) {

    eventBus.registerCodec(myCodec);

    DeliveryOptions options = new DeliveryOptions().setCodecName(myCodec.name());

    eventBus.send("orders", new MyPOJO(), options);
  }

  public void example11(EventBus eventBus, MessageCodec myCodec) {

    eventBus.registerDefaultCodec(MyPOJO.class, myCodec);

    eventBus.send("orders", new MyPOJO());
  }

  class MyPOJO {

  }


}
