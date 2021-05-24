package com.echo.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Consumer {
    public static void main(String[] args) {
        //1.创建连接工程
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.45.135");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;
        try{
            //2.创建Connection,名字叫做Producer
            connection = connectionFactory.newConnection("Producer");
            //3.通过连接获取通道channel
            channel = connection.createChannel();

            channel.basicConsume("queue1", true, new DeliverCallback() {
                @Override
                public void handle(String consumerTag, Delivery message) throws IOException {
                    System.out.println("收到的消息是" + new String(message.getBody(), "UTF-8"));
                }
            }, new CancelCallback() {
                @Override
                public void handle(String consumeTag) throws IOException {
                    System.out.println("接受消息失败...");
                }
            });
            System.out.println("开始接收消息");
            //在这里暂停一下，相当于getchar()
            //System.in.read();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //7.关闭通道
            if (channel != null && channel.isOpen()){
                try {
                    channel.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            //8.关闭连接
            if(connection != null && connection.isOpen()){
                try {
                    connection.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
