package com.genersoft.iot.vmp.gb28181.conf;

import com.genersoft.iot.vmp.gb28181.transmit.event.request.impl.message.notify.cmd.AlarmNotifyMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 获取sip默认配置
 * @author lin
 */
public class DefaultProperties {

    public static Properties getProperties(String name, boolean sipLog, boolean sipCacheServerConnections) {
        Properties properties = new Properties();
        properties.setProperty("javax.sip.STACK_NAME", name);
//        properties.setProperty("javax.sip.IP_ADDRESS", ip);
        // 关闭自动会话
        properties.setProperty("javax.sip.AUTOMATIC_DIALOG_SUPPORT", "off");

        /**
         * 完整配置参考 gov.nist.javax.sip.SipStackImpl，需要下载源码
         * gov/nist/javax/sip/SipStackImpl.class
         * sip消息的解析在 gov.nist.javax.sip.stack.UDPMessageChannel的processIncomingDataPacket方法
         */

        // 接收所有notify请求，即使没有订阅
        properties.setProperty("gov.nist.javax.sip.DELIVER_UNSOLICITED_NOTIFY", "true");
        properties.setProperty("gov.nist.javax.sip.AUTOMATIC_DIALOG_ERROR_HANDLING", "false");
        properties.setProperty("gov.nist.javax.sip.CANCEL_CLIENT_TRANSACTION_CHECKED", "true");
        // 为_NULL _对话框传递_终止的_事件
        properties.setProperty("gov.nist.javax.sip.DELIVER_TERMINATED_EVENT_FOR_NULL_DIALOG", "true");
        // 是否自动计算content length的实际长度，默认不计算
        properties.setProperty("gov.nist.javax.sip.COMPUTE_CONTENT_LENGTH_FROM_MESSAGE_BODY", "true");
        // 会话清理策略
        properties.setProperty("gov.nist.javax.sip.RELEASE_REFERENCES_STRATEGY", "Normal");
        // 处理由该服务器处理的基于底层TCP的保持生存超时
        properties.setProperty("gov.nist.javax.sip.RELIABLE_CONNECTION_KEEP_ALIVE_TIMEOUT", "60");
        // 获取实际内容长度，不使用header中的长度信息
        properties.setProperty("gov.nist.javax.sip.COMPUTE_CONTENT_LENGTH_FROM_MESSAGE_BODY", "true");
        // 线程可重入
        properties.setProperty("gov.nist.javax.sip.REENTRANT_LISTENER", "true");
        // 定义应用程序打算多久审计一次 SIP 堆栈，了解其内部线程的健康状况（该属性指定连续审计之间的时间（以毫秒为单位））
        properties.setProperty("gov.nist.javax.sip.THREAD_AUDIT_INTERVAL_IN_MILLISECS", "30000");

        // 部分设备会在短时间内发送大量注册， 导致协议栈内存溢出， 开启此项可以防止这部分设备注册， 避免服务崩溃，但是会降低系统性能， 描述如下
        // 默认值为 true。
        // 将此设置为 false 会使 Stack 在 Server Transaction 进入 TERMINATED 状态后关闭服务器套接字。
        // 这允许服务器防止客户端发起的基于 TCP 的拒绝服务攻击（即发起数百个客户端事务）。
        // 如果为 true（默认作），则堆栈将保持套接字打开，以便以牺牲线程和内存资源为代价来最大化性能 - 使自身容易受到 DOS 攻击。
        properties.setProperty("gov.nist.javax.sip.CACHE_SERVER_CONNECTIONS", String.valueOf(sipCacheServerConnections));

        properties.setProperty("gov.nist.javax.sip.MESSAGE_PROCESSOR_FACTORY", "gov.nist.javax.sip.stack.NioMessageProcessorFactory");

        /**
         * sip_server_log.log 和 sip_debug_log.log ERROR, INFO, WARNING, OFF, DEBUG, TRACE
         */
        Logger log = LoggerFactory.getLogger(AlarmNotifyMessageHandler.class);
        if (sipLog) {
            properties.setProperty("gov.nist.javax.sip.STACK_LOGGER", "com.genersoft.iot.vmp.gb28181.conf.StackLoggerImpl");
            properties.setProperty("gov.nist.javax.sip.SERVER_LOGGER", "com.genersoft.iot.vmp.gb28181.conf.ServerLoggerImpl");
            properties.setProperty("gov.nist.javax.sip.LOG_MESSAGE_CONTENT", "true");
            log.info("[SIP日志]已开启");
        }else {
            log.info("[SIP日志]已关闭");
        }
        return properties;
    }
}
