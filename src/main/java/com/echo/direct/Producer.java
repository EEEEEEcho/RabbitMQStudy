package com.echo.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static void main(String[] args) {
        //所有的中间价技术都是基于tcp/ip协议的，并在此协议上构建的。rabbitmq遵循的是amqp协议。
        //所以既然如此，必然会有ip和port
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
            //4.准备交换机
            String exchange = "direct-exchange";    //提前在页面中定义好的
            //5.路由key
            String routeKey = "email";
            String routeKey2 = "wechat";
            //6.交换机类型
            String type = "direct";
            //7.准备消息内容
            String message = "Hello world direct";
            // 8: 发送消息给中间件rabbitmq-server
            // @params1: 交换机exchange
            // @params2: 队列名称/routingkey
            // @params3: 属性配置
            // @params4: 发送消息的内容

            /**
             * 为什么没有指定交换机exchange与queue的绑定关系呢？
             * 是因为在上述课程中，在web页面中已经将direct-exchange与queue1,queue2,queue3绑定了。
             * 所以在此处不需要绑定了
             */


            channel.basicPublish(exchange, routeKey, null, message.getBytes());
            channel.basicPublish(exchange,routeKey2,null,message.getBytes());
            System.out.println("消息发送成功");
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
