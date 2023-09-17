package com.zhuang.kill.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface RabbitmqProducer {
    void publishMessagesAsync(String messages[]) throws IOException, TimeoutException;
    void publishMessageAsync(String message) throws IOException, TimeoutException;
}
