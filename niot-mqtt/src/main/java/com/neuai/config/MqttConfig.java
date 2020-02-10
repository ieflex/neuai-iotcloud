package com.neuai.config;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.util.StringUtils;

/**
 * MQTT接收消息处理
 */
@Configuration
@IntegrationComponentScan
public class MqttConfig {
	@Value("${spring.mqtt.username:admin}") // 默认用户名是admin
	private String username;

	@Value("${spring.mqtt.password:password}") // 默认密码是password
	private String password;

	@Value("${spring.mqtt.url:tcp://127.0.0.1:61613}") // 本机的访问路径，采用tcp协议访问
	private String url;

	@Value("${spring.mqtt.sendClientId:mqttProducer}") // 发送者的客户端ID
	private String sendClientId;

	@Value("${spring.mqtt.sendDefaultTopic:topic1}") // 发送者发送的主题
	private String sendDefaultTopic;

	@Value("${spring.mqtt.receiveClientId:mqttConsumer}") // 接收者的客户端ID
	private String receiveClientId;

	@Value("${spring.mqtt.receiveDefaultTopic:topic1}") // 接收者订阅的主题，这里可以是多个
	private String receiveDefaultTopic;
	// 接收方的唯一id
	private String clientid = UUID.randomUUID().toString();
	// 接收等级
	private int Qos = 2;
	// 超时时间单位秒
	private int connectionTimeout = 10;
	// 心跳时间设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
	private int keepAliveInterval = 20;

	@Bean
	public MqttConnectOptions getMqttConnectOptions() {
		MqttConnectOptions options = new MqttConnectOptions();
		// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
		// 这里设置为true表示每次连接到服务器都以新的身份连接
		options.setCleanSession(true);
		// 设置连接的用户名
		options.setUserName(username);
		// 设置连接的密码
		options.setPassword(password.toCharArray());
		options.setServerURIs(StringUtils.split(url, ","));
		// 设置超时时间 单位为秒
		options.setConnectionTimeout(connectionTimeout);
		// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线，但这个方法并没有重连的机制
		options.setKeepAliveInterval(keepAliveInterval);
		// 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
		// options.setWill("willTopic", WILL_DATA, 2, false);
		return options;
	}

	/**
	 * MQTT客户端
	 *
	 * @return {@link org.springframework.integration.mqtt.core.MqttPahoClientFactory}
	 */
	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(getMqttConnectOptions());
		return factory;
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	/**
	 * MQTT消息处理器（发送者）
	 *
	 * @return {@link org.springframework.messaging.MessageHandler}
	 */
	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(sendClientId, mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(sendDefaultTopic);
		return messageHandler;
	}

	/**
	 * MQTT信息通道（接收者）
	 *
	 * @return {@link org.springframework.messaging.MessageChannel}
	 */
	@Bean
	public MessageChannel mqttInboundChannel() {
		return new DirectChannel();
	}

	/**
	 * MQTT消息订阅绑定（接收者）
	 *
	 * @return {@link org.springframework.integration.core.MessageProducer}
	 */
	@Bean
	public MessageProducer inbound() {
		// 可以同时接收（订阅）多个Topic,MqttPahoMessageDrivenChannelAdapter内部根据参数的不同重载了多个构造方法，因此可以灵活运用，可以订阅多个主题，经过实验往后面加就行了。
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(receiveClientId,
				mqttClientFactory(), receiveDefaultTopic);
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(Qos);
		// 设置订阅通道
		adapter.setOutputChannel(mqttInboundChannel());
		return adapter;
	}

	/**
	 * MQTT消息处理器（接收者）
	 *
	 * @return {@link org.springframework.messaging.MessageHandler}
	 */

	@Bean
	// ServiceActivator注解表明当前方法用于处理MQTT消息，inputChannel参数指定了用于接收消息信息的channel。
	@ServiceActivator(inputChannel = "mqttInboundChannel")
	public MessageHandler handleMessage() {
		return message -> {
			System.out.println(message.getPayload().toString());
			System.out.println(message.getHeaders().toString());
			String payload = message.getPayload().toString();
			String topic = message.getHeaders().get("mqtt_topic").toString();
			// 根据topic分别进行消息处理。
			if (topic.equals("topic1")) {
				System.out.println("topic1: 处理消息 " + payload);
			} else if (topic.equals("topic2")) {
				System.out.println("topic2: 处理消息 " + payload);
			} else {
				System.out.println(topic + ": 丢弃消息 " + payload);
			}
		};
	}
}
