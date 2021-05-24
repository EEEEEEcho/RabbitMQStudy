package com.echo.all;

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
            //4.准备交换机名字
            String exchange = "direct-message-exchange";    //自己在代码中定义的
            //5.交换机类型
            String exchangeType = "direct";
            //6.在broker中声明交换机
            //第三个参数是交换机是否持久化，如果持久化 在broker关闭之后，该交换机也不会被移除
            channel.exchangeDeclare(exchange,exchangeType,true);
            //7.声明队列
            channel.queueDeclare("queue5",true,false,false,null);
            channel.queueDeclare("queue6",true,false,false,null);
            channel.queueDeclare("queue7",true,false,false,null);
            //8.绑定队列和交换机的关系,第三个参数是路由名字
            channel.queueBind("queue5",exchange,"order");
            channel.queueBind("queue6",exchange,"order");
            channel.queueBind("queue7",exchange,"course");

            //9.往队列里发送消息
            String messageOrder = "Hello direct All Order";
            String messageCourse = "Hello direct All Course";
            channel.basicPublish(exchange,"order",null,messageOrder.getBytes());
            channel.basicPublish(exchange,"course",null,messageCourse.getBytes());
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
