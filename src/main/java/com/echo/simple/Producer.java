package com.echo.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

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
            //4.声明队列
            String queueName = "queue1";
            //声明队列时的五个参数
            /**
             * @params1:队列名称
             * @params2:是否要持久化 durable=false,所谓持久化消息就是队列持久化后，队列中的消息是否会存盘，
             * false：非持久化，true:持久化，
             * 非持久化队列会存盘吗？
             * 答案是：会，但是会随着rabbitMQ的重启而消失
             * @params3:排他性，是否是一个独占队列
             * @params4:最后一个消费者消费完消息之后，是否自动把队列删除
             * @params5:携带一些附加的参数
             */
            channel.queueDeclare(queueName,false,false,false,null);
            //5.准备消息内容
            String message = "Hello world";
            //6.发送消息给队列
            //@params1:交换机，@params2:队列名称，唯一标识，也是路由key
            //@params3: 消息的状态控制，是否持久化，@params4:消息主体
            //可以存在没有交换机的队列吗？
            //答：不可能的，虽然没有指定交换机，但肯定会存在一个默认的交换机
            channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));
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
